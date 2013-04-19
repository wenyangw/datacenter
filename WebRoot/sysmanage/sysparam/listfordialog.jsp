<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.util.*,tms.datacenter.sysmanage.*"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.commontools.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/common.js"> </script>
<script language="javascript">
function selectParam(){
	var params = document.getElementsByName("pkfield");
	var fieldtype = document.getElementById("fieldtypeid");
	var operate = document.getElementById("operateid");
	
	var returnvalue = "";
	var pkfield = null;
	if(params && params.length > 0){
		for(i = 0; i <  params.length; i++){
			pkfield = params[i];
			if(pkfield.type=="checkbox" && pkfield.checked){
				if('<%=Field.FIELD_TYPE_INT%>'==fieldtype.value ||'<%=Field.FIELD_TYPE_INT%>'==fieldtype.value){
					if(operate.value=='9')
						returnvalue += pkfield.value+",";
					else
						returnvalue = pkfield.value;
				}else{
					if(operate.value=='9')
						returnvalue += "'"+pkfield.value+"',";
					else
						returnvalue = "'"+pkfield.value+"'";
					
				}
			}else if(pkfield.type=="radio" && pkfield.checked){
				if('<%=Field.FIELD_TYPE_INT%>'==fieldtype.value ||'<%=Field.FIELD_TYPE_INT%>'==fieldtype.value){
					if(operate.value=='9')
						returnvalue += pkfield.value+",";
					else
						returnvalue = pkfield.value;
				}else{
					if(operate.value=='9')
						returnvalue += "'"+pkfield.value+"',";
					else if(operate.value=='6')
						returnvalue = " like '"+pkfield.value+"%'";
					else if(operate.value=='7')
						returnvalue = " like '%"+pkfield.value+"'";
					else if(operate.value=='8')
						returnvalue = " like '%"+pkfield.value+"%'";
					else
						returnvalue = "'"+pkfield.value+"'";
					
				}
			}
		}
	}
	if(returnvalue=='')
	{
		alert("请选择参数！");
		return;
	}
	if(returnvalue.match("\\w*,"))
		returnvalue = returnvalue.substring(0,returnvalue.length-1);
	window.returnValue=returnvalue;
	window.close();
}

</script>

</head>
<body>
<%
	String fieldtype = (String)request.getAttribute("fieldtype");
	String operate = (String)request.getAttribute("operate");
	String inputtype = "radio";
	if(operate != null && operate.matches("\\d+")&&Integer.parseInt(operate) > 8)
		inputtype = "checkbox";
%>
<form name="listform" id="listformid" action="<%=request.getContextPath() %>/sysmanage/sysParamAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="listfordialog">
<input type="hidden" name="specialparam" id="specialparamId" value="">

<input type="hidden" name="fieldtype" id="fieldtypeid" value="<%=fieldtype==null?"":fieldtype %>">
<input type="hidden" name="operate" id="operateid" value="<%=operate==null?"":operate %>">
<div style="height:450px;">
<table width="100%" class="listtable">
	<tr>
		<td class="tdhead" align="center" height="25">选择</td>
		<td class="tdhead" align="center">参数编码</td>
		<td class="tdhead" align="center">参数名称</td>
		<td class="tdhead" align="center">参数类型</td>
	</tr>
	<%
		Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
		ArrayList records = (ArrayList)request.getAttribute("records");
		
		if(records != null && records.size() > 0){
			Record r = null;
			String paramcode = "";
			String paramname = "";
			String paramtype = "";
			String parentparam = "";
			String updatetime = "";
			String operator = "";
			String memo = "";
			String datatdclass = "";
			for(int i = 0; i < records.size(); i++){
				r = (Record)records.get(i);
				paramcode = r.get("paramcode");
				paramname = r.get("paramname");
				paramtype = r.get("paramtype");
				parentparam = r.get("parentparam");
				updatetime = r.get("updatetime");
				operator = r.get("operator");
				memo = r.get("memo");
				if(i%2 == 0){
					datatdclass = "data1";
				}else{
					datatdclass = "data2";
				}
				%>
				<tr>
				<td class="<%=datatdclass %>" height="20" align="center"><input type="<%=inputtype %>" name="pkfield" value="<%=paramcode %>"></td>
				<td class="<%=datatdclass %>" align="center"><%=paramcode %></td>
				<td class="<%=datatdclass %>" align="center"><%=paramname %></td>
				<td class="<%=datatdclass %>" align="center"><%=Record.getShowValue(fieldLabels,"paramtype",paramtype) %></td>
				</tr>
				<%
			}
		}
	%>
</table>
</div>
<table class="bottomtable" width="100%">
	<tr>
		<td align="center">
			<input type="button" value="确定" onclick="selectParam();">
			<input type="button" value="关闭" onclick="window.returnValue='';window.close();">
		</td>
	</tr>
</table>
</form>
</body>
</html>