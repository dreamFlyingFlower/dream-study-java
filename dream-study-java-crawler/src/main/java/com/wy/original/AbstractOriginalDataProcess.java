package com.wy.original;

import java.util.List;

import com.wy.enums.CrawlerEnum;
import com.wy.model.ParseItem;
import com.wy.model.ProcessFlowData;
import com.wy.process.AbstractProcessFlow;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化URL前置处理器
 */
@Slf4j
public abstract class AbstractOriginalDataProcess extends AbstractProcessFlow {

	/**
	 * 数据处理方法
	 *
	 * @param processFlowData
	 */
	@Override
	public void handler(ProcessFlowData processFlowData) {
		// 初始化URL数据列表
		List<ParseItem> initialDataList = processFlowData.getParseItemList();
		if (!(null != initialDataList && !initialDataList.isEmpty())) {
			// 解析初始URL获取需要抓取的URL链接
			log.info("获取初始化URL列表");
			initialDataList = parseOriginalRequestData(processFlowData);
			log.info("初始化URL列表完成，有效URL数量：{}", initialDataList.size());
			// 初始化方法的一些处理，这里面是一个空方法，没有任何实现
			initialDataListHandel(initialDataList);
		}
		if (null != initialDataList && !initialDataList.isEmpty()) {

			// 数据添加到Spider中
			addSpiderRequest(initialDataList);
		} else {
			log.error("没有有效的初始化URL列表");
		}

		// 后置处理，这里是一个空的实现
		postprocess(processFlowData);
	}

	/**
	 * 后置处理
	 *
	 * @param processFlowData
	 */
	public void postprocess(ProcessFlowData processFlowData) {

	}

	/**
	 * 解析初始的数据
	 *
	 * @return
	 */
	public abstract List<ParseItem> parseOriginalRequestData(ProcessFlowData processFlowData);

	/**
	 * 初始数据存储
	 *
	 * @param initialDataList
	 */
	public void initialDataListHandel(List<ParseItem> initialDataList) {}

	@Override
	public CrawlerEnum.ComponentType getComponentType() {
		return CrawlerEnum.ComponentType.NORMAL;
	}

}
