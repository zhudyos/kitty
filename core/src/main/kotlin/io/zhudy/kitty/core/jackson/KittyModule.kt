package io.zhudy.kitty.core.jackson

import com.fasterxml.jackson.databind.module.SimpleModule

/**
 * fasterxml jackson 自定义组件。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
class KittyModule : SimpleModule() {

    init {
        addSerializer(PageSerializer)
    }
}