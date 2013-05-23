package tms.datacenter.upload.action;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ActionSupport;


import tms.datacenter.commontools.DateUtil;
import tms.datacenter.commontools.ExcelReader;
import tms.datacenter.commontools.Pager;
import tms.datacenter.commontools.QueryConditionControl;
import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.ConnectionManage;
import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.RecordCheck;
import tms.datacenter.dbmanage.TableManage;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.UploadPrivilege;
import tms.datacenter.sysmanage.action.PrivilegeParentAction;
import tms.datacenter.upload.ColumnMsg;
import tms.datacenter.upload.RuleManage;
import tms.datacenter.upload.UploadConfig;
import tms.datacenter.upload.UploadLogManage;
import tms.datacenter.upload.UploadMsg;


public class UploadManageAction  extends PrivilegeParentAction {

	/**
	 * �ϴ���Ŀ�б�
	 * @return
	 */
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
		
//		UploadConfig uc = UploadConfig.getInstance();
//		ArrayList uploads = uc.getUploadslist();
		Record user = this.getLoginUser();
		String loginName = user.get("loginname");
		UploadPrivilege up = new UploadPrivilege();
		ArrayList uploads = up.getUserUploads(loginName);
		//request.setAttribute("records", records);
		request.setAttribute("records", uploads);
		//request.setAttribute("pager", pager.getPage());
		addFiledShowName();
		//request.setAttribute("fieldslabels", fieldslabels);
		
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		return "uploadlist";
	}
	
	/**
	 * ��ʾ�ϴ��б�
	 * @return
	 */
	public String uploadPage(){
		HttpServletRequest request = this.getRequest();
		String[] uploadnames = request.getParameterValues("tablename");
		
		if (uploadnames == null || uploadnames.length != 1)
			return this.operaterError("��ѡ��1����¼���в�����");
		
		String uploadname = uploadnames[0];
		
		UploadConfig uc = UploadConfig.getInstance();
		UploadMsg um = uc.getUpload(uploadname);
		if (um != null) {
			request.setAttribute("uploadMsg", um);
		} else
			return this.operaterError("��¼�Ѳ����ڣ�");
		//ArrayList allPrivilege = rm.getAllPrivilege();
		//Hashtable rolePrivilege = rm.getRolePrivilege("datacenter", rolecode);
		//request.setAttribute("allPrivilege", allPrivilege);
		//request.setAttribute("rolePrivilege", rolePrivilege);
		
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		return "upload";
	}
	
		
	/**
	 * ��ʾ�ϴ���־
	 * @return
	 */
	public String log(){
		HttpServletRequest request = this.getRequest();
		
		String[] uploadnames = request.getParameterValues("tablename");

		if (uploadnames == null || uploadnames.length != 1)
			return this.operaterError("��ѡ��1����¼���в�����");
		
		String uploadname = uploadnames[0];
		
		String page = request.getParameter("page");
		if (page == null || !page.matches("\\d+"))
			page = "0";
		int int_page = Integer.parseInt(page);
		
		Hashtable parames = new Hashtable();
		parames.put("methodName", "log");
		
		UploadLogManage ulm = new UploadLogManage();
		int totalcount = ulm.getRecordCount("datacenter", "uploadname = '" + uploadname + "'", "logid");

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/upload/uploadManageAction", parames);
		pager.setListMethodName("log");
		pager.setSize(15);
		int offset = pager.getStartposition();
		
		ArrayList al = (ArrayList)ulm.getPageRecord("datacenter",
			"uploadname = '" + uploadname + "'",
			"order by uploadtime desc",
			"logid",
			offset,
			pager.getSize());
		
		if (al != null) {
			//Record role = (Record) records.get(0);
			
			request.setAttribute("tablename", uploadname);
			request.setAttribute("loglist", al);
			request.setAttribute("pager", pager.getPage());
		} else{
			return this.operaterError("�ϴ���־�����ڣ�");
		}
		//ArrayList allPrivilege = rm.getAllPrivilege();
		//Hashtable rolePrivilege = rm.getRolePrivilege("datacenter", rolecode);
		//request.setAttribute("allPrivilege", allPrivilege);
		//request.setAttribute("rolePrivilege", rolePrivilege);
		
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		
		return "uploadlog";
	}
	
	/**
	 * ɾ���ϴ���־
	 * @return
	 */
	public String logDel(){
		HttpServletRequest request = this.getRequest();
		String tableName = request.getParameter("tablename");
		String[] logNos = request.getParameterValues("logNo");
		
		if (logNos == null || logNos.length != 1)
			return this.operaterError("��ѡ��1����¼���в�����");
		
		String logNo = logNos[0];
		
		TableManage tm = new TableManage();
				
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			//�����ϴ���־noɾ��
			int resLog = tm.deleteRecords(conn, "dc_uploadlog", "logNo", Field.FIELD_TYPE_TEXT,logNos);
			int resRec = tm.deleteRecords(conn, tableName, "logNo", Field.FIELD_TYPE_TEXT,logNos);
			if(resLog > 0 && resRec > 0){
				conn.commit();
				this.setReturnAction(request.getContextPath()
						+ "/upload/uploadManageAction");
				Hashtable params = new Hashtable();
				params.put("methodName", "list");
				params.put("moduleid", moduleid);
				this.setPromptMsg("�ɹ�ɾ���ϴ�����");
				this.setReturnParams(params);
				return "success";
			}else{
				conn.rollback();
				return this.operaterError("����ʧ�ܣ�û��ɾ���κμ�¼��");
			}
		} catch (SQLException e) {
			return this.operaterError("����ʧ��:"+e.getMessage());
		}finally{
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			cm.freeConnection("datacenter", conn);
		}
		
	}
	/**
	 * ��ʾ�ϴ���¼��ϸ
	 */
	public String detail(){
		HttpServletRequest request = this.getRequest();
		//String logNo = request.getParameter("logNo");
		String[] logNos = request.getParameterValues("logNo");
		String tableName = request.getParameter("tablename");
		
		if (logNos == null || logNos.length != 1)
			return this.operaterError("��ѡ��1����¼���в�����");
		
		String logNo = logNos[0];
		
		String page = request.getParameter("page");
		if (page == null || !page.matches("\\d+"))
			page = "0";
		int int_page = Integer.parseInt(page);

		Hashtable parames = new Hashtable();
		parames.put("methodName", "detail");
		
		TableManage tm = new TableManage();
		tm.setTableName(tableName);
		
		ArrayList resultList = tm.getAllRecords("datacenter", "logNo = '" + logNo + "'", "");
		
		int totalcount = resultList.size();

		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/upload/uploadManageAction", parames);
		pager.setListMethodName("detail");
		pager.setSize(15);
		int offset = pager.getStartposition();
		//�����ݿ��л�ȡ����List<Record>
		ArrayList al = (ArrayList)tm.getPageRecord("datacenter",
			"logNo = '" + logNo + "'",
			"",
			"id",
			offset,
			pager.getSize());
		
		//��������List<Record>��ת��ΪList<List>�������ݱ��޹أ�������jspҳ��
		if (al != null && al.size() != 0) {
			
			//Record role = (Record) records.get(0);
			request.setAttribute("tablename", tableName);
			request.setAttribute("logNo", logNo);
			request.setAttribute("details", al);
			request.setAttribute("pager", pager.getPage());
		} else{
			return this.operaterError("�ϴ����ݲ����ڣ�");
		}
		//ArrayList allPrivilege = rm.getAllPrivilege();
		//Hashtable rolePrivilege = rm.getRolePrivilege("datacenter", rolecode);
		//request.setAttribute("allPrivilege", allPrivilege);
		//request.setAttribute("rolePrivilege", rolePrivilege);
		
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);

		return "detail";
	}
	
	/**
	 * ɾ���ϴ���¼
	 * @return
	 */
	public String delete(){
		HttpServletRequest request = this.getRequest();
		String tableName = request.getParameter("tablename");
		String[] ids = request.getParameterValues("pkfield");
		
		if(ids == null || ids.length <= 0){
			return this.operaterError("������ѡ��һ����¼��");
		}
		
		TableManage tm = new TableManage();
				
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			conn.setAutoCommit(false);
			//�����ϴ���¼idɾ��
			int res = tm.deleteRecords(conn, tableName, "id", Field.FIELD_TYPE_INT,ids);
			if(res > 0){
				conn.commit();
				this.setReturnAction(request.getContextPath()
						+ "/upload/uploadManageAction");
				Hashtable params = new Hashtable();
				params.put("methodName", "list");
				params.put("moduleid", moduleid);
				this.setPromptMsg("�ɹ�ɾ��"+res+"����¼");
				this.setReturnParams(params);
				return "success";
			}else{
				conn.rollback();
				return this.operaterError("����ʧ�ܣ�û��ɾ���κμ�¼��");
			}
		} catch (SQLException e) {
			return this.operaterError("����ʧ��:"+e.getMessage());
		}finally{
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			cm.freeConnection("datacenter", conn);
		}
	}
	
	public String compare(){
		HttpServletRequest request = this.getRequest();
		String[] logNos = request.getParameterValues("logNo");
		String tableName = request.getParameter("tablename");
		
		if (logNos == null)
			return this.operaterError("������ѡ��1����¼���в�����");
		
		//String logNo = logNos[0];
		
		String page = request.getParameter("page");
		if (page == null || !page.matches("\\d+"))
			page = "0";
		int int_page = Integer.parseInt(page);

		Hashtable parames = new Hashtable();
		parames.put("methodName", "compare");
		
		TableManage tm = new TableManage();
		tm.setTableName(tableName);
		
		//��ȡ�ϴ������ļ����õ���Ӧ�ϴ���Ŀ���ֶ���
		UploadConfig uc = UploadConfig.getInstance();
		UploadMsg um = uc.getUpload(tableName);
		
		//ѡ������ζԱ�ʱ�������κ�����
		String condition = "(";
		for (int i = 0; i < logNos.length; i++){
			if (i != 0){
				condition += " or ";
			}
			condition += "logNo = '" + logNos[i] + "'";
			
		}
		condition += ")";
		
		//ArrayList resultList = tm.getAllRecords("datacenter", "logNo = '" + logNo + "'", "");
		ArrayList resultList = tm.getAllRecords("datacenter", condition, "");
		
		Map<String, List<Record>> map = new HashMap<String, List<Record>>();
		List<Record> mulList = new ArrayList<Record>();
		
		for (Object o : resultList){
			Record r = (Record)o;
			String allField = "";
			for(Object o1 : um.getColumnList()){
				allField += r.get(((ColumnMsg)o1).getFieldname()).trim();
			}
						
			if (map.containsKey(allField)){
				map.get(allField).add(r);
			}else{
				List<Record> temp = new ArrayList<Record>();
				temp.add(r);
				map.put(allField, temp);
			}
		}
		for (Map.Entry<String, List<Record>> entry : map.entrySet()) {
            List<Record> value = entry.getValue();
            if (value.size() > 1){
            	mulList.addAll(value);
            }
        }
		
		
		
//		int totalcount = resultList.size();
//
//		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
//				+ "/upload/uploadManageAction", parames);
//		pager.setListMethodName("detail");
//		pager.setSize(15);
//		int offset = pager.getStartposition();
		//�����ݿ��л�ȡ����List<Record>
//		ArrayList al = (ArrayList)tm.getPageRecord("datacenter",
//			"logNo = '" + logNo + "'",
//			"",
//			"id",
//			offset,
//			pager.getSize());
//		
		//��������List<Record>��ת��ΪList<List>�������ݱ��޹أ�������jspҳ��
//		if (al != null && al.size() != 0) {
			
			//Record role = (Record) records.get(0);
			request.setAttribute("tablename", tableName);
			//request.setAttribute("logNo", logNo);
			request.setAttribute("details", mulList);
//			request.setAttribute("pager", pager.getPage());
//		} else{
//			return this.operaterError("�ϴ����ݲ����ڣ�");
//		}
			
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);

		return "compare";
	}
}
