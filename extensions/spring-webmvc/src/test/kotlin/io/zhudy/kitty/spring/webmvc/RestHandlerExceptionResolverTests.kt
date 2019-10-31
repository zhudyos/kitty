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
package io.zhudy.kitty.spring.webmvc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import io.zhudy.kitty.core.biz.BizCode
import io.zhudy.kitty.core.biz.BizCodeException
import io.zhudy.kitty.rest.problem.RestProblemResolver
import io.zhudy.kitty.rest.problem.RestTracingUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.http.HttpMethod
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockServletContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class RestHandlerExceptionResolverTests {

    private val objectMapper = ObjectMapper()
    private val exceptionResolver = RestHandlerExceptionResolver(
            RestProblemResolver(),
            objectMapper
    )

    init {
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
        objectMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "none",
        ""
    ])
    fun resolveException(trace: String) {
        val request = MockMvcRequestBuilders
                .request(HttpMethod.GET, "/api/v1/integration-test")
                .param("trace", if (trace == "none") null else trace)
                .buildRequest(MockServletContext())
        val response = MockHttpServletResponse()
        val bz = BizCode.C500
        val ex = BizCodeException(bz)
        val mav = exceptionResolver.resolveException(request, response, null, ex)
        assertThat(mav).isNotNull

        assertThat(response.status).isEqualTo(bz.status)
        assertThat(response.contentType).isNotNull().isEqualTo(RestTracingUtils.PROBLEM_MEDIA_TYPE)
        assertThat(response.contentAsString).isNotEmpty()
    }
}