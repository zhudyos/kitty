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

import io.zhudy.kitty.core.biz.BizCodeException
import io.zhudy.kitty.rest.problem.Problem
import io.zhudy.kitty.rest.problem.ProblemHandler

/**
 * @see [BizCodeException]
 * @author Kevin Zou (kevinz@weghst.com)
 */
class BizCodeExceptionHandler : ProblemHandler {

    override fun handle(e: Throwable): Problem? {
        if (e is BizCodeException) {
            val bz = e.bizCode
            return Problem(
                    status = bz.status,
                    code = bz.code,
                    message = bz.message,
                    developerMessage = e.message
            )
        }
        return null
    }
}