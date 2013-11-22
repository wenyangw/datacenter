package tms.datacenter.upload;

import java.util.ArrayList;
import java.util.Hashtable;


public class UploadMsg {
	private String cnname="";
	private String specialparam = "";
	private String txtseparate = "";
	private String keywords = "";
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	private String table = "";
	private ArrayList columnList = new ArrayList();
	private Hashtable columnHash = new Hashtable();
	
	public void addColumn(ColumnMsg cm){
		if(cm == null)
			return;
		int number = cm.getNumber();
		columnList.add(cm);
		columnHash.put(""+number, cm);
		
	}
	public ColumnMsg getColumn(int number){
		if(number < 0)
			return null;
		return (ColumnMsg)columnHash.get(""+number);
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
	public ArrayList getColumnList() {
		return columnList;
	}
	public void setColumnList(ArrayList columnList) {
		this.columnList = columnList;
	}
	public Hashtable getColumnHash() {
		return columnHash;
	}
	public void setColumnHash(Hashtable columnHash) {
		this.columnHash = columnHash;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getTxtseparate() {
		return txtseparate;
	}
	public void setTxtseparate(String txtseparate) {
		this.txtseparate = txtseparate;
	}
}
