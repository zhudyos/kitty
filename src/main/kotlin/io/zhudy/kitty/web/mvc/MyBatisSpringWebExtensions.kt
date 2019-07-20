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
package io.zhudy.kitty.web.mvc

import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.biz.PubBizCodes
import org.apache.ibatis.session.RowBounds
import org.springframework.web.servlet.function.ServerRequest

/**
 * 返回 MyBatis 分页参数。
 */
fun ServerRequest.mybatisParams() = MyBatisPackParams(this)

/**
 * MyBatis 扩展参数。
 */
class MyBatisPackParams(private val request: ServerRequest) {

    companion object {
        /**
         * 设置 MyBatis 返回记录数的最大值。
         */
        var maxPageSize = 500
    }

    /**
     * 返回 MyBatis 分页参数对象。
     */
    fun rowBounds(): RowBounds {
        val p = request.queryInt("page")
        val s = request.queryInt("size")
        if (s > maxPageSize) {
            throw BizCodeException(PubBizCodes.C_999, "size 不能大于 $maxPageSize")
        }
        return RowBounds((p - 1) * s, s)
    }
}