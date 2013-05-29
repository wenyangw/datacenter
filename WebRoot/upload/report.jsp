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
<center><h2>使用柱图和饼图列取所有水果产量</h2></center>  
<%--     柱图:<img src="<%=request.getAttribute("chart") %>"><br/>   --%>
柱图:<img src="upload/chartAction.action"><br/>
<table width="100%" class="listtable">
	
</table>
</div>
<%=request.getAttribute("pager")==null?"":(String)request.getAttribute("pager") %>
<hr>
<table class="bottomtable" width="100%">
	<tr>
		<td align="right">
			<%
				ArrayList uo = (ArrayList)request.getAttribute("uo");
				out.println(RoleManage.paraUserOperationToButton(uo,Operation.SHOW_POS_LIST));
			%>
		</td>
	</tr>
</table>
</form>
  </body>
</html>
