package tms.datacenter.sysmanage.action;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import tms.datacenter.dbmanage.Record;
import tms.datacenter.sysmanage.MenuConfig;
import tms.datacenter.sysmanage.ParentMenu;
import tms.datacenter.sysmanage.SonMenu;

import com.opensymphony.xwork2.ActionSupport;

public class MenuAction extends ActionSupport {
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpSession session = request.getSession(true);
		Record ruser = (Record)session.getAttribute("dcuser");
		String main = request.getParameter("ismain");
		if(ruser == null)
			return "menu";
		MenuConfig menu = new MenuConfig();
		if("admin".equalsIgnoreCase(ruser.get("loginname"))){
			request.setAttribute("menus", menu.getParentmenus());
			if(main != null && main.trim().length() > 0){
					return "mainmenu";
			}
			return "menu";
		}
		Hashtable userpri = ruser.getUserPrivilege();
		if(userpri == null || userpri.size() <= 0)
			return "menu";
		//MenuConfig menu = new MenuConfig();
		ParentMenu pm = null;
		SonMenu sm = null;
		
		ArrayList pmenus = menu.getParentmenus();
		ArrayList smenus = null;
		ArrayList pmenusshow = new ArrayList();
		ParentMenu pmshow = null;
		if(pmenus != null && pmenus.size() > 0){
			String actionclass = "";
			String moduleid = "";
			int privilege = 0;
			String priuser = "";
			for(int i = 0; i < pmenus.size(); i++){
				pm = (ParentMenu)pmenus.get(i);
				boolean hassonmenu = false;
				pmshow = new ParentMenu();
				if(pm != null){
					pmshow.setId(pm.getId());
					pmshow.setName(pm.getName());
					smenus = pm.getSonmenus();
					if(smenus != null && smenus.size() > 0){
						for(int j = 0; j < smenus.size(); j++){
							sm = (SonMenu)smenus.get(j);
							if(sm != null){
								actionclass = sm.getActionclass();
								moduleid = sm.getId();
								if(moduleid == null)
									moduleid = "";
								privilege = sm.getPrivilege();
								priuser = (String)userpri.get(actionclass+"/"+moduleid);
								if(priuser == null || !priuser.matches("\\d+"))
									priuser = "0";
								if((privilege&Integer.parseInt(priuser)) > 0){
									pmshow.addSonMenu(sm);
									hassonmenu = true;
								}
							}
						}
					}
				}
				if(hassonmenu)
					pmenusshow.add(pmshow);
			}
		}
		request.setAttribute("menus", pmenusshow);
		if(main != null && main.trim().length() > 0)
			return "mainmenu";
		return "menu";
	}
}
