/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.autoconfigure.redis;

import java.util.Map;

import reactor.core.publisher.Flux;

import org.springframework.boot.actuate.autoconfigure.health.CompositeReactiveHealthContributorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.redis.RedisReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for
 * {@link RedisReactiveHealthIndicator}.
 *
 * @author Christian Dupuis
 * @author Richard Santana
 * @author Stephane Nicoll
 * @author Mark Paluch
 * @since 2.1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ ReactiveRedisConnectionFactory.class, Flux.class })
@ConditionalOnBean(ReactiveRedisConnectionFactory.class)
@ConditionalOnEnabledHealthIndicator("redis")
@AutoConfigureBefore(HealthContributorAutoConfiguration.class)
@AutoConfigureAfter(RedisReactiveAutoConfiguration.class)
public class RedisReactiveHealthContributorAutoConfiguration extends
		CompositeReactiveHealthContributorConfiguration<RedisReactiveHealthIndicator, ReactiveRedisConnectionFactory> {

	private final Map<String, ReactiveRedisConnectionFactory> redisConnectionFactories;

	RedisReactiveHealthContributorAutoConfiguration(
			Map<String, ReactiveRedisConnectionFactory> redisConnectionFactories) {
		this.redisConnectionFactories = redisConnectionFactories;
	}

	@Bean
	@ConditionalOnMissingBean(name = { "redisReactiveHealthIndicator", "redisReactiveHealthContributor" })
	public ReactiveHealthContributor redisReactiveHealthContributor() {
		return createContributor(this.redisConnectionFactories);
	}

}
