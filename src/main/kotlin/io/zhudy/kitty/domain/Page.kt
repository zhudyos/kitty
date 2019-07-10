/**
 * Copyright 2018-2018 the original author or authors
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

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.math.ceil

/**
 * 分页返回数据类型。
 *
 * @property items 指定页的数据项
 * @property totalItems 总记录数
 * @property pageable 分页参数
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
data class Page<out T>(
        val items: List<T> = emptyList(),
        val totalItems: Int = 0,
        @JsonIgnore
        val pageable: Pageable
) {

    /**
     * 总页数。
     */
    val totalPages get() = if (pageable.size == 0) 1 else ceil(totalItems.toDouble() / pageable.size.toDouble()).toInt()
}