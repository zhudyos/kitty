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
import org.apache.logging.log4j.LogManager
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * `RESTful` 错误解决器。
 *
 * 解决器会加载所有存在于 classpath 环境的 `META-INF/rest-problem.properties` 文件，设置错误处理器。
 *
 * ```properties
 * ### e.g. rest-problem.properties
 * ### The key is exception class name
 * ### The value is implementation class name of ProblemHandler
 * exception.class.name=io.xx.implementation.XxxProblemHandler
 * ```
 *
 * 同时用户可以通过构造函数定制的错误处理器，该错误处理器优先级别**最高**。
 *
 * @param customProblemHandlers 定制的错误处理器
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
class RestProblemResolver(customProblemHandlers: Map<String, ProblemHandler> = emptyMap()) {

    private val log = LogManager.getLogger(RestProblemResolver::class.java)
    private val problemHandlers = LinkedHashMap<String, ProblemHandler>()

    init {
        // 初始化错误处理器
        // 加载配置文件中预定义的错误处理器
        val classLoader = RestProblemResolver::class.java.classLoader ?: ClassLoader.getSystemClassLoader()
        val resources = classLoader.getResources("META-INF/rest-problem.properties")
        while (resources.hasMoreElements()) {
            val url = resources.nextElement()
            val props = Properties()
            url.openStream().use(props::load)

            props.forEach { k, v ->
                val exceptionName = k.toString()
                val handlerClassName = v.toString()

                try {
                    val c = classLoader.loadClass(handlerClassName)
                    val handler = c.newInstance() as ProblemHandler
                    putProblemHandler(exceptionName, handler)
                } catch (e: ClassNotFoundException) {
                    // ignore
                    log.debug("Not found problem handler class [{}:{}]", exceptionName, handlerClassName)
                }
            }
        }

        // 添加自定义的错误处理器
        customProblemHandlers.forEach(::putProblemHandler)
    }

    /**
     *
     */
    fun resolve(e: Throwable): Problem {
        val cause = rootCause(e)
        if (cause != null) {
            val handler = problemHandlers[cause::class.qualifiedName]
            if (handler != null) {
                val problem = handler.handle(cause)
                if (problem != null) {
                    return problem
                }
            }
        }

        val handler = problemHandlers[e::class.qualifiedName] ?: return handle0(e)
        return handler.handle(e) ?: handle0(e)
    }

    private fun rootCause(e: Throwable): Throwable? {
        // 如果是 BizCodeException 则不获取 root cause
        if (e is BizCodeException) {
            return null
        }

        var rootCause: Throwable? = null
        var cause = e.cause
        while (cause != null && cause != rootCause) {
            rootCause = cause
            cause = cause.cause
        }
        return rootCause
    }

    private fun handle0(e: Throwable): Problem {
        val c500 = BizCode.C500
        return Problem(
                status = c500.status,
                code = c500.code,
                message = c500.message,
                developerMessage = e.message
        )
    }

    private fun putProblemHandler(exceptionName: String, handler: ProblemHandler) {
        require(exceptionName.isNotEmpty()) { "The exception name is empty" }

        val existsHandler = problemHandlers[exceptionName]
        if (existsHandler != null) {
            // 记录日志
            log.warn(
                    "[{}] - [{}:{}] exists problem handler replace to [{}:{}]",
                    exceptionName,
                    existsHandler::class,
                    existsHandler,
                    handler::class,
                    handler
            )
        }
        problemHandlers[exceptionName] = handler
    }

}