# Redis分布式锁
## 使用方式

```
    JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
    final RedisClientWrapper clientWrapper = new SimpleRedisClientWrapper(jedisPool);
    RedisLock redisLock = new RedisLock(clientWrapper, "test");
    redisLock.acquire();
    //Doing you business
    redisLock.release();
```
