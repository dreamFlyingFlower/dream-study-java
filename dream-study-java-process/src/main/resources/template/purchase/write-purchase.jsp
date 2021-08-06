<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@page import="java.rmi.dgc.VMID"%>
<%@ include file="include.jsp" %>
<%@ page import="test.dao.*" %>
<%
	String title = "";
	String remark = "";
	String isSubmit = request.getParameter("isSubmit");
	String purchaseId = request.getParameter("purchaseId");
	String taskId = request.getParameter("taskId");
	BusinessDAO dao = new BusinessDAO();
	String issueperson = request.getSession().getAttribute("loginuser").toString();
	if(isSubmit!=null&&isSubmit.equals("true")){
		title = request.getParameter("title");
		remark = request.getParameter("remark");
		String sql = null;
		JbpmContext jbpmContext = dao.createJbpmContext();
		if(purchaseId!=null&&!purchaseId.equals("")){
			//修改采购流程
			sql = "update test_purchase set title=?,remark=? where id=?";
			dao.saveOrUpdateOrDelete(sql,new Object[]{purchaseId,title,remark});
			
			//结束修改任务
			TaskInstance ti = jbpmContext.getTaskInstance(Long.parseLong(taskId));
			ti.end();
			out.println("<h1>修改采购单操作成功</h1>");
		}else{
			//新增采购单
			sql = "insert into test_purchase values(?,?,?)";
			purchaseId = new VMID().toString();
			dao.saveOrUpdateOrDelete(sql,new Object[]{purchaseId,title,remark});
			
			//开始流程
			//设置当前的登录用户为issueperson
			jbpmContext.setActorId(issueperson);
			
			//获取名字为purchase的流程模版
			ProcessDefinition pd = jbpmContext.getGraphSession().findLatestProcessDefinition("purchase");
			ProcessInstance pi = pd.createProcessInstance();
			ContextInstance ci = pi.getContextInstance();
			
			//设置报销提交人为issueperson
			ci.setVariable("issueperson",issueperson);
			
			//创建开始节点的TaskInstance
			TaskInstance ti = pi.getTaskMgmtInstance().createStartTaskInstance();
			//向任务实例当中写入相关变量
			ti.setVariable("purchaseId",purchaseId);
			ti.end();
			
			out.println("<h1>新增采购单操作成功</h1>");
		}
		jbpmContext.close();
		return;
	}else{
		//对当前采购单的修改，取出当前采购单的数据
		if(purchaseId!=null&&!purchaseId.equals("")){
			String sql = "select * from test_purchase where id=?";
			Map map = (Map)dao.query(sql,new Object[]{purchaseId}).get(0);
			title = map.get("title").toString();
			remark = map.get("remark").toString();
		}else{
			purchaseId = "";
		}
	}
 %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>采购申请页面</title>
    
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
    <form action="write-purchase.jsp" method="post"> 
    	采购标题：<input type="text" name="title" value="<%=title %>"/><br/>
    	采购内容：<input type="text" name="remark" value="<%=remark %>"/><br/>
    	<input type="hidden" name="isSubmit" value="true"/>
    	<input type="hidden" name="taskId" value="<%=taskId %>"/>
    	<input type="hidden" name="purchaseId" value="<%=purchaseId %>"/>
    	<input type="submit" value="提交采购单"/>
    </form>
  </body>
</html>
