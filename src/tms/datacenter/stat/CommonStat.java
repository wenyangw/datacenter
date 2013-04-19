package tms.datacenter.stat;

import java.util.ArrayList;
import java.util.Hashtable;

import tms.datacenter.commontools.QueryConditionControl;


public class CommonStat {
	private String cnname;
	private String specialparam;
	private String poolname;
	private String sqlstr;
	private String tablename;
	private String hiddenfields;
	private String pkfield;
	private String methodname;
	private String orderby;
	public String getOrderby() {
		return orderby;
	}
	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	private ArrayList querycontrols = null;
	
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getHiddenfields() {
		return hiddenfields;
	}
	public void setHiddenfields(String hiddenfields) {
		this.hiddenfields = hiddenfields;
	}
	public String getPkfield() {
		return pkfield;
	}
	public void setPkfield(String pkfield) {
		this.pkfield = pkfield;
	}
	
	public ArrayList getQuerycontrols() {
		return querycontrols;
	}
	public void addQueryControl(QueryConditionControl qcc){
		if(qcc == null)
			return;
		if(querycontrols==null)
			querycontrols = new ArrayList();
		querycontrols.add(qcc);
	}
	public String getCnname() {
		return cnname;
	}
	public void setCnname(String cnname) {
		this.cnname = cnname;
	}
	public String getSpecialparam() {
		return specialparam;
	}
	public void setSpecialparam(String specialparam) {
		this.specialparam = specialparam;
	}
	public String getPoolname() {
		return poolname;
	}
	public void setPoolname(String poolname) {
		this.poolname = poolname;
	}
	public String getSqlstr() {
		return sqlstr;
	}
	public void setSqlstr(String sqlstr) {
		this.sqlstr = sqlstr;
	}
	public static void main(String[] args){
		
	}
}
