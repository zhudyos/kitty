package io.zhudy.kitty.domain

import io.zhudy.kitty.biz.BizCodeException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class PageableTests {

    @Test
    fun normal() {
        val p = Pageable(page = 1, size = 10)

        assertThat(p.offset).isEqualTo(0)
        assertThat(p.begin).isEqualTo(1)
        assertThat(p.end).isEqualTo(p.size)
    }

    @Test
    fun `page&size less 1`() {
        assertThatExceptionOfType(BizCodeException::class.java).isThrownBy {
            Pageable(page = 0, size = 10)
        }

        assertThatExceptionOfType(BizCodeException::class.java).isThrownBy {
            Pageable(page = 1, size = 0)
        }
    }
}