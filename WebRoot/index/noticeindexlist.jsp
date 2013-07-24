<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.util.*,tms.datacenter.index.*,tms.datacenter.sysmanage.*,tms.datacenter.dbmanage.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<table width="100%" border="0">
<%
	Notice notice  = new Notice();
	ArrayList al = notice.getAllRecords("datacenter"," inuse=1"," order by updatetime desc");
	if(al != null && al.size() > 0){
		Record r = null;
		String title = "";
		String updatetime = "";
		String titletemp = "";
		String noticeid = "";
		for(int i = 0; i < al.size(); i++){
			r = (Record)al.get(i);
			noticeid = r.get("noticeid");
			title = r.get("title");
			updatetime = r.get("updatetime");
			if(updatetime.length() > 10)
				updatetime = updatetime.substring(0,10);
			if(title != null && title.length() > 10)
				titletemp = title.substring(0,10)+"......";
			else
				titletemp = title;
			%>
			<tr><td><a href="<%=request.getContextPath() %>/index/noticeindexshow.jsp?noticeid=<%=noticeid %>" title="<%=title %>" target="_blank">
			<%=titletemp %></a></td><td align="right" width="70"><%=updatetime %></td></tr>
			<%
			}
	}else{
		out.println("暂无公告");
	}
%>
</table>

</body>
</html>