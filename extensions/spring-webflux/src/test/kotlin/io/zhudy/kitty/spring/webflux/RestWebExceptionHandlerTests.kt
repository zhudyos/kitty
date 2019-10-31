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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import io.zhudy.kitty.core.biz.BizCode
import io.zhudy.kitty.core.biz.BizCodeException
import io.zhudy.kitty.rest.problem.RestProblemResolver
import io.zhudy.kitty.rest.problem.RestTracingUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.http.MediaType
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.server.RouterFunctions
import reactor.core.publisher.Mono

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class RestWebExceptionHandlerTests {

    private val objectMapper = ObjectMapper()
    private val problemExceptionHandler = RestWebExceptionHandler(
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
    fun handle(trace: String) {
        val httpRequest = MockServerHttpRequest.get("/api/v1/integration-test").build()
        val exchange = MockServerWebExchange.builder(httpRequest).build()

        val queryParams = LinkedMultiValueMap<String, String?>()
        if (trace != "none") {
            queryParams["trace"] = trace
        }

        val serverRequest = MockServerRequest.builder()
                .uri(httpRequest.uri)
                .queryParams(queryParams)
                .exchange(exchange)
                .build()
        exchange.attributes[RouterFunctions.REQUEST_ATTRIBUTE] = serverRequest

        val bz = BizCode.C500
        val ex = BizCodeException(bz)
        problemExceptionHandler.handle(exchange, ex).block()

        val response = exchange.response
        assertThat(response.headers.contentType)
                .isNotNull()
                .isEqualTo(MediaType.parseMediaType(RestTracingUtils.PROBLEM_MEDIA_TYPE))
        assertThat(response.statusCodeValue).isEqualTo(bz.status)

        val body = response.bodyAsString.block()
        assertThat(body).isNotEmpty()
    }

    @Test
    fun handle2() {
        val httpRequest = MockServerHttpRequest.get("/api/v1/integration-test").build()
        val exchange = MockServerWebExchange.builder(httpRequest).build()
        exchange.response.setComplete()
        val ex = RuntimeException()
        val mono = problemExceptionHandler.handle(exchange, ex)
        assertThat(mono).isEqualTo(Mono.empty<Void>())
    }
}