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
