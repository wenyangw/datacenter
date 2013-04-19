package tms.datacenter.sysmanage;

import java.util.ArrayList;

import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;

public class ContentControl extends TableManage {
	public ContentControl() {
		setTableName("dc_content_control");
	}

	public ArrayList getAllControls() {
		return getAllRecords("datacenter", "", " order by tablename");
	}

	public ArrayList getUserControls(String loginname) {
		if(loginname == null || loginname.trim().length() <= 0)
			return null;
		String sql = "select * from dc_content_control where controlcode in ("
				+ "select controlcode from dc_control_in_user where loginname='"
				+ StringToZn.toDB(loginname) + "')";
		return  executeQuery("datacenter", sql);
		
	}
	public ArrayList getUserControlCodes(String loginname) {
		if(loginname == null || loginname.trim().length() <= 0)
			return null;
		String sql = "select controlcode from dc_content_control where controlcode in ("
				+ "select controlcode from dc_control_in_user where loginname='"
				+ StringToZn.toDB(loginname) + "')";
		ArrayList records =  executeQuery("datacenter", sql);
		Record r = null;
		ArrayList res = new ArrayList();
		if(records != null && records.size() > 0){
			for(int i = 0; i < records.size(); i++){
				r = (Record)records.get(i);
				if(r != null)
					res.add(r.get("controlcode"));
			}
		}
		return res;
	}
	public String getControlSQL(Record loginuser,String tablename){
		if(loginuser == null)
			return "2=1";
		if(tablename == null || tablename.trim().length() <= 0)
			return "2=1";
		String loginname = loginuser.get("loginname");
		String sql = "select conditionsql from dc_content_control where controlcode in ("
			+ "select controlcode from dc_control_in_user where loginname='"
			+ StringToZn.toDB(loginname) + "') and tablename='"+tablename+"'";
		ArrayList records =  executeQuery("datacenter", sql);
		Record r = null;
		String res = "";
		String csql = "";
		if(records != null && records.size() > 0){
			for(int i = 0; i < records.size(); i++){
				r = (Record)records.get(i);
				if(r != null){
					csql = r.get("conditionsql");
					csql = doWithSQL(csql,loginuser);
					if(csql != null && csql.trim().length() > 0)
						res+=csql +" and";
				}
			}
		}
		if(res.endsWith("and"))
			res = res.substring(0,res.length() -3);
		if(res == null)
			res = "";
		return res;
	}
	private String doWithSQL(String csql,Record ruser){
		csql = csql.replaceAll("/loginname/", StringToZn.toDB(ruser.get("loginname")));
		csql = csql.replaceAll("/organisation/", StringToZn.toDB(ruser.get("organisation")));
		csql = csql.replaceAll("/department/", StringToZn.toDB(ruser.get("department")));
		return csql;
	}
}
