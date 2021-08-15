package com.wy.jbpm;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;

/**
 * 组任务的赋值,和单个任务相对应
 *
 * @author ParadiseWY
 * @date 2018-03-22 23:35:45
 * @git {@link https://github.com/mygodness100}
 */
public class GroupAssignHandler implements AssignmentHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
		assignable.addCandidateGroup("userid");
	}
}