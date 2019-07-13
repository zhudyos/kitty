package io.zhudy.kitty.restful

import io.zhudy.kitty.restful.handlers.*
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import kotlin.streams.toList

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
class RestExceptionResolver : ApplicationContextAware, InitializingBean {

    private val handlers = arrayListOf<RestExceptionHandler>()
    private var applicationContext: ApplicationContext? = null

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun afterPropertiesSet() {
        handlers.clear()

        handlers.add(BizCodeExceptionHandler())
        handlers.add(ConstraintViolationExceptionHandler())
        handlers.add(HttpMediaTypeNotSupportedExceptionHandler())
        handlers.add(MissingKotlinParameterExceptionHandler())
        handlers.add(MissingRequestParameterExceptionHandler())
        handlers.add(RequestParameterFormatExceptionHandler())

        // 获取 spring 上下文中的 RestExceptionHandler
        val extHandlers = applicationContext?.run { getBeanProvider(RestExceptionHandler::class.java).stream().toList() }
                ?: emptyList()
        handlers.addAll(extHandlers)
        AnnotationAwareOrderComparator.sort(handlers)
    }

    /**
     * 解决异常。
     */
    fun resolve(ex: Exception): RestError? {
        var rs: RestError? = null
        for (h in handlers) {
            rs = h.handleException(ex)
            if (rs != null) {
                return rs
            }
        }
        return rs
    }
}