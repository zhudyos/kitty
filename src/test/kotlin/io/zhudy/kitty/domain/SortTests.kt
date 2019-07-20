package io.zhudy.kitty.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class SortTests {

    @Test
    fun sort() {
        val sort = Sort.parse("age")
        val first = sort.orders.first()
        assertThat(first.name).isEqualTo("age")
        assertThat(first.direction).isEqualTo(Sort.DIRECTION_ASC)
    }

    @Test
    fun `sort desc`() {
        val sort = Sort.parse("-age")
        val first = sort.orders.first()
        assertThat(first.name).isEqualTo("age")
        assertThat(first.direction).isEqualTo(Sort.DIRECTION_DESC)
    }

    @Test
    fun unsort() {
        val sort = Sort.parse(null)
        assertThat(sort).isEqualTo(Sort.unsorted)
    }

    @Test
    fun unsort2() {
        val sort = Sort.parse("")
        assertThat(sort).isEqualTo(Sort.unsorted)
    }
}