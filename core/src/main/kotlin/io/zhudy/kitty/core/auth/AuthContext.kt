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
package io.zhudy.kitty.core.auth

/**
 * 用户认证的上下文信息。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
interface AuthContext {

    companion object {
        /**
         * 保存在 Request 中的属性名称。
         */
        const val REQUEST_ATTRIBUTE = "io.zhudy.auth.UserContext"
    }

    /**
     * 当前认证的访问令牌。
     */
    val token: String
    /**
     * 当前请求的链路跟踪 ID。
     */
    val traceId: String
    /**
     * 用户账号 ID。
     */
    val uid: Int
    /**
     * 用户账号名称，与 [uid] 对应。
     *
     * @see UnsupportedOperationException
     */
    val uname: String
    /**
     * 用户昵称。
     *
     * @see UnsupportedOperationException
     */
    val nickname: String
    /**
     * 用户扩展属性。
     */
    val attrs: MutableMap<String, Any?>
}