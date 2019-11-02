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

import io.zhudy.kitty.core.exception.MissingRequestParameterException
import io.zhudy.kitty.core.exception.RequestParameterFormatException
import io.zhudy.kitty.core.util.TracingUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.data.domain.Sort
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.mock.web.MockServletContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerRequest
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class SpringWebExtensionsKtTests {

    private val servletContext = MockServletContext()

    private fun wrapRequest(httpServletRequest: HttpServletRequest): ServerRequest {
        val defaultMessageConverters = object : WebMvcConfigurationSupport() {
            fun defaultMessageConverters(): List<HttpMessageConverter<*>> {
                return messageConverters
            }
        }.defaultMessageConverters()

        return ServerRequest.create(httpServletRequest, defaultMessageConverters)
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "true",
        "True",
        "TRue",
        "TRUE",
        "TrUE",
        "On",
        "on",
        "ON",
        "1"
    ])
    fun pathBoolean(v: String) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/$v")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to v
                        ))
                        .buildRequest(servletContext)
        )
        assertThat(request.pathBoolean("v")).isTrue()
    }

    @ParameterizedTest
    @ValueSource(ints = [
        -1,
        0,
        99,
        Int.MIN_VALUE,
        Int.MAX_VALUE
    ])
    fun pathInt(v: Int) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/$v")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to v.toString(),
                                "v2" to "x",
                                "v3" to null
                        ))
                        .buildRequest(servletContext)
        )
        assertThat(request.pathInt("v")).isEqualTo(v)
    }

    @Test
    fun `pathInt format exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/x")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to "x"
                        ))
                        .buildRequest(servletContext)
        )
        assertThatThrownBy { request.pathInt("v") }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `pathInt missing exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/x")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to null
                        ))
                        .buildRequest(servletContext)
        )
        assertThatThrownBy { request.pathInt("v") }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        -1L,
        0L,
        99L,
        Long.MIN_VALUE,
        Long.MAX_VALUE
    ])
    fun pathLong(v: Long) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/$v")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to v.toString()
                        ))
                        .buildRequest(servletContext)
        )
        assertThat(request.pathLong("v")).isEqualTo(v)
    }

    @Test
    fun `pathLong format exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/x")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to "x"
                        ))
                        .buildRequest(servletContext)
        )
        assertThatThrownBy {
            request.pathLong("v")
        }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `pathLong missing exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/x")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to null
                        ))
                        .buildRequest(servletContext)
        )
        assertThatThrownBy {
            request.pathLong("v")
        }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(doubles = [
        0.0,
        99.0,
        9999.9999999
    ])
    fun pathDouble(v: Double) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/$v")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to v.toString()
                        ))
                        .buildRequest(servletContext)
        )
        assertThat(request.pathDouble("v")).isEqualTo(v)
    }

    @Test
    fun `pathDouble format exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/x")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to "x"
                        ))
                        .buildRequest(servletContext)
        )

        assertThatThrownBy {
            request.pathDouble("v")
        }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `pathDouble missing exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/x")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to null
                        ))
                        .buildRequest(servletContext)
        )
        assertThatThrownBy {
            request.pathDouble("v")
        }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "a",
        "abc",
        "你好",
        "*F+33344",
        "世界冠军"
    ])
    fun pathString(v: String) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test/$v")
                        .requestAttr(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, mapOf(
                                "v" to v
                        ))
                        .buildRequest(servletContext)
        )
        assertThat(request.pathString("v")).isEqualTo(v)
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "true",
        "True",
        "TRue",
        "TRUE",
        "TrUE",
        "On",
        "on",
        "ON",
        "1"
    ])
    fun queryBoolean(v: String) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=$v")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryBoolean("v")).isTrue()
    }

    @Test
    fun `queryBoolean default value`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryBoolean("v", true)).isTrue()
        assertThat(request.queryBoolean("v", false)).isFalse()
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "true",
        "True",
        "TRue",
        "TRUE",
        "TrUE",
        "On",
        "on",
        "ON",
        "1"
    ])
    fun `queryBoolean2 default value`(v: String) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=$v")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryBoolean("v", false)).isTrue()
    }

    @ParameterizedTest
    @ValueSource(ints = [
        -1,
        0,
        99,
        Int.MIN_VALUE,
        Int.MAX_VALUE
    ])
    fun queryInt(v: Int) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=$v")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryInt("v")).isEqualTo(v)
    }

    @ParameterizedTest
    @ValueSource(ints = [
        -1,
        0,
        99,
        Int.MIN_VALUE,
        Int.MAX_VALUE
    ])
    fun `queryInt default value`(v: Int) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryInt("v", v)).isEqualTo(v)
    }

    @Test
    fun `queryInt2 default value`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=xxx")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryInt("v", 1)).isEqualTo(1)
    }

    @Test
    fun `queryInt format exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=x")
                        .buildRequest(servletContext)
        )
        assertThatThrownBy { request.queryInt("v") }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `queryInt missing exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThatThrownBy { request.queryInt("v") }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        -1,
        0,
        99,
        Long.MIN_VALUE,
        Long.MAX_VALUE
    ])
    fun queryLong(v: Long) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=$v")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryLong("v")).isEqualTo(v)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        -1,
        0,
        99,
        Long.MIN_VALUE,
        Long.MAX_VALUE
    ])
    fun `queryLong default value`(v: Long) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryLong("v", v)).isEqualTo(v)
    }

    @Test
    fun `queryLong2 default value`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=xxx")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryLong("v", 1)).isEqualTo(1)
    }

    @Test
    fun `queryLong format exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=x")
                        .buildRequest(servletContext)
        )
        assertThatThrownBy {
            request.queryLong("v")
        }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `queryLong missing exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThatThrownBy {
            request.queryLong("v")
        }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(doubles = [
        -1.0,
        0.0,
        999999.99999
    ])
    fun queryDouble(v: Double) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=$v")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryDouble("v")).isEqualTo(v)
    }

    @ParameterizedTest
    @ValueSource(doubles = [
        -1.0,
        0.0,
        999999.99999
    ])
    fun `queryDouble default value`(v: Double) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryDouble("v", v)).isEqualTo(v)
    }

    @Test
    fun `queryDouble2 default value`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=xxx")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryDouble("v", 1.1)).isEqualTo(1.1)
    }

    @Test
    fun `queryDouble format exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=x")
                        .buildRequest(servletContext)
        )
        assertThatThrownBy { request.queryDouble("v") }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `queryDouble missing exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThatThrownBy { request.queryDouble("v") }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "a",
        "abc",
        "你好",
        "*F+33344",
        "世界冠军"
    ])
    fun queryString(v: String) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=$v")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryString("v")).isEqualTo(v)
    }

    @Test
    fun `queryString missing exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThatThrownBy { request.queryString("v") }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "a a  ",
        "  abc",
        "  你好   "
    ])
    fun queryTrimString(v: String) {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test?v=$v")
                        .buildRequest(servletContext)
        )
        assertThat(request.queryTrimString("v")).isEqualTo(v.trim())
    }

    @Test
    fun `queryTrimString missing exception`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThatThrownBy { request.queryTrimString("v") }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @Test
    fun `ip - x-real-ip`() {
        val ip = "127.0.0.1"
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .header("x-real-ip", ip)
                        .buildRequest(servletContext)
        )
        assertThat(request.ip).isEqualTo(ip)
    }

    @Test
    fun `ip - x-forwarded-for`() {
        val ip = "127.0.0.1"
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .header("x-forwarded-for", StringJoiner(",").add(ip).add("192.168.1.1").toString())
                        .buildRequest(servletContext)
        )
        assertThat(request.ip).isEqualTo(ip)
    }

    @Test
    fun `ip - remote address`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        assertThat(request.ip).isNotEmpty()
    }

    @Test
    fun traceId() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .buildRequest(servletContext)
        )
        val t1 = request.traceId
        val t2 = request.traceId
        assertThat(t1).isEqualTo(t2)
    }

    @Test
    fun `traceId - x-request-id`() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .header("x-request-id", TracingUtils.traceId())
                        .buildRequest(servletContext)
        )
        val t1 = request.traceId
        val t2 = request.traceId
        assertThat(t1).isEqualTo(t2)
    }

    @Test
    fun popularParams() {
        val request = wrapRequest(
                MockMvcRequestBuilders.get("/test")
                        .header("x-request-id", TracingUtils.traceId())
                        .param("sort", "name")
                        .param("page", "1")
                        .param("size", "15")
                        .buildRequest(servletContext)
        )

        val popularParams = request.popularParams

        val sort = popularParams.sort
        assertThat(sort.toList())
                .isNotEmpty
                .first()
                .hasFieldOrPropertyWithValue("direction", Sort.Direction.ASC)

        val p = popularParams.pageable
        assertThat(p.pageNumber).isEqualTo(0)
        assertThat(p.pageSize).isEqualTo(15)

        // 恒等
        val neoPopularParams = request.popularParams
        assertThat(neoPopularParams).isEqualTo(popularParams)
        assertThat(neoPopularParams.sort).isEqualTo(sort)
        assertThat(neoPopularParams.pageable).isEqualTo(p)
    }

}