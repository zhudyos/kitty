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
package io.zhudy.kitty.domain

import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.biz.PubBizCodes

/**
 * 分页查询参数对象。
 *
 * @property page 页码
 * @property size 每页条数
 * @property offset 分页偏移量
 * @property begin 开始行数
 * @property end 结束行数
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
data class Pageable(
        val page: Int = 1,
        val size: Int = 15
) {

    companion object {
        /**
         * 分页最大的记录数。
         */
        var maxSize = 1000
    }

    init {
        if (page <= 0) {
            throw BizCodeException(PubBizCodes.C_999, "page 必须大于 0")
        }
        if (size <= 0) {
            throw BizCodeException(PubBizCodes.C_999, "size 必须大于 0")
        }
        if (size >= maxSize) {
            throw BizCodeException(PubBizCodes.C_999, "size 不能大于 $maxSize")
        }
    }

    val offset get() = (page - 1) * size
    val begin get() = offset + 1
    val end get() = offset + size
}