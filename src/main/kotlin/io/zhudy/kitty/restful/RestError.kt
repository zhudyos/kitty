/**
 * Copyright 2018-2019 the original author or authors
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
package io.zhudy.kitty.restful

import java.time.LocalDateTime

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
        val timestamp: LocalDateTime = LocalDateTime.now(),
        val traceId: String = "",
        val status: Int,
        var path: String = "",
        var method: String = "",
        val code: Int,
        val message: String,
        val details: Set<Any> = emptySet(),
        var stacktrace: List<String> = emptyList()
)