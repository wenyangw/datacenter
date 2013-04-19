<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<STYLE>

</STYLE>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<title>欢迎使用数据共享系统</title>
</head>
<frameset rows="75,*" >
<frame name="logo" frameborder=0 scrolling="no" src="<%=request.getContextPath() %>/sysframe/logo.jsp">
<frameset  cols="170,*">
	<frame name="menu" frameborder=0 src="<%=request.getContextPath() %>/sysmanage/menuAction">
	<frame name="main" frameborder=0 src="<%=request.getContextPath() %>/sysframe/main.jsp">
</frameset>
</frameset>
</html>