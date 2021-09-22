//package com.wy.jbpm;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.jbpm.api.Configuration;
//import org.jbpm.api.ProcessEngine;
//
//import com.wy.model.Employee;
//
///**
// * 流程变量
// *
// * @author 飞花梦影
// * @date 2018-03-14 23:37:58
// * @git {@link https://github.com/mygodness100}
// */
//public class JbpmEx4 {
//
//	public static void main(String[] args) {
//
//	}
//
//	// 流程只要没有结束都可以设置流程变量,但是只能在进行到流程变量之前赋值
//	// 通过set方式设置的流程变量会随着流程的结束而被删除,若需要保存流程变量,需要使用create
//	public static void start() {
//		Map<String, String> var = new HashMap<>();
//		Employee employee = new Employee();
//		ProcessEngine pe = Configuration.getProcessEngine();
//
//		pe.getTaskService().setVariables("executeid", var);
//		// 传入executeid
//		pe.getExecutionService().setVariables("executeid", var);
//		pe.getExecutionService().setVariable("executeid", "key", "value");
//		// 流程实例对象,需要实现serializable接口
//		pe.getExecutionService().setVariable("executeid", "user", employee);
//		// 获得流程变量
//		pe.getExecutionService().getVariable("executeid", "key");
//
//		pe.getTaskService().setVariables("taskid", var);
//
//		pe.getExecutionService().createVariables("executeid", var, true);
//
//		// 若要在完成任务的时候设置流量变量,会出异常,不能直接调用这个方法,必须分2步
//		pe.getTaskService().completeTask("taskid", var);// error
//		pe.getTaskService().setVariables("taskid", var);// right
//		pe.getTaskService().completeTask("taskid");// right
//
//		// 只能在流程进行到当前节点的时候进行赋值,刚好和流程变量相辅相成
//		pe.getTaskService().assignTask("taskid", "让流程继续进行的值");
//	}
//}