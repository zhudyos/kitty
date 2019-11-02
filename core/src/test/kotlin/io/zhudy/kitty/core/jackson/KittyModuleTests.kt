package io.zhudy.kitty.core.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class KittyModuleTests {

    @Test
    fun serPage() {
        val mapper = ObjectMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        mapper.registerModules(KittyModule())

        val pageable = PageRequest.of(1, 10)
        val page = PageImpl(
                listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"),
                pageable,
                26
        )
        val json = mapper.writeValueAsString(page)
        assertThat(json).isNotEmpty()

        val page2 = PageImpl(
                listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
        )
        val json2 = mapper.writeValueAsString(page2)
        assertThat(json2).isNotEmpty()
    }

}