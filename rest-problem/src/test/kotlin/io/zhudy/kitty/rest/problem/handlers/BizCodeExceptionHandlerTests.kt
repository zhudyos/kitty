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
package io.zhudy.kitty.rest.problem.handlers

import io.zhudy.kitty.core.biz.BizCode
import io.zhudy.kitty.core.biz.BizCodeException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class BizCodeExceptionHandlerTests {

    @Test
    fun handle() {
        val handler = BizCodeExceptionHandler()
        val bz = BizCode.C810
        val ex = BizCodeException(bz)
        val problem = handler.handle(ex)!!

        assertThat(problem.status).isEqualTo(bz.status)
        assertThat(problem.code).isEqualTo(bz.code)
    }

    @Test
    fun handle1() {
        val handler = BizCodeExceptionHandler()
        val ex = RuntimeException()
        val problem = handler.handle(ex)

        assertThat(problem).isNull()
    }
}