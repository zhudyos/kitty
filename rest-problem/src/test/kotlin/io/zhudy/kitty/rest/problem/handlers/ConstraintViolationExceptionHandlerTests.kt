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
import io.zhudy.kitty.rest.problem.Problem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class ConstraintViolationExceptionHandlerTests {

    @Test
    fun handle() {
        val handler = ConstraintViolationExceptionHandler()
        val pair = Pair("", "")
        var ex: Exception = RuntimeException()
        try {
            validate(pair) {
                validate(Pair<String, String>::first).isNotEmpty()
                validate(Pair<String, String>::second).isNotEmpty()
            }
        } catch (e: ConstraintViolationException) {
            ex = e
        }

        val bz = BizCode.C400
        val problem = handler.handle(ex)
        assertThat(problem)
                .isNotNull
                .hasFieldOrPropertyWithValue(BizCode::status.name, bz.status)
                .hasFieldOrPropertyWithValue(BizCode::code.name, bz.status)
                .hasFieldOrProperty(Problem::details.name)
    }

    @Test
    fun handle1() {
        val handler = ConstraintViolationExceptionHandler()
        val ex = RuntimeException()
        val problem = handler.handle(ex)

        assertThat(problem).isNull()
    }
}