package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
class BizCodeExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (ex is BizCodeException) {
            return RestError(
                    status = ex.bizCode.status,
                    code = ex.bizCode.code,
                    message = ex.message
            )
        }
        return null
    }
}