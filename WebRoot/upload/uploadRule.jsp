<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>

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
<div class="listdiv">
<table width="100%" class="listtable">
	<tr>
<%--		<td class="tdhead" align="center" height="25">全选<input type="checkbox" name="selectAll" value="1" onclick="checkAll(this,'pkfield')"></td>--%>
		<td class="tdhead" align="center">上传项目名称</td>
		<td class="tdhead" align="center">上传频率</td>
		<td class="tdhead" align="center">开始时间</td>
		<td class="tdhead" align="center">下次上传时间</td>
		<td class="tdhead" align="center">操作</td>
	</tr>
	<%
		//Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
		ArrayList records = (ArrayList)request.getAttribute("upList");
		if(records != null && records.size() > 0){
			Record r = null;
			String ruleId = "";
			String proName = "";
			String ruleType = "";
			String startTime = "";
			String datatdclass = "";
			for(int i = 0; i < records.size(); i++){
				r = (Record)records.get(i);
				ruleId = r.get("RuleId");
				proName = r.get("ProId");
				ruleType = r.get("RuleType");
				startTime = r.get("StartTime");
				
				if(i%2 == 0){
					datatdclass = "data1";
				}else{
					datatdclass = "data2";
				}
				%>
				<tr>
<%--				<td class="<%=datatdclass %>" height="20" align="center"><input type="checkbox" name="pkfield" value="<%=ruleId %>"></td>--%>
				<td class="<%=datatdclass %>" align="center"><%=proName %></td>
				<td class="<%=datatdclass %>" align="center"><%=ruleType %></td>
				<td class="<%=datatdclass %>" align="center"><%=startTime %></td>
				<td class="<%=datatdclass %>" align="center"></td>
				<td class="<%=datatdclass %>" align="center"><a href="#">设置</a></td>
				</tr>
				<%
			}
		}
	%>		
	
</table>
</div>
  </body>
</html>
