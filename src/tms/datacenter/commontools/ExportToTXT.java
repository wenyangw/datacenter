package tms.datacenter.commontools;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.RecordCheck;
import tms.datacenter.upload.ColumnMsg;
import tms.datacenter.upload.UploadConfig;
import tms.datacenter.upload.UploadMsg;

public class ExportToTXT {
	/**
	 * 
	 * @param records 必须
	 * @param tablename 可为空""
	 * @param fieldslabels 可为null
	 * @param hiddenfiels 可为null
	 * @return
	 */
	public static void writeToTxt(HttpServletResponse response, ArrayList records,String tablename,Hashtable fieldslabels,ArrayList hiddenfiels) {  
		  
        
        BufferedOutputStream buff = null;  
        StringBuffer write = new StringBuffer();  
        String tab = "\t";  
        String enter = "\r\n";  
        ServletOutputStream outSTr = null;  
        ArrayList filterfield =  ExportToExcel.getFilterField();
        try {  
            outSTr = response.getOutputStream();// 建立  
            buff = new BufferedOutputStream(outSTr);  
            Record r = null;
            ArrayList fields = null;
            Field field = null;
            String fieldname = "";
            String fieldvalue = "";
            Record rdesc = new Record();
    		rdesc = RecordCheck.setRecordFieldDesc(tablename, rdesc);
           
            for(int i = 0; i < records.size(); i++){
    			r = (Record)records.get(i);
    			if(r!=null){
    				fields = r.getFieldslist();
    				if(fields != null && fields.size() > 0){
    					for(int j = 0;j < fields.size(); j++){
    						field = (Field)fields.get(j);
    						fieldname = field.getFieldName();
    						if(filterfield.contains(fieldname.toLowerCase()))
    							continue;
    						fieldvalue = field.getFieldValue();
    						if(!hiddenfiels.contains(fieldname.toLowerCase())){
    							write.append(fieldvalue + tab);
    						}
    					}
    					write.append(enter);
    				}
    			}
    		}  
            buff.write(write.toString().getBytes("GBK"));  
            buff.flush();  
            buff.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                buff.close();  
                outSTr.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
public static void writeToTxtList(HttpServletResponse response, ArrayList records,String tablename,Hashtable fieldslabels,ArrayList hiddenfiels) {  
		  
        
        BufferedOutputStream buff = null;  
        StringBuffer write = new StringBuffer();  
        String tab = "\t";  
        String enter = "\r\n";  
        ServletOutputStream outSTr = null;  
        ArrayList filterfield =  ExportToExcel.getFilterField();
        UploadConfig uc = UploadConfig.getInstance();
        UploadMsg um = uc.getUpload(tablename);
        ArrayList cloumns = um.getColumnList();
        if(cloumns == null)
        	cloumns = new ArrayList();
        ColumnMsg cm = null;
        try {  
            outSTr = response.getOutputStream();// 建立  
            buff = new BufferedOutputStream(outSTr);  
            Record r = null;
            ArrayList fields = null;
            Field field = null;
            String fieldname = "";
            String fieldvalue = "";
            Record rdesc = new Record();
    		rdesc = RecordCheck.setRecordFieldDesc(tablename, rdesc);
           
            for(int i = 0; i < records.size(); i++){
    			r = (Record)records.get(i);
    			if(r!=null){
    				fields = r.getFieldslist();
    				if(cloumns != null && cloumns.size() > 0){
    					for(int j = 0;j < cloumns.size(); j++){
    						cm = (ColumnMsg)cloumns.get(j);
    						fieldname = cm.getFieldname();
    						field = r.getField(fieldname);
    						if(filterfield.contains(fieldname.toLowerCase()))
    							continue;
    						fieldvalue = field.getFieldValue();
    						if(!hiddenfiels.contains(fieldname.toLowerCase())){
    							write.append(fieldvalue + tab);
    						}
    					}
    					write.append(enter);
    				}
    			}
    		}  
            buff.write(write.toString().getBytes("GBK"));  
            buff.flush();  
            buff.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                buff.close();  
                outSTr.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}
