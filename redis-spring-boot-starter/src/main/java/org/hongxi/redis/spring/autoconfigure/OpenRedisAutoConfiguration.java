package org.hongxi.redis.spring.autoconfigure;

import org.hongxi.redis.client.RedisTemplate;
import org.hongxi.redis.client.sharded.ShardedRedisClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * Created by shenhongxi on 2021/3/27.
 */
@Configuration
@ConditionalOnClass(Jedis.class)
@EnableConfigurationProperties(OpenRedisProperties.class)
public class OpenRedisAutoConfiguration {

    @Bean
    public ShardedRedisClient shardedRedisClient(OpenRedisProperties openRedisProperties) {
        return new ShardedRedisClient(
                openRedisProperties.getAddress(),
                openRedisProperties.getTimeout(),
                openRedisProperties.getPool());
    }

    @Bean
    public RedisTemplate redisTemplate(ShardedRedisClient shardedRedisClient) {
        return new RedisTemplate(shardedRedisClient);
    }
}
