<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
String rolecode = "";
String roleName = "";
String memo = "";
Record role = (Record)request.getAttribute("record");
if(role != null)
{
	rolecode = role.get("rolecode");
	roleName = role.get("rolename");
	memo = role.get("memo");
}
%>
<form name="form1" action="<%=request.getContextPath() %>/sysmanage/roleManageAction" method="post">
<%if(rolecode != null && rolecode.trim().length() > 0) {%>
				<input type="hidden" name="methodName" id="methodNameId" value="update">
			<%}else{
				%>
				<input type="hidden" name="methodName" id="methodNameId" value="add">
				<%
  }%>
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="m01m01">
<table class="listtable" width="100%">
	<tr>
		<td class="tdhead" align="right" width="20%">角色编码</td>
		<td class="data1" align="left">
			<%if(rolecode != null && rolecode.trim().length() > 0) {%>
				<input type="text" class="input300red" readonly="readonly" name="rolecode" value="<%=rolecode==null?"":rolecode %>">
			<%}else{
				%>
				<input type="text" class="input300" name="rolecode" value="<%=rolecode==null?"":rolecode %>">
				<%
			}%>
			<%=role.getDataDesc("rolecode") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">角色名称</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="rolename" value="<%=roleName==null?"":roleName %>">
			<%=role.getDataDesc("rolename") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">备注</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="memo" value="<%=memo==null?"":memo %>">
			<%=role.getDataDesc("memo") %>
		</td>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2">
			<input type="submit" value="确定">
			<input type="button" value="返回" onclick="javascript:history.back()">
		</td>
	</tr>
</table>
</form>
</body>
</html>