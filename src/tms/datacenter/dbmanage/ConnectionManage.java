package tms.datacenter.dbmanage;

import java.io.*;

import java.sql.*;

import java.util.*;

import java.util.Date;

/**
 * 
 * 管理类DBConnectionManager支持对一个或多个由属性文件定义的数据库连接
 * 
 * 池的访问.客户程序可以调用getInstance()方法访问本类的唯一实例.
 */

public class ConnectionManage {
	static public ConnectionManage instance; // 唯一实例
	static public int clients;
	public Vector drivers = new Vector(); // 驱动
	public Hashtable pools = new Hashtable(); // 连接

	static synchronized public ConnectionManage getInstance() {
		if (instance == null) {
			instance = new ConnectionManage();
		}
		clients++;
		return instance;
	}

	/**
	 * 建构函数私有以防止其它对象创建本类实例
	 */
	private ConnectionManage() {
		init();
	}

	/**
	 * 
	 * 将连接对象返回给由名字指定的连接池
	 * 
	 * @param name
	 *            在属性文件中定义的连接池名字
	 * 
	 * @param con
	 *            连接对象
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
	 * 获得一个可用的(空闲的)连接.如果没有可用连接,且已有连接数小于最大连接数
	 * 
	 * 限制,则创建并返回新连接
	 * 
	 * @param name
	 *            在属性文件中定义的连接池名字
	 * 
	 * @return Connection 可用连接或null
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
	 * 获得一个可用连接.若没有可用连接,且已有连接数小于最大连接数限制, 则创建并返回新连接.否则,在指定的时间内等待其它线程释放连接.
	 * 
	 * @param name
	 *            连接池名字
	 * 
	 * @param time
	 *            以毫秒计的等待时间
	 * 
	 * @return Connection 可用连接或null
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
	 * 关闭所有连接,撤销驱动程序的注册
	 */

	public synchronized void release() {
		// 等待直到最后一个客户程序调用
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
	 * 根据指定属性创建连接池实例.
	 * 
	 * @param props
	 *            连接池属性
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
				System.out.println("成功创建连接池" + dbname);
			}

		}
	}

	/**
	 * 171 * 装载和注册所有JDBC驱动程序
	 * 
	 * 172 *
	 * 
	 * 173 * @param props 属性
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
	 * 读取属性完成初始化
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
		 * 创建新的连接池
		 * 
		 * @param name
		 *            连接池名字
		 * @param URL
		 *            数据库的JDBC URL
		 * @param user
		 *            数据库帐号,或 null
		 * @param password
		 *            密码,或 null
		 * @param maxConn
		 *            此连接池允许建立的最大连接数
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
		 * 将不再使用的连接返回给连接池
		 * 
		 * @param con
		 *            客户程序释放的连接
		 */
		public synchronized void freeConnection(Connection con) {
			// 将指定连接加入到向量末尾
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
		 * 从连接池获得一个可用连接.如没有空闲的连接且当前连接数小于最大连接
		 * 
		 * 数限制,则创建新连接.如原来登记为可用的连接不再有效,则从向量删除之,
		 * 
		 * 然后递归调用自己以尝试新的可用连接.
		 */
		public synchronized Connection getConnection() {
			Connection con = null;
			if (freeConnections.size() > 0) {
				// 获取向量中第一个可用连接
				con = (Connection) freeConnections.firstElement();
				freeConnections.removeElementAt(0);
				try {
					if (con.isClosed()) {
						System.out.println("从连接池" + name + "删除一个无效连接");
						// 递归调用自己,尝试再次获取可用连接
						con = getConnection();
					}
				} catch (SQLException e) {
					System.out.println("从连接池" + name + "删除一个无效连接出错");
					// 递归调用自己,尝试再次获取可用连接
					con = getConnection();
				}
				if (freeConnections.size() > maxConn) {
					System.out.println(" 删除一个溢出连接 ");
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
			// 如果没有可用连接
			if (freeConnections.size() <= 0) {
				con = newConnection();
			}
			// 如果有可用连接
			else if (freeConnections.size() > 0) {
				con = (Connection) freeConnections.firstElement();
//				System.out.println(" [a 连接池可用连接数 ] : " + "[ "
//						+ freeConnections.size() + " ]");
				freeConnections.removeElementAt(0);
//				System.out.println(" [b 连接池可用连接数 ] : " + "[ "
//						+ freeConnections.size() + " ]");
				if(!isConnected(con))
					con = newConnection();
				try {
					if (con.isClosed()) {
//						System.out.println("从连接池" + name + "删除一个无效连接");
						returnConnection();
					}
				} catch (SQLException e) {
//					System.out.println("从连接池" + name + "删除一个无效连接出错");
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
		 * 从连接池获取可用连接.可以指定客户程序能够等待的最长时间 参见前一个getConnection()方法.
		 * 
		 * @param timeout
		 *            以毫秒计的等待时间限制
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
					// wait()返回的原因是超时
					return null;
				}
			}
			return con;
		}

		/**
		 * 
		 * 关闭所有连接
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
		 * 关闭一个连接
		 */
		public synchronized void releaseOne() {
			if (freeConnections.firstElement() != null) {
				Connection con = (Connection) freeConnections.firstElement();
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println("无法关闭连接池" + name + "中的一个连接");

				}
			} else {
				System.out
						.println("releaseOne() bug.......................................................");
			}
		}

		/**
		 * 
		 * 创建新的连接
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
