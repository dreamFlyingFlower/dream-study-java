<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>用户登录</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <%
  	//处理用户登录信息
  	String loginflag = request.getParameter("loginflag");
  	if(loginflag!=null&&loginflag.equals("ok")){
  		String username = request.getParameter("username");
  		String userpwd = request.getParameter("userpwd");
  		if(username.equals("user1")||username.equals("user2")||username.equals("manager1")||username.equals("manager2")||username.equals("supermanager")||username.equals("cashier")){
  			request.getSession().setAttribute("loginuser",username);
  			//登录成功转到main.jsp页面
  			response.sendRedirect("main.jsp");
  		}
  	}
   %>
  <body>
    <form action="login.jsp" method="get">
    	用户名：<input type="text" name="username"/><br/>
    	密码：<input type="password" name="userpwd"/><br/>
    	<input type="hidden" name="loginflag" value="ok"/>
    	<input type="submit" value="登录"/>
    </form>
  </body>
</html>
