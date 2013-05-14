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
String cid = "";
String ctitle = "";
String clink = "";
String showwhere = "";
String showtype = "";
String showorder = "";
String cheight = "";
String cwidth = "";
String memo = "";
Record column = (Record)request.getAttribute("record");
if(column != null)
{
	cid = column.get("cid");
	ctitle = column.get("ctitle");
	clink = column.get("clink")==null?"":column.get("clink");
	showwhere = column.get("showwhere")==null?"":column.get("showwhere");
	showtype = column.get("showtype")==null?"":column.get("showtype");
	showorder = column.get("showorder")==null?"":column.get("showorder");
	cheight = column.get("cheight")==null?"":column.get("cheight");
	memo = column.get("memo");
}
ArrayList pos  = (ArrayList)request.getAttribute("pos");
%>
<form name="form1" action="<%=request.getContextPath() %>/sysmanage/columnManageAction" method="post">
<%if(cid != null && cid.trim().length() > 0) {%>
				<input type="hidden" name="methodName" id="methodNameId" value="update">
			<%}else{
				%>
				<input type="hidden" name="methodName" id="methodNameId" value="add">
				<%
  }%>
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="m01m05">
<table class="listtable" width="100%">
	
			<tr>
		<td class="tdhead" align="right" width="20%">栏目编号</td>
		<td class="data1" align="left">
			<%if(cid != null && cid.trim().length() > 0) {%>
				<input type="text" class="input300red" readonly="readonly" name="cid" value="<%=cid==null?"":cid %>">
			<%}else{
				%>
				<input type="text" class="input300" name="cid" value="<%=cid==null?"":cid %>">
				<%
			}%>
			<%=column.getDataDesc("cid") %>
		</td>
	</tr>
		
	<tr>
		<td class="tdhead" align="right">标题</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="ctitle" value="<%=ctitle==null?"":ctitle %>">
			<%=column.getDataDesc("ctitle") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">链接地址</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="clink" value="<%=clink==null?"":clink %>">
			<%=column.getDataDesc("clink") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">显示位置</td>
		<td class="data1" align="left">
			<select name="showwhere" class="select300" >
				<option value=""></option>
				<%
					Record r = null;
					if(pos != null && pos.size() > 0){
						for(int i = 0; i < pos.size(); i++){
							r = (Record)pos.get(i);
							String selected = "";
							if(showwhere.equals(r.get("paramcode")))
								selected = "selected";
							%>
							<option value="<%=r.get("paramcode") %>" <%=selected %>><%=r.get("paramname")%></option>
							<%
						}
					}
				%>
				
			</select>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">显示方式</td>
		<td class="data1" align="left">
			<select name="showtype" class="select300" >
				<option value=""></option>
				<%
						String posname = "";
						for(int i = 1; i <= 2; i++){
							String selected = "";
							if(showtype.equals(i+""))
								selected = "selected";
							if("1".equals(i+"")){
								posname = "显示标题";
							}else if("2".equals(i+"")){
								posname = "显示内容";
							}
							%>
							<option value="<%=i %>" <%=selected %>><%=posname %></option>
							<%
						}
					
				%>
				
			</select>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">显示顺序</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="showorder" value="<%=showorder==null?"":showorder %>">
			<%=column.getDataDesc("showorder") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">栏目高度</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="cheight" value="<%=cheight==null?"":cheight %>">
			<%=column.getDataDesc("cheight") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">备注</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="memo" value="<%=memo==null?"":memo %>">
			<%=column.getDataDesc("memo") %>
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