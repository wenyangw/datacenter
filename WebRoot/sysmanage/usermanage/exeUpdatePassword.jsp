<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="java.util.*,tms.datacenter.commontools.*,tms.datacenter.dbmanage.*,tms.datacenter.sysmanage.UserManage"%>
<%
String oldpassword = request.getParameter("oldpassword");
String newpassword = request.getParameter("newpassword");
String confirmpassword = request.getParameter("confirmpassword");
Record dcuser = (Record)session.getAttribute("dcuser");
response.setContentType("text/html;charset=GBK");
try{
	if(dcuser == null){
		response.getWriter().write("��û�е�¼������ִ�д˲���");
		return;
	}
	if(newpassword==null || newpassword.trim().length() <= 0){
		response.getWriter().write("�����벻��Ϊ�գ�");
		return;
	}
	if(!newpassword.equals(confirmpassword)){
		response.getWriter().write("��������������ȷ�����벻һ�£�");
		return;
	}
	String loginname = dcuser.get("loginname");
	String sql = "select loginpsw from dc_user where loginname='"+StringToZn.toDB(loginname)+"'";
	UserManage um = new UserManage();
	ArrayList al = um.executeQuery("datacenter",sql);
	if(al == null || al.size() <= 0){
		response.getWriter().write("��ǰ�û�����");
		return;
	}
	Record r = (Record)al.get(0);
	String oldpsw = r.get("loginpsw");
	
	if(!oldpsw.equals(oldpassword)){
		response.getWriter().write("������ľ��������");
		return;
	}
	sql = "update dc_user set loginpsw = '"+StringToZn.toDB(newpassword)+"' where loginname='"+StringToZn.toDB(loginname)+"'";
	um.executeUpdate("datacenter",sql);
	int i = um.executeUpdate("datacenter",sql);
	if(i > 0){
		session.setAttribute("dcuser",dcuser);
		response.getWriter().write("s");
		return;
	}else{
		response.getWriter().write("�޸�ʧ�ܣ�");
		return;
	}
}
catch(Exception e){
	e.printStackTrace();
}
%>