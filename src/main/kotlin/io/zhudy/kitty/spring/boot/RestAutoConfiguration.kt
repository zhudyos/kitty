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
package io.zhudy.kitty.spring.boot

import io.zhudy.kitty.restful.RestHandlerExceptionResolver
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.*
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter
import org.springframework.util.ClassUtils
import kotlin.streams.toList

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Configuration(proxyBeanMethods = false)
class RestAutoConfiguration {

    /**
     *
     */
    @Bean
    @ConditionalOnClass(HttpMessageConverter::class)
    fun messageConverters(converters: ObjectProvider<HttpMessageConverter<*>>): HttpMessageConverters {
        val defaultConverters = arrayListOf<HttpMessageConverter<*>>()
        defaultConverters.add(ByteArrayHttpMessageConverter())
        defaultConverters.add(StringHttpMessageConverter())
        defaultConverters.add(ResourceHttpMessageConverter())
        defaultConverters.add(ResourceRegionHttpMessageConverter())
        defaultConverters.add(AllEncompassingFormHttpMessageConverter())

        val combined = arrayListOf<HttpMessageConverter<*>>()
        val processing = converters.orderedStream().toList().toMutableList()
        loop@ for (defaultConverter in defaultConverters) {
            val iterator = processing.iterator()
            while (iterator.hasNext()) {
                val candidate = iterator.next()
                if (ClassUtils.isAssignableValue(defaultConverter.javaClass, candidate)) {
                    combined.add(candidate)
                    iterator.remove()
                    continue@loop
                }
            }
            combined.add(defaultConverter)
        }
        combined.addAll(processing)
        return HttpMessageConverters(false, combined)
    }

    /**
     *
     */
    @Bean
    fun restHandlerExceptionResolver(messageConverters: HttpMessageConverters): RestHandlerExceptionResolver {
        return RestHandlerExceptionResolver(messageConverters.converters)
    }
}