<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="include.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <title>待办任务</title>
    
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
    <table width="100%" border="1" cellpadding="0" cellspacing="0">
    	<tr bgcolor="#FDFDD0">
    		<td>任务名称</td>
    		<td>创建时间</td>
    		<td>操作</td>
    	</tr>
    	<%
    		JbpmContext jbpmContext = JbpmConfiguration.getInstance().createJbpmContext();
    		try{
    			String currentperson = request.getSession().getAttribute("loginuser").toString();
    			//当前人员的名称
    			List taskList = jbpmContext.getTaskList(currentperson);
    			//根据当前的人员名称得到任务列表
    			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			for(Iterator iter = taskList.iterator();iter.hasNext();){
    				out.println("<tr>");
    				TaskInstance ti = (TaskInstance)iter.next();
    				out.println("<td>"+ ti.getName() +"</td>");
    				out.println("<td>"+ sd.format(ti.getCreate()) +"</td>");
    				String description = ti.getDescription();
    				String url = "cashier-process.jsp";//财务处理页面
    				if(description.equals("payment.manager.approve")||description.equals("payment.super.manager.approve")){
    					//如果是部门经理或总经理审批，则用另一个页面
    					url = "manager-process.jsp";
    				}
    				url += "?taskId=" + ti.getId();
    				out.println("<td><a href=\""+ url +"\">处理该任务</a></td>");
    				out.println("</tr>");
    			}
    		}finally{
    			jbpmContext.close();
    		}
    	 %>
    </table>
  </body>
</html>
