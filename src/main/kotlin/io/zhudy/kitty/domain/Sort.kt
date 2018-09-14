package io.zhudy.kitty.domain

/**
 * 排序实体类型。
 *
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
 * 无排序。
 */
val unsorted = Sort(emptyList())

/**
 * 解析排序字符串。
 */
fun parseSort(sort: String?): Sort {
    if (sort == null || sort.isEmpty()) {
        return unsorted
    }

    val orders = arrayListOf<Sort.Order>()
    sort.split(",").forEach {
        if (it.isNotEmpty()) {
            if (it.elementAt(0) == '-') {
                orders.add(Sort.Order(
                        name = it.substring(1),
                        direction = "desc"
                ))
            } else {
                orders.add(Sort.Order(name = it))
            }
        }
    }
    return Sort(orders)
}