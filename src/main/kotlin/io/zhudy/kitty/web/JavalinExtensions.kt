package io.zhudy.kitty.web

import io.javalin.Context

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
private const val path = "path"
private const val query = "query"
private const val form = "form"

/**
 * 返回 `path` 参数。
 *
 * `boolean` 取值设定。
 *
 * - true/false
 * - not 0/0
 * - on/off
 *
 * @param name 参数名称
 */
fun Context.pathBoolean(name: String) = requestBooleanParam(this, path, name)

/**
 * 返回 `path` 参数。
 *
 * @param name 参数名称
 */
fun Context.pathInt(name: String) = requestIntParam(this, path, name)

/**
 * 返回 `path` 参数。
 *
 * @param name 参数名称
 */
fun Context.pathLong(name: String) = requestLongParam(this, path, name)

/**
 * 返回 `path` 参数。
 *
 * @param name 参数名称
 */
fun Context.pathDouble(name: String) = requestDoubleParam(this, path, name)

/**
 * 返回 `path` 参数。
 *
 * @param name 参数名称
 */
fun Context.pathString(name: String) = requestStringParam(this, path, name)

/**
 * 返回 `path` 参数并去除前后空格。
 *
 * @param name 参数名称
 */
fun Context.pathTrimString(name: String) = requestTrimStringParam(this, path, name)

// =================================================================================================================

/**
 * 返回 `query` 参数。
 *
 * `boolean` 取值设定。
 *
 * - true/false
 * - not 0/0
 * - on/off
 *
 * @param name 参数名称
 */
fun Context.queryBoolean(name: String) = requestBooleanParam(this, query, name)

/**
 * 返回 `query` 参数。
 *
 * @param name 参数名称
 */
fun Context.queryInt(name: String) = requestIntParam(this, query, name)

/**
 * 返回 `query` 参数。
 *
 * @param name 参数名称
 */
fun Context.queryLong(name: String) = requestLongParam(this, query, name)

/**
 * 返回 `query` 参数。
 *
 * @param name 参数名称
 */
fun Context.queryDouble(name: String) = requestDoubleParam(this, query, name)

/**
 * 返回 `query` 参数。
 *
 * @param name 参数名称
 */
fun Context.queryString(name: String) = requestStringParam(this, query, name)

/**
 * 返回 `query` 参数并去除前后空格。
 *
 * @param name 参数名称
 */
fun Context.queryTrimString(name: String) = requestTrimStringParam(this, query, name)

// =================================================================================================================

/**
 * 返回 `form` 参数。
 *
 * `boolean` 取值设定。
 *
 * - true/false
 * - not 0/0
 * - on/off
 *
 * @param name 参数名称
 */
fun Context.formBoolean(name: String) = requestBooleanParam(this, form, name)

/**
 * 返回 `form` 参数。
 *
 * @param name 参数名称
 */
fun Context.formInt(name: String) = requestIntParam(this, form, name)

/**
 * 返回 `form` 参数。
 *
 * @param name 参数名称
 */
fun Context.formLong(name: String) = requestLongParam(this, form, name)

/**
 * 返回 `form` 参数。
 *
 * @param name 参数名称
 */
fun Context.formDouble(name: String) = requestDoubleParam(this, form, name)

/**
 * 返回 `form` 参数。
 *
 * @param name 参数名称
 */
fun Context.formString(name: String) = requestStringParam(this, form, name)

/**
 * 返回 `form` 参数并去除前后空格。
 *
 * @param name 参数名称
 */
fun Context.formTrimString(name: String) = requestTrimStringParam(this, form, name)

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
    return v == "true" || v != "0" || v == "on"
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
    path == where -> request.param(name)
    form == where -> request.formParam(name)
    else -> request.queryParam(name)
}
