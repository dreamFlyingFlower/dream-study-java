package com.wy.activity;

import java.util.List;
import java.util.Map;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ReceiveTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.cmd.MessageEventReceivedCmd;
import org.activiti.engine.impl.cmd.SignalEventReceivedCmd;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

import com.wy.collection.MapTool;

/**
 * Activiti整合SpringBoot
 * 
 * Activiti中的常用组件:
 * 
 * <pre>
 * {@link RepositoryService}:负责流程定义文件的管理,操作静态服务管理,流程文件中的xml
 * ->{@link Deployment}:流程部署文件对象
 * ->{@link DeploymentBuilder}:部署文件构造器
 * ->{@link DeploymentQuery}:部署文件查询器
 * ->{@link ProcessDefinition}:流程定义文件对象
 * ->{@link ProcessDefinitionQuery}:流程定义文件查询对象
 * ->{@link BpmnModel}:流程定义的Java格式
 * {@link RuntimeService}:启动流程及对流程数据的控制,触发流程操作,接收消息和信号.每次部署id会变,但key不会变,使用key最准确
 * ->{@link ProcessInstance}:流程实例,继承自Execution.一次工作流业务的数据实体
 * ->{@link Execution}:执行流查询,流程实例中具体的执行路径
 * </pre>
 * 
 * 流程触发:
 * 
 * <pre>
 * {@link ReceiveTask}:使用trigger触发ReceiveTask节点
 * {@link SignalEventReceivedCmd}:触发信号捕获事件signalEventReceived,可以全局触发
 * {@link MessageEventReceivedCmd}:触发消息捕获事件messageEventReceived,只能对单个流程实例触发
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-08-09 20:46:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Activiti {

	public void deploymentQuery(ProcessEngine processEngine) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("流程图的xml文件地址").addClasspathResource("可同时部署多个,文件放在resource目录下");
		deploymentBuilder.name("设备流程图的名称,也可不设置");
		// 部署
		Deployment deploy = deploymentBuilder.deploy();
		// 获得部署的流程定义的ID
		String deployId = deploy.getId();
		DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
		// 查询指定id的部署信息,得到的结果同deploy
		deploymentQuery.deploymentId(deployId).singleResult();
		// 根据部署的编号获得部署的流程定义信息
		repositoryService.createProcessDefinitionQuery().deploymentId(deployId).list();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		Map<String, Object> variables = MapTool.builder("test", "test").build();
		// 启动流程引擎时设置参数
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("key", variables);
		// 获得流程引擎设置的参数
		runtimeService.getVariables(processInstance.getId());
		// 在流程引擎运行过程中修改参数
		runtimeService.setVariable(processInstance.getId(), "test1", "test1");
		// 获得所有的流程引擎执行对象,即processInstance执行对象
		List<Execution> executions = runtimeService.createExecutionQuery().list();
		for (Execution execution : executions) {
			System.out.println(execution.getId());
		}
		// 主动触发执行事件
		Execution execution = runtimeService.createExecutionQuery().activityId("该id是xml文件中某个标签的id").singleResult();
		runtimeService.trigger(execution.getId());
		// 信号捕获事件
		runtimeService.createExecutionQuery().signalEventSubscriptionName("该name是xml文件中signal标签的name").singleResult();
		runtimeService.signalEventReceived("该name是xml文件中signal标签的name");
		// 消息捕获事件
		Execution execution2 = runtimeService.createExecutionQuery()
				.messageEventSubscriptionName("该name是xml文件中message标签的name").singleResult();
		runtimeService.messageEventReceived("该name是xml文件中message标签的name", execution2.getId());
	}
}