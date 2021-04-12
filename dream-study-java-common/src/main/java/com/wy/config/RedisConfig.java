package com.wy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

/**
 * 重写系统自动配置的redis注入,参考{RedisAutoConfiguration},多次注入会抛出异常,本类会先RedisAutoConfiguration对redis进行初始化
 * 
 * spring自动注入序列化对象时使用的jdk的序列化功能,重写之后利用fastjson或jackson对数据序列化
 * 重写使用fastjson序列化或者spring依赖的jackson序列化,这2种方式返回的RedisTemplate类型必须不一样,否则初始化为失败
 * ex:都返回RedisTemplate<Object,Object>时,spring会提示初始化失败,同样的类型初始化了2次,必须返回的类型不一样才可同时使用
 * 最好是只使用一种进行对象的初始化
 * 在使用RedisTemplate的是时候可以实例化2种类型,RedisTemplate<String,String>和任意RedisTemplate<k,v>
 * 
 * 使用 {TestConfiguration}
 * 
 * @author wanyang 2018年7月16日
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

	protected static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	/**
	 * 对象,map等数据类型在redis中的存储需要用到fastjson进行序列化,也可使用spring的依赖jackson
	 */
	@Bean
	@ConditionalOnMissingBean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();

		// 使用fastjson序列化
		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
		// 设置方法参照{RedisTemplate}类中的afterPropertiesSet方法
		// value值的序列化采用fastJsonRedisSerializer
		template.setValueSerializer(fastJsonRedisSerializer);
		template.setHashValueSerializer(fastJsonRedisSerializer);
		// key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		logger.info("||=========== fastjson 实例化redis成功 ===========||");
		init(template);
		return template;
	}
	
	// /**
	// * 同fastjon序列化,使用spring依赖的jackson,若同时和fastjson使用,必须实例化不能类型的RedisTemplate
	// */
	// @Bean
	// public RedisTemplate<String, Map<String,Object>> redisJacksonTemplate(
	// RedisConnectionFactory redisConnectionFactory) {
	// RedisTemplate<String, Map<String,Object>> template = new RedisTemplate<>();
	//
	// // 使用fastjson序列化
	// Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new
	// Jackson2JsonRedisSerializer<Object>(
	// Object.class);
	// // 设置方法参照{RedisTemplate}类中的afterPropertiesSet方法
	// // value值的序列化采用fastJsonRedisSerializer
	// template.setValueSerializer(jackson2JsonRedisSerializer);
	// template.setHashValueSerializer(jackson2JsonRedisSerializer);
	// // key的序列化采用StringRedisSerializer
	// template.setKeySerializer(new StringRedisSerializer());
	// template.setHashKeySerializer(new StringRedisSerializer());
	//
	// template.setConnectionFactory(redisConnectionFactory);
	// logger.info("||=========== jackson 实例化redis成功 ===========||");
	// return template;
	// }
	
	public void init(RedisTemplate<Object, Object> redisTemplate) {
		initVal(redisTemplate);
		initList(redisTemplate);
		initSet(redisTemplate);
		initHashMap(redisTemplate);
		initCluster(redisTemplate);
		initGeo(redisTemplate);
		initHyperLogLog(redisTemplate);
		initZSet(redisTemplate);
	}
	
	@Bean
	public ValueOperations<Object, Object> initVal(RedisTemplate<Object, Object> redisTemplate){
		return redisTemplate.opsForValue();
	}
	
	@Bean
	public ListOperations<Object, Object> initList(RedisTemplate<Object, Object> redisTemplate){
		return redisTemplate.opsForList();
	}
	
	@Bean
	public SetOperations<Object, Object> initSet(RedisTemplate<Object, Object> redisTemplate){
		return redisTemplate.opsForSet();
	}
	
	@Bean
	public HashOperations<Object, Object, Object> initHashMap(RedisTemplate<Object, Object> redisTemplate){
		return redisTemplate.opsForHash();
	}
	
	@Bean
	public ClusterOperations<Object, Object> initCluster(RedisTemplate<Object, Object> redisTemplate){
		return redisTemplate.opsForCluster();
	}
	
	@Bean
	public GeoOperations<Object, Object> initGeo(RedisTemplate<Object, Object> redisTemplate){
		return redisTemplate.opsForGeo();
	}
	
	@Bean
	public HyperLogLogOperations<Object, Object> initHyperLogLog(RedisTemplate<Object, Object> redisTemplate){
		return redisTemplate.opsForHyperLogLog();
	}
	
	@Bean
	public ZSetOperations<Object,Object> initZSet(RedisTemplate<Object, Object> redisTemplate){
		return redisTemplate.opsForZSet();
	}
}