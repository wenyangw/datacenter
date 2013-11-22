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
			daypart = "<font color=\"white\">，晚上";
		else if(now.getHours() >= 6 && now.getHours() < 12)
			daypart = "<font color=\"white\">，上午";
		else
			daypart = "<font color=\"white\">，下午";
		s = daypart+"好，现在的时间是</font><font color=\"#e2cc00\">"+year+"年"+month+"月"+date+"日 "+hour+":"+minute+":"+second+"&nbsp;"+week+"</font><font color=\"white\">，欢迎您使用本系统！</font>";
		var timespan = document.getElementById("timespan");
		timespan.innerHTML=s;
	}
	function showTime(){
		setInterval("getNowTime()",1000);
	}
	function toWelcome(){
		parent.main.location="<%=request.getContextPath() %>/index/loginmain.jsp";
		parent.menu.location="<%=request.getContextPath() %>/sysmanage/menuAction";
	}
	function showUpdatePsw(){
		window.showModelessDialog("<%=request.getContextPath()%>/sysmanage/usermanage/updatePassword.jsp","","dialogWidth:300px;dialogHeight:200px;scroll:no;status:no");
	}
</script>
</head>
<body onLoad="getNowTime();showTime();" style="padding:0px;margin-left:0px;margin-right:0px;">
<%
String str_user = "";
Record user = (Record)session.getAttribute("dcuser");
if(user != null){
	str_user+="<font color=\"#e2cc00\"><b>"+user.get("username")+"</b></font> ";
}
%>


<div style="width:100%;margin-left:0px;height:55px">
	<div style="float:left;width:35%;height:55px;background-image:url(../images/top_bj.jpg)">
			<img src="<%=request.getContextPath()%>/images/top_logobj.jpg" border="0" height="55">
		</div>
		<div style="width:65%;float:left;text-align:right;height:55px;background-image:url(../images/top_bj.jpg)">
		<%=str_user %><span id="timespan"></span>
			<font color="white">[&nbsp;<a href="#" style="color:white" onClick="showUpdatePsw()">修改密码</a>&nbsp;][&nbsp;<a href="#" style="color:white" onClick="logout()">退出系统</a>&nbsp;]&nbsp;&nbsp;&nbsp;[&nbsp;<a href="#" style="color:white" onClick="toWelcome();">欢迎页</a>&nbsp;]</font>
			
		</div>
</div>
</body>
</html>