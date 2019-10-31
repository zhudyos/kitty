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

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.zhudy.kitty.core.biz.BizCode
import io.zhudy.kitty.rest.problem.Problem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.instanceParameter

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class MissingKotlinParameterExceptionHandlerTests {

    @Test
    fun handle() {
        val handler = MissingKotlinParameterExceptionHandler()
        val ex = MissingKotlinParameterException(Pair<String, String>::first.instanceParameter!!, msg = "integration test")

        val bz = BizCode.C400
        val problem = handler.handle(ex)
        assertThat(problem)
                .isNotNull
                .hasFieldOrPropertyWithValue(BizCode::status.name, bz.status)
                .hasFieldOrPropertyWithValue(BizCode::code.name, bz.status)
                .hasFieldOrPropertyWithValue(Problem::developerMessage.name, ex.msg)
    }

    @Test
    fun handle1() {
        val handler = MissingKotlinParameterExceptionHandler()
        val ex = RuntimeException()
        val problem = handler.handle(ex)

        assertThat(problem).isNull()
    }
}