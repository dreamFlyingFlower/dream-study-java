package com.wy.liteflow;

import org.springframework.stereotype.Component;

import com.yomahub.liteflow.core.NodeComponent;

/**
 * 执行相关业务
 *
 * @author 飞花梦影
 * @date 2024-03-26 13:20:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component("prepareTrade")
public class PrepareTrade extends NodeComponent {

	/**
	 * 执行具体的业务
	 * 
	 * @throws Exception 异常
	 */
	@Override
	public void process() throws Exception {

	}

	/**
	 * 表示是否进入该节点执行业务逻辑
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isAccess() {
		return super.isAccess();
	}

	/**
	 * 判断在出错的情况下是否继续执行下一个组件,默认false
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isContinueOnError() {
		return super.isContinueOnError();
	}

	/**
	 * 表示是否终止流程,默认为true
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isEnd() {
		return super.isEnd();
	}
}