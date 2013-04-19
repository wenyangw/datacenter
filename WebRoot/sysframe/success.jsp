<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
	String promptMsg=(String)request.getAttribute("prompt");
	String returnAction = (String)request.getAttribute("returnAction");
	Hashtable params = (Hashtable)request.getAttribute("params");
%>
<form name="successform" action="<%=returnAction==null?"":returnAction %>">
	<%if(params != null && params.size() > 0){
		Set set = params.keySet();
		Iterator it = set.iterator();
		String key = "";
		String value = "";
		while(it.hasNext()){
			key = (String)it.next();
			value = (String)params.get(key);
			%>
			<input type="hidden" name="<%=key %>" value="<%=value %>">
			<%
		}
	}%>
	<table width="100%">
	<tr>
		<td height="100">
			
		</td>
	</tr>
	<tr>
		<td align="center" height="50">
			<%=promptMsg==null?"操作成功":promptMsg %>
		</td>
	</tr>
	<tr>
		<td align="center">
			<input type="submit" name="back" value="确定">
		</td>
	</tr>
</table>
</form>
</body>
</html>