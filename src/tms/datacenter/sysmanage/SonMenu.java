package tms.datacenter.sysmanage;

public class SonMenu {
	private String name;
	private String id;
	private String actionclass;
	private int privilege;
	private String link;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getActionclass() {
		return actionclass;
	}
	public void setActionclass(String actionclass) {
		this.actionclass = actionclass;
	}
	public int getPrivilege() {
		return privilege;
	}
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
