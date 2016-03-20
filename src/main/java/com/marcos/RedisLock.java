package com.marcos;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by marcos on 16/3/20.
 */
public class RedisLock {
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);
    private RedisClientWrapper client;
    private String lockKey;
    private static Object object = new Object();

    public RedisLock(RedisClientWrapper client, String key) {
        this.client = client;
        this.lockKey = key + "_lock";
    }

    public void acquire() {
        acquire(Integer.MAX_VALUE, TimeUnit.SECONDS);
    }

    public boolean acquire(int time, TimeUnit unit) {
        synchronized (object) {
            long orginalTimeout, timeout;
            orginalTimeout = timeout = caculateTime(time, unit);
            while (timeout >= 0) {
                long expires = System.currentTimeMillis() + timeout + 1;
                String expiresStr = String.valueOf(expires);
                if (client.setnx(lockKey, expiresStr) == 1) {
                    client.expire(lockKey, Integer.valueOf("" + timeout));
                    return true;
                }

                String currentValueStr = client.get(lockKey);
                if (StringUtils.isEmpty(currentValueStr)) {
                    continue;
                }
                if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("key：{} 已过锁过期时间将强制获取锁，并重置锁时间。", lockKey);
                    }
                    String oldValueStr = client.getSet(lockKey, expiresStr);
                    if (currentValueStr.equals(oldValueStr)) {
                        expires = System.currentTimeMillis() + orginalTimeout + 1;
                        expiresStr = String.valueOf(expires);
                        client.set(lockKey, expiresStr);
                        return true;
                    } else {
                        client.set(lockKey, oldValueStr);
                        timeout = orginalTimeout;
                    }
                }
                timeout -= 10;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
            return true;
        }

    }

    public boolean release() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("key：{}已清除锁成功", lockKey);
        }
        client.del(lockKey);
        return true;
    }


    private int caculateTime(int time, TimeUnit unit) {
        switch (unit) {
            case DAYS:
                return time * 24 * 60 * 60;
            case HOURS:
                return time * 60 * 60;
            case MINUTES:
                return time * 60;
            case SECONDS:
                return time;
            default:
                return 1;
        }
    }
}
