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

import io.zhudy.kitty.domain.Pageable
import io.zhudy.kitty.domain.Sort
import io.zhudy.kitty.domain.parseSort
import io.zhudy.kitty.web.Constants.HTTP_PATH_PARAM_NAME
import io.zhudy.kitty.web.Constants.HTTP_QUERY_PARAM_NAME
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
 * @param name 参数名称
 */
fun ServerRequest.queryInt(name: String) = requestIntParam(this, HTTP_QUERY_PARAM_NAME, name)

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
 */
fun ServerRequest.queryDouble(name: String) = requestDoubleParam(this, HTTP_QUERY_PARAM_NAME, name)

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
 * 排序参数。
 */
fun ServerRequest.sortParam(): Sort {
    val s = this.queryParam("sort").orElse(null)
    return parseSort(s)
}

/**
 * 分页参数。
 */
fun ServerRequest.pageParam(): Pageable {
    val p = queryInt("page")
    val s = queryInt("size")
    return Pageable(p, s)
}

// =================================================================================================================

/**
 *
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