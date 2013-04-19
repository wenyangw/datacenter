<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
String paramcode = "";
String paramname = "";
String paramtype = "";
String parentparam = "";
String memo = "";
Record param = (Record)request.getAttribute("record");
if(param != null)
{
	paramcode = param.get("paramcode");
	paramname = param.get("paramname");
	paramtype = param.get("paramtype")==null?"":param.get("paramtype");
	parentparam = param.get("parentparam")==null?"":param.get("parentparam");
	memo = param.get("memo");
}
ArrayList paramtypes  = (ArrayList)request.getAttribute("paramtypes");
ArrayList parentParams  = (ArrayList)request.getAttribute("parentParams");
%>
<form name="form1" action="<%=request.getContextPath() %>/sysmanage/sysParamAction" method="post">
<%if(paramcode != null && paramcode.trim().length() > 0) {%>
				<input type="hidden" name="methodName" id="methodNameId" value="update">
			<%}else{
				%>
				<input type="hidden" name="methodName" id="methodNameId" value="add">
				<%
  }%>
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="m01m02">
<table class="listtable" width="100%">
	<tr>
		<td class="tdhead" align="right" width="20%">参数编码</td>
		<td class="data1" align="left">
			<%if(paramcode != null && paramcode.trim().length() > 0) {%>
				<input type="text" class="input300red" readonly="readonly" name="paramcode" value="<%=paramcode==null?"":paramcode %>">
			<%}else{
				%>
				<input type="text" class="input300" name="paramcode" value="<%=paramcode==null?"":paramcode %>">
				<%
			}%>
			<%=param.getDataDesc("paramcode") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">参数名称</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="paramname" value="<%=paramname==null?"":paramname %>">
			<%=param.getDataDesc("paramname") %>
		</td>
	</tr>
	
	<tr>
		<td class="tdhead" align="right">参数类型</td>
		<td class="data1" align="left">
			<select name="paramtype" class="select300" >
				<option value=""></option>
				<%
					Record r = null;
					if(paramtypes != null && paramtypes.size() > 0){
						for(int i = 0; i < paramtypes.size(); i++){
							r = (Record)paramtypes.get(i);
							String selected = "";
							if(paramtype.equals(r.get("type")))
								selected = "selected";
							%>
							<option value="<%=r.get("type") %>" <%=selected %>><%=r.get("name")%></option>
							<%
						}
					}
				%>
				
			</select>
		</td>
	</tr>
	<!--<tr>
		<td class="tdhead" align="right">上级参数</td>
		<td class="data1" align="left">
			<select name="parentparam" class="select300">
				<option value=""></option>
				<%
					
					if(parentParams != null && parentParams.size() > 0){
						for(int i = 0; i < parentParams.size(); i++){
							r = (Record)parentParams.get(i);
							String selected = "";
							if(parentparam.equals(r.get("paramcode")))
								selected = "selected";
							%>
							<option value="<%=r.get("paramcode") %>" <%=selected %>><%=r.get("paramname")+"("+r.get("paramcode")+")"%></option>
							<%
						}
					}
				%>
				
			</select>
		</td>
	</tr>  -->
	<tr>
		<td class="tdhead" align="right">备注</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="memo" value="<%=memo==null?"":memo %>">
			<%=param.getDataDesc("memo") %>
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