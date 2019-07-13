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