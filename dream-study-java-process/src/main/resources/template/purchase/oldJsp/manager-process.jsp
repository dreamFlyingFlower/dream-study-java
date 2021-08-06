<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="include.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>审批页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <%
    	JbpmContext jbpmContext = JbpmConfiguration.getInstance().createJbpmContext();
    	try{
    		long taskId = Long.parseLong(request.getParameter("taskId"));
    		TaskInstance ti = jbpmContext.getTaskInstance(taskId);
    		String isSubmit = request.getParameter("isSubmit");
    		if(isSubmit!=null&&isSubmit.equals("true")){
    			//判断当前是部门经理审批还是总经理审批，分别写入不同的流程变量
    			String result = request.getParameter("approve_result");
    			if(ti.getDescription().equals("payment.manager.approve")){
    				ti.getContextInstance().setVariable("manager_approve_result",result);
    				if(result.equals("1")){
    					//部门经理审批结果为同意
    					ti.end("agree");
    				}else{
    					ti.end("disagree");
    				}
    				out.println("<h1>部门经理审批完成!</h1>");
    			}else{
    				ti.getContextInstance().setVariable("super_manager_approve_result",result);
    				ti.end();
    				out.println("<h1>总经理审批完成!</h1>");
    			}
    		}else{
    			String title = ti.getVariable("title").toString();
    			String money_count = ti.getVariable("money_count").toString();
    			String remark = ti.getVariable("remark").toString();
    			String issueperson = ti.getVariable("issueperson").toString();
    			%>
    			报销人：<%=issueperson %><br/>
    			报销主题：<%=title %><br/>
    			报销金额：<%=money_count %><br/>
    			报销说明：<%=remark %><br/>
    			
    			<form action="manager-process.jsp" method="post">
    				<input type="radio" name="approve_result" value="1" checked="checked"/>同意&nbsp;&nbsp;
    				<input type="radio" name="approve_result" value="0"/>不同意<br/>
    				<input type="hidden" value="true" name="isSubmit"/>
    				<input type="hidden" value="<%=taskId %>" name="taskId"/>
    				<input type="submit" value="提交审批结果"/>
    			</form>
    			
    			<%
    		}
    	}finally{
    		jbpmContext.close();
    	}
     %>
  </body>
</html>
