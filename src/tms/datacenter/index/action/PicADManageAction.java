package tms.datacenter.index.action;

import java.io.File;
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
import tms.datacenter.index.PicAD;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.RoleManage;
import tms.datacenter.sysmanage.SysParam;
import tms.datacenter.sysmanage.UserManage;
import tms.datacenter.sysmanage.action.PrivilegeParentAction;

public class PicADManageAction extends PrivilegeParentAction {
	public ArrayList getQueryControl() {
		HttpServletRequest request = this.getRequest();
		ArrayList controls = new ArrayList();
		String qctitle = request.getParameter("qtitle");
		qctitle = StringToZn.toZn(qctitle);
		QueryConditionControl qcc = new QueryConditionControl("qtitle",
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
		String condition = cc.getControlSQL(this.getLoginUser(), "dc_picad");
		
		
		String qtitle = request.getParameter("qtitle");
		String b_qupdatetime = request.getParameter("b_qupdatetime");
		String e_qupdatetime = request.getParameter("e_qupdatetime");
		
		if(qtitle != null && qtitle.trim().length() > 0){
			qtitle = StringToZn.toZn(qtitle);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "title like '%"+StringToZn.toDB(qtitle)+"%'";
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
		PicAD picad = new PicAD();
		int totalcount = picad
				.getRecordCount("datacenter", condition, "picid");

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/sysmanage/picADManageAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		ArrayList records = picad.getPageRecord("datacenter", condition, "order by updatetime desc",
				"picid", offset, pager.getSize());

		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());
		
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		request.setAttribute("querys", getQueryControl());
		return "picadlist";
	}
	
	public String addPage() {
		Record r = new Record();
		r = RecordCheck.setRecordFieldDesc("dc_picad",r);
		this.getRequest().setAttribute("record", r);
		return "picadmanage";
	}

	public String add() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String piccid = request.getParameter("picid");
		String title = request.getParameter("title");
		String picpath = request.getParameter("picpath");
		String link = request.getParameter("link");
		String showorder = request.getParameter("showorder");
		String memo = request.getParameter("memo");
		
//		title = StringToZn.toZn(title);
//		memo = StringToZn.toZn(memo);
		String operator = user.get("loginname");
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());

		Record r = new Record();
		//r.set("picid", picid, Field.FIELD_TYPE_INT, true);
		r.set("title", title, Field.FIELD_TYPE_TEXT, false);
		r.set("link", link, Field.FIELD_TYPE_TEXT, false);
		r.set("picpath", picpath, Field.FIELD_TYPE_TEXT, false);
		r.set("showorder", showorder, Field.FIELD_TYPE_DOUBLE, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_picad", r, true,true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		PicAD ad = new PicAD();
		int res = ad.insertRecord("datacenter", "dc_picad", r);
		
		if (res > 0) {
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/picADManageAction");
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
		String[] picids = request.getParameterValues("pkfield");
		if (picids == null || picids.length > 1)
			return this.operaterError("请选择1条记录进行操作！");
		String picid = picids[0];
		PicAD ad= new PicAD();
		ArrayList records = ad.getAllRecords("datacenter", "picid="
				+ ""+picid+"", "");
		if (records != null && records.size() > 0) {
			Record r = (Record) records.get(0);
			r = RecordCheck.setRecordFieldDesc("dc_picad",r);
			this.getRequest().setAttribute("record", r);
		} else
			return this.operaterError("记录已不存在！");
		
		return "picadmanage";
	}

	public String update() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String picid = request.getParameter("picid");
		String title = request.getParameter("title");
		String picpath = request.getParameter("picpath");
		String link = request.getParameter("link");
		String showorder = request.getParameter("showorder");
		String memo = request.getParameter("memo");
		
//		title = StringToZn.toZn(title);
//		memo = StringToZn.toZn(memo);
		String operator = user.get("loginname");
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());

		Record r = new Record();
		r.set("picid", picid, Field.FIELD_TYPE_INT, true);
		r.set("title", title, Field.FIELD_TYPE_TEXT, false);
		r.set("link", link, Field.FIELD_TYPE_TEXT, false);
		r.set("picpath", picpath, Field.FIELD_TYPE_TEXT, false);
		r.set("showorder", showorder, Field.FIELD_TYPE_DOUBLE, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_picad", r, true,true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		PicAD ad= new PicAD();
		if (!ad.isExist("datacenter", "dc_picad", "picid",
				Field.FIELD_TYPE_INT, picid))
			return this.operaterError("操作失败，参数已不存在！");
		int res = ad.updateRecord("datacenter", "dc_picad", r);

		if (res > 0) {
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/picADManageAction");
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

		int res = param.deleteRecords("datacenter", "dc_picad",
				"picid", Field.FIELD_TYPE_TEXT, cids);
		if (res > 0) {

			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/picADManageAction");
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
	}
	public Field getDetailPKField(){
		Field field = new Field();
		field.setFieldName("picid");
		field.setFieldType(Field.FIELD_TYPE_INT);
		return field;
	}
	public String getDetailPoolName(){
		return "datacenter";
	}
	public TableManage getDetailClass(){
		PicAD ad = new PicAD();
		return ad;
	}

}
