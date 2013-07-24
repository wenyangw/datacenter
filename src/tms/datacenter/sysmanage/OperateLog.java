package tms.datacenter.sysmanage;

import tms.datacenter.commontools.DateUtil;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;

public class OperateLog extends TableManage {
	public OperateLog() {
		setTableName("dc_operatelog");
	}
	public void AddLog(String loginname,String operate,String memo){
		String currenttime = DateUtil.dateToStringWithTime(new java.util.Date());
		Record r = new Record();
		r.set("loginname", loginname);
		r.set("operate", operate);
		r.set("updatetime", currenttime);
		r.set("memo", memo);
		this.insertRecord("datacenter", "dc_operatelog", r);
	}
}
