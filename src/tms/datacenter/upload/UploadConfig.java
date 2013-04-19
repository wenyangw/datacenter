package tms.datacenter.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class UploadConfig {
	private ArrayList uploads = new ArrayList();
	private Hashtable uploadshash = new Hashtable();
	public static UploadConfig uc = null;
	
	public static UploadConfig getInstance(){
		if(uc == null)
			uc = new UploadConfig();
		return uc;
	}
	private UploadConfig() {
		loadPrivilegeConfig();
	}
	private void loadPrivilegeConfig() {
		try {
			String cpath = java.net.URLDecoder.decode(UploadConfig.class.getResource("/").getPath()+ File.separatorChar + "uploadconfig.xml", "gb2312");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(cpath);
			Element root = doc.getRootElement();
			Iterator uploadsit = root.elementIterator("upload");
			UploadMsg upload = null;
			ColumnMsg column = null;
			while (uploadsit.hasNext()) {
				upload = new UploadMsg();
				Element e_upload = (Element) uploadsit.next();
				String cnname = getAttribute(e_upload, "cnname");
				String specialparam = getAttribute(e_upload, "specialparam");
				String table = getAttribute(e_upload, "table");
				String txtseparate = getAttribute(e_upload, "txtseparate");
				upload.setCnname(cnname);
				upload.setSpecialparam(specialparam);
				upload.setTable(table);
				upload.setTxtseparate(txtseparate);
				Iterator columnit = e_upload.elementIterator("column");
				while (columnit.hasNext()) {
					column = new ColumnMsg();
					Element e_column = (Element) columnit.next();
					String fieldname = getAttribute(e_column, "fieldname");
					String number = getAttribute(e_column, "number");
					if(fieldname== null || fieldname.trim().length() <= 0)
						continue;
					if(number== null || !number.matches("\\d+"))
						continue;
					column.setFieldname(fieldname);
					column.setNumber(Integer.parseInt(number));
					
					upload.addColumn(column);
				}
				addUpload(upload);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void addUpload(UploadMsg upload){
		if(upload == null)
			return;
		String specialparam = upload.getSpecialparam();
		if(specialparam != null && specialparam.trim().length() > 0){
			uploads.add(upload);
			uploadshash.put(specialparam.toLowerCase(), upload);
		}
	}
	public UploadMsg getUpload(String specialparam){
		if(specialparam == null || specialparam.trim().length() <= 0)
			return null;
		return (UploadMsg)uploadshash.get(specialparam.toLowerCase());
	}
	private String getAttribute(Element e, String an) {
		Attribute attribute = e.attribute(an);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	public ArrayList getUploadslist() {
		return uploads;
	}
	public Hashtable getUploadshash() {
		return uploadshash;
	}
	
	public static void main(String[] args){
		UploadConfig uc = UploadConfig.getInstance();
		ArrayList uploads = uc.getUploadslist();
		ArrayList columns = null;
		ColumnMsg c = null;
		if(uploads != null && uploads.size() > 0){
			UploadMsg um = null;
			for(int i = 0; i < uploads.size(); i++){
				um = (UploadMsg)uploads.get(i);
				System.out.println(um.getCnname());
				System.out.println(um.getSpecialparam());
				System.out.println(um.getTable());
				System.out.println(um.getTxtseparate());
				columns = um.getColumnList();
				for(int j = 0; j < columns.size(); j++){
					c = (ColumnMsg)columns.get(j);
					System.out.println(c.getFieldname());
					System.out.println(c.getNumber());
				}
			}
		}
	}
}
