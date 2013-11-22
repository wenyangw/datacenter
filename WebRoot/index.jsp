<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<LINK rel=stylesheet type=text/css href="<%=request.getContextPath() %>/login/images/login.css">
<title>系统登陆</title>
<script src="<%=request.getContextPath() %>/js/Calendar1.js" type="text/javascript"></script>
<script language="javascript">
function checkdata(){
	var username=document.getElementById("loginnameid").value;
	var userpass=document.getElementById("loginpswid").value;
	if(username==''){
		alert("请输入用户名！");
		return false;
	}
	if(userpass==''){
		alert("请输入密码！");
		return false;
	}
	return true;
}
</script>
</head>
<BODY onkeypress=onKeydownOper(event); class=body>
<DIV class=background-img>
<DIV class=primary-zone>
<DIV class=banner>
<UL>
  <LI></LI></UL></DIV>
<DIV class=login>
<form name="loginform" action="<%=request.getContextPath() %>/sysmanage/loginAction" method="POST">
<TABLE cellSpacing=0 cellPadding=1 width=400 align=right border=0>
  <TBODY>
  <TR>
    <TD></TD>
    <TD height=5 colSpan=2><SPAN class=prompt><STRONG><LABEL 
      id=err_info></LABEL></STRONG></SPAN>
      <DIV id=alarm class=prompt 
      style="DISPLAY: none"><BR></DIV>
      <DIV id=authInfo class=prompt style="DISPLAY: none"><SPAN id=authUser 
      style="COLOR: white"></SPAN><BR></DIV></TD></TR>
  <TR>
    <TD width=45 align=right>
      <P class=txt>用户名</P>
      <P class=txt>密　码</P></TD>
    <TD width=204>
      <P id=user_a class=user_on><INPUT onclick=set_focus_flag(); 
      onfocus=change_user_off(); onblur=change_user_on(); id=user class=user 
      onkeydown=set_focus_flag(); maxLength=95 name=loginName> </P>
      <P id=password_a class=password_on><INPUT onclick=set_focus_flag(); 
      onfocus="this.value='';change_pw_off();" onblur=change_pw_on(); 
      id=password class=user onkeydown=set_focus_flag(); maxLength=49 
      type=password name=loginPsw> </P></TD>
    <TD vAlign=middle width=145 align=left><INPUT onclick=if(KeyDownTime==0){onDo();} onfocus="this.className='buttons_off buttons';" onblur="this.className='buttons_on buttons';" id=button class=buttons type=submit value=登录 name=button> 
    </TD></TR>
  <TR>
    <TD></TD>
    
    <TD><font color="RED" SIZE="2"><%=request.getAttribute("errorMsg")==null?"":(String)request.getAttribute("errorMsg") %></font></TD></TR></TBODY></TABLE>
    </form>
    </DIV>
 </DIV></DIV>
<SCRIPT type=text/javascript>
focus_flag = false; //标记是否曾经主动定位到某输入框

function set_focus_flag()
{
	focus_flag = true;
}

function setClass(obj_id,_class)
{
	var obj=document.getElementById(obj_id);
	obj.setAttribute("class",_class);
	obj.setAttribute("className",_class);
}

function change_user_off()
{
	setClass("user_a", "user_off");
}

function change_user_on()
{
	setClass("user_a", "user_on");
}

function change_pw_off()
{
	setClass("password_a", "password_off");
}

function change_pw_on()
{
	setClass("password_a", "password_on");
}
</SCRIPT>
</BODY></HTML>