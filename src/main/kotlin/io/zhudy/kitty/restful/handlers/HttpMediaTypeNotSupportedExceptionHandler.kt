package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.HttpMediaTypeNotSupportedException

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Ordered.LOWEST_PRECEDENCE)
class HttpMediaTypeNotSupportedExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("org.springframework.web.HttpMediaTypeNotSupportedException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as HttpMediaTypeNotSupportedException
            return RestError(status = 415, code = 415, message = e.message ?: "不支持的 content-type: ${e.contentType}")
        }
        return null
    }
}