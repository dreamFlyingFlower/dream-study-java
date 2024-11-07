package dream.study.common.config;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;

/**
 * cahce-redis的使用,redis可以换成ecache或其他类型缓存,查看{CacheProperties}中的cachetype
 * 不论那种cache技术,可以不重写CacheManager方法,也可重写.KeyGenerator方法生成cache的key
 *
 * @author 飞花梦影
 * @date 2023-06-20 13:56:10
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

	/**
	 * 自定义存入到缓存中的key值
	 */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {

			@Override
			public Object generate(Object target, Method method, Object... params) {
				List<String> key = new ArrayList<>();
				key.add(target.getClass().getName());
				key.add(method.getName());
				for (Object obj : params) {
					key.add(obj.toString());
				}
				return String.join("_", key);
			}
		};
	}

	/**
	 * 定义缓存到那种缓存技术中
	 */
	@Bean
	@ConditionalOnMissingBean
	CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
				new Jackson2JsonRedisSerializer<>(Object.class);
		// 解决查询缓存转换异常的问题
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.activateDefaultTyping(new DefaultBaseTypeLimitingValidator(),
				ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		// 配置序列化,解决乱码的问题,过期时间600秒
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofSeconds(600))
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
				.disableCachingNullValues();
		// return RedisCacheManager.create(connectionFactory);
		return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
	}
}