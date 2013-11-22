<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.util.*,tms.datacenter.index.*,tms.datacenter.sysmanage.*,tms.datacenter.dbmanage.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<title>集团公告</title>
<script language="JavaScript">
if (window.Event)
  document.captureEvents(Event.MOUSEUP);
  
function nocontextmenu()
{
 event.cancelBubble = true
 event.returnValue = false;
  
 return false;
}
  
function norightclick(e)
{
 if (window.Event)
 {
  if (e.which == 2 || e.which == 3)
   return false;
 }
 else
  if (event.button == 2 || event.button == 3)
  {
   event.cancelBubble = true
   event.returnValue = false;
   return false;
  }
  
}
document.oncontextmenu = nocontextmenu;  // for IE5+
document.onmousedown = norightclick;  // for all others

</script>
</head>
<body>
<div style="text-align:center">
<table width="100%" border="0" bgcolor="white">

<%
	String noticeid = request.getParameter("noticeid");
	Notice notice  = new Notice();
	ArrayList al = notice.getAllRecords("datacenter"," noticeid="+noticeid,"");
	if(al != null && al.size() > 0){
		Record r = null;
		String title = "";
		String updatetime = "";
		String titletemp = "";
		String content = "";
		String publisher = "";
		String pdffile = "";
		for(int i = 0; i < al.size(); i++){
			r = (Record)al.get(i);
			noticeid = r.get("noticeid");
			title = r.get("title");
			updatetime = r.get("updatetime");
			content = r.get("content");
			publisher = r.get("publisher");
			pdffile = r.get("pdffile");
			if(updatetime.length() > 10)
				updatetime = updatetime.substring(0,10);
			if(title != null && title.length() > 10)
				titletemp = title.substring(0,10)+"......";
			else
				titletemp = title;
			%>
			<tr><td height="40" align="left">
				<a href="#" onclick="javascript:history.back();" style="color:blue">集团公告</a>&gt;&gt;<%=title %>
			</td></tr>
			<tr><td align="center" height="60"><font size="5"><%=title %></font></td></tr>
			
			<%if(pdffile != null && pdffile.trim().length() > 0){
				%>
				<tr><td align="center">
				<div style="margin-top:-75px;margin-left:auto;margin-right:auto">
				<object classid="clsid:CA8A9780-280D-11CF-A24D-444553540000" id="Pdf1" width="90%" height="506">
				<param name="_Version" value="327680">
				<param name="_ExtentX" value="19315">
				<param name="_ExtentY" value="16034">
				<param name="_StockProps" value="0">
				<param name="SRC" value="<%=request.getContextPath()%><%=pdffile %>">
				</object>
				</div></td></tr>
				<%
			}%>
			<tr><td align="left">
			<font size="3"><%=content %></font></td></tr>
			<tr><td height="40"></td></tr>
			<tr><td align="right"><font size="3"><%=publisher %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br><%=updatetime %></font></td></tr>
			<%
			}
	}else{
		out.println("暂无公告");
	}
%>
</table>
</div>
</body>
</html>