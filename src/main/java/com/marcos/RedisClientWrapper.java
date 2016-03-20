package com.marcos;

/**
 * Created by marcos on 16/3/20.
 */
public interface RedisClientWrapper {
    public String get(String key);
    public String set(String key,String value);
    public long setnx(String key,String value);
    public long expire(String key,int time);
    public String getSet(String key,String value);
    public long del(String key);
}
