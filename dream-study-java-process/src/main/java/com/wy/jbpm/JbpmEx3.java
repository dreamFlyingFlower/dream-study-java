//package com.wy.jbpm;
//
//import java.util.List;
//
//import org.jbpm.api.Configuration;
//import org.jbpm.api.ProcessEngine;
//import org.jbpm.api.task.Task;
//
///**
// * 启动流程实例,查询流程实例
// *
// * @author ParadiseWY
// * @date 2018-03-14 23:42:31
// * @git {@link https://github.com/mygodness100}
// */
//public class JbpmEx3 {
//
//	public static void main(String[] args) {}
//
//	// 执行流程实例
//	public static void start() {
//		ProcessEngine pe = Configuration.getProcessEngine();
//		// 获得执行服务,传入部署id
//		pe.getExecutionService().startProcessInstanceById("");
//		// 获得执行服务,传入文件key,若部署了多次相同的流程,用key启动会用最高版本的
//		pe.getExecutionService().startProcessInstanceByKey("example");
//		// 查询所有正在执行的任务,assignee传一个角色的值,可查特定人员正在执行的任务
//		List<Task> list = pe.getTaskService().createTaskQuery().assignee("user").list();
//		for (Task task : list) {
//			// 获得user的任务列表后,拿到执行id
//			System.out.println(task.getId());
//		}
//		// 完成任务,传入任务的id,即上一步task.getId()得到的值.完成后在相应表中会删除任务,在历史表是的state会变成completed
//		pe.getTaskService().completeTask("id");
//		// 查询所有完成了的任务
//		pe.getHistoryService().createHistoryTaskQuery().state("completed").list();
//		// 直接结束流程实例
//		pe.getExecutionService().endProcessInstance("pid", "ended or failed");
//		// 跳过某一个流程实例的阶段,直接进入到下一步
//		pe.getExecutionService().signalExecutionById("executionid", "若有多过分支,要确定走那个分支,也就是transition的name属性");
//	}
//}