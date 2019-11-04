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

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.zhudy.kitty.rest.problem.RestProblem
import io.zhudy.kitty.rest.problem.RestProblemResolver
import io.zhudy.kitty.rest.problem.RestTracingUtils
import io.zhudy.kitty.rest.problem.RestTracingUtils.HTTP_QUERY_TRACE_ENABLED
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.message.MessageFactory2
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerRequest
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * `RESTful` spring webmvc 异常解决器。
 *
 * @see [HandlerExceptionResolver]
 * @author Kevin Zou (kevinz@weghst.com)
 */
class RestHandlerExceptionResolver(
        private val resolver: RestProblemResolver,
        private val mapper: ObjectMapper
) : HandlerExceptionResolver {

    private val log = LogManager.getLogger()

    override fun resolveException(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse, handler: Any?,
                                  ex: Exception): ModelAndView? {
        val request = (httpRequest.getAttribute(RouterFunctions.REQUEST_ATTRIBUTE) as? ServerRequest)
                ?: ServerRequest.create(httpRequest, emptyList())

        val problem = resolver.resolve(ex)

        // include stacktrace
        val printTraceEnabled = request.param(HTTP_QUERY_TRACE_ENABLED).map { true }.orElse(false)
        val restProblem = RestProblem(
                timestamp = Instant.now(),
                traceId = request.traceId,
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

        try {
            httpResponse.characterEncoding = "UTF-8"
            val response = ServletServerHttpResponse(httpResponse)
            response.setStatusCode(HttpStatus.resolve(restProblem.status))
            response.headers.contentType = MediaType.APPLICATION_PROBLEM_JSON
            mapper.writeValue(response.body, restProblem)
            return ModelAndView()
        } catch (e: JsonProcessingException) {
            throw  e
        }
    }

    private fun logRestProblemJson(restProblem: RestProblem) =
            mapper.writeValueAsString(restProblem.copy(developerMessage = null, details = null, stacktrace = null))
}