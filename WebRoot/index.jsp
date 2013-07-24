<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="tms.datacenter.index.*,tms.datacenter.dbmanage.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登录主页</title>
<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/common.js"> </script>
</head>
<body class="indexbody">
<%Column column = new Column(); %>
<table width="100%" class="indextable">
	<tr>
		<td height="100%" align="center">
			<jsp:include page="index/top.jsp"/>
				<table width="1024">
				<tr>
					<td width="750" valign="top" align="left"  class="indextd">
						<div id="topdiv1" style="width:100%;">
						<iframe name="frameaddd" src="<%=request.getContextPath()%>/sysmanage/columnShowAction?methodName=show" scrolling="no" frameborder="0" width="100%" height="330px" marginheight="0" marginwidth="0"></iframe>
						<table class="columnbartable">
							<tr>
								<td height="25" width="20" class="columnbar1"></td>
								<td class="columnbar2"><font color="white">最新上传</font></td>
								<td class="columnbar3" width="20"></td>
							</tr>
						</table>
						<jsp:include page="upload/recentInfo.jsp" flush="true"/>
						<table class="columnbartable">
							<tr>
								<td height="25" width="20" class="columnbar1"></td>
								<td class="columnbar2"><font color="white">到期未上传</font></td>
								<td class="columnbar3" width="20"></td>
							</tr>
						</table>
						<jsp:include page="upload/remindInfo.jsp" flush="true"/>
								
							<%
								ArrayList lefts = column.getColumnByPos("left");
								if(lefts != null && lefts.size() > 0){
									Record r = null;
									String cid = "";
									String ctitle = "";
									String clink = "";
									String showtype = "";
									String updatetime = "";
									for(int i = 0; i < lefts.size(); i++){
										r = (Record)lefts.get(i);
										cid = r.get("cid");
										ctitle = r.get("ctitle");
										showtype = r.get("showtype");
										clink = r.get("clink");
										String clink2 = clink;
										updatetime = r.get("updatetime");
										if(updatetime!= null && updatetime.trim().length() > 10)
											updatetime = updatetime.substring(2,10);
										if(clink != null && clink.trim().length() > 0 && !clink.startsWith("http"))
											clink2 = request.getContextPath()+clink;
										if("1".equals(showtype)){
											if(clink != null && clink.trim().length() > 0){
											%>
											<img style="margin-top:5px;" src="<%=request.getContextPath()+"/images/columntitle.jpg" %>"><a href="<%=clink2 %>" title="<%=ctitle %>" target="blank"><%=ctitle %>&nbsp;&nbsp;【<%=updatetime %>】</a><br>
											<%}else{
												%>
											<img style="margin-top:5px;" src="<%=request.getContextPath()+"/images/columntitle.jpg" %>"><%=ctitle %>&nbsp;&nbsp;【<%=updatetime %>】<br>
												<%
											}
										}else if("2".equals(showtype)){
											%>
											<jsp:include page="index/columnshow.jsp" flush="true">
												<jsp:param value="<%=cid %>" name="cid"/>
											</jsp:include>
											<%
										}
									}
								}
							%>
						</div>
					</td>
					<td valign="top" align="left"  class="indextd">
						<div id="topdiv2">
						
							<%
								ArrayList rights = column.getColumnByPos("right");
								if(rights != null && rights.size() > 0){
									Record r = null;
									String cid = "";
									String ctitle = "";
									String clink = "";
									String showtype = "";
									String updatetime = "";
									for(int i = 0; i < rights.size(); i++){
										r = (Record)rights.get(i);
										cid = r.get("cid");
										ctitle = r.get("ctitle");
										showtype = r.get("showtype");
										clink = r.get("clink");
										String clink2 = clink;
										updatetime = r.get("updatetime");
										if(updatetime!= null && updatetime.trim().length() > 10)
											updatetime = updatetime.substring(2,10);
										if(clink != null && clink.trim().length() > 0 && !clink.startsWith("http"))
											clink2 = request.getContextPath()+clink;
										if("1".equals(showtype)){
											if(clink != null && clink.trim().length() > 0){
											%>
											<img style="margin-top:5px;" src="<%=request.getContextPath()+"/images/columntitle.jpg" %>"><a href="<%=clink2 %>" title="<%=ctitle %>" target="blank"><%=ctitle %>&nbsp;&nbsp;【<%=updatetime %>】</a><br>
											<%}else{
												%>
											<img style="margin-top:5px;" src="<%=request.getContextPath()+"/images/columntitle.jpg" %>"><%=ctitle %>&nbsp;&nbsp;【<%=updatetime %>】<br>
												<%
											}
										}else if("2".equals(showtype)){
											%>
											<table class="columnbartable">
											<tr><td height="25" width="20" class="columnbar1"></td>
												<td class="columnbar2"><font color="white"><%=ctitle %></font></td>
												<td class="columnbar3" width="20"></td>
											</tr></table>
											<jsp:include page="<%=clink %>" flush="true">
												<jsp:param value="<%=cid %>" name="cid"/>
											</jsp:include>
											<%
										}
									}
								}
							%>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>