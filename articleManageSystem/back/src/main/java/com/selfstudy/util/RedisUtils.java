package com.selfstudy.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    public static final String ARTICLE_LIST_KEY = "article_list"; // 文章列表缓存键
    public static final String ARTICLE_DETAIL_KEY = "article_detail:"; // 文章详情缓存键

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void deleteArticleListCache() {
        Set<String> keys = redisTemplate.keys("article_list*"); // 查找所有以 article_list 开头的键
        if (keys != null) {
            redisTemplate.delete(keys); // 删除这些键
        }
    }

    // 设置缓存，支持过期时间
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    // 设置缓存，默认过期时间为 1小时
    public void set(String key, Object value) {
        set(key, value, 1, TimeUnit.HOURS);
    }

    // 获取缓存
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除缓存
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 检查缓存是否存在
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    // 将多个值保存到 Redis
    public void setMultiple(List<String> keys, List<Object> values, long timeout, TimeUnit timeUnit) {
        if (CollectionUtils.isEmpty(keys) || CollectionUtils.isEmpty(values) || keys.size() != values.size()) {
            throw new IllegalArgumentException("Keys and values must not be empty and must have the same size.");
        }
        for (int i = 0; i < keys.size(); i++) {
            set(keys.get(i), values.get(i), timeout, timeUnit);
        }
    }

    // 获取多个缓存
    public List<Object> getMultiple(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    // 删除多个缓存
    public void deleteMultiple(Set<String> keys) {
        redisTemplate.delete(keys);
    }

    // 增加缓存的过期时间
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    // 获取缓存过期时间
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    // 对缓存的数字值执行加1操作
    public void increment(String key, long delta) {
        redisTemplate.opsForValue().increment(key, delta);
    }

    // 对缓存的数字值执行减1操作
    public void decrement(String key, long delta) {
        redisTemplate.opsForValue().increment(key, -delta);
    }

    // 操作 Hash（示例：获取 Hash 值）
    public Object getHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    // 操作 Hash（示例：设置 Hash 值）
    public void setHashValue(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    // 操作 Set（示例：将值加入到 Set）
    public void addToSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    // 操作 Set（示例：获取 Set 中的所有值）
    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }
}
