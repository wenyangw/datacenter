<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>图片轮转</title>
<style>
	div.addiv{
		width:750px;height:300px;overflow:false;
		margin-left:25px;
		margin-right:25px;
		margin-top:5px;
		
	}
	div.adbardiv{
		width:700px;background:yellow;
		margin-left:25px;
		margin-right:25px;
		margin-bottom:5px;
		border-left:1px solid black;
		border-right:1px solid black;
		border-bottom:1px solid black;
	}
	img.adimg{
		width:700px;
		height:300px;
		border-width:1px;
	}
	ul.adul{
		list-style:none;padding:2px;margin:0px;width:698px;float:right;
	}
	li.adli1{
		background:gray;width:20px;text-align:center;float:right;cursor:pointer;
	}
	li.adli2{
		background:white;width:20px;text-align:center;float:right;cursor:pointer;
	}
</style>
<%ArrayList al = (ArrayList)request.getAttribute("ads"); %>
<script type="text/javascript">
	var imgarr=new Array();
	<%
	Record r = null;
	String picpath = "";
	String link = "";
	if(al != null && al.size() > 0){
		for(int i = 0; i < al.size(); i++){
			r = (Record)al.get(i);
			picpath = r.get("picpath");
			link = r.get("link");
			if(link != null && link.trim().length() > 0){
				if(!link.toLowerCase().startsWith("http"))
					link = request.getContextPath()+link;
			%>
				imgarr[<%=i%>]="<a href=\"<%=link%>\" target=\"_blank\"><img class=\"adimg\"   src=\"<%=request.getContextPath()+picpath %>\"></a>";
			<%}else{
				%>
				imgarr[<%=i%>]="<img class=\"adimg\" src=\"<%=request.getContextPath()+picpath %>\">";
				<%
			}
		}
	}%>
	var currnum = 1;
	var maxnum = 0;
	var isplay = 1;
	function init(){
		var barcode="";
		barcode+="<ul class=\"adul\">";
		for(i = imgarr.length-1;i >= 0; i--){
			barcode+="<li class=\"adli1\" onmouseout=\"noselect()\" onmouseover=\"selectOnPic("+(i+1)+")\" id=\"bar"+(i+1)+"\">"+(i+1)+"</li>";
		}
		barcode+="</ul>";
		document.getElementById("bardiv").innerHTML=barcode;
		maxnum = imgarr.length;
		
		changePic();
	}
	function selectOnPic(num){
		isplay = 0;
		currnum = num;
		var picstr = imgarr[num-1];
		document.getElementById("addiv").innerHTML=picstr;
		for(i = 1; i <= maxnum; i++){
			if(i==num)
				document.getElementById("bar"+i).className="adli2";
			else
				document.getElementById("bar"+i).className="adli1";
		}
	}
	function noselect(){
		isplay = 1;
	}
	function changePic(){
		if(isplay==0)
			return;
		currnum++;
		if(currnum > maxnum)
			currnum = 1;
		var picstr = imgarr[currnum-1];
		document.getElementById("addiv").innerHTML=picstr;
		for(i = 1; i <= maxnum; i++){
			if(i==currnum)
				document.getElementById("bar"+i).className="adli2";
			else
				document.getElementById("bar"+i).className="adli1";
		}
		//currnum++;
	}
	function play(){
		setInterval("changePic()",3000);
	}
</script>
</head>
<body bgcolor="#AEEEEE">
<div id="addiv" class="addiv">
</div>
<div id="bardiv" class="adbardiv">
</div>
<script type="text/javascript">
init();play();
</script>
</body>
</html>