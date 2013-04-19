<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*,java.util.*,tms.datacenter.sysmanage.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function checkModule(parentCheckbox){
		
		var pid = parentCheckbox.id;
		var idlength = pid.length;
		var sonchecked = parentCheckbox.checked;
		var roleform = document.getElementById("roleform");
		var controls = roleform.elements;
		
		if(controls && controls.length > 0){
			var con_obj = null;
			for(i = 0; i < controls.length; i++){
				con_obj = controls[i];
				if(con_obj && con_obj.type == 'checkbox' && con_obj.id.match("^"+pid+"\\w+$") && con_obj.id.length > idlength){
					con_obj.checked = sonchecked;
				}
				else
					continue;
			}
		}
	}
</script>
</head>
<body>
<%
String rolecode = "";
String roleName = "";
String memo = "";
Record role = (Record)request.getAttribute("role");
if(role != null)
{
	rolecode = role.get("rolecode");
	roleName = role.get("rolename");
	memo = role.get("memo");
}
%>
<form name="form1" id="roleform" action="<%=request.getContextPath() %>/sysmanage/roleManageAction" method="post">
<input type="hidden" name="methodName" value="updatePrivilege">
<input type="hidden" name="rolecode" value="<%=rolecode %>">
<input type="hidden" name="moduleid" id="moduleidId" value="m01m01">
<table class="listtable" width="100%">
	<tr>
		<td class="tdhead" align="right" width="20%">角色编码</td>
		<td class="data1" align="left">
			<%=rolecode %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">角色名称</td>
		<td class="data1" align="left">
			<%=roleName %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">备注</td>
		<td class="data1" align="left">
			<%=memo %>
		</td>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2">
			 角色权限分配
		</td>
	</tr>
	<tr><td class="data1" colspan="2">
	<%
		ArrayList allPrivilege = (ArrayList)request.getAttribute("allPrivilege");
		Hashtable rolePrivilege = (Hashtable)request.getAttribute("rolePrivilege");
		if(rolePrivilege == null)
			rolePrivilege = new Hashtable();
		if(allPrivilege != null && allPrivilege.size() > 0){
			TopModule tm = null;
			String tmid = "";
			String tmname = "";
			ArrayList modules = null;
			for(int i = 0; i < allPrivilege.size(); i++){
				tm = (TopModule)allPrivilege.get(i);
				if(tm == null)
					continue;
				tmid = tm.getId();
				tmname = tm.getName();
				modules = tm.getModulesList();
				%>
				<table width="100%" class="listtable">
					<tr>
						<td class = "tdhead" width="100%" colspan="2"><input type="checkbox" name="tmodule" id="<%=tmid %>" onclick="checkModule(this)" value="1"><%=tmname %></td>
					</tr>
				<%if(modules != null && modules.size() > 0){
					Module m = null;
					String mid = "";
					String mname = "";
					String actionClass = "";
					ArrayList operations = null;
					String actionPri = "0";
					String actionClass2 = "0";
					for(int j = 0; j < modules.size(); j++){
						 m = (Module)modules.get(j);
						 mid = m.getId();
						 if(mid == null)
							 mid = "";
						 mname = m.getName();
						 actionClass = m.getActionclass();
						 actionClass2 = actionClass.replaceAll("\\.","#");
						 operations = m.getOperationsList();
						 actionPri = (String)rolePrivilege.get(actionClass+"/"+mid);
						 if(actionPri == null || !actionPri.matches("\\d+")){
							 actionPri = "0";
						 }
						 int intPri = Integer.parseInt(actionPri);
						 %>
					 <tr>
						<td class = "tdhead" width="15%">
						<input type="hidden" name="actionPath" value="<%=actionClass2+"/"+mid %>">
						<input type="checkbox" name="module" id="<%=mid %>" onclick="checkModule(this)" value="1"><%=mname %>
						</td>
						<td class = "tddata">
						<%if(operations != null && operations.size() > 0){
							Operation o = null;
							String oid = "";
							String ocnname = "";
							int privilegeValue = 0;
							String checkedStr = "";
							for(int k = 0; k < operations.size(); k++){
								checkedStr = "";
								o = (Operation)operations.get(k);
								oid = o.getId();
								ocnname = o.getCnname();
								privilegeValue = o.getPrivilegevalue();
								if((privilegeValue&intPri) > 0){
									checkedStr = "checked=\"checked\"";
								}
								%>
									<input type="checkbox" name="<%=actionClass2+"/"+mid %>" id="<%=oid %>" value="<%=privilegeValue %>" <%=checkedStr %>><%=ocnname %>&nbsp;
								<%
							}
						}%>
					
						</td>
					</tr>
						 <%
					}
				%>
					</table>
				<%}
			}
		}
	%>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2">
			<input name="confirm" type="submit" value="确定">
			<input name="backbtn" type="button" value="返回" onclick="javascript:history.back()">
		</td>
	</tr>
</table>
</form>
</body>
</html>