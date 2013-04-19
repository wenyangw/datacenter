<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.sysmanage.*"%>

<%@ page import="tms.datacenter.upload.RuleType"%>
<%@ page import="tms.datacenter.commontools.DateUtil"%>
<%@ page import="tms.datacenter.upload.UploadConfig"%>

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
  <form name="listform" id="listformid" action="<%=request.getContextPath() %>/upload/ruleManageAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="list">
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="<%=request.getAttribute("moduleid")==null?"":(String)request.getAttribute("moduleid") %>">
<div class="listdiv">
<table width="100%" class="listtable">
	<tr>
		<td class="tdhead" align="center" height="25">全选<input type="checkbox" name="selectAll" value="1" onclick="checkAll(this,'pkfield')"></td>
		<td class="tdhead" align="center">上传项目名称</td>
		<td class="tdhead" align="center">计划类型</td>
		<td class="tdhead" align="center">周期</td>
		<td class="tdhead" align="center">开始时间</td>
<%--		<td class="tdhead" align="center">操作</td>--%>
	</tr>
	<%
		//Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
		ArrayList records = (ArrayList)request.getAttribute("records");
		if(records != null && records.size() > 0){
			
			Record r = null;
			String ruleId = "";
			String uploadName = "";
			String ruleType = "";
			String cycle = "";
			String startTime = "";
			String datatdclass = "";
			UploadConfig uc = UploadConfig.getInstance();
			
			for(int i = 0; i < records.size(); i++){
				r = (Record)records.get(i);
				ruleId = r.get("RuleId");
				//uploadname = r.get("ProName");
				uploadName = uc.getUpload(r.get("UploadName")).getCnname();
				ruleType = RuleType.getRuleType(Integer.parseInt(r.get("RuleType")));
				cycle = r.get("Cycle");
				startTime = DateUtil.dateToString(DateUtil.stringToDate(r.get("StartTime"), "yyyy-mm-dd"), "yyyy-mm-dd");
				
				if(i%2 == 0){
					datatdclass = "data1";
				}else{
					datatdclass = "data2";
				}
				%>
				<tr>
				<td class="<%=datatdclass %>" height="20" align="center"><input type="checkbox" name="pkfield" value="<%=ruleId %>"></td>
				<td class="<%=datatdclass %>" align="center"><%=uploadName %></td>
				<td class="<%=datatdclass %>" align="center"><%=ruleType %></td>
				<td class="<%=datatdclass %>" align="center"><%=cycle %></td>
				<td class="<%=datatdclass %>" align="center"><%=startTime %></td>
<%--				<td class="<%=datatdclass %>" align="center"><a href="#">设置</a></td>--%>
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
