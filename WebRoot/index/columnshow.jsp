<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.index.*,tms.datacenter.dbmanage.*,java.util.*"%>

<%
String cid = (String)request.getParameter("cid");
Column column = new Column();
Record r = column.getColumnByID(cid);
if(r != null){
	String clink = r.get("clink");
	String cheight = r.get("cheight");
	String cwidth = r.get("cwidth");
	String ctitle = r.get("ctitle");
	if(clink != null && !clink.startsWith("http"))
		clink = request.getContextPath()+clink;
	
	%>
	<table class="columnbartable">
	<tr><td height="25" width="20" class="columnbar1"></td>
	<td class="columnbar2"><font color="white"><%=ctitle %></font></td>
	<td class="columnbar3" width="20"></td></tr></table>
	<iframe name="frame<%=cid %>" src="<%=clink%>" scrolling="no" frameborder="0" width="100%" height="<%=cheight %>px" marginheight="0" marginwidth="0"></iframe>
	<%
}%>