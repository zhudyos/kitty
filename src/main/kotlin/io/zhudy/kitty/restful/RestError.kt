package io.zhudy.kitty.restful

import java.time.ZonedDateTime

/**
 * `RESTful` 错误信息返回。
 *
 * @property timestamp 错误时间
 * @property status http status
 * @property path 请求的 url
 * @property traceId 错误追踪 ID
 * @property code 业务错误码
 * @property message 错误描述
 * @property details 详细错误信息
 * @property stacktrace 错误堆栈详细信息
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
class RestError(
        var timestamp: ZonedDateTime = ZonedDateTime.now(),
        var traceId: String = "",
        var path: String = "",
        var status: Int,
        var code: Int,
        var message: String,
        var details: Set<Any> = emptySet(),
        var stacktrace: List<String> = emptyList()
)