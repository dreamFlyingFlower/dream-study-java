package com.wy.shiro.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;

/**
 * 缓存实现类,实现序列 接口方便对象存储于第三方容器(Map存放键值对)
 * 
 * @author 飞花梦影
 * @date 2022-06-22 16:58:55
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class SimpleMapCache implements Cache<Object, Object>, Serializable {

	private static final long serialVersionUID = 8285971053995600221L;

	private final Map<Object, Object> map;

	private final String name;

	public SimpleMapCache(String name, Map<Object, Object> backingMap) {
		if (name == null) {
			throw new IllegalArgumentException("Cache name cannot be null.");
		}
		if (backingMap == null) {
			throw new IllegalArgumentException("Backing map cannot be null.");
		}
		this.name = name;
		this.map = backingMap;
	}

	@Override
	public Object get(Object key) throws CacheException {
		return map.get(key);
	}

	@Override
	public Object put(Object key, Object value) throws CacheException {
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) throws CacheException {
		return map.remove(key);
	}

	@Override
	public void clear() throws CacheException {
		map.clear();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Set<Object> keys() {
		Set<Object> keys = map.keySet();
		if (!keys.isEmpty()) {
			return Collections.unmodifiableSet(keys);
		}
		return Collections.emptySet();
	}

	@Override
	public Collection<Object> values() {
		Collection<Object> values = map.values();
		if (!CollectionUtils.isEmpty(values)) {
			return Collections.unmodifiableCollection(values);
		}
		return Collections.emptySet();
	}

	@Override
	public String toString() {
		return "SimpleMapCache{" + "map=" + map + ", name='" + name + '\'' + '}';
	}
}