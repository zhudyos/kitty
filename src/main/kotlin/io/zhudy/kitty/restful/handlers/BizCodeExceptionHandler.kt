package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.restful.RestError
import io.zhudy.kitty.restful.RestExceptionHandler
import javax.servlet.http.HttpServletRequest

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
class BizCodeExceptionHandler : RestExceptionHandler<BizCodeException> {

    override fun handleException(e: BizCodeException, request: HttpServletRequest): RestError {
        return RestError(
                status = e.bizCode.status,
                code = e.bizCode.code,
                message = e.message
        )
    }
}