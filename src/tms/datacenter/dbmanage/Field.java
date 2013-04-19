package tms.datacenter.dbmanage;

public class Field {
	//public static final String FIELD_TYPE_NUMBER="0";
	public static final String FIELD_TYPE_TEXT="1";
	public static final String FIELD_TYPE_DATE="2";
	
	public static final String FIELD_TYPE_INT="3";
	public static final String FIELD_TYPE_DOUBLE="4";
	
	public static final String SHOW_POS_NONE = "0";
	public static final String SHOW_POS_LIST = "1";
	public static final String SHOW_POS_ADD = "2";
	public static final String SHOW_POS_UPDATE = "3";
	
	public static final String CAN_NULL_YES = "1";
	public static final String CAN_NULL_NO = "0";
	
	public static final boolean IS_PK_YES = true;
	public static final boolean IS_PK_NO = false;
	
	private String fieldName;
	private String fieldType;
	private String fieldValue;
	private String fieldLabel;
	private boolean ispk;
	private String datadesc;
	public String getDatadesc() {
		return datadesc;
	}
	public void setDatadesc(String datadesc) {
		this.datadesc = datadesc;
	}
	public boolean isIspk() {
		return ispk;
	}
	public void setIspk(boolean ispk) {
		this.ispk = ispk;
	}
	private int fieldLength;
	private String cannull;;
	private String showpos;
	
	public int getFieldLength() {
		return fieldLength;
	}
	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}
	public String getCannull() {
		return cannull;
	}
	public void setCannull(String cannull) {
		this.cannull = cannull;
	}
	public String getShowpos() {
		return showpos;
	}
	public void setShowpos(String showpos) {
		this.showpos = showpos;
	}
	
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getFieldLabel() {
		if(fieldLabel == null || fieldLabel.trim().length() <= 0)
			return fieldName;
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
}
