<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.util.*,tms.datacenter.sysmanage.*,tms.datacenter.dbmanage.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<table class="columnbartable">
	<tr><td height="25" width="20" class="columnbar1"></td>
	<td class="columnbar2"><font color="white">办公版块</font></td>
	<td class="columnbar3" width="20"></td></tr></table>
<%
ArrayList pmenusshow = new ArrayList();
Record ruser = (Record)session.getAttribute("dcuser");
if(ruser!=null){
MenuConfig menu = new MenuConfig();
if("admin".equalsIgnoreCase(ruser.get("loginname"))){
	pmenusshow =  menu.getParentmenus();
}else{
Hashtable userpri = ruser.getUserPrivilege();

ParentMenu pm = null;
SonMenu sm = null;

ArrayList pmenus = menu.getParentmenus();
ArrayList smenus = null;

ParentMenu pmshow = null;
if(pmenus != null && pmenus.size() > 0){
	String actionclass = "";
	String moduleid = "";
	int privilege = 0;
	String priuser = "";
	for(int i = 0; i < pmenus.size(); i++){
		pm = (ParentMenu)pmenus.get(i);
		boolean hassonmenu = false;
		pmshow = new ParentMenu();
		if(pm != null){
			pmshow.setId(pm.getId());
			pmshow.setName(pm.getName());
			smenus = pm.getSonmenus();
			if(smenus != null && smenus.size() > 0){
				for(int j = 0; j < smenus.size(); j++){
					sm = (SonMenu)smenus.get(j);
					if(sm != null){
						actionclass = sm.getActionclass();
						moduleid = sm.getId();
						if(moduleid == null)
							moduleid = "";
						privilege = sm.getPrivilege();
						priuser = (String)userpri.get(actionclass+"/"+moduleid);
						if(priuser == null || !priuser.matches("\\d+"))
							priuser = "0";
						if((privilege&Integer.parseInt(priuser)) > 0){
							pmshow.addSonMenu(sm);
							hassonmenu = true;
						}
					}
				}
			}
		}
		if(hassonmenu)
			pmenusshow.add(pmshow);
	}
}
}
}

%>


<%if(pmenusshow != null && pmenusshow.size() > 0){
	ParentMenu pm = null;
	SonMenu sm = null;
	String pname = "";
	String pid = "";
	String sname = "";
	String sid = "";
	String link = "";
	ArrayList sonlist = null;
	String pids = "";
	String sids = "";
	String path = request.getContextPath();
	for(int i = 0; i < pmenusshow.size(); i++){
		pm = (ParentMenu)pmenusshow.get(i);
		if(pm != null){
			pid = pm.getId();
			pname = pm.getName();
			pids+= "'"+pid+"m',";
			%>
			<table width="100%"><tr><td class="indexmenutitle"  align="center" colspan="2"><%=pname %></td></tr>
			<%
			sonlist = pm.getSonmenus();
			if(sonlist != null && sonlist.size() > 0){
				for(int j = 0; j < sonlist.size(); j++){
					sm = (SonMenu)sonlist.get(j);
					if(sm == null)
						continue;
					sid = sm.getId();
					sname = sm.getName();
					link = sm.getLink();
					sids+= "'"+sid+"',";
					if(j%2 == 0)
						out.println("<tr>");
					%>
					<td class="indexmenucontent">
					<img style="margin-top:5px;" src="<%=request.getContextPath()+"/images/columntitle.jpg" %>"><a href="<%=path+link %>" target="_blank"><%=sname %></a></td>
					<%
					if(j%2 != 0)
						out.println("</tr>");
				}
			}
			if(sonlist.size()%2!=0)
				out.println("<td></td></tr>");
			%></table><%
		}
		
	}
}else{
	out.println("<font color=\"red\">您还有被赋予系统权限,或登录已超时 ！</font>");
}
%>
<hr></hr>
</body>
</html>