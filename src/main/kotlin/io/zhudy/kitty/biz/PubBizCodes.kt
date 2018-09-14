package io.zhudy.kitty.biz

/**
 * 公共错误码。
 */
enum class PubBizCodes(override val code: Int, override val msg: String, override val status: Int = 400) : BizCode {
    /**
     * 未知的系统错误。
     */
    C_0(0, "未知错误"),
    /**
     * 未认证。
     */
    C_401(401, "未认证", 401),
    C_403(403, "拒绝访问", 403),
    C_404(404, "未找到资源", 404),
    /**
     * 服务器内部错误。
     */
    C_500(500, "服务器内部错误", 500),
    C_996(996, "不是可选的枚举值"),
    C_997(997, "获取第三方数据失败"),
    /**
     * HTTP Request 参数错误。
     */
    C_998(998, "HTTP Request 参数类型错误"),
    C_999(999, "HTTP Request 参数错误"),
    ;
}