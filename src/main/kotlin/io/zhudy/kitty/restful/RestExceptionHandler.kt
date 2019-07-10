package io.zhudy.kitty.restful

import javax.servlet.http.HttpServletRequest

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
interface RestExceptionHandler<in E : Exception> {

    /**
     *
     */
    fun handleException(e: E, request: HttpServletRequest): RestError

}