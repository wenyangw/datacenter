package tms.datacenter.dbmanage;

import java.util.ArrayList;
import java.util.Hashtable;

public class TableDesc {
	private String name;
	private String cnname;
	private String poolname;
	private ArrayList fieldList = new ArrayList();
	private Hashtable fieldHash = new Hashtable();
	
	public String getPoolname() {
		return poolname;
	}
	public void setPoolname(String poolname) {
		this.poolname = poolname;
	}
	
	
	public void addField(Field field){
		if(field == null)
			return;
		String fieldName = field.getFieldName();
		if(fieldName != null && fieldName.trim().length() > 0){
			fieldList.add(field);
			fieldHash.put(fieldName.toLowerCase(), field);
		}
	}
	public Field getField(String fieldName){
		if(fieldName == null || fieldName.trim().length() <= 0)
			return null;
		return (Field)fieldHash.get(fieldName.toLowerCase());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCnname() {
		return cnname;
	}
	public void setCnname(String cnname) {
		this.cnname = cnname;
	}
	public ArrayList getFieldList() {
		return fieldList;
	}
	public void setFieldList(ArrayList fieldList) {
		this.fieldList = fieldList;
	}
	public Hashtable getFieldHash() {
		return fieldHash;
	}
	public void setFieldHash(Hashtable fieldHash) {
		this.fieldHash = fieldHash;
	}
}

