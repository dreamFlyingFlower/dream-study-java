package com.wy.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

/**
 * SpringBoot整合ElasticSearch
 * 
 * 官方文档:{@link https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html#CO16-3}
 * 
 * @author ParadiseWY
 * @date 2020-12-21 14:02:57
 * @git {@link https://github.com/mygodness100}
 */
@Service
public class ElesticSearchService {

	/**
	 * ES的增删改操作,不使用SpringBoot自带的data-elasticsearch
	 */
	public void operate() {

		try {
			// 设置连接的集群名称
			RestHighLevelClient client = new RestHighLevelClient(RestClient
					.builder(new HttpHost("localhost", 9200, "http"), new HttpHost("localhost", 9201, "http")));

			// 字符串类型提供document源,创建索引的时候同时创建document数据
			// 参数为索引名
			IndexRequest requestString = new IndexRequest("posts");
			// document的id
			requestString.id("1");
			// 数据源
			String jsonString = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\","
					+ "\"message\":\"trying out Elasticsearch\"" + "}";
			requestString.source(jsonString, XContentType.JSON);

			// map形式提供document源,会自动将map格式为json格式,创建索引的时候同时创建document数据
			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("user", "kimchy");
			jsonMap.put("postDate", new Date());
			jsonMap.put("message", "trying out Elasticsearch");
			new IndexRequest("posts").id("1").source(jsonMap);

			// 以XContentBuilder形式提供document源,会自动格式化为json格式,创建索引的时候同时创建document数据
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject();
			// builder.field("user", "kimchy").timeField("postDate", new
			// Date()).field("message",
			// "trying out Elasticsearch");
			{
				builder.field("user", "kimchy");
				builder.timeField("postDate", new Date());
				builder.field("message", "trying out Elasticsearch");
			}
			builder.endObject();
			new IndexRequest("posts").id("1").source(builder);

			// 类似Map类型的键值对document源,可以是任意类型参数,创建索引的时候同时创建document数据
			IndexRequest requestObject = new IndexRequest("posts").id("1").source("user", "kimchy", "postDate",
					new Date(), "message", "trying out Elasticsearch");

			// 路由选项
			requestObject.routing("routing");
			// 超时
			requestObject.timeout(TimeValue.timeValueSeconds(1));
			// 同上,默认是1m,1分钟
			requestObject.timeout("1s");

			// 同步执行,还可以异步执行
			IndexResponse indexResponse = client.index(requestObject, RequestOptions.DEFAULT);
			// 异步执行
			client.indexAsync(requestObject, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {

				/**
				 * 成功的处理方法
				 */
				@Override
				public void onResponse(IndexResponse response) {

				}

				/**
				 * 失败的处理方法
				 */
				@Override
				public void onFailure(Exception e) {

				}
			});

			// 响应的各种状态以及处理
			if (indexResponse.status() == RestStatus.ACCEPTED) {
			}
			// 删除索引,参数为索引名和document的id
			DeleteRequest requestDelete = new DeleteRequest("posts", "1");
			client.delete(requestDelete, RequestOptions.DEFAULT);
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ES的查询操作,不使用SpringBoot自带的data-elasticsearch
	 */
	public void search() {

		try {
			// 连接es
			RestHighLevelClient client = new RestHighLevelClient(RestClient
					.builder(new HttpHost("localhost", 9200, "http"), new HttpHost("localhost", 9201, "http")));
			// 执行查询
			SearchRequest searchRequest = new SearchRequest("posts");
			// 全文检索,也可以利用QueryBuilder自定义检索方式
			SearchSourceBuilder sourceBuilder =
					new SearchSourceBuilder().query(QueryBuilders.matchQuery("全文检索字段", "字段中的内容"));
			// 对所有字段分词查询
			// SearchSourceBuilder sourceBuilder = new
			// SearchSourceBuilder().query(QueryBuilders.queryStringQuery("全文"));

			// 通配符查询:*表示多个任务字符,?表示单个任意字符
			// SearchSourceBuilder sourceBuilder = new
			// SearchSourceBuilder().query(QueryBuilders.wildcardQuery("content", "全"));

			// 词条查询
			// SearchSourceBuilder sourceBuilder = new
			// SearchSourceBuilder().query(QueryBuilders.termQuery("content", "全"));

			// 模糊查询
			// SearchSourceBuilder sourceBuilder = new
			// SearchSourceBuilder().query(QueryBuilders.fuzzyQuery("content", "全"));

			// 分页查询,不传从0开始
			sourceBuilder.from();
			// 分页查询条件,默认10条
			sourceBuilder.size();
			// 聚合
			// 分组
			TermsAggregationBuilder terms = AggregationBuilders.terms("salaryTerms").field("salary");
			sourceBuilder.aggregation(terms);
			// 平均值:取平均值之后的名称,需要进行平均的字段
			sourceBuilder.aggregation(AggregationBuilders.avg("salaryAvg").field("salary"));
			searchRequest.source(sourceBuilder);
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			// 所有查询结果
			SearchHits hits = searchResponse.getHits();
			// 获取命中次数,查询结果有多少对象
			System.out.println("查询结果有:" + hits.getTotalHits() + "条");
			Iterator<SearchHit> iterator = hits.iterator();
			while (iterator.hasNext()) {
				SearchHit searchHit = iterator.next();
				// 每个查询对象
				System.out.println(searchHit.getSourceAsMap());
				// 获取字符串格式打印
				System.out.println(searchHit.getSourceAsString());
			}
			// 获得聚合信息
			Aggregations aggregations = searchResponse.getAggregations();
			// 根据搜索时定义的类型来获取指定聚合数据
			Terms terms2 = aggregations.get("salaryTerms");
			// 获得所有值
			terms2.getBuckets();
			// 关闭连接
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testGet() throws IOException {
		// 设置连接的集群名称
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http"), new HttpHost("localhost", 9201, "http")));

		// 构建get请求
		GetRequest getRequest = new GetRequest("index", "id");

		// 执行请求,获得结果
		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

		System.out.println(getResponse);
		System.out.println(getResponse.getId());
		// 获得结果
		System.out.println(getResponse.getSource());
		// 获得jsonstring数据
		System.out.println(getResponse.getSourceAsString());
	}
}