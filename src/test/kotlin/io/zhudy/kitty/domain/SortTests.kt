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
import org.junit.jupiter.api.Test

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class SortTests {

    @Test
    fun sort() {
        val sort = Sort.parse("age")
        val first = sort.orders.first()
        assertThat(first.name).isEqualTo("age")
        assertThat(first.direction).isEqualTo(Sort.DIRECTION_ASC)
    }

    @Test
    fun `sort desc`() {
        val sort = Sort.parse("-age")
        val first = sort.orders.first()
        assertThat(first.name).isEqualTo("age")
        assertThat(first.direction).isEqualTo(Sort.DIRECTION_DESC)
    }

    @Test
    fun unsort() {
        val sort = Sort.parse(null)
        assertThat(sort).isEqualTo(Sort.unsorted)
    }

    @Test
    fun unsort2() {
        val sort = Sort.parse("")
        assertThat(sort).isEqualTo(Sort.unsorted)
    }
}