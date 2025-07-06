package com.hk.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService<T> {

    private final RedisTemplate<String, T> redisTemplate;

    @Autowired
    @SuppressWarnings("unchecked")
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = (RedisTemplate<String, T>) redisTemplate;
    }

    // 设置键值对
    public void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 设置键值对，并设置过期时间
    public void setWithExpire(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 获取键对应的值
    public T get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除键
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    // 设置哈希表中的键值对
    public void putHash(String key, String hashKey, T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    // 获取哈希表中的键对应的值
    @SuppressWarnings("unchecked")
    public T getHash(String key, String hashKey) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    // 获取哈希表中的所有键值对
    public Map<Object, Object> entriesHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    //删除哈希表中的某个key
    public Long deleteHash(String key, String... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    // 在列表左侧添加元素
    public void leftPush(String key, T value) {
        redisTemplate.opsForList().leftPush(key, value);

    }

    // 在列表右侧弹出元素
    public T rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);

    }

    // 获取列表中指定范围的元素
    public List<T> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);

    }

    // 向集合中添加元素
    public void addSet(String key, T... values) {
        redisTemplate.opsForSet().add(key, values);

    }

    // 获取集合中的所有元素
    public Set<T> membersSet(String key) {
        return redisTemplate.opsForSet().members(key);

    }

    // 向有序集合中添加元素
    public void addZSet(String key, T value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);

    }

    // 获取有序集合中指定范围的元素
    public Set<T> rangeZSet(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    // 设置键的过期时间
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    // 判断键是否存在
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}