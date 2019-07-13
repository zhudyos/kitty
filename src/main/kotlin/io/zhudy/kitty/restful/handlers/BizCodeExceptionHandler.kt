package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.annotation.Order

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Int.MIN_VALUE + 1000)
class BizCodeExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("io.zhudy.kitty.biz.BizCodeException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as BizCodeException
            return RestError(
                    status = e.bizCode.status,
                    code = e.bizCode.code,
                    message = e.message
            )
        }
        return null
    }
}