package tms.datacenter.upload;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tms.datacenter.commontools.DateUtil;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;

public class RuleManage extends TableManage {
	public RuleManage(){
		setTableName("dc_uploadrule"); 
	}
	
	public ArrayList getPageRecord(String poolName, 
			String pkfield, int offset, int length){
		String sql = "select top " + length + " * from " + getTableName()
			+ " where " + pkfield + " not in " + "(select top "
			+ (offset) + " " + pkfield + " from " + getTableName()
			+ " where 1=1 ) ";
				
		ArrayList records = executeQuery("datacenter", sql);
		return records;
	}
	
	public ArrayList getRecord(String poolName, 
			String condition){
		String sql = "select * from " + getTableName()
			+ " where 1=1" 
			+ " and " + condition ;
			
		ArrayList records = executeQuery("datacenter", sql);
		return records;
	}
	
	public String getNextUpload(String pkfield){
		String nextUpload = "";
		String unRule = "未设定更新计划";
	
		//String sql = "select * from " + getTableName()
			//+ " where 1=1" 
			//+ " and uploadname = '" + pkfield + "'";
		//ArrayList al = (ArrayList)this.executeQuery("datacenter", sql);
		ArrayList al = this.getAllRecords("datacenter", "uploadname = '" + pkfield + "'", "");
		if (al != null && al.size() > 0) {
			Record r = (Record)al.get(0);
		
			UploadLogManage ulm = new UploadLogManage();
			String lastTime = ulm.getLastUpload(pkfield);
		
			int ruleType = Integer.parseInt(r.get("RuleType"));
			int cycle = Integer.parseInt(r.get("Cycle"));
			String startTime = r.get("StartTime");
			 		
			
			//从未更新过
			if(lastTime == null || lastTime == "" || DateUtil.daysBetween(lastTime, startTime) > 0){
				nextUpload = startTime;
			}else{
				switch (ruleType) {
				case 0:
					nextUpload = unRule;
					break;
				case 1:
					nextUpload = DateUtil.dateIncrease(lastTime, DateUtil.ISO_EXPANDED_DATE_FORMAT, Calendar.DATE, cycle - DateUtil.daysBetween(startTime, lastTime) % cycle);
					break;
				case 2:
					nextUpload = DateUtil.dateIncrease(lastTime, DateUtil.ISO_EXPANDED_DATE_FORMAT, Calendar.DATE, cycle * 7 - DateUtil.daysBetween(startTime, lastTime) % (cycle * 7));
					break;
				case 3:
					nextUpload = DateUtil.dateIncrease(startTime, DateUtil.ISO_EXPANDED_DATE_FORMAT, Calendar.MONTH, 
							cycle * (
									(Integer.parseInt(DateUtil.getMonth(DateUtil.stringToDate(lastTime))) - Integer.parseInt(DateUtil.getMonth(DateUtil.stringToDate(startTime))) / cycle + 1)));
					break;
				default:
					break;
				}
			}
		}else{
			nextUpload = "未设定更新计划";
		}
		
		return DateUtil.stringToString(nextUpload, DateUtil.ISO_EXPANDED_DATE_FORMAT, DateUtil.ISO_EXPANDED_DATE_FORMAT);
	}
	
	  
	
}
