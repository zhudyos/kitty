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
package io.zhudy.kitty.rest.problem

import java.time.Instant

/**
 * `REST` 错误响应接口定义。
 *
 * @property timestamp 错误发生的时间
 * @property traceId 发生错误的追踪 ID
 * @property path 请求的路径
 * @property method 请求的 HTTP Method
 * @property status HTTP Status
 * @property code 业务错误码
 * @property message 错误描述
 * @property developerMessage 错误描述（开发人员关注的信息）
 * @property details 错误的详细信息
 * @property stacktrace 错误堆栈
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
data class RestProblem(
        val timestamp: Instant,
        val traceId: String,
        val path: String,
        val method: String,
        val status: Int,
        val code: Int,
        val message: String,
        val developerMessage: String?,
        val details: Any?,
        val stacktrace: Any?
)
