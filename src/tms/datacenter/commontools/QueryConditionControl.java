package tms.datacenter.commontools;

import java.util.ArrayList;
import java.util.Hashtable;

public class QueryConditionControl {
	public static final String DATA_TYPE_NUMBER = "0";
	public static final String DATA_TYPE_TEXT = "1";
	public static final String DATA_TYPE_DATE = "2";

	public static final String QUERY_TYPE_COMMON = "1";
	public static final String QUERY_TYPE_RANGE = "2";
	public static final String CONTROL_TYPE_TEXT = "text";
	public static final String CONTROL_TYPE_SELECT = "select";
	public static final String CONTROL_TYPE_DATE = "date";
	
	public static final String IS_SHOW_YES = "1";
	public static final String IS_SHOW_NO = "0";

	private String name;
	private String value;
	private String value2;
	private String label;
	private String datatype;
	private String querytype;
	private String controltype = CONTROL_TYPE_TEXT;
	private ArrayList valuelist;
	private Hashtable texthash;
	private String optionSql;
	private String optionPoolname;
	private String conditionsql;
	private String conditionsql2;//∑∂Œß≤È—Ø π”√
	private String isshow;
	private String listfield;
	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

	public String getListfield() {
		return listfield;
	}

	public void setListfield(String listfield) {
		this.listfield = listfield;
	}

	public String getConditionsql2() {
		return conditionsql2;
	}

	public void setConditionsql2(String conditionsql2) {
		this.conditionsql2 = conditionsql2;
	}

	public String getConditionsql() {
		return conditionsql;
	}

	public void setConditionsql(String conditionsql) {
		this.conditionsql = conditionsql;
	}

	public String getOptionPoolname() {
		return optionPoolname;
	}

	public void setOptionPoolname(String optionPoolname) {
		this.optionPoolname = optionPoolname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getQuerytype() {
		return querytype;
	}

	public void setQuerytype(String querytype) {
		this.querytype = querytype;
	}

	public String getControltype() {
		return controltype;
	}

	public void setControltype(String controltype) {
		this.controltype = controltype;
	}

	public Hashtable getTexthash() {
		return texthash;
	}

	public void setTexthash(Hashtable texthash) {
		this.texthash = texthash;
	}

	public String getOptionSql() {
		return optionSql;
	}

	public void setOptionSql(String optionSql) {
		this.optionSql = optionSql;
	}

	public ArrayList getValuelist() {
		return valuelist;
	}

	
	public QueryConditionControl() {

	}

	public QueryConditionControl(String name, String label,
			String querytype, String controltype) {
		this.name = name;
		this.label = label;
		//this.datatype = datatype;
		this.querytype = querytype;
		this.controltype = controltype;
	}

	public void addOptions(String value, String text) {
		if (value == null || value.trim().length() <= 0)
			return;
		if (texthash == null)
			texthash = new Hashtable();
		if (valuelist == null)
			valuelist = new ArrayList();
		valuelist.add(value);
		texthash.put(value, text);
	}

	public void setDefaultValue(String value) {
		this.value = value;
	}

	public void setDefaultRangeValue(String value, String value2) {
		this.value = value;
		this.value2 = value2;
	}

	public String getControlStr() {
		String s = "";
		if (QUERY_TYPE_COMMON.equals(querytype)) {
			if (CONTROL_TYPE_TEXT.equalsIgnoreCase(controltype)) {
				s = label + "<input type=\"text\" class=\"querytext\" name=\""
						+ name + "\" value=\"" + value + "\">";
			} else if (CONTROL_TYPE_DATE.equalsIgnoreCase(controltype)) {
				s = label + "<input type=\"text\" class=\"querydate\" name=\""
						+ name + "\" value=\"" + value
						+ "\" onclick=\"calendar();\">";
			} else if (CONTROL_TYPE_SELECT.equalsIgnoreCase(controltype)) {
				s = label + "<select class=\"queryselect\" name=\"" + name
						+ "\"><option value=\"\"></option>";
				if (valuelist != null && valuelist.size() > 0) {
					String value_str = "";
					String text_str = "";
					for (int i = 0; i < valuelist.size(); i++) {
						value_str = (String) valuelist.get(i);
						text_str = (String) texthash.get(value_str);
						if (text_str == null || text_str.trim().length() <= 0)
							text_str = value_str;
						if (value_str.equals(value))
							s += "<option value=\"" + value_str
									+ "\" selected>" + text_str + "</option>";
						else
							s += "<option value=\"" + value_str + "\">"
									+ text_str + "</option>";
					}
				}
				s += "<select>";
			}
		} else if (QUERY_TYPE_RANGE.equals(querytype)) {
			if (CONTROL_TYPE_TEXT.equalsIgnoreCase(controltype)) {
				s = label
						+ "<input type=\"text\" class=\"querytext\" name=\"b_"
						+ name + "\" value=\"" + value + "\">-";
				s += "<input type=\"text\" class=\"querytext\" name=\"e_"
						+ name + "\" value=\"" + value + "\">";
			} else if (CONTROL_TYPE_DATE.equalsIgnoreCase(controltype)) {
				s = label + "<input type=\"text\" class=\"querydate\" name=\""
						+ name + "\" value=\"" + value
						+ "\" onclick=\"calendar();\">-";
				s += "<input type=\"text\" class=\"querydate\" name=\"" + name
						+ "\" value=\"" + value + "\" onclick=\"calendar();\">";
			} else if (CONTROL_TYPE_SELECT.equalsIgnoreCase(controltype)) {
				s = label + "<select name=\"b_" + name
						+ "\"><option value=\"\"></option>";
				if (valuelist != null && valuelist.size() > 0) {
					String value_str = "";
					String text_str = "";
					for (int i = 0; i < valuelist.size(); i++) {
						value_str = (String) valuelist.get(i);
						text_str = (String) texthash.get(value_str);
						if (text_str == null || text_str.trim().length() <= 0)
							text_str = value_str;
						if (value_str.equals(value))
							s += "<option value=\"" + value_str
									+ "\" selected>" + text_str + "</option>";
						else
							s += "<option value=\"" + value_str + "\">"
									+ text_str + "</option>";
					}
				}
				s += "<select>-";
				s += "<select name=\"e_" + name
						+ "\"><option value=\"\"></option>";
				if (valuelist != null && valuelist.size() > 0) {
					String value_str = "";
					String text_str = "";
					for (int i = 0; i < valuelist.size(); i++) {
						value_str = (String) valuelist.get(i);
						text_str = (String) texthash.get(value_str);
						if (text_str == null || text_str.trim().length() <= 0)
							text_str = value_str;
						if (value_str.equals(value2))
							s += "<option value=\"" + value_str
									+ "\" selected>" + text_str + "</option>";
						else
							s += "<option value=\"" + value_str + "\">"
									+ text_str + "</option>";
					}
				}
				s += "<select>";
			}
		}
		return s;
	}
}
