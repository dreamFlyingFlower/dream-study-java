<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h5>Huanying,Freemarker的模板</h5>
	<h2>${test}<h2>
	<h2>${user.name}</h2>
	<!-- assign:定义变量,前面的#不能掉 -->
	<#assign test1="是谁">
	<#assign test2={"key1":"value1","key2":"value2"}>
	<!-- 使用变量 -->
	${test1}
	${test2.key1}
	<!-- include:模板文件的嵌套 -->
	<#include "header.ftl">
	<!-- if:条件中相等可以直接用单个=,也可以双等,变量引用可以不加${} -->
	<#if code=1>
	<#else>
	</#if>
	<!-- 集合:默认取元素下标可以直接在变量后加_index -->
	<#list items as item>
		<span>${item}.${item_index}</span>
	</#list>
	<!-- 双问号判断变量是否存在 -->
	<#if test3??>
	<#else>
	</#if>
	<!-- 当变量test4不存在时,输出fdsss -->
	${test4!'fdsss'}
</body>
</html>