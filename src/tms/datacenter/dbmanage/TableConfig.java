package tms.datacenter.dbmanage;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TableConfig {
	private ArrayList tableslist = new ArrayList();
	private Hashtable tableshash = new Hashtable();
	public static TableConfig tc = null;
	
	public static TableConfig getInstance(){
		if(tc == null)
			tc = new TableConfig();
		return tc;
	}
	private TableConfig() {
		loadPrivilegeConfig();
	}
	private void loadPrivilegeConfig() {
		try {
			String cpath = java.net.URLDecoder.decode(TableConfig.class.getResource("/").getPath()+ File.separatorChar + "tabledesc.xml", "gb2312");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(cpath);
			Element root = doc.getRootElement();
			Iterator tablesit = root.elementIterator("table");
			TableDesc table = null;
			Field field = null;
			while (tablesit.hasNext()) {
				table = new TableDesc();
				Element e_table = (Element) tablesit.next();
				String tname = getAttribute(e_table, "name");
				String cnname = getAttribute(e_table, "cnname");
				String poolname = getAttribute(e_table, "poolname");
				table.setName(tname);
				table.setCnname(cnname);
				table.setPoolname(poolname);
				Iterator fieldit = e_table.elementIterator("field");
				while (fieldit.hasNext()) {
					field = new Field();
					Element e_field = (Element) fieldit.next();
					String fieldname = getAttribute(e_field, "fieldname");
					String fieldlabel = getAttribute(e_field, "fieldlabel");
					String fieldtype = getAttribute(e_field, "fieldtype");
					String fieldlength = getAttribute(e_field, "fieldlength");
					String ispk = getAttribute(e_field, "ispk");
					String cannull = getAttribute(e_field, "cannull");
					String showpos = getAttribute(e_field, "showpos");
					
					if(fieldname== null || fieldname.trim().length() <= 0)
						continue;
					if(fieldlength== null || !fieldlength.matches("\\d+"))
						fieldlength = "50";
					field.setCannull(cannull);
					field.setFieldLabel(fieldlabel);
					field.setFieldLength(Integer.parseInt(fieldlength));
					field.setFieldName(fieldname);
					field.setFieldType(fieldtype);
					if("1".equals(ispk))
					{
						field.setIspk(Field.IS_PK_YES);
					}else{
						field.setIspk(Field.IS_PK_NO);
					}
					field.setShowpos(showpos);
					table.addField(field);
				}
				addTable(table);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void addTable(TableDesc table){
		if(table == null)
			return;
		String tableName = table.getName();
		if(tableName != null && tableName.trim().length() > 0){
			tableslist.add(table);
			tableshash.put(tableName.toLowerCase(), table);
		}
	}
	public TableDesc getTable(String tableName){
		if(tableName == null || tableName.trim().length() <= 0)
			return null;
		return (TableDesc)tableshash.get(tableName.toLowerCase());
	}
	private String getAttribute(Element e, String an) {
		Attribute attribute = e.attribute(an);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	public ArrayList getTableslist() {
		return tableslist;
	}
	private void setTableslist(ArrayList tableslist) {
		this.tableslist = tableslist;
	}
	public Hashtable getTableshash() {
		return tableshash;
	}
	private void setTableshash(Hashtable tableshash) {
		this.tableshash = tableshash;
	}
	public static void main(String[] args){
		
	}
}
