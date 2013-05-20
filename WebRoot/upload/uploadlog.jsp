<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.sysmanage.*,tms.datacenter.commontools.*"%>
<%@page import="tms.datacenter.upload.UploadConfig"%>

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
<%
String tableName = (String)request.getAttribute("tablename");
%>
  </head>
  
  <body>
<form name="listform" id="listformid" action="<%=request.getContextPath() %>/upload/uploadManageAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="log">
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="tablename" value="<%=tableName %>">
<input type="hidden" name="moduleid" id="moduleidId" value="<%=request.getAttribute("moduleid")==null?"":(String)request.getAttribute("moduleid") %>">
<div class="listdiv">
<table width="100%" class="listtable">
	<tr>
		<td class="tdhead" align="center" height="25">选择</td>
		<td class="tdhead" align="center">上传项目名称</td>
		<td class="tdhead" align="center">更新时间</td>
		<td class="tdhead" align="center">操作员</td>
		<td class="tdhead" align="center">部门</td>
	</tr>
	<%
		
	//Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
		ArrayList records = (ArrayList)request.getAttribute("loglist");
		if(records != null && records.size() > 0){
			Record r = null;
			String uploadName = "";
			String logId = "";
			String logNo = "";
			//String pkfield = "";
			String uploadTime = "";
			String username = "";
			String orgName = "";
			String datatdclass = "";
			//String plink = "";
			UploadConfig uc = UploadConfig.getInstance();
			for(int i = 0; i < records.size(); i++){
				r = (Record)records.get(i);
				//pkfield = r.get("uploadName");
				logId = r.get("logId");
				uploadName = uc.getUpload(tableName).getCnname();
				uploadTime = r.get("uploadTime");
				username = r.get("username");
				orgName = r.get("organization");
				logNo = r.get("logNo");
				
				//plink = request.getContextPath()+"/upload/uploadManageAction?methodName=detail&pkfield="+logNo + "&tablename="+pkfield;
				if(i%2 == 0){
					datatdclass = "data1";
				}else{
					datatdclass = "data2";
				}
				%>
				<tr>
				<td class="<%=datatdclass %>" height="20" align="center"><input type="checkbox" name="logNo" value="<%=logNo %>"></td>
				<td class="<%=datatdclass %>" align="center"><%=uploadName %></td>
				<td class="<%=datatdclass %>" align="center"><%=uploadTime %></td>
				<td class="<%=datatdclass %>" align="center"><%=username %></td>
				<td class="<%=datatdclass %>" align="center"><%=orgName %></td>
				</tr>
				<%
			}
		}
	%>		
	
</table>
</div>
<%=request.getAttribute("pager")==null?"":(String)request.getAttribute("pager") %>
<hr>

<table class="bottomtable" width="100%">
	<tr>
		<td align="right">
			<input type="button" value="返回" onclick="javascript:history.back()">
		</td>
		<td align="right">
			<%
				ArrayList uo = (ArrayList)request.getAttribute("uo");
				out.println(RoleManage.paraUserOperationToButton(uo,"2"));
			%>
		</td>
	</tr>
</table>


<!-- 
<table class="bottomtable" width="100%">
	<tr>
		<td align="center">
			<input type="button" value="返回" onclick="javascript:history.back()">
		</td>
	</tr>
</table>
 -->
</form>
  </body>
</html>
