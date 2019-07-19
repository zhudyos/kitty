package io.zhudy.kitty.okhttp3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
internal class MediaTypesTests {

    @Test
    fun getAPPLICATION_JSON() {
        assertThat(MediaTypes.APPLICATION_JSON.subtype()).isEqualTo("json")
    }
}