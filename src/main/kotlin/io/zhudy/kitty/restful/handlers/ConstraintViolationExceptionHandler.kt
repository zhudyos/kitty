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

import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.toMessage

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
class ConstraintViolationExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("org.valiktor.ConstraintViolationException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as ConstraintViolationException
            return RestError(
                    status = 400,
                    code = PubBizCodes.C_999.code,
                    message = "参数校验错误",
                    details = e.constraintViolations.asSequence().map {
                        ConstraintViolationMessage(it.property, it.value, it.toMessage().message)
                    }.toSet()
            )
        }
        return null
    }

    data class ConstraintViolationMessage(val property: String, val value: Any?, val message: String)
}