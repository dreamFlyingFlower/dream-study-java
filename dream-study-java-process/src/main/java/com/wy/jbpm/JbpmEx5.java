//package com.wy.jbpm;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.jbpm.api.Configuration;
//import org.jbpm.api.ProcessEngine;
//
///**
// * 若是下一步需要传参数,则需要在当前对下一步的值赋值,否则报错
// *
// * @author ParadiseWY
// * @date 2018-03-14 23:42:12
// * @git {@link https://github.com/mygodness100}
// */
//public class JbpmEx5 {
//
//	public static void giveParam() {
//		ProcessEngine pe = Configuration.getProcessEngine();
//		// 流程启动的时候要给下一步的部门经理赋值
//		Map<String, String> var = new HashMap<>();
//		var.put("departUser", "部门经理角色的人");
//		pe.getExecutionService().startProcessInstanceByKey("example", var);
//
//		// 流程中需要对总经理审批进行赋值
//		Map<String, String> var1 = new HashMap<>();
//		var1.put("zongUser", "总经理角色的人");
//		pe.getTaskService().setVariables("pdid", var1);
//		pe.getTaskService().completeTask("pdid");
//	}
//}