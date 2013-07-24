<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.Record"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<script language="javascript">
	function logout(){
		if(window.confirm("您确定要退出系统吗？")){
			top.window.location='<%=request.getContextPath() %>/sysmanage/logoutAction';
		}
	}
	
	var now = null;
	var year="";
	var month="";
	var date="";
	var hour="";
	var minute="";
	var second="";
	var s = "";
	var day;
	var daypart = "";
	var week;
	var arr_week=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	function getNowTime(){
		now= new Date();
		year=now.getYear();
		month=(now.getMonth()+1)<10?"0"+(now.getMonth()+1):(now.getMonth()+1);
		date=now.getDate()<10?"0"+now.getDate():now.getDate();
		hour=now.getHours()<10?"0"+now.getHours():now.getHours();
		minute=now.getMinutes()<10?"0"+now.getMinutes():now.getMinutes();
		second=now.getSeconds()<10?"0"+now.getSeconds():now.getSeconds();
		day=now.getDay();  
		week=arr_week[day];
		if(now.getHours() >= 18 || now.getHours() < 6)
			daypart = "，晚上";
		else if(now.getHours() >= 6 && now.getHours() < 12)
			daypart = "，上午";
		else
			daypart = "，下午";
		s = daypart+"好，现在的时间是<font color=blue>"+year+"年"+month+"月"+date+"日 "+hour+":"+minute+":"+second+"&nbsp;"+week+"</font>，欢迎您使用本系统！";
		var timespan = document.getElementById("timespan");
		timespan.innerHTML=s;
	}
	function showTime(){
		setInterval("getNowTime()",1000);
	}
</script>
</head>
<body onload="getNowTime();showTime();">
<%
String str_user = "";
Record user = (Record)session.getAttribute("dcuser");
if(user != null){
	str_user+="<font color=\"red\"><b>"+user.get("username")+"</b></font> ";
}
%>
<table width="100%" height="76" class="toptable">
	<tr>
		<td height="52" width="5%" class="toptd" valign="MIDDLE" align="left">
			<img style="height:40px;margin-top:5px;" src="<%=request.getContextPath() %>/images/s.gif">
		</td>
		<td height="52" class="toptd" valign="MIDDLE" align="left">
			<font size="6" color="white">北方联合出版传媒数据共享平台</font>
		</td>
	</tr>
	<tr>
		<td height="20" class="toptd2" width="40%" colspan="2" valign="top" align="left">
			&nbsp;&nbsp;<%=str_user %><span id="timespan"></span>&nbsp;&nbsp;
		<a href="#" onclick="logout()">退出系统</a>&nbsp;&nbsp;&nbsp;<a href="<%=request.getContextPath() %>/index.jsp" target="_top">首页</a>
		</td>
	</tr>
</table>
</body>
</html>