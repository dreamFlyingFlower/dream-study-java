package com.wy.crl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.model.Search;
import com.wy.repository.SearchRepository;
import com.wy.result.Result;

/**
 * 使用elasticsearch进行检索
 * 
 * @author ParadiseWY
 * @date 2019-04-23 15:37:19
 * @git {@link https://github.com/mygodness100}
 */
@RestController
@RequestMapping("elasticSearch")
public class ElasticSearchCrl {

	// @Autowired
	// private ElasticsearchClient client;
	// @Autowired
	// private ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	private SearchRepository repository;

	@GetMapping("search")
	public Result<?> search() {
		// 从es中查询数据
		Optional<Search> optional = repository.findById(1l);
		// 保存数据到es中,user类删必须添加注解Document,该注解是属于es的,并非jdk的Documented
		// Document的属性中,indexName为索引的名称(已废弃),type为类型
		repository.save(new Search());
		return Result.ok(optional.get());
	}
}