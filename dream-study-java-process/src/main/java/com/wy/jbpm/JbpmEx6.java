package com.wy.jbpm;

import java.util.List;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Participation;
import org.jbpm.api.task.Task;

/**
 * 获得某个任务中角色的所有执行中的任务
 * 
 * id是任务继续执行下去的主键id,Executionid是当前实例的id,即整个从开始到结束的流程实例
 *
 * @author ParadiseWY
 * @date 2018-03-14 23:41:35
 * @git {@link https://github.com/mygodness100}
 */
public class JbpmEx6 {

	public static void getTaskList() {
		ProcessEngine pe = Configuration.getProcessEngine();
		// 获得个人可办理任务assign
		List<Task> tasks = pe.getTaskService().findPersonalTasks("任务中assign的值");
		for (Task task : tasks) {
			System.out.println(task.getId() + task.getExecutionId());
			// 单人完成任务.可直接完成
			pe.getTaskService().completeTask(task.getId());
		}

		// 根据任务id获得组候选人的值
		List<Participation> taskParticipations = pe.getTaskService().getTaskParticipations("taskid");
		for (Participation p : taskParticipations) {
			System.out.println(p);
		}

		// 获得多人可办理任务candidate-users
		List<Task> tasks2 = pe.getTaskService().findGroupTasks("任务组中某一个成员的标识符");
		for (Task task : tasks2) {
			// 多人完成任务需要先将这个任务变成单人任务
			// 当某一个人将多人任务变成单人任务时,其他成员将不能再take本任务,所以这里要枷锁
			pe.getTaskService().takeTask(task.getId(), "任务组中某一成员的标识符");
			pe.getTaskService().completeTask(task.getId());
			// 若此人拿到任务后又不想办理,那么可以再将此任务变成多任务,第2个参数固定写null
			// 若是想直接将任务分给某个成员时,null可改成某个成员的标识符
			pe.getTaskService().assignTask(task.getId(), null);
		}
	}
}