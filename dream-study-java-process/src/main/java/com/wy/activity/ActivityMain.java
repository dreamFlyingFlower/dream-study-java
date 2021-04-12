package com.wy.activity;

import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.google.common.collect.Maps;

/**
 * activity流程图
 * 
 * @apiNote 核心API:ProcessEnigne,可通过该API操作以下service:
 *          RepositoryService:负责流程定义文件的管理,操作静态服务管理,流程文件中的xml
 *          RuntimeService:流程控制,对流程进行启动,挂起等 TaskService:对人工任务进行增删改查等操作,操作权限等
 *          IdentityService:对用户和用户组的管理 FormService:对流程任务中的表单进行解析渲染
 *          HistoryService:主要对执行完成的任务进行查询等 ManagementService:对流程基础和定时任务状态的管理
 *          DynamicBpmService:动态对流程的模型进行修改
 * @apiNote 数据表分类: ACT_GE_*:通用数据表;ACT_RE_*:流程定义存储表;ACT_ID_*:身份信息表;
 *          ACT_RU_*:运行时数据库表;ACT_HI_*:历史数据库表;
 *          ACT_RE_DEPLOYMENT:流程部署记录表;ACT_RE_PROCDEF:流程定义信息表;
 *          ACT_RE_MODEL:模型信息表(用于web设计器);ACT_PROCDED_INFO:流程定义动态改变信息表;
 *          ACT_ID_USER:用户基本信息;ACT_ID_INFO:用户扩展信息;
 *          ACT_ID_GROUP:群组;ACT_ID_MEMBERSHIP:用户和群组关联;
 *          ACT_RU_EXECUTION:流程实例与分支执行信息;ACT_RU_TASK:用户任务信息;
 *          ACT_RU_VARIABLE:变量信息;ACT_RU_IDENTITYLINK:参与者相关信息;
 *          ACT_RU_EVENT_SUBSCR:事件监听表;ACT_RU_JOB:作业表; ACT_RU_TIMER_JOB:定时器表;
 *          ACT_RU_SUSPENDED_JOB:暂停作业表; ACT_RU_DEADLETTER_JOB:死信表;
 * @author ParadiseWY
 * @date 2019年8月6日
 */
public class ActivityMain {

	public static void main(String[] args) {
		// 创建流程引擎
		ProcessEngine processEngine = extracted();
		// 部署流程定义文件
		ProcessDefinition definition = extracted(processEngine);
		// 启动运行程序
		ProcessInstance processInstance = extracted(processEngine, definition);
		// 处理流程任务
		handlerTask(processEngine, processInstance);
	}

	private static ProcessEngine extracted() {
		// 在内存中创建流程图引擎
		ProcessEngineConfiguration pec = ProcessEngineConfiguration
				.createStandaloneInMemProcessEngineConfiguration();
		// ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("流程图的xml文件地址");
		ProcessEngine processEngine = pec.buildProcessEngine();
		String name = processEngine.getName();
		String version = ProcessEngine.VERSION;
		System.out.println(name);
		System.out.println(version);
		// 获得managermentservice,对数据库进行操作
		ManagementService managementService = processEngine.getManagementService();
		// 获得数据库表中每个表的数据量,key是表明,value是数据量,可以从中拿到表
		Map<String, Long> tableCount = managementService.getTableCount();
		for (Map.Entry<String, Long> keEntry : tableCount.entrySet()) {
			System.out.println(keEntry);
		}
		managementService.executeCommand((context) -> {
			context.getResult();
			return context;
		});
		return processEngine;
	}

	private static ProcessDefinition extracted(ProcessEngine processEngine) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("流程图的xml文件地址");
		deploymentBuilder.name("设备流程图的名称,也可不设置");
		Deployment deploy = deploymentBuilder.deploy();
		String deployId = deploy.getId();
		// 流程定义与用户以及用户组建立关联,与业务相关
		repositoryService.addCandidateStarterUser(deployId, "userId");
		repositoryService.addCandidateStarterGroup(deployId, "groupid");
		System.out.println(deployId);
		// 流程定义对象
		ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployId).singleResult();
		return definition;
	}

	private static ProcessInstance extracted(ProcessEngine processEngine,
			ProcessDefinition definition) {
		// 流程启动
		RuntimeService runtimeService = processEngine.getRuntimeService();
		// 用流程定义的id或key或message启动,推荐使用id和key,因为唯一
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(definition.getId());
		// runtimeService.startProcessInstanceByKey(definition.getKey());
		System.out.println(processInstance.getActivityId());
		// 返回流程实例对象
		return processInstance;
	}

	/**
	 * 任务的执行,完成等,也可以给任务添加附件,评论,event等
	 * 
	 * @param processEngine 流程引擎
	 * @param processInstance 流程实例
	 */
	private static void handlerTask(ProcessEngine processEngine, ProcessInstance processInstance) {
		TaskService taskService = processEngine.getTaskService();
		Task singleResult = taskService.createTaskQuery().singleResult();
		// 指定这个任务的拥有者,发起人
		taskService.setOwner(singleResult.getId(), "userid");
		// 查看任务是否已经指定了待办人,若是指定了则会抛出异常
		taskService.claim(singleResult.getId(), "userid");
		// 指定任务的待办人,但要慎用,避免多次重复指定不同待办人,最好指定之前查询是否指定待办人
		taskService.setAssignee(singleResult.getId(), "userid");
		// 查询没有指定待办人的任务列表
		taskService.createTaskQuery().taskUnassigned().listPage(0, 100);
		// 查询没有指定待办人,但是候选列表中有特定待办人的任务李彪
		taskService.createTaskQuery().taskCandidateUser("userid").taskUnassigned().listPage(0, 100);
		// 获得已经指定了待办人的任务列表
		taskService.getIdentityLinksForTask(singleResult.getId());
		// 查询指定了userid为待办人的任务列表
		taskService.createTaskQuery().taskAssignee("userid").listPage(0, 100);
		taskService.createAttachment("url", singleResult.getId(), processInstance.getId(), "附件的名称",
				"附件的描述", "http://url地址");
		taskService.getAttachment(singleResult.getId());
		taskService.addComment(singleResult.getId(), processInstance.getId(), "context");
		taskService.getComment(singleResult.getId());
		// event是系统自带的,会记录流程中的事件步骤
		taskService.getTaskEvents(singleResult.getId());
		List<Task> tasks = taskService.createTaskQuery().list();
		for (Task task : tasks) {
			System.out.println(task.getName());
			taskService.complete(task.getId());
			// 完成任务,也可以在完成任务的时候加一些参数
			taskService.complete(task.getId(), Maps.newHashMap());
			// 查看结果
			processEngine.getRuntimeService().createProcessInstanceQuery()
					.processInstanceId(processInstance.getId()).singleResult();
		}

		HistoryService historyService = processEngine.getHistoryService();
		// HistoricProcessInstance历史流程实例实体
		historyService.createHistoricProcessInstanceQuery().list();
		// HistoricVariableInstance流程或任务变量值的实体
		historyService.createHistoricVariableInstanceQuery().list();
		// HistoricActivityInstance单个活动节点执行的信息
		historyService.createHistoricActivityInstanceQuery().list();
		// HistoricTaskInstance用户任务实例的信息
		historyService.createHistoricTaskInstanceQuery().list();
		// HistoricDetail历史流程活动任务详细信息
		historyService.createHistoricDetailQuery().list();
	}
}