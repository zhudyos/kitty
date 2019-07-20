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
package io.zhudy.kitty.service

import io.zhudy.kitty.auth.UserContext

/**
 * 用户权限校验。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
interface AuthorityService {

    /**
     * @property uri 请求的`uri`
     * @property method 请求的`method`
     * @property contentType 请求的`content-type`
     */
    data class Request(
            val uri: String,
            val method: String,
            val contentType: String?
    )

    /**
     * 校验用户是否有指定的访问权限。
     */
    fun checkAuthority(request: Request, uc: UserContext): Boolean
}