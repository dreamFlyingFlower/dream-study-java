package com.wy.crl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.result.Result;
import com.wy.service.SolrService;

/**
 * 测试单机版solr的增删改查,SolrCloud搭建比较复杂,使用时和httpsolrclient差不多
 * 
 * @author ParadiseWY
 * @date 2019年4月22日 下午2:09:45
 * @git {@link https://github.com/mygodness100}
 */
@RestController
@RequestMapping("solr")
public class SolrCrl {

	@Autowired
	private SolrService solrService;

	/**
	 * 非实体类新增到solr中
	 * 
	 * @return 失败或成功
	 */
	@GetMapping("addSingle")
	public Result<?> addSingle() {
		solrService.addSingle();
		return Result.ok();
	}

	/**
	 * 实体类批量从数据库中取出之后存入到solr中
	 * 
	 * @return 结果集
	 */
	@GetMapping("addBeans")
	public Result<?> addBeans() {
		solrService.addBeans();
		return Result.ok();
	}

	@GetMapping("deleteSingle/{id}")
	public Result<?> deleteSingle(@PathVariable String id) {
		solrService.deleteSingle(id);
		return Result.ok();
	}

	@PostMapping("deletes")
	public Result<?> deletes(@RequestBody List<String> ids) {
		solrService.deletes(ids);
		return Result.ok();
	}

	/**
	 * 删除所有数据
	 */
	@GetMapping("deleteAll")
	public Result<?> deleteAll() {
		solrService.deleteAll();
		return Result.ok();
	}

	@GetMapping("getById/{id}")
	public Result<?> getById(@PathVariable String id) {
		return Result.ok(solrService.getById(id));
	}

	@GetMapping("getByIds")
	public Result<?> getByIds(@RequestBody List<String> ids) {
		return Result.ok(solrService.getByIds(ids));
	}

	/**
	 * 查询的参数格式应该是key:q
	 * 
	 * @param param 需要查询的参数,应该是一个map;多个条件的时候可以用and或or进行连接 数字的区间范围可以用salary:[1 TO 9];若是不包含边界,用{}
	 * @return 结果集
	 */
	@PostMapping("search")
	public Result<?> search(@RequestBody Map<String, Object> param) {
		return solrService.search(param);
	}
}