<%@page import="tms.datacenter.upload.UploadMsg"%>
<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.sysmanage.*"%>


<%
String path = (String)request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'upload.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/common.js"> </script>

  </head>
  
  <body>
<form name="listform" id="listformid" action="<%=request.getContextPath() %>/upload/reportAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="list">
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="<%=request.getAttribute("moduleid")==null?"":(String)request.getAttribute("moduleid") %>">
<div class="listdiv">
<center><h2>2013年图书销售统计</h2></center>
<table width="100%" class="listtable">
	<tr>
		<td class="tdhead" align="center" height="25"></td>
		<td class="tdhead" align="center">1月</td>
		<td class="tdhead" align="center">2月</td>
		<td class="tdhead" align="center">3月</td>
		<td class="tdhead" align="center">4月</td>
		<td class="tdhead" align="center">5月</td>
	</tr>
	<tr>
		<td height="20" align="center">社科</td>
		<td height="20" align="center">100</td>
		<td height="20" align="center">200</td>
		<td height="20" align="center">300</td>
		<td height="20" align="center">400</td>
		<td height="20" align="center">500</td>
	</td>
	<tr>
		<td height="20" align="center">音像</td>
		<td height="20" align="center">100</td>
		<td height="20" align="center">200</td>
		<td height="20" align="center">300</td>
		<td height="20" align="center">400</td>
		<td height="20" align="center">500</td>
	</td>
	<tr>
		<td height="20" align="center">文艺</td>
		<td height="20" align="center">100</td>
		<td height="20" align="center">200</td>
		<td height="20" align="center">300</td>
		<td height="20" align="center">400</td>
		<td height="20" align="center">500</td>
	</td>
</table>
</div>
<div style="width:100%">
		<div class="pagerdiv">&nbsp;
			<%=request.getAttribute("pager")==null?"":(String)request.getAttribute("pager") %>
		</div>
		<div class="buttondiv">
			<%
				ArrayList uo = (ArrayList)request.getAttribute("uo");
				out.println(RoleManage.paraUserOperationToButton(uo,Operation.SHOW_POS_LIST));
			%>
		</div>
</div>
</form>
  </body>
</html>
