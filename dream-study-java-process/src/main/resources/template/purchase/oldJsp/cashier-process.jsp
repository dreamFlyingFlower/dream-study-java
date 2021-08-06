<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="include.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>财务处理</title>
    
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
    			//结束当前的财务处理任务
    			ti.end();
    			out.println("<h1>财务处理完成!</h1>");
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
    			<form action="cashier-process.jsp" method="post">
    				<input type="hidden" value="true" name="isSubmit"/>
    				<input type="hidden" value="<%=taskId %>" name="taskId"/>
    				<input type="submit" value="财务处理完成"/>
    			</form>
    			<%
    		}
    	}finally{
    		jbpmContext.close();
    	}
     %>
  </body>
</html>
