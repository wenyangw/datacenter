package tms.datacenter.sysmanage;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MenuConfig {
	private ArrayList parentmenus = new ArrayList();

	public MenuConfig() {
		loadMenuConfig();
	}
	public ArrayList getParentmenus() {
		return parentmenus;
	}
	private void loadMenuConfig() {
		try {
			String cpath = java.net.URLDecoder.decode(MenuConfig.class.getResource("/").getPath()+ File.separatorChar + "menu.xml", "gb2312");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(cpath);
			Element root = doc.getRootElement();
			Iterator pmenuit = root.elementIterator("parentmenu");
			ParentMenu pmenu = null;
			SonMenu smenu = null;
			String pname = "";
			String pid = "";
			String sid = "";
			String sname="";
			String actionclass="";
			String privilege="";
			String link = "";
			while (pmenuit.hasNext()) {
				pmenu = new ParentMenu();
				Element pmenu_e = (Element) pmenuit.next();
				pname = getAttribute(pmenu_e, "name");
				pid = getAttribute(pmenu_e, "id");
				pmenu.setName(pname);
				pmenu.setId(pid);
				Iterator smenuit = pmenu_e.elementIterator("sonmenu");
				while (smenuit.hasNext()) {
					
					smenu = new SonMenu();
					Element smenu_e = (Element) smenuit.next();
					sname = getAttribute(smenu_e, "name");
					sid = getAttribute(smenu_e, "id");
					actionclass = getAttribute(smenu_e, "actionclass");
					privilege = getAttribute(smenu_e, "privilege");
					link = getAttribute(smenu_e, "link");
					
					smenu.setName(sname);
					smenu.setId(sid);
					smenu.setActionclass(actionclass);
					if(privilege == null || !privilege.matches("\\d+"))
						privilege = "0";
					smenu.setPrivilege(Integer.parseInt(privilege));
					smenu.setLink(link);
					pmenu.addSonMenu(smenu);
				}
				parentmenus.add(pmenu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getAttribute(Element e, String an) {
		Attribute attribute = e.attribute(an);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	public static void main(String[] args){
		
	}
}
