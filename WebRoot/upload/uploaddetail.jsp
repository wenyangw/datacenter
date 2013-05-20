<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.sysmanage.*,tms.datacenter.commontools.*"%>
<%@page import="tms.datacenter.upload.*"%>

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
	String logNo = (String)request.getAttribute("logNo");
		
	//获得数据表的对象
	TableConfig tc = TableConfig.getInstance();
	TableDesc td = tc.getTable(tableName);
	
	//取得上传表的字段
	UploadConfig uc = UploadConfig.getInstance();
	UploadMsg um = uc.getUpload(tableName);
	ArrayList lists = um.getColumnList();
	
	
	%>
  </head>
  
  
  <body>
<form name="listform" id="listformid" action="<%=request.getContextPath() %>/upload/uploadManageAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="detail">
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="<%=request.getAttribute("moduleid")==null?"":(String)request.getAttribute("moduleid") %>">
<input type="hidden" name="tablename" value="<%=tableName %>">
<input type="hidden" name="logNo" value="<%=logNo %>">
<div class="listdiv">
<table width="100%" class="listtable">
	<tr>
		<td class="tdhead" align="center" height="25">全选<input type="checkbox" name="selectAll" value="1" onclick="checkAll(this,'pkfield')"></td>
		<%
		for(int i = 0; i < lists.size(); i++){
			//取得上传字段的名称
			String str = ((ColumnMsg)lists.get(i)).getFieldname();
			//取得数据表字段的描述
			String label = td.getField(str).getFieldLabel();
		%>
			<td class="tdhead" align="center"><%=label %></td>
		<%
		}
		%>	
		</tr>
	<%
		
		//Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
		ArrayList records = (ArrayList)request.getAttribute("details");	
	
		if(records != null && records.size() > 0){
					
			String datatdclass = "";
			String logId = "";
			String value = "";
			//String plink = "";
			
			for(int i = 0; i < records.size(); i++){
				Record r = (Record)records.get(i);
				
				logId = r.get("id");
				
				//plink = request.getContextPath()+"/upload/uploadManageAction?methodName=detail&pkfield="+logNo + "&tablename="+pkfield;
				if(i%2 == 0){
					datatdclass = "data1";
				}else{
					datatdclass = "data2";
				}
				
				%>
				<tr>
				<td class="<%=datatdclass %>" height="20" align="center"><input type="checkbox" name="pkfield" value="<%=logId %>"></td>
				<%
				for(int j = 0;j < lists.size();j++){
					String fieldName = ((ColumnMsg)lists.get(j)).getFieldname();
					value = r.get(fieldName);
				%>
				<td class="<%=datatdclass %>" align="center"><%=value %></td>
				<%
				}
				%>
<%--				<td class="<%=datatdclass %>" align="center"><a href="<%=plink%>"><详细</a></td>--%>
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
				out.println(RoleManage.paraUserOperationToButton(uo,Operation.SHOW_POS_UPDATE));
			%>
		</td>
	</tr>
</table>

</form>
  </body>
</html>
