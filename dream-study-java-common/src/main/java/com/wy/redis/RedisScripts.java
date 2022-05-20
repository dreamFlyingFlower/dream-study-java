package com.wy.redis;

/**
 * Redis Lua脚本
 * 
 * @author 飞花梦影
 * @date 2022-05-20 14:08:47
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface RedisScripts {

	/**
	 * 获得key的值并比较.若从redis获得的值与传递的ARGV[1]的值相同,则删除key,否则直接返回0
	 * 
	 * KEYS[1]:key值; ARGV[1]:key的预期值
	 */
	String SCRIPT_DELETE =
	        "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

	/**
	 * 调用redis的setnx命令设置值,用来做分布式锁
	 * 
	 * KEYS[1]:key值; ARGV[1]:key的预期值; ARGV[2]:过期时间,单位毫秒
	 */
	String SCRIPT_SETNX =
	        "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then redis.call('pexpire',KEYS[1],ARGV[2]) return 1 else return 0 end";

	/**
	 * 延长key的过期时间.若从redis中获得的值与ARGV[1]的值相同,则重新设置过期时间,否则直接返回0
	 * 
	 * KEYS[1]:key值; ARGV[1]:key的预期值; ARGV[2]:重新设置的过期时间,单位毫秒
	 */
	String SCRIPT_EXTEND_EXPIRE_TIME =
	        "if redis.call('get',KEYS[1]) == ARGV[1] then redis.call('pexpire',KEYS[1],ARGV[2])  return 1 else return 0 end ";
}