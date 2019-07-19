package io.zhudy.kitty.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class PageTests {

    @ParameterizedTest
    @ValueSource(ints = [9, 10, 19, 59, 99, 100, 999])
    fun totalPages(totalItems: Int) {
        val p = Pageable(page = 1, size = 10)
        val items = (1..p.size).map { it }
        val page = Page(items = items, totalItems = totalItems, pageable = p)

        val expected = if (totalItems % p.size == 0) {
            totalItems / p.size
        } else {
            totalItems / p.size + 1
        }
        assertThat(page.totalPages).isEqualTo(expected)
    }
}