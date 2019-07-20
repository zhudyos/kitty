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
package io.zhudy.kitty.restful.handlers

import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.servlet.NoHandlerFoundException

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Ordered.LOWEST_PRECEDENCE)
class NoHandlerFoundExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("org.springframework.web.servlet.NoHandlerFoundException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as NoHandlerFoundException
            return RestError(status = 404, code = 404, message = "未发现对应的资源")
        }
        return null
    }
}