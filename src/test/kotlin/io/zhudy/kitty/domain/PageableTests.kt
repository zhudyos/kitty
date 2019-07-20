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
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class PageableTests {

    @Test
    fun normal() {
        val p = Pageable(page = 1, size = 10)

        assertThat(p.page).isEqualTo(1)
        assertThat(p.offset).isEqualTo(0)
        assertThat(p.begin).isEqualTo(1)
        assertThat(p.end).isEqualTo(p.size)
    }

    @Test
    fun `page&size less 1`() {
        assertThatExceptionOfType(BizCodeException::class.java).isThrownBy {
            Pageable(page = 0, size = 10)
        }

        assertThatExceptionOfType(BizCodeException::class.java).isThrownBy {
            Pageable(page = 1, size = 0)
        }
    }

    @Test
    fun `page max size`() {
        assertThatThrownBy {
            Pageable(page = 1, size = Pageable.maxSize)
        }.isInstanceOf(BizCodeException::class.java)
    }
}