package com.wy.service;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.wy.model.Search;
import com.wy.repository.SearchRepository;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-08-05 14:52:45
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
public class ElasticRepositoryService {

	@Autowired
	private SearchRepository searchRepository;

	/**
	 * 修改和新增是同一个接口,区分的依据就是id是否为空
	 */
	public void testAdd() {
		this.searchRepository.save(new Search(1l, "zhang3"));
	}

	/**
	 * 删除
	 */
	public void testDelete() {
		this.searchRepository.deleteById(1l);
	}

	/**
	 * 查询单个
	 */
	public void testFind() {
		System.out.println(this.searchRepository.findById(1l).get());
	}

	/**
	 * 自定义查询
	 * 
	 * NativeSearchQueryBuilder:Spring提供的一个查询条件构建器,帮助构建json格式的请求体
	 */
	public void testNative() {
		// 初始化自定义查询对象
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		// 构建查询
		queryBuilder.withQuery(QueryBuilders.matchQuery("username", "冰冰"));
		// 排序
		queryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC));
		// 分页
		queryBuilder.withPageable(PageRequest.of(0, 2));
		// 高亮
		queryBuilder.withHighlightBuilder(new HighlightBuilder().field("username").preTags("<em>").postTags("</em>"));
		// 执行查询,获取分页结果集
		@SuppressWarnings("deprecation")
		Page<Search> userPage = this.searchRepository.search(queryBuilder.build());
		// 总页数
		System.out.println(userPage.getTotalPages());
		// 总记录数
		System.out.println(userPage.getTotalElements());
		// 当前页数据
		System.out.println(userPage.getContent());
	}
}