package com.marcos;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by marcos on 16/3/20.
 */
public class ShardedRedisClientWrapper implements RedisClientWrapper {
    public ShardedJedisPool shardedJedisPool = null;

    public ShardedRedisClientWrapper(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    @Override
    public String get(String key) {
        ShardedJedis shardedJedis = this.shardedJedisPool.getResource();
        try {
            return shardedJedis.get(key);
        } finally {
            this.shardedJedisPool.returnResourceObject(shardedJedis);
        }
    }

    @Override
    public String set(String key, String value) {
        ShardedJedis shardedJedis = this.shardedJedisPool.getResource();
        try{
            return shardedJedis.set(key,value);
        }finally {
            this.shardedJedisPool.returnResourceObject(shardedJedis);
        }
    }

    @Override
    public long setnx(String key, String value) {
        ShardedJedis shardedJedis = this.shardedJedisPool.getResource();
        try{
            return shardedJedis.setnx(key, value);
        }finally {
            this.shardedJedisPool.returnResourceObject(shardedJedis);
        }
    }

    @Override
    public long expire(String key,int time) {
        ShardedJedis shardedJedis = this.shardedJedisPool.getResource();
        try{

            return shardedJedis.expire(key, time);
        }finally {
            this.shardedJedisPool.returnResourceObject(shardedJedis);
        }
    }

    @Override
    public String getSet(String key, String value) {
        ShardedJedis shardedJedis = this.shardedJedisPool.getResource();
        try{
            return shardedJedis.getSet(key, value);
        }finally {
            this.shardedJedisPool.returnResourceObject(shardedJedis);
        }
    }

    @Override
    public long del(String key) {
        ShardedJedis shardedJedis = this.shardedJedisPool.getResource();
        try{
            return shardedJedis.del(key);
        }finally {
            this.shardedJedisPool.returnResourceObject(shardedJedis);
        }
    }
}
