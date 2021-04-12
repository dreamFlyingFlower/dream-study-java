package com.wy.enums;

public enum RedisKey {

	REDIS_KEY_STR("STRING:%s"), REDIS_KEY_NUM("NUMBER:%s"), REDIS_KEY_ARRAY("ARRAY:%s"), 
	REDIS_KEY_SET("HASHSET:%s"), REDIS_KEY_MAP("HASHMAP:%s"), REDIS_KEY_SYNTHETIC("SYNTHETIC:%s"), 
	REDIS_KEY_OBJECT("OBJECT:%s");

	private String format;

	private RedisKey(String format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return format;
	}
	
	public String getKey(Object key) {
		return String.format(format, key);
	}
}