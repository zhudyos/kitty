package io.zhudy.kitty.auth

/**
 * 用户上下文信息。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
interface UserContext {

    companion object {
        /**
         *
         */
        const val HTTP_REQUEST_ATTRIBUTE_KEY = "zhudy.auth.UserContext"
    }

    /**
     * 访问的令牌。
     */
    val token: String

    /**
     * 用户ID。
     */
    val id: Int

    /**
     * 用户名称。
     */
    val name: String

    /**
     * 用户请求追踪ID。
     */
    val traceId: String

    /**
     * 用户扩展属性。
     */
    val attrs: MutableMap<String, String?>
}