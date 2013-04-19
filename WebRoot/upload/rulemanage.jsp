<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.upload.RuleType"%>
<%@ page import="tms.datacenter.commontools.DateUtil"%>
<%@page import="tms.datacenter.upload.UploadConfig"%>
<%@page import="tms.datacenter.upload.UploadMsg"%>
<%@page import="java.util.ArrayList,java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath() %>/js/Calendar1.js" type="text/javascript"></script>
<script>
    function changeRuleType(ruletypeObj,defaulevalue){
       	var selectedvalue = ruletypeObj.options[ruletypeObj.options.selectedIndex].value;
       	var cycleobj = document.getElementById("cycle");
       	var span = document.getElementById("cyclespan");
       	if(selectedvalue=="0"){
       		span.style.display="none";
       		cycleobj.value="0";
       	}else{
       		span.style.display="inline";
       		cycleobj.value = defaulevalue;
       	}
    }
    </script>
</head>
<body>
<%

String ruleId = "";
String uploadName = "";
String uploadpk = "";
int ruleType = 0;
int cycle = 0;
String startTime = "";
ArrayList uploadlist = null;

Record rule = (Record)request.getAttribute("rule");
if(rule != null)
{
	ruleId = rule.get("RuleId");
	uploadpk = rule.get("UploadName");
	UploadConfig uc = UploadConfig.getInstance();
	uploadName = uc.getUpload(uploadpk).getCnname();
	ruleType = Integer.parseInt(rule.get("RuleType"));
	cycle = Integer.parseInt(rule.get("Cycle"));
	startTime = DateUtil.dateToString(DateUtil.stringToDate(rule.get("StartTime"), "yyyy-mm-dd"), "yyyy-mm-dd");
}else{
	uploadlist = (ArrayList)request.getAttribute("uploadlist");
}
%>
<form name="form1" action="<%=request.getContextPath() %>/upload/ruleManageAction" method="post">
<%
if(ruleId != null && ruleId.trim().length() > 0) {
%>
	<input type="hidden" name="methodName" id="methodNameId" value="update">
	<input type="hidden" name="moduleid" id="moduleidId" value="<%=request.getAttribute("moduleid")==null?"":(String)request.getAttribute("moduleid") %>">
	<input type="hidden" name="ruleId" value="<%=ruleId %>">
	<input type="hidden" name="uploadpk" value="<%=uploadpk %>">
<%}else{ %>
	<input type="hidden" name="methodName" id="methodNameId" value="add">
	<%} %>
<input type="hidden" name="specialparam" id="specialparamId" value="">
<table class="listtable" width="100%">
	<tr>
		<td class="tdhead" align="right" width="20%">上传项目名称</td>
		<td class="data1" align="left">
		<%
		if(ruleId != null && ruleId.trim().length() > 0) {
		%>
			<input type="text" class="input300red" readonly="readonly" name="uploadName" value="<%=uploadName %>">
		<%}else{ %>
		<select name="uploadName">
				<%
					for(int i = 0; i < uploadlist.size(); i++){
						UploadMsg um = (UploadMsg)uploadlist.get(i);
				%>
						<option value="<%=um.getSpecialparam() %>"><%=um.getCnname() %></option>
				<%
					}				
				%>
			</select>
		<%} %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">计划设定</td>
		<td class="data1" align="left">
			<select name="ruleType" id="ruleType" onchange="changeRuleType(this,'<%=cycle %>')">
				<%
					List ruleTypes = (List)RuleType.getRuleTypes();
					String val = "";
					for(int count = 0;count < ruleTypes.size(); count++){
						val = (String)ruleTypes.get(count);
						if(count == ruleType){
				%>
						<option value="<%=count %>" selected = "selected"><%=val %></option>
				<%
						}else{
				%>
						<option value="<%=count %>"><%=val %></option>
				<%
						}
						
					}				
				%>
			</select>
			<span id="cyclespan"><input type="text" width="300px" name="cycle" value="<%=cycle %>" id="cycle"></span>
			<script type="text/javascript">
				changeRuleType(document.getElementById('ruleType'),'<%=cycle %>')
			</script>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">计划开始时间</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="startTime" value="<%=startTime %>" id="datepicker" onclick = "calendar()" readonly="readonly">
		</td>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2">
			<input type="submit" value="确定">
			<input type="button" value="返回" onclick="javascript:history.back()">
		</td>
	</tr>
</table>
</form>
</body>
</html>