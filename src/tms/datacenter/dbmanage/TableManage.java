package tms.datacenter.dbmanage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import tms.datacenter.commontools.StringToZn;

public class TableManage {

	private String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ArrayList getAllRecords(Connection conn, String condition,
			String orderby) {
		ArrayList records = new ArrayList();
		if (conn == null)
			return null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = "select * from " + tableName + " where 1=1";
			if (condition != null && condition.trim().length() > 0)
				sql += " and " + condition;
			if (orderby != null && orderby.trim().length() > 0) {
				sql += " " + orderby;
			}
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int fieldCount = meta.getColumnCount();
			Record r = null;
			while (rs.next()) {
				r = new Record();
				String fieldName = "";
				String fieldValue = "";
				for (int i = 1; i <= fieldCount; i++) {
					fieldName = meta.getColumnName(i);
					fieldValue = rs.getString(i);
					r.set(fieldName, fieldValue);
				}
				records.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
		}
		return records;
	}

	public ArrayList getAllRecords(String poolName, String condition,
			String orderby) {
		Connection conn = null;
		ConnectionManage cm = ConnectionManage.getInstance();
		conn = cm.getConnection(poolName);
		if (conn == null)
			return null;
		ArrayList records = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = "select * from " + tableName + " where 1=1";
			if (condition != null && condition.trim().length() > 0)
				sql += " and " + condition;
			if (orderby != null && orderby.trim().length() > 0) {
				sql += " " + orderby;
			}
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int fieldCount = meta.getColumnCount();
			Record r = null;
			while (rs.next()) {
				r = new Record();
				String fieldName = "";
				String fieldValue = "";
				for (int i = 1; i <= fieldCount; i++) {
					fieldName = meta.getColumnName(i);
					fieldValue = rs.getString(i);
					r.set(fieldName, fieldValue);
				}
				records.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
			cm.freeConnection(poolName, conn);
		}
		return records;
	}

	public ArrayList getPageRecord(Connection conn, String condition,
			String orderby, String pkfield, int offset, int length) {

		if (conn == null)
			return null;
		ArrayList records = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String condition_str = "";
			if (condition != null && condition.trim().length() > 0)
				condition_str += " and " + condition;
			if (orderby == null || orderby.trim().length() <= 0) {
				orderby = "";
			}
			String sql = "select top " + length + " * from " + tableName
					+ " where " + pkfield + " not in " + "(select top "
					+ (offset) + " " + pkfield + " from " + tableName
					+ " where 1=1 " + condition_str + " " + orderby + ") "
					+ orderby;
			// System.out.println(sql);
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int fieldCount = meta.getColumnCount();
			Record r = null;
			while (rs.next()) {
				r = new Record();
				String fieldName = "";
				String fieldValue = "";
				for (int i = 1; i <= fieldCount; i++) {
					fieldName = meta.getColumnName(i);
					fieldValue = rs.getString(i);
					r.set(fieldName, fieldValue);
				}
				records.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
		}
		return records;
	}

	public ArrayList getPageRecord(String poolName, String condition,
			String orderby, String pkfield, int offset, int length) {
		Connection conn = null;
		ConnectionManage cm = ConnectionManage.getInstance();
		conn = cm.getConnection(poolName);

		if (conn == null)
			return null;
		ArrayList records = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String condition_str = "";
			if (condition != null && condition.trim().length() > 0)
				condition_str += " and " + condition;
			if (orderby == null || orderby.trim().length() <= 0) {
				orderby = "";
			}
			String sql = "select top " + length + " * from " + tableName
					+ " where " + pkfield + " not in " + "(select top "
					+ (offset) + " " + pkfield + " from " + tableName
					+ " where 1=1 " + condition_str + " " + orderby + ") "+ condition_str + " " + orderby;
			// System.out.println(sql);
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int fieldCount = meta.getColumnCount();
			Record r = null;
			while (rs.next()) {
				r = new Record();
				String fieldName = "";
				String fieldValue = "";
				for (int i = 1; i <= fieldCount; i++) {
					fieldName = meta.getColumnName(i);
					fieldValue = rs.getString(i);
					r.set(fieldName, fieldValue);
				}
				records.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
			cm.freeConnection(poolName, conn);
		}
		return records;
	}

	public int getRecordCount(Connection conn, String condition, String pkfield) {
		ArrayList records = new ArrayList();
		if (conn == null)
			return -1;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = "select count(" + pkfield + ") rcount from "
					+ tableName + " where 1=1";
			if (condition != null && condition.trim().length() > 0)
				sql += " and " + condition;
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int fieldCount = meta.getColumnCount();
			Record r = null;
			while (rs.next()) {
				int rcount = rs.getInt("rcount");
				return rcount;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
		}
		return 0;
	}

	public int getRecordCount(String poolName, String condition, String pkfield) {
		Connection conn = null;
		ConnectionManage cm = ConnectionManage.getInstance();
		conn = cm.getConnection(poolName);
		ArrayList records = new ArrayList();
		if (conn == null)
			return -1;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = "select count(" + pkfield + ") rcount from "
					+ tableName + " where 1=1";
			if (condition != null && condition.trim().length() > 0)
				sql += " and " + condition;
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int fieldCount = meta.getColumnCount();
			Record r = null;
			while (rs.next()) {
				int rcount = rs.getInt("rcount");
				return rcount;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
			cm.freeConnection(poolName, conn);
		}
		return 0;
	}

	public static ArrayList executeQuery(Connection conn, String sql) {
		ArrayList records = new ArrayList();
		if (conn == null)
			return null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int fieldCount = meta.getColumnCount();
			Record r = null;
			while (rs.next()) {
				r = new Record();
				String fieldName = "";
				String fieldValue = "";
				for (int i = 1; i <= fieldCount; i++) {
					fieldName = meta.getColumnName(i);
					fieldValue = rs.getString(i);
					r.set(fieldName, fieldValue);
				}
				records.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
		}
		return records;
	}

	public static ArrayList executeQuery(String poolName, String sql) {
		Connection conn = null;
		ConnectionManage cm = ConnectionManage.getInstance();
		conn = cm.getConnection(poolName);
		ArrayList records = new ArrayList();
		if (conn == null)
			return null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int fieldCount = meta.getColumnCount();
			Record r = null;
			while (rs.next()) {
				r = new Record();
				String fieldName = "";
				String fieldValue = "";
				for (int i = 1; i <= fieldCount; i++) {
					fieldName = meta.getColumnName(i);
					fieldValue = rs.getString(i);
					r.set(fieldName, fieldValue);
				}
				records.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
			cm.freeConnection(poolName, conn);
		}
		return records;
	}
	public static String checkSelectSql(String poolName, String sql) {
		Connection conn = null;
		ConnectionManage cm = ConnectionManage.getInstance();
		conn = cm.getConnection(poolName);
		if (conn == null)
			return null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			return e.getMessage();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {

			}
			cm.freeConnection(poolName, conn);
		}
		return "";
	}
	public static int executeUpdate(Connection conn, String sql) {
		if (conn == null)
			return -1;
		Statement stmt = null;
		int res = 0;
		try {
			stmt = conn.createStatement();
			res = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {

			}
		}
		return res;
	}

	public static int executeUpdate(String poolName, String sql) {
		Connection conn = null;
		ConnectionManage cm = ConnectionManage.getInstance();
		conn = cm.getConnection(poolName);
		if (conn == null)
			return -1;
		Statement stmt = null;
		int res = 0;
		try {
			stmt = conn.createStatement();
			res = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (stmt != null)
					stmt.close();

			} catch (Exception e) {

			}
			cm.freeConnection(poolName, conn);
		}
		return res;
	}

	public boolean isExist(Connection conn, String tableName, String fieldName,
			String fieldType, String fieldValue) {
		String sql = "";
		if (Field.FIELD_TYPE_INT.equals(fieldType)||Field.FIELD_TYPE_DOUBLE.equals(fieldType))
			sql = "select top 1 " + fieldName + " from " + tableName
					+ " where " + fieldName + "=" + fieldValue;
		else if (Field.FIELD_TYPE_TEXT.equals(fieldType)) {
			sql = "select top 1 " + fieldName + " from " + tableName
					+ " where " + fieldName + "='" + fieldValue + "'";
		}
		ArrayList records = this.executeQuery(conn, sql);
		if (records != null && records.size() > 0)
			return true;
		return false;
	}

	public boolean isExist(String poolName, String tableName, String fieldName,
			String fieldType, String fieldValue) {
		String sql = "";
		if (Field.FIELD_TYPE_INT.equals(fieldType)||Field.FIELD_TYPE_DOUBLE.equals(fieldType))
			sql = "select top 1 " + fieldName + " from " + tableName
					+ " where " + fieldName + "=" + fieldValue;
		else if (Field.FIELD_TYPE_TEXT.equals(fieldType)) {
			sql = "select top 1 " + fieldName + " from " + tableName
					+ " where " + fieldName + "='" + fieldValue + "'";
		}
		ArrayList records = this.executeQuery(poolName, sql);
		if (records != null && records.size() > 0)
			return true;
		return false;
	}

	public int insertRecord(Connection conn, String tableName, Record r) {
		if (conn == null)
			return -1;
		String fields_str = "";
		String values_str = "";
		if (r != null) {
			Hashtable fields = r.getFields();
			if (fields != null && fields.size() > 0) {
				Set set = fields.keySet();
				Iterator it = set.iterator();
				Field field = null;
				String fieldName = "";
				String fieldType = "";
				String fieldValue = "";
				while (it.hasNext()) {
					fieldName = (String) it.next();
					field = (Field)fields.get(fieldName);
					fieldType = field.getFieldType();
					fieldValue = field.getFieldValue();
					if (fieldName == null || fieldName.trim().length() <= 0)
						return -2;
					fields_str += fieldName + ",";
					if (Field.FIELD_TYPE_INT.equals(fieldType)||Field.FIELD_TYPE_DOUBLE.equals(fieldType)) {
						if (fieldValue == null
								|| fieldValue.trim().length() <= 0)
							values_str += "null,";
						else
							values_str += fieldValue + ",";
					} else {
						values_str += "'" + StringToZn.toDB(fieldValue) + "',";
					}
				}
				if (fields_str.endsWith(","))
					fields_str = fields_str.substring(0,
							fields_str.length() - 1);
				if (values_str.endsWith(","))
					values_str = values_str.substring(0,
							values_str.length() - 1);
				String sql = "insert into " + tableName + "(" + fields_str
						+ ") values (" + values_str + ")";
				return executeUpdate(conn, sql);
			} else
				return -2;
		}
		return -2;
	}

	public int insertRecord(String poolName, String tableName, Record r) {
		String fields_str = "";
		String values_str = "";
		if (r != null) {
			Hashtable fields = r.getFields();
			if (fields != null && fields.size() > 0) {
				Set set = fields.keySet();
				Iterator it = set.iterator();
				Field field = null;
				String fieldName = "";
				String fieldType = "";
				String fieldValue = "";
				while (it.hasNext()) {
					fieldName = (String) it.next();
					field = (Field)fields.get(fieldName);
					fieldType = field.getFieldType();
					fieldValue = field.getFieldValue();
					if (fieldName == null || fieldName.trim().length() <= 0)
						return -2;
					fields_str += fieldName + ",";
					if (Field.FIELD_TYPE_INT.equals(fieldType)||Field.FIELD_TYPE_DOUBLE.equals(fieldType)) {
						if (fieldValue == null
								|| fieldValue.trim().length() <= 0)
							values_str += "null,";
						else
							values_str += fieldValue + ",";
					} else {
						values_str += "'" + StringToZn.toDB(fieldValue) + "',";
					}
				}
				if (fields_str.endsWith(","))
					fields_str = fields_str.substring(0,
							fields_str.length() - 1);
				if (values_str.endsWith(","))
					values_str = values_str.substring(0,
							values_str.length() - 1);
				String sql = "insert into " + tableName + "(" + fields_str
						+ ") values (" + values_str + ")";
				return executeUpdate(poolName, sql);
			} else
				return -2;
		}
		return -2;
	}
	public int updateRecord(Connection conn, String tableName, Record r) {
		String name_value = "";
		String condition = "";
		if (r != null) {
			Hashtable fields = r.getFields();
			if (fields != null && fields.size() > 0) {
				Set set = fields.keySet();
				Iterator it = set.iterator();
				Field field = null;
				String fieldName = "";
				String fieldType = "";
				String fieldValue = "";
				boolean ispk = false;
				boolean haspk = false;
				while (it.hasNext()) {
					fieldName = (String) it.next();
					field = (Field)fields.get(fieldName);
					fieldType = field.getFieldType();
					fieldValue = field.getFieldValue();
					ispk = field.isIspk();
					if (!ispk) {
						if (fieldName == null || fieldName.trim().length() <= 0)
							return -2;
						if (Field.FIELD_TYPE_INT.equals(fieldType)||Field.FIELD_TYPE_DOUBLE.equals(fieldType)) {
							if (fieldValue == null
									|| fieldValue.trim().length() <= 0)
								name_value += fieldName + "=null,";
							else
								name_value += fieldName + "=" + fieldValue
										+ ",";
						} else {
							name_value += fieldName + "='"
									+ StringToZn.toDB(fieldValue) + "',";
						}
					} else {
						if (fieldName == null || fieldName.trim().length() <= 0)
							return -2;
						if (Field.FIELD_TYPE_INT.equals(fieldType)||Field.FIELD_TYPE_DOUBLE.equals(fieldType)) {
							if (fieldValue == null
									|| fieldValue.trim().length() <= 0)
								condition += " "+fieldName + " is null and";
							else
								condition += " "+fieldName + "=" + fieldValue
										+ " and";
						} else {
							condition += " "+fieldName + "='"
									+ StringToZn.toDB(fieldValue) + "' and";
						}
						haspk = true;
					}
				}
				if (!haspk)
					return -3;
				if (name_value.endsWith(","))
					name_value = name_value.substring(0,
							name_value.length() - 1);
				if (condition.endsWith("and"))
					condition = condition.substring(0, condition.length() - 3);
				String sql = "update " + tableName + " set " + name_value
						+ " where " + condition;
				
				try {
					
					int res = executeUpdate(conn, sql);
					
					if (res != 1) {
						
						return -5;
					}
					
					return res;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					
				}
				
			} else
				return -2;
		}
		return -2;
	}
	public int updateRecord(String poolName, String tableName, Record r) {
		String name_value = "";
		String condition = "";
		if (r != null) {
			Hashtable fields = r.getFields();
			if (fields != null && fields.size() > 0) {
				Set set = fields.keySet();
				Iterator it = set.iterator();
				Field field = null;
				String fieldName = "";
				String fieldType = "";
				String fieldValue = "";
				boolean ispk = false;
				boolean haspk = false;
				while (it.hasNext()) {
					fieldName = (String) it.next();
					field = (Field)fields.get(fieldName);
					fieldType = field.getFieldType();
					fieldValue = field.getFieldValue();
					ispk = field.isIspk();
					if (!ispk) {
						if (fieldName == null || fieldName.trim().length() <= 0)
							return -2;
						if (Field.FIELD_TYPE_INT.equals(fieldType)||Field.FIELD_TYPE_DOUBLE.equals(fieldType)) {
							if (fieldValue == null
									|| fieldValue.trim().length() <= 0)
								name_value += fieldName + "=null,";
							else
								name_value += fieldName + "=" + fieldValue
										+ ",";
						} else {
							name_value += fieldName + "='"
									+ StringToZn.toDB(fieldValue) + "',";
						}
					} else {
						if (fieldName == null || fieldName.trim().length() <= 0)
							return -2;
						if (Field.FIELD_TYPE_INT.equals(fieldType)||Field.FIELD_TYPE_DOUBLE.equals(fieldType)) {
							if (fieldValue == null
									|| fieldValue.trim().length() <= 0)
								condition += " "+fieldName + " is null and";
							else
								condition += " "+fieldName + "=" + fieldValue
										+ " and";
						} else {
							condition += " "+fieldName + "='"
									+ StringToZn.toDB(fieldValue) + "' and";
						}
						haspk = true;
					}
				}
				if (!haspk)
					return -3;
				if (name_value.endsWith(","))
					name_value = name_value.substring(0,
							name_value.length() - 1);
				if (condition.endsWith("and"))
					condition = condition.substring(0, condition.length() - 3);
				String sql = "update " + tableName + " set " + name_value
						+ " where " + condition;
				
				try {
					
					int res = executeUpdate(poolName, sql);
					
					if (res != 1) {
						
						return -5;
					}
					
					return res;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					
				}
				
			} else
				return -2;
		}
		return -2;
	}

	public int deleteRecords(String poolName, String tableName, String pkField,
			String pkFieldType, String[] pkvalues) {
		
		if (tableName == null || tableName.trim().length() < 0
				|| pkField == null || pkField.trim().length() < 0)
			return -1;
		String sql_in = "";
		if(pkvalues == null || pkvalues.length <= 0){
			return 0;
		}
		for(int i = 0; i< pkvalues.length; i++){
			if(Field.FIELD_TYPE_INT.equals(pkFieldType)||Field.FIELD_TYPE_DOUBLE.equals(pkFieldType)){
				sql_in+=pkvalues[i]+",";
			}else{
				sql_in+="'"+StringToZn.toDB(pkvalues[i])+"',";
			}
		}
		if(sql_in.endsWith(","))
			sql_in = sql_in.substring(0, sql_in.length() -1);
		if(sql_in.trim().length() <= 0)
			return 0;
		String sql = "delete from "+tableName+" where "+pkField+" in ("+sql_in+")";
		return this.executeUpdate(poolName, sql);
	}
	public int deleteRecords(Connection conn, String tableName, String pkField,
			String pkFieldType, String[] pkvalues) {
		
		if (tableName == null || tableName.trim().length() < 0
				|| pkField == null || pkField.trim().length() < 0)
			return -1;
		String sql_in = "";
		if(pkvalues == null || pkvalues.length <= 0){
			return 0;
		}
		for(int i = 0; i< pkvalues.length; i++){
			if(Field.FIELD_TYPE_INT.equals(pkFieldType)||Field.FIELD_TYPE_DOUBLE.equals(pkFieldType)){
				sql_in+=pkvalues[i]+",";
			}else{
				sql_in+="'"+StringToZn.toDB(pkvalues[i])+"',";
			}
		}
		if(sql_in.endsWith(","))
			sql_in = sql_in.substring(0, sql_in.length() -1);
		if(sql_in.trim().length() <= 0)
			return 0;
		String sql = "delete from "+tableName+" where "+pkField+" in ("+sql_in+")";
		//System.out.println(sql);
		return this.executeUpdate(conn, sql);
	}
	public String valueInUse(String poolName, String tableName, String fieldname,
			String fieldtype, String[] fieldvalues) {
		
		if (tableName == null || tableName.trim().length() < 0
				|| fieldname == null || fieldname.trim().length() < 0)
			return "";
		String sql_in = "";
		if(fieldvalues == null || fieldvalues.length <= 0){
			return "";
		}
		for(int i = 0; i< fieldvalues.length; i++){
			if(Field.FIELD_TYPE_INT.equals(fieldtype)||Field.FIELD_TYPE_DOUBLE.equals(fieldtype)){
				sql_in+=fieldvalues[i]+",";
			}else{
				sql_in+="'"+StringToZn.toDB(fieldvalues[i])+"',";
			}
		}
		if(sql_in.endsWith(","))
			sql_in = sql_in.substring(0, sql_in.length() -1);
		if(sql_in.trim().length() <= 0)
			return "";
		String sql = "select "+fieldname+" from "+tableName+" where "+fieldname+" in ("+sql_in+")";
		ArrayList records = this.executeQuery(poolName, sql);
		int count = 0;
		String res = "";
		Record r = null;
		if(records != null && records.size() > 0){
			int size = records.size();
			String fieldvalue = "";
			for(int i = 0; i < records.size(); i++){
				if(count >= 10){
					if(res.endsWith(","))
						res = res.substring(0, res.length() -1);
					return "有"+res+"等"+records.size()+"个值正在被使用";
				}
				r = (Record)records.get(i);
				fieldvalue = r.get(fieldname);
				res+=fieldvalue+",";
				count++;
			}
			if(res.endsWith(","))
				res = res.substring(0, res.length() -1);
			return res+"正在被使用";
		}
		return "";
	}
	
	public static void main(String[] args) {
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		TableManage tm = new TableManage();
		tm.setTableName("dc_user");
		ArrayList records = tm.getAllRecords(conn, "", "");
		Record r = (Record) records.get(0);
		System.out.println(r.get("loginname"));
		System.out.println(r.get("loginpsw"));
	}
}
