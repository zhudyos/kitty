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
package io.zhudy.kitty.rest.problem

import io.zhudy.kitty.core.biz.BizCode
import io.zhudy.kitty.core.biz.BizCodeException
import io.zhudy.kitty.rest.problem.handlers.BizCodeExceptionHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class RestProblemResolverTests {

    @Test
    fun handle() {
        val bus = RestProblemResolver(mapOf(
                "io.zhudy.kitty.core.biz.BizCodeException" to BizCodeExceptionHandler()
        ))
        assertThat(bus).isNotNull
    }

    @Test
    fun handle2() {
        val bus = RestProblemResolver()
        val ex = RuntimeException("integration test")
        val problem = bus.resolve(ex)

        assertThat(problem.details).isEqualTo(ex)
    }

    @Test
    fun handle3() {
        val bus = RestProblemResolver()
        val cause = RuntimeException("integration test")
        val ex = IntegrationTestException(cause)
        val problem = bus.resolve(ex)

        assertThat(problem.details).isEqualTo(cause)
    }

    @Test
    fun handle4() {
        val bus = RestProblemResolver()
        val cause = IntegrationTestException()
        val ex = IntegrationTestException(cause)
        val problem = bus.resolve(ex)

        assertThat(problem.status).isEqualTo(BizCode.C500.status)
        assertThat(problem.code).isEqualTo(BizCode.C500.code)
        assertThat(problem.message).isEqualTo(BizCode.C500.message)
        assertThat(problem.developerMessage).isNotEmpty()
    }

    @Test
    fun handle5() {
        val bz = BizCode.C999
        val bus = RestProblemResolver()
        val cause = RuntimeException()
        val ex = BizCodeException(bz, cause)
        val problem = bus.resolve(ex)

        assertThat(problem.status).isEqualTo(bz.status)
        assertThat(problem.code).isEqualTo(bz.code)
        assertThat(problem.message).isEqualTo(bz.message)
        assertThat(problem.developerMessage).isNotEmpty()
    }
}