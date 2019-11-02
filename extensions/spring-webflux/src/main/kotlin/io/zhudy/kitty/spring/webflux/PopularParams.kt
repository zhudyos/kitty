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
package io.zhudy.kitty.spring.webflux

import io.zhudy.kitty.core.util.SortUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.reactive.function.server.ServerRequest

/**
 * 流行的参数封装。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
class PopularParams(private val request: ServerRequest) {

    private var _sort: Sort? = null

    /**
     * 返回排序对象。
     */
    val sort: Sort
        get() {
            var tmp = _sort
            if (tmp != null) {
                return tmp
            }
            tmp = request.queryParam("sort").map(SortUtils::parse).orElse(Sort.unsorted())
            _sort = tmp
            return tmp
        }

    private var _pageable: Pageable? = null
    /**
     * 返回分页参数。
     */
    val pageable: Pageable
        get() {
            var tmp = _pageable
            if (tmp != null) {
                return tmp
            }
            val p = request.queryInt("page") - 1
            val s = request.queryInt("size")

            tmp = PageRequest.of(p, s, sort)
            _pageable = tmp
            return tmp
        }
}