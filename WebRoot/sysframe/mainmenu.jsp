<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.util.*,tms.datacenter.sysmanage.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<title>菜单</title>
<style type="text/css">

 </style>
<script language="javascript">
function clickMenu1(menuid){
	var sonmenu = document.getElementById(menuid+"m");
	var isshow = sonmenu.style.display;
	if(isshow=='none')
		isshow = "block";
	else
		isshow = "none";
	sonmenu.style.display = isshow;
}
</script>
</head>
<body>
<%
	String path = request.getContextPath();
	ArrayList menus = (ArrayList)request.getAttribute("menus");
	String pids = "";
	String sids = "";
%>
<div>
<%if(menus != null && menus.size() > 0){
	ParentMenu pm = null;
	SonMenu sm = null;
	String pname = "";
	String pid = "";
	String sname = "";
	String sid = "";
	String link = "";
	ArrayList sonlist = null;
	
	for(int i = 0; i < menus.size(); i++){
		pm = (ParentMenu)menus.get(i);
		if(pm != null){
			pid = pm.getId();
			pname = pm.getName();
			pids+= "'"+pid+"m',";
			%>
			<span style="background:green;"><font color="white" size="6"><%=pname %></font></span><br>
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
					%>
					<div id="<%=sid %>" class="sonmenudiv" onclick="top.main.window.location='<%=path+link %>';clickMenu2(this.id)"><%=sname %></div>
					<%
				}
			}
		}
		
	}
}else{
	out.println("<font color=\"red\">您还有被赋予系统权限 ！</font>");
}
%>

</div>
<script language="javascript">
function clickMenu1(menuid){
	var pids = new Array(<%=pids+"''"%>);
	var sids = new Array(<%=sids+"''"%>);
	var sonmenu = document.getElementById(menuid+"m");
	var isshow = sonmenu.style.display;
	if(isshow=='none'){
		var sondivs = null;
		for(i = 0; i < pids.length; i++){
			if(pids[i] == '')
				continue;
			sondivs = document.getElementById(pids[i]);
			if(sondivs && sondivs.id == sonmenu.id){
				sondivs.style.display = "block";
			}else if(sondivs && sondivs.id != sonmenu.id){
				sondivs.style.display = "none";
			}
		}
	}
	else
		sonmenu.style.display = "none";
}
function clickMenu2(menu2id){
	var sids = new Array(<%=sids+"''"%>);
	var sonmenu = null;
	for(i = 0; i < sids.length; i++){
		if(sids[i] == '')
			continue;
		sonmenu = document.getElementById(sids[i]);
		if(sonmenu && sonmenu.id == menu2id){
			sonmenu.className = "sonmenudiv2";
		}else if(sonmenu && sonmenu.id != menu2id){
			sonmenu.className = "sonmenudiv";
		}
	}
}
</script>
</body>
</html>