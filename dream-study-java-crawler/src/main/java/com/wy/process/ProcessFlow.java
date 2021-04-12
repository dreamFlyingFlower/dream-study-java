package com.wy.process;

import com.wy.enums.CrawlerEnum;
import com.wy.model.ProcessFlowData;

public interface ProcessFlow {

	/**
	 * 处理主业务
	 *
	 * @param processFlowData
	 */
	void handler(ProcessFlowData processFlowData);

	/**
	 * 获取抓取类型
	 *
	 * @return
	 */
	CrawlerEnum.ComponentType getComponentType();

	/**
	 * 获取优先级
	 * 
	 * @return
	 */
	int getPriority();
}