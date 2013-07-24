package tms.datacenter.index.action;

import java.io.InputStream;
import java.io.OutputStream;
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
import tms.datacenter.index.Notice;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.RoleManage;
import tms.datacenter.sysmanage.SysParam;
import tms.datacenter.sysmanage.UserManage;
import tms.datacenter.sysmanage.action.PrivilegeParentAction;

public class NoticeManageAction extends PrivilegeParentAction {
	public ArrayList getQueryControl() {
		HttpServletRequest request = this.getRequest();
		ArrayList controls = new ArrayList();
		String qshowpos = request.getParameter("qoperator");
		qshowpos = StringToZn.toZn(qshowpos);
		QueryConditionControl qcc = new QueryConditionControl("qoperator",
				"操作人", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_SELECT);
		qcc.setDefaultValue(qshowpos);
		UserManage operator = new UserManage();
		ArrayList operators =operator.getAllRecords("datacenter", "", "");
		if (operators != null && operators.size() > 0) {
			Record r = null;
			for (int i = 0; i < operators.size(); i++) {
				r = (Record) operators.get(i);
				qcc.addOptions(r.get("loginname"), r.get("username"));
			}
		}
		String qctitle = request.getParameter("qtitle");
		qctitle = StringToZn.toZn(qctitle);
		qcc = new QueryConditionControl("qtitle",
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
		
		String operator = request.getParameter("qoperator");
		String qctitle = request.getParameter("qtitle");
		String b_qupdatetime = request.getParameter("b_qupdatetime");
		String e_qupdatetime = request.getParameter("e_qupdatetime");
		if(operator != null && operator.trim().length() > 0){
			operator = StringToZn.toZn(operator);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "operator = '"+StringToZn.toDB(operator)+"'";
		}
		if(qctitle != null && qctitle.trim().length() > 0){
			qctitle = StringToZn.toZn(qctitle);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "title like '%"+StringToZn.toDB(qctitle)+"%'";
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
		Notice column = new Notice();
		int totalcount = column
				.getRecordCount("datacenter", condition, "noticeid");

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/sysmanage/noticeManageAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		ArrayList records = column.getPageRecord("datacenter", condition, "order by updatetime desc",
				"noticeid", offset, pager.getSize());

		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());
		
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		request.setAttribute("querys", getQueryControl());
		return "noticelist";
	}
	
	public String addPage() {
		HttpServletRequest request = this.getRequest();
		Record r = new Record();
		r = RecordCheck.setRecordFieldDesc("dc_notice",r);
		this.getRequest().setAttribute("record", r);
		return "noticemanage";
	}

	public String add() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String ctitle = request.getParameter("title");
		String content = request.getParameter("content");
		String inuse = request.getParameter("inuse");
		String memo = request.getParameter("memo");
		String publisher = request.getParameter("publisher");
		String pdffile = request.getParameter("pdffile");
		publisher = StringToZn.toZn(publisher);
		ctitle = StringToZn.toZn(ctitle);
		content = StringToZn.toZn(content);
		memo = StringToZn.toZn(memo);
		String operator = user.get("loginname");
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());
		
		Record r = new Record();
		r.set("title", ctitle, Field.FIELD_TYPE_TEXT, false);
		r.set("content", content, Field.FIELD_TYPE_TEXT, false);
		r.set("inuse", inuse, Field.FIELD_TYPE_INT, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		r.set("publisher", publisher, Field.FIELD_TYPE_TEXT, false);
		r.set("pdffile", pdffile, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_notice", r, false,true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		Notice n = new Notice();
		int res = n.insertRecord("datacenter", "dc_notice", r);
		
		if (res > 0) {
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/noticeManageAction");
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
		String[] noticeids = request.getParameterValues("pkfield");
		if (noticeids == null || noticeids.length > 1)
			return this.operaterError("请选择1条记录进行操作！");
		String noticeid = noticeids[0];
		Notice column= new Notice();
		ArrayList records = column.getAllRecords("datacenter", "noticeid="+noticeid, "");
		if (records != null && records.size() > 0) {
			Record r = (Record) records.get(0);
			r = RecordCheck.setRecordFieldDesc("dc_notice",r);
			this.getRequest().setAttribute("record", r);
		} else
			return this.operaterError("记录已不存在！");
		
		return "noticemanage";
	}

	public String update() {
		HttpServletRequest request = this.getRequest();
		Record user = this.getLoginUser();
		String ctitle = request.getParameter("title");
		String content = request.getParameter("content");
		String inuse = request.getParameter("inuse");
		String memo = request.getParameter("memo");
		String noticeid = request.getParameter("noticeid");
		String pdffile = request.getParameter("pdffile");
		ctitle = StringToZn.toZn(ctitle);
		content = StringToZn.toZn(content);
		memo = StringToZn.toZn(memo);
		String operator = user.get("loginname");
		String updatetime = DateUtil.dateToStringWithTime(new java.util.Date());
		String publisher = request.getParameter("publisher");
		publisher = StringToZn.toZn(publisher);
		Record r = new Record();
		r.set("noticeid", noticeid, Field.FIELD_TYPE_INT, true);
		r.set("title", ctitle, Field.FIELD_TYPE_TEXT, false);
		r.set("content", content, Field.FIELD_TYPE_TEXT, false);
		r.set("inuse", inuse, Field.FIELD_TYPE_INT, false);
		r.set("updatetime", updatetime, Field.FIELD_TYPE_DATE, false);
		r.set("operator", operator, Field.FIELD_TYPE_TEXT, false);
		r.set("memo", memo, Field.FIELD_TYPE_TEXT, false);
		r.set("publisher", publisher, Field.FIELD_TYPE_TEXT, false);
		r.set("publisher", publisher, Field.FIELD_TYPE_TEXT, false);
		r.set("pdffile", pdffile, Field.FIELD_TYPE_TEXT, false);
		String error = RecordCheck.checkRecord("dc_column", r, false,true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		Notice column = new Notice();
		if (!column.isExist("datacenter", "dc_notice", "noticeid",
				Field.FIELD_TYPE_INT, noticeid))
			return this.operaterError("操作失败，公告已不存在！");
		int res = column.updateRecord("datacenter", "dc_notice", r);

		if (res > 0) {
			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/noticeManageAction");
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
		String[] noticeids = request.getParameterValues("pkfield");
		if (noticeids == null || noticeids.length <= 0) {
			return this.operaterError("请至少选择一条记录！");
		}
		SysParam param = new SysParam();

		int res = param.deleteRecords("datacenter", "dc_notice",
				"noticeid", Field.FIELD_TYPE_INT, noticeids);
		if (res > 0) {

			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/noticeManageAction");
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
		this.addFieldParameters("inuse", "1", "是");
		this.addFieldParameters("inuse", "0", "否");
	}
	public Field getDetailPKField(){
		Field field = new Field();
		field.setFieldName("noticeid");
		field.setFieldType(Field.FIELD_TYPE_INT);
		return field;
	}
	public String getDetailPoolName(){
		return "datacenter";
	}
	public TableManage getDetailClass(){
		Notice notice = new Notice();
		return notice;
	}

}
