package io.zhudy.kitty.service

import io.zhudy.kitty.auth.UserContext

/**
 * 用户权限校验。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
interface AuthorityService {

    /**
     * @property uri 请求的`uri`
     * @property method 请求的`method`
     * @property contentType 请求的`content-type`
     */
    data class Request(
            val uri: String,
            val method: String,
            val contentType: String?
    )

    /**
     * 校验用户是否有指定的访问权限。
     */
    fun checkAuthority(request: Request, uc: UserContext): Boolean
}