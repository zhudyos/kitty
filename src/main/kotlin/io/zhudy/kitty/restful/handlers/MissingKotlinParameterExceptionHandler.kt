package io.zhudy.kitty.restful.handlers

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.restful.RestError
import io.zhudy.kitty.restful.RestExceptionHandler
import javax.servlet.http.HttpServletRequest

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
class MissingKotlinParameterExceptionHandler : RestExceptionHandler<MissingKotlinParameterException> {

    override fun handleException(e: MissingKotlinParameterException, request: HttpServletRequest): RestError {
        return RestError(
                status = 400,
                code = PubBizCodes.C_999.code,
                message = "缺少参数 ${e.parameter.name}"
        )
    }
}