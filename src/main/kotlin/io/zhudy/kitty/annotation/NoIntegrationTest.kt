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
package io.zhudy.kitty.annotation

import org.springframework.context.annotation.Profile

/**
 * [Spring Annotation Programming Model](https://github.com/spring-projects/spring-framework/wiki/spring-annotation-programming-model)
 * 添加注解可在集群测试环境 `integration-test` 中禁用某些功能，作用范围与 [Profile] 一致。
 *
 * @see Profile
 * @author Kevin Zou (kevinz@weghst.com)
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention
@MustBeDocumented
@Profile("!integration-test")
annotation class NoIntegrationTest

