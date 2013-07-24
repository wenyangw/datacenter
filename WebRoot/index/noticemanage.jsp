<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.dbmanage.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/editor/themes/default/default.css" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/editor/plugins/code/prettify.css" />
	<script charset="utf-8" src="<%=request.getContextPath() %>/editor/kindeditor.js"></script>
	<script charset="utf-8" src="<%=request.getContextPath() %>/editor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="<%=request.getContextPath() %>/editor/plugins/code/prettify.js"></script>
	<script>
		KindEditor.ready(function(K) {
			window.editor1 = K.create('textarea[name="content"]', {
				cssPath : '<%=request.getContextPath() %>/editor/plugins/code/prettify.css',
				uploadJson : '<%=request.getContextPath() %>/editor/upload_json.jsp',
				fileManagerJson : '<%=request.getContextPath() %>/editor/file_manager_json.jsp',
				allowFileManager : true
			});
			prettyPrint();
		});
		function submitForm(){
			window.editor1.sync();
			document.forms['form1'].submit();
		}
	</script>
	<script type="text/javascript">
function selectFile(){
var obj = new Object();
obj.name="请选择文件";
var res = window.showModalDialog("<%=request.getContextPath() %>/index/upload.jsp",obj,"dialogWidth:500px;dialogHeight:500px;resizable:yes");
var pdffile = document.getElementById("pdffile");
if(res&&res!=''){
	pdffile.value = res;
}

}
</script>
</head>
<body>
<%
String noticeid = "";
String title = "";
String content = "";
String inuse = "";
String memo = "";
String publisher = "";
String pdffile = "";
Record notice = (Record)request.getAttribute("record");
if(notice != null)
{
	noticeid = notice.get("noticeid");
	title = notice.get("title")==null?"":notice.get("title");
	content = notice.get("content")==null?"":notice.get("content");
	inuse = notice.get("inuse")==null?"":notice.get("inuse");
	memo = notice.get("memo")==null?"":notice.get("memo");
	publisher = notice.get("publisher")==null?"":notice.get("publisher");
	pdffile =notice.get("pdffile")==null?"":notice.get("pdffile");
}
%>
<form name="form1" action="<%=request.getContextPath() %>/sysmanage/noticeManageAction" method="post">
<%if(noticeid != null && noticeid.trim().length() > 0) {%>
				<input type="hidden" name="methodName" id="methodNameId" value="update">
			<%}else{
				%>
				<input type="hidden" name="methodName" id="methodNameId" value="add">
				<%
  }%>
<input type="hidden" name="specialparam" id="specialparamId" value="">
<input type="hidden" name="moduleid" id="moduleidId" value="m01m07">
<table class="listtable" width="100%">
	
			
			<%if(noticeid != null && noticeid.trim().length() > 0) {%>
			<tr>
		<td class="tdhead" align="right" width="20%">栏目编号</td>
		<td class="data1" align="left">
				<input type="text" class="input300red" readonly="readonly" name="noticeid" value="<%=noticeid==null?"":noticeid %>">
				</td>
	</tr>
			<%}%>
			
		
		
	<tr>
		<td class="tdhead" align="right">标题</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="title" value="<%=title==null?"":title %>">
			<%=notice.getDataDesc("title") %>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">发布人</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="publisher" value="<%=publisher==null?"":publisher %>">
			<%=notice.getDataDesc("publisher") %>
		</td>
	</tr>
	<tr>
		<td  class="tdhead" align="right">PDF形式</td>
		<td class="data1" align="left">
			<input type="text" class="input300red" id="pdffile" readonly="readonly" name="pdffile" value="<%=pdffile==null?"":pdffile %>"/>
			<input type="button" value="选择文件" onclick="selectFile()"/>
			<%=notice.getDataDesc("pdffile") %>
		</td>
	</tr>
	<tr>
		<td  class="tdhead" align="right">内容</td>
		<td class="data1" align="left">
			<textarea  style="width:100%;height:400px;" name="content"><%=htmlspecialchars(content)%></textarea>
			<%=notice.getDataDesc("content")%>
		</td>
	</tr>
	<tr>
		<td class="tdhead" align="right">是否有效</td>
		<td class="data1" align="left">
			<select name="inuse" class="select300" >
				<option value=""></option>
				<%
					
					Record r = null;
					String selected="";
					for(int i = 1; i >=0; i--){
						if((""+i).equals(inuse))
							selected = "selected";
						else
							selected = "";
							%>
							<option value="<%=i%>" <%=selected %>><%=i==1?"是":"否"%></option>
							<%
					}
					
				%>
				
			</select>
		</td>
	</tr>
	
	
	<tr>
		<td class="tdhead" align="right">备注</td>
		<td class="data1" align="left">
			<input type="text" class="input300" name="memo" value="<%=memo==null?"":memo %>">
			<%=notice.getDataDesc("memo") %>
		</td>
	</tr>
	<tr>
		<td class="data1" align="center" colspan="2">
			<input type="button" value="确定" onclick="submitForm();">
			<input type="button" value="返回" onclick="javascript:history.back()">
		</td>
	</tr>
</table>
</form>
</body>
</html>
<%!
private String htmlspecialchars(String str) {
	str = str.replaceAll("&", "&amp;");
	str = str.replaceAll("<", "&lt;");
	str = str.replaceAll(">", "&gt;");
	str = str.replaceAll("\"", "&quot;");
	return str;
}
%>