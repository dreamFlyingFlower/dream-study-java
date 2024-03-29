package com.wy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wy.model.Course;
import com.wy.model.query.CourseSearchQuery;
import com.wy.result.Result;

/**
 * 课程案例代码
 *
 * @author 飞花梦影
 * @date 2022-08-03 16:19:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
public class ElasticCourseService {

	@Value("${course.source_field}")
	private String source_field;

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	// 课程搜索
	public Result<List<Course>> list(int page, int size, CourseSearchQuery courseSearchQuery) {
		if (courseSearchQuery == null) {
			courseSearchQuery = new CourseSearchQuery();
		}
		// 创建搜索请求对象
		SearchRequest searchRequest = new SearchRequest("course");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 过虑源字段
		String[] source_field_array = source_field.split(",");
		searchSourceBuilder.fetchSource(source_field_array, new String[] {});
		// 创建布尔查询对象
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 搜索条件
		// 根据关键字搜索
		if (StringUtils.isNotEmpty(courseSearchQuery.getKeyword())) {
			MultiMatchQueryBuilder multiMatchQueryBuilder =
			        QueryBuilders.multiMatchQuery(courseSearchQuery.getKeyword(), "name", "description", "teachplan")
			                .minimumShouldMatch("70%").field("name", 10);
			boolQueryBuilder.must(multiMatchQueryBuilder);
		}
		if (StringUtils.isNotEmpty(courseSearchQuery.getMt())) {
			// 根据一级分类
			boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchQuery.getMt()));
		}
		if (StringUtils.isNotEmpty(courseSearchQuery.getSt())) {
			// 根据二级分类
			boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchQuery.getSt()));
		}
		if (StringUtils.isNotEmpty(courseSearchQuery.getGrade())) {
			// 根据难度等级
			boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchQuery.getGrade()));
		}

		// 设置boolQueryBuilder到searchSourceBuilder
		searchSourceBuilder.query(boolQueryBuilder);
		// 设置分页参数
		if (page <= 0) {
			page = 1;
		}
		if (size <= 0) {
			size = 12;
		}
		// 起始记录下标
		int from = (page - 1) * size;
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(size);

		// 设置高亮
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<font class='eslight'>");
		highlightBuilder.postTags("</font>");
		// 设置高亮字段
		// <font class='eslight'>node</font>学习
		highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
		searchSourceBuilder.highlighter(highlightBuilder);

		searchRequest.source(searchSourceBuilder);

		Result<List<Course>> result = new Result<>();
		List<Course> list = new ArrayList<>();
		try {
			// 2执行搜索
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			// 3获取响应结果
			SearchHits hits = searchResponse.getHits();
			long totalHits = hits.getTotalHits().value;
			// 匹配的总记录数
			// long totalHits = hits.totalHits;
			result.setTotal(totalHits);
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit : searchHits) {
				Course course = new Course();
				// 源文档
				Map<String, Object> sourceAsMap = hit.getSourceAsMap();
				// 取出id
				String id = (String) sourceAsMap.get("id");
				course.setId(id);
				// 取出name
				String name = (String) sourceAsMap.get("name");
				// 取出高亮字段name
				Map<String, HighlightField> highlightFields = hit.getHighlightFields();
				if (highlightFields != null) {
					HighlightField highlightFieldName = highlightFields.get("name");
					if (highlightFieldName != null) {
						Text[] fragments = highlightFieldName.fragments();
						StringBuffer stringBuffer = new StringBuffer();
						for (Text text : fragments) {
							stringBuffer.append(text);
						}
						name = stringBuffer.toString();
					}
				}
				course.setName(name);
				// 图片
				String pic = (String) sourceAsMap.get("pic");
				course.setPic(pic);
				// 价格
				Double price = null;
				try {
					if (sourceAsMap.get("price") != null) {
						price = (Double) sourceAsMap.get("price");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				course.setPrice(price);
				// 旧价格
				Double price_old = null;
				try {
					if (sourceAsMap.get("price_old") != null) {
						price_old = (Double) sourceAsMap.get("price_old");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				course.setPrice_old(price_old);
				// 将course对象放入list
				list.add(course);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		result.setData(list);
		return result;
	}
}