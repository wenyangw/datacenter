package tms.datacenter.index.action;

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
import tms.datacenter.index.Column;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.RoleManage;
import tms.datacenter.sysmanage.SysParam;
import tms.datacenter.sysmanage.UserManage;
import tms.datacenter.sysmanage.action.PrivilegeParentAction;

public class ColumnManageAction extends PrivilegeParentAction {
	public ArrayList getQueryControl() {
		HttpServletRequest request = this.getRequest();
		ArrayList controls = new ArrayList();
		String qshowpos = request.getParameter("qshowpos");
		qshowpos = StringToZn.toZn(qshowpos);
		QueryConditionControl qcc = new QueryConditionControl("qshowpos",
				"显示位置", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_SELECT);
		qcc.setDefaultValue(qshowpos);
		SysParam sp = new SysParam();
		ArrayList positions =sp.getAllRecords("datacenter", "paramtype=4", "order by paramcode");
		if (positions != null && positions.size() > 0) {
			Record r = null;
			for (int i = 0; i < positions.size(); i++) {
				r = (Record) positions.get(i);
				qcc.addOptions(r.get("paramcode"), r.get("paramname"));
			}
		}
		controls.add(qcc);
		String qctitle = request.getParameter("qctitle");
		qctitle = StringToZn.toZn(qctitle);
		qcc = new QueryConditionControl("qctitle",
				"标题", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(qctitle);
		controls.add(qcc);
		
		String bqupdatetime = request.getParameter("b_qupdatetime");
		String equpdatetime = request.getParameter("e_qupdatetime");
		bqupdatetime = StringToZn.toZn(bqupdatetime);
		equpdatetime = StringToZn.toZn(equpdatetime);
		qcc = new QueryConditionControl("qupdatetime",
				"更新时间", QueryConditionControl.QUERY_TYPE_RANGE,
				QueryConditionControl.CONTROL_TYPE_DATE);
		qcc.setDefaultRangeValue(bqupdatetime,equpdatetime);
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
		String condition = cc.getControlSQL(this.getLoginUser(), "dc_column");
		
		String qshowpos = request.getParameter("qshowpos");
		String qctitle = request.getParameter("qctitle");
		String b_qupdatetime = request.getParameter("b_qupdatetime");
		String e_qupdatetime = request.getParameter("e_qupdatetime");
		if(qshowpos != null && qshowpos.trim().length() > 0){
			qshowpos = StringToZn.toZn(qshowpos);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "showwhere = '"+StringToZn.toDB(qshowpos)+"'";
		}
		if(qctitle != null && qctitle.trim().length() > 0){
			qctitle = StringToZn.toZn(qctitle);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "ctitle like '%"+StringToZn.toDB(qctitle)+"%'";
		}
		if(b_qupdatetime != null && b_qupdatetime.trim().length() > 0){
			b_qupdatetime = StringToZn.toZn(b_qupdatetime);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "updatetime >= "+StringToZn.toDB(b_qupdatetime)+"";
		}
		if(e_qupdatetime != null && e_qupdatetime.trim().length() > 0){
			e_qupdatetime = StringToZn.toZn(e_qupdatetime);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "updatetime <= "+StringToZn.toDB(e_qupdatetime+" 23:59:59")+"";
		}
		Column column = new Column();
		int totalcount = column
				.getRecordCount("datacenter", condition, "cid");

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/sysmanage/columnManageAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		ArrayList records = column.getPageRecord("datacenter", condition, "order by updatetime desc",
				"cid", offset, pager.getSize());

		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());
		
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		request.setAttribute("querys", getQueryControl());
		return "columnlist";
	}
	
	public String addPage() {
		SysParam param = new SysParam();
		ArrayList showpos = param.getAllRecords("datacenter", "paramtype=4", "order by paramcode");
		HttpServletRequest request = this.getRequest();
		request.setAttribute("pos", showpos);
		Record r = new Record();
		r = RecordCheck.setRecordFieldDesc("dc_column",r);
		this.getRequest().setAttribute("record", r);
		return "columnmanage";
	}

	public String add() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String cid = request.getParameter("cid");
		String ctitle = request.getParameter("ctitle");
		String clink = request.getParameter("clink");
		String showwhere = request.getParameter("showwhere");
		String showtype = request.getParameter("showtype");
		String showorder = request.getParameter("showorder");
		String memo = request.getParameter("memo");
		
		String cheight = request.getParameter("cheight");
		String cwidth = request.getParameter("cwidth");
		
		ctitle = StringToZn.toZn(ctitle);
		memo = StringToZn.toZn(memo);
		String operator = user.get("loginname");
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());

		Record r = new Record();
		r.set("cid", cid, Field.FIELD_TYPE_TEXT, true);
		r.set("ctitle", ctitle, Field.FIELD_TYPE_TEXT, false);
		r.set("clink", clink, Field.FIELD_TYPE_TEXT, false);
		r.set("showwhere", showwhere, Field.FIELD_TYPE_TEXT, false);
		r.set("cheight", cheight, Field.FIELD_TYPE_INT, false);
		r.set("cwidth", 100+"", Field.FIELD_TYPE_INT, false);
		r.set("showtype", showtype, Field.FIELD_TYPE_INT, false);
		r.set("showorder", showorder, Field.FIELD_TYPE_DOUBLE, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_column", r, true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		SysParam param = new SysParam();
//		if (param.isExist("datacenter", "dc_sys_param", "paramcode",
//				Field.FIELD_TYPE_TEXT, paramcode))
//			return this.operaterError("操作失败，参数编码已存在！");
		int res = param.insertRecord("datacenter", "dc_column", r);
		
		if (res > 0) {
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/columnManageAction");
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
		String[] cids = request.getParameterValues("pkfield");
		if (cids == null || cids.length > 1)
			return this.operaterError("请选择1条记录进行操作！");
		String cid = cids[0];
		Column column= new Column();
		ArrayList records = column.getAllRecords("datacenter", "cid="
				+ "'"+cid+"'", "");
		if (records != null && records.size() > 0) {
			Record r = (Record) records.get(0);
			r = RecordCheck.setRecordFieldDesc("dc_column",r);
			this.getRequest().setAttribute("record", r);
		} else
			return this.operaterError("记录已不存在！");
		SysParam param = new SysParam();
		ArrayList showpos = param.getAllRecords("datacenter", "paramtype=4", "order by paramcode");
		request.setAttribute("pos", showpos);
		
		return "columnmanage";
	}

	public String update() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String cid = request.getParameter("cid");
		String ctitle = request.getParameter("ctitle");
		String clink = request.getParameter("clink");
		String showwhere = request.getParameter("showwhere");
		String showtype = request.getParameter("showtype");
		String showorder = request.getParameter("showorder");
		String memo = request.getParameter("memo");
		
		String cheight = request.getParameter("cheight");
		String cwidth = request.getParameter("cwidth");
		
		ctitle = StringToZn.toZn(ctitle);
		memo = StringToZn.toZn(memo);
		String operator = user.get("loginname");
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());

		Record r = new Record();
		r.set("cid", cid, Field.FIELD_TYPE_TEXT, true);
		r.set("ctitle", ctitle, Field.FIELD_TYPE_TEXT, false);
		r.set("clink", clink, Field.FIELD_TYPE_TEXT, false);
		r.set("showwhere", showwhere, Field.FIELD_TYPE_TEXT, false);
		r.set("showtype", showtype, Field.FIELD_TYPE_INT, false);
		r.set("cheight", cheight, Field.FIELD_TYPE_INT, false);
		r.set("cwidth", 100+"", Field.FIELD_TYPE_INT, false);
		r.set("showorder", showorder, Field.FIELD_TYPE_DOUBLE, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_column", r, true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		Column column = new Column();
		if (!column.isExist("datacenter", "dc_column", "cid",
				Field.FIELD_TYPE_TEXT, cid))
			return this.operaterError("操作失败，参数已不存在！");
		int res = column.updateRecord("datacenter", "dc_column", r);

		if (res > 0) {
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/columnManageAction");
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
		String[] cids = request.getParameterValues("pkfield");
		if (cids == null || cids.length <= 0) {
			return this.operaterError("请至少选择一条记录！");
		}
		SysParam param = new SysParam();

		int res = param.deleteRecords("datacenter", "dc_column",
				"cid", Field.FIELD_TYPE_TEXT, cids);
		if (res > 0) {

			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/columnManageAction");
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
		SysParam sp = new SysParam();
		ArrayList positions =sp.getAllRecords("datacenter", "paramtype=4", "order by paramcode");
		if(positions != null && positions.size() > 0){
			Record r = null;
			for(int i = 0; i < positions.size(); i++){
				r = (Record)positions.get(i);
				this.addFieldParameters("showwhere", r.get("paramcode"), r.get("paramname"));
			}
		}
		this.addFieldParameters("showtype", "1", "显示标题");
		this.addFieldParameters("showtype", "2", "显示内容");
	}
	public Field getDetailPKField(){
		Field field = new Field();
		field.setFieldName("cid");
		field.setFieldType(Field.FIELD_TYPE_TEXT);
		return field;
	}
	public String getDetailPoolName(){
		return "datacenter";
	}
	public TableManage getDetailClass(){
		Column column = new Column();
		return column;
	}

}
