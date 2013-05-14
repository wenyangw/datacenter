package tms.datacenter.timertask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tms.datacenter.commontools.DateUtil;

/**
 * Servlet implementation class LoadTaskServlet2
 */
public class LoadTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadTaskServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		System.out.println("加载定时任务Servlet启动成功！");
		TaskConfig tc = new TaskConfig();
		ArrayList tasklist = tc.getTaskslist();
		if (tasklist != null && tasklist.size() > 0) {
			Task task = null;
			String taskid = "";
			String taskname = "";
			String taskclass = "";
			String periodtype = "";
			int periodday = 1;
			int delayday = 0;
			String tasktime = "";
			Timer timer = null;
			for (int i = 0; i < tasklist.size(); i++) {
				timer = new Timer();
				task = (Task) tasklist.get(i);
				taskid = task.getTaskid();
				taskname = task.getTaskname();
				taskclass = task.getTaskclass();
				periodday = task.getPeriodday();
				delayday = task.getDelayday();
				tasktime = task.getTasktime();
				timer = new Timer();
				try {
					timer.schedule((TimerTask) (Class.forName(taskclass)
							.newInstance()), DateUtil.stringToDate(DateUtil.dateToString(DateUtil.dateIncreaseByDay(
							new Date(), delayday))+" "+tasktime,DateUtil.DATETIME_PATTERN), 24 * 60 * 60 * 1000
							* periodday);
					System.out.println("定时任务"+taskname+"("+taskid+"),启动成功，首次执行:"+DateUtil.dateToString(DateUtil.dateIncreaseByDay(
							new Date(), delayday))+" "+tasktime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
