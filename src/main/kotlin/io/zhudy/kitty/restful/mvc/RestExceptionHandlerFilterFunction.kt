package io.zhudy.kitty.restful.mvc

import io.zhudy.kitty.restful.RestError
import io.zhudy.kitty.restful.RestExceptionResolver
import io.zhudy.kitty.web.requestId
import org.apache.logging.log4j.LogManager
import org.springframework.web.servlet.function.HandlerFilterFunction
import org.springframework.web.servlet.function.HandlerFunction
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.status
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
class RestExceptionHandlerFilterFunction(
        private val resolver: RestExceptionResolver
) : HandlerFilterFunction<ServerResponse, ServerResponse> {

    companion object {
        private val log = LogManager.getLogger(RestExceptionHandlerFilterFunction::class.java)
    }

    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): ServerResponse {
        return try {
            next.handle(request)
        } catch (ex: Exception) {
            val rs = resolver.resolve(ex) ?: serverError(request, ex)
            rs.path = request.path()
            request.param("debug").ifPresent {
                rs.stacktrace = stacktrace(ex)
            }
            status(rs.status).body(rs)
        }
    }

    private fun serverError(request: ServerRequest, e: Exception): RestError {
        val traceId = request.requestId()
        log.error("[{}] 服务器内部出现错误", traceId, e)

        return RestError(
                traceId = traceId,
                status = 500,
                code = 500,
                message = e.message ?: "server internal error"
        )
    }

    private fun stacktrace(ex: Exception): List<String> {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        ex.printStackTrace(pw)
        return sw.buffer.lines()
    }
}