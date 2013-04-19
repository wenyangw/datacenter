package tms.datacenter.sysmanage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import tms.datacenter.dbmanage.DBConfig;
import tms.datacenter.dbmanage.DataBase;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;

public class SysParam extends TableManage{
	public static final int PARAM_TYPE_SYS=0;
	public static final int PARAM_TYPE_ORG=1;
	public static final int PARAM_TYPE_DEPARTMENT=2;
	private ArrayList paramtypes = new ArrayList();
	public SysParam(){
		setTableName("dc_sys_param"); 
	}
	
	public  ArrayList getParamtypes(){
		ArrayList al = new ArrayList();
		Record r = new Record();
		r.set("type", ""+PARAM_TYPE_ORG);
		r.set("name", "机构单位");
		paramtypes.add(r);
		r = new Record();
		r.set("type", ""+PARAM_TYPE_DEPARTMENT);
		r.set("name", "部门");
		paramtypes.add(r);
		loadDBConfig();
		return paramtypes;
	}
	private void loadDBConfig() {
		try {
			String cpath = java.net.URLDecoder.decode(DBConfig.class.getResource("/").getPath()+ File.separatorChar + "paramtype.xml", "gb2312");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(cpath);
			Element root = doc.getRootElement();
			Iterator databases = root.elementIterator("paramtype");
			DataBase database = null;
			Element etype = null;
			String name = "";
			String type = "";
			Record r = null;
			while (databases.hasNext()) {
				etype = (Element) databases.next();
				name = getAttribute(etype, "name");
				type = getAttribute(etype, "type");
				r = new Record();
				r.set("type", type);
				r.set("name", name);
				paramtypes.add(r);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getAttribute(Element e, String an) {
		Attribute attribute = e.attribute(an);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}
}
