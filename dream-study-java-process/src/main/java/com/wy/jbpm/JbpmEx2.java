//package com.wy.jbpm;
//
//import org.jbpm.api.Configuration;
//import org.jbpm.api.ProcessEngine;
//
///**
// * 创建完18张表后,需要通过eclipse的jbpm插件设计一个流程图<br>
// * 设计完流程图会生成一个example.jpdl.xml文件和一张流程定义的图片
// *
// * @author 飞花梦影
// * @date 2018-03-14 23:38:20
// * @git {@link https://github.com/mygodness100}
// */
//public class JbpmEx2 {
//
//	public static void main(String[] args) {}
//
//	/**
//	 * 对流程定义进行发布 主要涉及18张表的jbpm4_deployment,jbpm4_deployprop,jbpm4_lob
//	 * jbpm4_deployment:描述一次部署 jbpm4_deployprop:部署属性表,每次部署都会生成4条数据
//	 * jbpm4_lob:部署的时候,xml和png文件都会放在该表中
//	 */
//	public static void deploy() {
//		// 1.第一种部署方式
//		// 获得jbpm的流程引擎
//		ProcessEngine pe = Configuration.getProcessEngine();
//		// 获得jbpm服务
//		pe.getRepositoryService()
//				// 创建部署
//				.createDeployment()
//				// 部署的xml流程文件
//				.addResourceFromClasspath("example.jpdl.xml")
//				// 部署的png流程图片,可以没有
//				.addResourceFromClasspath("")
//				// 部署
//				.deploy();
//
//		// 2.第二种部署方式
//
//		ProcessEngine pe1 = Configuration.getProcessEngine();
//		pe1.getRepositoryService().createDeployment()
//				.addResourceFromInputStream("example.jpdl.xml",
//						JbpmEx2.class.getClassLoader().getResourceAsStream("example.jpdl.xml"))
//				.addResourceFromInputStream("", null)
//				// 部署
//				.deploy();
//	}
//
//	public static void query() {
//		// 1.第一种部署方式
//		// 获得jbpm的流程引擎
//		ProcessEngine pe = Configuration.getProcessEngine();
//		pe.getRepositoryService()
//				// 创建查询
//				.createDeploymentQuery()
//				// 传入部署id
//				.deploymentId("").uniqueResult();
//
//		// 查询所有的流程部署
//		pe.getRepositoryService().createDeploymentQuery().list();
//
//		pe.getRepositoryService().deleteDeployment("部署id");
//	}
//}