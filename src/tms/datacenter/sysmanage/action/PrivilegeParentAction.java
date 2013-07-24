package tms.datacenter.sysmanage.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.RecordCheck;
import tms.datacenter.dbmanage.TableManage;
import tms.datacenter.sysmanage.Module;
import tms.datacenter.sysmanage.OperateLog;
import tms.datacenter.sysmanage.Operation;
import tms.datacenter.sysmanage.PrivilegeConfig;
import tms.datacenter.sysmanage.RoleManage;
import tms.datacenter.sysmanage.TopModule;

import com.opensymphony.xwork2.ActionSupport;

public class PrivilegeParentAction extends ActionSupport{
	
	public Hashtable fieldslabels = new Hashtable();
	private String log_str = "";
	@Override
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest (); 
		String methodName=StringToZn.toZn(request.getParameter("methodName"));
		String specialParam=StringToZn.toZn(request.getParameter("specialParam"));
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		Record dcuser = getLoginUser();
		log_str = "";
		int privilegeValue = getOperationPrivilegeValue(methodName,specialParam,moduleid);
		if(privilegeValue <= 0 || (privilegeValue != 1 && privilegeValue%2!=0)){
			Class c = getClass();
			Method method = c.getMethod(methodName, null);
			request.setAttribute("uo", getAllOperations());
			return (String)method.invoke(c.newInstance(), null);
		}
		if(dcuser == null)
			return "nopower";
		if("admin".equalsIgnoreCase(dcuser.get("loginname"))){
			Class c = getClass();
			Method method = c.getMethod(methodName, null);
			request.setAttribute("uo", getAllOperations());
			if(privilegeValue > 1){
				OperateLog log = new OperateLog();
				log.AddLog(dcuser.get("loginname"), log_str, "");
			}
			return (String)method.invoke(c.newInstance(), null);
		}
		int lastValue = 1;
		Hashtable user_privilege = null;
		if(privilegeValue > 0 && (privilegeValue == 1 || privilegeValue%2==0)){
			user_privilege = dcuser.getUserPrivilege();
			String value = (String)user_privilege.get(this.getClass().getName()+"/"+moduleid);
			if(value != null && value.matches("\\d+")){
				lastValue = privilegeValue&(Integer.parseInt(value));
			}else{
				lastValue=0;
			}
		}if(user_privilege == null)
			user_privilege = new Hashtable();
		if(lastValue > 0){
			Class c = getClass();
			Method method = c.getMethod(methodName, null);
			request.setAttribute("uo", getUserHasOperations(user_privilege));
			if(privilegeValue > 1){
				OperateLog log = new OperateLog();
				log.AddLog(dcuser.get("loginname"), log_str, "");
			}
			return (String)method.invoke(c.newInstance(), null);
		}else{
			//无权限提示
			return "nopower";
		}
    }
	public String detail(){
		TableManage tm = getDetailClass();
		if(tm == null)
			return "detail";
		String pkfieldvalue = this.getRequest().getParameter("pkfield");
		String poolName = getDetailPoolName();
		Field pkfield = getDetailPKField();
		if(pkfield == null)
			return "detail";
		String fieldtype = pkfield.getFieldType();
		String fieldName = pkfield.getFieldName();
		if(fieldName == null || fieldName.trim().length() <= 0)
			return "detail";
		String condition = "";
		if(Field.FIELD_TYPE_INT.equals(fieldtype)||Field.FIELD_TYPE_DOUBLE.equals(fieldtype))
			condition = fieldName+"="+pkfieldvalue;
		else
			condition = fieldName+"='"+StringToZn.toDB(pkfieldvalue)+"'";
		if(condition == null || condition.trim().length() <= 0)
			return "detail";
		ArrayList records = tm.getAllRecords(poolName, condition, "");
		if(records != null && records.size() > 0){
			Record r = (Record)records.get(0);
			r = RecordCheck.setRecordFieldDesc(tm.getTableName(), r);
			this.getRequest().setAttribute("record",r);
			addFiledShowName();
			this.getRequest().setAttribute("fieldslabels", fieldslabels);
		}
		return "detail";
	}
	public Field getDetailPKField(){
		return null;
	}
	public String getDetailPoolName(){
		return "";
	}
	public void addFiledShowName(){
		
	}
	public TableManage getDetailClass(){
		return null;
	}
	public void addFieldParameters(String fieldName,String fieldValue,String fieldLabel){
		if(fieldName == null || fieldName.trim().length() <= 0){
			return;
		}
		fieldName = fieldName.toLowerCase();
		Hashtable ht = (Hashtable)fieldslabels.get(fieldName);
		if(ht == null)
			ht = new Hashtable();
		ht.put(fieldValue, fieldLabel);
		if(fieldValue != null && fieldValue.trim().length() > 0)
			fieldslabels.put(fieldName, ht);
	}
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	public HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	public HttpSession getSession(){
		return ServletActionContext.getRequest().getSession(true);
	}
	public Record getLoginUser(){
		HttpSession session = this.getSession();
		Record dcuser = (Record)session.getAttribute("dcuser");
		return dcuser;
	}
	private int getOperationPrivilegeValue(String method,String specialParam,String moduleid){
		log_str = "";
		String actionClass = this.getClass().getName();
		//System.out.print(actionClass);
		if(actionClass == null || actionClass.trim().length() <= 0)
			return 0;
		if(method == null || method.trim().length() <= 0)
			return 0;
		if(moduleid == null)
			moduleid = "";
		if(specialParam == null)
			specialParam = "";
		PrivilegeConfig pc = PrivilegeConfig.getInstance();
		
		ArrayList topmodule = pc.getModules();
		if(topmodule != null && topmodule.size() > 0){
			TopModule tm = null;
			Hashtable modules = null;
			for(int i = 0; i < topmodule.size(); i++){
				log_str = "";
				tm = (TopModule)topmodule.get(i);
				modules = tm.getModules();
				Module m = null;
				if(modules != null && modules.size() > 0){
					m = (Module)modules.get(actionClass+"/"+moduleid);
					Operation o = null;
					Hashtable operations = null;
					if(m != null){
						operations = m.getOperations();
						o = (Operation)operations.get(method+"分隔符"+specialParam.trim());
						log_str = tm.getName()+"--"+m.getName()+"--"+o.getCnname();
						return o.getPrivilegevalue();
					}
				}
			}
		}
		return 0;
	}
	private ArrayList getUserHasOperations(Hashtable user_privilege){
		String actionClass = this.getClass().getName();
		String moduleid = StringToZn.toZn(this.getRequest().getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		//System.out.print(actionClass);
		if(actionClass == null || actionClass.trim().length() <= 0)
			return null;
		String value = (String)user_privilege.get(this.getClass().getName()+"/"+moduleid);
		int user_pvalue = 0;
		if(value != null && value.matches("\\d+")){
			user_pvalue = Integer.parseInt(value);
		}
		ArrayList userHasOperations = new ArrayList();
		PrivilegeConfig pc = PrivilegeConfig.getInstance();
		ArrayList topmodule = pc.getModules();
		if(topmodule != null && topmodule.size() > 0){
			TopModule tm = null;
			Hashtable modules = null;
			for(int i = 0; i < topmodule.size(); i++){
				tm = (TopModule)topmodule.get(i);
				modules = tm.getModules();
				Module m = null;
				if(modules != null && modules.size() > 0){
					m = (Module)modules.get(actionClass+"/"+moduleid);
					Operation o = null;
					ArrayList operations = null;
					if(m != null){
						operations = m.getOperationsList();
						if(operations != null && operations.size() > 0){
							for(int j = 0; j < operations.size(); j++){
								o = (Operation)operations.get(j);
								int privilege = o.getPrivilegevalue();
								if((user_pvalue&privilege) > 0)
									userHasOperations.add(o);
							}
						}
					}
				}
			}
		}
		return userHasOperations;
	}
	private ArrayList getAllOperations(){
		String actionClass = this.getClass().getName();
		String specialParam=StringToZn.toZn(this.getRequest().getParameter("specialParam"));
		//System.out.print(actionClass);
		if(actionClass == null || actionClass.trim().length() <= 0)
			return null;
		String moduleid = StringToZn.toZn(this.getRequest().getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		ArrayList userHasOperations = new ArrayList();
		PrivilegeConfig pc = PrivilegeConfig.getInstance();
		ArrayList topmodule = pc.getModules();
		if(topmodule != null && topmodule.size() > 0){
			TopModule tm = null;
			Hashtable modules = null;
			for(int i = 0; i < topmodule.size(); i++){
				tm = (TopModule)topmodule.get(i);
				modules = tm.getModules();
				Module m = null;
				if(modules != null && modules.size() > 0){
					m = (Module)modules.get(actionClass+"/"+moduleid);
					
					Operation o = null;
					ArrayList operations = null;
					if(m != null){
						operations = m.getOperationsList();
						if(operations != null && operations.size() > 0){
							for(int j = 0; j < operations.size(); j++){
								o = (Operation)operations.get(j);
								if(specialParam.equals(o.getSpecialparam().trim()))
									userHasOperations.add(o);
							}
						}
					}
				}
			}
		}
		return userHasOperations;
	}
	public String operaterError(String errorMsg){
		getRequest().setAttribute("errorMsg", errorMsg);
		return "error";
	}
	public void setReturnAction(String action){
		getRequest().setAttribute("returnAction", action);
	}
	public void setPromptMsg(String msg){
		getRequest().setAttribute("prompt", msg);
	}
	public void setReturnParams(Hashtable params){
		getRequest().setAttribute("params", params);
	}
	
}
