package tms.datacenter.sysmanage.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.ConnectionManage;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.sysmanage.RoleManage;
import tms.datacenter.sysmanage.UserManage;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport{
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest (); 
		String loginName=StringToZn.toZn(request.getParameter("loginName"));
		String loginPsw=request.getParameter("loginPsw");
		Record user = checkUser(loginName,loginPsw);
		if(user != null){
			HttpSession session = request.getSession(true);
			session.setAttribute("dcuser", user);
			return "loginSuccess";
		}
		request.setAttribute("errorMsg","用户名或密码错误，请重新登录！");
		return "login";
    }
	private Record checkUser(String loginName,String loginPsw){
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		UserManage um = new UserManage();
		String condition = "loginName='"+loginName+"' and loginPsw='"+loginPsw+"' and inuse='1'";
		ArrayList al = um.getAllRecords(conn, condition, null);
		cm.freeConnection("datacenter", conn);
		if(al != null && al.size() > 0){
			Record r = (Record)al.get(0);
			RoleManage role = new RoleManage();
			Hashtable userPrivilege = role.getUserPrivilege(conn, loginName);
			r.setUserPrivilege(userPrivilege);
			return r;
		}
		return null;
	}
}
