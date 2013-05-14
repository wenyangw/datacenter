package tms.datacenter.timertask;

import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;

public class SynLog extends TableManage {
	public SynLog() {
		setTableName("dc_synlog");
	}

	public boolean writeLog(String synname, String starttime, String endtime,
			int syncount, String batchid, String tablename, String memo) {
		Record r = new Record();
		r.set("synname", synname);
		r.set("starttime", starttime);
		r.set("endtime", endtime);
		r.set("syncount", ""+syncount);
		r.set("batchid", batchid);
		r.set("tablename", tablename);
		r.set("memo", memo);
		int i = insertRecord("datacenter", "dc_synlog", r);
		if(i > 0)
			return true;
		else
			return false;
	}
}
