<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*"%>
<%@page import="tms.datacenter.upload.UploadMsg"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript" src="<%=request.getContextPath() %>/js/util.js">

</script>
</head>
<body>
<%
String pkfield = "";
String uploadName = "";
//String lastUpdate = "";
//String nextUpload = "";
//ProjectManage pm = new ProjectManage();
UploadMsg project = (UploadMsg)request.getAttribute("uploadMsg");
if(project != null)
{
	pkfield = project.getSpecialparam();
	uploadName = project.getCnname();
}
%>
<form name="form1" enctype="multipart/form-data" action="<%=request.getContextPath() %>/upload/uploadAction" method="post" >
<input type="hidden" name="methodName" id="methodNameId" value="">
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="<%=request.getAttribute("moduleid")==null?"":(String)request.getAttribute("moduleid") %>">
<input type="hidden" name="pkfield" id="pkfield" value="<%=pkfield %>">
<table class="listtable" width="100%">
	<tr>
		<td class="tdhead" align="right" width="20%">上传项目名称</td>
		<td class="data1" align="left">
			
			<input type="text" class="input300red" readonly="readonly" name="uploadName" value="<%=uploadName %>">
		</td>
	</tr>
	
	<tr>
		<td class="tdhead" align="right">选择上传文件</td>
		<td class="data2" align="left">
			<input type="file" class="input300" name="upload" >
		</td>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2">
			<input type="submit" value="确定" onclick="showloading('数据正在上传，请稍等。。。')">
			<input type="button" value="返回" onclick="javascript:history.back()">
		</td>
	</tr>
</table>
</form>
</body>
</html>