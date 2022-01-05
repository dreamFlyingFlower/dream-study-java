package com.wy.enums;

import com.wy.config.MessageService;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-01-05 14:46:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum ExecutorRouteStrategyEnum {

	FIRST(MessageService.getMessage("jobconf_route_first")),
	LAST(MessageService.getMessage("jobconf_route_last")),
	ROUND(MessageService.getMessage("jobconf_route_round")),
	RANDOM(MessageService.getMessage("jobconf_route_random")),
	CONSISTENT_HASH(MessageService.getMessage("jobconf_route_consistenthash")),
	LEAST_FREQUENTLY_USED(MessageService.getMessage("jobconf_route_lfu")),
	LEAST_RECENTLY_USED(MessageService.getMessage("jobconf_route_lru")),
	FAILOVER(MessageService.getMessage("jobconf_route_failover")),
	BUSYOVER(MessageService.getMessage("jobconf_route_busyover")),
	SHARDING_BROADCAST(MessageService.getMessage("jobconf_route_shard"));

	ExecutorRouteStrategyEnum(String title) {
		this.title = title;
	}

	private String title;

	public String getTitle() {
		return title;
	}

	public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem) {
		if (name != null) {
			for (ExecutorRouteStrategyEnum item : ExecutorRouteStrategyEnum.values()) {
				if (item.name().equals(name)) {
					return item;
				}
			}
		}
		return defaultItem;
	}
}