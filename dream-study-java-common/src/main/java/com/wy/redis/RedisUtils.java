package com.wy.redis;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.wy.collection.ListTool;
import com.wy.config.RedisConfig;
import com.wy.digest.DigestTool;
import com.wy.enums.RedisKey;

import lombok.extern.slf4j.Slf4j;

/**
 * redis缓存中设置和获取值,key的类型全部都是string,若是需要特殊类型的key,使用不带后缀的set和get
 * 用什么opsFor类型存,就必须用该类型取,否则会报错
 * 所有set方法后带out的代表会设置超时时间,默认是30分钟;不带out的都是没有过期时间的,最好是设置超时,避免内存溢出
 * 
 * @auther 飞花梦影
 * @date 2018-07-23 19:50:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Scope("singleton")
@ConditionalOnBean({ RedisConfig.class, RedisTemplate.class })
@Slf4j
public class RedisUtils {

	/** 使用LUA语法比较redis中的值,并删除当前key,原子性,支持高并发集群 */
	public static final String SCRIPT_COMPARE_AND_DELETE =
			"if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	/**
	 * 有序集合添加
	 *
	 * @param key
	 * @param value
	 * @param scoure
	 */
	public void add(String key, Object value, double scoure) {
		redisTemplate.opsForZSet().add(key, value, scoure);
	}

	/**
	 * 原子比较redis集群中的值并删除redis当前key
	 * 
	 * @param key key
	 * @param value value
	 * @param failCallback 失败回调
	 * @param successCallback 成功回调
	 */
	public void atomicCompareAndDelete(Object key, Object value, Consumer<RedisTemplate<Object, Object>> failCallback,
			Consumer<RedisTemplate<Object, Object>> successCallback) {
		Long result = redisTemplate.execute(new DefaultRedisScript<Long>(SCRIPT_COMPARE_AND_DELETE, Long.class),
				Arrays.asList(key), value);
		if (result == 0L) {
			// 失败
			if (Objects.nonNull(failCallback)) {
				failCallback.accept(redisTemplate);
			}
		} else {
			if (Objects.nonNull(successCallback)) {
				successCallback.accept(redisTemplate);
			}
		}
	}

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

	/**
	 * 清除redis中的所有缓存,若是redis清除某个key时,并没有这个key,返回0
	 * 无论是用delete或者设置key的超时时间都无法清除key是乱码的数据
	 */
	public Long clearAll() {
		return redisTemplate.delete(keys());
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
	 * 从指定的list中删除count个元素,从value第一次出现开始
	 */
	public Long delList(Object key, Long count, Object value) {
		return redisTemplate.opsForList().remove(RedisKey.REDIS_KEY_ARRAY.getKey(key), count, value);
	}

	/**
	 * 删除特殊key指向的hashmap中的某个map中的某个key
	 */
	public Long delMap(Object key, Object... hashKeys) {
		return redisTemplate.opsForHash().delete(RedisKey.REDIS_KEY_MAP.getKey(key), hashKeys);
	}

	/**
	 * 判断redis中是否存在指定key
	 * 
	 * @param key key
	 * @return true->存在;false->不存在
	 */
	public boolean exist(Object key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 获取key指向的值
	 */
	public Object get(Object key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 获取key指向的list集合中的所有元素
	 */
	public List<Object> getList(Object key) {
		return redisTemplate.opsForList().range(RedisKey.REDIS_KEY_ARRAY.getKey(key), 0l, -1);
	}

	/**
	 * 获取key指向的list集合中下标为index的元素
	 */
	public Object getList(Object key, Long index) {
		return redisTemplate.opsForList().index(RedisKey.REDIS_KEY_ARRAY.getKey(key), index);
	}

	/**
	 * 获取key指向的list集合中元素的个数
	 */
	public Long getListSize(Object key) {
		return redisTemplate.opsForList().size(RedisKey.REDIS_KEY_ARRAY.getKey(key));
	}

	/**
	 * 获取key指向的map中的所有值
	 */
	public Map<Object, Object> getMap(Object key) {
		return redisTemplate.opsForHash().entries(RedisKey.REDIS_KEY_MAP.getKey(key));
	}

	/**
	 * 获取key指向的map中的指定hashkey所关联的值
	 */
	public Object getMap(Object key, Object hashKey) {
		return redisTemplate.opsForHash().get(RedisKey.REDIS_KEY_MAP.getKey(key), hashKey);
	}

	/**
	 * 将数字类型的包装类从redis中取出,之后根据传入的数字类型强制转换 clazz可以是Integer.class等继承自Number的子类
	 */
	public <T extends Number> T getNum(String key, Class<T> clazz) {
		return NumberUtils.parseNumber(stringRedisTemplate.opsForValue().get(RedisKey.REDIS_KEY_NUM.getKey(key)),
				clazz);
	}

	/**
	 * 获取key指向的数据,并转换为json输出
	 */
	public Object getObj(String key) {
		return redisTemplate.opsForValue().get(RedisKey.REDIS_KEY_OBJECT.getKey(key));
	}

	/**
	 * 获取key所指向的set中的所有值
	 */
	public Set<Object> getSet(String key) {
		return redisTemplate.opsForSet().members(RedisKey.REDIS_KEY_SET.getKey(key));
	}

	/**
	 * 获取特殊key指向的字符串值
	 */
	public String getStr(String key) {
		return stringRedisTemplate.opsForValue().get(RedisKey.REDIS_KEY_STR.getKey(key));
	}

	/**
	 * 获取key指向的复合类实例数据,并转换为json输出
	 */
	public <T> T getSynthetic(Object key, Class<T> clazz) {
		Object obj = redisTemplate.opsForValue().get(RedisKey.REDIS_KEY_SYNTHETIC.getKey(key));
		return obj == null ? null : JSON.parseObject(obj.toString(), clazz);
	}

	/**
	 * 获取redis缓存中所有的key值
	 */
	public Set<Object> keys() {
		return redisTemplate.keys("*");
	}

	/**
	 * 试用分布式锁,执行方法
	 * 
	 * @param key redis加锁的key
	 * @param function 需要执行的方法
	 * @param t 执行方法的参数
	 * @return 执行成功的返回值
	 */
	public <T, R> R lock(String key, Function<T, R> function, T t) {
		return lock(key, 100l, function, t);
	}

	/**
	 * 使用分布式锁执行方法
	 * 
	 * @param key redis加锁的key
	 * @param timeout 锁过期时间,默认100MS
	 * @param function 需要执行的方法
	 * @param t 执行方法的参数
	 * @return 执行成功的返回值
	 */
	public <T, R> R lock(String key, long timeout, Function<T, R> function, T t) {
		String uuid = DigestTool.uuid();
		// 分布式锁占坑,设置过期时间,必须和加锁一起作为原子性操作
		Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(RedisKey.REDIS_KEY_LOCK.getKey(key), uuid,
				timeout <= 0 ? 100 : timeout, TimeUnit.MILLISECONDS);
		if (lock) {
			try {
				return function.apply(t);
			} finally {
				// 利用redis的脚本功能执行删除的操作,需要原子环境,防止锁刚过期,删除到其他人的锁.0删除失败,1删除成功
				stringRedisTemplate.execute(new DefaultRedisScript<Long>(SCRIPT_COMPARE_AND_DELETE, Long.class),
						Arrays.asList(RedisKey.REDIS_KEY_LOCK.getKey(key)), uuid);
			}
		} else {
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return lock(key, function, t);
		}
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
	 * @param key
	 */
	public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end) {
		return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
	}

	/**
	 * 有序集合获取排名
	 *
	 * @param key 集合名称
	 * @param value 值
	 */
	public Long rank(String key, Object value) {
		return redisTemplate.opsForZSet().rank(key, value);
	}

	/**
	 * 有序集合获取排名
	 *
	 * @param key
	 */
	public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScore(String key, long start, long end) {
		return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, start, end);
	}

	/**
	 * 有序集合获取排名
	 *
	 * @param key
	 */
	public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScore(String key, long start, long end) {
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
	}

	/**
	 * 有序集合添加
	 *
	 * @param key
	 * @param value
	 */
	public Double score(String key, Object value) {
		return redisTemplate.opsForZSet().score(key, value);
	}

	/**
	 * 有序集合添加分数
	 *
	 * @param key
	 * @param value
	 * @param scoure
	 */
	public void scoreIncre(String key, Object value, double scoure) {
		redisTemplate.opsForZSet().incrementScore(key, value, scoure);
	}

	/**
	 * 直接将传入的key-value值缓存到redis中,不超时
	 */
	public void set(Object key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 将数据存入到一个list集合中,该集合中泛型不确定,可存入任何类型
	 * 注意:使用该方法前必须先调用leftPushAll方法或者rightPushAll方法,让redis中有该key,否则报错
	 */
	public void setList(Object key, Long index, Object value) {
		if (redisTemplate.opsForList().size(key) == null) {
			log.error("#####ERRER:redis缓存中没有key值为{}的list集合", key);
			return;
		}
		redisTemplate.opsForList().set(RedisKey.REDIS_KEY_ARRAY.getKey(key), index, value);
	}

	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相反,不过期
	 */
	public void setListLeft(Object key, List<Object> vals) {
		redisTemplate.opsForList().leftPushAll(RedisKey.REDIS_KEY_ARRAY.getKey(key), vals);
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
		redisTemplate.opsForList().rightPushAll(RedisKey.REDIS_KEY_ARRAY.getKey(key), vals);
		setOut(redisTemplate.opsForList().getOperations(), RedisKey.REDIS_KEY_ARRAY.getKey(key), timeout, unit);
	}

	/**
	 * 将整个list存入到redis可操作的缓存中,添加到缓存中元素的顺序和原list中元素的数序相同,不过期
	 */
	public void setListRight(Object key, List<Object> vals) {
		redisTemplate.opsForList().rightPushAll(RedisKey.REDIS_KEY_ARRAY.getKey(key), vals);
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
		redisTemplate.opsForList().rightPushAll(RedisKey.REDIS_KEY_ARRAY.getKey(key), vals);
		setOut(redisTemplate.opsForList().getOperations(), RedisKey.REDIS_KEY_ARRAY.getKey(key), timeout, unit);
	}

	/**
	 * 将一整个map存入到redis缓存中,不过期
	 * 
	 * @param key redis中的key值
	 * @param values 一个map对象
	 */
	public void setMap(Object key, Map<Object, Object> values) {
		redisTemplate.opsForHash().putAll(RedisKey.REDIS_KEY_MAP.getKey(key), values);
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
		redisTemplate.opsForHash().putAll(RedisKey.REDIS_KEY_MAP.getKey(key), values);
		setOut(redisTemplate.opsForHash().getOperations(), RedisKey.REDIS_KEY_MAP.getKey(key), timeout, unit);
	}

	/**
	 * 将数据存入到一个hashmap中,一次存一个键值对,调用该方法前必须调用putall方法,否则redis中没有该key,会报错
	 * 
	 * @param key redis中key值
	 * @param hashKey map中的key值
	 * @param value map中的value值
	 */
	public void setMap(Object key, Object hashKey, Object value) {
		redisTemplate.opsForHash().put(RedisKey.REDIS_KEY_MAP.getKey(key), hashKey, value);
	}

	/**
	 * 将特殊key-value数字类型的包装类存入到redis中,不超时
	 */
	public void setNum(String key, Number value) {
		stringRedisTemplate.opsForValue().set(RedisKey.REDIS_KEY_NUM.getKey(key), value.toString());
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
		stringRedisTemplate.opsForValue().set(RedisKey.REDIS_KEY_NUM.getKey(key), value.toString(), timeout, unit);
	}

	/**
	 * 当redis中没有值时才设置,有值时不设置,默认30S过期
	 * 
	 * @param key key
	 * @param value value
	 * @return true->redis中没有该key,设置成功;false->redis中已经存在该key,设置失败
	 */
	public boolean setNX(Object key, Object value) {
		return setNX(key, value, 30l);
	}

	/**
	 * 当redis中没有值时才设置,有值时不设置
	 * 
	 * @param key key
	 * @param value value
	 * @param duration 过期时间
	 * @return true->redis中没有该key,设置成功;false->redis中已经存在该key,设置失败
	 */
	public boolean setNX(Object key, Object value, Duration duration) {
		return redisTemplate.opsForValue().setIfAbsent(key, value, duration);
	}

	/**
	 * 当redis中没有值时才设置,有值时不设置.默认过期时间单位为秒
	 * 
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间
	 * @return true->redis中没有该key,设置成功;false->redis中已经存在该key,设置失败
	 */
	public boolean setNX(Object key, Object value, long timeout) {
		return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 当redis中没有值时才设置,有值时不设置
	 * 
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间
	 * @param timeUnit 过期时间单位
	 * @return true->redis中没有该key,设置成功;false->redis中已经存在该key,设置失败
	 */
	public boolean setNX(Object key, Object value, long timeout, TimeUnit timeUnit) {
		return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
	}

	/**
	 * 将对象存入到redis中,程序会自动将数据json序列化,不超时
	 */
	public void setObj(String key, Object obj) {
		redisTemplate.opsForValue().set(RedisKey.REDIS_KEY_OBJECT.getKey(key), obj);
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
		redisTemplate.opsForValue().set(RedisKey.REDIS_KEY_OBJECT.getKey(key), value, timeout, unit);
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
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

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
		return redisTemplate.opsForSet().add(RedisKey.REDIS_KEY_SET.getKey(key), values);
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
		Long num = redisTemplate.opsForSet().add(RedisKey.REDIS_KEY_SET.getKey(key), values);
		setOut(redisTemplate.opsForSet().getOperations(), RedisKey.REDIS_KEY_SET.getKey(key), timeout, unit);
		return num;
	}

	/**
	 * 将特殊key值的key-value存到redis缓存中,不超时
	 */
	public void setStr(String key, String value) {
		stringRedisTemplate.opsForValue().set(RedisKey.REDIS_KEY_STR.getKey(key), value);
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
		redisTemplate.opsForValue().set(RedisKey.REDIS_KEY_STR.getKey(key), value, timeout, unit);
	}

	/**
	 * 将实体类bean数据json序列化成字符串之后存入到redis中,主要是用来存储实体类的实例,不超时
	 */
	public <T> void setSynthetic(String key, T t) {
		redisTemplate.opsForValue().set(RedisKey.REDIS_KEY_SYNTHETIC.getKey(key), t);
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
		redisTemplate.opsForValue().set(RedisKey.REDIS_KEY_SYNTHETIC.getKey(key), t, timeout, unit);
	}

	/**
	 * 当redis中有值时设置,没有值时不设置,默认30S过期
	 * 
	 * @param key key
	 * @param value value
	 * @return true->redis中有该key,设置成功;false->redis中不存在该key,设置失败
	 */
	public boolean setXX(Object key, Object value) {
		return setXX(key, value, 30l);
	}

	/**
	 * 当redis中有值时设置,没有值时不设置
	 * 
	 * @param key key
	 * @param value value
	 * @param duration 过期时间
	 * @return true->redis中有该key,设置成功;false->redis中不存在该key,设置失败
	 */
	public boolean setXX(Object key, Object value, Duration duration) {
		return redisTemplate.opsForValue().setIfPresent(key, value, duration);
	}

	/**
	 * 当redis中有值时设置,没有值时不设置.默认过期时间单位为秒
	 * 
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间
	 * @return true->redis中有该key,设置成功;false->redis中不存在该key,设置失败
	 */
	public boolean setXX(Object key, Object value, long timeout) {
		return redisTemplate.opsForValue().setIfPresent(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 当redis中有值时设置,没有值时不设置
	 * 
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间
	 * @param timeUnit 过期时间单位
	 * @return true->redis中有该key,设置成功;false->redis中不存在该key,设置失败
	 */
	public boolean setXX(Object key, Object value, long timeout, TimeUnit timeUnit) {
		return redisTemplate.opsForValue().setIfPresent(key, value, timeout, timeUnit);
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
}