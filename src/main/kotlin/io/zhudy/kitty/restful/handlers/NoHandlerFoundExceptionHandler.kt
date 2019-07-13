package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.servlet.NoHandlerFoundException

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Ordered.LOWEST_PRECEDENCE)
class NoHandlerFoundExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("org.springframework.web.servlet.NoHandlerFoundException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as NoHandlerFoundException
            return RestError(status = 404, code = 404, message = "未发现对应的资源")
        }
        return null
    }
}