<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function selectFile(){
var obj = new Object();
obj.name="请选择文件";
var res = window.showModalDialog("<%=request.getContextPath() %>/index/upload.jsp",obj,"dialogWidth:500px;dialogHeight:500px;resizable:yes");
var picpath = document.getElementById("picpathid");
if(picpath&&picpath!=''){
	picpath.value = res;
	document.getElementById("showpic").src='<%=request.getContextPath()%>'+res;
}

}
</script>
</head>
<body>
<%
String picid = "";
String title = "";
String link = "";
String showorder = "";
String picpath = "";
String memo = "";
Record picad = (Record)request.getAttribute("record");
if(picad != null)
{
	picid = picad.get("picid");
	title = picad.get("title");
	link = picad.get("link")==null?"":picad.get("link");
	showorder = picad.get("showorder")==null?"":picad.get("showorder");
	picpath = picad.get("picpath")==null?"":picad.get("picpath");
	memo = picad.get("memo");
}
%>
<form name="form1" action="<%=request.getContextPath() %>/sysmanage/picADManageAction"  enctype="multipart/form-data" method="post">
<%if(picid != null && picid.trim().length() > 0) {%>
				<input type="hidden" name="methodName" id="methodNameId" value="update">
			<%}else{
				%>
				<input type="hidden" name="methodName" id="methodNameId" value="add">
				<%
  }%>
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="m01m06">
<table class="listtable" width="100%">
	
			
			<%if(picid != null && picid.trim().length() > 0) {%>
			<tr>
				<td class="tdhead" align="right" width="20%">ID</td>
				<td class="data1" align="left">
					<input type="text" class="input300red" readonly="readonly" name="picid" value="<%=picid==null?"":picid %>">
					<%=picad.getDataDesc("picid") %>
				</td>
			</tr><%} %>
		
	<tr>
		<td class="tdhead" align="right">标题</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="title" value="<%=title==null?"":title %>">
			<%=picad.getDataDesc("title") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">图片</td>
		<td class="data1" align="left">
			<input type="text" class="input300red" id="picpathid" readonly="readonly" name="picpath" value="<%=picpath==null?"":picpath %>"/>
			<input type="button" value="选择文件" onclick="selectFile()"/>
			<%if(picpath != null && picpath.trim().length() > 0) {
				%>
				<br><img alt="没有找到图片" id="showpic" src="<%=request.getContextPath()+picpath %>" width="210" height="90">
				<%
			}%>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">链接地址</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="link" value="<%=link==null?"":link %>">
			<%=picad.getDataDesc("link") %>
		</td>
	</tr>
	
	<tr>
		<td class="tdhead" align="right">显示顺序</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="showorder" value="<%=showorder==null?"":showorder %>">
			<%=picad.getDataDesc("showorder") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">备注</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="memo" value="<%=memo==null?"":memo %>">
			<%=picad.getDataDesc("memo") %>
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