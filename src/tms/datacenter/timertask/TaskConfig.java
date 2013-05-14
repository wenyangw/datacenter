package tms.datacenter.timertask;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TaskConfig {
	private Hashtable<String, Task> taskshash = new Hashtable<String, Task>();
	private ArrayList taskslist = new ArrayList();

	public TaskConfig() {
		loadTaskConfig();
	}

	private void loadTaskConfig() {
		try {
			String cpath = java.net.URLDecoder.decode(TaskConfig.class
					.getResource("/").getPath()
					+ File.separatorChar + "taskconfig.xml", "gb2312");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(cpath);
			Element root = doc.getRootElement();
			Iterator tasks = root.elementIterator("task");
			Task task = null;
			while (tasks.hasNext()) {
				task = new Task();
				Element task_e = (Element) tasks.next();
				String taskid = getAttribute(task_e, "taskid");
				String taskname = getAttribute(task_e, "taskname");
				String taskclass = getAttribute(task_e, "taskclass");
				String periodday = getAttribute(task_e, "periodday");
				String delayday = getAttribute(task_e, "delayday");
				String tasktime = getAttribute(task_e, "tasktime");
				if (periodday == null || !periodday.matches("\\d+"))
					periodday = "0";
				if (delayday == null || !delayday.matches("\\d+"))
					delayday = "0";
				task.setTaskid(taskid);
				task.setTaskname(taskname);
				task.setTaskclass(taskclass);
				task.setPeriodday(Integer.parseInt(periodday));
				task.setDelayday(Integer.parseInt(delayday));
				task.setTasktime(tasktime);
				taskshash.put(taskid, task);
				taskslist.add(task);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Hashtable<String, Task> getTaskshash() {
		return taskshash;
	}

	public ArrayList getTaskslist() {
		return taskslist;
	}

	private String getAttribute(Element e, String an) {
		Attribute attribute = e.attribute(an);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	public static void main(String[] args) {
		TaskConfig taskconfig = new TaskConfig();
		ArrayList al = taskconfig.getTaskslist();
		Task task = null;
		for (int i = 0; i < al.size(); i++) {
			task = (Task) al.get(i);
			System.out.println(task.getDelayday());
			System.out.println(task.getTaskclass());
			System.out.println(task.getTaskid());
			System.out.println(task.getTaskname());
			System.out.println(task.getTasktime());
		}

	}
}
