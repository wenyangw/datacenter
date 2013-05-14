<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*"%>
<%@page import="tms.datacenter.upload.UploadMsg"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<base target="_self">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
</head>
<body>

<form name="form1" enctype="multipart/form-data" action="<%=request.getContextPath() %>/sysmanage/uploadPicAction" target="" method="post" >
<input type="hidden" name="savePath" value="/uploadpic">
<table class="listtable" width="100%">
	<tr>
		<td class="tdhead" align="right">选择上传文件</td>
		<td class="data2" align="left">
			<input type="file" class="input300" name="image" >
		</td>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2">
			<input type="submit" value="确定">
			<input type="button" value="返回" onclick="javascript:window.close();">
		</td>
	</tr>
</table>
</form>
</body>
</html>