package com.example.springbootredisson.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class CacheConfiguration {

	@Autowired
	RedisProperties redisProperties;

	@Configuration
	@ConditionalOnClass({Redisson.class})
	@ConditionalOnExpression("'${spring.redis.mode}'=='single' or '${spring.redis.mode}'=='cluster' or '${spring.redis.mode}'=='sentinel'")
	protected class RedissonSingleClientConfiguration {

		/**
		 * 单机模式 redisson 客户端
		 */
		@Bean
		@ConditionalOnProperty(name = "spring.redis.mode", havingValue = "single")
		RedissonClient redissonSingle() {
			Config config = new Config();
			String node = redisProperties.getSingle().getAddress();
			node = node.startsWith("redis://") ? node : "redis://" + node;
			SingleServerConfig serverConfig = config.useSingleServer()
					.setAddress(node)
					.setTimeout(redisProperties.getPools().getConnTimeout())
					.setConnectionPoolSize(redisProperties.getPools().getSize())
					.setConnectionMinimumIdleSize(redisProperties.getPools().getMinIdle());
			if (StringUtils.isNotBlank(redisProperties.getPassword())) {
				serverConfig.setPassword(redisProperties.getPassword());
			}
			return Redisson.create(config);
		}

		/**
		 * 集群模式的 redisson 客户端
		 *
		 * @return
		 */
		@Bean
		@ConditionalOnProperty(name = "spring.redis.mode", havingValue = "cluster")
		RedissonClient redissonCluster() {
			System.out.println("cluster redisProperties:" + redisProperties.getCluster());

			Config config = new Config();
			String[] nodes = redisProperties.getCluster().getNodes().split(",");
			List<String> newNodes = new ArrayList(nodes.length);
			Arrays.stream(nodes).forEach((index) -> newNodes.add(
					index.startsWith("redis://") ? index : "redis://" + index));

			ClusterServersConfig serverConfig = config.useClusterServers()
					.addNodeAddress(newNodes.toArray(new String[0]))
					.setScanInterval(
							redisProperties.getCluster().getScanInterval())
					.setIdleConnectionTimeout(
							redisProperties.getPools().getSoTimeout())
					.setConnectTimeout(
							redisProperties.getPools().getConnTimeout())
					.setFailedAttempts(
							redisProperties.getCluster().getFailedAttempts())
					.setRetryAttempts(
							redisProperties.getCluster().getRetryAttempts())
					.setRetryInterval(
							redisProperties.getCluster().getRetryInterval())
					.setMasterConnectionPoolSize(redisProperties.getCluster()
							.getMasterConnectionPoolSize())
					.setSlaveConnectionPoolSize(redisProperties.getCluster()
							.getSlaveConnectionPoolSize())
					.setTimeout(redisProperties.getTimeout());
			if (StringUtils.isNotBlank(redisProperties.getPassword())) {
				serverConfig.setPassword(redisProperties.getPassword());
			}
			return Redisson.create(config);
		}

		/**
		 * 哨兵模式 redisson 客户端
		 * @return
		 */

		@Bean
		@ConditionalOnProperty(name = "spring.redis.mode", havingValue = "sentinel")
		RedissonClient redissonSentinel() {
			System.out.println("sentinel redisProperties:" + redisProperties.getSentinel());
			Config config = new Config();
			String[] nodes = redisProperties.getSentinel().getNodes().split(",");
			List<String> newNodes = new ArrayList(nodes.length);
			Arrays.stream(nodes).forEach((index) -> newNodes.add(
					index.startsWith("redis://") ? index : "redis://" + index));

			SentinelServersConfig serverConfig = config.useSentinelServers()
					.addSentinelAddress(newNodes.toArray(new String[0]))
					.setMasterName(redisProperties.getSentinel().getMaster())
					.setReadMode(ReadMode.SLAVE)
					.setFailedAttempts(redisProperties.getSentinel().getFailMax())
					.setTimeout(redisProperties.getTimeout())
					.setMasterConnectionPoolSize(redisProperties.getPools().getSize())
					.setSlaveConnectionPoolSize(redisProperties.getPools().getSize());

			if (StringUtils.isNotBlank(redisProperties.getPassword())) {
				serverConfig.setPassword(redisProperties.getPassword());
			}

			return Redisson.create(config);
		}
	}

	@Bean
	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		return new RedissonConnectionFactory(redisson);
	}

	@Bean("redisTemplate")
	public RedisTemplate getRedisTemplate(RedisConnectionFactory redissonConnectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(redissonConnectionFactory);
		redisTemplate.setValueSerializer(valueSerializer());
		redisTemplate.setKeySerializer(keySerializer());
		redisTemplate.setHashKeySerializer(keySerializer());
		redisTemplate.setHashValueSerializer(valueSerializer());
		return redisTemplate;
	}

	@Bean
	public RedisSerializer keySerializer() {
		return new StringRedisSerializer();
	}

//	@Bean
//	public RedisSerializer valueSerializer() {
//		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//		return jackson2JsonRedisSerializer;
//	}

	@Bean
	public RedisSerializer valueSerializer() {
		return new GenericFastJsonRedisSerializer();
	}

}
