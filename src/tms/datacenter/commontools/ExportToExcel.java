package tms.datacenter.commontools;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.RecordCheck;
import tms.datacenter.upload.ColumnMsg;
import tms.datacenter.upload.UploadConfig;
import tms.datacenter.upload.UploadMsg;

public class ExportToExcel {
	/**
	 * 
	 * @param records 必须
	 * @param tablename 可为空""
	 * @param fieldslabels 可为null
	 * @param hiddenfiels 可为null
	 * @return
	 */
	public static ArrayList getFilterField(){
		ArrayList al = new ArrayList();
		al.add("id");
		al.add("username");
		al.add("organization");
		al.add("department");
		al.add("logno");
		al.add("updatetime");
		return al;
	}
	public HSSFWorkbook getWorkbook(ArrayList records,String tablename,Hashtable fieldslabels,ArrayList hiddenfiels){
		if(records == null || records.size() <= 0)
			return null;
		ArrayList filterfield =  getFilterField();
		Record rdesc = new Record();
		rdesc = RecordCheck.setRecordFieldDesc(tablename, rdesc);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		HSSFRow row = sheet.createRow(0);

		Record r = (Record)records.get(0);
		ArrayList fields = null;
		Field field = null;
		String datatdclass = "";
		String fieldlabel = "";
		r = (Record)records.get(0);
		fields = r.getFieldslist();
		Field descfield = null;
		String fieldname = "";
		String fieldvalue = "";
		String fieldtype = "";
		if(hiddenfiels == null)
			hiddenfiels = new ArrayList();
		if(fields != null && fields.size() > 0){
			int cellnum = 0;
			HSSFCell cell = null;
			for(int j = 0;j < fields.size(); j++){
				field = (Field)fields.get(j);
				fieldname = field.getFieldName();
				if(filterfield.contains(fieldname.toLowerCase()))
					continue;
				fieldtype = field.getFieldType();
				if(rdesc != null){
					fieldlabel = rdesc.getLabel(fieldname);
				}
				if(fieldlabel == null || fieldlabel.trim().length() <= 0)
					fieldlabel = fieldname;
				fieldvalue = field.getFieldValue();
				if(!hiddenfiels.contains(fieldname.toLowerCase())){
					cell = row.createCell(cellnum);
					cell.setCellValue(fieldlabel);
					cellnum++;
				}
					
			}
		}
		int rownum = 1;
		String showvalue= "";
		for(int i = 0; i < records.size(); i++){
			r = (Record)records.get(i);
			if(r!=null){
				row = sheet.createRow(rownum);
				rownum++;
				fields = r.getFieldslist();
				if(fields != null && fields.size() > 0){
					int cellnum = 0;
					HSSFCell cell = null;
					for(int j = 0;j < fields.size(); j++){
						field = (Field)fields.get(j);
						fieldname = field.getFieldName();
						if(filterfield.contains(fieldname.toLowerCase()))
							continue;
						fieldtype = field.getFieldType();
						if(rdesc != null){
							fieldlabel = rdesc.getLabel(fieldname);
							fieldtype = rdesc.getFieldType(fieldname);
						}
						fieldvalue = field.getFieldValue();
						if(!hiddenfiels.contains(fieldname.toLowerCase())){
							cell = row.createCell(cellnum);
							showvalue = Record.getShowValue(fieldslabels,fieldname,fieldvalue);
							
							if(Field.FIELD_TYPE_DOUBLE.equals(fieldtype)){
								try{
									double d_value = Double.parseDouble(showvalue);
									cell.setCellValue(d_value);
								}catch(Exception e){
									cell.setCellValue(showvalue);
								}
							}else if(Field.FIELD_TYPE_INT.equals(fieldtype)){
								try{
									double i_value = Integer.parseInt(showvalue);
									cell.setCellValue(i_value);
								}catch(Exception e){
									cell.setCellValue(showvalue);
								}
							}else{
								cell.setCellValue(showvalue);
							}
							cellnum++;
						}
							
					}
				}
			}
		}
		return workbook;
	}
	
	public HSSFWorkbook getWorkbookList(ArrayList records,String tablename,Hashtable fieldslabels,ArrayList hiddenfiels){
		if(records == null || records.size() <= 0)
			return null;
		ArrayList filterfield =  getFilterField();
		Record rdesc = new Record();
		rdesc = RecordCheck.setRecordFieldDesc(tablename, rdesc);
		
		UploadConfig uc = UploadConfig.getInstance();
		UploadMsg um = uc.getUpload(tablename);
		ArrayList cloumns = um.getColumnList();
		if(cloumns == null)
			cloumns = new ArrayList();
		ColumnMsg cm = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		HSSFRow row = sheet.createRow(0);

		Record r = (Record)records.get(0);
		ArrayList fields = null;
		Field field = null;
		Field fielddesc = null;
		String datatdclass = "";
		String fieldlabel = "";
		r = (Record)records.get(0);
		fields = r.getFieldslist();
		Field descfield = null;
		String fieldname = "";
		String fieldvalue = "";
		String fieldtype = "";
		if(hiddenfiels == null)
			hiddenfiels = new ArrayList();
		if(cloumns != null && cloumns.size() > 0){
			int cellnum = 0;
			HSSFCell cell = null;
			for(int j = 0;j < cloumns.size(); j++){
				cm = (ColumnMsg)cloumns.get(j);
				fieldname = cm.getFieldname();
				field = r.getField(fieldname);
				fielddesc = rdesc.getField(fieldname);
				if(filterfield.contains(fieldname.toLowerCase()))
					continue;
				fieldtype = fielddesc.getFieldType();
				if(rdesc != null){
					fieldlabel = rdesc.getLabel(fieldname.toLowerCase());
				}
				if(fieldlabel == null || fieldlabel.trim().length() <= 0)
					fieldlabel = fieldname;
				fieldvalue = field.getFieldValue();
				if(!hiddenfiels.contains(fieldname.toLowerCase())){
					cell = row.createCell(cellnum);
					cell.setCellValue(fieldlabel);
					cellnum++;
				}
					
			}
		}
		int rownum = 1;
		String showvalue= "";
		for(int i = 0; i < records.size(); i++){
			r = (Record)records.get(i);
			if(r!=null){
				row = sheet.createRow(rownum);
				rownum++;
				fields = r.getFieldslist();
				if(cloumns != null && cloumns.size() > 0){
					int cellnum = 0;
					HSSFCell cell = null;
					for(int j = 0;j < cloumns.size(); j++){
						cm = (ColumnMsg)cloumns.get(j);
						fieldname = cm.getFieldname();
						field = r.getField(fieldname);
						if(filterfield.contains(fieldname.toLowerCase()))
							continue;
						fieldtype = field.getFieldType();
						if(rdesc != null){
							fieldlabel = rdesc.getLabel(fieldname);
							fieldtype = rdesc.getFieldType(fieldname.toLowerCase());
						}
						fieldvalue = field.getFieldValue();
						if(!hiddenfiels.contains(fieldname.toLowerCase())){
							cell = row.createCell(cellnum);
							showvalue = Record.getShowValue(fieldslabels,fieldname,fieldvalue);
							
							if(Field.FIELD_TYPE_DOUBLE.equals(fieldtype)){
								try{
									double d_value = Double.parseDouble(showvalue);
									cell.setCellValue(d_value);
								}catch(Exception e){
									cell.setCellValue(showvalue);
								}
							}else if(Field.FIELD_TYPE_INT.equals(fieldtype)){
								try{
									double i_value = Integer.parseInt(showvalue);
									cell.setCellValue(i_value);
								}catch(Exception e){
									cell.setCellValue(showvalue);
								}
							}else{
								cell.setCellValue(showvalue);
							}
							cellnum++;
						}
							
					}
				}
			}
		}
		return workbook;
	}
}
