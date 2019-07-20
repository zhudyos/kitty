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
package io.zhudy.kitty.auth

/**
 * 用户上下文信息。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
interface UserContext {

    companion object {
        /**
         *
         */
        const val HTTP_REQUEST_ATTRIBUTE_KEY = "zhudy.auth.UserContext"
    }

    /**
     * 访问的令牌。
     */
    val token: String

    /**
     * 用户ID。
     */
    val id: Int

    /**
     * 用户名称。
     */
    val name: String

    /**
     * 用户请求追踪ID。
     */
    val traceId: String

    /**
     * 用户扩展属性。
     */
    val attrs: MutableMap<String, String?>
}