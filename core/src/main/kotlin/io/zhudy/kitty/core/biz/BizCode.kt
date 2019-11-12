/**
 * Copyright 2019-2019 the original author or authors
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
package io.zhudy.kitty.core.biz

/**
 * 业务错误码定义。
 *
 * 错误码规范：
 * - 采用大写 `C` 字母开头
 * - 错误码必须为一个数字
 * - 在同一业务中错误码要保证唯一性
 * - 同一错误码可在多个业务中使用，但是错误类型必须相同
 *
 * 错误码目标：
 * - 客户端能通过错误码快速识别错误原因
 * - 开发人员可以通过错误码快速识别错误原因
 * - 开发人员可以通过错误码快速解决问题
 *
 * **服务端未捕获或未知异常统一返回 [C500] 错误类型，同时需要在日志中记录详细的错误描述及输入参数，便于开发人员后期排查问题。**
 *
 * @author Kevin Zou <kevinz@weghst.com>
 */
interface BizCode {

    /**
     * 错误码。通过业务码客户及开发人员能够快速识别错误原因的**标识**。
     *
     * **`1000` 以内为保留错误码，在扩展时避免使用 `1000` 以内的错误码，避免冲突。**
     */
    val code: Int

    /**
     * 错误码描述。
     */
    val message: String

    /**
     * 状态码。一般表示 `HTTP Status`。
     */
    val status: Int

    /**
     * 通用业务错误码定义。
     *
     * 以下所有的错误都是 `1000` 以内的保留错误码。
     */
    companion object {

        /**
         * 错误的请求。通常是指缺少参数，或者参数不符合接口要求。
         */
        val C400: BizCode = GeneralCode(400, "错误的请求", 400)

        /**
         * 未认证或认证失效，访问资源需要认证校验。
         */
        val C401: BizCode = GeneralCode(401, "未认证或认证失效", 401)

        /**
         * 拒绝访问，没有资源访问权限。
         */
        val C403: BizCode = GeneralCode(403, "拒绝访问", 403)

        /**
         * 未找到指定的资源。
         */
        val C404: BizCode = GeneralCode(404, "未找到资源", 404)

        /**
         * 资源冲突。
         */
        val C409: BizCode = GeneralCode(409, "资源冲突", 409)

        /**
         * 不支持的请求类型。通常指 HTTP `content-type` 错误。
         */
        val C415: BizCode = GeneralCode(415, "不支持的请求类型", 415)

        /**
         * 服务器内部错误，没有捕获的异常或意外的错误。
         */
        val C500: BizCode = GeneralCode(500, "服务器内部错误", 500)

        // ==========================================================================//
        // 800 - 830 数据库相关错误码
        // ==========================================================================//
        /**
         * 主键、唯一索引冲突。
         *
         * 示例：数据库已经存在用户名为 `tom` 的数据，在次提交用户名为 `tom` 数据保存时。
         */
        val C810: BizCode = GeneralCode(800, "主键、唯一键冲突", 409)

        /**
         * 数据修改异常，受影响的行记录数不正确，**事务回滚**。
         *
         * 示例：我们想要修改 1 条数据库记录实际修改了 3 条记录。
         */
        val C811: BizCode = GeneralCode(811, "数据修改异常，受影响的行记录数不正确", 500)

        /**
         * 数据类型错误。
         *
         * 示例：当年龄 `age` 为 `number` 类型时，客户端提交的参数值类型为 `string`。
         */
        val C998: BizCode = GeneralCode(998, "数据类型错误", 400)

        /**
         * 数据格式错误。
         *
         * 示例：当生日 `birthday` 为 `2000-08-12` 格式时，客户端提交的参数格式为 `2000/08/12`。
         */
        val C999: BizCode = GeneralCode(999, "数据格式错误", 400)

    }

    private class GeneralCode(override val code: Int, override val message: String, override val status: Int) : BizCode
}