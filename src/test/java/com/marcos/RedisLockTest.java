package com.marcos;

import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by marcos on 16/3/20.
 */

public class RedisLockTest {

    @Test
    public void testRedisLockTest() throws InterruptedException {
        //build RedisPool
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
        final RedisClientWrapper clientWrapper = new SimpleRedisClientWrapper(jedisPool);
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 100; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    RedisLock redisLock = new RedisLock(clientWrapper, "test");
                    redisLock.acquire();
                    System.out.println("线程" + Thread.currentThread().getName() + "得到锁" + System.currentTimeMillis());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    redisLock.release();
                }
            });
        }
        executorService.shutdown();
        while (true) {
            Thread.sleep(1000);
        }
    }
}
