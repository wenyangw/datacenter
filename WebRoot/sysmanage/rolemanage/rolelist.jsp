<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.util.*,tms.datacenter.sysmanage.*"%>
<%@ page import="java.util.ArrayList,tms.datacenter.dbmanage.*"%>
<%@ page import="tms.datacenter.commontools.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/common.js"> </script>
<script src="<%=request.getContextPath() %>/js/Calendar1.js" type="text/javascript"></script>
<script language="javascript">
</script>

</head>
<body>
<form name="listform" id="listformid" action="<%=request.getContextPath() %>/sysmanage/roleManageAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="list">
<input type="hidden" name="specialParam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="m01m01">
<%
	ArrayList querys = (ArrayList)request.getAttribute("querys");
	if(querys != null && querys.size() > 0){
		QueryConditionControl qcc = null;
		for(int i = 0; i < querys.size(); i++){
			qcc = (QueryConditionControl)querys.get(i);
			if(qcc != null)
				out.println(qcc.getControlStr());
		}
	}
	out.println("<input type=\"button\" value=\"查询\" onclick=\"commonOperateSimple('list')\">");
%>
<div class="listdiv">
<table width="100%" class="listtable">
	<tr>
		<td class="tdhead" align="center" height="25">全选<input type="checkbox" name="selectAll" value="1" onclick="checkAll(this,'pkfield')"></td>
		<td class="tdhead" align="center">角色编码</td>
		<td class="tdhead" align="center">角色名称</td>
		<td class="tdhead" align="center">操作人</td>
		<td class="tdhead" align="center">权限维护</td>
		<td class="tdhead" align="center">更新时间</td>
	</tr>
	<%
		Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
		ArrayList records = (ArrayList)request.getAttribute("records");
		if(records != null && records.size() > 0){
			Record r = null;
			String rolecode = "";
			String rolename = "";
			String updatetime = "";
			String operator = "";
			String memo = "";
			String plink = "";
			String datatdclass = "";
			for(int i = 0; i < records.size(); i++){
				r = (Record)records.get(i);
				rolecode = r.get("rolecode");
				rolename = r.get("rolename");
				updatetime = r.get("updatetime");
				operator = r.get("operator");
				memo = r.get("memo");
				plink = request.getContextPath()+"/sysmanage/roleManageAction?methodName=updatePrivilegePage&pkfield="+rolecode;
				if(i%2 == 0){
					datatdclass = "data1";
				}else{
					datatdclass = "data2";
				}
				%>
				<tr>
				<td class="<%=datatdclass %>" height="20" align="center"><input type="checkbox" name="pkfield" value="<%=rolecode %>"></td>
				<td class="<%=datatdclass %>" align="center"><%=rolecode %></td>
				<td class="<%=datatdclass %>" align="center"><%=rolename %></td>
				<td class="<%=datatdclass %>" align="center"><%=Record.getShowValue(fieldLabels,"operator",operator) %></td>
				<td class="<%=datatdclass %>" align="center"><a href="<%=plink %>">权限维护</a></td>
				<td class="<%=datatdclass %>" align="center"><%=updatetime %></td>
				</tr>
				<%
			}
		}
	%>
</table>
</div>
<%=request.getAttribute("pager")==null?"":(String)request.getAttribute("pager") %>
<hr>
<table class="bottomtable" width="100%">
	<tr>
		<td  align="right">
			<%
				ArrayList uo = (ArrayList)request.getAttribute("uo");
				out.println(RoleManage.paraUserOperationToButton(uo,Operation.SHOW_POS_LIST));
			%>
		</td>
	</tr>
</table>
</form>
</body>
</html>