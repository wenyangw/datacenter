package tms.datacenter.sysmanage.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tms.datacenter.commontools.*;
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

public class UserManageAction extends PrivilegeParentAction {
	/**
	 * �����ѯ�ؼ�
	 * 
	 * @return
	 */
	public ArrayList getQueryControl() {
		HttpServletRequest request = this.getRequest();
		ArrayList controls = new ArrayList();
		String loginname = request.getParameter("qloginname");
		loginname = StringToZn.toZn(loginname);
		QueryConditionControl qcc = new QueryConditionControl("qloginname",
				"��¼��", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(loginname);
		controls.add(qcc);

		String username = request.getParameter("qusername");
		username = StringToZn.toZn(username);
		qcc = new QueryConditionControl("qusername", "����",
				QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(username);
		controls.add(qcc);

		String organisation = request.getParameter("qorganisation");
		organisation = StringToZn.toZn(organisation);
		qcc = new QueryConditionControl("qorganisation", "��λ",
				QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_SELECT);
		qcc.setDefaultValue(organisation);

		String department = request.getParameter("qdepartment");
		department = StringToZn.toZn(department);
		QueryConditionControl qcc2 = new QueryConditionControl("qdepartment",
				"����", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_SELECT);
		qcc2.setDefaultValue(department);

		SysParam sp = new SysParam();
		ArrayList params = sp.getAllRecords("datacenter", "", "");
		if (params != null && params.size() > 0) {
			Record r = null;
			for (int i = 0; i < params.size(); i++) {
				r = (Record) params.get(i);
				if (("" + SysParam.PARAM_TYPE_ORG).equalsIgnoreCase(r
						.get("paramtype")))
					qcc.addOptions(r.get("paramcode"), r.get("paramname") + "("
							+ r.get("paramcode") + ")");
				else if (("" + SysParam.PARAM_TYPE_DEPARTMENT)
						.equalsIgnoreCase(r.get("paramtype")))
					qcc2.addOptions(r.get("paramcode"), r.get("paramname")
							+ "(" + r.get("paramcode") + ")");
			}
		}
		controls.add(qcc);
		controls.add(qcc2);
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
		String condition = cc.getControlSQL(this.getLoginUser(), "dc_user");
		String loginname = request.getParameter("qloginname");
		String username = request.getParameter("qusername");
		String origanisation = request.getParameter("qorganisation");
		String department = request.getParameter("qdepartment");
		if (loginname != null && loginname.trim().length() > 0) {
			loginname = StringToZn.toZn(loginname);
			if (condition.trim().length() > 0)
				condition += " and ";
			condition += "loginname like '%" + StringToZn.toDB(loginname)
					+ "%'";
		}
		if (username != null && username.trim().length() > 0) {
			username = StringToZn.toZn(username);
			if (condition.trim().length() > 0)
				condition += " and ";
			condition += "username like '%" + StringToZn.toDB(username) + "%'";
		}
		if (origanisation != null && origanisation.trim().length() > 0) {
			origanisation = StringToZn.toZn(origanisation);
			if (condition.trim().length() > 0)
				condition += " and ";
			condition += "organisation = '" + StringToZn.toDB(origanisation)
					+ "'";
		}
		if (department != null && department.trim().length() > 0) {
			department = StringToZn.toZn(department);
			if (condition.trim().length() > 0)
				condition += " and ";
			condition += "department = '" + StringToZn.toDB(department) + "'";
		}
		UserManage um = new UserManage();
		int totalcount = um
				.getRecordCount("datacenter", condition, "loginname");

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/sysmanage/userManageAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		ArrayList records = um.getPageRecord("datacenter", condition, "",
				"loginname", offset, pager.getSize());

		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());

		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		request.setAttribute("querys", getQueryControl());
		return "userlist";
	}

	public String addPage() {
		SysParam sp = new SysParam();
		ArrayList orgs = sp.getAllRecords("datacenter", "paramtype="
				+ SysParam.PARAM_TYPE_ORG, "");
		ArrayList depts = sp.getAllRecords("datacenter", "paramtype="
				+ SysParam.PARAM_TYPE_DEPARTMENT, "");
		HttpServletRequest request = this.getRequest();
		request.setAttribute("orgs", orgs);
		request.setAttribute("depts", depts);
		RoleManage rm = new RoleManage();
		ArrayList allroles = rm.getAllRecords("datacenter", "", " order by rolename");
		request.setAttribute("allroles", allroles);
		Record r = new Record();
		r = RecordCheck.setRecordFieldDesc("dc_user", r);
		this.getRequest().setAttribute("record", r);
		ContentControl cc = new ContentControl();
		ArrayList allcontrols = cc.getAllControls();
		request.setAttribute("allcontrols", allcontrols);
		UploadPrivilege up = new UploadPrivilege();
		request.setAttribute("allupload", up.getAllUploads());
		// request.setAttribute("userupload", up.getUserUploadsName(loginname));
		return "usermanage";
	}

	public String add() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String curruser = user.get("loginname");
		String loginname = request.getParameter("loginname");
		String loginpsw = request.getParameter("loginpsw");
		String username = request.getParameter("username");
		String gender = request.getParameter("gender");
		String birthday = request.getParameter("birthday");
		String organisation = request.getParameter("organisation");
		String department = request.getParameter("department");
		String inuse = request.getParameter("inuse");
		String memo = request.getParameter("memo");
		// System.out.println(username);
		username = StringToZn.toZn(username);
		memo = StringToZn.toZn(memo);
		// System.out.println(username);
		String operator = curruser;
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());

		Record r = new Record();
		r.set("loginname", loginname, Field.FIELD_TYPE_TEXT, true);
		r.set("loginpsw", loginpsw, Field.FIELD_TYPE_TEXT, false);
		r.set("username", username, Field.FIELD_TYPE_TEXT, false);
		r.set("gender", gender, Field.FIELD_TYPE_TEXT, false);
		r.set("birthday", birthday, Field.FIELD_TYPE_DATE, false);
		r.set("organisation", organisation, Field.FIELD_TYPE_TEXT, false);
		r.set("department", department, Field.FIELD_TYPE_TEXT, false);
		r.set("inuse", inuse, Field.FIELD_TYPE_TEXT, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_user", r, true);
		if (error != null && error.trim().length() > 0)
			return this.operaterError(error);
		if (department != null && department.trim().length() > 0
				&& (organisation == null || organisation.trim().length() <= 0)) {
			return this.operaterError("��Ϊ����ѡ��λ��");
		}
		UserManage um = new UserManage();
		if (um.isExist("datacenter", "dc_user", "loginname",
				Field.FIELD_TYPE_TEXT, loginname))
			return this.operaterError("����ʧ�ܣ���¼���Ѵ��ڣ�");
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			int res = um.insertRecord(conn, "dc_user", r);
			if (res <= 0) {
				conn.rollback();
				return this.operaterError("�����û���Ϣʧ�ܣ�");
			}
			String sql = "delete from dc_role_in_user where loginname='"
					+ loginname + "'";
			int res2 = um.executeUpdate(conn, sql);
			if (res2 < 0) {
				conn.rollback();
				return this.operaterError("ɾ���û�ԭ�н�ɫʧ�ܣ�");
			}
			String[] rolecodes = request.getParameterValues("rolecode");
			if (rolecodes != null && rolecodes.length > 0) {
				String addRoleCode = "";
				Record r_role_in_user = null;
				for (int i = 0; i < rolecodes.length; i++) {
					addRoleCode = rolecodes[i];
					if (addRoleCode != null && addRoleCode.trim().length() > 0) {
						r_role_in_user = new Record();
						r_role_in_user.set("loginname", loginname);
						r_role_in_user.set("rolecode", addRoleCode);
						int res3 = um.insertRecord(conn, "dc_role_in_user",
								r_role_in_user);
						if (res3 != 1) {
							conn.rollback();
							return this.operaterError("��ɫ����ʧ�ܣ�");
						}
					}
				}
			}
			String[] usercontrols = request.getParameterValues("controlcode");
			if (usercontrols != null && usercontrols.length > 0) {
				String addControlCode = "";
				Record r_control_in_user = null;
				for (int i = 0; i < usercontrols.length; i++) {
					addControlCode = usercontrols[i];
					if (addControlCode != null
							&& addControlCode.trim().length() > 0) {
						r_control_in_user = new Record();
						r_control_in_user.set("loginname", loginname);
						r_control_in_user.set("controlcode", addControlCode);
						int res3 = um.insertRecord(conn, "dc_control_in_user",
								r_control_in_user);
						if (res3 != 1) {
							conn.rollback();
							return this.operaterError("���ݿ��Ƹ���ʧ�ܣ�");
						}
					}
				}
			}
			String[] uploadspecialparams = request
					.getParameterValues("uploadspecialparam");
			if (uploadspecialparams != null && uploadspecialparams.length > 0) {
				UploadPrivilege up = new UploadPrivilege();
				if (!up.setPrivilege(conn, loginname, uploadspecialparams)) {
					conn.rollback();
					return this.operaterError("�����ϴ�Ȩ��ʧ�ܣ�");
				}

			}
			conn.commit();

			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/userManageAction");
			Hashtable params = new Hashtable();
			params.put("methodName", "list");
			String moduleid = request.getParameter("moduleid");
			String specialParam = request.getParameter("specialParam");
			if (moduleid == null)
				moduleid = "";
			if (specialParam == null)
				specialParam = "";
			params.put("moduleid", moduleid);
			params.put("specialParam", specialParam);
			this.setReturnParams(params);
			return "success";
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cm.freeConnection("datacenter", conn);
		}

		return this.operaterError("����ʧ��");

	}

	public String updatePage() {
		HttpServletRequest request = this.getRequest();
		String[] loginname_arr = request.getParameterValues("pkfield");
		if (loginname_arr == null || loginname_arr.length > 1)
			return this.operaterError("��ѡ��1����¼���в�����");
		String loginname = loginname_arr[0];
		UserManage um = new UserManage();
		ArrayList records = um.getAllRecords("datacenter", "loginname='"
				+ loginname + "'", "");
		if (records != null && records.size() > 0) {
			Record user = (Record) records.get(0);
			user = RecordCheck.setRecordFieldDesc("dc_user", user);
			this.getRequest().setAttribute("record", user);
		} else
			return this.operaterError("��¼�Ѳ����ڣ�");

		SysParam sp = new SysParam();
		ArrayList orgs = sp.getAllRecords("datacenter", "paramtype="
				+ SysParam.PARAM_TYPE_ORG, "");
		ArrayList depts = sp.getAllRecords("datacenter", "paramtype="
				+ SysParam.PARAM_TYPE_DEPARTMENT, "");
		request.setAttribute("orgs", orgs);
		request.setAttribute("depts", depts);

		RoleManage rm = new RoleManage();
		ArrayList allroles = rm.getAllRecords("datacenter", "", " order by rolename");
		request.setAttribute("allroles", allroles);

		ArrayList userroles = rm.getUserRole("datacenter", loginname);
		request.setAttribute("userroles", userroles);
		ContentControl cc = new ContentControl();
		ArrayList allcontrols = cc.getAllControls();
		ArrayList usercontrols = cc.getUserControlCodes(loginname);
		request.setAttribute("allcontrols", allcontrols);
		request.setAttribute("usercontrols", usercontrols);
		UploadPrivilege up = new UploadPrivilege();
		request.setAttribute("allupload", up.getAllUploads());
		request.setAttribute("userupload", up.getUserUploadsName(loginname));
		return "usermanage";
	}

	public String update() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String curruser = user.get("loginname");
		String loginname = request.getParameter("loginname");
		String loginpsw = request.getParameter("loginpsw");
		String username = request.getParameter("username");
		String gender = request.getParameter("gender");
		String birthday = request.getParameter("birthday");
		String organisation = request.getParameter("organisation");
		String department = request.getParameter("department");
		String inuse = request.getParameter("inuse");
		String memo = request.getParameter("memo");
		username = StringToZn.toZn(username);
		memo = StringToZn.toZn(memo);
		String operator = curruser;
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());

		Record r = new Record();
		r.set("loginname", loginname, Field.FIELD_TYPE_TEXT, true);
		r.set("loginpsw", loginpsw, Field.FIELD_TYPE_TEXT, false);
		r.set("username", username, Field.FIELD_TYPE_TEXT, false);
		r.set("gender", gender, Field.FIELD_TYPE_TEXT, false);
		r.set("birthday", birthday, Field.FIELD_TYPE_DATE, false);
		r.set("organisation", organisation, Field.FIELD_TYPE_TEXT, false);
		r.set("department", department, Field.FIELD_TYPE_TEXT, false);
		r.set("inuse", inuse, Field.FIELD_TYPE_TEXT, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_user", r, true);
		if (error != null && error.trim().length() > 0)
			return this.operaterError(error);
		if (department != null && department.trim().length() > 0
				&& (organisation == null || organisation.trim().length() <= 0)) {
			return this.operaterError("��Ϊ����ѡ��λ��");
		}
		UserManage um = new UserManage();
		if (!um.isExist("datacenter", "dc_user", "loginname",
				Field.FIELD_TYPE_TEXT, loginname))
			return this.operaterError("����ʧ�ܣ���¼���Ѳ����ڣ�");
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			int res = um.updateRecord(conn, "dc_user", r);
			if (res != 1) {
				conn.rollback();
				return this.operaterError("�����û���Ϣʧ�ܣ�");
			}
			String sql = "delete from dc_role_in_user where loginname='"
					+ loginname + "'";
			int res2 = um.executeUpdate(conn, sql);
			if (res2 < 0) {
				conn.rollback();
				return this.operaterError("ɾ���û�ԭ�н�ɫʧ�ܣ�");
			}
			sql = "delete from dc_control_in_user where loginname='"
					+ loginname + "'";
			res2 = um.executeUpdate(conn, sql);
			if (res2 < 0) {
				conn.rollback();
				return this.operaterError("ɾ���û�ԭ�����ݷ��ʿ���ʧ�ܣ�");
			}
			String[] rolecodes = request.getParameterValues("rolecode");
			if (rolecodes != null && rolecodes.length > 0) {
				String addRoleCode = "";
				Record r_role_in_user = null;
				for (int i = 0; i < rolecodes.length; i++) {
					addRoleCode = rolecodes[i];
					if (addRoleCode != null && addRoleCode.trim().length() > 0) {
						r_role_in_user = new Record();
						r_role_in_user.set("loginname", loginname);
						r_role_in_user.set("rolecode", addRoleCode);
						int res3 = um.insertRecord(conn, "dc_role_in_user",
								r_role_in_user);
						if (res3 != 1) {
							conn.rollback();
							return this.operaterError("��ɫ����ʧ�ܣ�");
						}
					}
				}
			}
			String[] usercontrols = request.getParameterValues("controlcode");
			if (usercontrols != null && usercontrols.length > 0) {
				String addControlCode = "";
				Record r_control_in_user = null;
				for (int i = 0; i < usercontrols.length; i++) {
					addControlCode = usercontrols[i];
					if (addControlCode != null
							&& addControlCode.trim().length() > 0) {
						r_control_in_user = new Record();
						r_control_in_user.set("loginname", loginname);
						r_control_in_user.set("controlcode", addControlCode);
						int res3 = um.insertRecord(conn, "dc_control_in_user",
								r_control_in_user);
						if (res3 != 1) {
							conn.rollback();
							return this.operaterError("���ݿ��Ƹ���ʧ�ܣ�");
						}
					}
				}
			}
			String[] uploadspecialparams = request
					.getParameterValues("uploadspecialparam");

			UploadPrivilege up = new UploadPrivilege();
			if (!up.setPrivilege(conn, loginname, uploadspecialparams)) {
				conn.rollback();
				return this.operaterError("�����ϴ�Ȩ��ʧ�ܣ�");
			}

			conn.commit();
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/userManageAction");
			Hashtable params = new Hashtable();
			params.put("methodName", "list");
			String moduleid = request.getParameter("moduleid");
			String specialParam = request.getParameter("specialParam");
			if (moduleid == null)
				moduleid = "";
			if (specialParam == null)
				specialParam = "";
			params.put("moduleid", moduleid);
			params.put("specialParam", specialParam);
			this.setReturnParams(params);
			return "success";
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cm.freeConnection("datacenter", conn);
		}

		return this.operaterError("����ʧ��");
	}

	public String delete() {
		HttpServletRequest request = this.getRequest();
		String[] loginnames = request.getParameterValues("pkfield");
		if (loginnames == null || loginnames.length <= 0) {
			return this.operaterError("������ѡ��һ����¼��");
		}
		String sysuser = "admin";
		for (int i = 0; i < loginnames.length; i++) {
			if (sysuser.equals(loginnames[i]))
				return this.operaterError("admin�û����ܱ�ɾ����");
		}
		UserManage um = new UserManage();
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			int res = um.deleteRecords(conn, "dc_user", "loginname",
					Field.FIELD_TYPE_TEXT, loginnames);
			int res2 = um.deleteRecords(conn, "dc_role_in_user", "loginname",
					Field.FIELD_TYPE_TEXT, loginnames);
			int res3 = um.deleteRecords(conn, "dc_control_in_user",
					"loginname", Field.FIELD_TYPE_TEXT, loginnames);
			if (res > 0 && res2 >= 0 && res3 >= 0) {
				conn.commit();
				this.setReturnAction(request.getContextPath()
						+ "/sysmanage/userManageAction");
				Hashtable params = new Hashtable();
				params.put("methodName", "list");
				String moduleid = request.getParameter("moduleid");
				String specialParam = request.getParameter("specialParam");
				if (moduleid == null)
					moduleid = "";
				if (specialParam == null)
					specialParam = "";
				params.put("moduleid", moduleid);
				params.put("specialParam", specialParam);
				this.setPromptMsg("�ɹ�ɾ��" + res + "����¼");
				this.setReturnParams(params);
				return "success";
			} else {
				conn.rollback();
				return this.operaterError("����ʧ�ܣ�û��ɾ���κμ�¼��");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return this.operaterError("����ʧ��:" + e.getMessage());
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cm.freeConnection("datacenter", conn);
		}
	}

	public void addFiledShowName() {
		UserManage um = new UserManage();
		ArrayList users = um.getAllRecords("datacenter", "", "");
		if (users != null && users.size() > 0) {
			Record r = null;
			for (int i = 0; i < users.size(); i++) {
				r = (Record) users.get(i);
				this.addFieldParameters("operator", r.get("loginname"), r
						.get("username")
						+ "(" + r.get("loginname") + ")");
			}
		}
		SysParam sp = new SysParam();
		ArrayList params = sp.getAllRecords("datacenter", "", "");
		if (params != null && params.size() > 0) {
			Record r = null;
			for (int i = 0; i < params.size(); i++) {
				r = (Record) params.get(i);
				if (("" + SysParam.PARAM_TYPE_ORG).equalsIgnoreCase(r
						.get("paramtype")))
					this.addFieldParameters("organisation", r.get("paramcode"),
							r.get("paramname"));
				else if (("" + SysParam.PARAM_TYPE_DEPARTMENT)
						.equalsIgnoreCase(r.get("paramtype")))
					this.addFieldParameters("department", r.get("paramcode"), r
							.get("paramname"));
			}
		}
		this.addFieldParameters("gender", "1", "��");
		this.addFieldParameters("gender", "2", "Ů");

		this.addFieldParameters("inuse", "1", "��");
		this.addFieldParameters("inuse", "0", "��");

	}

	public Field getDetailPKField() {
		Field field = new Field();
		field.setFieldName("loginname");
		field.setFieldType(Field.FIELD_TYPE_TEXT);
		return field;
	}

	public String getDetailPoolName() {
		return "datacenter";
	}

	public TableManage getDetailClass() {
		UserManage um = new UserManage();
		return um;
	}

}
