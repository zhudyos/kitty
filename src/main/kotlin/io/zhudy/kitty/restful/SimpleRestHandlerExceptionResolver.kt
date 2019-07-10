package io.zhudy.kitty.restful

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.biz.PubBizCodes
import org.apache.logging.log4j.LogManager
import org.springframework.core.NestedExceptionUtils
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.toMessage
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
open class SimpleRestHandlerExceptionResolver(private val objectMapper: ObjectMapper) : HandlerExceptionResolver {

    private val log = LogManager.getLogger()

    override fun resolveException(request: HttpServletRequest, response: HttpServletResponse, handler: Any?,
                                  ex: Exception): ModelAndView? {
        val er = doResolveException(ex) ?: resolveException(request, NestedExceptionUtils.getMostSpecificCause(ex))

        response.characterEncoding = "UTF-8"
        response.setHeader("cache-control", "no-store")
        response.setHeader("content-type", "application/json")
        response.status = er.status

        try {
            val writer = response.writer
            objectMapper.writeValue(writer, er)
            writer.flush()
        } catch (e: IOException) {
            // ignore
        }
        return ModelAndView()
    }

    /**
     *
     */
    protected fun doResolveException(ex: Exception): RestError? {
        return null
    }

    private fun resolveException(request: HttpServletRequest, e: Throwable): RestError {
        val path = request.requestURI
        return when (e) {
            is MissingKotlinParameterException -> {
                RestError(
                        status = 400,
                        path = path,
                        code = PubBizCodes.C_999.code,
                        message = "缺少参数 ${e.parameter.name}"
                )
            }
            is ConstraintViolationException -> {
                RestError(
                        status = 400,
                        path = path,
                        code = PubBizCodes.C_999.code,
                        message = "参数校验错误",
                        details = e.constraintViolations.asSequence().map { it.toMessage() }.toSet()
                )
            }
            is BizCodeException -> {
                RestError(
                        status = e.bizCode.status,
                        path = path,
                        code = e.bizCode.code,
                        message = e.message
                )
            }
            is HttpMediaTypeNotSupportedException -> {
                RestError(
                        status = 415,
                        path = path,
                        code = 415,
                        message = e.message ?: "不支持的 content-type"
                )
            }
            else -> {
                val traceId = request.getHeader("x-request-id") ?: UUID.randomUUID().toString().replace("-", "")
                log.error("traceId: {}, path: {}, queryParams: {}", traceId, path, request.queryString, e)
                RestError(
                        status = 500,
                        path = path,
                        code = 500,
                        message = e.message ?: e.javaClass.canonicalName,
                        traceId = traceId
                )
            }
        }
    }

}