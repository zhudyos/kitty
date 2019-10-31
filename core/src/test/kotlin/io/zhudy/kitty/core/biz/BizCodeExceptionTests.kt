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

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class BizCodeExceptionTests {

    @Test
    fun constructor1() {
        val b = BizCode.C500
        val ex = BizCodeException(b)

        assertThat(ex.bizCode).isEqualTo(b)
        assertThat(ex.developerMessage).isNull()
    }

    @Test
    fun constructor2() {
        val b = BizCode.C500
        val message = "custom exception message"
        val ex = BizCodeException(b, message)

        assertThat(ex.bizCode).isEqualTo(b)
        assertThat(ex.developerMessage).isEqualTo(message)
    }

    @Test
    fun constructor3() {
        val b = BizCode.C500
        val cause = IllegalArgumentException()
        val ex = BizCodeException(b, cause)

        assertThat(ex.bizCode).isEqualTo(b)
        assertThat(ex.cause).isEqualTo(cause)
        assertThat(ex.developerMessage).isNull()
    }

    @Test
    fun construct4() {
        val b = BizCode.C500
        val message = "custom exception message"
        val cause = IllegalArgumentException()
        val ex = BizCodeException(b, message, cause)

        assertThat(ex.bizCode).isEqualTo(b)
        assertThat(ex.developerMessage).isEqualTo(message)
        assertThat(ex.cause).isEqualTo(cause)
    }
}