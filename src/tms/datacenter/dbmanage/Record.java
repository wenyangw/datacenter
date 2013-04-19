package tms.datacenter.dbmanage;

import java.util.ArrayList;
import java.util.Hashtable;

public class Record {

	Hashtable<String, Field> fields = new Hashtable<String, Field>();
	ArrayList fieldslist = new ArrayList();
	Hashtable userPrivilege = new Hashtable();

	public ArrayList getFieldslist() {
		return fieldslist;
	}

	public void setFieldslist(ArrayList fieldslist) {
		this.fieldslist = fieldslist;
	}

	public void set(String fieldName, String fieldValue, String fieldtype,
			boolean ispk) {
		if (fieldName == null || fieldName.trim().length() <= 0)
			return;
		fieldName = fieldName.toLowerCase();
		Field field = fields.get(fieldName);
		if (field != null) {
			field.setFieldValue(fieldValue);
			field.setFieldType(fieldtype);
			field.setIspk(ispk);
		} else {
			field = new Field();
			field.setFieldName(fieldName);
			field.setFieldValue(fieldValue);
			field.setFieldType(fieldtype);
			field.setIspk(ispk);
		}
		fields.put(fieldName, field);
		fieldslist.add(field);
	}

	public Hashtable getUserPrivilege() {
		return userPrivilege;
	}

	public void setUserPrivilege(Hashtable userPrivilege) {
		this.userPrivilege = userPrivilege;
	}

	public void set(String fieldName, String fieldValue, String fieldtype,
			String fieldLabel) {
		if (fieldName == null || fieldName.trim().length() <= 0)
			return;
		fieldName = fieldName.toLowerCase();
		Field field = fields.get(fieldName);
		if (field != null) {
			field.setFieldValue(fieldValue);
			field.setFieldType(fieldtype);
			field.setFieldLabel(fieldLabel);
		} else {
			field = new Field();
			field.setFieldName(fieldName);
			field.setFieldValue(fieldValue);
			field.setFieldType(fieldtype);
			field.setFieldLabel(fieldLabel);
		}
		fields.put(fieldName, field);
		fieldslist.add(field);
	}

	public void set(String fieldName, String fieldValue) {
		if (fieldName == null || fieldName.trim().length() <= 0)
			return;
		fieldName = fieldName.toLowerCase();
		Field field = fields.get(fieldName);
		if (field != null) {
			field.setFieldValue(fieldValue);
		} else {
			field = new Field();
			field.setFieldName(fieldName);
			field.setFieldValue(fieldValue);
		}
		fields.put(fieldName, field);
		fieldslist.add(field);
	}

	public String get(String fieldName) {
		if (fieldName == null || fieldName.trim().length() <= 0)
			return null;
		fieldName = fieldName.toLowerCase();
		Field field = fields.get(fieldName);
		if (field != null)
			return field.getFieldValue();
		return null;
	}

	public Hashtable getFields() {
		return fields;
	}

	public Field getField(String fieldName) {
		if (fieldName == null)
			return null;
		fieldName = fieldName.toLowerCase();
		return fields.get(fieldName);
	}

	public void addField(Field field) {
		if (field == null)
			return;
		String fieldName = field.getFieldName();
		if (fieldName == null || fieldName.trim().length() <= 0)
			return;
		fieldName = fieldName.toLowerCase();
		fields.put(fieldName, field);
		fieldslist.add(field);

	}

	public String getLabel(String fieldName) {
		if (fieldName == null && fieldName.trim().length() <= 0)
			return "";
		Field f = fields.get(fieldName);
		if (f == null)
			return "";
		return f.getFieldLabel();
	}

	public String getDataDesc(String fieldName) {
		if (fieldName == null && fieldName.trim().length() <= 0)
			return "";
		Field f = fields.get(fieldName);
		if (f == null)
			return "";
		return f.getDatadesc();
	}
	public String getFieldType(String fieldName) {
		if (fieldName == null && fieldName.trim().length() <= 0)
			return "";
		Field f = fields.get(fieldName);
		if (f == null)
			return "";
		return f.getFieldType();
	}
	public static String getShowValue(Hashtable showValueHash,
			String fieldName, String value) {
		if (showValueHash != null && showValueHash.size() > 0) {
			if (fieldName != null && fieldName.trim().length() > 0) {
				Hashtable ht = (Hashtable) showValueHash.get(fieldName
						.toLowerCase());
				if (ht != null && ht.size() > 0) {

					if (value != null && value.trim().length() > 0) {
						String label = (String) ht.get(value);
						if (label != null && label.trim().length() > 0) {
							return label;
						}
					}
				}
			}
		}
		if (value == null)
			value = "";
		return value;
	}
}
