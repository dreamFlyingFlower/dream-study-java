package com.wy.liteflow;

import org.springframework.stereotype.Component;

import com.yomahub.liteflow.core.NodeSwitchComponent;

/**
 * 执行相关业务
 *
 * @author 飞花梦影
 * @date 2024-03-26 13:37:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component("grantScore")
public class GrantScore extends NodeSwitchComponent {

	/**
	 * 执行具体的业务
	 * 
	 * @throws Exception 异常
	 */
	@Override
	public String processSwitch() throws Exception {
		return null;
	}
}