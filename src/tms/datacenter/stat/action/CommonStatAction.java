package tms.datacenter.stat.action;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import tms.datacenter.commontools.DateUtil;
import tms.datacenter.commontools.ExportToExcel;
import tms.datacenter.commontools.ExportToTXT;
import tms.datacenter.commontools.Pager;
import tms.datacenter.commontools.QueryConditionControl;
import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.ConnectionManage;
import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.RecordCheck;
import tms.datacenter.dbmanage.TableManage;
import tms.datacenter.stat.CommonStat;
import tms.datacenter.stat.StatConfig;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.RoleManage;
import tms.datacenter.sysmanage.SysParam;
import tms.datacenter.sysmanage.UserManage;
import tms.datacenter.sysmanage.action.PrivilegeParentAction;

public class CommonStatAction extends PrivilegeParentAction {
	public void addFiledShowName(){
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		request.setAttribute("specialparam", specialParam==null?"":specialParam);
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		ArrayList controls = new ArrayList();
		ArrayList qccs = cs.getQuerycontrols();
		ArrayList valuelist = null;
		Hashtable texthash = null;
		String optionValue = "";
		String optionText = "";
		if(qccs != null && qccs.size() > 0){
			QueryConditionControl qcc = null;
			String listfield = "";
			for(int i=0; i < qccs.size(); i++){
				qcc = (QueryConditionControl)qccs.get(i);
				if(qcc != null){
					
					listfield = qcc.getListfield();
					if(listfield == null || listfield.trim().length() <= 0)
						continue;
					valuelist = qcc.getValuelist();
					texthash = qcc.getTexthash();
					if(valuelist != null && valuelist.size() > 0){
						for(int j = 0; j < valuelist.size(); j++){
							optionValue = (String)valuelist.get(j);
							optionText = optionValue;
							if(optionValue != null && optionValue.trim().length() > 0){
								if(texthash != null && texthash.size() > 0){
									optionText = (String)texthash.get(optionValue);
									if(optionText == null || optionText.trim().length() <= 0)
										optionText = optionValue;
								}
							}
							this.addFieldParameters(listfield, optionValue, optionText);
						}
					}
				}
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
	public ArrayList getQueryControl() {
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		request.setAttribute("specialparam", specialParam==null?"":specialParam);
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		ArrayList controls = new ArrayList();
		ArrayList qccs = cs.getQuerycontrols();
		if(qccs != null && qccs.size() > 0){
			QueryConditionControl qcc = null;
			String queryname = "";
			String querytype = "";
			String defaultvalue = "";
			String defaultvalue2 = "";
			String isshow = "";
			String listfield = "";
			for(int i=0; i < qccs.size(); i++){
				qcc = (QueryConditionControl)qccs.get(i);
				if(qcc != null){
					queryname = qcc.getName();
					isshow = qcc.getIsshow();
					if(!QueryConditionControl.IS_SHOW_YES.equals(isshow))
						continue;
					if(queryname != null && queryname.trim().length() > 0){
						querytype = qcc.getQuerytype();
						if(QueryConditionControl.QUERY_TYPE_COMMON.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter(queryname));
							qcc.setDefaultValue(defaultvalue);
						}else if(QueryConditionControl.QUERY_TYPE_RANGE.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter("b_"+queryname));
							defaultvalue2 = StringToZn.toZn(request.getParameter("e_"+queryname));
							qcc.setDefaultRangeValue(defaultvalue, defaultvalue2);
						}
						
						controls.add(qcc);
					}
				}
			}
		}
		return controls;
	}
	public String list(){
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		if(cs == null)
			return "list";
		String methodName = cs.getMethodname();
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		if("sqlStat".equals(methodName))
			return sqlStat();
		else
			return tableList();
	}
	private String sqlStat() {
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		if(cs == null)
			return "list";
		String statsql = cs.getSqlstr();
		String poolname = cs.getPoolname();
		ArrayList qccs = cs.getQuerycontrols();
		if(qccs != null && qccs.size() > 0){
			QueryConditionControl qcc = null;
			String queryname = "";
			String querytype = "";
			String defaultvalue = "";
			String defaultvalue2 = "";
			String conditionstr = "";
			String conditionstr2 = "";
			for(int i=0; i < qccs.size(); i++){
				qcc = (QueryConditionControl)qccs.get(i);
				if(qcc != null){
					queryname = qcc.getName();
					if(queryname != null && queryname.trim().length() > 0){
						querytype = qcc.getQuerytype();
						if(QueryConditionControl.QUERY_TYPE_COMMON.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter(queryname));
							conditionstr = qcc.getConditionsql();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/"+queryname+"/", defaultvalue);
								statsql = statsql.replaceAll("/"+queryname+"/", conditionstr);
							}else{
								statsql = statsql.replaceAll("/"+queryname+"/", "");
							}
						}else if(QueryConditionControl.QUERY_TYPE_RANGE.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter("b_"+queryname));
							defaultvalue2 = StringToZn.toZn(request.getParameter("e_"+queryname));
							conditionstr = qcc.getConditionsql();
							conditionstr2 = qcc.getConditionsql2();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/b_"+queryname+"/", defaultvalue);
							}else
								conditionstr = "";
							if(defaultvalue2 != null && defaultvalue2.trim().length() > 0){
								conditionstr2 = conditionstr2.replaceAll("/e_"+queryname+"/", defaultvalue);
							}else
								conditionstr2 = "";
							statsql = statsql.replaceAll("/"+queryname+"/", conditionstr+conditionstr2);
						}
						
					}
				}
			}
		}
		ArrayList records = TableManage.executeQuery(poolname, statsql);
		request.setAttribute("records", records);
		
		request.setAttribute("querys", getQueryControl());
		return "list";
	}
	private String tableList() {
		HttpServletRequest request = this.getRequest();
		
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		if(cs == null)
			return "list";
		String poolname = cs.getPoolname();
		String tablename = cs.getTablename();
		String pkfield = cs.getPkfield();
		String orderby = cs.getOrderby();
		String hiddenfields = cs.getHiddenfields();
		ContentControl c = new ContentControl();
		String condition = c.getControlSQL(this.getLoginUser(), tablename);
		ArrayList qccs = cs.getQuerycontrols();
		if(qccs != null && qccs.size() > 0){
			QueryConditionControl qcc = null;
			String queryname = "";
			String querytype = "";
			String defaultvalue = "";
			String defaultvalue2 = "";
			String conditionstr = "";
			String conditionstr2 = "";
			for(int i=0; i < qccs.size(); i++){
				qcc = (QueryConditionControl)qccs.get(i);
				if(qcc != null){
					queryname = qcc.getName();
					if(queryname != null && queryname.trim().length() > 0){
						querytype = qcc.getQuerytype();
						if(QueryConditionControl.QUERY_TYPE_COMMON.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter(queryname));
							conditionstr = qcc.getConditionsql();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/"+queryname+"/", defaultvalue);
								condition+= conditionstr;
							}
						}else if(QueryConditionControl.QUERY_TYPE_RANGE.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter("b_"+queryname));
							defaultvalue2 = StringToZn.toZn(request.getParameter("e_"+queryname));
							conditionstr = qcc.getConditionsql();
							conditionstr2 = qcc.getConditionsql2();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/b_"+queryname+"/", defaultvalue);
							}else
								conditionstr = "";
							if(defaultvalue2 != null && defaultvalue2.trim().length() > 0){
								conditionstr2 = conditionstr2.replaceAll("/e_"+queryname+"/", defaultvalue2);
							}else
								conditionstr2 = "";
							condition+=  conditionstr+conditionstr2;
						}
						
					}
				}
			}
		}
		
		if(condition!= null&& condition.trim().length() > 0){
			if(condition.trim().toLowerCase().startsWith("and"))
				condition = condition.trim().substring(3);
		}
		
		String page = request.getParameter("page");
		if (page == null || !page.matches("\\d+"))
			page = "0";
		int int_page = Integer.parseInt(page);
		TableManage tm = new TableManage();
		tm.setTableName(tablename);
		int totalcount = tm.getRecordCount(poolname, condition,pkfield);
		Hashtable parames = new Hashtable();
		parames.put("methodName", "list");
		Pager pager = new Pager(int_page, totalcount, request.getContextPath()
				+ "/stat/commonStatAction", parames);
		pager.setSize(15);
		int offset = pager.getStartposition();
		
		ArrayList records = tm.getPageRecord(poolname, condition, orderby,
				pkfield, offset, pager.getSize());
		String[] hidden = hiddenfields.split(",");
		ArrayList hiddenlist = new ArrayList();
		if(hidden != null && hidden.length > 0){
			for(int i = 0; i < hidden.length; i++){
				if(hidden[i] != null && hidden[i].trim().length() > 0)
					hiddenlist.add(hidden[i].toLowerCase());
			}
		}
		Record rdesc = new Record();
		rdesc = RecordCheck.setRecordFieldDesc(tablename, rdesc);
		request.setAttribute("rdesc", rdesc);
		request.setAttribute("hiddenlist", hiddenlist);
		request.setAttribute("records", records);
		request.setAttribute("pager", pager.getPage());
		request.setAttribute("querys", getQueryControl());
		return "list";
	}
	public String exportToExcel(){
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		if(cs == null)
			return this.operaterError("没有相应的配置信息");
		String methodName = cs.getMethodname();
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		if("sqlStat".equals(methodName))
			return exportStat();
		else
			return exportTable();
	}
	public String exportToTXT(){
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		if(cs == null)
			return this.operaterError("没有相应的配置信息");
		String methodName = cs.getMethodname();
		addFiledShowName();
		request.setAttribute("fieldslabels", fieldslabels);
		if("sqlStat".equals(methodName))
			return exportStatTxt();
		else
			return exportTableTxt();
	}
	private String exportStat() {
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		if(cs == null)
			return "list";
		String hiddenfields = cs.getHiddenfields();
		if(hiddenfields==null)
			hiddenfields = "";
		String[] hidden = hiddenfields.split(",");
		ArrayList hiddenlist = new ArrayList();
		if(hidden != null && hidden.length > 0){
			for(int i = 0; i < hidden.length; i++){
				if(hidden[i] != null && hidden[i].trim().length() > 0)
					hiddenlist.add(hidden[i].toLowerCase());
			}
		}
		String statsql = cs.getSqlstr();
		String poolname = cs.getPoolname();
		ArrayList qccs = cs.getQuerycontrols();
		if(qccs != null && qccs.size() > 0){
			QueryConditionControl qcc = null;
			String queryname = "";
			String querytype = "";
			String defaultvalue = "";
			String defaultvalue2 = "";
			String conditionstr = "";
			String conditionstr2 = "";
			for(int i=0; i < qccs.size(); i++){
				qcc = (QueryConditionControl)qccs.get(i);
				if(qcc != null){
					queryname = qcc.getName();
					if(queryname != null && queryname.trim().length() > 0){
						querytype = qcc.getQuerytype();
						if(QueryConditionControl.QUERY_TYPE_COMMON.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter(queryname));
							conditionstr = qcc.getConditionsql();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/"+queryname+"/", defaultvalue);
								statsql = statsql.replaceAll("/"+queryname+"/", conditionstr);
							}else{
								statsql = statsql.replaceAll("/"+queryname+"/", "");
							}
						}else if(QueryConditionControl.QUERY_TYPE_RANGE.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter("b_"+queryname));
							defaultvalue2 = StringToZn.toZn(request.getParameter("e_"+queryname));
							conditionstr = qcc.getConditionsql();
							conditionstr2 = qcc.getConditionsql2();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/b_"+queryname+"/", defaultvalue);
							}else
								conditionstr = "";
							if(defaultvalue2 != null && defaultvalue2.trim().length() > 0){
								conditionstr2 = conditionstr2.replaceAll("/e_"+queryname+"/", defaultvalue);
							}else
								conditionstr2 = "";
							statsql = statsql.replaceAll("/"+queryname+"/", conditionstr+conditionstr2);
						}
						
					}
				}
			}
		}
		ArrayList records = TableManage.executeQuery(poolname, statsql);
		HttpServletResponse response = this.getResponse();
		try {
			OutputStream os = response.getOutputStream();
			response.setHeader("Content-disposition","attachment; filename="+specialParam+".xls");
			response.setContentType("application/msexcel;charset=UTF-8");
			response.setHeader("Pragma","No-cache");
			response.setHeader("Cache-Control","no-cache");
			response.setDateHeader("Expires", 0);
			ExportToExcel ex = new ExportToExcel();
			HSSFWorkbook workbook = ex.getWorkbook(records, "", fieldslabels, hiddenlist);
			workbook.write(os);
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String exportTable() {
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		if(cs == null)
			return "list";
		String poolname = cs.getPoolname();
		String tablename = cs.getTablename();
		String pkfield = cs.getPkfield();
		String orderby = cs.getOrderby();
		String hiddenfields = cs.getHiddenfields();
		ContentControl c = new ContentControl();
		String condition = c.getControlSQL(this.getLoginUser(), tablename);
		ArrayList qccs = cs.getQuerycontrols();
		if(qccs != null && qccs.size() > 0){
			QueryConditionControl qcc = null;
			String queryname = "";
			String querytype = "";
			String defaultvalue = "";
			String defaultvalue2 = "";
			String conditionstr = "";
			String conditionstr2 = "";
			for(int i=0; i < qccs.size(); i++){
				qcc = (QueryConditionControl)qccs.get(i);
				if(qcc != null){
					queryname = qcc.getName();
					if(queryname != null && queryname.trim().length() > 0){
						querytype = qcc.getQuerytype();
						if(QueryConditionControl.QUERY_TYPE_COMMON.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter(queryname));
							conditionstr = qcc.getConditionsql();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/"+queryname+"/", defaultvalue);
								condition+= conditionstr;
							}
						}else if(QueryConditionControl.QUERY_TYPE_RANGE.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter("b_"+queryname));
							defaultvalue2 = StringToZn.toZn(request.getParameter("e_"+queryname));
							conditionstr = qcc.getConditionsql();
							conditionstr2 = qcc.getConditionsql2();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/b_"+queryname+"/", defaultvalue);
							}else
								conditionstr = "";
							if(defaultvalue2 != null && defaultvalue2.trim().length() > 0){
								conditionstr2 = conditionstr2.replaceAll("/e_"+queryname+"/", defaultvalue);
							}else
								conditionstr2 = "";
							condition+=  conditionstr+conditionstr2;
						}
						
					}
				}
			}
		}
		
		if(condition!= null&& condition.trim().length() > 0){
			if(condition.trim().toLowerCase().startsWith("and"))
				condition = condition.trim().substring(3);
		}
		
		TableManage tm = new TableManage();
		tm.setTableName(tablename);
		
		ArrayList records = tm.getAllRecords(poolname, condition, orderby);
		if(hiddenfields==null)
			hiddenfields = "";
		String[] hidden = hiddenfields.split(",");
		ArrayList hiddenlist = new ArrayList();
		if(hidden != null && hidden.length > 0){
			for(int i = 0; i < hidden.length; i++){
				if(hidden[i] != null && hidden[i].trim().length() > 0)
					hiddenlist.add(hidden[i].toLowerCase());
			}
		}
		HttpServletResponse response = this.getResponse();
		try {
			OutputStream os = response.getOutputStream();
			response.setHeader("Content-disposition","attachment; filename="+specialParam+".xls");
			response.setContentType("application/msexcel;charset=UTF-8");
			response.setHeader("Pragma","No-cache");
			response.setHeader("Cache-Control","no-cache");
			response.setDateHeader("Expires", 0);
			ExportToExcel ex = new ExportToExcel();
			HSSFWorkbook workbook =  ex.getWorkbookList(records, tablename, fieldslabels, hiddenlist);
			workbook.write(os);
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String exportTableTxt() {
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		if(cs == null)
			return "list";
		String poolname = cs.getPoolname();
		String tablename = cs.getTablename();
		String pkfield = cs.getPkfield();
		String orderby = cs.getOrderby();
		String hiddenfields = cs.getHiddenfields();
		ContentControl c = new ContentControl();
		String condition = c.getControlSQL(this.getLoginUser(), tablename);
		ArrayList qccs = cs.getQuerycontrols();
		if(qccs != null && qccs.size() > 0){
			QueryConditionControl qcc = null;
			String queryname = "";
			String querytype = "";
			String defaultvalue = "";
			String defaultvalue2 = "";
			String conditionstr = "";
			String conditionstr2 = "";
			for(int i=0; i < qccs.size(); i++){
				qcc = (QueryConditionControl)qccs.get(i);
				if(qcc != null){
					queryname = qcc.getName();
					if(queryname != null && queryname.trim().length() > 0){
						querytype = qcc.getQuerytype();
						if(QueryConditionControl.QUERY_TYPE_COMMON.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter(queryname));
							conditionstr = qcc.getConditionsql();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/"+queryname+"/", defaultvalue);
								condition+= conditionstr;
							}
						}else if(QueryConditionControl.QUERY_TYPE_RANGE.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter("b_"+queryname));
							defaultvalue2 = StringToZn.toZn(request.getParameter("e_"+queryname));
							conditionstr = qcc.getConditionsql();
							conditionstr2 = qcc.getConditionsql2();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/b_"+queryname+"/", defaultvalue);
							}else
								conditionstr = "";
							if(defaultvalue2 != null && defaultvalue2.trim().length() > 0){
								conditionstr2 = conditionstr2.replaceAll("/e_"+queryname+"/", defaultvalue);
							}else
								conditionstr2 = "";
							condition+=  conditionstr+conditionstr2;
						}
						
					}
				}
			}
		}
		
		if(condition!= null&& condition.trim().length() > 0){
			if(condition.trim().toLowerCase().startsWith("and"))
				condition = condition.trim().substring(3);
		}
		
		TableManage tm = new TableManage();
		tm.setTableName(tablename);
		
		ArrayList records = tm.getAllRecords(poolname, condition, orderby);
		if(hiddenfields==null)
			hiddenfields = "";
		String[] hidden = hiddenfields.split(",");
		ArrayList hiddenlist = new ArrayList();
		if(hidden != null && hidden.length > 0){
			for(int i = 0; i < hidden.length; i++){
				if(hidden[i] != null && hidden[i].trim().length() > 0)
					hiddenlist.add(hidden[i].toLowerCase());
			}
		}
		HttpServletResponse response = this.getResponse();
		response.setContentType("text/plain");// 一下两行关键的设置  
        response.addHeader("Content-Disposition",  
                "attachment;filename="+specialParam+".txt");// filename指定默认的名字  
		try {
			ExportToTXT txt = new ExportToTXT();
			txt.writeToTxtList(response, records, tablename, fieldslabels, hiddenlist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String exportStatTxt() {
		HttpServletRequest request = this.getRequest();
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		StatConfig sc = new StatConfig();
		CommonStat cs = sc.getStat(specialParam);
		if(cs == null)
			return "list";
		String hiddenfields = cs.getHiddenfields();
		if(hiddenfields==null)
			hiddenfields = "";
		String[] hidden = hiddenfields.split(",");
		ArrayList hiddenlist = new ArrayList();
		if(hidden != null && hidden.length > 0){
			for(int i = 0; i < hidden.length; i++){
				if(hidden[i] != null && hidden[i].trim().length() > 0)
					hiddenlist.add(hidden[i].toLowerCase());
			}
		}
		String statsql = cs.getSqlstr();
		String poolname = cs.getPoolname();
		ArrayList qccs = cs.getQuerycontrols();
		if(qccs != null && qccs.size() > 0){
			QueryConditionControl qcc = null;
			String queryname = "";
			String querytype = "";
			String defaultvalue = "";
			String defaultvalue2 = "";
			String conditionstr = "";
			String conditionstr2 = "";
			for(int i=0; i < qccs.size(); i++){
				qcc = (QueryConditionControl)qccs.get(i);
				if(qcc != null){
					queryname = qcc.getName();
					if(queryname != null && queryname.trim().length() > 0){
						querytype = qcc.getQuerytype();
						if(QueryConditionControl.QUERY_TYPE_COMMON.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter(queryname));
							conditionstr = qcc.getConditionsql();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/"+queryname+"/", defaultvalue);
								statsql = statsql.replaceAll("/"+queryname+"/", conditionstr);
							}else{
								statsql = statsql.replaceAll("/"+queryname+"/", "");
							}
						}else if(QueryConditionControl.QUERY_TYPE_RANGE.equals(querytype)){
							defaultvalue = StringToZn.toZn(request.getParameter("b_"+queryname));
							defaultvalue2 = StringToZn.toZn(request.getParameter("e_"+queryname));
							conditionstr = qcc.getConditionsql();
							conditionstr2 = qcc.getConditionsql2();
							if(defaultvalue != null && defaultvalue.trim().length() > 0){
								conditionstr = conditionstr.replaceAll("/b_"+queryname+"/", defaultvalue);
							}else
								conditionstr = "";
							if(defaultvalue2 != null && defaultvalue2.trim().length() > 0){
								conditionstr2 = conditionstr2.replaceAll("/e_"+queryname+"/", defaultvalue);
							}else
								conditionstr2 = "";
							statsql = statsql.replaceAll("/"+queryname+"/", conditionstr+conditionstr2);
						}
						
					}
				}
			}
		}
		ArrayList records = TableManage.executeQuery(poolname, statsql);
		HttpServletResponse response = this.getResponse();
		response.setContentType("text/plain");// 一下两行关键的设置  
        response.addHeader("Content-Disposition",  
                "attachment;filename="+specialParam+".txt");// filename指定默认的名字  
		try {
			ExportToTXT txt = new ExportToTXT();
			txt.writeToTxt(response, records, "", fieldslabels, hiddenlist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
