package io.zhudy.kitty.service

import io.zhudy.kitty.auth.UserContext

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
interface AuthorityService {

    /**
     *
     */
    fun checkAuthority(requestUri: String, uc: UserContext): Boolean

}