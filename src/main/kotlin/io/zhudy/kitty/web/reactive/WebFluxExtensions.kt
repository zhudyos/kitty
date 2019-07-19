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
package io.zhudy.kitty.web.reactive

import io.zhudy.kitty.domain.Pageable
import io.zhudy.kitty.domain.parseSort
import io.zhudy.kitty.web.Constants.HTTP_PATH_PARAM_NAME
import io.zhudy.kitty.web.Constants.HTTP_QUERY_PARAM_NAME
import io.zhudy.kitty.web.MissingRequestParameterException
import io.zhudy.kitty.web.RequestParameterFormatException
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

/**
 * `webflux` 扩展函数。
 *
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
fun ServerRequest.pathBoolean(name: String) = requestBooleanParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.pathInt(name: String) = requestIntParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.pathLong(name: String) = requestLongParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.pathDouble(name: String) = requestDoubleParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.pathString(name: String) = requestStringParam(this, HTTP_PATH_PARAM_NAME, name)

/**
 * 返回 `path` 参数并去除前后空格.
 *
 * @param name 参数名称
 */
fun ServerRequest.pathTrimString(name: String) = requestTrimStringParam(this, HTTP_PATH_PARAM_NAME, name)

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
fun ServerRequest.queryBoolean(name: String) = requestBooleanParam(this, HTTP_QUERY_PARAM_NAME, name)

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
    val v = this.queryParam(name).orElse(null) ?: return defValue
    return v == "true" || v == "1" || v == "on"
}

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryInt(name: String) = requestIntParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 * @param defValue 如果指定的参数不存在则返回默认值
 */
fun ServerRequest.queryInt(name: String, defValue: Int): Int {
    val v = this.queryParam(name).orElse(null) ?: return defValue
    return v.toIntOrNull() ?: defValue
}

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryLong(name: String) = requestLongParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 * @param defValue 如果指定的参数不存在则返回默认值
 */
fun ServerRequest.queryLong(name: String, defValue: Long): Long {
    val v = this.queryParam(name).orElse(null) ?: return defValue
    return v.toLongOrNull() ?: defValue
}

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryDouble(name: String) = requestDoubleParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 * @param defValue 如果指定的参数不存在则返回默认值
 */
fun ServerRequest.queryDouble(name: String, defValue: Double): Double {
    val v = this.queryParam(name).orElse(null) ?: return defValue
    return v.toDoubleOrNull() ?: defValue
}

/**
 * 返回 `query` 参数.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryString(name: String) = requestStringParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 返回 `query` 参数并去除前后空格.
 *
 * @param name 参数名称
 */
fun ServerRequest.queryTrimString(name: String) = requestTrimStringParam(this, HTTP_QUERY_PARAM_NAME, name)

/**
 * 公共的包装参数对象。
 */
fun ServerRequest.packParams() = PackParams(this)

/**
 * 包装公共参数。
 */
class PackParams(private val request: ServerRequest) {

    /**
     * 返回排序对象。
     */
    fun sort() = parseSort(request.queryParam("sort").orElse(null))

    /**
     * 返回分页参数。
     */
    fun page(): Pageable {
        val p = request.queryInt("page")
        val s = request.queryInt("size")
        return Pageable(p, s)
    }
}

// =================================================================================================================

/**
 * 响应对象。
 */
fun ServerResponse.BodyBuilder.body(o: Any): Mono<ServerResponse> = body(BodyInserters.fromObject(o))


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

private fun requestParam(request: ServerRequest, where: String, name: String): String? = if (HTTP_PATH_PARAM_NAME == where) request.pathVariable(name) else request.queryParam(name).orElse(null)