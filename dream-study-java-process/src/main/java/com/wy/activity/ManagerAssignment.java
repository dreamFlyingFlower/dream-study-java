package com.wy.activity;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;

import com.test.Constants;

/**
 * 给任务实例分配参与者的Handler类
 * 
 * @author 飞花梦影
 * @date 2021-08-04 17:06:02
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ManagerAssignment implements AssignmentHandler,Constants{

	@Override
	public void assign(Assignable assignable, ExecutionContext executionContext) throws Exception {
		//从当前流程实例上下文中取出当前报销人
		String issueperson = executionContext.getContextInstance().getVariable(this.ISSUE_PERSON).toString();
		
		if(issueperson.equals("user1")){
			//当报销人员为user1时,对应的经理为manager1
			assignable.setActorId("manager1");
		}else{
			assignable.setActorId("manager2");
		}
	}
}