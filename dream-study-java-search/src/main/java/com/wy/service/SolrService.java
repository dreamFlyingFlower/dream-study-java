package com.wy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.wy.crypto.CryptoUtils;
import com.wy.mapper.UserMapper;
import com.wy.model.User;
import com.wy.result.Result;
import com.wy.utils.ListUtils;
import com.wy.utils.MapUtils;

/**
 * 测试单机版solr的增删改查,SolrCloud搭建比较复杂,使用时和httpsolrclient差不多
 * 
 * @author ParadiseWY
 * @date 2019年4月22日 下午2:09:45
 * @git {@link https://github.com/mygodness100}
 */
@RestController
@RequestMapping("solr")
public class SolrService {

	@Autowired
	private SolrClient solrClient;

	@Autowired
	private UserMapper userMapper;

	@Value("${solr.collection}")
	private String collection;

	/**
	 * 非实体类新增到solr中
	 * 
	 * @return 失败或成功
	 */
	public void addSingle() {
		SolrInputDocument ds = new SolrInputDocument();
		ds.addField("id", CryptoUtils.UUID());
		ds.addField("username", "测试单个新增");
		try {
			solrClient.add(collection, ds);
			solrClient.commit(collection);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 实体类批量从数据库中取出之后存入到solr中
	 * 
	 * @return 结果集
	 */
	public void addBeans() {
		List<User> users = userMapper.selectEntitys(User.builder().build());
		try {
			solrClient.addBeans(collection, users);
			solrClient.commit(collection);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteSingle(String id) {
		try {
			solrClient.deleteById(collection, id);
			solrClient.commit(collection);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	public void deletes(List<String> ids) {
		try {
			solrClient.deleteById(collection, ids);
			solrClient.commit(collection);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除所有数据
	 */
	public void deleteAll() {
		try {
			solrClient.deleteByQuery(collection, "*:*");
			solrClient.commit(collection);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	public SolrDocument getById(String id) {
		SolrDocument result = null;
		try {
			result = solrClient.getById(collection, id);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public SolrDocumentList getByIds(List<String> ids) {
		SolrDocumentList result = null;
		try {
			result = solrClient.getById(collection, ids);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询的参数格式应该是key:q
	 * 
	 * @param param 需要查询的参数,应该是一个map;多个条件的时候可以用and或or进行连接 数字的区间范围可以用salary:[1 TO 9];若是不包含边界,用{}
	 * @return 结果集
	 */
	public Result<?> search(Map<String, Object> param) {
		try {
			List<String> q = new ArrayList<>();
			if (MapUtils.isNotBlank(param)) {
				for (Map.Entry<String, Object> entry : param.entrySet()) {
					q.add(entry.getKey() + ":" + entry.getValue());
				}
			}
			// 查询参数对象，继承了SolrParams抽象类
			ModifiableSolrParams params = new ModifiableSolrParams();
			// 查询条件
			params.add("q", ListUtils.isNotBlank(q) ? String.join(" and ", q) : "*:*");
			// 这里的分页和mysql分页一样
			params.add("start", "0");
			params.add("rows", "10");
			QueryResponse query = solrClient.query("collection1", params);
			// 查询结果
			SolrDocumentList results = query.getResults();
			System.out.println(results.toString());

			SolrQuery cnds = new SolrQuery();
			// 设置查询条件
			cnds.set("q", ListUtils.isNotBlank(q) ? String.join(" and ", q) : "*:*");
			// 排序
			cnds.addSort("id", ORDER.asc);
			// 分页,和mysql一样
			cnds.setStart(0);
			cnds.setRows(10);
			// 高亮
			cnds.setHighlight(true);
			// 高亮字段
			cnds.addHighlightField("username");
			// 设置前缀
			cnds.setHighlightSimplePre("<span style='color:red'>");
			// 设置后缀
			cnds.setHighlightSimplePost("</span>");

			QueryResponse response = solrClient.query(cnds);
			SolrDocumentList documentList = response.getResults();
			System.out.println(JSON.toJSONString(documentList));

			// 将查询结果直接转化为List,这里有个坑,对象每个属性必须要加 @Field("id") 属性,包为import
			// org.apache.solr.client.solrj.beans.Field;
			// 如果不加属性的话，会返回相等长度的的List，但是List里面每个对象的值均为空
			// 该方法报错
			List<User> beans = query.getBeans(User.class);
			return Result.ok(beans);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}