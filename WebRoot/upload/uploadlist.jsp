<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.sysmanage.*"%>

<%@page import="tms.datacenter.upload.UploadMsg"%>
<%@page import="tms.datacenter.upload.UploadLogManage"%>
<%@page import="tms.datacenter.upload.RuleManage"%>
<%@page import="tms.datacenter.commontools.DateUtil"%>

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
<form name="listform" id="listformid" action="<%=request.getContextPath() %>/upload/uploadManageAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="list">
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="<%=request.getAttribute("moduleid")==null?"":(String)request.getAttribute("moduleid") %>">
<div class="listdiv">
<table width="100%" class="listtable">
	<tr>
		<td class="tdhead" align="center" height="25">选择</td>
		<td class="tdhead" align="center">上传项目名称</td>
		<td class="tdhead" align="center">上次更新时间</td>
		<td class="tdhead" align="center">下次更新时间</td>
<%--		<td class="tdhead" align="center">上传</td>--%>
	</tr>
	<%
		//Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
		ArrayList records = (ArrayList)request.getAttribute("records");
		if(records != null && records.size() > 0){
			UploadMsg r = null;
			String specialparam = "";
			String cnname = "";
			String lastUpload = "";
			String nextUpload = "";
			String datatdclass = "";
			//String plink = "";
			//ProjectManage pm = new ProjectManage();
			UploadLogManage ulm = new UploadLogManage();
			RuleManage rm = new RuleManage();
			for(int i = 0; i < records.size(); i++){
				r = (UploadMsg)records.get(i);
				specialparam = r.getSpecialparam();
				
				cnname = r.getCnname();
				lastUpload = ulm.getLastUpload(specialparam);
				
				//lastDate = r.get("LastUpdate");
				//nextDate = pm.getNextUploadTime(Integer.parseInt(proId));
				nextUpload = rm.getNextUpload(specialparam);
				//plink = request.getContextPath()+"/upload/uploadManageAction?methodName=uploadPage&pkfield="+proId;
				if(i%2 == 0){
					datatdclass = "data1";
				}else{
					datatdclass = "data2";
				}
				%>
				<tr>
				<td class="<%=datatdclass %>" height="20" align="center"><input type="checkbox" name="tablename" value="<%=specialparam %>"></td>
				<td class="<%=datatdclass %>" align="center"><%=cnname %></td>
				<td class="<%=datatdclass %>" align="center"><%=lastUpload %></td>
				<td class="<%=datatdclass %>" align="center">
				<%if(DateUtil.daysBetween(DateUtil.getCurrentDateString(DateUtil.ISO_EXPANDED_DATE_FORMAT), nextUpload) > 0){ %>
				
				<font color="blue"><%=nextUpload %></font>
				<%}else{ %>
				<font color="red"><%=nextUpload %></font>
				<%} %>
				</td>
<%--				<td class="<%=datatdclass %>" align="center"><a href="<%=plink%>">上传</a></td>--%>
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
