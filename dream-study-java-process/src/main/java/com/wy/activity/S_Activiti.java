package com.wy.activity;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

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
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractBpmnParseHandler;
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
import org.activiti.engine.parse.BpmnParseHandler;
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
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ActivitiProperties;
import org.activiti.spring.boot.ProcessEngineAutoConfiguration;
import org.springframework.web.multipart.MultipartFile;

import com.wy.collection.MapTool;

/**
 * Activiti流程引擎,可画流程图的网站{@link https://bpmn.io/}
 * 
 * 一些概念:
 * 
 * <pre>
 * 流程定义:一种,流程定义就是流程图,定义某些流程的顺序结构
 * 流程实例:可以有无数个,每启动一次流程就会产生一个流程实例
 * 流程变量:流程运行期间的一些数据
 * 任务:流程执行到的需要具体的执行一个任务
 * 起始,结束:Start,End,流程的开始和结束
 * 网关:Gateway,控制流程的流转方向
 * </pre>
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
 * ->{@link ProcessInstance}:流程实例,继承自Execution.一次工作流业务的数据实体,一个ProcessInstance包含一个或多个Execution
 * ->{@link Execution}:执行流查询,流程实例中具体的执行路径
 * {@link TaskService}:对用户任务进行增删改查等,设置用户任务的权限(拥有者,候选人,办理人).给用户任务添加任务附件,评论和事件记录
 * {@link HistoryService}:主要对执行完成的任务进行查询等 
 * {@link ManagementService}:对流程基础运行,数据库,定时任务状态的管理
 * {@link DynamicBpmnService}:动态对流程的模型进行修改
 * {@link FormService}:对流程任务中的表单进行解析渲染,7以上已经废弃
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
 * 流程解析器{@link BpmnParser}
 * 
 * <pre>
 * {@link BpmnParser}:对于每个流程,BpmnParser都会创建一个新的{@link BpmnParse}实例,这个实例会作为解析过程中的容器来使用.
 * {@link BpmnParseHandler}:解析流程时产生的文件处理实例
 * {@link ProcessEngineConfigurationImpl#setPreBpmnParseHandlers}:根据实现手动配置,前置流程解析器
 * {@link ProcessEngineConfigurationImpl#setPostBpmnParseHandlers}:根据实现手动配置,后置流程解析器
 * {@link ProcessEngineConfigurationImpl#setCustomDefaultBpmnParseHandlers}:替换默认BpmnParseHandler实例,慎用 
 * {@link AbstractBpmnParseHandler}:若想解析自定义的元素,可继承该类,实现getHandledType()和executeParse()
 * 
 * 解析过程:对于每个BPMN2.0元素,引擎中都会有一个对应的BpmnParseHandler实例.
 * 		解析器会保存一个BPMN2.0元素与BpmnParseHandler实例的映射.
 * 		Activiti默认使用BpmnParseHandler来处理所有支持的元素,也使用它来提供执行监听器,以支持流程历史.
 * 		向Activiti引擎中添加自定义的BpmnParseHandler实例,经常看到的用例是把执行监听器添加到对应的环节来处理一些事件队列
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
 * 历史数据级别,需要在配置文件中开启并设置:
 * 
 * <pre>
 * none:默认值,忽略所有历史存档.这是流程执行时性能最好的状态,但没有任何历史信息可用
 * 	activity:保存所有流程实例和活动实例信息.在流程实例结束时,最后一个流程实例的最新变量值将赋值给历史变量.不保存过程中的信息
 * 	audit:保存所有流程实例信息,活动信息,保证所有的变量和提交的表单属性保持同步.这样所有用户交互信息都是可追溯的,可以用来审计
 * 	full:最高级别的历史信息存档,也是最慢的.这个级别存储发生在审核以及所有其它细节的信息,主要是更新流程变量
 * </pre>
 *
 * 数据表分类:
 * 
 * <pre>
 * ACT_GE_*:通用数据表
 * ->ACT_GE_PROPERTY:属性表,保存流程引擎的kv键值属性,{@link PropertyEntityImpl}
 * ->ACT_GE_BYTEARRAY:资源表,存储流程定义相关的资源,{@link ByteArrayEntityImpl}
 * 
 * ACT_RE_*:流程定义存储表.RE表示repository,包含了流程定义和流程静态资源(图片,规则,等等)
 * ->ACT_RE_DEPLOYMENT:流程部署记录表,{@link DeploymentEntityImpl}
 * ->ACT_RE_PROCDEF:流程定义信息表,{@link ProcessDefinitionEntityImpl}
 * ->ACT_RE_MODEL:模型信息表(用于web设计器)
 * 
 * ACT_RU_*:运行时数据库表.RU表示runtime,包含流程实例,任务,变量,异步任务等运行中的数据.
 * 		Activiti只在流程实例执行过程中保存这些数据,在流程结束时就会删除这些记录
 * ->ACT_RU_EXECUTION:流程实例与分支执行信息,{@link ExecutionEntityImpl}
 * ->ACT_RU_TASK:用户任务信息,{@link TaskEntityImpl}
 * ->ACT_RU_VARIABLE:变量信息,{@link VariableInstanceEntityImpl}
 * ->ACT_RU_IDENTITYLINK:参与者相关信息,{@link IdentityLinkEntityImpl}
 * ->ACT_RU_EVENT_SUBSCR:事件监听表,{@link EventSubscriptionEntityImpl}
 * ->ACT_RU_JOB:作业表,{@link JobEntityImpl}
 * ->ACT_RU_TIMER_JOB:定时器表,{@link TimerJobEntityImpl}
 * ->ACT_RU_SUSPENDED_JOB:暂停作业表
 * ->ACT_RU_DEADLETTER_JOB:死信表
 * 
 * ACT_HI_*:历史数据库表.HI表示history,这些表包含历史数据,比如历史流程实例,变量,任务等等
 * ->ACT_HI_PROCINST:历史流程实例表,{@link HistoricProcessInstanceEntityImpl}
 * ->ACT_HI_ACTINST:历史节点信息表
 * ->ACT_HI_TASKINST:历史任务表
 * ->ACT_HI_VARINST:历史变量
 * ->ACT_HI_IDENTITYLINK:历史参与者
 * ->ACT_HI_DETAIL:历史变更
 * ->ACT_HI_ATTACHMENT:附件
 * ->ACT_HI_COMMENT:评论
 * ->ACT_HI_LOG:事件日志
 * 
 * ACT_ID_*:身份信息表,版本7以上已废弃
 * ->ACT_ID_USER:用户基本信息
 * ->ACT_ID_INFO:用户扩展信息
 * ->ACT_ID_GROUP:群组
 * ->ACT_ID_MEMBERSHIP:用户和群组关联
 * 
 * ACT_EVT_LOG:事件日志表,{@link EventLogEntryEntityImpl}
 * ACT_PROCDED_INFO:流程定义动态改变信息表
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
 * Spring集成Activiti,在启动时会默认加载资源目录下的/processes/目录下的流程(bpmn)文件:
 * 
 * <pre>
 * {@link ProcessEngineAutoConfiguration}:自动注入Activiti相关配置,如数据库,事务,历史数据库等,
 * 		构建{@link SpringProcessEngineConfiguration}并注入Spring上下文
 * {@link ProcessEngineFactoryBean}:利用自动注入的{@link ProcessEngineConfiguration}构建{@link ProcessEngine}
 * {@link ActivitiProperties}:Activiti相关配置,定义资源文件目录,文件后缀,是否使用历史数据等
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-08-09 20:46:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Activiti {

	public static void main(String[] args) throws IOException {
		// 创建流程引擎
		ProcessEngine processEngine = createProcessEngine();
		// 部署流程定义文件
		ProcessDefinition definition = createProcessDefinition(processEngine, null);
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
		// ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
		// ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("流程图的xml文件地址");
		// ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream("流程文件的流地址");

		// 设置历史数据的级别
		pec.setHistoryLevel(null)
				// 设置邮箱用户名
				.setMailServerUsername(null);
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

	/**
	 * 流程部署相关
	 * 
	 * <pre>
	 * 每次流程图有修改以后,都需要重新部署,流程引擎才能用到新的流程
	 * 以前旧的流程都是存储在数据库中,新的流程也要存在数据库中,多个相同流程默认会使用最新版本
	 * 每次部署都会保存一个新的流程定义act_re_procdef
	 * 流程定义的key是用来识别版本是否叠加的
	 * </pre>
	 * 
	 * @param processEngine 流程引擎
	 * @return
	 * @throws IOException
	 */
	public static ProcessDefinition createProcessDefinition(ProcessEngine processEngine, MultipartFile multipartFile)
			throws IOException {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("流程图的xml文件地址").addClasspathResource("可同时部署多个,文件放在resource目录下");
		// 由上传的文件流程bpmn文件进行部署:文件名,流程文件数据
		deploymentBuilder.addBytes(multipartFile.getOriginalFilename(), multipartFile.getBytes());
		// 由上传的zip文件部署
		ZipInputStream zipInputStream = new ZipInputStream(multipartFile.getInputStream());
		deploymentBuilder.addZipInputStream(zipInputStream);
		deploymentBuilder.name("设备流程图的名称,也可不设置");
		// deploymentBuilder.tenantId("租户ID,相当于一个标识符");
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
		// 普通删除,如果当前规则下有正在执行的流程,抛异常
		// repositoryService.deleteDeployment(deployId);
		// 级联删除,会删除和当前规则相关的所有信息,正在执行的信息,包括历史信息
		// repositoryService.deleteDeployment(deployId, true);
		System.out.println(deployId);
		// 流程定义对象
		ProcessDefinition definition =
				repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
		// 获得流程定义和流程定义图片
		repositoryService.getResourceAsStream(deployId, definition.getDiagramResourceName());
		repositoryService.getResourceAsStream(deployId, definition.getResourceName());
		// 判断流程是否挂起
		System.out.println(definition.isSuspended());
		// 流程挂起,该流程定义下所有流程实例都将挂起
		// repositoryService.suspendProcessDefinitionById(definition.getId());
		// 流程激活
		// repositoryService.activateProcessDefinitionById(definition.getId());
		// 查询所有的流程定义
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		for (ProcessDefinition processDefinition : list) {
			System.out.println("流程id,流程框架生成的:" + processDefinition.getId());
			System.out.println("流程name,画流程时的流程名:" + processDefinition.getName());
			System.out.println("流程的key,画流程时的id" + processDefinition.getKey());
			System.out.println("流程的资源信息:" + processDefinition.getResourceName() + "==>"
					+ processDefinition.getDiagramResourceName());
			System.out.println("流程的版本号:" + processDefinition.getVersion());
			System.out.println("=====================");
		}
		return definition;
	}

	public static ProcessInstance createProcessInstance(ProcessEngine processEngine, ProcessDefinition definition) {
		// 流程启动
		RuntimeService runtimeService = processEngine.getRuntimeService();
		// 流程启动时的参数
		Map<String, Object> variables = MapTool.builder("test", "test").build();
		// 启动流程引擎时设置参数,用流程定义的id或key或message启动,推荐使用key,因为唯一,id每次部署都会改变
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("key", variables);
		// businessKey通常可以是数据库中某个表的主键或唯一值,用于关联流程信息和业务信息
		// runtimeService.startProcessInstanceByKey("key","businessKey");
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
		// 挂起单个的流程实例
		runtimeService.suspendProcessInstanceById(processInstance.getId());
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
		// 将任务指定待办人,只能单次指定,若多次指定会抛出异常
		taskService.claim(task.getId(), "userid");
		// 指定任务的待办人,可重复指定,但要慎用,避免多次重复指定不同待办人,最好指定之前查询是否指定待办人
		taskService.setAssignee(task.getId(), "userid");
		// 指定任务的候选人,可多次添加
		taskService.addCandidateUser(task.getId(), "userId");
		// 指定任务的候选组
		taskService.addCandidateGroup(task.getId(), "groupId");
		// 设置任务局部变量,也可以在DelegateTask,TaskListener中设置
		taskService.setVariableLocal(null, null, task);
		// 查询没有指定待办人,但是候选列表中有特定待办人的任务
		taskService.createTaskQuery().taskCandidateUser("userid").taskUnassigned().listPage(0, 100);
		// 获得已经指定了待办人的任务列表
		taskService.getIdentityLinksForTask(task.getId());
		taskService.createTaskQuery()
				// 返回指定名称的任务
				.taskName("name").taskNameLike("name")
				// 返回指定描述的任务
				.taskDescription("descripe").taskDescriptionLike("descripe")
				// 返回指定优先级的任务
				.taskPriority(1)
				// 返回指定优先级以下/以上的任务
				.taskMaxPriority(5).taskMinPriority(1)
				// 返回指定了userid为待办人的任务
				.taskAssignee("userid").taskAssigneeLike("userid")
				// 返回指定拥有者的任务
				.taskOwner("owner").taskOwnerLike("owner")
				// 返回没有指定待办人的任务,如果传false,该值会被忽略
				.taskUnassigned()
				// 返回指定代理状态的任务
				.taskDelegationState(DelegationState.PENDING)
				// 返回可以被指定用户领取的任务,包含将用户设置为直接候选人和用户作为候选群组一员的情况
				.taskCandidateUser("user")
				// 返回可以被指定群组中用户领取的任务
				.taskCandidateGroup("group")
				// 返回指定用户参与过的任务
				.taskInvolvedUser("user")
				// 返回指定任务定义id的任务
				.taskDefinitionKey("key").taskDefinitionKeyLike("key")
				// 返回作为指定id的流程实例的一部分任务
				.processInstanceId("processinstanceid")
				// 返回作为指定key的流程实例的一部分任务
				.processInstanceBusinessKey("businesskey").processDefinitionKeyLike("businesskey")
				// 返回作为指定流程定义key的流程实例的一部分任务,即查看流程到了那一步
				.processDefinitionId("processdefinitionId").processDefinitionKey("processdefinitionkey")
				.processDefinitionKeyLike("processdefinitionkey")
				// 返回作为指定流程定义名称的流程实例的一部分任务
				.processDefinitionName("name").processDefinitionNameLike("name")
				// 返回作为指定id分支的一分部的任务
				.executionId("exeid")
				// 返回只当创建时间的任务
				.taskCreatedOn(null).taskCreatedBefore(null).taskCreatedAfter(null)
				// 返回指定持续时间的任务
				.taskDueDate(null).taskDueBefore(null).taskDueAfter(null)
				// 返回没有设置持续时间的任务,如果值为false,则忽略该值
				.withoutTaskDueDate()
				// 返回非子任务的任务
				.excludeSubtasks()
				// 若为true,只返回未挂起的任务;若为false,只返回作为挂起流程一部分的任务
				.active()
				// 返回包含任务变量的任务
				.includeTaskLocalVariables()
				// 返回结果中包含变量的任务
				.includeProcessVariables()
				// 排序
				.orderByTaskCreateTime()
				// 分页
				.listPage(0, 100);
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
	 * 对历史数据进行操作,即使流程结束,该数据仍然保存在数据库中,需要开启相关配置.注意清理历史数据
	 * 
	 * @param processEngine 流程引擎实例
	 */
	public static void handlerHistory(ProcessEngine processEngine) {
		HistoryService historyService = processEngine.getHistoryService();
		// HistoricProcessInstance:历史流程实例实体,包含当前和已经结束的流程实例信息
		historyService.createHistoricProcessInstanceQuery()
				// 指定历史流程实例id
				.processInstanceId("")
				// 历史流程实例的流程定义key
				.processDefinitionKey("")
				// 历史流程实例的流程定义id
				.processDefinitionId("")
				// 历史流程实例的businessKey
				.processInstanceBusinessKey("")
				// 历史流程实例的参与者
				.involvedUser("")
				// 返回历史流程实例结束历史
				.finished().finishedBefore(null).finishedAfter(null)
				// 历史流程实例的上级流程实例id
				.superProcessInstanceId("")
				// 返回非子流程的历史流程实例
				.excludeSubprocesses(true)
				// 返回指定时间开始的历史流程实例
				.startedBy(null).startedAfter(null).startedBefore(null)
				// 是否应该返回历史流程实例变量
				.includeProcessVariables().list();
		// HistoricTaskInstance:用户任务实例的信息,包含关于当前和过去的(已完成或已删除)任务实例信息
		historyService.createHistoricTaskInstanceQuery()
				// 历史任务实例id
				.taskId("")
				// 历史任务实例的流程实例id
				.processInstanceId("")
				// 历史任务实例的流程定义key
				.processDefinitionKey("").processDefinitionKeyLike("")
				// 历史任务实例的流程定义id
				.processDefinitionId("")
				// 历史任务实例的流程定义名称
				.processDefinitionName("").processDefinitionNameLike("")
				// 历史任务实例的流程实例businessKey
				.processInstanceBusinessKey(null).processInstanceBusinessKeyLike(null)
				.processInstanceBusinessKeyLikeIgnoreCase(null)
				// 历史任务实例的分支id
				.executionId(null)
				// 流程的任务部分的流程定义key
				.taskDefinitionKey(null)
				// 历史任务实例的任务名称
				.taskName(null).taskNameLike(null).taskNameLikeIgnoreCase(null)
				// 历史任务实例的任务描述
				.taskDescription(null).taskDescriptionLike(null).taskDescriptionLikeIgnoreCase(null)
				// 历史任务实例对应的流程定义的任务定义key
				.taskDefinitionKey(null).taskDefinitionKeyLike(null)
				// 历史任务实例的删除任务原因
				.taskDeleteReason(null).taskDeleteReasonLike(null)
				// 历史任务实例的负责人
				.taskAssignee(null).taskAssigneeLike(null).taskAssigneeLikeIgnoreCase(null)
				// 历史任务实例的原拥有者
				.taskOwner(null).taskOwnerLike(null).taskOwnerLikeIgnoreCase(null)
				// 历史任务实例的参与者
				.taskInvolvedUser(null)
				// 历史任务实例的优先级
				.taskPriority(null)
				// 表示是否历史任务实例已经结束了
				.finished().unfinished()
				// 表示历史任务实例的流程实例是否已经结束了
				.processFinished()
				// 历史任务实例的可能的上级任务id
				.taskParentTaskId(null)
				// 返回指定持续时间的历史任务实例
				.taskDueDate(null).taskDueBefore(null).taskDueAfter(null)
				// 返回没有设置持续时间的历史任务实例,当设置为false时会忽略这个参数
				.withoutTaskDueDate()
				// 返回在指定时间完成的历史任务实例
				.taskCompletedOn(null).taskCompletedBefore(null).taskCompletedAfter(null)
				// 返回指定创建时间的历史任务实例
				.taskCreatedOn(null).taskCreatedBefore(null).taskCreatedAfter(null)
				// 表示是否应该返回历史任务实例局部变量
				.includeTaskLocalVariables()
				// 表示是否应该返回历史任务实例全局变量
				.includeProcessVariables()
				// 返回删除原因包含指定字符串的历史任务
				.taskDeleteReason(null).taskDeleteReasonLike(null)
				// 花费时间最长的排序
				.orderByHistoricTaskInstanceDuration().list();
		// HistoricVariableInstance:流程或任务变量值的实体,包含最新的流程变量或任务变量
		historyService.createHistoricVariableInstanceQuery()
				// 历史变量实例的流程实例id
				.processInstanceId(null)
				// 历史变量实例的任务id
				.taskId(null).taskIds(null)
				// 表示从结果中排除任务变量
				.excludeTaskVariables()
				// 历史变量实例的变量名称
				.variableName(null).variableNameLike(null).list();
		// HistoricActivityInstance:单个活动节点执行的信息,包含一个活动(流程上的节点)的执行信息
		historyService.createHistoricActivityInstanceQuery()
				// 活动实例id
				.activityId(null)
				// 历史活动实例id
				.activityInstanceId(null)
				// 历史活动实例的名称
				.activityName(null)
				// 历史活动实例的元素类型,如serviceTask
				.activityType(null)
				// 历史活动实例的分支id
				.executionId(null)
				// 表示历史活动实例是否完成
				.finished()
				// 历史活动实例的负责人
				.taskAssignee(null)
				// 历史活动实例的流程实例id
				.processInstanceId(null)
				// 历史活动实例的流程定义id
				.processDefinitionId(null).list();
		// HistoricDetail:历史流程活动任务详细信息,包含历史流程实例,活动实例,任务实例的各种信息
		historyService.createHistoricDetailQuery()
				// 流程实例中产生的可变更新信息,某些变量名可能包含多个HistoricVariableUpdate实体
				.variableUpdates()
				// 历史细节的id
				.id(null)
				// 历史细节的流程实例id
				.processInstanceId(null)
				// 历史细节的分支id
				.executionId(null)
				// 历史细节的活动实例id
				.activityInstanceId(null)
				// 历史细节的任务id
				.taskId(null).list();
		// 删除流程实例相关的历史
		historyService.deleteHistoricProcessInstance("processInstanceId");
		// 删除任务相关的历史
		historyService.deleteHistoricTaskInstance("taskId");
	}
}