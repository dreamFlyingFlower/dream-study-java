package com.wy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import com.wy.model.Search;

/**
 * {@link ElasticsearchRestTemplate}是RestHighLevel客户端
 *
 * @author 飞花梦影
 * @date 2022-08-05 14:46:29
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
public class ElasticTemplateService {

	@Autowired
	private ElasticsearchRestTemplate restTemplate;

	public void test01() {
		// 创建索引
		this.restTemplate.indexOps(Search.class).create();
		// 创建映射
		this.restTemplate.indexOps(Search.class).createMapping();
		// 删除索引
		this.restTemplate.indexOps(Search.class).delete();
	}
}