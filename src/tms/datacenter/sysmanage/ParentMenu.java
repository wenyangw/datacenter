package tms.datacenter.sysmanage;

import java.util.ArrayList;

public class ParentMenu {
	private String name;
	private String id;
	private ArrayList sonmenus = new ArrayList();
	public String getName() {
		return name;
	}
	public void addSonMenu(SonMenu sonmenu){
		sonmenus.add(sonmenu);
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
	public ArrayList getSonmenus() {
		return sonmenus;
	}
	public void setSonmenus(ArrayList sonmenus) {
		this.sonmenus = sonmenus;
	}
}
