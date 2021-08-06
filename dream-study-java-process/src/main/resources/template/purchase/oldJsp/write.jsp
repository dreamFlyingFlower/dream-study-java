<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>填写报销单</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <%@include file="include.jsp" %>
  
  <%
  	String isSubmit = request.getParameter("isSubmit");
  	if(isSubmit!=null&&isSubmit.equals("true")){
  		//处理提交的报销单
  		String title = request.getParameter("title");
  		String money_count = request.getParameter("money_count");
  		String remark = request.getParameter("remark");
  		String issueperson = request.getSession().getAttribute("loginuser").toString();
  		
  		JbpmContext jbpmContext = JbpmConfiguration.getInstance().createJbpmContext();
  		try{
  			//设置当前登录用户为issueperson
  			jbpmContext.setActorId(issueperson);
  			
  			//获取名为payment流程模版
  			ProcessDefinition pd = jbpmContext.getGraphSession().findLatestProcessDefinition("payment");
  			ProcessInstance pi = pd.createProcessInstance();
  			ContextInstance ci = pi.getContextInstance();
  			
  			//设置报销提交人为issueperson
  			ci.setVariable("issueperson",issueperson);
  			
  			//创建开始节点的任务实例TaskInstance
  			TaskInstance ti = pi.getTaskMgmtInstance().createStartTaskInstance();
  			//向任务实例当中写入相关变量
  			ti.setVariable("title",title);
  			ti.setVariable("money_count",money_count);
  			ti.setVariable("remark",remark);
  			
  			//结束任务实例，流程的Token就进入部门经理审批节点
  			ti.end();
  		}finally{
  			jbpmContext.close();
  		}
  		out.print("<h1>报销申请提交成功</h1>");
  		return;
  	}
   %>
  
  
  <body>
    <form action="write.jsp" method="post">
    	报销主题：<input type="text" name="title"/><br/>
    	报销金额：<input type="text" name="money_count"/><br/>
    	报销说明：<input type="text" name="remark"/><br/>
    	<input type="hidden" name="isSubmit" value="true"/>
    	<input type="submit" value="提交报销"/>
    </form>
  </body>
</html>
