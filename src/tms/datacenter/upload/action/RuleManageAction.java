package tms.datacenter.upload.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

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
import tms.datacenter.sysmanage.UserManage;
import tms.datacenter.sysmanage.action.PrivilegeParentAction;
//import tms.datacenter.upload.ProjectManage;
import tms.datacenter.upload.RuleManage;
import tms.datacenter.upload.UploadConfig;

import com.opensymphony.xwork2.ActionSupport;

public class RuleManageAction extends PrivilegeParentAction{
	
	
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
		
//		String rolecode = request.getParameter("qrolecode");
//		String rolename = request.getParameter("qrolename");
//		if(rolecode != null && rolecode.trim().length() > 0){
//			rolecode = StringToZn.toZn(rolecode);
//			if(condition.trim().length() > 0)
//				condition+=" and ";
//			condition += "rolecode like '%"+StringToZn.toDB(rolecode)+"%'";
//		}
//		if(rolename != null && rolename.trim().length() > 0){
//			rolename = StringToZn.toZn(rolename);
//			if(condition.trim().length() > 0)
//				condition+=" and ";
//			condition += "rolename like '%"+StringToZn.toDB(rolename)+"%'";
//		}
		
		RuleManage rm = new RuleManage();
		int totalcount = rm.getRecordCount("datacenter", condition, "ruleId");

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/upload/ruleManageAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		
		
		
		//ArrayList records = um.getPageRecord("datacenter", condition, "",
		//		"ruleId", offset, pager.getSize());
		ArrayList records = rm.getPageRecord("datacenter", "ruleId", offset, pager.getSize());
				
		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());
		addFiledShowName();
		//request.setAttribute("fieldslabels", fieldslabels);
		//request.setAttribute("querys", getQueryControl());
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		return "rulelist";
	}
	
	public String updatePage(){
		HttpServletRequest request = this.getRequest();
		String ruleId = request.getParameter("pkfield");
		if (ruleId == null || ruleId.trim().length() <= 0)
			return this.operaterError("请选择1条记录进行操作！");
		RuleManage rm = new RuleManage();

		//ArrayList records = rm.getAllRecords("datacenter", "ruleId="
		//		+ ruleId, "");
		ArrayList records = rm.getRecord("datacenter", "ruleId=" + ruleId);
		if (records != null && records.size() > 0) {
			Record rule = (Record) records.get(0);
			request.setAttribute("rule", rule);
		} else
			return this.operaterError("记录已不存在！");
		//ArrayList allPrivilege = rm.getAllPrivilege();
		//Hashtable rolePrivilege = rm.getRolePrivilege("datacenter", rolecode);
		//request.setAttribute("allPrivilege", allPrivilege);
		//request.setAttribute("rolePrivilege", rolePrivilege);
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		return "rulemanage";
	}
	
	public String update(){
		HttpServletRequest request = this.getRequest();
		String ruleId = request.getParameter("ruleId");
		String uploadName = request.getParameter("uploadpk");
		String ruleType = request.getParameter("ruleType");
		String cycle = "0";
		if (!ruleType.equals("0")){
			cycle = request.getParameter("cycle");
		}
		String startTime = request.getParameter("startTime");
		
		RuleManage rm = new RuleManage();
		
		Record r = (Record)rm.getRecord("datacenter", "ruleId = " + ruleId).get(0);
		r.set("ruleid", ruleId, Field.FIELD_TYPE_INT, true);
		r.set("uploadname", uploadName, Field.FIELD_TYPE_TEXT, false);
		r.set("ruleType", ruleType, Field.FIELD_TYPE_INT, false);
		r.set("cycle", cycle, Field.FIELD_TYPE_INT, false);
		r.set("starttime", startTime, Field.FIELD_TYPE_DATE, false);
		
		String error = RecordCheck.checkRecord("dc_uploadrule", r, true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			int res = rm.updateRecord(conn, "dc_uploadrule", r);
			if (res <= 0) {
				conn.rollback();
				return this.operaterError("更新上传计划失败！");
			}
			conn.commit();
			this.setReturnAction(request.getContextPath()
					+ "/upload/ruleManageAction");
			Hashtable params = new Hashtable();
			params.put("methodName", "list");
			params.put("moduleid", moduleid);
			this.setReturnParams(params);
			
			return SUCCESS;
		}catch (Exception e) {
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

		return this.operaterError("操作失败");
	}
	
	public String addPage() {
		
		Record r = new Record();
		this.getRequest().setAttribute("record", r);
		
		UploadConfig uc = UploadConfig.getInstance();
		ArrayList uploadlist = uc.getUploadslist();
		this.getRequest().setAttribute("uploadlist", uploadlist);
		HttpServletRequest request = this.getRequest();
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		return "rulemanage";
	}
	
	public String add(){
		HttpServletRequest request = this.getRequest();
		
		String uploadName = request.getParameter("uploadName");
		String ruleType= request.getParameter("ruleType");
		String cycle= request.getParameter("cycle");
		String startTime= request.getParameter("startTime");
		
		RuleManage rm = new RuleManage();
		
		if (rm.isExist("datacenter", "dc_uploadrule", "uploadname",
				Field.FIELD_TYPE_TEXT, uploadName)){
			return this.operaterError("操作失败，上传项目计划已存在！");
		}
		
		if (!ruleType.equals("0") && Integer.parseInt(cycle) <= 0){
			return this.operaterError("操作失败，计划周期必须为大于0的整数！");
		}
		
		if(DateUtil.daysBetween(DateUtil.stringToDate(startTime), DateUtil.getCurrentDateTime()) >= 0){
			return this.operaterError("操作失败，计划开始日期请选择未来的日期！");
		}
		
		Record r = new Record();
		r.set("uploadname", uploadName, Field.FIELD_TYPE_TEXT, false);
		r.set("ruletype", ruleType, Field.FIELD_TYPE_INT, false);
		r.set("cycle", cycle, Field.FIELD_TYPE_INT, false);
		r.set("starttime", startTime, Field.FIELD_TYPE_DATE, false);
		
		String error = RecordCheck.checkRecord("dc_uploadrule", r, true);
		if(error != null && error.trim().length() > 0)
			return this.operaterError(error);
		
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			int res = rm.insertRecord(conn, "dc_uploadrule", r);
			if (res <= 0) {
				conn.rollback();
				return this.operaterError("添加上传计划失败！");
			}
			conn.commit();
			this.setReturnAction(request.getContextPath()
					+ "/upload/ruleManageAction");
			Hashtable params = new Hashtable();
			params.put("methodName", "list");
			params.put("moduleid", moduleid);
			this.setReturnParams(params);
			
			return SUCCESS;
		}catch (Exception e) {
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

		return this.operaterError("操作失败");
		
	}
	
	public String delete(){
		HttpServletRequest request = this.getRequest();
		String[] ruleIds = request.getParameterValues("pkfield");
		if(ruleIds == null || ruleIds.length <= 0){
			return this.operaterError("请至少选择一条记录！");
		}
		RuleManage rm = new RuleManage();
		
		
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			int res = rm.deleteRecords(conn, "dc_uploadrule", "ruleid", Field.FIELD_TYPE_INT,ruleIds);
			//int res2 = rm.deleteRecords(conn, "dc_role_privilege", "rolecode", Field.FIELD_TYPE_TEXT,rolecodes);
			if(res > 0){
				conn.commit();
				this.setReturnAction(request.getContextPath()
						+ "/upload/ruleManageAction");
				Hashtable params = new Hashtable();
				params.put("methodName", "list");
				params.put("moduleid", moduleid);
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
	
}
