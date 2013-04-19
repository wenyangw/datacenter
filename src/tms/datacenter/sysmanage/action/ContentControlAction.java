package tms.datacenter.sysmanage.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tms.datacenter.commontools.DateUtil;
import tms.datacenter.commontools.Pager;
import tms.datacenter.commontools.QueryConditionControl;
import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.ConnectionManage;
import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.RecordCheck;
import tms.datacenter.dbmanage.TableConfig;
import tms.datacenter.dbmanage.TableDesc;
import tms.datacenter.dbmanage.TableManage;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.SysParam;
import tms.datacenter.sysmanage.UserManage;

public class ContentControlAction extends PrivilegeParentAction {
	public ArrayList getQueryControl() {
		HttpServletRequest request = this.getRequest();
		ArrayList controls = new ArrayList();
		String controlcode = request.getParameter("qcontrolcode");
		controlcode = StringToZn.toZn(controlcode);
		QueryConditionControl qcc = new QueryConditionControl("qcontrolcode",
				"控制编码", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(controlcode);
		controls.add(qcc);

		String tablename = request.getParameter("qtablename");
		tablename = StringToZn.toZn(tablename);
		qcc = new QueryConditionControl("qtablename", "表名称",
				QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_SELECT);
		qcc.setDefaultValue(tablename);
		TableConfig tc = TableConfig.getInstance();
		ArrayList tablelist = tc.getTableslist();
		if (tablelist != null && tablelist.size() > 0) {
			TableDesc td = null;
			for (int i = 0; i < tablelist.size(); i++) {
				td = (TableDesc) tablelist.get(i);
				if (td != null) {
					qcc.addOptions(td.getName(), td.getCnname());
				}
			}
		}
		controls.add(qcc);

		String controldesc = request.getParameter("qcontroldesc");
		controldesc = StringToZn.toZn(controldesc);
		qcc = new QueryConditionControl("qcontroldesc", "条件描述",
				QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(controldesc);
		controls.add(qcc);

		return controls;
	}

	public String checkSQL() {
		HttpServletRequest request = this.getRequest();
		HttpServletResponse response = this.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		String conditionsql = request.getParameter("sql");
		String tablename = request.getParameter("tablename");
		conditionsql = StringToZn.toZn(conditionsql);

		String sql = "select top 1 * from " + tablename + " where "
				+ conditionsql;
		String errormsg = ContentControl.checkSelectSql("datacenter", sql);

		try {
			response.getWriter().write(errormsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String list() {
		HttpServletRequest request = this.getRequest();
		String page = request.getParameter("page");
		if (page == null || !page.matches("\\d+"))
			page = "0";
		int int_page = Integer.parseInt(page);

		ContentControl c = new ContentControl();
		String condition = c.getControlSQL(this.getLoginUser(), "dc_content_control");
		String controlcode = request.getParameter("qcontrolcode");
		String tablename = request.getParameter("qtablename");
		String controldesc = request.getParameter("qcontroldesc");
		if (controlcode != null && controlcode.trim().length() > 0) {
			controlcode = StringToZn.toZn(controlcode);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "controlcode like '%" + StringToZn.toDB(controlcode)
					+ "%'";
		}
		if (tablename != null && tablename.trim().length() > 0) {
			tablename = StringToZn.toZn(tablename);
			if (condition.trim().length() > 0)
				condition += " and ";
			condition += "tablename = '" + StringToZn.toDB(tablename) + "'";
		}
		if (controldesc != null && controldesc.trim().length() > 0) {
			controldesc = StringToZn.toZn(controldesc);
			condition += "controldesc like '%" + StringToZn.toDB(controldesc)
					+ "%'";
		}
		ContentControl cc = new ContentControl();
		int totalcount = cc.getRecordCount("datacenter", condition,
				"controlcode");

		Hashtable parames = new Hashtable();
		parames.put("methodName", "list");
		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/sysmanage/contentControlAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		ArrayList records = cc.getPageRecord("datacenter", condition, "",
				"controlcode", offset, pager.getSize());

		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		request.setAttribute("querys", getQueryControl());
		return "list";
	}

	public String addPage() {
		Record r = new Record();
		r = RecordCheck.setRecordFieldDesc("dc_content_control", r);
		this.getRequest().setAttribute("record", r);
		TableConfig tc = TableConfig.getInstance();
		ArrayList tablelist = tc.getTableslist();
		this.getRequest().setAttribute("tablelist", tablelist);
		SysParam p = new SysParam();
		ArrayList paramtypes = p.getParamtypes();
		this.getRequest().setAttribute("paramtypes", paramtypes);
		return "manage";
	}

	public String add() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String loginName = user.get("loginname");
		String controlcode = request.getParameter("controlcode");
		String tablename = request.getParameter("tablename");
		String controldesc = request.getParameter("controldesc");
		String conditionsql = request.getParameter("conditionsql");
		String memo = request.getParameter("memo");
		controldesc = StringToZn.toZn(controldesc);
		memo = StringToZn.toZn(memo);
		String operator = loginName;
		conditionsql = StringToZn.toZn(conditionsql);

		String sql = "select top 1 * from " + tablename + " where "
				+ conditionsql;
		String errormsg = ContentControl.checkSelectSql("datacenter", sql);
		if (errormsg != null && errormsg.trim().length() > 0) {
			return this.operaterError("SQL语句有误:" + errormsg);
		}
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());

		ContentControl cc = new ContentControl();

		if (!cc.isExist("datacenter", "dc_content_control", "controlcode",
				Field.FIELD_TYPE_TEXT, StringToZn.toDB(controlcode))) {
			Record r = new Record();
			r.set("controlcode", controlcode, Field.FIELD_TYPE_TEXT, true);
			r.set("tablename", tablename, Field.FIELD_TYPE_TEXT, false);
			r.set("controldesc", controldesc, Field.FIELD_TYPE_TEXT, false);
			r.set("conditionsql", conditionsql, Field.FIELD_TYPE_TEXT, false);
			r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
			r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
			r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
			String error = RecordCheck.checkRecord("dc_content_control", r,
					true);
			if (error != null && error.trim().length() > 0)
				return this.operaterError(error);
			if (cc.insertRecord("datacenter", "dc_content_control", r) > 0) {
				this.setReturnAction(request.getContextPath()
						+ "/sysmanage/contentControlAction");
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
		} else {
			return this.operaterError("控制编码已存在！");
		}

		return this.operaterError("操作失败");
	}

	public String updatePage() {
		HttpServletRequest request = this.getRequest();
		String[] controlcodes = request.getParameterValues("pkfield");
		if (controlcodes == null || controlcodes.length > 1)
			return this.operaterError("请选择1条记录进行操作！");
		String controlcode = controlcodes[0];
		ContentControl rm = new ContentControl();

		ArrayList records = rm.getAllRecords("datacenter", "controlcode='"
				+ controlcode + "'", "");
		if (records != null && records.size() > 0) {
			Record control = (Record) records.get(0);
			control = RecordCheck.setRecordFieldDesc("dc_content_control",
					control);
			this.getRequest().setAttribute("record", control);
			request.setAttribute("record", control);
			TableConfig tc = TableConfig.getInstance();
			ArrayList tablelist = tc.getTableslist();
			this.getRequest().setAttribute("tablelist", tablelist);
		} else
			return this.operaterError("记录已不存在！");
		SysParam p = new SysParam();
		ArrayList paramtypes = p.getParamtypes();
		this.getRequest().setAttribute("paramtypes", paramtypes);
		return "manage";
	}

	public String update() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String loginName = user.get("loginname");
		String controlcode = request.getParameter("controlcode");
		String tablename = request.getParameter("tablename");
		String controldesc = request.getParameter("controldesc");
		String conditionsql = request.getParameter("conditionsql");
		String memo = request.getParameter("memo");
		controldesc = StringToZn.toZn(controldesc);
		memo = StringToZn.toZn(memo);
		String operator = loginName;
		conditionsql = StringToZn.toZn(conditionsql);

		String sql = "select top 1 * from " + tablename + " where "
				+ conditionsql;
		String errormsg = ContentControl.checkSelectSql("datacenter", sql);
		if (errormsg != null && errormsg.trim().length() > 0) {
			return this.operaterError("SQL语句有误:" + errormsg);
		}
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());
		ContentControl cc = new ContentControl();

		if (cc.isExist("datacenter", "dc_content_control", "controlcode",
				Field.FIELD_TYPE_TEXT, StringToZn.toDB(controlcode))) {
			Record r = new Record();
			r.set("controlcode", controlcode, Field.FIELD_TYPE_TEXT, true);
			r.set("tablename", tablename, Field.FIELD_TYPE_TEXT, false);
			r.set("controldesc", controldesc, Field.FIELD_TYPE_TEXT, false);
			r.set("conditionsql", conditionsql, Field.FIELD_TYPE_TEXT, false);
			r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
			r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
			r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
			String error = RecordCheck.checkRecord("dc_content_control", r,
					true);
			if (error != null && error.trim().length() > 0)
				return this.operaterError(error);
			if (cc.updateRecord("datacenter", "dc_content_control", r) > 0) {
				this.setReturnAction(request.getContextPath()
						+ "/sysmanage/contentControlAction");
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
		} else {
			return this.operaterError("记录不存在！");
		}

		return this.operaterError("操作失败");
	}

	public String delete() {
		HttpServletRequest request = this.getRequest();
		String[] controlcodes = request.getParameterValues("pkfield");
		if (controlcodes == null || controlcodes.length <= 0) {
			return this.operaterError("请至少选择一条记录！");
		}
		
		ContentControl cc = new ContentControl();
		String inuse = cc.valueInUse("datacenter", "dc_control_in_user", "controlcode",
				Field.FIELD_TYPE_TEXT, controlcodes);
		if(inuse != null && inuse.trim().length() > 0){
			return this.operaterError(inuse);
		}
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			int res = cc.deleteRecords(conn, "dc_content_control",
					"controlcode", Field.FIELD_TYPE_TEXT, controlcodes);
			int res2 = cc.deleteRecords(conn, "dc_control_in_user",
					"controlcode", Field.FIELD_TYPE_TEXT, controlcodes);
			if (res > 0 && res2 >= 0) {
				conn.commit();
				this.setReturnAction(request.getContextPath()
						+ "/sysmanage/contentControlAction");
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
				this.setPromptMsg("成功删除" + res + "条记录");
				this.setReturnParams(params);
				return "success";
			} else {
				conn.rollback();
				return this.operaterError("操作失败，没有删除任何记录！");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return this.operaterError("操作失败:" + e.getMessage());
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
		TableConfig tc = TableConfig.getInstance();
		ArrayList tablelist = tc.getTableslist();
		if (tablelist != null && tablelist.size() > 0) {
			TableDesc td = null;
			for (int i = 0; i < tablelist.size(); i++) {
				td = (TableDesc) tablelist.get(i);
				if (td != null) {
					this.addFieldParameters("tablename", td.getName(), td
							.getCnname()
							+ "(" + td.getName() + ")");
				}
			}
		}
	}

	public Field getDetailPKField() {
		Field field = new Field();
		field.setFieldName("controlcode");
		field.setFieldType(Field.FIELD_TYPE_TEXT);
		return field;
	}

	public String getDetailPoolName() {
		return "datacenter";
	}

	public TableManage getDetailClass() {
		ContentControl cc = new ContentControl();
		return cc;
	}
}
