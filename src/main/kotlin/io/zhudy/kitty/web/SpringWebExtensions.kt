package io.zhudy.kitty.web

import io.zhudy.kitty.auth.UserContext
import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.util.TracingUtils
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.attributeOrNull
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.function.remoteAddressOrNull

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
/**
 * 业务追踪 ID。
 */
const val TRACE_ID = "io.zhudy.traceId"
private const val HTTP_PATH_PARAM_NAME = "path"
private const val HTTP_QUERY_PARAM_NAME = "query"

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
    val v = this.paramOrNull(name) ?: return defValue
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
    val v = this.paramOrNull(name) ?: return defValue
    return v.toIntOrNull() ?: throw RequestParameterFormatException(HTTP_QUERY_PARAM_NAME, name, "\"$v\" 非法的 int 数字")
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
    val v = this.paramOrNull(name) ?: return defValue
    return v.toLongOrNull() ?: throw RequestParameterFormatException(HTTP_QUERY_PARAM_NAME, name, "\"$v\" 非法的 long 数字")
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
    val v = this.paramOrNull(name) ?: return defValue
    return v.toDoubleOrNull()
            ?: throw RequestParameterFormatException(HTTP_QUERY_PARAM_NAME, name, "\"$v\" 非法的 double 数字")
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
    return remoteAddressOrNull()?.hostName ?: ""
}

/**
 * 返回追踪 `trace-id`，在同一个请求中多次调用返回的 `trace-id` 相同。
 *
 * 如果请求中不包括 `x-request-id` 则自动生成。
 */
fun ServerRequest.traceId(): String {
    var traceId = attributeOrNull(TRACE_ID)?.toString()
    if (traceId != null) {
        return traceId
    }

    traceId = headers().header("x-request-id").firstOrNull() ?: TracingUtils.traceId()
    attributes()[TRACE_ID] = traceId
    return traceId
}

/**
 * 获取用户登录的上下信息。
 */
fun ServerRequest.userContext(): UserContext {
    return attributeOrNull(UserContext.HTTP_REQUEST_ATTRIBUTE_KEY) as? UserContext
            ?: throw BizCodeException(PubBizCodes.C_500, "缺少 UserContext")
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

private fun requestParam(request: ServerRequest, where: String, name: String): String? = if (HTTP_PATH_PARAM_NAME == where) request.pathVariable(name) else request.paramOrNull(name)
