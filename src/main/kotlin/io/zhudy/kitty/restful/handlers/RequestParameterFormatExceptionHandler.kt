package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import io.zhudy.kitty.web.RequestParameterFormatException
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
class RequestParameterFormatExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("io.zhudy.kitty.web.RequestParameterFormatException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as RequestParameterFormatException
            return RestError(
                    status = 400,
                    code = PubBizCodes.C_999.code,
                    message = "在 \"${e.where}\" 的参数 \"${e.parameter}\"：${e.msg}"
            )
        }
        return null
    }
}