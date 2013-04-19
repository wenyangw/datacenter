package tms.datacenter.upload;

import java.util.ArrayList;

import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;

public class UploadLogManage extends TableManage {
	public UploadLogManage() {
		setTableName("dc_uploadlog");
	}
	
	public String getLastUpload(String pkfield){
		String lastUpload = "";
		
		ArrayList al = (ArrayList)this.getAllRecords("datacenter", "uploadname = '" + pkfield + "'", "order by uploadtime desc");
		if (al != null && al.size() > 0) {
			Record r = (Record)al.get(0);
			lastUpload = r.get("UploadTime");
		}
		return lastUpload;
	}
	
	
}
