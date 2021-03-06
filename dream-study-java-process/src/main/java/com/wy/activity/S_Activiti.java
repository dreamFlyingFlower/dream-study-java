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
 * Activiti????????????,????????????????????????{@link https://bpmn.io/}
 * 
 * ????????????:
 * 
 * <pre>
 * ????????????:??????,???????????????????????????,?????????????????????????????????
 * ????????????:??????????????????,???????????????????????????????????????????????????
 * ????????????:?????????????????????????????????
 * ??????:???????????????????????????????????????????????????
 * ??????,??????:Start,End,????????????????????????
 * ??????:Gateway,???????????????????????????
 * </pre>
 * 
 * Activiti??????????????????,??????API{@link ProcessEnigne},????????????API????????????service:
 * 
 * <pre>
 * {@link RepositoryService}:?????????????????????????????????,????????????????????????,??????????????????xml
 * ->{@link Deployment}:????????????????????????
 * ->{@link DeploymentBuilder}:?????????????????????
 * ->{@link DeploymentQuery}:?????????????????????
 * ->{@link ProcessDefinition}:????????????????????????
 * ->{@link ProcessDefinitionQuery}:??????????????????????????????
 * ->{@link BpmnModel}:???????????????Java??????
 * {@link RuntimeService}:???????????????????????????????????????,??????????????????,?????????????????????.????????????id??????,???key?????????,??????key?????????
 * ->{@link ProcessInstance}:????????????,?????????Execution.????????????????????????????????????,??????ProcessInstance?????????????????????Execution
 * ->{@link Execution}:???????????????,????????????????????????????????????
 * {@link TaskService}:????????????????????????????????????,???????????????????????????(?????????,?????????,?????????).?????????????????????????????????,?????????????????????
 * {@link HistoryService}:????????????????????????????????????????????? 
 * {@link ManagementService}:?????????????????????,?????????,???????????????????????????
 * {@link DynamicBpmnService}:????????????????????????????????????
 * {@link FormService}:?????????????????????????????????????????????,7??????????????????
 * </pre>
 * 
 * ????????????:
 * 
 * <pre>
 * {@link ReceiveTask}:??????trigger??????ReceiveTask??????
 * {@link SignalEventReceivedCmd}:????????????????????????signalEventReceived,??????????????????
 * {@link MessageEventReceivedCmd}:????????????????????????messageEventReceived,?????????????????????????????????
 * </pre>
 * 
 * activiti????????????:
 * 
 * <pre>
 * {@link ActivitiEvent}:??????????????????
 * {@link ActivitiEventListener}:???????????????,???????????????????????????????????????????????????
 * {@link ActivitiEventType}:????????????,????????????????????????????????????
 * {@link BaseEntityEventListener}:???????????????????????????,???????????????????????????,?????????????????????
 * {@link ProcessEngineConfigurationImpl#eventListeners}:?????????ActivitiEventListener??????????????????
 * {@link ProcessEngineConfigurationImpl#typedEventListeners}:????????????ActivitiEventListener????????????
 * </pre>
 * 
 * Command???????????????,?????????????????????????????????????????????:
 * 
 * <pre>
 * {@link CommandInterceptor}:Command??????????????????
 * {@link AbstractCommandInterceptor}:??????????????????????????????
 * {@link ProcessEngineConfigurationImpl#customPreCommandInterceptors}:????????????????????????,?????????????????????
 * {@link ProcessEngineConfigurationImpl#customPostCommandInterceptors}:????????????????????????,?????????????????????
 * {@link ProcessEngineConfigurationImpl#commandInvoker}:???????????????????????????
 * </pre>
 * 
 * ???????????????{@link BpmnParser}
 * 
 * <pre>
 * {@link BpmnParser}:??????????????????,BpmnParser????????????????????????{@link BpmnParse}??????,??????????????????????????????????????????????????????.
 * {@link BpmnParseHandler}:??????????????????????????????????????????
 * {@link ProcessEngineConfigurationImpl#setPreBpmnParseHandlers}:????????????????????????,?????????????????????
 * {@link ProcessEngineConfigurationImpl#setPostBpmnParseHandlers}:????????????????????????,?????????????????????
 * {@link ProcessEngineConfigurationImpl#setCustomDefaultBpmnParseHandlers}:????????????BpmnParseHandler??????,?????? 
 * {@link AbstractBpmnParseHandler}:??????????????????????????????,???????????????,??????getHandledType()???executeParse()
 * 
 * ????????????:????????????BPMN2.0??????,?????????????????????????????????BpmnParseHandler??????.
 * 		????????????????????????BPMN2.0?????????BpmnParseHandler???????????????.
 * 		Activiti????????????BpmnParseHandler??????????????????????????????,????????????????????????????????????,?????????????????????.
 * 		???Activiti???????????????????????????BpmnParseHandler??????,?????????????????????????????????????????????????????????????????????????????????????????????
 * </pre>
 * 
 * ??????(Job)?????????,??????????????????????????????:
 * 
 * <pre>
 * {@link AsyncExecutor}:???????????????????????????
 * {@link ProcessEngineConfiguration#asyncExecutorActivate}:???????????????????????????
 * {@link ProcessEngineConfiguration#asyncExecutor}:????????????????????????
 * ->{@link DefaultAsyncJobExecutor}:??????????????????????????????
 * {@link TimerStartEventJobHandler}:???????????????????????????
 * timeDate:??????????????????,????????????xml???startEvent???????????????
 * timeDuration:?????????????????????????????????,????????????xml???startEvent???????????????
 * timeCycle:??????????????????????????????,????????????xml???startEvent???????????????.???R5/PT10S,??????5???,??????10S
 * 
 * {@link JobQuery}:??????????????????
 * {@link TimerJobQuery}:??????????????????
 * {@link SuspendedJobQuery}:??????????????????
 * {@link DeadLetterJobQuery}:???????????????????????????
 * </pre>
 * 
 * ??????????????????,???????????????????????????????????????:
 * 
 * <pre>
 * none:?????????,????????????????????????.??????????????????????????????????????????,?????????????????????????????????
 * 	activity:?????????????????????????????????????????????.????????????????????????,??????????????????????????????????????????????????????????????????.???????????????????????????
 * 	audit:??????????????????????????????,????????????,?????????????????????????????????????????????????????????.????????????????????????????????????????????????,??????????????????
 * 	full:?????????????????????????????????,???????????????.??????????????????????????????????????????????????????????????????,???????????????????????????
 * </pre>
 *
 * ???????????????:
 * 
 * <pre>
 * ACT_GE_*:???????????????
 * ->ACT_GE_PROPERTY:?????????,?????????????????????kv????????????,{@link PropertyEntityImpl}
 * ->ACT_GE_BYTEARRAY:?????????,?????????????????????????????????,{@link ByteArrayEntityImpl}
 * 
 * ACT_RE_*:?????????????????????.RE??????repository,??????????????????????????????????????????(??????,??????,??????)
 * ->ACT_RE_DEPLOYMENT:?????????????????????,{@link DeploymentEntityImpl}
 * ->ACT_RE_PROCDEF:?????????????????????,{@link ProcessDefinitionEntityImpl}
 * ->ACT_RE_MODEL:???????????????(??????web?????????)
 * 
 * ACT_RU_*:?????????????????????.RU??????runtime,??????????????????,??????,??????,?????????????????????????????????.
 * 		Activiti???????????????????????????????????????????????????,??????????????????????????????????????????
 * ->ACT_RU_EXECUTION:?????????????????????????????????,{@link ExecutionEntityImpl}
 * ->ACT_RU_TASK:??????????????????,{@link TaskEntityImpl}
 * ->ACT_RU_VARIABLE:????????????,{@link VariableInstanceEntityImpl}
 * ->ACT_RU_IDENTITYLINK:?????????????????????,{@link IdentityLinkEntityImpl}
 * ->ACT_RU_EVENT_SUBSCR:???????????????,{@link EventSubscriptionEntityImpl}
 * ->ACT_RU_JOB:?????????,{@link JobEntityImpl}
 * ->ACT_RU_TIMER_JOB:????????????,{@link TimerJobEntityImpl}
 * ->ACT_RU_SUSPENDED_JOB:???????????????
 * ->ACT_RU_DEADLETTER_JOB:?????????
 * 
 * ACT_HI_*:??????????????????.HI??????history,???????????????????????????,????????????????????????,??????,????????????
 * ->ACT_HI_PROCINST:?????????????????????,{@link HistoricProcessInstanceEntityImpl}
 * ->ACT_HI_ACTINST:?????????????????????
 * ->ACT_HI_TASKINST:???????????????
 * ->ACT_HI_VARINST:????????????
 * ->ACT_HI_IDENTITYLINK:???????????????
 * ->ACT_HI_DETAIL:????????????
 * ->ACT_HI_ATTACHMENT:??????
 * ->ACT_HI_COMMENT:??????
 * ->ACT_HI_LOG:????????????
 * 
 * ACT_ID_*:???????????????,??????7???????????????
 * ->ACT_ID_USER:??????????????????
 * ->ACT_ID_INFO:??????????????????
 * ->ACT_ID_GROUP:??????
 * ->ACT_ID_MEMBERSHIP:?????????????????????
 * 
 * ACT_EVT_LOG:???????????????,{@link EventLogEntryEntityImpl}
 * ACT_PROCDED_INFO:?????????????????????????????????
 * </pre>
 * 
 * ??????????????????:
 * 
 * <pre>
 * {@link ServiceTask}:???????????????????????????????????????
 * {@link SendTask}:????????????,????????????????????????????????????
 * {@link ReceiveTask}:????????????,?????????????????????????????????
 * {@link UserTask}:????????????????????????
 * {@link ScriptTask}:????????????,??????????????????????????????
 * </pre>
 * 
 * ????????????:
 * 
 * <pre>
 * Exclusive Gateway:????????????,????????????,???????????????????????????X
 * Parallel Gateway:????????????,????????????????????????,??????????????????????????????????????????.???????????????????????????+
 * Inclusive Gateway:???????????????,?????????????????????????????????.??????????????????????????????
 * Event-based Gateway:??????????????????,???????????????????????????????????????.???????????????????????????????????????
 * </pre>
 * 
 * ?????????:
 * 
 * <pre>
 * Sub-Process:?????????
 * Event Sub-Process:???????????????
 * Transcation Sub-Process:???????????????
 * Call Activity:??????????????????
 * </pre>
 * 
 * Spring??????Activiti,?????????????????????????????????????????????/processes/??????????????????(bpmn)??????:
 * 
 * <pre>
 * {@link ProcessEngineAutoConfiguration}:????????????Activiti????????????,????????????,??????,??????????????????,
 * 		??????{@link SpringProcessEngineConfiguration}?????????Spring?????????
 * {@link ProcessEngineFactoryBean}:?????????????????????{@link ProcessEngineConfiguration}??????{@link ProcessEngine}
 * {@link ActivitiProperties}:Activiti????????????,????????????????????????,????????????,???????????????????????????
 * </pre>
 * 
 * @author ????????????
 * @date 2021-08-09 20:46:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_Activiti {

	public static void main(String[] args) throws IOException {
		// ??????????????????
		ProcessEngine processEngine = createProcessEngine();
		// ????????????????????????
		ProcessDefinition definition = createProcessDefinition(processEngine, null);
		// ????????????
		createManagement(processEngine);
		// ??????????????????
		ProcessInstance processInstance = createProcessInstance(processEngine, definition);
		// ??????????????????
		handlerTask(processEngine, processInstance);
		// ??????????????????
		handlerHistory(processEngine);
	}

	public static ProcessEngine createProcessEngine() {
		// ?????????????????????????????????
		ProcessEngineConfiguration pec = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		// ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
		// ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("????????????xml????????????");
		// ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream("????????????????????????");

		// ???????????????????????????
		pec.setHistoryLevel(null)
				// ?????????????????????
				.setMailServerUsername(null);
		ProcessEngine processEngine = pec.buildProcessEngine();
		String name = processEngine.getName();
		String version = ProcessEngine.VERSION;
		System.out.println(name);
		System.out.println(version);
		return processEngine;
	}

	public static void createManagement(ProcessEngine processEngine) {
		// ??????managermentservice,????????????????????????
		ManagementService managementService = processEngine.getManagementService();
		// ??????????????????????????????????????????,key?????????,value????????????,?????????????????????
		Map<String, Long> tableCount = managementService.getTableCount();
		for (Map.Entry<String, Long> keEntry : tableCount.entrySet()) {
			System.out.println(keEntry);
		}
		managementService.executeCommand((context) -> {
			context.getResult();
			return context;
		});
		// ????????????????????????
		List<Job> timerJobs = managementService.createTimerJobQuery().listPage(0, 100);
		for (Job job : timerJobs) {
			System.out.println(job);
		}
	}

	/**
	 * ??????????????????
	 * 
	 * <pre>
	 * ??????????????????????????????,?????????????????????,????????????????????????????????????
	 * ?????????????????????????????????????????????,????????????????????????????????????,?????????????????????????????????????????????
	 * ????????????????????????????????????????????????act_re_procdef
	 * ???????????????key????????????????????????????????????
	 * </pre>
	 * 
	 * @param processEngine ????????????
	 * @return
	 * @throws IOException
	 */
	public static ProcessDefinition createProcessDefinition(ProcessEngine processEngine, MultipartFile multipartFile)
			throws IOException {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("????????????xml????????????").addClasspathResource("?????????????????????,????????????resource?????????");
		// ????????????????????????bpmn??????????????????:?????????,??????????????????
		deploymentBuilder.addBytes(multipartFile.getOriginalFilename(), multipartFile.getBytes());
		// ????????????zip????????????
		ZipInputStream zipInputStream = new ZipInputStream(multipartFile.getInputStream());
		deploymentBuilder.addZipInputStream(zipInputStream);
		deploymentBuilder.name("????????????????????????,???????????????");
		// deploymentBuilder.tenantId("??????ID,????????????????????????");
		// ??????
		Deployment deploy = deploymentBuilder.deploy();
		// ??????????????????????????????ID
		String deployId = deploy.getId();
		DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
		// ????????????id???????????????,??????????????????deploy
		deploymentQuery.deploymentId(deployId).singleResult();
		// ??????????????????????????????????????????????????????
		repositoryService.createProcessDefinitionQuery().deploymentId(deployId).list();
		// ????????????????????????????????????????????????,???????????????
		repositoryService.addCandidateStarterUser(deployId, "userId");
		repositoryService.addCandidateStarterGroup(deployId, "groupid");
		// ????????????,?????????????????????????????????????????????,?????????
		// repositoryService.deleteDeployment(deployId);
		// ????????????,?????????????????????????????????????????????,?????????????????????,??????????????????
		// repositoryService.deleteDeployment(deployId, true);
		System.out.println(deployId);
		// ??????????????????
		ProcessDefinition definition =
				repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
		// ???????????????????????????????????????
		repositoryService.getResourceAsStream(deployId, definition.getDiagramResourceName());
		repositoryService.getResourceAsStream(deployId, definition.getResourceName());
		// ????????????????????????
		System.out.println(definition.isSuspended());
		// ????????????,????????????????????????????????????????????????
		// repositoryService.suspendProcessDefinitionById(definition.getId());
		// ????????????
		// repositoryService.activateProcessDefinitionById(definition.getId());
		// ???????????????????????????
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		for (ProcessDefinition processDefinition : list) {
			System.out.println("??????id,?????????????????????:" + processDefinition.getId());
			System.out.println("??????name,????????????????????????:" + processDefinition.getName());
			System.out.println("?????????key,???????????????id" + processDefinition.getKey());
			System.out.println("?????????????????????:" + processDefinition.getResourceName() + "==>"
					+ processDefinition.getDiagramResourceName());
			System.out.println("??????????????????:" + processDefinition.getVersion());
			System.out.println("=====================");
		}
		return definition;
	}

	public static ProcessInstance createProcessInstance(ProcessEngine processEngine, ProcessDefinition definition) {
		// ????????????
		RuntimeService runtimeService = processEngine.getRuntimeService();
		// ????????????????????????
		Map<String, Object> variables = MapTool.builder("test", "test").build();
		// ?????????????????????????????????,??????????????????id???key???message??????,????????????key,????????????,id????????????????????????
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("key", variables);
		// businessKey?????????????????????????????????????????????????????????,???????????????????????????????????????
		// runtimeService.startProcessInstanceByKey("key","businessKey");
		System.out.println(processInstance.getActivityId());
		// ProcessInstance processInstance =
		// runtimeService.startProcessInstanceById(definition.getId());
		// ?????????????????????????????????
		runtimeService.getVariables(processInstance.getId());
		// ??????????????????????????????????????????
		runtimeService.setVariable(processInstance.getId(), "test1", "test1");
		// ????????????????????????????????????activit???
		runtimeService.addEventListener(new MyActivitiListener());
		// ???????????????????????????
		runtimeService.dispatchEvent(new ActivitiEventImpl(ActivitiEventType.CUSTOM));
		// ???????????????????????????
		runtimeService.suspendProcessInstanceById(processInstance.getId());
		// ???????????????????????????????????????,???processInstance????????????
		List<Execution> executions = runtimeService.createExecutionQuery().list();
		for (Execution execution : executions) {
			System.out.println(execution.getId());
		}
		// ????????????????????????
		Execution execution = runtimeService.createExecutionQuery().activityId("???id???xml????????????????????????id").singleResult();
		runtimeService.trigger(execution.getId());
		// ??????????????????
		runtimeService.createExecutionQuery().signalEventSubscriptionName("???name???xml?????????signal?????????name").singleResult();
		runtimeService.signalEventReceived("???name???xml?????????signal?????????name");
		// ??????????????????
		Execution execution2 = runtimeService.createExecutionQuery()
				.messageEventSubscriptionName("???name???xml?????????message?????????name").singleResult();
		runtimeService.messageEventReceived("???name???xml?????????message?????????name", execution2.getId());
		// ????????????????????????
		processEngine.getManagementService()
				.getEventLogEntriesByProcessInstanceId(processInstance.getProcessInstanceId());
		// ?????????????????????????????????
		processEngine.getManagementService().createTimerJobQuery().list();
		// ????????????????????????
		return processInstance;
	}

	/**
	 * ???????????????,?????????,??????????????????????????????,??????,event???
	 * 
	 * @param processEngine ????????????
	 * @param processInstance ????????????
	 */
	public static void handlerTask(ProcessEngine processEngine, ProcessInstance processInstance) {
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		// ??????????????????????????????,?????????
		taskService.setOwner(task.getId(), "userid");
		// ????????????????????????,??????????????????,??????????????????????????????
		taskService.claim(task.getId(), "userid");
		// ????????????????????????,???????????????,????????????,???????????????????????????????????????,?????????????????????????????????????????????
		taskService.setAssignee(task.getId(), "userid");
		// ????????????????????????,???????????????
		taskService.addCandidateUser(task.getId(), "userId");
		// ????????????????????????
		taskService.addCandidateGroup(task.getId(), "groupId");
		// ????????????????????????,????????????DelegateTask,TaskListener?????????
		taskService.setVariableLocal(null, null, task);
		// ???????????????????????????,????????????????????????????????????????????????
		taskService.createTaskQuery().taskCandidateUser("userid").taskUnassigned().listPage(0, 100);
		// ?????????????????????????????????????????????
		taskService.getIdentityLinksForTask(task.getId());
		taskService.createTaskQuery()
				// ???????????????????????????
				.taskName("name").taskNameLike("name")
				// ???????????????????????????
				.taskDescription("descripe").taskDescriptionLike("descripe")
				// ??????????????????????????????
				.taskPriority(1)
				// ???????????????????????????/???????????????
				.taskMaxPriority(5).taskMinPriority(1)
				// ???????????????userid?????????????????????
				.taskAssignee("userid").taskAssigneeLike("userid")
				// ??????????????????????????????
				.taskOwner("owner").taskOwnerLike("owner")
				// ????????????????????????????????????,?????????false,??????????????????
				.taskUnassigned()
				// ?????????????????????????????????
				.taskDelegationState(DelegationState.PENDING)
				// ??????????????????????????????????????????,?????????????????????????????????????????????????????????????????????????????????
				.taskCandidateUser("user")
				// ???????????????????????????????????????????????????
				.taskCandidateGroup("group")
				// ????????????????????????????????????
				.taskInvolvedUser("user")
				// ????????????????????????id?????????
				.taskDefinitionKey("key").taskDefinitionKeyLike("key")
				// ??????????????????id?????????????????????????????????
				.processInstanceId("processinstanceid")
				// ??????????????????key?????????????????????????????????
				.processInstanceBusinessKey("businesskey").processDefinitionKeyLike("businesskey")
				// ??????????????????????????????key?????????????????????????????????,??????????????????????????????
				.processDefinitionId("processdefinitionId").processDefinitionKey("processdefinitionkey")
				.processDefinitionKeyLike("processdefinitionkey")
				// ?????????????????????????????????????????????????????????????????????
				.processDefinitionName("name").processDefinitionNameLike("name")
				// ??????????????????id???????????????????????????
				.executionId("exeid")
				// ?????????????????????????????????
				.taskCreatedOn(null).taskCreatedBefore(null).taskCreatedAfter(null)
				// ?????????????????????????????????
				.taskDueDate(null).taskDueBefore(null).taskDueAfter(null)
				// ???????????????????????????????????????,????????????false,???????????????
				.withoutTaskDueDate()
				// ???????????????????????????
				.excludeSubtasks()
				// ??????true,???????????????????????????;??????false,?????????????????????????????????????????????
				.active()
				// ?????????????????????????????????
				.includeTaskLocalVariables()
				// ????????????????????????????????????
				.includeProcessVariables()
				// ??????
				.orderByTaskCreateTime()
				// ??????
				.listPage(0, 100);
		// ?????????????????????
		taskService.createAttachment("url", task.getId(), processInstance.getId(), "???????????????", "???????????????", "http://url??????");
		taskService.getAttachment(task.getId());
		// ?????????????????????
		taskService.addComment(task.getId(), processInstance.getId(), "context");
		taskService.getComment(task.getId());
		// event??????????????????,?????????????????????????????????
		taskService.getTaskEvents(task.getId());
		List<Task> tasks = taskService.createTaskQuery().list();
		for (Task item : tasks) {
			System.out.println(item.getName());
			taskService.complete(item.getId());
			// ????????????,????????????????????????????????????????????????
			taskService.complete(item.getId(), MapTool.newHashMap());
			// ????????????
			processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstance.getId())
					.singleResult();
		}
	}

	/**
	 * ???????????????????????????,??????????????????,????????????????????????????????????,????????????????????????.????????????????????????
	 * 
	 * @param processEngine ??????????????????
	 */
	public static void handlerHistory(ProcessEngine processEngine) {
		HistoryService historyService = processEngine.getHistoryService();
		// HistoricProcessInstance:????????????????????????,????????????????????????????????????????????????
		historyService.createHistoricProcessInstanceQuery()
				// ????????????????????????id
				.processInstanceId("")
				// ?????????????????????????????????key
				.processDefinitionKey("")
				// ?????????????????????????????????id
				.processDefinitionId("")
				// ?????????????????????businessKey
				.processInstanceBusinessKey("")
				// ??????????????????????????????
				.involvedUser("")
				// ????????????????????????????????????
				.finished().finishedBefore(null).finishedAfter(null)
				// ???????????????????????????????????????id
				.superProcessInstanceId("")
				// ???????????????????????????????????????
				.excludeSubprocesses(true)
				// ?????????????????????????????????????????????
				.startedBy(null).startedAfter(null).startedBefore(null)
				// ??????????????????????????????????????????
				.includeProcessVariables().list();
		// HistoricTaskInstance:???????????????????????????,??????????????????????????????(?????????????????????)??????????????????
		historyService.createHistoricTaskInstanceQuery()
				// ??????????????????id
				.taskId("")
				// ?????????????????????????????????id
				.processInstanceId("")
				// ?????????????????????????????????key
				.processDefinitionKey("").processDefinitionKeyLike("")
				// ?????????????????????????????????id
				.processDefinitionId("")
				// ???????????????????????????????????????
				.processDefinitionName("").processDefinitionNameLike("")
				// ?????????????????????????????????businessKey
				.processInstanceBusinessKey(null).processInstanceBusinessKeyLike(null)
				.processInstanceBusinessKeyLikeIgnoreCase(null)
				// ???????????????????????????id
				.executionId(null)
				// ????????????????????????????????????key
				.taskDefinitionKey(null)
				// ?????????????????????????????????
				.taskName(null).taskNameLike(null).taskNameLikeIgnoreCase(null)
				// ?????????????????????????????????
				.taskDescription(null).taskDescriptionLike(null).taskDescriptionLikeIgnoreCase(null)
				// ??????????????????????????????????????????????????????key
				.taskDefinitionKey(null).taskDefinitionKeyLike(null)
				// ???????????????????????????????????????
				.taskDeleteReason(null).taskDeleteReasonLike(null)
				// ??????????????????????????????
				.taskAssignee(null).taskAssigneeLike(null).taskAssigneeLikeIgnoreCase(null)
				// ?????????????????????????????????
				.taskOwner(null).taskOwnerLike(null).taskOwnerLikeIgnoreCase(null)
				// ??????????????????????????????
				.taskInvolvedUser(null)
				// ??????????????????????????????
				.taskPriority(null)
				// ?????????????????????????????????????????????
				.finished().unfinished()
				// ????????????????????????????????????????????????????????????
				.processFinished()
				// ??????????????????????????????????????????id
				.taskParentTaskId(null)
				// ?????????????????????????????????????????????
				.taskDueDate(null).taskDueBefore(null).taskDueAfter(null)
				// ???????????????????????????????????????????????????,????????????false????????????????????????
				.withoutTaskDueDate()
				// ????????????????????????????????????????????????
				.taskCompletedOn(null).taskCompletedBefore(null).taskCompletedAfter(null)
				// ?????????????????????????????????????????????
				.taskCreatedOn(null).taskCreatedBefore(null).taskCreatedAfter(null)
				// ??????????????????????????????????????????????????????
				.includeTaskLocalVariables()
				// ??????????????????????????????????????????????????????
				.includeProcessVariables()
				// ??????????????????????????????????????????????????????
				.taskDeleteReason(null).taskDeleteReasonLike(null)
				// ???????????????????????????
				.orderByHistoricTaskInstanceDuration().list();
		// HistoricVariableInstance:?????????????????????????????????,??????????????????????????????????????????
		historyService.createHistoricVariableInstanceQuery()
				// ?????????????????????????????????id
				.processInstanceId(null)
				// ???????????????????????????id
				.taskId(null).taskIds(null)
				// ????????????????????????????????????
				.excludeTaskVariables()
				// ?????????????????????????????????
				.variableName(null).variableNameLike(null).list();
		// HistoricActivityInstance:?????????????????????????????????,??????????????????(??????????????????)???????????????
		historyService.createHistoricActivityInstanceQuery()
				// ????????????id
				.activityId(null)
				// ??????????????????id
				.activityInstanceId(null)
				// ???????????????????????????
				.activityName(null)
				// ?????????????????????????????????,???serviceTask
				.activityType(null)
				// ???????????????????????????id
				.executionId(null)
				// ????????????????????????????????????
				.finished()
				// ??????????????????????????????
				.taskAssignee(null)
				// ?????????????????????????????????id
				.processInstanceId(null)
				// ?????????????????????????????????id
				.processDefinitionId(null).list();
		// HistoricDetail:????????????????????????????????????,????????????????????????,????????????,???????????????????????????
		historyService.createHistoricDetailQuery()
				// ??????????????????????????????????????????,?????????????????????????????????HistoricVariableUpdate??????
				.variableUpdates()
				// ???????????????id
				.id(null)
				// ???????????????????????????id
				.processInstanceId(null)
				// ?????????????????????id
				.executionId(null)
				// ???????????????????????????id
				.activityInstanceId(null)
				// ?????????????????????id
				.taskId(null).list();
		// ?????????????????????????????????
		historyService.deleteHistoricProcessInstance("processInstanceId");
		// ???????????????????????????
		historyService.deleteHistoricTaskInstance("taskId");
	}
}