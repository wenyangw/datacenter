package tms.datacenter.dbmanage;

import java.io.*;

import java.sql.*;

import java.util.*;

import java.util.Date;

/**
 * 
 * ������DBConnectionManager֧�ֶ�һ�������������ļ���������ݿ�����
 * 
 * �صķ���.�ͻ�������Ե���getInstance()�������ʱ����Ψһʵ��.
 */

public class ConnectionManage {
	static public ConnectionManage instance; // Ψһʵ��
	static public int clients;
	public Vector drivers = new Vector(); // ����
	public Hashtable pools = new Hashtable(); // ����

	static synchronized public ConnectionManage getInstance() {
		if (instance == null) {
			instance = new ConnectionManage();
		}
		clients++;
		return instance;
	}

	/**
	 * ��������˽���Է�ֹ�������󴴽�����ʵ��
	 */
	private ConnectionManage() {
		init();
	}

	/**
	 * 
	 * �����Ӷ��󷵻ظ�������ָ�������ӳ�
	 * 
	 * @param name
	 *            �������ļ��ж�������ӳ�����
	 * 
	 * @param con
	 *            ���Ӷ���
	 */

	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(con);
		} else {
			System.out.println("pool ==null");
		}
		clients--;
	}

	/**
	 * 
	 * ���һ�����õ�(���е�)����.���û�п�������,������������С�����������
	 * 
	 * ����,�򴴽�������������
	 * 
	 * @param name
	 *            �������ļ��ж�������ӳ�����
	 * 
	 * @return Connection �������ӻ�null
	 */
	public Connection getConnection(String name) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.returnConnection();
		}
		return null;
	}

	/**
	 * 
	 * ���һ����������.��û�п�������,������������С���������������, �򴴽�������������.����,��ָ����ʱ���ڵȴ������߳��ͷ�����.
	 * 
	 * @param name
	 *            ���ӳ�����
	 * 
	 * @param time
	 *            �Ժ���Ƶĵȴ�ʱ��
	 * 
	 * @return Connection �������ӻ�null
	 */

	public Connection getConnection(String name, long time)

	{
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	/**
	 * 
	 * �ر���������,�������������ע��
	 */

	public synchronized void release() {
		// �ȴ�ֱ�����һ���ͻ��������
		if (--clients != 0) {
			return;
		}
		Enumeration allPools = pools.elements();
		while (allPools.hasMoreElements()) {
			DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
			pool.release();
		}
		Enumeration allDrivers = drivers.elements();
		while (allDrivers.hasMoreElements()) {
			Driver driver = (Driver) allDrivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);

			} catch (SQLException e) {

			}
		}
	}

	/**
	 * 
	 * ����ָ�����Դ������ӳ�ʵ��.
	 * 
	 * @param props
	 *            ���ӳ�����
	 */
	private void createPools() {
		DBConfig dbconfig = new DBConfig();
		Hashtable<String, DataBase> databases = dbconfig.getDatabases();
		Set set = databases.keySet();
		Iterator it = set.iterator();
		DataBase db = null;
		while (it.hasNext()) {
			String dbname = (String) it.next();
			db = databases.get(dbname);
			if (db != null) {
				DBConnectionPool pool = new DBConnectionPool(dbname, db
						.getConnstr(), db.getUser(), db.getPassword(), db
						.getMaxcount());
				pools.put(dbname, pool);
				System.out.println("�ɹ��������ӳ�" + dbname);
			}

		}
	}

	/**
	 * 171 * װ�غ�ע������JDBC��������
	 * 
	 * 172 *
	 * 
	 * 173 * @param props ����
	 * 
	 * 174
	 */

	private void loadDrivers()
	{
		DBConfig dbconfig = new DBConfig();
		Hashtable<String, DataBase> databases = dbconfig.getDatabases();
		Set set = databases.keySet();
		Iterator it = set.iterator();
		DataBase db = null;
		while (it.hasNext()) {
			String dbname = (String) it.next();
			db = databases.get(dbname);
			String driver_str = db.getDriver();
			try {
				Driver driver = (Driver)
				Class.forName(driver_str).newInstance();
				DriverManager.registerDriver(driver);
				drivers.addElement(driver);
				System.out.println(driver_str);
			}
			catch (Exception e)
			{
			}
		}
	}

	/**
	 * 
	 * ��ȡ������ɳ�ʼ��
	 */
	private void init() {
		try {
			loadDrivers();
			createPools();
		} catch (Exception e) {
		}
	}

	class DBConnectionPool {
		// private int checkedOut;
		private Vector freeConnections = new Vector();
		private int maxConn;
		private String name;
		private String password;
		private String URL;
		private String user;

		/**
		 * �����µ����ӳ�
		 * 
		 * @param name
		 *            ���ӳ�����
		 * @param URL
		 *            ���ݿ��JDBC URL
		 * @param user
		 *            ���ݿ��ʺ�,�� null
		 * @param password
		 *            ����,�� null
		 * @param maxConn
		 *            �����ӳ������������������
		 */
		public DBConnectionPool(String name, String URL, String user,
				String password, int maxConn) {
			this.name = name;
			this.URL = URL;
			this.user = user;
			this.password = password;
			this.maxConn = maxConn;
//			for(int i = 0; i < maxConn; i++){
//				freeConnections.add(newConnection());
//			}
		}

		/**
		 * ������ʹ�õ����ӷ��ظ����ӳ�
		 * 
		 * @param con
		 *            �ͻ������ͷŵ�����
		 */
		public synchronized void freeConnection(Connection con) {
			// ��ָ�����Ӽ��뵽����ĩβ
			try {
				if (con.isClosed()) {
					System.out.println("before freeConnection con is closed");
				}
				freeConnections.addElement(con);
				Connection contest = (Connection) freeConnections.lastElement();
				if (contest.isClosed()) {
					System.out.println("after freeConnection contest is closed");
				}
				notifyAll();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}

		/**
		 * 
		 * �����ӳػ��һ����������.��û�п��е������ҵ�ǰ������С���������
		 * 
		 * ������,�򴴽�������.��ԭ���Ǽ�Ϊ���õ����Ӳ�����Ч,�������ɾ��֮,
		 * 
		 * Ȼ��ݹ�����Լ��Գ����µĿ�������.
		 */
		public synchronized Connection getConnection() {
			Connection con = null;
			if (freeConnections.size() > 0) {
				// ��ȡ�����е�һ����������
				con = (Connection) freeConnections.firstElement();
				freeConnections.removeElementAt(0);
				try {
					if (con.isClosed()) {
						System.out.println("�����ӳ�" + name + "ɾ��һ����Ч����");
						// �ݹ�����Լ�,�����ٴλ�ȡ��������
						con = getConnection();
					}
				} catch (SQLException e) {
					System.out.println("�����ӳ�" + name + "ɾ��һ����Ч���ӳ���");
					// �ݹ�����Լ�,�����ٴλ�ȡ��������
					con = getConnection();
				}
				if (freeConnections.size() > maxConn) {
					System.out.println(" ɾ��һ��������� ");
					releaseOne();
				}
			} else if ((maxConn == 0) || (freeConnections.size() < maxConn)) {
				con = newConnection();
			}
			return con;
		}

		public synchronized Connection returnConnection()

		{
			Connection con = null;
			// ���û�п�������
			if (freeConnections.size() <= 0) {
				con = newConnection();
			}
			// ����п�������
			else if (freeConnections.size() > 0) {
				con = (Connection) freeConnections.firstElement();
//				System.out.println(" [a ���ӳؿ��������� ] : " + "[ "
//						+ freeConnections.size() + " ]");
				freeConnections.removeElementAt(0);
//				System.out.println(" [b ���ӳؿ��������� ] : " + "[ "
//						+ freeConnections.size() + " ]");
				if(!isConnected(con))
					con = newConnection();
				try {
					if (con.isClosed()) {
//						System.out.println("�����ӳ�" + name + "ɾ��һ����Ч����");
						returnConnection();
					}
				} catch (SQLException e) {
//					System.out.println("�����ӳ�" + name + "ɾ��һ����Ч���ӳ���");
					returnConnection();
				}
			}
			return con;
		}
		private  boolean isConnected(Connection con){
			if (con == null)
				return false;
			Statement stmt = null;
			ResultSet res = null;
			try {
				stmt = con.createStatement();
				res = stmt.executeQuery("select 1");
				return true;
			} catch (SQLException e) {
				return false;
			} finally {
				try {
					if (res != null){
						res.close();
						res = null;
					}
					if (stmt != null){
						stmt.close();
						stmt = null;
					}
				} catch (Exception e) {
				}
			}
		}
		/**
		 * �����ӳػ�ȡ��������.����ָ���ͻ������ܹ��ȴ����ʱ�� �μ�ǰһ��getConnection()����.
		 * 
		 * @param timeout
		 *            �Ժ���Ƶĵȴ�ʱ������
		 */
		public synchronized Connection getConnection(long timeout) {
			long startTime = new Date().getTime();
			Connection con;
			while ((con = getConnection()) == null) {
				try {
					wait(timeout);
				} catch (InterruptedException e) {
				}
				if ((new Date().getTime() - startTime) >= timeout) {
					// wait()���ص�ԭ���ǳ�ʱ
					return null;
				}
			}
			return con;
		}

		/**
		 * 
		 * �ر���������
		 */
		public synchronized void release() {
			Enumeration allConnections = freeConnections.elements();
			while (allConnections.hasMoreElements()) {
				Connection con = (Connection) allConnections.nextElement();
				try {
					con.close();

				} catch (SQLException e) {

				}
			}
			freeConnections.removeAllElements();
		}

		/**
		 * 
		 * �ر�һ������
		 */
		public synchronized void releaseOne() {
			if (freeConnections.firstElement() != null) {
				Connection con = (Connection) freeConnections.firstElement();
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println("�޷��ر����ӳ�" + name + "�е�һ������");

				}
			} else {
				System.out
						.println("releaseOne() bug.......................................................");
			}
		}

		/**
		 * 
		 * �����µ�����
		 */
		private Connection newConnection() {
			Connection con = null;
			try {
				if (user == null) {
					con = DriverManager.getConnection(URL);
				} else {
					con = DriverManager.getConnection(URL, user, password);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			return con;
		}
	}

	public static void main(String[] args) {
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select 1+2 test");
			while (rs.next()) {
				String haha = rs.getString("test");
				System.out.println(haha);
			}
			rs.close();
			st.close();
			cm.freeConnection("datacenter", conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
