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

import java.io.CharArrayWriter
import java.io.PrintWriter

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
object RestTracingUtils {

    /**
     *
     */
    const val PROBLEM_MEDIA_TYPE = "application/problem+json"
    /**
     *
     */
    const val HTTP_QUERY_TRACE_ENABLED = "trace"
    /**
     *
     */
    const val TRACE_ENABLED = "restful.print.trace.enabled"
    /**
     *
     */
    const val TRACE_FORCE_DISABLED = "restful.print.trace.force.disabled"

    /**
     *
     */
    fun checkAndPrintStackTrace(enabled: Boolean, ex: Throwable): List<String>? {
        if (System.getProperty(TRACE_FORCE_DISABLED) != null) {
            return null
        }
        if (enabled || System.getProperty(TRACE_ENABLED) != null) {
            return printStackTrace(ex)
        }
        return null
    }

    fun printStackTrace(ex: Throwable): List<String> {
        val cw = CharArrayWriter(256)
        val pw = PrintWriter(cw)
        ex.printStackTrace(pw)

        val sb = StringBuilder(cw.size())
        sb.append(cw.toCharArray())
        return sb.lines()
    }
}