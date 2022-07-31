package com.wy.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse.ShardInfo;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
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
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
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

	// 设置连接的集群名称
	private RestHighLevelClient client = new RestHighLevelClient(RestClient
			.builder(new HttpHost("192.168.1.150", 9200, "http"), new HttpHost("192.168.1.150", 9201, "http")));

	/**
	 * ES的增删改操作,不使用SpringBoot自带的data-elasticsearch
	 */
	public void add() {
		try {
			// 字符串类型提供document源,创建索引的时候同时创建document数据
			// 参数为索引名
			IndexRequest requestString = new IndexRequest("index_name");

			// document的id
			requestString.id("1");
			// 数据源
			String jsonString = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\","
					+ "\"message\":\"trying out Elasticsearch\"" + "}";
			// 添加数据,需指定传输的数据类型为JSON字符串
			requestString.source(jsonString, XContentType.JSON);

			// map形式提供document源,会自动将map格式为json格式,创建索引的时候同时创建document数据
			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("user", "kimchy");
			jsonMap.put("postDate", new Date());
			jsonMap.put("message", "trying out Elasticsearch");
			new IndexRequest("index_name").id("1").source(jsonMap);

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
			new IndexRequest("index_name").id("1").source(builder);

			// 类似Map类型的键值对document源,可以是任意类型参数,创建索引的时候同时创建document数据
			IndexRequest requestObject = new IndexRequest("index_name").id("1").source("user", "kimchy", "postDate",
					new Date(), "message", "trying out Elasticsearch");

			// 路由选项
			requestObject.routing("routing");
			// 超时
			requestObject.timeout(TimeValue.timeValueSeconds(1));
			// 同上,默认是1m,1分钟
			requestObject.timeout("1s");

			// 同步执行
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
			// 创建类型
			if (indexResponse.getResult() == Result.CREATED) {
				Result result = indexResponse.getResult();
				System.out.println(result);
			}
			// 更新类型
			if (indexResponse.getResult() == Result.UPDATED) {

			}

			// 分片信息
			ShardInfo shardInfo = indexResponse.getShardInfo();
			if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
				System.out.println("处理成功的分片数小于总分片数,请检查是否有分片故障");
			}
			// 分片失败信息
			if (shardInfo.getFailed() > 0) {
				for (ShardInfo.Failure failure : shardInfo.getFailures()) {
					System.out.println(failure.reason());
				}
			}

			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() throws IOException {
		// 设置连接的集群名称
		RestHighLevelClient client = new RestHighLevelClient(RestClient
				.builder(new HttpHost("192.168.1.150", 9200, "http"), new HttpHost("192.168.1.150", 9201, "http")));

		// map形式提供document源,会自动将map格式为json格式,创建索引的时候同时创建document数据
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("user", "kimchy");
		jsonMap.put("postDate", new Date());
		jsonMap.put("message", "trying out Elasticsearch");
		new IndexRequest("index_name").id("1").source(jsonMap);

		// 更新请求,请求参数同新增,但是需要多传一个_id
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.doc(jsonMap);

		// 设置失败重试次数
		updateRequest.retryOnConflict(3);

		// 同步或异步执行
		UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

		// 没有任何修改
		if (updateResponse.getResult() == Result.NOOP) {

		}
	}

	public void delete() throws IOException {
		// 设置连接的集群名称
		RestHighLevelClient client = new RestHighLevelClient(RestClient
				.builder(new HttpHost("192.168.1.150", 9200, "http"), new HttpHost("192.168.1.150", 9201, "http")));
		// 删除索引,参数为索引名和document的id
		DeleteRequest requestDelete = new DeleteRequest("index_name", "1");
		client.delete(requestDelete, RequestOptions.DEFAULT);
	}

	/**
	 * 批量操作,将增删改查都放在一次请求中,减少网络请求
	 * 
	 * @throws IOException
	 */
	public void bulk() throws IOException {
		// 设置连接的集群名称
		RestHighLevelClient client = new RestHighLevelClient(RestClient
				.builder(new HttpHost("192.168.1.150", 9200, "http"), new HttpHost("192.168.1.150", 9201, "http")));
		// 创建请求
		BulkRequest request = new BulkRequest();
		request.add(new IndexRequest("index_name").id("3").source(XContentType.JSON, "field", "1"));
		request.add(new IndexRequest("index_name").id("4").source(XContentType.JSON, "field", "2"));

		request.add(new UpdateRequest("index_name", "1").doc(XContentType.JSON, "field", "3"));
		request.add(new DeleteRequest("index_name").id("2"));

		BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

		// 获取结果
		for (BulkItemResponse itemResponse : bulkResponse) {
			DocWriteResponse response = itemResponse.getResponse();
			switch (itemResponse.getOpType()) {
			case INDEX:
				IndexResponse indexResponse = (IndexResponse) response;
				System.out.println("INDEX:" + indexResponse.getResult());
				break;
			case CREATE:
				IndexResponse createResponse = (IndexResponse) response;
				System.out.println("CREATE:" + createResponse.getResult());
				break;
			case UPDATE:
				UpdateResponse updateResponse = (UpdateResponse) response;
				System.out.println("UPDATE:" + updateResponse.getResult());
				break;
			case DELETE:
				DeleteResponse deleteResponse = (DeleteResponse) response;
				System.out.println("DELETE:" + deleteResponse.getResult());
				break;
			}
		}
	}

	/**
	 * ES的查询操作,不使用SpringBoot自带的data-elasticsearch
	 */
	public void search() {
		try {
			// 连接es
			RestHighLevelClient client = new RestHighLevelClient(RestClient
					.builder(new HttpHost("192.168.1.150", 9200, "http"), new HttpHost("192.168.1.150", 9201, "http")));
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
		RestHighLevelClient client = new RestHighLevelClient(RestClient
				.builder(new HttpHost("192.168.1.150", 9200, "http"), new HttpHost("192.168.1.150", 9201, "http")));

		// 构建get请求
		GetRequest getRequest = new GetRequest("index", "id");

		// 添加查询参数:是否查询源数据,包括的字段数组,排除的字段数组
		FetchSourceContext fetchSourceContext = new FetchSourceContext(true, null, null);
		getRequest.fetchSourceContext(fetchSourceContext);

		// 只排除特定字段,其他字段全部查出
		FetchSourceContext fetchSourceContext1 =
				new FetchSourceContext(true, Strings.EMPTY_ARRAY, new String[] { "test5" });
		getRequest.fetchSourceContext(fetchSourceContext1);

		// 执行请求,同步获得结果
		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

		// 异步获得结果,需要实现监听器
		client.getAsync(getRequest, RequestOptions.DEFAULT, new ActionListener<GetResponse>() {

			@Override
			public void onResponse(GetResponse response) {

			}

			@Override
			public void onFailure(Exception e) {

			}
		});

		System.out.println(getResponse);
		System.out.println(getResponse.getId());
		// 获得结果
		System.out.println(getResponse.getSource());
		// 获得jsonstring数据
		System.out.println(getResponse.getSourceAsString());
		System.out.println(getResponse.getSourceAsMap());
	}

	/**
	 * 创建索引
	 * 
	 * @throws IOException
	 */
	public void testCreateIndex() throws IOException {
		// 在ES中用Web界面创建索引的格式
		// PUT /my_index
		// {
		// 设置
		// "settings": {
		// 分片数
		// "number_of_shards": 1,
		// 分片副本数
		// "number_of_replicas": 1
		// },
		// 映射
		// "mappings": {
		// "properties": {
		// "field1":{
		// "type": "text"
		// },
		// "field2":{
		// "type": "text"
		// }
		// }
		// },
		// 索引别名
		// "aliases": {
		// "default_index": {}
		// }
		// }

		// 创建索引对象
		CreateIndexRequest createIndexRequest = new CreateIndexRequest("index_name");
		// 设置参数
		createIndexRequest.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "0"));
		// 指定映射1
		createIndexRequest.mapping(
				" {\n" + " \t\"properties\": {\n" + "            \"name\":{\n" + "             \"type\":\"keyword\"\n"
						+ "           },\n" + "           \"description\": {\n" + "              \"type\": \"text\"\n"
						+ "           },\n" + "            \"price\":{\n" + "             \"type\":\"long\"\n"
						+ "           },\n" + "           \"pic\":{\n" + "             \"type\":\"text\",\n"
						+ "             \"index\":false\n" + "           }\n" + " \t}\n" + "}",
				XContentType.JSON);

		// 指定映射2
		// Map<String, Object> message = new HashMap<>();
		// message.put("type", "text");
		// Map<String, Object> properties = new HashMap<>();
		// properties.put("message", message);
		// Map<String, Object> mapping = new HashMap<>();
		// mapping.put("properties", properties);
		// createIndexRequest.mapping(mapping);

		// 指定映射3
		// XContentBuilder builder = XContentFactory.jsonBuilder();
		// builder.startObject();
		// {
		// builder.startObject("properties");
		// {
		// builder.startObject("message");
		// {
		// builder.field("type", "text");
		// }
		// builder.endObject();
		// }
		// builder.endObject();
		// }
		// builder.endObject();
		// createIndexRequest.mapping(builder);

		// 设置别名
		createIndexRequest.alias(new Alias("index_name_new"));

		// 设置超时时间
		createIndexRequest.setTimeout(TimeValue.timeValueMinutes(2));
		// 设置主节点超时时间
		createIndexRequest.setMasterTimeout(TimeValue.timeValueMinutes(1));
		// 在创建索引API返回响应之前等待的活动分片副本的数量,以int形式表示
		createIndexRequest.waitForActiveShards(ActiveShardCount.from(2));
		createIndexRequest.waitForActiveShards(ActiveShardCount.DEFAULT);

		// 操作索引的客户端
		IndicesClient indices = client.indices();
		// 执行创建索引库
		CreateIndexResponse createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);

		// 得到响应(全部)
		boolean acknowledged = createIndexResponse.isAcknowledged();
		// 得到响应 指示是否在超时前为索引中的每个分片启动了所需数量的碎片副本
		boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();

		System.out.println("acknowledged:" + acknowledged);
		System.out.println("shardsAcknowledged:" + shardsAcknowledged);
	}

	/**
	 * 创建索引异步方式
	 * 
	 * @throws IOException
	 */
	public void testCreateIndexAsync() throws IOException {
		// 创建索引对象
		CreateIndexRequest createIndexRequest = new CreateIndexRequest("index_name");
		// 设置参数
		createIndexRequest.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "0"));
		// 指定映射1
		createIndexRequest.mapping(
				" {\n" + " \t\"properties\": {\n" + "            \"name\":{\n" + "             \"type\":\"keyword\"\n"
						+ "           },\n" + "           \"description\": {\n" + "              \"type\": \"text\"\n"
						+ "           },\n" + "            \"price\":{\n" + "             \"type\":\"long\"\n"
						+ "           },\n" + "           \"pic\":{\n" + "             \"type\":\"text\",\n"
						+ "             \"index\":false\n" + "           }\n" + " \t}\n" + "}",
				XContentType.JSON);

		// 指定映射2
		// Map<String, Object> message = new HashMap<>();
		// message.put("type", "text");
		// Map<String, Object> properties = new HashMap<>();
		// properties.put("message", message);
		// Map<String, Object> mapping = new HashMap<>();
		// mapping.put("properties", properties);
		// createIndexRequest.mapping(mapping);

		// 指定映射3
		// XContentBuilder builder = XContentFactory.jsonBuilder();
		// builder.startObject();
		// {
		// builder.startObject("properties");
		// {
		// builder.startObject("message");
		// {
		// builder.field("type", "text");
		// }
		// builder.endObject();
		// }
		// builder.endObject();
		// }
		// builder.endObject();
		// createIndexRequest.mapping(builder);

		// 设置别名
		createIndexRequest.alias(new Alias("index_name_new"));

		// 设置超时时间
		createIndexRequest.setTimeout(TimeValue.timeValueMinutes(2));
		// 设置主节点超时时间
		createIndexRequest.setMasterTimeout(TimeValue.timeValueMinutes(1));
		// 在创建索引API返回响应之前等待的活动分片副本的数量,以int形式表示
		createIndexRequest.waitForActiveShards(ActiveShardCount.from(2));
		createIndexRequest.waitForActiveShards(ActiveShardCount.DEFAULT);

		// 操作索引的客户端
		IndicesClient indices = client.indices();
		System.out.println(indices);
		// 执行创建索引库
		// CreateIndexResponse createIndexResponse = indices.create(createIndexRequest,
		// RequestOptions.DEFAULT);
		ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {

			@Override
			public void onResponse(CreateIndexResponse createIndexResponse) {
				// 得到响应(全部)
				boolean acknowledged = createIndexResponse.isAcknowledged();
				// 得到响应 指示是否在超时前为索引中的每个分片启动了所需数量的碎片副本
				boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();

				System.out.println("acknowledged:" + acknowledged);
				System.out.println("shardsAcknowledged:" + shardsAcknowledged);
			}

			@Override
			public void onFailure(Exception e) {
				e.printStackTrace();
			}
		};

		client.indices().createAsync(createIndexRequest, RequestOptions.DEFAULT, listener);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除索引
	 * 
	 * @throws IOException
	 */
	public void testDeleteIndex() throws IOException {
		// 创建删除索引请求
		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("index_name");
		// 执行
		AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
		// 得到相应
		boolean acknowledged = delete.isAcknowledged();
		System.out.println("acknowledged:" + acknowledged);

	}

	/**
	 * 删除索引异步操作
	 * 
	 * @throws IOException
	 */
	public void testDeleteIndexAsync() throws IOException {
		// 创建删除索引请求
		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("index_name");

		// 异步删除
		ActionListener<AcknowledgedResponse> listener = new ActionListener<AcknowledgedResponse>() {

			@Override
			public void onResponse(AcknowledgedResponse acknowledgedResponse) {
				// 得到相应
				boolean acknowledged = acknowledgedResponse.isAcknowledged();
				System.out.println("acknowledged:" + acknowledged);
			}

			@Override
			public void onFailure(Exception e) {
				e.printStackTrace();
			}
		};

		client.indices().deleteAsync(deleteIndexRequest, RequestOptions.DEFAULT, listener);
	}

	/**
	 * index exist api
	 * 
	 * @throws IOException
	 */
	public void testExistIndex() throws IOException {
		GetIndexRequest request = new GetIndexRequest("index_name");
		// 从主节点返回本地索引信息状态
		request.local(false);
		// 以适合人类的格式返回
		request.humanReadable(true);
		// 是否返回每个索引的所有默认配置
		request.includeDefaults(false);

		boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
		System.out.println("exists:" + exists);
	}

	/**
	 * 关闭索引
	 * 
	 * @throws IOException
	 */
	public void testCloseIndex() throws IOException {
		CloseIndexRequest request = new CloseIndexRequest("index_name");
		AcknowledgedResponse close = client.indices().close(request, RequestOptions.DEFAULT);
		boolean acknowledged = close.isAcknowledged();
		System.out.println("acknowledged:" + acknowledged);
	}

	/**
	 * 开启索引
	 * 
	 * @throws IOException
	 */
	public void testOpenIndex() throws IOException {
		OpenIndexRequest request = new OpenIndexRequest("index_name");
		OpenIndexResponse open = client.indices().open(request, RequestOptions.DEFAULT);
		boolean acknowledged = open.isAcknowledged();
		System.out.println("acknowledged:" + acknowledged);
	}
}