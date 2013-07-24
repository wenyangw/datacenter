<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
<%
String uploadFile = (String)request.getAttribute("filepath");
%>
function getPicPath(){
	window.returnValue='<%= uploadFile%>';
	window.close();
}
</script>
</head>
<body>

操作成功！
<br><input type="button" onclick="getPicPath()" value="关闭"></input><br>
</body>
</html>