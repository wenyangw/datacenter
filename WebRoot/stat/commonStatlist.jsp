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
<script language="javascript">
</script>

</head>
<body>
<form name="listform" id="listformid" action="<%=request.getContextPath() %>/stat/commonStatAction" method="post">
<input type="hidden" name="methodName" id="methodNameId" value="stat">
<input type="hidden" name="specialParam" id="specialparamId" value="<%=request.getAttribute("specialparam")==null?"":(String)request.getAttribute("specialparam") %>">
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
%>
</div>
<div class="listdiv">
<table width="100%" class="listtable">
	
	<%
		ArrayList records = (ArrayList)request.getAttribute("records");
		if(records != null && records.size() > 0){
			Record r = null;
			String fieldname = "";
			String fieldvalue = "";
			ArrayList fields = null;
			Field field = null;
			String datatdclass = "";
			
			r = (Record)records.get(0);
			fields = r.getFieldslist();
			out.println("<tr>");
			if(fields != null && fields.size() > 0){
				for(int j = 0;j < fields.size(); j++){
					field = (Field)fields.get(j);
					fieldname = field.getFieldName();
					fieldvalue = field.getFieldValue();
					out.println("<td class=\"tdhead\" align=\"center\">"+fieldname+"</td>");
				}
			}
			out.println("</tr>");
			
			for(int i = 0; i < records.size(); i++){
				r = (Record)records.get(i);
				fields = r.getFieldslist();
				if(i%2 == 0){
					datatdclass = "data1";
				}else{
					datatdclass = "data2";
				}
				out.println("<tr>");
				if(fields != null && fields.size() > 0){
					for(int j = 0;j < fields.size(); j++){
						field = (Field)fields.get(j);
						fieldname = field.getFieldName();
						fieldvalue = field.getFieldValue();
						out.println("<td class=\""+datatdclass+"\" align=\"center\">"+fieldvalue+"</td>");
					}
				}
				out.println("</tr>");
			}
		}
	%>
</table>
</div>
<div style="width:100%">
		<div class="pagerdiv">&nbsp;
			
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