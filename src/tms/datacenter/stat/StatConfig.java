package tms.datacenter.stat;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import tms.datacenter.commontools.QueryConditionControl;
import tms.datacenter.dbmanage.ConnectionManage;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;

public class StatConfig {
	public static final String OPTION_VALUE_FIELD="optionvalue";
	public static final String OPTION_TEXT_FIELD="optiontext";
	private ArrayList stats = new ArrayList();
	private Hashtable stats_hash = new Hashtable();
	public StatConfig() {
		loadStatConfig();
	}

	public ArrayList getStats() {
		return stats;
	}
	public CommonStat getStat(String specialparam){
		if(specialparam == null || specialparam.trim().length() <= 0)
			return null;
		return (CommonStat)stats_hash.get(specialparam);
	}
	private void loadStatConfig() {
		try {
			String cpath = java.net.URLDecoder.decode(StatConfig.class
					.getResource("/").getPath()
					+ File.separatorChar + "statconfig.xml", "gb2312");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(cpath);
			Element root = doc.getRootElement();
			Iterator statsit = root.elementIterator("stat");
			CommonStat cs = null;
			QueryConditionControl qcc = null;
			String cnname = "";
			String specialparam = "";
			String poolname = "";
			String sqlstr = "";
			String tablename="";
			String hiddenfields = "";
			String exporthidden = "";
			String pkfield = "";
			String methodname="";
			String orderby = "";
			while (statsit.hasNext()) {
				cs = new CommonStat();
				Element cs_e = (Element) statsit.next();
				cnname = getAttribute(cs_e, "cnname");
				specialparam = getAttribute(cs_e, "specialparam");
				if(specialparam == null|| specialparam.trim().length() <= 0)
					continue;
				poolname = getAttribute(cs_e, "poolname");
				sqlstr = getAttribute(cs_e, "sqlstr");
				tablename = getAttribute(cs_e, "tablename");
				hiddenfields = getAttribute(cs_e, "hiddenfields");
				exporthidden = getAttribute(cs_e, "exporthidden");
				pkfield = getAttribute(cs_e, "pkfield");
				methodname = getAttribute(cs_e, "methodname");
				orderby = getAttribute(cs_e, "orderby");
				cs.setCnname(cnname);
				cs.setPoolname(poolname);
				cs.setSpecialparam(specialparam);
				cs.setSqlstr(sqlstr);
				cs.setHiddenfields(hiddenfields);
				cs.setExporthidden(exporthidden);
				cs.setMethodname(methodname);
				cs.setPkfield(pkfield);
				cs.setTablename(tablename);
				cs.setOrderby(orderby);
				Iterator controlsit = cs_e.elementIterator("querycontrol");
				while (controlsit.hasNext()) {
					qcc = new QueryConditionControl();
					Element qcc_e = (Element) controlsit.next();
					qcc.setControltype(getAttribute(qcc_e, "type"));
					qcc.setName(getAttribute(qcc_e, "name"));
					qcc.setLabel(getAttribute(qcc_e, "label"));
					qcc.setQuerytype(getAttribute(qcc_e, "querytype"));
					qcc.setOptionSql(getAttribute(qcc_e, "optionSql"));
					qcc.setOptionPoolname(getAttribute(qcc_e,
									"optionPoolname"));
					qcc.setConditionsql(getAttribute(qcc_e, "conditionsql"));
					qcc.setConditionsql2(getAttribute(qcc_e, "conditionsql2"));
					qcc.setIsshow(getAttribute(qcc_e, "isshow"));
					qcc.setListfield(getAttribute(qcc_e, "listfield"));
					qcc = addOptions(qcc);
					Iterator optionsit = qcc_e.elementIterator("option");
					while (optionsit != null && optionsit.hasNext()) {
						Element option_e = (Element) optionsit.next();
						qcc.addOptions(getAttribute(option_e, "value"),
								getAttribute(option_e, "text"));
					}
					cs.addQueryControl(qcc);
				}
				stats.add(cs);
				stats_hash.put(specialparam, cs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private QueryConditionControl addOptions(QueryConditionControl qcc) {
		if (qcc != null) {
			String optionsql = qcc.getOptionSql();
			String poolName=qcc.getOptionPoolname();
			if (optionsql == null || optionsql.trim().length() <= 0 || poolName == null || poolName.trim().length() <= 0) 
				return qcc;
			ArrayList records = TableManage.executeQuery(poolName, optionsql);
			if(records != null && records.size() > 0){
				Record r = null;
				String value = "";
				String text = "";
				for(int i = 0;i < records.size(); i++){
					r = (Record)records.get(i);
					value = r.get(OPTION_VALUE_FIELD);
					text = r.get(OPTION_TEXT_FIELD);
					qcc.addOptions(value, text);
				}
			}
		}
		return qcc;
	}

	private String getAttribute(Element e, String an) {
		Attribute attribute = e.attribute(an);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	public static void main(String[] args) {

	}
}
