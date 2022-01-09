package com.wy.enums;

import com.wy.config.MessageService;

/**
 * 调度过期策略
 *
 * @author 飞花梦影
 * @date 2022-01-05 15:05:53
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum MisfireStrategyEnum {

	/** 什么都不做 */
	DO_NOTHING(MessageService.getMessage("misfire_strategy_do_nothing")),

	/** 立即执行一次 */
	FIRE_ONCE_NOW(MessageService.getMessage("misfire_strategy_fire_once_now"));

	private String title;

	MisfireStrategyEnum(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public static MisfireStrategyEnum match(String name, MisfireStrategyEnum defaultItem) {
		for (MisfireStrategyEnum item : MisfireStrategyEnum.values()) {
			if (item.name().equals(name)) {
				return item;
			}
		}
		return defaultItem;
	}
}