<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	import="tms.datacenter.dbmanage.*,java.util.*,tms.datacenter.sysmanage.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=request.getContextPath() %>/js/common.js"> </script>
</head>
<body>
<%
String controlcode = "";
String tablename = "";
String controldesc = "";
String conditionsql = "";
String memo = "";
Record control = (Record)request.getAttribute("record");
if(control != null)
{
	controlcode = control.get("controlcode");
	tablename = control.get("tablename")==null?"":control.get("tablename");
	controldesc = control.get("controldesc")==null?"":control.get("controldesc");
	conditionsql = control.get("conditionsql")==null?"":control.get("conditionsql");
	memo = control.get("memo");
}

ArrayList tablelist = (ArrayList)request.getAttribute("tablelist");

%>
<form name="form1"
	action="<%=request.getContextPath() %>/sysmanage/contentControlAction"
	method="post">
<%if(controlcode != null && controlcode.trim().length() > 0) {%> <input
	type="hidden" name="methodName" id="methodNameId" value="update">
<%}else{
				%> <input type="hidden" name="methodName" id="methodNameId"
	value="add"> <%
  }%> <input type="hidden" name="specialparam" id="specialparamId"
	value="">
	<input type="hidden" name="moduleid" id="moduleidId" value="m01m04">
<table class="listtable" width="100%">
	<tr>
		<td class="tdhead" align="right" width="20%">控制编码</td>
		<td class="data1" align="left">
		<%if(controlcode != null && controlcode.trim().length() > 0) {%> <input
			type="text" class="input300red" readonly="readonly"
			name="controlcode" value="<%=controlcode==null?"":controlcode %>">
		<%}else{
				%> <input type="text" class="input300" name="controlcode"
			value="<%=controlcode==null?"":controlcode %>"> <%
			}%> <%=control.getDataDesc("controlcode") %></td>
	</tr>
	<tr>
		<td class="tdhead" align="right">表名称</td>
		<td class="data1" align="left"><select name="tablename"
			class="select300" id="tablenameid" onchange="getFields()">
			<option value=""></option>
			<%
					TableDesc r = null;
					if(tablelist != null && tablelist.size() > 0){
						for(int i = 0; i < tablelist.size(); i++){
							r = (TableDesc)tablelist.get(i);
							String selected = "";
							if(tablename.equals(r.getName()))
								selected = "selected";
							%>
			<option value="<%=r.getName() %>" <%=selected %>><%=r.getCnname()+"("+r.getName()+")"%></option>
			<%
						}
					}
				%>

		</select></td>
	</tr>
	<tr>
		<td class="tdhead" align="right">条件描述</td>
		<td class="data1" align="left"><input type="text"
			class="input300" name="controldesc"
			value="<%=controldesc==null?"":controldesc %>"> <%=control.getDataDesc("controldesc") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">备注</td>
		<td class="data1" align="left"><input type="text"
			class="input300" name="memo" value="<%=memo==null?"":memo %>">
		<%=control.getDataDesc("memo") %></td>
	</tr>
	<tr>
		<td class="data1" align="left" colspan="2">
		<table width="100%">
			<tr>
				<td width="100%">列名<select name="fname" id="fnameid">
					<option value=""></option>
				</select> 操作<select name="oname" id="onameid">
					<option value="1">等于</option>
					<option value="2">大于</option>
					<option value="3">小于</option>
					<option value="4">大等于</option>
					<option value="5">小等于</option>
					<option value="10">不等于</option>
					<option value="6">前匹配</option>
					<option value="7">后匹配</option>
					<option value="8">不限匹配</option>
					<option value="9">包含</option>
				</select><br>
				<textArea style="width: 100%; height: 70px;" id="vnameid"
					name="vname"></textArea><br>
				系统参数类型<select name="paramtype" id="paramtypeid">
					<option value=""></option>
					<%
								ArrayList paramtypes  = (ArrayList)request.getAttribute("paramtypes");
								Record rp = null;
								if(paramtypes != null && paramtypes.size() > 0){
									for(int i = 0; i < paramtypes.size(); i++){
										rp = (Record)paramtypes.get(i);
										%>
					<option value="<%=rp.get("type") %>"><%=rp.get("name")%></option>
					<%
									}
								}
							%>

				</select> <input type="button" name="selectParam" value="从系统参数选值"
					onclick="selectFromPram();"> <input type="button"
					name="gensql" value="添加条件SQL" onclick="generateSql();">
					系统内置参数：
					<span onclick="addSysParam('/loginname/')" style="cursor:pointer"><font color="blue">登录用户名</font></span>，
					<span onclick="addSysParam('/organisation/')" style="cursor:pointer"><font color="blue">登录用户单位</font></span>，
					<span onclick="addSysParam('/department/')" style="cursor:pointer"><font color="blue">登录用户部门</font></span>
					</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">条件SQL语句</td>
		<td class="data1" align="left"><textArea class="areaformbig"
			name="conditionsql" id="conditionsqlid"><%=conditionsql==null?"":conditionsql %></textArea>
		<BR><input type="button" value="SQL校验" onclick="checkSql();">
		<%=control.getDataDesc("conditionsql") %></td>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2"><input type="submit"
			value="确定"> <input type="button" value="返回"
			onclick="javascript:history.back()"></td>
	</tr>
</table>
</form>
<script language="javascript">
var a = new Array();
function getTableField(){
	<%
	if(tablelist != null && tablelist.size() > 0){
		ArrayList fieldlist = null;
		String table = "";
		String cnname = "";
		String allfields = "";
		Field field = null;
		String fieldname = "";
		String fieldtype = "";
		String fieldlabel = "";
		String typename = "";
		int k = 0;
		for(int i = 0; i < tablelist.size(); i++){
			r = (TableDesc)tablelist.get(i);
			if(r != null){
				table = r.getName();
				cnname = r.getCnname();
				fieldlist = r.getFieldList();
				if(fieldlist != null && fieldlist.size() > 0){
					for(int j = 0; j < fieldlist.size(); j++){
						field = (Field)fieldlist.get(j);
						if(field != null){
							typename = "字符";
							fieldname = field.getFieldName();
							fieldtype = field.getFieldType();
							fieldlabel = field.getFieldLabel();
							if(Field.FIELD_TYPE_INT.equals(fieldtype)||Field.FIELD_TYPE_DOUBLE.equals(fieldtype))
								typename = "数值";
							else if(Field.FIELD_TYPE_DATE.equals(fieldtype))
								typename = "日期";
							%>
		a[<%=k%>] = new field('<%=table%>','<%=fieldname%>','<%=fieldlabel%>','<%=fieldtype%>','<%=typename%>');
							<%
							k++;
						}
					}
				}
			}
		}
	}
	%>

}
function field(tablename,fieldname,fieldlabel,fieldtype,typename){
	this.tablename = tablename;
	this.fieldname = fieldname;
	this.fieldlabel = fieldlabel;
	this.fieldtype = fieldtype;
	this.typename = typename;
	return this;
}
function getFields(){
	var tableObj = document.getElementById("tablenameid");
	var fieldObj = document.getElementById("fnameid");
	var table = tableObj.options[tableObj.options.selectedIndex].value;
	
	var fields = fieldObj.options;
	if(fields && fields.length > 0){
		fieldObj.options.length=0;
	}
	var fieldnew = null; 
	var fieldOption = null;
	
	if(a && a.length > 0){
		for(i=0;i <a.length;i++){
			fieldnew = a[i];
			if(fieldnew && fieldnew.tablename==table){
				fieldOption = new Option(fieldnew.fieldlabel+"("+fieldnew.typename+")", fieldnew.fieldname); 
				fieldObj.options.add(fieldOption);
			}     
		}
	}
}
getTableField();
getFields();
function selectFromPram(){
	var obj = new Object();
	obj.name="请选择参数";
	var tableObj = document.getElementById("tablenameid");
	var fieldObj = document.getElementById("fnameid");
	if(tableObj==null||tableObj.value==''){
		alert("请选择表！");
		return;
	}
	if(!fieldObj==null||fieldObj.value==''){
		alert("请选择表中的列！");
		return;
	}
	var fieldtype=getFieldType(tableObj.value,fieldObj.value);
	var paramtype = document.getElementById("paramtypeid");
	var oname = document.getElementById("onameid");
	var typevalue = paramtype.options[paramtype.options.selectedIndex].value;
	var onamevalue = oname.options[oname.options.selectedIndex].value;
	if(onamevalue == '')
	{
		alert("请选择操作符！");
		return;
	}
	var selectedvalue = window.showModalDialog("<%=request.getContextPath() %>/sysmanage/sysParamAction?methodName=listfordialog&qparamtype="+typevalue+"&operate="+onamevalue+"&fieldtype="+fieldtype,obj,"dialogWidth:500px;dialogHeight:500px;resizable:yes");
	var sqlcontrol = document.getElementById("vnameid");
	if(selectedvalue&&selectedvalue!='')
		sqlcontrol.value = selectedvalue;
}
function getFieldType(tablename,fieldname){
	var fieldnew = null; 
	if(a && a.length > 0){
		for(i=0;i <a.length;i++){
			fieldnew = a[i];
			if(fieldnew && fieldnew.tablename==tablename&&fieldnew.fieldname==fieldname){
				return fieldnew.fieldtype;
			}     
		}
	}
	return '';
}
var req;
function createXMLHttpRequest(){ 
	if(window.XMLHttpRequest){
		req = new XMLHttpRequest();
	}
	else if(window.ActiveXObject){   
	  try{   
		 req   =   new   ActiveXObject("Msxml2.XMLHTTP");   
	  }catch(e){   
	  	 try{   
		  req   =   new   ActiveXObject("Microsoft.XMLHTTP");   
	  	 }catch(e){}   
	  }   
	}   
}  
function checkSql(){
	var tablename = document.getElementById("tablenameid");
	var conditonsql = document.getElementById("conditionsqlid"); 
	var table = tablename.options[tablename.options.selectedIndex].value;
	var sql = conditonsql.value;
	if(table == ''||sql==''){
		alert("表名和条件语句不能为空！");
		return;
	}
	createXMLHttpRequest();
	var url = "<%=request.getContextPath()%>/sysmanage/contentControlAction"; 
	req.open("POST",url,true);   
	req.onreadystatechange   =   checkRes;//指定响应函数   
	req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var param = "methodName=checkSQL&tablename="+table+"&"+"sql="+conditonsql.value.replace(eval("/&/gi"),"&amp;");
	req.send(param);//发送请求   
}
function checkRes(){
	if(req.readyState   ==   4){   //   判断对象状态   
		if(req.status   ==   200){   //   信息已经成功返回，开始处理信息   
		  	if(req.responseText=='')
			  	alert("语句正确！");
		  	else
		  		alert(req.responseText);
		}else{   //页面不正常   
		    
		}   
	}		
}
function addSysParam(param){
	if(param && param != ''){
		var sqlcontrol = document.getElementById("vnameid");
		sqlcontrol.value = "'"+param+"'";
	}
}
</script>
</body>
</html>