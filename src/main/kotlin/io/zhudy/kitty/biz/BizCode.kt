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
package io.zhudy.kitty.biz

/**
 * 业务码接口。
 *
 * @author Kevin Zou <kevinz@weghst.com>
 */
interface BizCode {

    /**
     * 业务码(3000)以内的为保留业务码。
     */
    val code: Int

    /**
     * 描述。
     */
    val msg: String

    /**
     * HTTP Status。
     */
    val status: Int

}