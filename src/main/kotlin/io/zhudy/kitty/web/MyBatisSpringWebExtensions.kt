package io.zhudy.kitty.web

import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.domain.parseSort
import org.apache.ibatis.session.RowBounds
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.paramOrNull

/**
 * 返回 MyBatis 分页参数。
 */
fun ServerRequest.queryPageParam(): RowBounds {
    val page = queryInt("page")
    val size = queryInt("size")
    if (size <= 0 || size > 500) {
        throw BizCodeException(PubBizCodes.C_999, "分页 size 参数必须是大于等于 1 且小于等于 500 的数字")
    }
    return RowBounds((page - 1) * size, size)
}

/**
 * 排序参数。
 */
fun ServerRequest.querySortParam() = parseSort(this.paramOrNull("sort"))