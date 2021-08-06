<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ page import="test.dao.*" %>
<%@ include file="include.jsp" %>
<%
	String title = null;
	String remark = null;
	String isSubmit = request.getParameter("isSubmit");
	String purchaseId = request.getParameter("purchaseId");
	String taskId = request.getParameter("taskId");
	if(isSubmit!=null&&isSubmit.equals("true")){
		JbpmContext jbpmContext = JbpmConfiguration.getInstance().createJbpmContext();
		TaskInstance ti = jbpmContext.loadTaskInstance(Long.parseLong(taskId));
		String approve_result = request.getParameter("approve_result");
		ti.getContextInstance().setVariable("purchase_manager_approve_result",approve_result);
		ti.end();
		jbpmContext.close();
		out.println("<h1>审批处理成功</h1>");
		return;
	}else{
		BusinessDAO dao = new BusinessDAO();
		String sql = "select * from test_purchase where id=?";
		Map map = (Map)dao.query(sql,new Object[]{purchaseId}).get(0);
		title = map.get("title").toString();
		remark = map.get("remark").toString();
	}

 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>取消申请</title>
    
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
    <form action="cancel-apply.jsp" method="post"> 
    	采购标题：<input type="text" name="title" value="<%=title %>"/><br/>
    	采购内容：<input type="text" name="remark" value="<%=remark %>"/><br/>
    	<input type="hidden" name="isSubmit" value="true"/>
    	<input type="hidden" name="purchaseId" value="<%=purchaseId %>"/>
    	<input type="hidden" name="taskId" value="<%=taskId %>"/>
    	<input type="submit" value="取消采购申请"/>
    </form>
  </body>
</html>
