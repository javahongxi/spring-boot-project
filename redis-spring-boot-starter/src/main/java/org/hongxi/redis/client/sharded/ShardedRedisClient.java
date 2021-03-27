package org.hongxi.redis.client.sharded;

import org.springframework.beans.factory.DisposableBean;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenhongxi on 2018/6/27.
 */
public class ShardedRedisClient implements DisposableBean {

    private final ShardedJedisPool shardedJedisPool;

    public ShardedRedisClient(String address, int timeout, JedisPoolConfig jedisPoolConfig) {
        List<JedisShardInfo> shardInfos = new ArrayList<>();
        String[] addressArr = address.split(",");
        for (String internalAddress : addressArr) {
            String[] infoParams = internalAddress.split(":");
            shardInfos.add(new JedisShardInfo(infoParams[1], Integer.parseInt(infoParams[2]), timeout, infoParams[0]));
        }
        shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, shardInfos);
    }

    @Override
    public void destroy() throws Exception {
        if (shardedJedisPool != null) {
            shardedJedisPool.close();
        }
    }

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }
}
