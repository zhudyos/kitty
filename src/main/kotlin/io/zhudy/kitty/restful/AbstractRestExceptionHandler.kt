package io.zhudy.kitty.restful

import org.springframework.core.NestedExceptionUtils

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
abstract class AbstractRestExceptionHandler : RestExceptionHandler {

    /**
     *
     */
    protected fun shouldApplyTo(expectClassName: String, ex: Exception): Boolean {
        val actualClassName = ex.javaClass.name
        return expectClassName == actualClassName
                || expectClassName == NestedExceptionUtils.getRootCause(ex)?.javaClass?.name
    }

}