<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.index.*,tms.datacenter.dbmanage.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script language="javascript">
	function logout(){
		if(window.confirm("您确定要退出系统吗？")){
			top.window.location='<%=request.getContextPath() %>/sysmanage/logoutAction';
		}
	}
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
<table width="1024">
				<tr>
					<td colspan="2" align="left" height="60" class="indextd">
						<div style="line-height:60px;height:60px;width:200px;float:left"><img src="<%=request.getContextPath() %>/images/s.gif"></div>
						<div style="line-height:60px;height:60px;width:400px;float:left"><font size="6" color="#636363">北方联合出版传媒</font></div>
						<div style="text-align:right;line-height:20px;height:60px;width:385px;float:right">
							<%
								String str_user = "";
								Record user = (Record)session.getAttribute("dcuser");
								if(user != null){
									str_user+="<font color=\"red\"><b>"+user.get("username")+"</b></font>,您好！ ";
									str_user+="<span style=\"cursor:pointer\" onclick=\"javascript:window.location='"+request.getContextPath()+"/sysframe/mainframe.jsp';\"><font color=blue>管理</font></span>";
									str_user+="&nbsp;&nbsp;&nbsp;&nbsp;<span style=\"cursor:pointer\" onclick=\"logout();\"><font color=blue>退出</font></span>";
								}else{
									//str_user="游客您好，欢迎光临！<span style=\"cursor:pointer\" onclick=\"javascript:window.location='"+request.getContextPath()+"/login.jsp';\"><font color=blue>登录</font></span>";
									%>
									<form name="loginform" action="<%=request.getContextPath() %>/sysmanage/loginAction" method="POST">
									<table>
										<tr>
											<td height="40" valign="top" align="left">用户名<input type="text" name="loginName" id="loginNameid" value="" style="width:100px;">
											密码<input type="password" name="loginPsw" id="loginPswid" value="" style="width:100px;">
											<input type="submit" value="登陆" onclick="return checkdata();">&nbsp;
											<input type="reset" value="重置">
											<font color="red"><%=request.getAttribute("errorMsg")==null?"":(String)request.getAttribute("errorMsg") %></font>
											</td>
										</tr>
									</table>
									
									</form>
									<%
								}
							%>
							<%=str_user %>
						</div>
					</td>
				</tr>
				</table>
