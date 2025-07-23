package com.hk.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
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

    public Boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    // 判断键是否存在
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置位值
     *
     * @param key    键
     * @param offset 位偏移量
     * @param value  位值 (true=1, false=0)
     * @return 修改前的位值
     */
    public Boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * 获取位值
     *
     * @param key    键
     * @param offset 位偏移量
     * @return 位值 (true=1, false=0)
     */
    public Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 统计位值为1的数量
     *
     * @param key 键
     * @return 1的数量
     */
    public Long bitCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.bitCount(key.getBytes())
        );
    }

    /**
     * 统计范围内位值为1的数量
     *
     * @param key   键
     * @param startBit 起始字节位置
     * @param endBit   结束字节位置
     * @return 1的数量
     */
    public Long bitCount(String key, long startBit, long endBit) {
        // 将位偏移量转换为字节索引
        long startByte = startBit / 8;
        long endByte = endBit / 8;

        // 使用Redis原生bitCount方法（按字节范围）
        Long count = redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.bitCount(key.getBytes(), startByte, endByte)
        );

        // 如果起始位不在字节边界，需要减去多余的部分
        if (startBit % 8 != 0) {
            // 计算起始字节中多余的部分
            long extraStart = startBit % 8;
            for (long i = startBit; i < (startByte + 1) * 8; i++) {
                if (getBit(key, i)) {
                    count--;
                }
            }
        }

        // 如果结束位不在字节边界，需要减去多余的部分
        if (endBit % 8 != 7) {
            // 计算结束字节中多余的部分
            long extraEnd = endBit % 8;
            for (long i = (endByte) * 8; i <= endBit; i++) {
                if (getBit(key, i)) {
                    count--;
                }
            }
        }
        return count;
    }

    /**
     * 执行位运算操作
     *
     * @param op      运算类型 (AND/OR/XOR/NOT)
     * @param destKey 目标键
     * @param srcKeys 源键列表
     * @return 结果长度
     */
    public Long bitOp(RedisStringCommands.BitOperation op, String destKey, String... srcKeys) {
        byte[][] srcBytes = new byte[srcKeys.length][];
        for (int i = 0; i < srcKeys.length; i++) {
            srcBytes[i] = srcKeys[i].getBytes();
        }
        return redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.bitOp(op, destKey.getBytes(), srcBytes)
        );
    }
}