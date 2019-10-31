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

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.zhudy.kitty.rest.problem.RestProblem
import io.zhudy.kitty.rest.problem.RestProblemResolver
import io.zhudy.kitty.rest.problem.RestTracingUtils
import io.zhudy.kitty.rest.problem.RestTracingUtils.HTTP_QUERY_TRACE_ENABLED
import io.zhudy.kitty.rest.problem.RestTracingUtils.PROBLEM_MEDIA_TYPE
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.message.MessageFactory2
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono
import java.time.Instant

/**
 * `RESTful` spring webflux 异常处理器。
 *
 * @see [WebExceptionHandler]
 * @author Kevin Zou (kevinz@weghst.com)
 */
class RestWebExceptionHandler(
        private val resolver: RestProblemResolver,
        private val mapper: ObjectMapper
) : WebExceptionHandler {

    private val log = LogManager.getLogger()
    private val problemMediaType = MediaType.parseMediaType(PROBLEM_MEDIA_TYPE)

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response
        if (response.isCommitted) {
            return Mono.empty()
        }

        val request = exchange.getRequiredAttribute<ServerRequest>(RouterFunctions.REQUEST_ATTRIBUTE)

        // 解决异常错误
        val problem = resolver.resolve(ex)

        // include stacktrace
        val printTraceEnabled = request.queryParam(HTTP_QUERY_TRACE_ENABLED).map { true }.orElse(false)
        val restProblem = RestProblem(
                timestamp = Instant.now(),
                traceId = request.traceId(),
                path = request.path(),
                method = request.methodName(),
                status = problem.status,
                code = problem.code,
                message = problem.message,
                developerMessage = problem.developerMessage,
                details = problem.details,
                stacktrace = RestTracingUtils.checkAndPrintStackTrace(printTraceEnabled, ex)
        )

        if (problem.status >= 500) {
            log.error(logRestProblemJson(restProblem), ex)
        } else {
            log.debug {
                log.getMessageFactory<MessageFactory2>().newMessage(logRestProblemJson(restProblem), ex)
            }
        }

        return try {
            val buffer = response.bufferFactory().wrap(mapper.writeValueAsBytes(restProblem))
            response.headers.contentType = problemMediaType
            response.statusCode = HttpStatus.resolve(problem.status)
            response.writeWith(Mono.just(buffer)).doOnError { DataBufferUtils.release(buffer) }
        } catch (e: JsonProcessingException) {
            Mono.error(e)
        }
    }

    private fun logRestProblemJson(restProblem: RestProblem) =
            mapper.writeValueAsString(restProblem.copy(developerMessage = null, details = null, stacktrace = null))
}