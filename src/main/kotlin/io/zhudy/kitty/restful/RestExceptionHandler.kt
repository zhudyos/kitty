package io.zhudy.kitty.restful

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
interface RestExceptionHandler {

    /**
     *
     */
    fun handleException(ex: Exception): RestError?

}