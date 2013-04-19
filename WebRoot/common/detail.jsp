<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script src="<%=request.getContextPath() %>/js/Calendar1.js" type="text/javascript"></script>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
Record record = (Record)request.getAttribute("record");
Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
%>

<table class="listtable" width="100%">
	<%if(record!= null){
		ArrayList fieldslist = record.getFieldslist();
		Field field = null;
		if(fieldslist != null && fieldslist.size() > 0){
			for(int i = 0; i < fieldslist.size(); i++){
				field = (Field)fieldslist.get(i);
				if(field != null){
					%>
					<tr>
						<td class="tdhead" align="right" width="20%"><%=field.getFieldLabel() %></td>
						<td class="data1" align="left">
							<%=Record.getShowValue(fieldLabels,field.getFieldName(),field.getFieldValue()) %>
						</td>
					</tr>
					<%
				}
			}
		}
	} %>
	
	
	<tr>
		<td class="data1" align="center" colspan="2">
			<input type="button" value="返回" onclick="javascript:history.back()">
		</td>
	</tr>
</table>
</body>
</html>