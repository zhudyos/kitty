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

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.restful.AbstractRestExceptionHandler
import io.zhudy.kitty.restful.RestError
import org.springframework.core.NestedExceptionUtils
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
class MissingKotlinParameterExceptionHandler : AbstractRestExceptionHandler() {

    override fun handleException(ex: Exception): RestError? {
        if (shouldApplyTo("com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException", ex)) {
            val e = NestedExceptionUtils.getMostSpecificCause(ex) as MissingKotlinParameterException
            return RestError(
                    status = 400,
                    code = PubBizCodes.C_999.code,
                    message = "缺少参数 ${snakeCase.translate(e.parameter.name)}"
            )
        }
        return null
    }
}