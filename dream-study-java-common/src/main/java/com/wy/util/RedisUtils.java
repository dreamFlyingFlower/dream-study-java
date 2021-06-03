package com.wy.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.wy.collection.ListTool;
import com.wy.config.RedisConfig;
import com.wy.enums.RedisKey;

/**
 * redis缓存中设置和获取值,key的类型全部都是string,若是需要特殊类型的key,使用不带后缀的set和get
 * 用什么opsFor类型存,就必须用该类型取,否则会报错
 * 所有set方法后带out的代表会设置超时时间,默认是30分钟;不带out的都是没有过期时间的,最好是设置超时,避免内存溢出
 * 
 * @author wanyang 2018年7月23日
 */
@Component
@Scope("singleton")
@ConditionalOnBean({ RedisConfig.class, RedisTemplate.class })
public class RedisUtils {

	protected static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

	@Autowired
	private StringRedisTemplate template;
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	private ValueOperations<Object, Object> valueOperaions;
	@Autowired
	private ListOperations<Object, Object> listOperations;
	@Autowired
	private SetOperations<Object, Object> setOperations;
	@Autowired
	private HashOperations<Object, Object, Object> hashOperations;

	/**
	 * 直接将传入的key-value值缓存到redis中,不超时
	 */
	public void set(Object key, Object value) {
		valueOperaions.set(key, value);
	}

	/**
	 * 直接将传入的key-value缓存到redis中,超时接口
	 */
	public void setOut(Object key, Object value) {
		setOut(key, value, 30, TimeUnit.MINUTES);
	}

	/**
	 * 直接将传入的key-value缓存到redis中,超时接口
	 */
	public void setOut(Object key, Object value, long timeout) {
		setOut(key, value, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 直接将传入的key-value缓存到redis中,超时接口
	 */
	public void setOut(Object key, Object value, long timeout, TimeUnit unit) {
		valueOperaions.set(key, value, timeout, unit);
	}

	/**
	 * 获取key指向的值
	 */
	public Object get(Object key) {
		return valueOperaions.get(key);
	}

	/*-------------------------------------------String start -------------------------------------------*/
	/**
	 * 将特殊key值的key-value存到redis缓存中,不超时
	 */
	public void setStr(String key, String value) {
		template.opsForValue().set(RedisKey.REDIS_KEY_STR.getKey(key), value);
	}

	/**
	 * 将特殊key值的key-value存到redis缓存中,超时
	 */
	public void setStrOut(Object key, Object value) {
		setStrOut(key, value, 30, TimeUnit.MINUTES);
	}

	/**
	 * 将特殊key值的key-value存到redis缓存中,超时
	 */
	public void setStrOut(Object key, Object value, long timeout) {
		setStrOut(key, value, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 将特殊key值的key-value存到redis缓存中,超时
	 */
	public void setStrOut(Object key, Object value, long timeout, TimeUnit unit) {
		valueOperaions.set(RedisKey.REDIS_KEY_STR.getKey(key), value, timeout, unit);
	}

	/**
	 * 获取特殊key指向的字符串值
	 */
	public String getStr(String key) {
		return template.opsForValue().get(RedisKey.REDIS_KEY_STR.getKey(key));
	}
	/*-------------------------------------------String end -------------------------------------------*/

	/*-------------------------------------------number start -------------------------------------------*/
	/**
	 * 将特殊key-value数字类型的包装类存入到redis中,不超时
	 */
	public void setNum(String key, Number value) {
		template.opsForValue().set(RedisKey.REDIS_KEY_NUM.getKey(key), value.toString());
	}

	/**
	 * 将特殊key值的key-value存到redis缓存中,超时
	 */
	public void setNumOut(Object key, Object value) {
		setStrOut(key, value, 30, TimeUnit.MINUTES);
	}

	/**
	 * 将特殊key值的key-value存到redis缓存中,超时
	 */
	public void setNumOut(Object key, Object value, long timeout) {
		setNumOut(key, value, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 将特殊key值的key-value存到redis缓存中,超时
	 */
	public void setNumOut(Object key, Object value, long timeout, TimeUnit unit) {
		template.opsForValue().set(RedisKey.REDIS_KEY_NUM.getKey(key), value.toString(), timeout, unit);
	}

	/**
	 * 将数字类型的包装类从redis中取出,之后根据传入的数字类型强制转换 clazz可以是Integer.class等继承自Number的子类
	 */
	public <T extends Number> T getNum(String key, Class<T> clazz) {
		return NumberUtils.parseNumber(template.opsForValue().get(RedisKey.REDIS_KEY_NUM.getKey(key)), clazz);
	}
	/*-------------------------------------------number end -------------------------------------------*/

	/*-------------------------------------------object start -------------------------------------------*/
	/**
	 * 将对象存入到redis中,程序会自动将数据json序列化,不超时
	 */
	public void setObj(String key, Object obj) {
		valueOperaions.set(RedisKey.REDIS_KEY_OBJECT.getKey(key), obj);
	}

	/**
	 * 设置有过期时间的redis缓存,默认单位是30分钟,超时
	 */
	public void setObjOut(String key, Object value) {
		setObjOut(key, value, 30, TimeUnit.MINUTES);
	}

	/**
	 * 设置有过期时间的redis缓存,默认单位是分钟,超时
	 */
	public void setObjOut(String key, Object value, Long timeout) {
		setObjOut(key, value, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 设置有过期时间的redis缓存,超时
	 */
	public void setObjOut(String key, Object value, long timeout, TimeUnit unit) {
		valueOperaions.set(RedisKey.REDIS_KEY_OBJECT.getKey(key), value, timeout, unit);
	}

	/**
	 * 获取key指向的数据,并转换为json输出
	 */
	public Object getObj(String key) {
		return valueOperaions.get(RedisKey.REDIS_KEY_OBJECT.getKey(key));
	}
	/*-------------------------------------------object end -------------------------------------------*/

	/*-------------------------------------------Synthetic start -------------------------------------------*/
	/**
	 * 将实体类bean数据json序列化成字符串之后存入到redis中,主要是用来存储实体类的实例,不超时
	 */
	public <T> void setSynthetic(String key, T t) {
		valueOperaions.set(RedisKey.REDIS_KEY_SYNTHETIC.getKey(key), t);
	}

	/**
	 * 将实体类bean数据json序列化成字符串之后存入到redis中,主要是用来存储实体类的实例,默认超时30分钟
	 */
	public <T> void setSyntheticOut(String key, T t) {
		setSyntheticOut(key, t, 30);
	}

	/**
	 * 将实体类bean数据json序列化成字符串之后存入到redis中,主要是用来存储实体类的实例,超时
	 */
	public <T> void setSyntheticOut(String key, Object t, long timeout) {
		setSyntheticOut(key, t, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 将实体类bean数据json序列化成字符串之后存入到redis中,主要是用来存储实体类的实例,超时
	 */
	public <T> void setSyntheticOut(String key, T t, long timeout, TimeUnit unit) {
		valueOperaions.set(RedisKey.REDIS_KEY_SYNTHETIC.getKey(key), t, timeout, unit);
	}

	/**
	 * 获取key指向的复合类实例数据,并转换为json输出
	 */
	public <T> T getSynthetic(Object key, Class<T> clazz) {
		Object obj = valueOperaions.get(RedisKey.REDIS_KEY_SYNTHETIC.getKey(key));
		return obj == null ? null : JSON.parseObject(obj.toString(), clazz);
	}
	/*-------------------------------------------Synthetic end -------------------------------------------*/

	/*-------------------------------------------list start -------------------------------------------*/
	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相同,不过期
	 */
	public void setListRight(Object key, List<Object> vals) {
		listOperations.rightPushAll(RedisKey.REDIS_KEY_ARRAY.getKey(key), vals);
	}

	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相同,过期
	 */
	public void setListRightOut(Object key, List<Object> vals) {
		setListRightOut(key, vals, 30);
	}

	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相同,过期
	 */
	public void setListRightOut(Object key, List<Object> vals, long timeout) {
		setListRightOut(key, vals, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相同,过期
	 */
	public void setListRightOut(Object key, List<Object> vals, long timeout, TimeUnit unit) {
		listOperations.rightPushAll(RedisKey.REDIS_KEY_ARRAY.getKey(key), vals);
		setOut(listOperations.getOperations(), RedisKey.REDIS_KEY_ARRAY.getKey(key), timeout, unit);
	}

	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相反,不过期
	 */
	public void setListLeft(Object key, List<Object> vals) {
		listOperations.leftPushAll(RedisKey.REDIS_KEY_ARRAY.getKey(key), vals);
	}
	
	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相反,过期
	 */
	public void setListLeftOut(Object key, List<Object> vals) {
		setListLeftOut(key, vals, 30);
	}

	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相反,过期
	 */
	public void setListLeftOut(Object key, List<Object> vals, long timeout) {
		setListLeftOut(key, vals, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相反,过期
	 */
	public void setListLeftOut(Object key, List<Object> vals, long timeout, TimeUnit unit) {
		listOperations.rightPushAll(RedisKey.REDIS_KEY_ARRAY.getKey(key), vals);
		setOut(listOperations.getOperations(), RedisKey.REDIS_KEY_ARRAY.getKey(key), timeout, unit);
	}

	/**
	 * 将数据存入到一个list集合中,该集合中泛型不确定,可存入任何类型
	 * 注意:使用该方法前必须先调用leftPushAll方法或者rightPushAll方法,让redis中有该key,否则报错
	 */
	public void setList(Object key, Long index, Object value) {
		if (listOperations.size(key) == null) {
			logger.error(String.format("#####ERRER:redis缓存中没有key值为%s的list集合", key));
			return;
		}
		listOperations.set(RedisKey.REDIS_KEY_ARRAY.getKey(key), index, value);
	}

	/**
	 * 获取key指向的list集合中元素的个数
	 */
	public Long getListSize(Object key) {
		return listOperations.size(RedisKey.REDIS_KEY_ARRAY.getKey(key));
	}

	/**
	 * 获取key指向的list集合中的所有元素
	 */
	public List<Object> getList(Object key) {
		return listOperations.range(RedisKey.REDIS_KEY_ARRAY.getKey(key), 0l, -1);
	}

	/**
	 * 获取key指向的list集合中下标为index的元素
	 */
	public Object getList(Object key, Long index) {
		return listOperations.index(RedisKey.REDIS_KEY_ARRAY.getKey(key), index);
	}

	/**
	 * 从指定的list中删除count个元素,从value第一次出现开始
	 */
	public Long delList(Object key, Long count, Object value) {
		return listOperations.remove(RedisKey.REDIS_KEY_ARRAY.getKey(key), count, value);
	}
	/*-------------------------------------------list end -------------------------------------------*/

	/*-------------------------------------------set start -------------------------------------------*/
	/**
	 * 将集合以set的形式存入到redis的可操作缓存中,不超时
	 */
	public Long setSet(Object key, Collection<Object> vals) {
		return setSet(key, vals.toArray());
	}

	/**
	 * 将数据存入到一个set集合中,该集合中泛型不确定,可存入任何类型,不超时
	 */
	public Long setSet(Object key, Object... values) {
		return setOperations.add(RedisKey.REDIS_KEY_SET.getKey(key), values);
	}

	/**
	 * 将集合以set的形式存入到redis的可操作缓存中,超时
	 */
	public Long setSetOut(Object key, Collection<Object> vals) {
		return setSetOut(key, vals.toArray());
	}

	/**
	 * 将数据存入到一个set集合中,该集合中泛型不确定,可存入任何类型,超时
	 */
	public Long setSetOut(Object key, Object... values) {
		return setSetOut(key, 30, values);
	}

	/**
	 * 将数据存入到一个set集合中,该集合中泛型不确定,可存入任何类型,超时
	 */
	public Long setSetOut(Object key, long timeout, Object... values) {
		return setSetOut(key, timeout, TimeUnit.MINUTES, values);
	}

	/**
	 * 将数据存入到一个set集合中,该集合中泛型不确定,可存入任何类型,超时
	 */
	public Long setSetOut(Object key, long timeout, TimeUnit unit, Object... values) {
		Long num = setOperations.add(RedisKey.REDIS_KEY_SET.getKey(key), values);
		setOut(setOperations.getOperations(), RedisKey.REDIS_KEY_SET.getKey(key), timeout, unit);
		return num;
	}

	/**
	 * 获取key所指向的set中的所有值
	 */
	public Set<Object> getSet(String key) {
		return setOperations.members(RedisKey.REDIS_KEY_SET.getKey(key));
	}
	/*-------------------------------------------set end -------------------------------------------*/

	/*-------------------------------------------hashmap start -------------------------------------------*/
	/**
	 * 将一整个map存入到redis缓存中,不过期
	 * 
	 * @param key redis中的key值
	 * @param values 一个map对象
	 */
	public void setMap(Object key, Map<Object, Object> values) {
		hashOperations.putAll(RedisKey.REDIS_KEY_MAP.getKey(key), values);
	}

	/**
	 * 将一整个map存入到redis缓存中,30分钟过期
	 */
	public void setMapOut(Object key, Map<Object, Object> values) {
		setMapOut(key, values, 30);
	}

	/**
	 * 将一整个map存入到redis缓存中,过期
	 */
	public void setMapOut(Object key, Map<Object, Object> values, long timeout) {
		setMapOut(key, values, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 将一整个map存入到redis缓存中,过期
	 */
	public void setMapOut(Object key, Map<Object, Object> values, long timeout, TimeUnit unit) {
		hashOperations.putAll(RedisKey.REDIS_KEY_MAP.getKey(key), values);
		setOut(hashOperations.getOperations(), RedisKey.REDIS_KEY_MAP.getKey(key), timeout, unit);
	}

	/**
	 * 将数据存入到一个hashmap中,一次存一个键值对,调用该方法前必须调用putall方法,否则redis中没有该key,会报错
	 * 
	 * @param key redis中key值
	 * @param hashKey map中的key值
	 * @param value map中的value值
	 */
	public void setMap(Object key, Object hashKey, Object value) {
		hashOperations.put(RedisKey.REDIS_KEY_MAP.getKey(key), hashKey, value);
	}

	/**
	 * 获取key指向的map中的所有值
	 */
	public Map<Object, Object> getMap(Object key) {
		return hashOperations.entries(RedisKey.REDIS_KEY_MAP.getKey(key));
	}

	/**
	 * 获取key指向的map中的指定hashkey所关联的值
	 */
	public Object getMap(Object key, Object hashKey) {
		return hashOperations.get(RedisKey.REDIS_KEY_MAP.getKey(key), hashKey);
	}

	/**
	 * 删除特殊key指向的hashmap中的某个map中的某个key
	 */
	public Long delMap(Object key, Object... hashKeys) {
		return hashOperations.delete(RedisKey.REDIS_KEY_MAP.getKey(key), hashKeys);
	}
	/*-------------------------------------------hashmap end -------------------------------------------*/

	/*------------------------------------------- zset start -------------------------------------------*/
	 /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        redisTemplate.opsForZSet().add(key, value, scoure);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        return redisTemplate.opsForZSet().rangeByScore(key, scoure, scoure1);
    }

    /**
     * 有序集合获取排名
     *
     * @param key 集合名称
     * @param value 值
     */
    public Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key,value);
    }


    /**
     * 有序集合获取排名
     *
     * @param key
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(String key, long start,long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key,start,end);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     */
    public Double zSetScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key,value);
    }


    /**
     * 有序集合添加分数
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void incrementScore(String key, Object value, double scoure) {
        redisTemplate.opsForZSet().incrementScore(key, value, scoure);
    }


    /**
     * 有序集合获取排名
     *
     * @param key
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithScore(String key, long start,long end) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, start, end);
    }

    /**
     * 有序集合获取排名
     *
     * @param key
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }
	/*------------------------------------------- zset end ---------------------------------------------*/

	/*-------------------------------------------过期时间处理 start -------------------------------------------*/
	/**
	 * 通过key过期时间处理
	 */
	public boolean setOut(RedisOperations<Object, Object> redisOperations, Object key) {
		return setOut(redisOperations, key, 30);
	}

	/**
	 * 通过key过期时间处理
	 */
	public boolean setOut(RedisOperations<Object, Object> redisOperations, Object key, long timeout) {
		return setOut(redisOperations, key, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 通过key过期时间处理
	 */
	public boolean setOut(RedisOperations<Object, Object> redisOperations, Object key, long timeout, TimeUnit unit) {
		return redisOperations.expire(key, timeout, unit);
	}
	/*-------------------------------------------过期时间处理 end -------------------------------------------*/

	/*-------------------------------------------clear start -------------------------------------------*/
	/**
	 * 清除单个直接以key值存入的缓存
	 */
	public boolean clear(Object key) {
		return redisTemplate.delete(key);
	}

	/**
	 * 清除所有直接以key值存入的缓存
	 */
	public Long clear(Collection<Object> keys) {
		return redisTemplate.delete(keys);
	}

	private Long handlerKey(RedisKey uniqueKey, Collection<Object> keys) {
		if (ListTool.isEmpty(keys)) {
			return 0l;
		}
		for (Object key : keys) {
			key = uniqueKey.getKey(key);
		}
		return redisTemplate.delete(keys);
	}

	/**
	 * 清除redis中单个特殊key指向的string的缓存
	 */
	public boolean clearStr(Object key) {
		return redisTemplate.delete(RedisKey.REDIS_KEY_STR.getKey(key));
	}

	/**
	 * 清除redis中所有的特殊key指向的string的缓存
	 */
	public Long clearStr(Collection<Object> keys) {
		return handlerKey(RedisKey.REDIS_KEY_STR, keys);
	}

	/**
	 * 清除redis中单个特殊key指向的number的缓存
	 */
	public boolean clearNum(Object key) {
		return redisTemplate.delete(RedisKey.REDIS_KEY_NUM.getKey(key));
	}

	/**
	 * 清除redis中所有的特殊key指向的number的缓存
	 */
	public Long clearNum(Collection<Object> keys) {
		return handlerKey(RedisKey.REDIS_KEY_NUM, keys);
	}

	/**
	 * 清除redis中单个特殊key指向的list的缓存
	 */
	public boolean clearList(Object key) {
		return redisTemplate.delete(RedisKey.REDIS_KEY_ARRAY.getKey(key));
	}

	/**
	 * 清除redis中所有的特殊key指向的list的缓存
	 */
	public Long clearList(Collection<Object> keys) {
		return handlerKey(RedisKey.REDIS_KEY_ARRAY, keys);
	}

	/**
	 * 清除redis中单个特殊key指向的set的缓存
	 */
	public boolean clearSet(Object key) {
		return redisTemplate.delete(RedisKey.REDIS_KEY_SET.getKey(key));
	}

	/**
	 * 清除redis中所有的特殊key指向的set的缓存
	 */
	public Long clearSet(Collection<Object> keys) {
		return handlerKey(RedisKey.REDIS_KEY_SET, keys);
	}

	/**
	 * 清除redis中单个特殊key指向的map的缓存
	 */
	public boolean clearMap(Object key) {
		return redisTemplate.delete(RedisKey.REDIS_KEY_MAP.getKey(key));
	}

	/**
	 * 清除redis中所有的特殊key指向的map的缓存
	 */
	public Long clearMap(Collection<Object> keys) {
		return handlerKey(RedisKey.REDIS_KEY_MAP, keys);
	}

	/**
	 * 清除redis中单个的特殊key指向的object的缓存
	 */
	public boolean clearObj(Object key) {
		return redisTemplate.delete(RedisKey.REDIS_KEY_SYNTHETIC.getKey(key));
	}

	/**
	 * 清除redis中所有的特殊key指向的object的缓存
	 */
	public Long clearObj(Collection<Object> keys) {
		return handlerKey(RedisKey.REDIS_KEY_SYNTHETIC, keys);
	}

	/**
	 * 清除redis中的所有缓存,若是redis清除某个key时,并没有这个key,返回0
	 * 无论是用delete或者设置key的超时时间都无法清除key是乱码的数据
	 */
	public Long clearAll() {
		return redisTemplate.delete(keys());
	}
	/*-------------------------------------------clear end -------------------------------------------*/

	/**
	 * 获取redis缓存中所有的key值
	 */
	public Set<Object> keys() {
		return redisTemplate.keys("*");
	}
}