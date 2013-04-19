package tms.datacenter.dbmanage;

import java.util.ArrayList;

public class RecordCheck {
	public static String checkRecord(String tableName,Record r,boolean allfield){
		if(tableName == null || tableName.trim().length() <= 0){
			return "表名参数错误！";
		}
		if(r == null)
			return "记录不能为空！";
		TableConfig tc = TableConfig.getInstance();
		TableDesc table = tc.getTable(tableName);
		if(table == null)
			return "";
		ArrayList fieldList  = table.getFieldList();
		if(fieldList != null && fieldList.size() > 0){
			Field field = null;
			Field rfield = null;
			String rfieldvalue = "";
			String fieldname = "";
			String fieldlabel = "";
			String fieldtype = "";
			int fieldlength = 0;
			boolean ispk = false;
			String cannull = "0";
			for(int i = 0; i < fieldList.size(); i++){
				field = (Field)fieldList.get(i);
				fieldname = field.getFieldName();
				fieldlabel = field.getFieldLabel();
				fieldtype = field.getFieldType();
				fieldlength = field.getFieldLength();
				ispk = field.isIspk();
				cannull = field.getCannull();
				rfield = r.getField(fieldname);
				if(rfield == null){
					if(allfield)
						return fieldlabel+"不能为空！";
					else
						continue;
				}
				rfieldvalue = rfield.getFieldValue();
				if(rfieldvalue == null || rfieldvalue.trim().length() <= 0){
					if(Field.CAN_NULL_NO.equals(cannull)){
						return fieldlabel+"不能为空！";
					}
				}
				if(ispk && !rfieldvalue.matches("^[a-zA-Z0-9]+$")){
					return fieldlabel+"只能输入字母和数字！";
				}
				if(Field.FIELD_TYPE_INT.equals(fieldtype)&& rfieldvalue != null && rfieldvalue.trim().length() > 0){
					try{
						Integer.parseInt(rfieldvalue);
					}catch(Exception e){
						return fieldlabel+"只能输入整数！";
					}
				}
				if(Field.FIELD_TYPE_DOUBLE.equals(fieldtype)&& rfieldvalue != null && rfieldvalue.trim().length() > 0){
					try{
						Double.parseDouble(rfieldvalue);
					}catch(Exception e){
						return fieldlabel+"只能输入数字！";
					}
				}
				if(Field.FIELD_TYPE_DATE.equals(fieldtype) && rfieldvalue != null && rfieldvalue.trim().length() > 0 && !rfieldvalue.matches("^\\d{4}\\-\\d{1,2}\\-\\d{1,2}(\\s\\d{1,2}:\\d{1,2}:\\d{1,2})?$")){
					return fieldlabel+"请输入正确的日期格式如2012-01-01！";
				}
				if(Field.FIELD_TYPE_TEXT.equals(fieldtype)){
					if(rfieldvalue == null)
						rfieldvalue = "";
					if(rfieldvalue.length() > fieldlength)
						return fieldlabel+"长度不能大于"+fieldlength+"！";
				}
			}
		}
		return "";
	}
	public static Record setRecordFieldDesc(String tableName,Record r){
		if(tableName == null || tableName.trim().length() <= 0){
			return r;
		}
		if(r == null)
			return r;
		TableConfig tc = TableConfig.getInstance();
		TableDesc table = tc.getTable(tableName);
		if(table == null)
			return r;
		ArrayList fieldList  = table.getFieldList();
		if(fieldList != null && fieldList.size() > 0){
			Field field = null;
			Field rfield = null;
			String rfieldvalue = "";
			String fieldname = "";
			String fieldlabel = "";
			String fieldtype = "";
			int fieldlength = 0;
			boolean ispk = false;
			String cannull = "0";
			Record res = new Record();;
			String datadesc = "";
			for(int i = 0; i < fieldList.size(); i++){
				field = (Field)fieldList.get(i);
				fieldname = field.getFieldName();
				fieldlabel = field.getFieldLabel();
				fieldtype = field.getFieldType();
				fieldlength = field.getFieldLength();
				ispk = field.isIspk();
				cannull = field.getCannull();
				rfield = r.getField(fieldname);
				if(rfield == null){
					rfield = new Field();
					rfield.setFieldName(fieldname);
				}
				rfield.setFieldLabel(fieldlabel);
				datadesc = "";
				if(ispk && Field.FIELD_TYPE_TEXT.equals(fieldtype) && !rfieldvalue.matches("^[a-zA-Z0-9]+$")){
					datadesc+="只能包含字母和数字！";
				}
				if(Field.FIELD_TYPE_INT.equals(fieldtype)){
					datadesc+="请输入整数！";
				}
				if(Field.FIELD_TYPE_DOUBLE.equals(fieldtype)){
					datadesc+="请输入数字！";
				}
				if(Field.FIELD_TYPE_DATE.equals(fieldtype)){
					datadesc+="请输入日期，格式如2012-01-01！";
				}
				if(Field.FIELD_TYPE_TEXT.equals(fieldtype)){
					datadesc+="长度不能大于"+fieldlength+"！";
				}
				rfield.setFieldType(fieldtype);
				rfield.setDatadesc(datadesc);
				res.addField(rfield);
			}
			return res;
		}
		return r;
		
	}
}
