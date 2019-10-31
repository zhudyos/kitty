/**
 * Copyright 2019-2019 the original author or authors
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
package io.zhudy.kitty.spring.webmvc

import io.zhudy.kitty.core.exception.MissingRequestParameterException
import io.zhudy.kitty.core.exception.RequestParameterFormatException
import io.zhudy.kitty.core.util.TracingUtils
import io.zhudy.kitty.core.util.TracingUtils.TRACE_REQUEST_ATTRIBUTE
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.function.remoteAddressOrNull

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */

/**
 * 返回 `path` 参数.
 *
 * `boolean` 取值设定.
 *
 * - true/false
 * - 1/not 1
 * - on/off
 *
 * @param name 参数名称
 */
fun ServerRequest.pathBoolean(name: String) = requestBooleanParam(this, "path", name)

/**
 * 返回 `path` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.pathInt(name: String) = requestIntParam(this, "path", name)

/**
 * 返回 `path` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.pathLong(name: String) = requestLongParam(this, "path", name)

/**
 * 返回 `path` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.pathDouble(name: String) = requestDoubleParam(this, "path", name)

/**
 * 返回 `path` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.pathString(name: String) = requestStringParam(this, "path", name)

/**
 * 返回 `query` 参数.
 *
 * `boolean` 取值设定.
 *
 * - true/false
 * - 1/not 1
 * - on/off
 *
 * @param name 参数名称
 */
fun ServerRequest.queryBoolean(name: String) = requestBooleanParam(this, "query", name)

/**
 * 返回 `query` 参数.
 *
 * `boolean` 取值设定.
 *
 * - true/false
 * - 1/not 1
 * - on/off
 *
 * @param name 参数名称
 * @param defValue 如果指定的参数不存在则返回默认值
 */
fun ServerRequest.queryBoolean(name: String, defValue: Boolean): Boolean {
    val v = this.paramOrNull(name)?.toLowerCase() ?: return defValue
    return v == "true" || v == "1" || v == "on"
}

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryInt(name: String) = requestIntParam(this, "query", name)

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 * @param defValue 如果指定的参数不存在则返回默认值
 */
fun ServerRequest.queryInt(name: String, defValue: Int): Int {
    val v = this.paramOrNull(name) ?: return defValue
    return v.toIntOrNull() ?: defValue
}

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryLong(name: String) = requestLongParam(this, "query", name)

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 * @param defValue 如果指定的参数不存在则返回默认值
 */
fun ServerRequest.queryLong(name: String, defValue: Long): Long {
    val v = this.paramOrNull(name) ?: return defValue
    return v.toLongOrNull() ?: defValue
}

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryDouble(name: String) = requestDoubleParam(this, "query", name)

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 * @param defValue 如果指定的参数不存在则返回默认值
 */
fun ServerRequest.queryDouble(name: String, defValue: Double): Double {
    val v = this.paramOrNull(name) ?: return defValue
    return v.toDoubleOrNull() ?: defValue
}

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryString(name: String) = requestStringParam(this, "query", name)

/**
 * 返回 `query` 参数并去除前后空格.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryTrimString(name: String) = requestTrimStringParam(this, "query", name)

// =================================================================================================================

/**
 * 返回客户端 IP 地址。
 *
 * 1. http header `x-real-ip`
 * 2. http header `x-forwarded-for`
 * 3. `remote address`
 *
 * 根据以上顺序获取客户端 IP。
 */
fun ServerRequest.ip(): String {
    val realIp = headers().header("x-real-ip").firstOrNull()
    if (realIp != null) {
        return realIp
    }
    val xff = headers().header("x-forwarded-for").firstOrNull()?.split(",")
    if (xff != null && xff.isNotEmpty()) {
        return xff[0]
    }
    return remoteAddressOrNull()?.run { address.hostAddress } ?: ""
}

/**
 * 返回追踪 `trace-id`，在同一个请求中多次调用返回的 `trace-id` 相同。
 *
 * 如果请求中不包括 `x-request-id` 则自动生成。
 */
fun ServerRequest.traceId(): String {
    return attribute(TRACE_REQUEST_ATTRIBUTE).orElseGet {
        val traceId = headers().header(TracingUtils.HTTP_HEADER_REQUEST_ID).firstOrNull() ?: TracingUtils.traceId()
        attributes()[TRACE_REQUEST_ATTRIBUTE] = traceId
        traceId
    }.toString()
}

/**
 * 公共的包装参数对象。
 */
fun ServerRequest.popularParams(): PopularParams {
    val name = "io.zhudy.kitty.webmvc.popularParams"
    return attribute(name).orElseGet {
        val pp = PopularParams(this)
        attributes()[name] = pp
        pp
    } as PopularParams
}

// =================================================================================================================
private fun requestBooleanParam(request: ServerRequest, where: String, name: String): Boolean {
    val v = requestStringParam(request, where, name).toLowerCase()
    return v == "true" || v == "1" || v == "on"
}

private fun requestIntParam(request: ServerRequest, where: String, name: String): Int {
    val v = requestStringParam(request, where, name)
    return v.toIntOrNull() ?: throw RequestParameterFormatException(where, name, "\"$v\" 非法的 int 数字")
}

private fun requestLongParam(request: ServerRequest, where: String, name: String): Long {
    val v = requestStringParam(request, where, name)
    return v.toLongOrNull() ?: throw RequestParameterFormatException(where, name, "\"$v\" 非法的 long 数字")
}

private fun requestDoubleParam(request: ServerRequest, where: String, name: String): Double {
    val v = requestStringParam(request, where, name)
    return v.toDoubleOrNull() ?: throw RequestParameterFormatException(where, name, "\"$v\" 非法的 double 数字")
}

private fun requestStringParam(request: ServerRequest, where: String, name: String): String {
    val v = requestParam(request, where, name)
    if (v.isNullOrEmpty()) {
        throw MissingRequestParameterException(where, name)
    }
    return v
}

private fun requestTrimStringParam(request: ServerRequest, where: String, name: String): String {
    val v = requestParam(request, where, name)?.trim()
    if (v.isNullOrEmpty()) {
        throw MissingRequestParameterException(where, name)
    }
    return v
}

private fun requestParam(request: ServerRequest, where: String, name: String): String? = if ("path" == where) request.pathVariable(name) else request.paramOrNull(name)
