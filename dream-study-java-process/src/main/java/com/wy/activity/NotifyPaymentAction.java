package com.wy.activity;

import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.test.Constants;

/**
 * 打印报销结果Action类
 * 
 * @author 飞花梦影
 * @date 2021-08-04 17:06:55
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NotifyPaymentAction implements ActionHandler, Constants {

	@Override
	public void execute(ExecutionContext executionContext) throws Exception {
		// 获取流程上下文对象
		ContextInstance ct = executionContext.getContextInstance();

		String issueperson = ct.getVariable(this.ISSUE_PERSON).toString();
		String managerApproveResult = ct.getVariable(this.MANAGER_APPROVE_RESULT).toString();
		// 部门经理审批的结果
		boolean result = false;
		if (managerApproveResult.equals(this.APPROVE_RESULT_OK)) {
			// 部门经理是否审批通过
			String moneyCount = ct.getVariable(this.MONEY_COUNT).toString();
			// 报销金额
			if (Integer.parseInt(moneyCount) > 1000) {
				// 报销金额是否大于1000
				String superManagerApproveResult = ct.getVariable(this.SUPER_MANAGER_APPROVE_RESULT).toString();
				// 总经理审批结果
				if (superManagerApproveResult.equals(this.APPROVE_RESULT_OK)) {
					// 总经理是否审批通过
					result = true;
				}
			} else {
				result = false;
			}
		}
		if (result) {
			System.out.println("您好： " + issueperson + " 你的报销审批已经通过");
		} else {
			System.out.println("您好： " + issueperson + " 你的报销审批未通过");
		}
		executionContext.getToken().signal();
	}
}