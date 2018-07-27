package io.zhudy.kitty.okhttp3

import okhttp3.MediaType

/**
 * okhttp3 [MediaType] 全局定义。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
object MediaTypes {

    /**
     * `application/json; charset=utf-8`.
     */
    val APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8")
}
