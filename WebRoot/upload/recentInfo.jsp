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
	
	ArrayList resultList = new ArrayList();
	String uploadNames = "";
	if(uploads != null || uploads.size() != 0){
		int i = 0;
		while(i < uploads.size()){
			UploadMsg um = (UploadMsg)uploads.get(i);
			uploadNames += "'" + um.getSpecialparam() + "'";
			i++;
			if(i < uploads.size()){
				uploadNames += ",";
			}
		}

		TableManage tm = new TableManage();

		resultList = tm.executeQuery("datacenter", 
			"select top 10 * from dc_uploadlog where uploadName in (" + uploadNames + ") order by uploadTime desc");
	}
	UploadConfig uc = UploadConfig.getInstance();
	
	%>
  </head>
  
  <body>
  <%
  if(resultList != null && resultList.size() != 0){
  	for(int j = 0; j < resultList.size(); j++){
	  Record record = (Record)resultList.get(j);
	  UploadMsg um = uc.getUpload(record.get("uploadName"));
	  %>
	  <%=record.get("username") + "在 " + record.get("uploadTime") + " 更新了 '" + um.getCnname() + "'"%>
	  <%if(j !=  resultList.size() -1) {
		  %>
		  <hr class="indexuphr">
		  <%
	  }%>
	  <%
  	}
  }else{
	  %>
	  无最新上传信息！
	  <%
  }
  %>

  </body>
</html>
