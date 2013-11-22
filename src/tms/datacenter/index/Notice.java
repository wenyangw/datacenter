package tms.datacenter.index;

import org.json.simple.JSONObject;

import tms.datacenter.dbmanage.TableManage;

public class Notice  extends TableManage {
	public Notice() {
		setTableName("dc_notice");
	}
	public static String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", new Integer(1));
		obj.put("message", message);
		return obj.toJSONString();
	}
}
