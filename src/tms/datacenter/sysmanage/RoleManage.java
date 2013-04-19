package tms.datacenter.sysmanage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;

import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.ConnectionManage;
import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;

public class RoleManage extends TableManage {
	public RoleManage() {
		setTableName("dc_role");
	}
	public ArrayList getUserRole(String poolName,String loginName){
		if(poolName == null || poolName.trim().length() <= 0)
			return null;
		if(loginName == null || loginName.trim().length() <= 0)
			return null;
		try{
			String sql="select * from dc_role_in_user where loginname='"+StringToZn.toDB(loginName)+"'";
			return this.executeQuery(poolName, sql);
		}catch(Exception e){
			
		}
		return null;
	}
	public Hashtable getUserPrivilege(Connection conn, String loginName) {
		String sql = "select * from dc_role_privilege where rolecode in ("
				+ "select rolecode from dc_role_in_user where loginname='"
				+ loginName + "')";
		ArrayList al = this.executeQuery(conn, sql);
		Hashtable user_privilege = new Hashtable();
		if (al != null && al.size() > 0) {
			String actionClass = "";
			String moduleid = "";
			String privilegeValue = "0";
			String privilegeValue_exist = "0";
			Record r = null;
			int i_pvalue = 0;
			for (int i = 0; i < al.size(); i++) {
				r = (Record) al.get(i);
				actionClass = r.get("actionclass");
				moduleid = r.get("moduleid");
				if(moduleid == null)
					moduleid = "";
				privilegeValue = r.get("privilegevalue");
				if (!privilegeValue.matches("\\d+"))
					privilegeValue = "0";
				if (actionClass != null && actionClass.trim().length() > 0) {
					privilegeValue_exist = (String) user_privilege
							.get(actionClass+"/"+moduleid);
					if (privilegeValue_exist== null|| !privilegeValue_exist.matches("\\d+"))
						privilegeValue_exist = "0";
					int newvalue = Integer.parseInt(privilegeValue_exist)
							| Integer.parseInt(privilegeValue);
					user_privilege.put(actionClass+"/"+moduleid, "" + newvalue);
				}
			}
		}
		return user_privilege;
	}

	public Hashtable getUserPrivilege(String poolName, String loginName) {
		String sql = "select * from dc_role_privilege where rolecode in ("
				+ "select rolecode from dc_role_in_user where loginname='"
				+ loginName + "')";
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			ArrayList al = this.executeQuery(conn, sql);
			Hashtable user_privilege = new Hashtable();
			if (al != null && al.size() > 0) {
				String actionClass = "";
				String moduleid = "";
				String privilegeValue = "0";
				String privilegeValue_exist = "0";
				Record r = null;
				int i_pvalue = 0;
				for (int i = 0; i < al.size(); i++) {
					r = (Record) al.get(i);
					actionClass = r.get("actionclass");
					moduleid = r.get("moduleid");
					if(moduleid == null)
						moduleid = "";
					privilegeValue = r.get("privilegevalue");
					if (!privilegeValue.matches("\\d+"))
						privilegeValue = "0";
					if (actionClass != null && actionClass.trim().length() > 0) {
						privilegeValue_exist = (String) user_privilege
								.get(actionClass+"/"+moduleid);
						if (!privilegeValue_exist.matches("\\d+"))
							privilegeValue_exist = "0";
						int newvalue = Integer.parseInt(privilegeValue)
								| Integer.parseInt(privilegeValue);
						user_privilege.put(actionClass+"/"+moduleid, "" + newvalue);
					}
				}
			}
			return user_privilege;
		} catch (Exception e) {

		}finally{
			cm.freeConnection("datacenter", conn);
		}
		return null;
	}

	public Hashtable getRolePrivilege(String poolName, String roleCode) {
		if (poolName == null || poolName.trim().length() <= 0)
			return null;
		String sql = "select * from dc_role_privilege where rolecode = '"
				+ roleCode + "'";
		ArrayList al = this.executeQuery(poolName, sql);
		Hashtable role_privilege = new Hashtable();
		if (al != null && al.size() > 0) {
			String actionClass = "";
			String moduleid = "";
			String privilegeValue = "0";
			Record r = null;
			for (int i = 0; i < al.size(); i++) {
				r = (Record) al.get(i);
				actionClass = r.get("actionclass");
				moduleid = r.get("moduleid");
				if(moduleid == null)
					moduleid = "";
				privilegeValue = r.get("privilegevalue");
				if (!privilegeValue.matches("\\d+"))
					privilegeValue = "0";
				if (actionClass != null && actionClass.trim().length() > 0) {
					role_privilege.put(actionClass+"/"+moduleid, privilegeValue);
				}
			}
		}
		return role_privilege;
	}

	public ArrayList getAllPrivilege() {
		PrivilegeConfig pc = PrivilegeConfig.getInstance();
		return pc.getModules();
	}

	public int getActionPrivilegeValue(String[] values) {
		String strValue;
		int totalValue = 0;
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				strValue = values[i];
				if (!strValue.matches("\\d+")) {
					continue;
				}
				int intValue = Integer.parseInt(strValue);
				if (intValue == 1 || intValue % 2 == 0)
					totalValue = totalValue | intValue;
			}
		}
		return totalValue;
	}
	public static String paraUserOperationToButton(ArrayList userOperations,String showpos){
		if(userOperations == null || userOperations.size() <= 0)
			return "";
		if(showpos == null || showpos.trim().length() <= 0)
			return "";
		String button_str = "";
		Operation o = null;
		String o_showpos = "";
		String name = "";
		String cnname = "";
		String specialparam = "";
		String needconfirm = "";
		String selectcount = "";
		for(int i = 0; i < userOperations.size(); i++){
			o = (Operation)userOperations.get(i);
			if(o != null){
				o_showpos = o.getShowpos();
				name = o.getName();
				cnname = o.getCnname();
				specialparam = o.getSpecialparam();
				needconfirm = o.getNeedconfirm();
				selectcount = o.getSelectcount();
				if(showpos.equals(o_showpos)){
					button_str+="<input type=\"button\" value=\""+cnname+"\" onclick=\"commonOperate('"+name+"','"+specialparam+"','"+cnname+"','"+needconfirm+"',"+selectcount+")\">&nbsp;";
				}
			}
		}
		return button_str;
	}
	public boolean hasPrivilege(Record loginUser,String actionClass,String method,String specialParam,String moduleid){
		if(loginUser ==null)
			return false;
		if("admin".equals(loginUser.get("loginname")))
			return true;
		Hashtable user_privilege = null;
		int operation_pri = getOperationPri(actionClass,method,specialParam,moduleid);
		if(operation_pri > 0 && (operation_pri == 1 || operation_pri%2==0)){
			user_privilege = loginUser.getUserPrivilege();
			String value = (String)user_privilege.get(actionClass+"/"+moduleid);
			if(value != null && value.matches("\\d+")){
				int lastValue = operation_pri&(Integer.parseInt(value));
				if(lastValue > 0)
					return true;
			}else{
				return false;
			}
		}
		return false;
	}
	private int getOperationPri(String actionClass,String method,String specialParam,String moduleid){
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
				tm = (TopModule)topmodule.get(i);
				modules = tm.getModules();
				Module m = null;
				if(modules != null && modules.size() > 0){
					m = (Module)modules.get(actionClass+"/"+moduleid);
					Operation o = null;
					Hashtable operations = null;
					if(m != null){
						operations = m.getOperations();
						o = (Operation)operations.get(method+"·Ö¸ô·û"+specialParam.trim());
						return o.getPrivilegevalue();
					}
				}
			}
		}
		return 0;
	}
	public static void main(String[] args) {

	}
}
