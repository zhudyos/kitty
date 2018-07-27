package io.zhudy.kitty.domain

/**
 * @author Kevin Zou (kevinz@weghst.com)
 *
 * @property orders 排序字段信息
 */
data class Sort(
        val orders: List<Order>
) {

    /**
     * @property name 排序字段名称
     * @property direction 排序方式 `asc` 升序，`desc` 降序
     */
    data class Order(
            val name: String = "",
            val direction: String = "asc"
    )
}

/**
 * 空排序。
 */
val unsorted = Sort(emptyList())
