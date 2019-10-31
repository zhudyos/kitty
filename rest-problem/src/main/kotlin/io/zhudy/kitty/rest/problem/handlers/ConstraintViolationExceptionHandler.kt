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
package io.zhudy.kitty.rest.problem.handlers

import io.zhudy.kitty.core.biz.BizCode
import io.zhudy.kitty.rest.problem.Problem
import io.zhudy.kitty.rest.problem.ProblemHandler
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.toMessage

/**
 * @see [ConstraintViolationException]
 * @author Kevin Zou (kevinz@weghst.com)
 */
class ConstraintViolationExceptionHandler : ProblemHandler {

    override fun handle(e: Throwable): Problem? {
        if (e is ConstraintViolationException) {
            val bz = BizCode.C400
            return Problem(
                    status = bz.status,
                    code = bz.code,
                    message = "参数校验失败",
                    developerMessage = e.message,
                    details = e.constraintViolations.map { it.toMessage() }
            )
        }
        return null
    }
}