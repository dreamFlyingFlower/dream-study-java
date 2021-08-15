package com.wy.activity;

import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ReceiveTask;
import org.activiti.bpmn.model.ScriptTask;
import org.activiti.bpmn.model.SendTask;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.BaseEntityEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.impl.asyncexecutor.AsyncExecutor;
import org.activiti.engine.impl.asyncexecutor.DefaultAsyncJobExecutor;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cmd.MessageEventReceivedCmd;
import org.activiti.engine.impl.cmd.SignalEventReceivedCmd;
import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.CommandInterceptor;
import org.activiti.engine.impl.jobexecutor.TimerStartEventJobHandler;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.impl.persistence.entity.DeploymentEntityImpl;
import org.activiti.engine.impl.persistence.entity.EventLogEntryEntityImpl;
import org.activiti.engine.impl.persistence.entity.EventSubscriptionEntityImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntityImpl;
import org.activiti.engine.impl.persistence.entity.JobEntityImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.impl.persistence.entity.PropertyEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.impl.persistence.entity.TimerJobEntityImpl;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.DeadLetterJobQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.SuspendedJobQuery;
import org.activiti.engine.runtime.TimerJobQuery;
import org.activiti.engine.task.Task;

import com.wy.collection.MapTool;

/**
 * Activiti流程引擎,可画流程图的网站{@link https://bpmn.io/}
 * 
 * Activiti中的常用组件,核心API{@link ProcessEnigne},可通过该API操作以下service:
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
 * {@link TaskService}:对用户任务进行增删改查等,设置用户任务的权限(拥有者,候选人,办理人).给用户任务添加任务附件,评论和事件记录
 * {@link HistoryService}:主要对执行完成的任务进行查询等 
 * {@link ManagementService}:对流程基础运行,数据库,定时任务状态的管理
 * {@link DynamicBpmnService}:动态对流程的模型进行修改
 * {@link FormService}:对流程任务中的表单进行解析渲染
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
 * activiti事件监听:
 * 
 * <pre>
 * {@link ActivitiEvent}:事件对象接口
 * {@link ActivitiEventListener}:事件监听器,实现该接口可对监听事件做个性化处理
 * {@link ActivitiEventType}:监听类型,判断监听事件时的事件类型
 * {@link BaseEntityEventListener}:对实体类数据的监听,若更关心数据的操作,可继承该类监听
 * {@link ProcessEngineConfigurationImpl#eventListeners}:将实现ActivitiEventListener的类设置其中
 * {@link ProcessEngineConfigurationImpl#typedEventListeners}:所有实现ActivitiEventListener的类集合
 * </pre>
 * 
 * Command命令拦截器,主要是对流程中各种节点进行拦截:
 * 
 * <pre>
 * {@link CommandInterceptor}:Command拦截器主接口
 * {@link AbstractCommandInterceptor}:默认实现的抽象拦截器
 * {@link ProcessEngineConfigurationImpl#customPreCommandInterceptors}:根据实现手动配置,前置命令拦截器
 * {@link ProcessEngineConfigurationImpl#customPostCommandInterceptors}:根据实现手动配置,后置命令拦截器
 * {@link ProcessEngineConfigurationImpl#commandInvoker}:配置在最后的拦截器
 * </pre>
 * 
 * 作业(Job)执行器,流程定义定时启动流程:
 * 
 * <pre>
 * {@link AsyncExecutor}:作业执行器主要接口
 * {@link ProcessEngineConfiguration#asyncExecutorActivate}:是否使用作业执行器
 * {@link ProcessEngineConfiguration#asyncExecutor}:作业执行器实现类
 * ->{@link DefaultAsyncJobExecutor}:默认作业执行器实现类
 * {@link TimerStartEventJobHandler}:开始执行任务实现类
 * timeDate:指定启动时间,在流程图xml的startEvent标签中使用
 * timeDuration:指定持续时间间隔后执行,在流程图xml的startEvent标签中使用
 * timeCycle:指定事件段后周期执行,在流程图xml的startEvent标签中使用.如R5/PT10S,执行5次,间隔10S
 * 
 * {@link JobQuery}:查询一般工作
 * {@link TimerJobQuery}:查询定时工作
 * {@link SuspendedJobQuery}:查询中断工作
 * {@link DeadLetterJobQuery}:查询无法执行的工作
 * </pre>
 *
 * 数据表分类:
 * 
 * <pre>
 * ACT_GE_*:通用数据表;
 * ->ACT_GE_PROPERTY:属性表,保存流程引擎的kv键值属性,{@link PropertyEntityImpl}
 * ->ACT_GE_BYTEARRAY:资源表,存储流程定义相关的资源,{@link ByteArrayEntityImpl}
 * ACT_RE_*:流程定义存储表;
 * ->ACT_RE_DEPLOYMENT:流程部署记录表,{@link DeploymentEntityImpl}
 * ->ACT_RE_PROCDEF:流程定义信息表,{@link ProcessDefinitionEntityImpl}
 * ->ACT_RE_MODEL:模型信息表(用于web设计器);
 * ACT_PROCDED_INFO:流程定义动态改变信息表;
 * ACT_ID_*:身份信息表,版本7以上已废弃;
 * ->ACT_ID_USER:用户基本信息;
 * ->ACT_ID_INFO:用户扩展信息;
 * ->ACT_ID_GROUP:群组;
 * ->ACT_ID_MEMBERSHIP:用户和群组关联;
 * ACT_RU_*:运行时数据库表;
 * ->ACT_RU_EXECUTION:流程实例与分支执行信息,{@link ExecutionEntityImpl}
 * ->ACT_RU_TASK:用户任务信息,{@link TaskEntityImpl}
 * ->ACT_RU_VARIABLE:变量信息,{@link VariableInstanceEntityImpl}
 * ->ACT_RU_IDENTITYLINK:参与者相关信息,{@link IdentityLinkEntityImpl}
 * ->ACT_RU_EVENT_SUBSCR:事件监听表,{@link EventSubscriptionEntityImpl}
 * ->ACT_RU_JOB:作业表,{@link JobEntityImpl}
 * ->ACT_RU_TIMER_JOB:定时器表,{@link TimerJobEntityImpl}
 * ->ACT_RU_SUSPENDED_JOB:暂停作业表;
 * ->ACT_RU_DEADLETTER_JOB:死信表;
 * ACT_HI_*:历史数据库表;
 * ->ACT_HI_PROCINST:历史流程实例表,{@link HistoricProcessInstanceEntityImpl}
 * ->ACT_HI_ACTINST:历史节点信息表
 * ->ACT_HI_TASKINST:历史任务表
 * ->ACT_HI_VARINST:历史变量
 * ->ACT_HI_IDENTITYLINK:历史参与者
 * ->ACT_HI_DETAIL:历史变更
 * ->ACT_HI_ATTACHMENT:附件
 * ->ACT_HI_COMMENT:评论
 * ->ACT_HI_LOG:事件日志
 * ACT_EVT_LOG:事件日志表,{@link EventLogEntryEntityImpl}
 * </pre>
 * 
 * 核心任务流程:
 * 
 * <pre>
 * {@link ServiceTask}:调用外部服务或自动执行程序
 * {@link SendTask}:发出任务,处理向外部发送消息的任务
 * {@link ReceiveTask}:接收任务,接收外部发送过来的消息
 * {@link UserTask}:需要人参与的任务
 * {@link ScriptTask}:脚本任务,执行定义好的脚本程序
 * </pre>
 * 
 * 网关类型:
 * 
 * <pre>
 * Exclusive Gateway:单一网关,单一条件,菱形图形中间是一个X
 * Parallel Gateway:并行网关,多条线路同时进行,多条线路都完成之后再进行合并.菱形图形中间是一个+
 * Inclusive Gateway:包容性网关,包含单一网关和并行网关.菱形图形中间是一个⚪
 * Event-based Gateway:基于事件网关,事件进行到此处时会暂停下来.菱形图形事件是双圆加五边形
 * </pre>
 * 
 * 子流程:
 * 
 * <pre>
 * Sub-Process:子流程
 * Event Sub-Process:事件子流程
 * Transcation Sub-Process:事务子流程
 * Call Activity:调用式子流程
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-08-09 20:46:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Activiti {

	public static void main(String[] args) {
		// 创建流程引擎
		ProcessEngine processEngine = createProcessEngine();
		// 部署流程定义文件
		ProcessDefinition definition = createProcessDefinition(processEngine);
		// 流程管理
		createManagement(processEngine);
		// 启动运行程序
		ProcessInstance processInstance = createProcessInstance(processEngine, definition);
		// 处理流程任务
		handlerTask(processEngine, processInstance);
		// 历史数据管理
		handlerHistory(processEngine);
	}

	public static ProcessEngine createProcessEngine() {
		// 在内存中创建流程图引擎
		ProcessEngineConfiguration pec = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		// ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("流程图的xml文件地址");
		ProcessEngine processEngine = pec.buildProcessEngine();
		String name = processEngine.getName();
		String version = ProcessEngine.VERSION;
		System.out.println(name);
		System.out.println(version);
		return processEngine;
	}

	public static void createManagement(ProcessEngine processEngine) {
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
		// 获得定时工作列表
		List<Job> timerJobs = managementService.createTimerJobQuery().listPage(0, 100);
		for (Job job : timerJobs) {
			System.out.println(job);
		}
	}

	public static ProcessDefinition createProcessDefinition(ProcessEngine processEngine) {
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
		// 流程定义与用户以及用户组建立关联,与业务相关
		repositoryService.addCandidateStarterUser(deployId, "userId");
		repositoryService.addCandidateStarterGroup(deployId, "groupid");
		System.out.println(deployId);
		// 流程定义对象
		ProcessDefinition definition =
				repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
		return definition;
	}

	public static ProcessInstance createProcessInstance(ProcessEngine processEngine, ProcessDefinition definition) {
		// 流程启动
		RuntimeService runtimeService = processEngine.getRuntimeService();
		Map<String, Object> variables = MapTool.builder("test", "test").build();
		// 启动流程引擎时设置参数,用流程定义的id或key或message启动,推荐使用key,因为唯一,id每次部署都会改变
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("key", variables);
		System.out.println(processInstance.getActivityId());
		// ProcessInstance processInstance =
		// runtimeService.startProcessInstanceById(definition.getId());
		// 获得流程引擎设置的参数
		runtimeService.getVariables(processInstance.getId());
		// 在流程引擎运行过程中修改参数
		runtimeService.setVariable(processInstance.getId(), "test1", "test1");
		// 将自定义的事件监听加入到activit中
		runtimeService.addEventListener(new MyActivitiListener());
		// 设置事件监听的类型
		runtimeService.dispatchEvent(new ActivitiEventImpl(ActivitiEventType.CUSTOM));
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
		// 获得流程事件日志
		processEngine.getManagementService()
				.getEventLogEntriesByProcessInstanceId(processInstance.getProcessInstanceId());
		// 获得流程中所有定时任务
		processEngine.getManagementService().createTimerJobQuery().list();
		// 返回流程实例对象
		return processInstance;
	}

	/**
	 * 任务的执行,完成等,也可以给任务添加附件,评论,event等
	 * 
	 * @param processEngine 流程引擎
	 * @param processInstance 流程实例
	 */
	public static void handlerTask(ProcessEngine processEngine, ProcessInstance processInstance) {
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		// 指定这个任务的拥有者,发起人
		taskService.setOwner(task.getId(), "userid");
		// 查看任务是否已经指定了待办人,若是指定了则会抛出异常
		taskService.claim(task.getId(), "userid");
		// 指定任务的待办人,但要慎用,避免多次重复指定不同待办人,最好指定之前查询是否指定待办人
		taskService.setAssignee(task.getId(), "userid");
		// 指定任务的候选人
		taskService.addCandidateUser(task.getId(), "userId");
		// 指定任务的候选组
		taskService.addCandidateGroup(task.getId(), "groupId");
		// 查询没有指定待办人的任务列表
		taskService.createTaskQuery().taskUnassigned().listPage(0, 100);
		// 查询没有指定待办人,但是候选列表中有特定待办人的任务李彪
		taskService.createTaskQuery().taskCandidateUser("userid").taskUnassigned().listPage(0, 100);
		// 获得已经指定了待办人的任务列表
		taskService.getIdentityLinksForTask(task.getId());
		// 查询指定了userid为待办人的任务列表
		taskService.createTaskQuery().taskAssignee("userid").listPage(0, 100);
		// 指定任务的附件
		taskService.createAttachment("url", task.getId(), processInstance.getId(), "附件的名称", "附件的描述", "http://url地址");
		taskService.getAttachment(task.getId());
		// 指定任务的评论
		taskService.addComment(task.getId(), processInstance.getId(), "context");
		taskService.getComment(task.getId());
		// event是系统自带的,会记录流程中的事件步骤
		taskService.getTaskEvents(task.getId());
		List<Task> tasks = taskService.createTaskQuery().list();
		for (Task item : tasks) {
			System.out.println(item.getName());
			taskService.complete(item.getId());
			// 完成任务,也可以在完成任务的时候加一些参数
			taskService.complete(item.getId(), MapTool.newHashMap());
			// 查看结果
			processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstance.getId())
					.singleResult();
		}
	}

	/**
	 * 对历史数据进行操作
	 * 
	 * @param processEngine 流程引擎实例
	 */
	public static void handlerHistory(ProcessEngine processEngine) {
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
		// 删除流程实例相关的历史
		historyService.deleteHistoricProcessInstance("processInstanceId");
		// 删除任务相关的历史
		historyService.deleteHistoricTaskInstance("taskId");
	}
}