package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.restful.RestError
import io.zhudy.kitty.restful.RestExceptionHandler
import org.springframework.web.HttpMediaTypeNotSupportedException
import javax.servlet.http.HttpServletRequest

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
class HttpMediaTypeNotSupportedExceptionHandler : RestExceptionHandler<HttpMediaTypeNotSupportedException> {

    override fun handleException(e: HttpMediaTypeNotSupportedException, request: HttpServletRequest): RestError {
        return RestError(status = 415, code = 415, message = e.message ?: "不支持的 content-type")
    }
}