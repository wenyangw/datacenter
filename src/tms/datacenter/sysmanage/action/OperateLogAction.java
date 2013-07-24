package tms.datacenter.sysmanage.action;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import tms.datacenter.commontools.Pager;
import tms.datacenter.commontools.QueryConditionControl;
import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.OperateLog;
import tms.datacenter.sysmanage.UserManage;

public class OperateLogAction  extends PrivilegeParentAction {
	public ArrayList getQueryControl() {
		HttpServletRequest request = this.getRequest();
		ArrayList controls = new ArrayList();
		String qloginname = request.getParameter("qloginname");
		qloginname = StringToZn.toZn(qloginname);
		QueryConditionControl qcc = new QueryConditionControl("qloginname",
				"用户", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_SELECT);
		qcc.setDefaultValue(qloginname);
		UserManage user = new UserManage();
		ArrayList users =user.getAllRecords("datacenter", "", "order by loginname");
		if (users != null && users.size() > 0) {
			Record r = null;
			for (int i = 0; i < users.size(); i++) {
				r = (Record) users.get(i);
				qcc.addOptions(r.get("loginname"), r.get("username")+"("+r.get("loginname")+")");
			}
		}
		controls.add(qcc);
		String qoperate = request.getParameter("qoperate");
		qoperate = StringToZn.toZn(qoperate);
		qcc = new QueryConditionControl("qoperate",
				"操作", QueryConditionControl.QUERY_TYPE_COMMON,
				QueryConditionControl.CONTROL_TYPE_TEXT);
		qcc.setDefaultValue(qoperate);
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
		String condition = cc.getControlSQL(this.getLoginUser(), "dc_role");
		
		String qloginname = request.getParameter("qloginname");
		String qoperate = request.getParameter("qoperate");
		String b_qupdatetime = request.getParameter("b_qupdatetime");
		String e_qupdatetime = request.getParameter("e_qupdatetime");
		if(qloginname != null && qloginname.trim().length() > 0){
			qloginname = StringToZn.toZn(qloginname);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "loginname = '"+qloginname+"'";
		}
		if(qoperate != null && qoperate.trim().length() > 0){
			qoperate = StringToZn.toZn(qoperate);
			if(condition.trim().length() > 0)
				condition+=" and ";
			condition += "operate like '%"+StringToZn.toDB(qoperate)+"%'";
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
		OperateLog log = new OperateLog();
		int totalcount = log.getRecordCount("datacenter", condition, "logid");

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/sysmanage/operateLogAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		ArrayList records = log.getPageRecord("datacenter", condition, " order by updatetime desc",
				"logid", offset, pager.getSize());

		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		request.setAttribute("querys", getQueryControl());
		return "loglist";
	}

	public String delete() {
		HttpServletRequest request = this.getRequest();
		String[] logids = request.getParameterValues("pkfield");
		if (logids == null || logids.length <= 0) {
			return this.operaterError("请至少选择一条记录！");
		}
		OperateLog log = new OperateLog();

		int res = log.deleteRecords("datacenter", "dc_operatelog",
				"logid", Field.FIELD_TYPE_INT, logids);
		if (res > 0) {

			this.setReturnAction(request.getContextPath()
					+ "/sysmanage/operateLogAction");
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
				this.addFieldParameters("loginname", r.get("loginname"), r.get("username")+"("+r.get("loginname")+")");
			}
		}
	}
	public Field getDetailPKField(){
		Field field = new Field();
		field.setFieldName("logid");
		field.setFieldType(Field.FIELD_TYPE_INT);
		return field;
	}
	public String getDetailPoolName(){
		return "datacenter";
	}
	public TableManage getDetailClass(){
		OperateLog log = new OperateLog();
		return log;
	}
}
