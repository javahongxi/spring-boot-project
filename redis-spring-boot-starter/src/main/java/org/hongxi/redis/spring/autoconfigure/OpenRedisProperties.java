package org.hongxi.redis.spring.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import redis.clients.jedis.JedisPoolConfig;

import static org.hongxi.spring.boot.common.constants.Constants.OPEN_REDIS_PREFIX;

/**
 * Created by shenhongxi on 2021/3/27.
 */
@Data
@ConfigurationProperties(prefix = OPEN_REDIS_PREFIX)
public class OpenRedisProperties {

    private String address;

    private int timeout = 3000;

    @NestedConfigurationProperty
    private JedisPoolConfig pool;
}
