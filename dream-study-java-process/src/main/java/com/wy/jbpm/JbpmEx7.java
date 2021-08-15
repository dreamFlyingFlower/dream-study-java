package com.wy.jbpm;

import java.util.List;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Task;

/**
 * jbpm中各种命令的用法
 *
 * @author ParadiseWY
 * @date 2018-03-14 23:41:13
 * @git {@link https://github.com/mygodness100}
 */
public class JbpmEx7 {

	public static void getTaskList() {
		ProcessEngine pe = Configuration.getProcessEngine();
		// 最常用的task,不介绍
		// state,通常是达到某过状态的时候,直接跳过这个state节点,常用方法就一个
		List<Task> list = pe.getTaskService().createTaskQuery().list();
		for (Task t : list) {
			System.out.println(t.getExecutionId());
		}
		pe.getExecutionService().signalExecutionById("executionid", "走那条路线");

		// decision,判断节点,多条线路只走一条
		// 实现DecisionHandler接口,判断的结果就是走那条线的名字,即return 线路的名字(name属性)

		// fork,判断节点,多条线路,每条都走
		// join,合并节点,跟fork相对,多条线路都必须同时到达join节点才能合并节点,继续走下去
		// custom,自定义节点,需要实现ExternalActivityBehaviour接口
		// on元素,在图形化定义界面没有这个标签,只能在xml文件中写,可以直接写在跟元素上,也可以写在其他元素之内

		// 在每一个task任务节点都有一个Assignment属性,当这个属性选择assign时表示这个任务只能单个人办理,
		// 当这个属性选择candidate-users时表示可多人办理这个任务,多人可用逗号隔开,也可以用#{表达式}
	}
}