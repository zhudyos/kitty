package io.zhudy.kitty.restful

import io.zhudy.kitty.restful.handlers.*
import io.zhudy.kitty.util.TracingUtils
import io.zhudy.kitty.web.mvc.TRACE_ID
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.Ordered
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import org.springframework.core.annotation.Order
import org.springframework.http.InvalidMediaTypeException
import org.springframework.http.MediaType
import org.springframework.http.converter.GenericHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.cors.CorsUtils
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.streams.toList


/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class RestHandlerExceptionResolver(private val messageConverters: List<HttpMessageConverter<*>>) :
        HandlerExceptionResolver, ApplicationContextAware, InitializingBean {

    companion object {
        private val log = LogManager.getLogger(RestHandlerExceptionResolver::class.java)
    }

    private val handlers = arrayListOf<RestExceptionHandler>()
    private var applicationContext: ApplicationContext? = null

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun afterPropertiesSet() {
        handlers.clear()

        // 内置的异常处理器
        handlers.add(BizCodeExceptionHandler())
        handlers.add(ConstraintViolationExceptionHandler())
        handlers.add(MissingKotlinParameterExceptionHandler())
        handlers.add(MissingRequestParameterExceptionHandler())
        handlers.add(RequestParameterFormatExceptionHandler())
        handlers.add(HttpMediaTypeNotSupportedExceptionHandler())
        handlers.add(NoHandlerFoundExceptionHandler())

        // 外部扩展的异常处理器
        val extHandlers = applicationContext?.run { getBeanProvider(RestExceptionHandler::class.java).stream().toList() }
                ?: emptyList()
        handlers.addAll(extHandlers)
        AnnotationAwareOrderComparator.sort(handlers)
    }

    override fun resolveException(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, ex: Exception): ModelAndView? {
        // 不处理跨域请求校验
        if (CorsUtils.isPreFlightRequest(request)) {
            return null
        }
        response.setHeader("cache-control", "no-store")

        try {
            var rs: RestError? = null
            for (h in handlers) {
                rs = h.handleException(ex)
                if (rs != null) {
                    break
                }
            }

            if (rs == null) {
                rs = serverError(request, ex)
            }

            rs.path = request.requestURI
            rs.method = request.method
            if (request.getParameter("debug") != null) {
                rs.stacktrace = stacktrace(ex)
            }

            response.status = rs.status
            writeEntityWithMessageConverters(rs, response)
        } catch (throwable: Throwable) {
            response.status = 500

            val traceId = traceId(request)
            log.error("[{}] 服务器解决异常出错", traceId, throwable)
        }
        return ModelAndView()
    }

    private fun traceId(request: HttpServletRequest): String {
        return request.getAttribute(TRACE_ID)?.toString() ?: request.getHeader("x-request-id") ?: TracingUtils.traceId()
    }

    private fun serverError(request: HttpServletRequest, ex: Exception): RestError {
        val traceId = traceId(request)
        log.error("[{}] 服务器内部错误", traceId, ex)
        return RestError(
                traceId = traceId,
                status = 500,
                code = 500,
                message = ex.message ?: "服务器内部错误"
        )
    }

    private fun stacktrace(ex: Exception): List<String> {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        ex.printStackTrace(pw)
        return sw.buffer.lines()
    }

    @Throws(ServletException::class, IOException::class)
    private fun writeEntityWithMessageConverters(entity: Any, response: HttpServletResponse) {
        val serverResponse = ServletServerHttpResponse(response)
        val contentType = getContentType(response)
        val entityClass = entity.javaClass

        for (messageConverter in messageConverters) {
            if (messageConverter is GenericHttpMessageConverter<*>) {
                val genericMessageConverter = messageConverter as GenericHttpMessageConverter<Any>
                if (genericMessageConverter.canWrite(entityClass, entityClass, contentType)) {
                    genericMessageConverter.write(entity, entityClass, contentType, serverResponse)
                    return
                }
            }
            if (messageConverter.canWrite(entityClass, contentType)) {
                (messageConverter as HttpMessageConverter<Any>).write(entity, contentType, serverResponse)
                return
            }
        }
    }

    private fun getContentType(response: HttpServletResponse): MediaType? {
        return try {
            MediaType.parseMediaType(response.contentType).removeQualityValue()
        } catch (ex: InvalidMediaTypeException) {
            null
        }
    }

}