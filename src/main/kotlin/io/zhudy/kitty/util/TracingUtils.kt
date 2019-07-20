/**
 * Copyright 2018-2019 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.zhudy.kitty.util

import java.util.*

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
object TracingUtils {

    /**
     * 自动生成一个 TRACE_ID。
     */
    fun traceId(): String {
        val uuid = UUID.randomUUID()
        return digits(uuid.mostSignificantBits shr 32, 8) +
                digits(uuid.mostSignificantBits shr 16, 4) +
                digits(uuid.mostSignificantBits, 4) +
                digits(uuid.leastSignificantBits shr 48, 4) +
                digits(uuid.leastSignificantBits, 12)
    }

    /** Returns val represented by the specified number of hex digits.  */
    private fun digits(v: Long, digits: Int): String {
        val hi = 1L shl digits * 4
        return java.lang.Long.toHexString(hi or (v and hi - 1)).substring(1)
    }
}
