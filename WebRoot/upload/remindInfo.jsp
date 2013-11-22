<%@page import="tms.datacenter.commontools.DateUtil"%>
<%@page import="tms.datacenter.upload.RuleManage"%>
<%@page import="tms.datacenter.upload.UploadConfig"%>
<%@page import="tms.datacenter.upload.UploadMsg"%>
<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.sysmanage.*"%>


<%
String path = (String)request.getContextPath();
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
	
	<%
	Record user = (Record)session.getAttribute("dcuser");
	UploadPrivilege up = new UploadPrivilege();
	ArrayList uploads = new ArrayList();
	if(user != null){
		String loginName = user.get("loginname");
		uploads = up.getUserUploads(loginName);
	}else{
		uploads = up.getAllUploads();
	}
	
	RuleManage rm = new RuleManage();
	//ArrayList reminds = new ArrayList();
	String reminds = "";
	for(int i = 0; i < uploads.size(); i++){
		UploadMsg um = (UploadMsg)uploads.get(i);
		
		String nextUpload = rm.getNextUpload(um.getSpecialparam());
		
		if(nextUpload != null){
			int days = DateUtil.daysBetween(DateUtil.getCurrentDateString(DateUtil.ISO_EXPANDED_DATE_FORMAT), nextUpload);
			if(days < 0){
				reminds += um.getCnname() + " 应该在 " + nextUpload + " 上传！";
				out.println("<hr class=\"indexuphr\">");
			}
		}
	}
	
	%>
  </head>
  
  <body>
  <%if(reminds.length() == 0){ %>
  	无到期未上传信息！
  	<%}else{ %>
  	<%=reminds %>
  	<%} %>
  </body>
</html>
