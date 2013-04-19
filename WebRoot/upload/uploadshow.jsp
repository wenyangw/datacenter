<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.sysmanage.*"%>

<%@page import="tms.datacenter.upload.UploadMsg"%>
<%@page import="tms.datacenter.upload.UploadLogManage"%>
<%@page import="tms.datacenter.upload.RuleManage"%>
<%@page import="tms.datacenter.upload.ColumnMsg"%>
<%@page import="java.io.File"%>

<%
String path = request.getContextPath();
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
  <%
  List lists = (List)request.getAttribute("table");
  List records = (List)request.getAttribute("records");
  
  String pktable = (String)request.getAttribute("pktable");
  request.setAttribute("contents", records);
  %>
  
<form name="listform" id="listformid" action="<%=request.getContextPath() %>/upload/uploadManageAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="upload">
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="<%=request.getAttribute("moduleid")==null?"":(String)request.getAttribute("moduleid") %>">
<input type="hidden" name="pktable" value="<%=pktable %>">
<input type="hidden" name="records" value="<%=records %>">
<div class="listdiv">
<table width="100%" class="listtable">
	<tr>
		<td colspan="<%=lists.size() %>">上传项目字段列表</td>
	</tr>
	<tr>
	<%
	for(Object o : lists){
		String str = ((ColumnMsg)o).getFieldname();
	%>
		<td class="tdhead" align="center"><%=str %></td>
	<%
	}
	%>	
	</tr>
	<tr><td colspan="<%=lists.size() %>">&nbsp;</td></tr>
	<tr>
		<td colspan="<%=lists.size() %>">上传文件内容</td>
	</tr>
	<%
		//Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
		String datatdclass = "";
		
		if(records != null && records.size() > 0){
			List r = new ArrayList();
			for(int i = 0; i < records.size(); i++){
				r = (List)records.get(i);
				%>
				<tr>
				<%
				if(i%2 == 0){
					datatdclass = "data2";
				}else{
					datatdclass = "data1";
				}
				for(Object o : r){
					String strCell = String.valueOf(o);
					if(i == 0){
						%>
						<td class="tdhead" align="center"><%=strCell %></td>
						<%
					}else{
						%>
						<td class="<%=datatdclass %>" align="center"><%=strCell %></td>
						<%
					}
				}
				
				
				%>
				</tr>
				<%
			}
		}
	%>		
	<tr>
		<td class="data1" align="center" colspan="<%=lists.size() %></td>">
			<input type="submit" value="确定">
			<input type="button" value="返回" onclick="javascript:history.back()">
		</td>
	</tr>
</table>
</div>
<%=request.getAttribute("pager")==null?"":(String)request.getAttribute("pager") %>
<hr>

</form>
  </body>
</html>
