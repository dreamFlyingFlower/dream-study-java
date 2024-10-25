package dream.study.common.caffeine;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * Caffeine缓存
 * 
 * <pre>
 * {@link LoadingCache}:一种自动加载的缓存.和普通缓存不同的地方在于,当缓存不存在/缓存已过期时,若调用get(),则会自动调用CacheLoader.load()加载最新值.
 * 调用getAll()将遍历所有的key调用get(),除非实现了CacheLoader.loadAll().使用LoadingCache时,需要指定CacheLoader,并实现其中的load()供缓存缺失时自动加载
 * 在多线程情况下,同时调用get(),则后一线程将被阻塞,直至前一线程更新缓存完成
 * 
 * {@link AsyncCache}:Cache的一个变体,其响应结果均为CompletableFuture,通过这种方式,AsyncCache对异步编程模式进行了适配.
 * 默认情况下,缓存计算使用ForkJoinPool.commonPool()作为线程池,如果想要指定线程池,则可以覆盖并实现Caffeine.executor(Executor).
 * synchronous()提供了阻塞直到异步缓存生成完毕的能力,它将以Cache进行返回
 * 在多线程情况下,同时调用get(key, k -> value)会返回同一个CompletableFuture.由于返回结果本身不进行阻塞,可以自行选择阻塞等待或者非阻塞
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-04-14 20:55:24
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfCaffeine {

	public static void main(String[] args) {
		Cache<String, String> cache = Caffeine.newBuilder().initialCapacity(5)
				// 超出时淘汰
				.maximumSize(10)
				// 设置写缓存后n秒钟过期
				.expireAfterWrite(60, TimeUnit.SECONDS)
				// 设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite.若2者同时存在,以expireAfterWrite为准
				.expireAfterAccess(17, TimeUnit.SECONDS)
				// 监听缓存被移除
				.removalListener((key, val, removalCause) -> {
				})
				// 记录命中
				.recordStats().build();

		// 直接存入,key相同会覆盖
		cache.put("key", "value");
		String key = "test";
		// 类似于Map#computeIfAbsent,存在则直接返回,不存在则进行自定义操作.2者为原子操作
		cache.get(key, k -> {
			System.out.println(k);
			return k;
		});
		// 若存在,则直接返回
		cache.getIfPresent(key);

		LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
				// 创建缓存或者最近一次更新缓存后经过指定时间间隔,刷新缓存;refreshAfterWrite仅支持LoadingCache
				.refreshAfterWrite(10, TimeUnit.SECONDS).expireAfterWrite(10, TimeUnit.SECONDS)
				.expireAfterAccess(10, TimeUnit.SECONDS).maximumSize(10)
				// 根据key查询数据库里面的值
				.build(k -> new Date().toString());
		System.out.println(loadingCache);

		AsyncLoadingCache<String, String> asyncLoadingCache = Caffeine.newBuilder()
				// 创建缓存或者最近一次更新缓存后经过指定时间间隔刷新缓存;仅支持LoadingCache
				.refreshAfterWrite(1, TimeUnit.SECONDS).expireAfterWrite(1, TimeUnit.SECONDS)
				.expireAfterAccess(1, TimeUnit.SECONDS).maximumSize(10)
				// 根据key查询数据库里面的值
				.buildAsync(k -> {
					Thread.sleep(1000);
					return new Date().toString();
				});

		// 异步缓存返回的是CompletableFuture
		CompletableFuture<String> future = asyncLoadingCache.get("1");
		future.thenAccept(System.out::println);
	}
}