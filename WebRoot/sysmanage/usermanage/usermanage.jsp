<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*,java.util.*,tms.datacenter.upload.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script src="<%=request.getContextPath() %>/js/Calendar1.js" type="text/javascript"></script>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
String loginname = "";
String loginpsw = "";
String username = "";
String gender = "";
String birthday = "";
String organisation = "";
String department = "";
String inuse = "";
String memo = "";
Record user = (Record)request.getAttribute("record");
if(user != null)
{
	loginname = user.get("loginname")==null?"":user.get("loginname");
	loginpsw = user.get("loginpsw")==null?"":user.get("loginpsw");
	username = user.get("username")==null?"":user.get("username");
	gender = user.get("gender")==null?"":user.get("gender");
	birthday = user.get("birthday")==null?"":user.get("birthday");
	organisation = user.get("organisation")==null?"":user.get("organisation");
	department = user.get("department")==null?"":user.get("department");
	inuse = user.get("inuse")==null?"":user.get("inuse");
	memo = user.get("memo")==null?"":user.get("memo");
}
ArrayList orgs  = (ArrayList)request.getAttribute("orgs");
ArrayList depts  = (ArrayList)request.getAttribute("depts");


%>
<form name="form1" action="<%=request.getContextPath() %>/sysmanage/userManageAction" method="post">
<%if(loginname != null && loginname.trim().length() > 0) {%>
				<input type="hidden" name="methodName" id="methodNameId" value="update">
			<%}else{
				%>
				<input type="hidden" name="methodName" id="methodNameId" value="add">
				<%
  }%>
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="m01m03">
<table class="listtable" width="100%">
	<tr>
		<td class="tdhead" align="right" width="20%">登录名</td>
		<td class="data1" align="left">
			<%if(loginname != null && loginname.trim().length() > 0) {%>
				<input type="text" class="input300red" readonly="readonly" name="loginname" value="<%=loginname==null?"":loginname %>">
			<%}else{
				%>
				<input type="text" class="input300" name="loginname" value="<%=loginname==null?"":loginname %>">
				<%
			}%>
			<%=user.getDataDesc("loginname") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">登录密码</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="loginpsw" value="<%=loginpsw==null?"":loginpsw %>">
			<%=user.getDataDesc("loginpsw") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">姓名</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="username" value="<%=username==null?"":username %>">
			<%=user.getDataDesc("lusername") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">性别</td>
		<td class="data1" align="left">
		<%if("1".equals(gender)){
			%><select name="gender" class="select300" >
				<option value=""></option>
				<option value="1" selected>男</option>
				<option value="2">女</option>
			 </select><%
		}else if("2".equals(gender)){
			%><select name="gender" class="select300" >
				<option value=""></option>
				<option value="1">男</option>
				<option value="2" selected>女</option>
			 </select><%
		}else{
			%><select name="gender" class="select300" >
				<option value=""></option>
				<option value="1">男</option>
				<option value="2">女</option>
			 </select><%
		}%>
			
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">出生日期</td>
		<td class="data1" align="left">
			<input type="text" class="input300" onclick="calendar()" name="birthday" value="<%=birthday==null||birthday.trim().length() == 0?"":birthday.substring(0,10) %>">
			<%=user.getDataDesc("birthday") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">所在单位</td>
		<td class="data1" align="left">
			<select name="organisation" class="select300" >
				<option value=""></option>
				<%
					Record r = null;
					if(orgs != null && orgs.size() > 0){
						for(int i = 0; i < orgs.size(); i++){
							r = (Record)orgs.get(i);
							String selected = "";
							if(organisation.equals(r.get("paramcode")))
								selected = "selected";
							%>
							<option value="<%=r.get("paramcode") %>" <%=selected %>><%=r.get("paramname")%></option>
							<%
						}
					}
				%>
				
			</select>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">部门</td>
		<td class="data1" align="left">
			<select name="department" class="select300" >
				<option value=""></option>
				<%
					
					if(depts != null && depts.size() > 0){
						for(int i = 0; i < depts.size(); i++){
							r = (Record)depts.get(i);
							String selected = "";
							if(department.equals(r.get("paramcode")))
								selected = "selected";
							%>
							<option value="<%=r.get("paramcode") %>" <%=selected %>><%=r.get("paramname")%></option>
							<%
						}
					}
				%>
				
			</select>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">是否可用</td>
		<td class="data1" align="left">
		<%if("1".equals(gender)){
			%><select name="inuse" class="select300" >
				<option value="1" selected>是</option>
				<option value="0">否</option>
			 </select><%
		}else if("0".equals(gender)){
			%><select name="inuse" class="select300" >
				<option value="1">是</option>
				<option value="0" selected>否</option>
			 </select><%
		}else{
			%><select name="inuse" class="select300" >
				<option value="1">是</option>
				<option value="0">否</option>
			 </select><%
		}%>
			
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">备注</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="memo" value="<%=memo==null?"":memo %>">
			<%=user.getDataDesc("memo") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="center" colspan="2">用户角色</td>
	</tr>
	<tr>
		<td class="data1"  colspan="2">
			<%
			ArrayList allroles = (ArrayList)request.getAttribute("allroles");
			ArrayList userroles = (ArrayList)request.getAttribute("userroles");
			ArrayList userRoleCodes = new ArrayList();
			if(userroles != null && userroles.size() > 0){
				Record userRole = null;
				String userRoleCode = "";
				for(int i = 0; i < userroles.size(); i++){
					userRole = (Record)userroles.get(i);
					userRoleCode = userRole.get("rolecode");
					if(userRoleCode != null&&userRoleCode.trim().length() > 0){
						userRoleCodes.add(userRoleCode);
					}
				}
			}
			if(allroles != null && allroles.size() > 0){
				Record role = null;
				String roleCode = "";
				String roleName = "";
				for(int i = 0; i < allroles.size(); i++){
					role = (Record)allroles.get(i);
					roleCode = role.get("rolecode");
					roleName = role.get("rolename");
					if(roleCode != null&&roleCode.trim().length() > 0){
						if(userRoleCodes.contains(roleCode)){
							%>
							<input type="checkbox" name="rolecode" checked="checked" value="<%=roleCode %>"><%=roleName+"("+roleCode+")" %>&nbsp;
							<%
						}else{
							%>
							<input type="checkbox" name="rolecode" value="<%=roleCode %>"><%=roleName+"("+roleCode+")" %>&nbsp;
							<%
						}
					}
				}
			}
			%>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="center" colspan="2">表内容访问控制</td>
	</tr>
	<tr>
		<td class="data1"  colspan="2">
			<%
			ArrayList allcontrols =  (ArrayList)request.getAttribute("allcontrols");
			ArrayList usercontrols =  (ArrayList)request.getAttribute("usercontrols");
			if(usercontrols == null)
				usercontrols = new ArrayList();
			TableConfig tc = TableConfig.getInstance();
			if(allcontrols != null && allcontrols.size() > 0){
				Record control = null;
				String controlcode = "";
				String controldesc = "";
				String tablename = "";
				String tablecnname = "";
				TableDesc td = null;
				for(int i = 0; i < allcontrols.size(); i++){
					control = (Record)allcontrols.get(i);
					controlcode = control.get("controlcode");
					tablename = control.get("tablename");
					controldesc = control.get("controldesc");
					td = tc.getTable(tablename);
					if(td != null)
						tablecnname = td.getCnname();
					if(controlcode != null&&controlcode.trim().length() > 0){
						if(usercontrols.contains(controlcode)){
							%>
							<input type="checkbox" name="controlcode" checked="checked" value="<%=controlcode %>"><%=tablecnname+"("+tablename+")_"+controldesc %>&nbsp;
							<%
						}else{
							%>
							<input type="checkbox" name="controlcode" value="<%=controlcode %>"><%=tablecnname+"("+tablename+")_"+controldesc %>&nbsp;
							<%
						}
					}
				}
			}
			%>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="center" colspan="2">上传权限控制</td>
	</tr>
	<tr>
		<td class="data1"  colspan="2">
			<%
			ArrayList allupload =  (ArrayList)request.getAttribute("allupload");
			ArrayList userupload =  (ArrayList)request.getAttribute("userupload");
			if(userupload == null)
				userupload = new ArrayList();
			if(allupload != null && allupload.size() > 0){
				String upname = "";
				String specialparam = "";
				UploadMsg um = null;
				for(int i = 0; i < allupload.size(); i++){
					um = (UploadMsg)allupload.get(i);
					upname = um.getCnname();
					specialparam = um.getSpecialparam();
					if(specialparam != null&&specialparam.trim().length() > 0){
						if(userupload.contains(specialparam)){
							%>
							<input type="checkbox" name="uploadspecialparam" checked="checked" value="<%=specialparam %>"><%=upname%>&nbsp;
							<%
						}else{
							%>
							<input type="checkbox" name="uploadspecialparam"  value="<%=specialparam %>"><%=upname%>&nbsp;
							<%
						}
					}
				}
			}
			%>
		</td>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2">
			<input type="submit" value="确定">
			<input type="button" value="返回" onclick="javascript:history.back()">
		</td>
	</tr>
</table>
</form>
</body>
</html>