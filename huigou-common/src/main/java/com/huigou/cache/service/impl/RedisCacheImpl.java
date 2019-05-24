package com.huigou.cache.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.huigou.cache.service.ICache;
import com.huigou.util.ClassHelper;
import com.huigou.util.Md5Builder;
import com.huigou.util.StringUtil;

/**
 * Redis缓存 实现
 * 
 * @author xiex
 */
public class RedisCacheImpl implements ICache {
    private RedisTemplate<String, Object> redisTemplate;

    // 缓存名字
    private String name;

    // 失效时间
    private Long liveTime = 0l;

    // 是否压缩key
    private boolean compressed = false;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLiveTime(Long liveTime) {
        this.liveTime = liveTime;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    private Object getf(Object key) {
        final String keyf = obj2Str(key);
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key = keyf.getBytes();
                byte[] value = connection.get(key);
                if (value == null) {
                    return null;
                }
                return toObject(value);
            }
        });
    }

    private void putf(Object key, Object value) {
        final String keyf = obj2Str(key);
        final Object valuef = value;
        final Long expire = this.liveTime != null ? this.liveTime : 0l;
        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyb = keyf.getBytes();
                byte[] valueb = toByteArray(valuef);
                connection.set(keyb, valueb);
                if (expire.compareTo(0l) > 0) {
                    connection.expire(keyb, expire);
                }
                return 1L;
            }
        });
    }

    private String obj2Str(Object key) {
        String keyStr = null;
        if (key instanceof Integer) {
            keyStr = ((Integer) key).toString();
        } else if (key instanceof Long) {
            keyStr = ((Long) key).toString();
        } else {
            keyStr = (String) key;
        }
        if (isCompressed()) {
            // 压缩key
            keyStr = Md5Builder.getMd5(keyStr);
        }
        return String.format("%s:%s", this.name, keyStr);
    }

    /**
     * 描述 : <Object转byte[]>. <br>
     * <p>
     * <使用方法说明>
     * </p>
     * 
     * @param obj
     * @return
     */
    private byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 描述 : <byte[]转Object>. <br>
     * <p>
     * <使用方法说明>
     * </p>
     * 
     * @param bytes
     * @return
     */
    private Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    private void evict(String key) {
        String keyf = obj2Str(key);
        Set<String> keys = redisTemplate.keys(keyf.concat("*"));
        redisTemplate.delete(keys);
    }

    private void clear() {
        String keyf = this.name;
        Set<String> keys = redisTemplate.keys(keyf.concat("*"));
        redisTemplate.delete(keys);
    }

    @Override
    public void put(String cacheKey, Serializable obj) {
        this.putf(cacheKey, obj);
    }

    @Override
    public Object get(String cacheKey) {
        return this.getf(cacheKey);
    }

    @Override
    public <T> T get(String cacheKey, Class<T> cls) {
        Object obj = this.getf(cacheKey);
        if (obj != null) {
            return ClassHelper.convert(obj, cls);
        }
        return null;
    }

    @Override
    public void remove(String key, String separator) {
        String separatorkey = key;
        if (StringUtil.isNotBlank(separator)) {
            separatorkey += separator;
        }
        this.evict(separatorkey);
    }

    @Override
    public void removeAll() {
        this.clear();
    }

    @Override
    public void delete(String key) {
        String keyf = obj2Str(key);
        redisTemplate.delete(keyf);
    }

}
