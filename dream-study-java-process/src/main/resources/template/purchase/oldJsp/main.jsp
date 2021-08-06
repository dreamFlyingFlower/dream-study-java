<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>测试工作流系统</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
		body{
			margin-left:0px;
			margin-top:0px;
			margin-right:0px;
			margin-bottom:0px;
		}
	</style>
  </head>
  
  <body>
    <table width="100%" height="700" border="0" cellpadding="0" cellspacing="0">
    	<tr>
    		<td width="15%" height="100%" valign="top">
    			<a href="task-list.jsp" target="mainFrame">我的待办任务</a>
    			<a href="write.jsp" target="mainFrame">我要报销</a>
    		</td>
    		<td width="80%" valign="top">
    			<iframe src="task-list.jsp" name="mainFrame" frameborder="0" marginheight="0" marginwidth="0" height="700" width="100%">
    			</iframe>
    		</td>
    	</tr>
    </table>
  </body>
</html>
