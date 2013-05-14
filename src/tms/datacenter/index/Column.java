package tms.datacenter.index;

import java.util.ArrayList;

import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;

public class Column extends TableManage {
	public Column() {
		setTableName("dc_column");
	}
	public ArrayList getColumnByPos(String showwhere){
		if(showwhere == null || showwhere.trim().length() <= 0)
			return null;
		return this.getAllRecords("datacenter", "showwhere='"+showwhere+"'", "order by showorder");
	}
	public Record getColumnByID(String cid){
		if(cid == null || cid.trim().length() <= 0)
			return null;
		ArrayList al = this.getAllRecords("datacenter", "cid='"+cid+"'", "");
		if(al != null && al.size() > 0)
			return (Record)al.get(0);
		return null;
	}
}
