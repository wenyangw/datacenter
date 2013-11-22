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
<frameset rows="55,*" framespacing="0">
<frame name="logo" frameborder=0 style="border-left:0px solid #7B68EE;border-right:0px solid #7B68EE" scrolling="no" src="<%=request.getContextPath() %>/sysframe/logo.jsp">
<frameset  cols="172,4,*" framespacing="0">
	<frame name="menu" frameborder=0 style="border-left:1px solid #9CB8CC;border-right:1px solid #9CB8CC" src="<%=request.getContextPath() %>/sysmanage/menuAction">
	<frame name="blank" frameborder=0 src="<%=request.getContextPath() %>/sysframe/blank.jsp">
	<frame name="main" frameborder=0 src="<%=request.getContextPath() %>/index/loginmain.jsp">
</frameset>
</frameset>
</html>