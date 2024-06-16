package com.wy.redis.hotkey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis实现简单的热搜
 *
 * @author 飞花梦影
 * @date 2024-01-04 16:11:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class HotkeyManager {

	/**
	 * 分隔符号
	 */
	private static final String SPLIT = ":";

	private static final String SEARCH = "search";

	private static final String SEARCH_HISTORY = "search-history";

	private static final String HOT_SEARCH = "hot-search";

	private static final String SEARCH_TIME = "search-time";

	/**
	 * 取热搜前几名返回
	 */
	private static final Integer HOT_SEARCH_NUMBER = 9;

	/**
	 * 多少时间内的搜索记录胃热搜
	 */
	private static final Long HOT_SEARCH_TIME = 30 * 24 * 60 * 60L;

	@Resource
	private StringRedisTemplate redisSearchTemplate;

	/**
	 * 每个用户的个人搜索记录hash
	 */
	public static String getSearchHistoryKey(String userId) {
		return SEARCH + SPLIT + SEARCH_HISTORY + SPLIT + userId;
	}

	/**
	 * 总的热搜zset
	 */
	public static String getHotSearchKey() {
		return SEARCH + SPLIT + HOT_SEARCH;
	}

	/**
	 * 每个搜索记录的时间戳记录：key-value
	 */
	public static String getSearchTimeKey(String searchKey) {
		return SEARCH + SPLIT + SEARCH_TIME + SPLIT + searchKey;
	}

	/**
	 * 新增一条该userid用户在搜索栏的历史记录
	 */
	public Long addSearchHistoryByUserId(String userId, String searchKey) {
		try {
			String redisKey = getSearchHistoryKey(userId);
			// 如果存在这个key
			boolean b = Boolean.TRUE.equals(redisSearchTemplate.hasKey(redisKey));
			if (b) {
				// 获取这个关键词hash的值,有就返回,没有就新增
				Object hk = redisSearchTemplate.opsForHash().get(redisKey, searchKey);
				if (hk != null) {
					return 1L;
				} else {
					redisSearchTemplate.opsForHash().put(redisKey, searchKey, "1");
				}
			} else {
				// 没有这个关键词就新增
				redisSearchTemplate.opsForHash().put(redisKey, searchKey, "1");
			}
			return 1L;
		} catch (Exception e) {
			log.error("redis发生异常,异常原因：", e);
			return 0L;
		}
	}

	/**
	 * 删除个人历史数据
	 */
	public Long delSearchHistoryByUserId(String userId, String searchKey) {
		try {
			String redisKey = getSearchHistoryKey(userId);
			// 删除这个用户的关键词记录
			return redisSearchTemplate.opsForHash().delete(redisKey, searchKey);
		} catch (Exception e) {
			log.error("redis发生异常,异常原因：", e);
			return 0L;
		}
	}

	/**
	 * 获取个人历史数据列表
	 */
	public List<String> getSearchHistoryByUserId(String userId) {
		try {
			List<String> stringList = null;
			String redisKey = getSearchHistoryKey(userId);
			// 判断存不存在
			boolean b = Boolean.TRUE.equals(redisSearchTemplate.hasKey(redisKey));
			if (b) {
				stringList = new ArrayList<>();
				// 逐个扫描,ScanOptions.NONE为获取全部键对,ScanOptions.scanOptions().match("map1").build()
				// 匹配获取键位map1的键值对,不能模糊匹配
				Cursor<Map.Entry<Object, Object>> cursor =
						redisSearchTemplate.opsForHash().scan(redisKey, ScanOptions.NONE);
				while (cursor.hasNext()) {
					Map.Entry<Object, Object> map = cursor.next();
					String key = map.getKey().toString();
					stringList.add(key);
				}
				return stringList;
			}
			return null;
		} catch (Exception e) {
			log.error("redis发生异常,异常原因：", e);
			return null;
		}
	}

	/**
	 * 根据searchKey搜索其相关最热的前十名 (如果searchKey为null空,则返回redis存储的前十最热词条)
	 */
	public List<String> getHotList(String searchKey) {
		try {
			Long now = System.currentTimeMillis();
			List<String> result = new ArrayList<>();
			ZSetOperations<String, String> zSetOperations = redisSearchTemplate.opsForZSet();
			ValueOperations<String, String> valueOperations = redisSearchTemplate.opsForValue();
			Set<String> value = zSetOperations.reverseRangeByScore(getHotSearchKey(), 0, Double.MAX_VALUE);
			// key不为空的时候 推荐相关的最热前十名
			if (StrHelper.isNotBlank(searchKey)) {
				for (String val : value) {
					if (StrHelper.containsIgnoreCase(val, searchKey)) {
						// 只返回最热的前十名
						if (result.size() > HOT_SEARCH_NUMBER) {
							break;
						}
						Long time = Long.valueOf(Objects.requireNonNull(valueOperations.get(val)));
						// 返回最近一个月的数据
						if ((now - time) < HOT_SEARCH_TIME) {
							result.add(val);
						} else {// 时间超过一个月没搜索就把这个词热度归0
							zSetOperations.add(getHotSearchKey(), val, 0);
						}
					}
				}
			} else {
				for (String val : value) {
					// 只返回最热的前十名
					if (result.size() > HOT_SEARCH_NUMBER) {
						break;
					}
					Long time = Long.valueOf(Objects.requireNonNull(valueOperations.get(val)));
					// 返回最近一个月的数据
					if ((now - time) < HOT_SEARCH_TIME) {
						result.add(val);
					} else {
						// 时间超过一个月没搜索就把这个词热度归0
						zSetOperations.add(getHotSearchKey(), val, 0);
					}
				}
			}
			return result;
		} catch (Exception e) {
			log.error("redis发生异常,异常原因：", e);
			return null;
		}
	}

	/**
	 * 新增一条热词搜索记录,将用户输入的热词存储下来
	 */
	public int incrementScoreByUserId(String searchKey) {
		Long now = System.currentTimeMillis();
		ZSetOperations<String, String> zSetOperations = redisSearchTemplate.opsForZSet();
		ValueOperations<String, String> valueOperations = redisSearchTemplate.opsForValue();
		List<String> title = new ArrayList<>();
		title.add(searchKey);
		for (int i = 0, length = title.size(); i < length; i++) {
			String tle = title.get(i);
			try {
				if (zSetOperations.score(getHotSearchKey(), tle) <= 0) {
					zSetOperations.add(getHotSearchKey(), tle, 0);
					valueOperations.set(getSearchTimeKey(tle), String.valueOf(now));
				}
			} catch (Exception e) {
				zSetOperations.add(getHotSearchKey(), tle, 0);
				valueOperations.set(getSearchTimeKey(tle), String.valueOf(now));
			}
		}
		return 1;
	}

	/**
	 * 每次点击给相关词searchKey热度 +1
	 */
	public Long incrementScore(String searchKey) {
		try {
			Long now = System.currentTimeMillis();
			ZSetOperations<String, String> zSetOperations = redisSearchTemplate.opsForZSet();
			ValueOperations<String, String> valueOperations = redisSearchTemplate.opsForValue();
			// 没有的话就插入,有的话的直接更新；add是有就覆盖,没有就插入
			zSetOperations.incrementScore(getHotSearchKey(), searchKey, 1);
			valueOperations.getAndSet(getSearchTimeKey(searchKey), String.valueOf(now));
			return 1L;
		} catch (Exception e) {
			log.error("redis发生异常,异常原因：", e);
			return 0L;
		}
	}
}