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
<form name="listform" id="listformid" action="<%=request.getContextPath() %>/sysmanage/userManageAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="list">
<input type="hidden" name="specialParam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="m01m03">
<div class="querydiv">
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
	Record ru = (Record)session.getAttribute("dcuser");
	String curruser = ru.get("loginname");
	boolean showadmin = false;
	if("admin".equals(curruser))
		showadmin = true;
%>
</div>
<div class="listdiv">
<table width="100%" class="listtable">
	<tr>
		<td class="tdhead" align="center">全选<input type="checkbox" name="selectAll" value="1" onclick="checkAll(this,'pkfield')"></td>
		<td class="tdhead" align="center">登录名</td>
		<td class="tdhead" align="center">姓名</td>
		<td class="tdhead" align="center">性别</td>
		<td class="tdhead" align="center">单位</td>
		<td class="tdhead" align="center">部门</td>
		<td class="tdhead" align="center">是否可用</td>
	</tr>
	<%
		Hashtable fieldLabels = (Hashtable)request.getAttribute("fieldslabels");
		ArrayList records = (ArrayList)request.getAttribute("records");
		if(records != null && records.size() > 0){
			Record r = null;
			String loginname = "";
			String username = "";
			String gender = "";
			String organisation = "";
			String department = "";
			String inuse = "";
			String datatdclass = "";
			String disabled = "";
			for(int i = 0; i < records.size(); i++){
				disabled = "";
				r = (Record)records.get(i);
				loginname = r.get("loginname");
				username = r.get("username");
				gender = r.get("gender");
				organisation = r.get("organisation");
				department = r.get("department");
				inuse = r.get("inuse");
				if(i%2 == 0){
					datatdclass = "data1";
				}else{
					datatdclass = "data2";
				}
				if(!showadmin && loginname.equals("admin"))
					disabled = "disabled=\"disabled\"";
				%>
				<tr>
				<td class="<%=datatdclass %>" height="20" align="center"><input type="checkbox" name="pkfield" value="<%=loginname %>" <%=disabled %>></td>
				<td class="<%=datatdclass %>" align="center"><%=loginname %></td>
				<td class="<%=datatdclass %>" align="center"><%=username %></td>
				<td class="<%=datatdclass %>" align="center"><%=Record.getShowValue(fieldLabels,"gender",gender) %></td>
				<td class="<%=datatdclass %>" align="center"><%=Record.getShowValue(fieldLabels,"organisation",organisation) %></td>
				<td class="<%=datatdclass %>" align="center"><%=Record.getShowValue(fieldLabels,"department",department) %></td>
				<td class="<%=datatdclass %>" align="center"><%=Record.getShowValue(fieldLabels,"inuse",inuse) %></td>
				</tr>
				<%
			}
		}
	%>
</table>
</div>
<div style="width:100%">
		<div class="pagerdiv">&nbsp;
			<%=request.getAttribute("pager")==null?"":(String)request.getAttribute("pager") %>
		</div>
		<div class="buttondiv">
			<%
				ArrayList uo = (ArrayList)request.getAttribute("uo");
				out.println(RoleManage.paraUserOperationToButton(uo,Operation.SHOW_POS_LIST));
			%>
		</div>
</div>
</form>
</body>
</html>