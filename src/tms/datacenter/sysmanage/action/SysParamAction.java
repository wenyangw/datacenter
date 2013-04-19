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
import tms.datacenter.sysmanage.UserManage;

public class SysParamAction extends PrivilegeParentAction {
	public ArrayList getQueryControl() {
		HttpServletRequest request = this.getRequest();
		ArrayList controls = new ArrayList();
		String paramcode = request.getParameter("qparamcode");
		paramcode = StringToZn.toZn(paramcode);
		QueryConditionControl qcc = new QueryConditionControl("qparamcode",
				"参数编码", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(paramcode);
		controls.add(qcc);
		
		String paramname = request.getParameter("qparamname");
		paramname = StringToZn.toZn(paramname);
		qcc = new QueryConditionControl("qparamname",
				"参数名称", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(paramname);
		controls.add(qcc);
		

		String paramtype = request.getParameter("qparamtype");
		paramtype = StringToZn.toZn(paramtype);
		qcc = new QueryConditionControl("qparamtype",
				"参数类型", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_SELECT);
		qcc.setDefaultValue(paramtype);
		
		SysParam sp = new SysParam();
		ArrayList paramtypes =sp.getParamtypes();
		if (paramtypes != null && paramtypes.size() > 0) {
			Record r = null;
			for (int i = 0; i < paramtypes.size(); i++) {
				r = (Record) paramtypes.get(i);
				qcc.addOptions(r.get("type"), r.get("name"));
			}
		}
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
		String condition = cc.getControlSQL(this.getLoginUser(), "dc_sys_param");
		
		String paramcode = request.getParameter("qparamcode");
		String paramname = request.getParameter("qparamname");
		String paramtype = request.getParameter("qparamtype");
		if(paramcode != null && paramcode.trim().length() > 0){
			paramcode = StringToZn.toZn(paramcode);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "paramcode like '%"+StringToZn.toDB(paramcode)+"%'";
		}
		if(paramname != null && paramname.trim().length() > 0){
			paramname = StringToZn.toZn(paramname);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "paramname like '%"+StringToZn.toDB(paramname)+"%'";
		}
		if(paramtype != null && paramtype.trim().length() > 0){
			paramtype = StringToZn.toZn(paramtype);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "paramtype = "+StringToZn.toDB(paramtype)+"";
		}
		SysParam param = new SysParam();
		int totalcount = param
				.getRecordCount("datacenter", condition, "paramcode");

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/sysmanage/sysParamAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		ArrayList records = param.getPageRecord("datacenter", condition, "",
				"paramcode", offset, pager.getSize());

		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());
		
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		request.setAttribute("querys", getQueryControl());
		return "paramlist";
	}
	public String listfordialog() {
		HttpServletRequest request = this.getRequest();
		String condition = "";
		String fieldtype = request.getParameter("fieldtype");
		String operate = request.getParameter("operate");
		request.setAttribute("fieldtype", fieldtype);
		request.setAttribute("operate", operate);
		String paramtype = request.getParameter("qparamtype");
		if(paramtype != null && paramtype.trim().length() > 0){
			paramtype = StringToZn.toZn(paramtype);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "paramtype = "+StringToZn.toDB(paramtype)+"";
		}
		SysParam param = new SysParam();
		ArrayList records = param.getAllRecords("datacenter", condition, "order by paramtype,paramcode");

		request.setAttribute("records", records);
		
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		request.setAttribute("querys", getQueryControl());
		
		return "listfordialog";
	}
	public String addPage() {
		SysParam param = new SysParam();
		ArrayList paramtypes = param.getParamtypes();
		ArrayList parentParams = param.getAllRecords("datacenter", "", "order by paramtype,paramcode");
		HttpServletRequest request = this.getRequest();
		request.setAttribute("paramtypes", paramtypes);
		request.setAttribute("parentParams", parentParams);
		Record r = new Record();
		r = RecordCheck.setRecordFieldDesc("dc_sys_param",r);
		this.getRequest().setAttribute("record", r);
		return "parammanage";
	}

	public String add() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String loginname = user.get("loginname");
		String paramcode = request.getParameter("paramcode");
		String paramname = request.getParameter("paramname");
		String paramtype = request.getParameter("paramtype");
		String parentparam = request.getParameter("parentparam");
		String memo = request.getParameter("memo");
		paramname = StringToZn.toZn(paramname);
		memo = StringToZn.toZn(memo);
		String operator = loginname;
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());

		Record r = new Record();
		r.set("paramcode", paramcode, Field.FIELD_TYPE_TEXT, true);
		r.set("paramname", paramname, Field.FIELD_TYPE_TEXT, false);
		r.set("paramtype", paramtype, Field.FIELD_TYPE_INT, false);
		r.set("parentparam", parentparam, Field.FIELD_TYPE_TEXT, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_sys_param", r, true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		SysParam param = new SysParam();
		if (param.isExist("datacenter", "dc_sys_param", "paramcode",
				Field.FIELD_TYPE_TEXT, paramcode))
			return this.operaterError("操作失败，参数编码已存在！");
		int res = param.insertRecord("datacenter", "dc_sys_param", r);
		
		if (res > 0) {
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/sysParamAction");
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

	public String updatePage() {
		HttpServletRequest request = this.getRequest();
		String[] paramcodes = request.getParameterValues("pkfield");
		if (paramcodes == null || paramcodes.length > 1)
			return this.operaterError("请选择1条记录进行操作！");
		String paramcode = paramcodes[0];
		SysParam param= new SysParam();
		ArrayList records = param.getAllRecords("datacenter", "paramcode='"
				+ paramcode + "'", "");
		if (records != null && records.size() > 0) {
			Record r = (Record) records.get(0);
			r = RecordCheck.setRecordFieldDesc("dc_sys_param",r);
			this.getRequest().setAttribute("record", r);
		} else
			return this.operaterError("记录已不存在！");
		ArrayList paramtypes = param.getParamtypes();
		ArrayList parentParams = param.getAllRecords("datacenter", "paramcode <> '"+paramcode+"'", "order by paramtype,paramcode");
		request.setAttribute("paramtypes", paramtypes);
		request.setAttribute("parentParams", parentParams);
		
		return "parammanage";
	}

	public String update() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String loginname = user.get("loginname");
		String paramcode = request.getParameter("paramcode");
		String paramname = request.getParameter("paramname");
		String paramtype = request.getParameter("paramtype");
		String parentparam = request.getParameter("parentparam");
		String memo = request.getParameter("memo");
		paramname = StringToZn.toZn(paramname);
		memo = StringToZn.toZn(memo);
		String operator = loginname;
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());

		Record r = new Record();
		r.set("paramcode", paramcode, Field.FIELD_TYPE_TEXT, true);
		r.set("paramname", paramname, Field.FIELD_TYPE_TEXT, false);
		r.set("paramtype", paramtype, Field.FIELD_TYPE_INT, false);
		r.set("parentparam", parentparam, Field.FIELD_TYPE_TEXT, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_sys_param", r, true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		SysParam param = new SysParam();
		if (!param.isExist("datacenter", "dc_sys_param", "paramcode",
				Field.FIELD_TYPE_TEXT, paramcode))
			return this.operaterError("操作失败，参数已不存在！");
		int res = param.updateRecord("datacenter", "dc_sys_param", r);

		if (res > 0) {
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/sysParamAction");
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

	public String delete() {
		HttpServletRequest request = this.getRequest();
		String[] paramcodes = request.getParameterValues("pkfield");
		if (paramcodes == null || paramcodes.length <= 0) {
			return this.operaterError("请至少选择一条记录！");
		}
		SysParam param = new SysParam();

		int res = param.deleteRecords("datacenter", "dc_sys_param",
				"paramcode", Field.FIELD_TYPE_TEXT, paramcodes);
		if (res > 0) {

			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/sysParamAction");
			Hashtable params = new Hashtable();
			params.put("methodName", "list");
			this.setPromptMsg("成功删除" + res + "条记录");
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
		} else {
			return this.operaterError("操作失败，没有删除任何记录！");
		}
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
		SysParam param = new SysParam();
		ArrayList paramstypes = param.getParamtypes();
		if(paramstypes != null && paramstypes.size() > 0){
			Record r = null;
			for(int i = 0; i < paramstypes.size(); i++){
				r = (Record)paramstypes.get(i);
				this.addFieldParameters("paramtype", r.get("type"), r.get("name"));
			}
		}
		SysParam sp = new SysParam();
		ArrayList params = sp.getAllRecords("datacenter", "", "");
		if(params != null && params.size() > 0){
			Record r = null;
			for(int i = 0; i < params.size(); i++){
				r = (Record)params.get(i);
				this.addFieldParameters("parentparam", r.get("paramcode"), r.get("paramname"));
			}
		}
	}
	public Field getDetailPKField(){
		Field field = new Field();
		field.setFieldName("paramcode");
		field.setFieldType(Field.FIELD_TYPE_TEXT);
		return field;
	}
	public String getDetailPoolName(){
		return "datacenter";
	}
	public TableManage getDetailClass(){
		SysParam sp = new SysParam();
		return sp;
	}

}
