package io.zhudy.kitty.restful

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.core.NestedExceptionUtils

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
abstract class AbstractRestExceptionHandler : RestExceptionHandler {

    companion object {

        @JvmStatic
        val snakeCase = PropertyNamingStrategy.SNAKE_CASE as PropertyNamingStrategy.SnakeCaseStrategy
    }

    /**
     *
     */
    protected fun shouldApplyTo(expectClassName: String, ex: Exception): Boolean {
        val actualClassName = ex.javaClass.name
        return expectClassName == actualClassName
                || expectClassName == NestedExceptionUtils.getRootCause(ex)?.javaClass?.name
    }

}