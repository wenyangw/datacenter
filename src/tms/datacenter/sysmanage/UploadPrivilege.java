package tms.datacenter.sysmanage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.ConnectionManage;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.TableManage;
import tms.datacenter.upload.UploadConfig;
import tms.datacenter.upload.UploadMsg;

public class UploadPrivilege extends TableManage {
	public UploadPrivilege() {
		setTableName("dc_uploadPrivilege");
	}

	public ArrayList getAllUploads() {
		UploadConfig uc = UploadConfig.getInstance();
		ArrayList uploads = uc.getUploadslist();
		return uploads;
	}

	public ArrayList getUserUploads(String loginname) {
		if ("admin".equalsIgnoreCase(loginname))
			return getAllUploads();
		ArrayList res = new ArrayList();
		ArrayList al = this.getAllRecords("datacenter", "loginname='"
				+ loginname + "'", "");
		if (al != null && al.size() > 0) {
			UploadConfig uc = UploadConfig.getInstance();
			Record r = null;
			UploadMsg up = null;
			for (int i = 0; i < al.size(); i++) {
				r = (Record) al.get(i);
				String specialparam = r.get("specialparam");
				up = uc.getUpload(specialparam);
				if (up != null)
					res.add(up);
			}
		}
		return res;
	}
	public ArrayList getUserUploadsName(String loginname) {
		if ("admin".equalsIgnoreCase(loginname))
			return getAllUploads();
		ArrayList res = new ArrayList();
		ArrayList al = this.getAllRecords("datacenter", "loginname='"
				+ loginname + "'", "");
		if (al != null && al.size() > 0) {
			UploadConfig uc = UploadConfig.getInstance();
			Record r = null;
			UploadMsg up = null;
			for (int i = 0; i < al.size(); i++) {
				r = (Record) al.get(i);
				String specialparam = r.get("specialparam");
				up = uc.getUpload(specialparam);
				if (up != null)
					res.add(specialparam);
			}
		}
		return res;
	}

	public boolean setPrivilege(Connection conn,String loginname, String[] specialparams) {
		if (loginname == null || loginname.trim().length() <= 0)
			return false;
		Statement stmt = null;
		if (conn != null) {
			try {
				String sql = "delete from dc_uploadPrivilege where loginname='"
						+ loginname + "'";
				stmt = conn.createStatement();
				int res = stmt.executeUpdate(sql);
				if (res < 0) {
					return false;
				}
				if (specialparams != null && specialparams.length > 0) {
					for (int i = 0; i < specialparams.length; i++) {
						if (specialparams[i] != null
								&& specialparams[i].trim().length() > 0) {
							sql = "insert into dc_uploadPrivilege values('"
									+ loginname + "','" + StringToZn.toDB(specialparams[i])
									+ "','')";
							stmt.executeUpdate(sql);
						}
					}
				}
				return true;
			} catch (SQLException e) {
				return false;
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
