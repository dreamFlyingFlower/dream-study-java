package com.wy.flowable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.flowable.engine.DynamicBpmnService;
import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * Flowable文档:https://flowable.com/open-source/docs/bpmn/ch14-Applications/
 * 
 * Flowable-ui使用
 * 
 * <pre>
 * 1.从官网下载flowable-6.6.0: https://github.com/flowable/flowable-engine/releases/download/flowable-6.6.0/flowable-6.6.0.zip
 * 2.将压缩包中的 flowable-6.6.0\wars\flowable-ui.war 丢到Tomcat中跑起来
 * 3.打开http://localhost:8080/flowable-ui 用账户(admin/test)登录
 * 4.进入APP.MODELER创建流程,之后可以导出流程到项目中使用,或者配置tomcat下flowable-ui的配置文件flowable-default.properties连接本地数据库.
 * ->修改本地库时需要将mysql的jar包放到flowable-ui-rest/WEB-INF/lib下
 * </pre>
 * 
 * Flowable表结构,几乎和Activit完全相同:
 * 
 * <pre>
 * 通用数据表
 * act_ge_bytearray:二进制数据表,如流程定义、流程模板、流程图的字节流文件
 * act_ge_property:属性数据表(不常用)
 * 
 * 历史表,HistoryService接口操作的表
 * act_hi_actinst:历史节点表,存放流程实例运转的各个节点信息(包含开始、结束等非任务节点)
 * act_hi_attachment:历史附件表,存放历史节点上传的附件信息(不常用)
 * act_hi_comment:历史意见表
 * act_hi_detail:历史详情表,存储节点运转的一些信息(不常用)
 * act_hi_identitylink:历史流程人员表,存储流程各节点候选、办理人员信息,常用于查询某人或部门的已办任务
 * act_hi_procinst:历史流程实例表,存储流程实例历史数据(包含正在运行的流程实例)
 * act_hi_taskinst:历史流程任务表,存储历史任务节点
 * act_hi_varinst:流程历史变量表,存储流程历史节点的变量信息
 * 
 * 用户相关表,IdentityService接口操作的表
 * act_id_group:用户组信息表,对应节点选定候选组信息
 * act_id_info:用户扩展信息表,存储用户扩展信息
 * act_id_membership:用户与用户组关系表
 * act_id_user:用户信息表,对应节点选定办理人或候选人信息
 * 
 * 流程定义、流程模板相关表,RepositoryService接口操作的表
 * act_re_deployment:部属信息表,存储流程定义、模板部署信息
 * act_re_procdef:流程定义信息表,存储流程定义相关描述信息,但其真正内容存储在act_ge_bytearray表中,以字节形式存储
 * act_re_model:流程模板信息表,存储流程模板相关描述信息,但其真正内容存储在act_ge_bytearray表中,以字节形式存储
 * 
 * 流程运行时表,RuntimeService接口操作的表
 * act_ru_task:运行时流程任务节点表,存储运行中流程的任务节点信息,重要,常用于查询人员或部门的待办任务时使用
 * act_ru_event_subscr:监听信息表,不常用
 * act_ru_execution:运行时流程执行实例表,记录运行中流程运行的各个分支信息(当没有子流程时,其数据与act_ru_task表数据是一一对应的)
 * act_ru_identitylink:运行时流程人员表,重要,常用于查询人员或部门的待办任务时使用
 * act_ru_job:运行时定时任务数据表,存储流程的定时任务信息
 * act_ru_variable:运行时流程变量数据表,存储运行中的流程各节点的变量信息
 * </pre>
 * 
 * 主要类,和Activiti基本相同:
 * 
 * <pre>
 * {@link RepositoryService}:提供管理与控制部署(deployments)与流程定义(process definitions)的操作,管理静态信息
 * {@link RuntimeService}:用于启动流程定义的新流程实例
 * {@link IdentityService}:用于管理(创建,更新,删除,查询……)组与用户.Activiti已经废弃
 * {@link FormService}:可选服务,表单相关.Activiti已经废弃
 * {@link HistoryService}:暴露Flowable引擎收集的所有历史数据,提供查询历史数据的能力
 * {@link ManagementService}:通常在用Flowable编写用户应用时不需要使用,可以读取数据库表与表原始数据的信息,也提供了对作业(job)的查询与管理操作
 * {@link DynamicBpmnService}:用于修改流程定义中的部分内容,而不需要重新部署.例如可修改流程定义中一个用户任务的办理人设置,或修改一个服务任务中的类名
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2023-05-07 23:46:24
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Slf4j
public class FlowableConfig {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private org.flowable.engine.TaskService taskService;

	@Autowired
	private org.flowable.engine.IdentityService identityService;

	public void createDeploymentZip() {

		// 部署xml,压缩到zip形式,直接xml需要配置相对路径,麻烦,不建议使用
		try {
			File zipTemp = new File("f:/leave_approval.bpmn20.zip");
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipTemp));
			Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
			log.info("部署成功:{}", deployment.getId());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// 查询部署的流程定义
		List<ProcessDefinition> list =
				repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave_approval").list();
		list.forEach(t -> System.out.println(t));
		List<ProcessDefinition> pages =
				repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave_approval").listPage(1, 30);
		pages.forEach(t -> System.out.println(t));

		// 启动流程,创建实例
		// 流程定义的key,对应请假的流程图
		String processDefinitionKey = "leave_approval";
		// 业务代码,根据自己的业务用
		String businessKey = "schoolleave";
		// 流程变量,可以自定义扩充
		Map<String, Object> variablesDefinition = new HashMap<>();
		ProcessInstance processInstance =
				runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variablesDefinition);
		log.info("启动成功:{}", processInstance.getId());

		// 查询指定流程所有启动的实例列表 列表,或 分页 删除
		List<Execution> executions =
				runtimeService.createExecutionQuery().processDefinitionKey("leave_approval").list();
		executions.forEach(System.out::print);
		List<Execution> executionPages =
				runtimeService.createExecutionQuery().processDefinitionKey("leave_approval").listPage(1, 30);
		executionPages.forEach(System.out::print);
		// 删除实例
		// runtimeService.deleteProcessInstance(processInstanceId, deleteReason);

		// 学生查询可以操作的任务,并完成任务
		// 候选组 xml文件里面的 flowable:candidateGroups="stu_group"
		String candidateGroup = "stu_group";
		List<Task> taskList =
				taskService.createTaskQuery().taskCandidateGroup(candidateGroup).orderByTaskCreateTime().desc().list();
		for (Task task : taskList) {
			// 申领任务
			taskService.claim(task.getId(), "my");
			// 完成
			taskService.complete(task.getId());
		}

		// 老师查询可以操作的任务,并完成任务
		// 候选组 xml文件里面的 flowable:candidateGroups="te_group"
		String candidateGroupTe = "te_group";
		List<Task> taskListTe = taskService.createTaskQuery().taskCandidateGroup(candidateGroupTe)
				.orderByTaskCreateTime().desc().list();
		for (Task task : taskListTe) {
			// 申领任务
			taskService.claim(task.getId(), "myte");
			// 完成
			Map<String, Object> variables = new HashMap<>();
			// 携带变量,用于网关流程的条件判定,这里的条件是同意
			variables.put("command", "agree");
			taskService.complete(task.getId(), variables);
		}

		// 历史查询,因为一旦流程执行完毕,活动的数据都会被清空,上面查询的接口都查不到数据,但是提供历史查询接口
		// 历史流程实例
		List<HistoricProcessInstance> historicProcessList =
				historyService.createHistoricProcessInstanceQuery().processDefinitionKey("leave_approval").list();
		historicProcessList.forEach(System.out::print);
		// 历史任务
		List<HistoricTaskInstance> historicTaskList =
				historyService.createHistoricTaskInstanceQuery().processDefinitionKey("leave_approval").list();
		historicTaskList.forEach(System.out::print);
		// 实例历史变量 , 任务历史变量
		// historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId);
		// historyService.createHistoricVariableInstanceQuery().taskId(taskId);

		// 可能还需要的API
		// 移动任务,人为跳转任务
		// runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId)
		// .moveActivityIdTo(currentActivityTaskId, newActivityTaskId).changeState();

		// 如果在数据库配置了分组和用户,还会用到
		// 用户查询,用户id对应xml 里面配置的用户
		List<User> users = identityService.createUserQuery().list();
		users.forEach(System.out::print);
		// 分组查询,分组id对应xml 里面配置的分组 如 stu_group,te_group,在表里是id的值
		// 另外,每个查询后面都可以拼条件,包括模糊查询,大小比较等
		List<Group> groups = identityService.createGroupQuery().list();
		groups.forEach(System.out::print);
	}
}