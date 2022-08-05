package com.wy.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.wy.model.Search;

/**
 * 操作es中的数据,可直接使用jpa的ElasticsearchRepository.若不想使用jpa,可使用ElasticsearchTemplate
 * 
 * @author ParadiseWY
 * @date 2019-10-13 15:48:26
 */
public interface SearchRepository extends ElasticsearchRepository<Search, Long> {

	/**
	 * 可以直接在接口上利用注解写sql语句或者直接利用方法名来当做条件查询,此处使用es的query
	 * 
	 * @param q 关键字
	 * @return 结果集
	 */
	Page<Search> findDistinctSearchByTitleContainingOrSummaryContainingOrContentContaining(String q);

	@Query("{\n" + "    \"range\": {\n" + "      \"id\": {\n" + "        \"gte\": \"?0\",\n"
	        + "        \"lte\": \"?1\"\n" + "      }\n" + "    }\n" + "  }")
	List<Search> findByQuery(Integer id1, Integer id2);
}