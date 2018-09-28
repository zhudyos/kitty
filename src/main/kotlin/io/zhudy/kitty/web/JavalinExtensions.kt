/**
 * Copyright 2018-2018 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.zhudy.kitty.web

import io.javalin.Context
import io.zhudy.kitty.domain.Pageable
import io.zhudy.kitty.domain.Sort
import io.zhudy.kitty.domain.parseSort
import io.zhudy.kitty.web.Constants.HTTP_FORM_PARAM_NAME
import io.zhudy.kitty.web.Constants.HTTP_PATH_PARAM_NAME
import io.zhudy.kitty.web.Constants.HTTP_QUERY_PARAM_NAME

/**
 * `javalin` 扩展函数。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */

/**
 * 返回 `path` 参数。
 *
 * `boolean` 取值设定。
 *
 * - true/false
 * - 1/not 1
 * - on/off
 *
 * @param name 参数名称
 */
fun Context.pathBoolean(name: String) = requestBooleanParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数。
 *
 * @param name 参数名称
 */
fun Context.pathInt(name: String) = requestIntParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数。
 *
 * @param name 参数名称
 */
fun Context.pathLong(name: String) = requestLongParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数。
 *
 * @param name 参数名称
 */
fun Context.pathDouble(name: String) = requestDoubleParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数。
 *
 * @param name 参数名称
 */
fun Context.pathString(name: String) = requestStringParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数并去除前后空格。
 *
 * @param name 参数名称
 */
fun Context.pathTrimString(name: String) = requestTrimStringParam(this, HTTP_PATH_PARAM_NAME, name)

// =================================================================================================================

/**
 * 返回 `query` 参数。
 *
 * `boolean` 取值设定。
 *
 * - true/false
 * - 1/not 1
 * - on/off
 *
 * @param name 参数名称
 */
fun Context.queryBoolean(name: String) = requestBooleanParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 返回 `query` 参数。
 *
 * @param name 参数名称
 */
fun Context.queryInt(name: String) = requestIntParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 返回 `query` 参数。
 *
 * @param name 参数名称
 */
fun Context.queryLong(name: String) = requestLongParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 返回 `query` 参数。
 *
 * @param name 参数名称
 */
fun Context.queryDouble(name: String) = requestDoubleParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 返回 `query` 参数。
 *
 * @param name 参数名称
 */
fun Context.queryString(name: String) = requestStringParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 返回 `query` 参数并去除前后空格。
 *
 * @param name 参数名称
 */
fun Context.queryTrimString(name: String) = requestTrimStringParam(this, HTTP_QUERY_PARAM_NAME, name)

// =================================================================================================================

/**
 * 返回 `form` 参数。
 *
 * `boolean` 取值设定。
 *
 * - true/false
 * - 1/not 1
 * - on/off
 *
 * @param name 参数名称
 */
fun Context.formBoolean(name: String) = requestBooleanParam(this, HTTP_FORM_PARAM_NAME, name)

/**
 * 返回 `form` 参数。
 *
 * @param name 参数名称
 */
fun Context.formInt(name: String) = requestIntParam(this, HTTP_FORM_PARAM_NAME, name)

/**
 * 返回 `form` 参数。
 *
 * @param name 参数名称
 */
fun Context.formLong(name: String) = requestLongParam(this, HTTP_FORM_PARAM_NAME, name)

/**
 * 返回 `form` 参数。
 *
 * @param name 参数名称
 */
fun Context.formDouble(name: String) = requestDoubleParam(this, HTTP_FORM_PARAM_NAME, name)

/**
 * 返回 `form` 参数。
 *
 * @param name 参数名称
 */
fun Context.formString(name: String) = requestStringParam(this, HTTP_FORM_PARAM_NAME, name)

/**
 * 返回 `form` 参数并去除前后空格。
 *
 * @param name 参数名称
 */
fun Context.formTrimString(name: String) = requestTrimStringParam(this, HTTP_FORM_PARAM_NAME, name)

/**
 * 排序参数。
 */
fun Context.sortParam(): Sort {
    val s = this.queryParam("sort")
    return parseSort(s)
}

/**
 * 分页参数。
 */
fun Context.pageParam(): Pageable {
    val p = queryInt("page")
    val s = queryInt("size")
    return Pageable(p, s)
}

/**
 * 获取客户端真实 IP。
 *
 * 获取顺序：
 *
 * - 获取 http header `x-real-ip`
 * - 获取 http header `x-forwarded-for` 第一个元素
 * - 获取远程连接地址 remote address
 */
fun Context.clientIp(): String {
    var ip = this.header("x-real-ip")
    if (ip != null && ip.isNotEmpty()) {
        return ip
    }
    ip = this.header("x-forwarded-for")
    if (ip != null && ip.isNotEmpty()) {
        ip = ip.split(",").firstOrNull()
    }
    if (ip != null && ip.isNotEmpty()) {
        return ip
    }
    return this.ip()
}

/**
 * 返回 `user-agent`。如果不存在则返回空字符。
 */
fun Context.ua() = userAgent() ?: ""

// =================================================================================================================
private fun requestBooleanParam(request: Context, where: String, name: String): Boolean {
    val v = requestTrimStringParam(request, where, name).toLowerCase()
    return v == "true" || v == "1" || v == "on"
}

private fun requestIntParam(request: Context, where: String, name: String): Int {
    val v = requestTrimStringParam(request, where, name)
    try {
        return v.toInt()
    } catch (e: NumberFormatException) {
        throw RequestParameterFormatException(where, name, """无法将 "$v" 转换为 int 类型""")
    }
}

private fun requestLongParam(request: Context, where: String, name: String): Long {
    val v = requestTrimStringParam(request, where, name)
    try {
        return v.toLong()
    } catch (e: NumberFormatException) {
        throw RequestParameterFormatException(where, name, """无法将 "$v" 转换为 long 类型""")
    }
}

private fun requestDoubleParam(request: Context, where: String, name: String): Double {
    val v = requestTrimStringParam(request, where, name)
    try {
        return v.toDouble()
    } catch (e: NumberFormatException) {
        throw RequestParameterFormatException(where, name, """无法将 "$v" 转换为 double 类型""")
    }
}

private fun requestStringParam(request: Context, where: String, name: String): String {
    val v = requestParam(request, where, name)
    if (v == null || v.isEmpty()) {
        throw MissingRequestParameterException(where, name)
    }
    return v
}

private fun requestTrimStringParam(request: Context, where: String, name: String): String {
    val v = requestParam(request, where, name)?.trim()
    if (v == null || v.isEmpty()) {
        throw MissingRequestParameterException(where, name)
    }
    return v
}

private fun requestParam(request: Context, where: String, name: String): String? = when {
    HTTP_PATH_PARAM_NAME == where -> request.pathParam(name)
    HTTP_FORM_PARAM_NAME == where -> request.formParam(name)
    else -> request.queryParam(name)
}
