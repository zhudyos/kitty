package io.zhudy.kitty.core.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * [Page] 自定义序列化实现。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
object PageSerializer : StdSerializer<Page<*>>(Page::class.java) {

    class P1(
            val page: Int,
            val size: Int,
            val offset: Long,
            val totalPages: Int,
            val totalItems: Long,
            val items: List<*>
    )

    class P2(
            val items: List<*>
    )

    override fun serialize(value: Page<*>, gen: JsonGenerator, serializers: SerializerProvider) {
        val pa = value.pageable
        if (pa != Pageable.unpaged()) {
            gen.writeObject(P1(
                    page = value.pageable.pageNumber + 1,
                    size = value.pageable.pageSize,
                    offset = value.pageable.offset,
                    totalPages = value.totalPages,
                    totalItems = value.totalElements,
                    items = value.content
            ))
        } else {
            gen.writeObject(P2(value.content))
        }
    }

}