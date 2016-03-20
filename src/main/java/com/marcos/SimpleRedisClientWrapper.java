package com.marcos;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by marcos on 16/3/20.
 */
public class SimpleRedisClientWrapper implements RedisClientWrapper {

    public JedisPool jedisPool;

    public SimpleRedisClientWrapper(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public String get(String key) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            return jedis.get(key);
        } finally {
            this.jedisPool.returnResource(jedis);
        }

    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            return jedis.set(key, value);
        } finally {
            this.jedisPool.returnResource(jedis);
        }
    }

    @Override
    public long setnx(String key, String value) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            return jedis.setnx(key, value);
        } finally {
            this.jedisPool.returnResource(jedis);
        }
    }

    @Override
    public long expire(String key, int time) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            return jedis.expire(key, time);
        } finally {
            this.jedisPool.returnResource(jedis);
        }
    }

    @Override
    public String getSet(String key, String value) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            return jedis.getSet(key, value);
        } finally {
            this.jedisPool.returnResource(jedis);
        }
    }

    @Override
    public long del(String key) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            return jedis.del(key);
        } finally {
            this.jedisPool.returnResource(jedis);
        }
    }
}
