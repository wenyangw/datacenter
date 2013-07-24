<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<title>系统登陆</title>
<script src="<%=request.getContextPath() %>/js/Calendar1.js" type="text/javascript"></script>
<script language="javascript">
function checkdata(){
	var username=document.getElementById("loginnameid").value;
	var userpass=document.getElementById("loginpswid").value;
	if(username==''){
		alert("请输入用户名！");
		return false;
	}
	if(userpass==''){
		alert("请输入密码！");
		return false;
	}
	return true;
}
</script>
</head>
<body class="loginbody">
<img style="height:40px;margin-top:10px;" src="<%=request.getContextPath() %>/images/s.gif">
<font size="5" color="#FFFFFF">北方联合出版传媒数据共享平台</font>
<table width="100%">
	<tr>
		<td height="70"></td>
	</tr>
	<tr>
<td align="center">
<form name="loginform" action="<%=request.getContextPath() %>/sysmanage/loginAction" method="POST">
<table class="logintable">
<tr>
	<td colspan="2" height="30" valign="bottom" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<img style="height:40px;margin-top:0px;" src="<%=request.getContextPath() %>/images/userlogin.png"><font color="green">用户登录</font></td>
</tr>
<tr>
	<td height="20" width="30%" align="right">用户名</td>
	<td align="left"><input type="text" name="loginName" id="loginNameid" value="" style="width:200px;"></td>
</tr>
<tr>
	<td height="20"  width="30%" align="right">密&nbsp;&nbsp;&nbsp;&nbsp;码</td>
	<td align="left"><input type="password" name="loginPsw" id="loginPswid" value="" style="width:200px;"></td>
</tr>
<tr>
	<td height="20" colspan="2" align="center">
	<input type="submit" value="登陆" onclick="return checkdata();">&nbsp;
	<input type="reset" value="重置">
	</td>
</tr>
<tr>
	<td colspan="2" height="20" align="center" valign="top">
		<font color="white"><%=request.getAttribute("errorMsg")==null?"":(String)request.getAttribute("errorMsg") %></font>
	</td>
</tr>
</table>
</form>
</td>
	</tr>
	<tr>
		<td></td>
	</tr>
	</table>
</body>
</html>