package tms.datacenter.sysmanage;

import java.util.ArrayList;
import java.util.Hashtable;

public class TopModule {
	private String name;
	private String id;
	private Hashtable modules;
	private ArrayList modulesList;
	public void addModule(Module m){
		if(m == null)
			return;
		String actionclass = m.getActionclass();
		String moduleid = m.getId();
		if(moduleid == null)
			moduleid = "";
		if(modules == null)
			modules = new Hashtable();
		modules.put(actionclass+"/"+moduleid, m);
	}
	public void addModuleToList(Module m){
		if(m == null)
			return;
		if(modulesList == null)
			modulesList = new ArrayList();
		modulesList.add(m);
	}
	public ArrayList getModulesList() {
		return modulesList;
	}
	public void setModulesList(ArrayList modulesList) {
		this.modulesList = modulesList;
	}
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
	public Hashtable getModules() {
		return modules;
	}
	public void setModules(Hashtable modules) {
		this.modules = modules;
	}
}
