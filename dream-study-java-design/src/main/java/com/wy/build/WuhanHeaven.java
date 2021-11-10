package com.wy.build;

import java.util.Arrays;

import com.wy.entity.StrollSkyJiuGe;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-11-04 13:40:10
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class WuhanHeaven implements HeavenBuilder {

	private StrollSkyJiuGe build = new StrollSkyJiuGe();

	@Override
	public void name() {
		build.setName("天行九歌");
	}

	@Override
	public void author() {
		build.setAuthor("武汉-玄机");
	}

	@Override
	public void people() {
		build.setPeople(Arrays.asList("大叔", "二叔", "天明"));
	}

	@Override
	public StrollSkyJiuGe build() {
		return build;
	}
}