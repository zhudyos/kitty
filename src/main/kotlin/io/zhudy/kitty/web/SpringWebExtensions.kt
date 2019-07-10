package io.zhudy.kitty.web

import io.zhudy.kitty.auth.UserContext
import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.biz.PubBizCodes
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.attributeOrNull
import org.springframework.web.servlet.function.paramOrNull

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
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
    try {
        return v.toInt()
    } catch (e: NumberFormatException) {
        throw RequestParameterFormatException(HTTP_QUERY_PARAM_NAME, name, """无法将 "$v" 转换为 int 类型""")
    }
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
    try {
        return v.toLong()
    } catch (e: NumberFormatException) {
        throw RequestParameterFormatException(HTTP_QUERY_PARAM_NAME, name, """无法将 "$v" 转换为 long 类型""")
    }
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
    try {
        return v.toDouble()
    } catch (e: NumberFormatException) {
        throw RequestParameterFormatException(HTTP_QUERY_PARAM_NAME, name, """无法将 "$v" 转换为 double 类型""")
    }
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
 * 获取用户登录的上下信息。
 */
fun ServerRequest.userContext(): UserContext {
    return attributeOrNull(UserContext.HTTP_REQUEST_ATTRIBUTE_KEY) as? UserContext
            ?: throw BizCodeException(PubBizCodes.C_500, "缺少 UserContext")
}

// =================================================================================================================
private fun requestBooleanParam(request: ServerRequest, where: String, name: String): Boolean {
    val v = requestTrimStringParam(request, where, name).toLowerCase()
    return v == "true" || v == "1" || v == "on"
}

private fun requestIntParam(request: ServerRequest, where: String, name: String): Int {
    val v = requestTrimStringParam(request, where, name)
    try {
        return v.toInt()
    } catch (e: NumberFormatException) {
        throw RequestParameterFormatException(where, name, """无法将 "$v" 转换为 int 类型""")
    }
}

private fun requestLongParam(request: ServerRequest, where: String, name: String): Long {
    val v = requestTrimStringParam(request, where, name)
    try {
        return v.toLong()
    } catch (e: NumberFormatException) {
        throw RequestParameterFormatException(where, name, """无法将 "$v" 转换为 long 类型""")
    }
}

private fun requestDoubleParam(request: ServerRequest, where: String, name: String): Double {
    val v = requestTrimStringParam(request, where, name)
    try {
        return v.toDouble()
    } catch (e: NumberFormatException) {
        throw RequestParameterFormatException(where, name, """无法将 "$v" 转换为 double 类型""")
    }
}

private fun requestStringParam(request: ServerRequest, where: String, name: String): String {
    val v = requestParam(request, where, name)
    if (v == null || v.isEmpty()) {
        throw MissingRequestParameterException(where, name)
    }
    return v
}

private fun requestTrimStringParam(request: ServerRequest, where: String, name: String): String {
    val v = requestParam(request, where, name)?.trim()
    if (v == null || v.isEmpty()) {
        throw MissingRequestParameterException(where, name)
    }
    return v
}

private fun requestParam(request: ServerRequest, where: String, name: String): String? = if (HTTP_PATH_PARAM_NAME == where) request.pathVariable(name) else request.paramOrNull(name)

