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
package io.zhudy.kitty.web.reactive

import io.zhudy.kitty.domain.Sort
import io.zhudy.kitty.util.TracingUtils
import io.zhudy.kitty.web.MissingRequestParameterException
import io.zhudy.kitty.web.RequestParameterFormatException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import java.net.InetSocketAddress
import java.net.URI
import java.util.*

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class WebFluxExtensionsKtTests {

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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", v)
                .build()
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", v.toString())
                .build()
        assertThat(request.pathInt("v")).isEqualTo(v)
    }

    @Test
    fun `pathInt format exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", "x")
                .build()
        assertThatThrownBy { request.pathInt("v") }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `pathInt missing exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", "")
                .build()
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", v.toString())
                .build()
        assertThat(request.pathLong("v")).isEqualTo(v)
    }

    @Test
    fun `pathLong format exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", "x")
                .build()
        assertThatThrownBy { request.pathLong("v") }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `pathLong missing exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", "")
                .build()
        assertThatThrownBy { request.pathLong("v") }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(doubles = [
        0.0,
        99.0,
        9999.9999999
    ])
    fun pathDouble(v: Double) {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", v.toString())
                .build()
        assertThat(request.pathDouble("v")).isEqualTo(v)
    }

    @Test
    fun `pathDouble format exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", "x")
                .build()
        assertThatThrownBy { request.pathDouble("v") }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `pathDouble missing exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", "")
                .build()
        assertThatThrownBy { request.pathDouble("v") }.isInstanceOf(MissingRequestParameterException::class.java)
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .pathVariable("v", v)
                .build()
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", v)
                .build()
        assertThat(request.queryBoolean("v")).isTrue()
    }

    @Test
    fun `queryBoolean default value`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", v)
                .build()
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", v.toString())
                .build()
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
        assertThat(request.queryInt("v", v)).isEqualTo(v)
    }

    @Test
    fun `queryInt2 default value`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", "x")
                .build()
        assertThat(request.queryInt("v", 1)).isEqualTo(1)
    }

    @Test
    fun `queryInt format exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", "x")
                .build()
        assertThatThrownBy { request.queryInt("v") }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `queryInt missing exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", v.toString())
                .build()
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", "x")
                .build()
        assertThat(request.queryLong("v", v)).isEqualTo(v)
    }

    @Test
    fun `queryLong2 default value`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
        assertThat(request.queryLong("v", 1)).isEqualTo(1)
    }

    @Test
    fun `queryLong format exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", "x")
                .build()
        assertThatThrownBy { request.queryLong("v") }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `queryLong missing exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
        assertThatThrownBy { request.queryLong("v") }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(doubles = [
        -1.0,
        0.0,
        999999.99999
    ])
    fun queryDouble(v: Double) {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test?v=$v"))
                .queryParam("v", v.toString())
                .build()
        assertThat(request.queryDouble("v")).isEqualTo(v)
    }

    @ParameterizedTest
    @ValueSource(doubles = [
        -1.0,
        0.0,
        999999.99999
    ])
    fun `queryDouble default value`(v: Double) {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
        assertThat(request.queryDouble("v", v)).isEqualTo(v)
    }

    @Test
    fun `queryDouble2 default value`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", "x")
                .build()
        assertThat(request.queryDouble("v", 1.0)).isEqualTo(1.0)
    }

    @Test
    fun `queryDoulbe format exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", "x")
                .build()
        assertThatThrownBy { request.queryDouble("v") }.isInstanceOf(RequestParameterFormatException::class.java)
    }

    @Test
    fun `queryDouble missing exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
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
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", v)
                .build()
        assertThat(request.queryString("v")).isEqualTo(v)
    }

    @Test
    fun `queryString missing exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
        assertThatThrownBy { request.queryString("v") }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "a a  ",
        "  abc",
        "  你好   "
    ])
    fun queryTrimString(v: String) {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("v", v)
                .build()
        assertThat(request.queryTrimString("v")).isEqualTo(v.trim())
    }

    @Test
    fun `queryTrimString missing exception`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
        assertThatThrownBy { request.queryTrimString("v") }.isInstanceOf(MissingRequestParameterException::class.java)
    }

    @Test
    fun `ip - x-real-ip`() {
        val ip = "127.0.0.1"
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .header("x-real-ip", ip)
                .build()
        assertThat(request.ip()).isEqualTo(ip)
    }

    @Test
    fun `ip - x-forwarded-for`() {
        val ip = "127.0.0.1"
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .header("x-forwarded-for", StringJoiner(",").add(ip).add("192.168.1.1").toString())
                .build()
        assertThat(request.ip()).isEqualTo(ip)
    }

    @Test
    fun `ip - remote address`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .remoteAddress(InetSocketAddress("127.0.0.1", 8080))
                .build()
        assertThat(request.ip()).isNotEmpty()
    }

    @Test
    fun traceId() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .build()
        val t1 = request.traceId()
        val t2 = request.traceId()
        assertThat(t1).isEqualTo(t2)
    }

    @Test
    fun `traceId - x-request-id`() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .header("x-request-id", TracingUtils.traceId())
                .build()
        val t1 = request.traceId()
        val t2 = request.traceId()
        assertThat(t1).isEqualTo(t2)
    }

    @Test
    fun packParams() {
        val request = MockServerRequest.builder()
                .uri(URI.create("/test"))
                .queryParam("sort", "name")
                .queryParam("page", "1")
                .queryParam("size", "15")
                .build()

        val packParams = request.packParams()

        val sort = packParams.sort()
        assertThat(sort.orders)
                .isNotEmpty
                .first()
                .extracting(Sort.Order::direction)
                .isEqualTo("asc")

        val p = packParams.page()
        assertThat(p.page).isEqualTo(1)
        assertThat(p.size).isEqualTo(15)
    }
}