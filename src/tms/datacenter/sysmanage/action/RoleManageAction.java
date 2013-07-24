package tms.datacenter.sysmanage.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tms.datacenter.commontools.DateUtil;
import tms.datacenter.commontools.Pager;
import tms.datacenter.commontools.QueryConditionControl;
import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.ConnectionManage;
import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.RecordCheck;
import tms.datacenter.dbmanage.TableManage;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.RoleManage;
import tms.datacenter.sysmanage.SysParam;
import tms.datacenter.sysmanage.UploadPrivilege;
import tms.datacenter.sysmanage.UserManage;

public class RoleManageAction extends PrivilegeParentAction {
	public ArrayList getQueryControl() {
		HttpServletRequest request = this.getRequest();
		ArrayList controls = new ArrayList();
		String rolecode = request.getParameter("qrolecode");
		rolecode = StringToZn.toZn(rolecode);
		QueryConditionControl qcc = new QueryConditionControl("qrolecode",
				"角色编码", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(rolecode);
		controls.add(qcc);
		
		String rolename = request.getParameter("qrolename");
		rolename = StringToZn.toZn(rolename);
		qcc = new QueryConditionControl("qrolename",
				"角色名称", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(rolename);
		controls.add(qcc);
		return controls;
	}
	public String list() {
		HttpServletRequest request = this.getRequest();
		String page = request.getParameter("page");
		if (page == null || !page.matches("\\d+"))
			page = "0";
		int int_page = Integer.parseInt(page);

		Hashtable parames = new Hashtable();
		parames.put("methodName", "list");

		ContentControl cc = new ContentControl();
		String condition = cc.getControlSQL(this.getLoginUser(), "dc_role");
		
		String rolecode = request.getParameter("qrolecode");
		String rolename = request.getParameter("qrolename");
		if(rolecode != null && rolecode.trim().length() > 0){
			rolecode = StringToZn.toZn(rolecode);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "rolecode like '%"+StringToZn.toDB(rolecode)+"%'";
		}
		if(rolename != null && rolename.trim().length() > 0){
			rolename = StringToZn.toZn(rolename);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "rolename like '%"+StringToZn.toDB(rolename)+"%'";
		}
		
		RoleManage um = new RoleManage();
		int totalcount = um.getRecordCount("datacenter", condition, "rolecode");

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/sysmanage/roleManageAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		ArrayList records = um.getPageRecord("datacenter", condition, " order by updatetime desc",
				"rolecode", offset, pager.getSize());

		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		request.setAttribute("querys", getQueryControl());
		return "rolelist";
	}
	
	public String addPage() {
		Record r = new Record();
		r = RecordCheck.setRecordFieldDesc("dc_role",r);
		this.getRequest().setAttribute("record", r);
		return "rolemanage";
	}

	public String add() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String loginName = user.get("loginname");
		String rolecode = request.getParameter("rolecode");
		String roleName = request.getParameter("rolename");
		String memo = request.getParameter("memo");
		roleName = StringToZn.toZn(roleName);
		memo = StringToZn.toZn(memo);
		String operator = loginName;
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());
		RoleManage rm = new RoleManage();

		if (!rm.isExist("datacenter", "dc_role", "rolecode",
				Field.FIELD_TYPE_TEXT, StringToZn.toDB(rolecode))) {
			Record r = new Record();
			r.set("rolecode", rolecode);
			r.set("rolename", roleName);
			r.set("updatetime", updatetime);
			r.set("operator", operator);
			r.set("memo", memo);
			String error = RecordCheck.checkRecord("dc_role", r, true);
			if(error != null && error.trim().length() > 0)
				return this.operaterError(error);
			if (rm.insertRecord("datacenter","dc_role",r) > 0) {
				this.setReturnAction(request.getContextPath()
						+ "/sysmanage/roleManageAction");
				Hashtable params = new Hashtable();
				params.put("methodName", "list");
				String moduleid = request.getParameter("moduleid");
				String specialParam = request.getParameter("specialParam");
				if(moduleid == null)
					moduleid = "";
				if(specialParam == null)
					specialParam = "";
				params.put("moduleid", moduleid);
				params.put("specialParam", specialParam);
				this.setReturnParams(params);
				return "success";
			}
		}

		return this.operaterError("操作失败");
	}

	public String updatePage() {
		HttpServletRequest request = this.getRequest();
		String[] rolecode = request.getParameterValues("pkfield");
		if (rolecode == null || rolecode.length > 1)
			return this.operaterError("请选择1条记录进行操作！");
		String rolecode2 = rolecode[0];
		RoleManage rm = new RoleManage();

		ArrayList records = rm.getAllRecords("datacenter", "rolecode='"
				+ rolecode2 + "'", "");
		if (records != null && records.size() > 0) {
			Record role = (Record) records.get(0);
			role = RecordCheck.setRecordFieldDesc("dc_role",role);
			this.getRequest().setAttribute("record", role);
			request.setAttribute("record", role);
		} else
			return this.operaterError("记录已不存在！");

		return "rolemanage";
	}

	public String update() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String loginName = user.get("loginname");
		String rolecode = request.getParameter("rolecode");
		String roleName = request.getParameter("rolename");
		String memo = request.getParameter("memo");
		roleName = StringToZn.toZn(roleName);
		memo = StringToZn.toZn(memo);
		String operator = loginName;
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());
		RoleManage rm = new RoleManage();
		Record r = new Record();
		r.set("rolecode", rolecode, Field.FIELD_TYPE_TEXT, true);
		r.set("rolename", roleName, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		String error = RecordCheck.checkRecord("dc_role", r, true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		int res = rm.updateRecord("datacenter", "dc_role", r);
		if(res > 0){
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/roleManageAction");
			Hashtable params = new Hashtable();
			params.put("methodName", "list");
			String moduleid = request.getParameter("moduleid");
			String specialParam = request.getParameter("specialParam");
			if(moduleid == null)
				moduleid = "";
			if(specialParam == null)
				specialParam = "";
			params.put("moduleid", moduleid);
			params.put("specialParam", specialParam);
			this.setReturnParams(params);
			return "success";
		}
			
		return this.operaterError("操作失败");
	}
	public String delete(){
		HttpServletRequest request = this.getRequest();
		String[] rolecodes = request.getParameterValues("pkfield");
		if(rolecodes == null || rolecodes.length <= 0){
			return this.operaterError("请至少选择一条记录！");
		}
		RoleManage rm = new RoleManage();
		String inuse = rm.valueInUse("datacenter", "dc_role_in_user", "rolecode",
				Field.FIELD_TYPE_TEXT, rolecodes);
		if(inuse != null && inuse.trim().length() > 0){
			return this.operaterError(inuse);
		}
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			int res = rm.deleteRecords(conn, "dc_role", "rolecode", Field.FIELD_TYPE_TEXT,rolecodes);
			int res2 = rm.deleteRecords(conn, "dc_role_privilege", "rolecode", Field.FIELD_TYPE_TEXT,rolecodes);
			if(res > 0 && res2 >= 0){
				conn.commit();
				this.setReturnAction(request.getContextPath()
						+ "/sysmanage/roleManageAction");
				Hashtable params = new Hashtable();
				params.put("methodName", "list");
				String moduleid = request.getParameter("moduleid");
				String specialParam = request.getParameter("specialParam");
				if(moduleid == null)
					moduleid = "";
				if(specialParam == null)
					specialParam = "";
				params.put("moduleid", moduleid);
				params.put("specialParam", specialParam);
				this.setPromptMsg("成功删除"+res+"条记录");
				this.setReturnParams(params);
				return "success";
			}else{
				conn.rollback();
				return this.operaterError("操作失败，没有删除任何记录！");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return this.operaterError("操作失败:"+e.getMessage());
		}finally{
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cm.freeConnection("datacenter", conn);
		}
	}
	public String updatePrivilegePage(){
		HttpServletRequest request = this.getRequest();
		String rolecode = request.getParameter("pkfield");
		if (rolecode == null || rolecode.trim().length() <= 0)
			return this.operaterError("请选择1条记录进行操作！");
		RoleManage rm = new RoleManage();

		ArrayList records = rm.getAllRecords("datacenter", "rolecode='"
				+ rolecode + "'", "");
		if (records != null && records.size() > 0) {
			Record role = (Record) records.get(0);
			request.setAttribute("role", role);
		} else
			return this.operaterError("记录已不存在！");
		ArrayList allPrivilege = rm.getAllPrivilege();
		Hashtable rolePrivilege = rm.getRolePrivilege("datacenter", rolecode);
		request.setAttribute("allPrivilege", allPrivilege);
		request.setAttribute("rolePrivilege", rolePrivilege);
		
		return "privilege";
	}
	public String updatePrivilege(){
		HttpServletRequest request = this.getRequest();
		String[] actionClasses = request.getParameterValues("actionPath");
		String rolecode = request.getParameter("rolecode");
		if(rolecode == null||rolecode.trim().length() <= 0)
			return this.operaterError("没有获得角色编码！");
		if(actionClasses != null && actionClasses.length > 0){
			ConnectionManage cm = ConnectionManage.getInstance();
			Connection conn = null;
			try {
				conn = cm.getConnection("datacenter");
				conn.setAutoCommit(false);
				String sql = "delete from dc_role_privilege where rolecode='"+rolecode+"'";
				String[] class_module = null;
				String actionClass = "";
				String moduleid = "";
				String[] actionPrivilegeVales = null;
				RoleManage rm = new RoleManage();
				int delcount = rm.executeUpdate(conn, sql);
				if(delcount < 0){
					return this.operaterError("操作失败！");
				}
				Record privilege = null;
				for(int i = 0; i < actionClasses.length; i++){
					actionClass = actionClasses[i];
					class_module = actionClass.split("/");
					if(class_module == null || class_module.length != 2){
						conn.rollback();
						return this.operaterError("权限配置文件错误("+actionClass+")！");
					}
					actionClass = class_module[0];
					moduleid = class_module[1];
					actionPrivilegeVales = request.getParameterValues(actionClass+"/"+moduleid);
					int totalValue = rm.getActionPrivilegeValue(actionPrivilegeVales);
					privilege = new Record();
					privilege.set("rolecode", rolecode, Field.FIELD_TYPE_TEXT, null);
					privilege.set("actionclass", actionClass.replaceAll("#", "."), Field.FIELD_TYPE_TEXT, null);
					privilege.set("moduleid", moduleid, Field.FIELD_TYPE_TEXT, null);
					privilege.set("privilegevalue", ""+totalValue, Field.FIELD_TYPE_INT, null);
					int res = rm.insertRecord(conn, "dc_role_privilege", privilege);
					if(res <= 0){
						conn.rollback();
						return this.operaterError("操作失败！");
					}
				}
				conn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}finally{
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cm.freeConnection("datacenter", conn);
			}
			
		}
		this.setReturnAction(request.getContextPath()
				+ "/sysmanage/roleManageAction");
		Hashtable params = new Hashtable();
		params.put("methodName", "list");
		String moduleid = request.getParameter("moduleid");
		String specialParam = request.getParameter("specialParam");
		if(moduleid == null)
			moduleid = "";
		if(specialParam == null)
			specialParam = "";
		params.put("moduleid", moduleid);
		params.put("specialParam", specialParam);
		this.setReturnParams(params);
		return "success";
	}
	public void addFiledShowName(){
		UserManage um = new UserManage();
		ArrayList users = um.getAllRecords("datacenter", "", "");
		if(users != null && users.size() > 0){
			Record r = null;
			for(int i = 0; i < users.size(); i++){
				r = (Record)users.get(i);
				this.addFieldParameters("operator", r.get("loginname"), r.get("username")+"("+r.get("loginname")+")");
			}
		}
	}
	public Field getDetailPKField(){
		Field field = new Field();
		field.setFieldName("rolecode");
		field.setFieldType(Field.FIELD_TYPE_TEXT);
		return field;
	}
	public String getDetailPoolName(){
		return "datacenter";
	}
	public TableManage getDetailClass(){
		RoleManage rm = new RoleManage();
		return rm;
	}
}
