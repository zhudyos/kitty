package io.zhudy.kitty.biz

/**
 * 业务码接口。
 *
 * @author Kevin Zou <kevinz@weghst.com>
 */
interface BizCode {

    /**
     * 业务码(3000)以内的为保留业务码。
     */
    val code: Int

    /**
     * 描述。
     */
    val msg: String

    /**
     * HTTP Status。
     */
    val status: Int

}