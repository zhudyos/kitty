package io.zhudy.kitty.restful

import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.ResolvableType
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KClass
import kotlin.streams.toList

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
class RestHandlerExceptionResolver : HandlerExceptionResolver, InitializingBean, ApplicationContextAware {

    private var applicationContext: ApplicationContext? = null
    private val exceptionHandlers = arrayListOf<Pair<KClass<*>, RestExceptionHandler<*>>>()

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun afterPropertiesSet() {
        if (applicationContext == null) {
            throw IllegalStateException("")
        }

        applicationContext!!.getBeanProvider(RestExceptionHandler::class.java)
                .orderedStream()
                .toList()
                .forEach { bean ->
                    val exClass = ResolvableType.forClass(AopUtils.getTargetClass(bean)).generics[0].rawClass
                    exceptionHandlers.add(Pair(exClass.kotlin, bean))
                }
    }

    override fun resolveException(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any?,
            ex: Exception
    ): ModelAndView? {
        var error: RestError

//        for (eh in exceptionHandlers) {
//            if (eh.first.isInstance(ex)) {
//                error = eh.second.handleException(ex)
//            }
//        }
        return null
    }

}