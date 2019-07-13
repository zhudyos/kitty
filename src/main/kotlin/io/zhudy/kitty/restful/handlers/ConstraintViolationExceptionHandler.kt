package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.annotation.Order
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.toMessage

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Int.MIN_VALUE + 1000)
class ConstraintViolationExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("org.valiktor.ConstraintViolationException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as ConstraintViolationException
            return RestError(
                    status = 400,
                    code = PubBizCodes.C_999.code,
                    message = "参数校验错误",
                    details = e.constraintViolations.asSequence().map { it.toMessage() }.toSet()
            )
        }
        return null
    }
}