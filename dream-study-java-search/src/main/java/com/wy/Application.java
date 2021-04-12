package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 搜索:常用的ElasticSearch(Es),用于实时搜索;Solr,用于离线搜索
 * 
 * @apiNote Es的版本可能和Spring中的不兼容,需要从官网上查看具体的版本
 * 
 * @apiNote Solr中的2个类:单机版使用HttpSolrClient,集群使用CloudSolrClient<br>
 *          在使用Solr之前需要先在solr的web管理界面中新建一个core,否则提交时会报错
 *          Solr另外一种方式是配置EnableSolrRepositories,可使用SolrDocument等相关注解
 *          若不使用spring的自动配置,可调用HttpSolrClient.Builder()来创建SolrClient
 * 
 * @author ParadiseWY
 * @date 2020-11-23 09:58:05
 * @git {@link https://github.com/mygodness100}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}