<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<title>Insert title here</title>
</head>
<body>
<%Record dcuser = (Record)session.getAttribute("dcuser"); %>
<table width="100%">
	<tr>
		<td height="100">
			
		</td>
	</tr>
	<tr>
		<td align="center" height="50">
		<%if(dcuser == null){ %>
			您没有登录或登录已超时，请登录！
		<%}else{
			%>
			对不起，您没有权限操作该功能！
			<%
		}%>
			
		</td>
	</tr>
	<tr>
		<td align="center">
		<%if(dcuser == null){ %>
			<input type="button" name="login" value="登录" onclick="javascript:window.location='<%=request.getContextPath() %>/index.jsp';">
		<%}else{
			%>
			<input type="button" name="back" value="返回" onclick="javascript:history.back();">
			<%
		}%>
			
		</td>
	</tr>
</table>
</body>
</html>