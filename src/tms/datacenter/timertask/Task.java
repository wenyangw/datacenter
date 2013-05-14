package tms.datacenter.timertask;

public class Task {
	String taskid;
	String taskname;
	String taskclass;
	int periodday;
	int delayday;
	String tasktime;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getTaskclass() {
		return taskclass;
	}
	public void setTaskclass(String taskclass) {
		this.taskclass = taskclass;
	}
	
	public int getPeriodday() {
		return periodday;
	}
	public void setPeriodday(int periodday) {
		this.periodday = periodday;
	}
	public int getDelayday() {
		return delayday;
	}
	public void setDelayday(int delayday) {
		this.delayday = delayday;
	}
	public String getTasktime() {
		return tasktime;
	}
	public void setTasktime(String tasktime) {
		this.tasktime = tasktime;
	}
}
