package com.wy.jbpm;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;

/**
 * JBPM流程管理,需要使用JBPM4.4
 * 
 * 当流程运行到decision的时候,可自动执行实现了decisionhandler接口的方法
 *
 * @author ParadiseWY
 * @date 2018-03-15 23:34:52
 * @git {@link https://github.com/mygodness100}
 */
public class DayDecision implements DecisionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public String decide(OpenExecution arg0) {
		int day = 5;
		if (day > 3) {
			return "请假大于3天";
		}
		return "请假小于3天";
	}
}