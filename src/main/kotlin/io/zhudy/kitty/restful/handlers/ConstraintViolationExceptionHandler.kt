package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.restful.RestError
import io.zhudy.kitty.restful.RestExceptionHandler
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.toMessage
import javax.servlet.http.HttpServletRequest

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
class ConstraintViolationExceptionHandler : RestExceptionHandler<ConstraintViolationException> {

    override fun handleException(e: ConstraintViolationException, request: HttpServletRequest): RestError {
        return RestError(
                status = 400,
                code = PubBizCodes.C_999.code,
                message = "参数校验错误",
                details = e.constraintViolations.asSequence().map { it.toMessage() }.toSet()
        )
    }
}