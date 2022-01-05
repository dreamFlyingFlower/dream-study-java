package com.wy.enums;

import com.wy.config.MessageService;

/**
 * 国际化
 *
 * @author 飞花梦影
 * @date 2022-01-05 14:30:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum ScheduleTypeEnum {

	NONE(MessageService.getMessage("schedule_type_none")),

	/**
	 * schedule by cron
	 */
	CRON(MessageService.getMessage("schedule_type_cron")),

	/**
	 * schedule by fixed rate (in seconds)
	 */
	FIX_RATE(MessageService.getMessage("schedule_type_fix_rate"));

	/**
	 * schedule by fix delay (in seconds)， after the last time
	 */
	/* FIX_DELAY(MessageService.getMessage("schedule_type_fix_delay")) */;

	private String title;

	ScheduleTypeEnum(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public static ScheduleTypeEnum match(String name, ScheduleTypeEnum defaultItem) {
		for (ScheduleTypeEnum item : ScheduleTypeEnum.values()) {
			if (item.name().equals(name)) {
				return item;
			}
		}
		return defaultItem;
	}
}