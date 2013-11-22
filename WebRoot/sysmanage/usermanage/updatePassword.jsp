<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*,java.util.*,tms.datacenter.upload.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改密码</title>
<script src="<%=request.getContextPath() %>/js/Calendar1.js" type="text/javascript"></script>
<script src="<%=request.getContextPath() %>/js/jquery-1.8.3.js" type="text/javascript"></script>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function updatePsw(){
		var oldpassword = $("#oldpassword").val();
		var newpassword = $("#newpassword").val();
		var confirmpassword = $("#confirmpassword").val();
		if(newpassword != confirmpassword){
			alert("您输入的新密码和确认密码不一致！");
			return;
		}
		$.post("<%=request.getContextPath()%>/sysmanage/usermanage/exeUpdatePassword.jsp",
		{
			oldpassword:oldpassword,
			newpassword:newpassword,
			confirmpassword:confirmpassword		
		},
		updateRes()
		);
	}
	function updateRes(){
			return function(data,status){
		    	if(status=="success"){
		    		if(data.indexOf("s") == 0){
		    			alert("修改成功！");
			    		window.close();
		    		}else{
		    			alert(data);
		    		}
		    	} 
		    };
	}
</script>
</head>
<body>
<table class="listtable" width="100%">
	<tr>
		<td class="tdhead" align="right" width="20%">旧密码</td>
		<td class="data1" align="left">
			<input type="password" id="oldpassword" style="width:90%" name="oldpassword" value=""><font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">新密码</td>
		<td class="data1" align="left">
			<input type="password" id="newpassword" style="width:90%" name="newpassword" value=""><font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">确认密码</td>
		<td class="data1" align="left">
			<input type="password" id="confirmpassword" style="width:90%" name="confirmpassword" value=""><font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2" valign="bottom" height="50">
			<input type="button" value="确定" onclick="updatePsw()">
			<input type="button" value="取消" onclick="javascript:window.close()">
		</td>
	</tr>
</table>
</body>
</html>