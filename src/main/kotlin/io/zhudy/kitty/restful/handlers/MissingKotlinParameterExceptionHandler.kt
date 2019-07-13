package io.zhudy.kitty.restful.handlers

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
class MissingKotlinParameterExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as MissingKotlinParameterException
            return RestError(
                    status = 400,
                    code = PubBizCodes.C_999.code,
                    message = "缺少参数 ${e.parameter.name}"
            )
        }
        return null
    }
}