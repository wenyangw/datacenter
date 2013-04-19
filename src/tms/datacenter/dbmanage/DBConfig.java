package tms.datacenter.dbmanage;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DBConfig {
	private Hashtable<String, DataBase> databases = new Hashtable<String, DataBase>();

	public DBConfig() {
		loadDBConfig();
	}
	private void loadDBConfig() {
		try {
			String cpath = java.net.URLDecoder.decode(DBConfig.class.getResource("/").getPath()+ File.separatorChar + "dbconfig.xml", "gb2312");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(cpath);
			Element root = doc.getRootElement();
			Iterator databases = root.elementIterator("database");
			DataBase database = null;
			while (databases.hasNext()) {
				database = new DataBase();
				Element db = (Element) databases.next();
				String dbname = getAttribute(db, "dbname");
				String dbtype = getAttribute(db, "dbtype");
				String driver = getAttribute(db, "driver");
				String user = getAttribute(db, "user");
				String password = getAttribute(db, "password");
				String connstr = getAttribute(db, "connstr");
				String maxcount = getAttribute(db, "maxcount");
				database.setDbname(dbname);
				database.setDbtype(dbtype);
				database.setDriver(driver);
				database.setUser(user);
				database.setPassword(password);
				database.setConnstr(connstr);
				if (!maxcount.matches("\\d+"))
					maxcount = "0";
				database.setMaxcount(Integer.parseInt(maxcount));
				this.databases.put(dbname, database);
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

	private void setDatabases(Hashtable<String, DataBase> databases) {
		this.databases = databases;
	}

	public Hashtable<String, DataBase> getDatabases() {
		return databases;
	}
	public static void main(String[] args){
		DBConfig dbconfig = new DBConfig();
		Hashtable<String, DataBase> databases = dbconfig.getDatabases();
		DataBase db = databases.get("datacenter");
		System.out.println(db.getDbname());
		System.out.println(db.getDbtype());
		System.out.println(db.getDriver());
		System.out.println(db.getConnstr());
		System.out.println(db.getUser());
		System.out.println(db.getPassword());
		System.out.println(db.getMaxcount());
		
	}
}
