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
package io.zhudy.kitty.core.biz

/**
 * 业务异常，与 [BizCode] 联合使用。
 *
 * @author Kevin Zou <kevinz@weghst.com>
 */
class BizCodeException : RuntimeException {

    /**
     * 业务错误码。
     */
    val bizCode: BizCode
    /**
     * 开发人员使用的消息。
     */
    val developerMessage: String?

    /**
     * 构建一个业务码异常。
     *
     * @param bizCode 业务错误码
     */
    constructor(bizCode: BizCode) {
        this.bizCode = bizCode
        this.developerMessage = null
    }

    /**
     * 构建一个业务码异常。
     *
     * @param bizCode 业务错误码
     * @param message 错误描述
     */
    constructor(bizCode: BizCode, message: String?) : super(message) {
        this.bizCode = bizCode
        this.developerMessage = message
    }

    /**
     * 构建一个业务码异常。
     *
     * @param bizCode 业务错误码
     * @param cause 错误原因
     */
    constructor(bizCode: BizCode, cause: Throwable) : super(cause) {
        this.bizCode = bizCode
        this.developerMessage = cause.message
    }

    /**
     * 构建一个业务码异常。
     *
     * @param bizCode 业务错误码
     * @param message 错误描述
     * @param cause 错误原因
     */
    constructor(bizCode: BizCode, message: String?, cause: Throwable) : super(message, cause) {
        this.bizCode = bizCode
        this.developerMessage = message
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(javaClass.name).append(": [").append(bizCode.code).append("] ").append(bizCode.message)
        if (message != null) {
            sb.append(" => ").append(message)
        }
        return sb.toString()
    }
}
