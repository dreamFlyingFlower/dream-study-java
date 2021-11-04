package com.wy.build;

import java.util.Arrays;

import com.wy.entity.HeavenNineSong;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-11-04 13:43:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class HubeiHeaven implements HeavenBuilder {

	private HeavenNineSong build = new HeavenNineSong();

	@Override
	public void name() {
		build.setName("天行九歌");
	}

	@Override
	public void author() {
		build.setAuthor("湖北-玄机");
	}

	@Override
	public void people() {
		build.setPeople(Arrays.asList("大叔", "二叔", "天明", "高月"));
	}

	@Override
	public HeavenNineSong build() {
		return build;
	}
}