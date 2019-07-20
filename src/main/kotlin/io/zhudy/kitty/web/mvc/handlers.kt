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
package io.zhudy.kitty.web.mvc

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import io.lettuce.core.api.StatefulRedisConnection
import io.zhudy.kitty.auth.RedisUserContext
import io.zhudy.kitty.auth.UserContext
import io.zhudy.kitty.biz.BizCodeException
import io.zhudy.kitty.biz.PubBizCodes
import io.zhudy.kitty.service.AuthorityService
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.attributeOrNull

/**
 * `access_token` 认证。
 *
 * @author Kevin Zou (kevinz@weghst.com)
 */
fun securityHandle(
        algorithm: Algorithm,
        redisConnection: StatefulRedisConnection<String, String>,
        next: (ServerRequest) -> ServerResponse
) = { request: ServerRequest ->

    val token = request.param("access_token").orElseGet {
        val v = request.headers().header("authorization").firstOrNull()
        if (v != null && v.length >= 7) {
            v.substring(7)
        } else {
            null
        }
    }

    if (token.isNullOrEmpty()) {
        throw BizCodeException(PubBizCodes.C_401, "缺少 access_token 参数")
    }

    val jwt = try {
        JWT.decode(token)
    } catch (e: JWTDecodeException) {
        throw BizCodeException(PubBizCodes.C_401, "错误的 access_token 参数 $token")
    }

    try {
        algorithm.verify(jwt)
    } catch (e: SignatureVerificationException) {
        throw BizCodeException(PubBizCodes.C_401, "access_token signature failed")
    }

    val id = try {
        jwt.id.toInt()
    } catch (e: Exception) {
        throw BizCodeException(PubBizCodes.C_401, "错误的 access_token")
    }

    // 存储 UserContext
    val traceId = request.headers().header("x-request-id").firstOrNull() ?: ""
    val context = RedisUserContext(
            token = token,
            id = id,
            name = jwt.getClaim("name")?.asString() ?: "",
            traceId = traceId,
            redisConnection = redisConnection
    )
    request.attributes()[UserContext.HTTP_REQUEST_ATTRIBUTE_KEY] = context
    next(request)
}

/**
 * 权限认证。
 */
fun authorityHandle(service: AuthorityService, next: (ServerRequest) -> ServerResponse) = { request: ServerRequest ->
    val uc = request.userContext()
    val matchingUri = request.attributeOrNull(RouterFunctions.MATCHING_PATTERN_ATTRIBUTE) as? String
            ?: throw IllegalStateException("not found ${RouterFunctions.MATCHING_PATTERN_ATTRIBUTE}")

    val r = AuthorityService.Request(
            uri = matchingUri,
            method = request.methodName(),
            contentType = request.headers().header("content-type").firstOrNull()
    )
    if (!service.checkAuthority(r, uc)) {
        throw BizCodeException(PubBizCodes.C_403, "您没有权限访问该资源")
    }
    next(request)
}