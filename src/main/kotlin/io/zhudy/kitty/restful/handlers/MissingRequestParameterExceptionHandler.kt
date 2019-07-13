package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import io.zhudy.kitty.web.MissingRequestParameterException
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.annotation.Order

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Int.MIN_VALUE + 1000)
class MissingRequestParameterExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("io.zhudy.kitty.web.MissingRequestParameterException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as MissingRequestParameterException
            return RestError(
                    status = 400,
                    code = PubBizCodes.C_999.code,
                    message = "在 \"${e.where}\" 的参数 \"${e.parameter}\" 不存在或为空"
            )
        }
        return null
    }
}