<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>图片轮转</title>
<style>
	div.addpdiv{
		position:relative;
	}
	div.addiv{
		width:394px;height:324px;overflow:false;
		position:absolute;
		top:0px;
		left:0px;
	}
	div.adbardiv{
		width:390px;
		border-left:0px solid #A4D3EE;
		border-right:0px solid #A4D3EE;
		border-bottom:0px solid #A4D3EE;
		position:absolute;
		top:297px;
		left:0px;
		padding-right:2px;
	}
	img.adimg{
		width:394px;
		height:324px;
		border-width:0px;
	}
	ul.adul{
		list-style:none;padding:2px;margin:0px;width:346px;float:right;
	}
	li.adli1{
		background:gray;width:20px;text-align:center;float:right;cursor:pointer;
	}
	li.adli2{
		background:red;width:20px;text-align:center;float:right;cursor:pointer;color:white
	}
</style>
<%ArrayList al = (ArrayList)request.getAttribute("ads"); %>
<script type="text/javascript">
	var imgarr=new Array();
	<%
	Record r = null;
	String picpath = "";
	String link = "";
	String title = "";
	if(al != null && al.size() > 0){
		for(int i = 0; i < al.size(); i++){
			r = (Record)al.get(i);
			picpath = r.get("picpath");
			link = r.get("link");
			title = r.get("title");
			
			if(link != null && link.trim().length() > 0){
				if(!link.toLowerCase().startsWith("http"))
					link = request.getContextPath()+link;
			%>
				imgarr[<%=i%>]="<a href=\"<%=link%>\" target=\"_blank\"><img class=\"adimg\" alt=\"<%=title%>\"   src=\"<%=request.getContextPath()+picpath %>\"></a><br>";
			<%}else{
				%>
				imgarr[<%=i%>]="<img class=\"adimg\" alt=\"<%=title%>\" src=\"<%=request.getContextPath()+picpath %>\">";
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
<body>
<div>
<div id="addiv" class="addiv">
</div>
<div id="bardiv" class="adbardiv">
</div>
</div>
<script type="text/javascript">
init();play();
</script>
</body>
</html>