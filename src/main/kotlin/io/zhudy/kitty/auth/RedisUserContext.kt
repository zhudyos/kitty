package io.zhudy.kitty.auth

import io.lettuce.core.api.StatefulRedisConnection

/**
 * 采用 Redis 存储用户上下文信息。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
class RedisUserContext(
        override val token: String,
        override val id: Int,
        override val name: String,
        override val traceId: String,
        private val redisConnection: StatefulRedisConnection<String, String>
) : UserContext {

    companion object {
        const val REDIS_USER_ATTRS_PREFIX = "user:attrs:"
    }

    private val userAttrsKey = REDIS_USER_ATTRS_PREFIX + id

    override val attrs: MutableMap<String, String?> = object : MutableMap<String, String?> {

        override val size: Int get() = TODO("not implemented")

        override fun containsKey(field: String) = redisConnection.sync().hexists(userAttrsKey, field)

        override fun containsValue(value: String?) = TODO("not implemented")

        override fun get(field: String) = redisConnection.sync().hget(userAttrsKey, field)

        override fun isEmpty() = TODO("not implemented")

        override val entries: MutableSet<MutableMap.MutableEntry<String, String?>> = redisConnection.sync().hgetall(userAttrsKey).entries

        override val keys: MutableSet<String> get() = TODO("not implemented")

        override val values: MutableCollection<String?> = TODO("not implemented")

        override fun clear() {
            redisConnection.sync().del(userAttrsKey)
        }

        override fun put(field: String, value: String?): String? {
            redisConnection.sync().hset(userAttrsKey, field, value)
            return value
        }

        override fun putAll(from: Map<out String, String?>) {
            val comms = redisConnection.sync()
            from.forEach {
                comms.hset(userAttrsKey, it.key, it.value)
            }
        }

        override fun remove(field: String): String? {
            val comms = redisConnection.sync()
            val v = get(field)
            comms.hdel(userAttrsKey, field)
            return v
        }
    }
}