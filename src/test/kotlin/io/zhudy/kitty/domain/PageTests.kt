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

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class PageTests {

    @ParameterizedTest
    @ValueSource(ints = [9, 10, 19, 59, 99, 100, 999])
    fun totalPages(totalItems: Int) {
        val p = Pageable(page = 1, size = 10)
        val items = (1..p.size).map { it }
        val page = Page(items = items, totalItems = totalItems, pageable = p)

        val expected = if (totalItems % p.size == 0) {
            totalItems / p.size
        } else {
            totalItems / p.size + 1
        }
        assertThat(page.totalPages).isEqualTo(expected)
        assertThat(page.items).isEqualTo(items)
        assertThat(page.totalItems).isEqualTo(totalItems)
        assertThat(page.pageable).isEqualTo(p)
    }
}