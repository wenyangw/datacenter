package tms.datacenter.sysmanage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class PrivilegeConfig {
	private ArrayList modules = new ArrayList();
	public static PrivilegeConfig pc = null;
	
	public static PrivilegeConfig getInstance(){
		if(pc == null)
			pc = new PrivilegeConfig();
		return pc;
	}
	private PrivilegeConfig() {
		loadPrivilegeConfig();
	}
	private void loadPrivilegeConfig() {
		try {
			String cpath = java.net.URLDecoder.decode(PrivilegeConfig.class.getResource("/").getPath()+ File.separatorChar + "privilege.xml", "gb2312");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(cpath);
			Element root = doc.getRootElement();
			Iterator topsit = root.elementIterator("topmodule");
			TopModule tmodule = null;
			Module module = null;
			Operation operation = null;
			while (topsit.hasNext()) {
				tmodule = new TopModule();
				Element top_module = (Element) topsit.next();
				String tname = getAttribute(top_module, "name");
				String tid = getAttribute(top_module, "id");
				tmodule.setName(tname);
				tmodule.setId(tid);
				Iterator moduleit = top_module.elementIterator("module");
				while (moduleit.hasNext()) {
					module = new Module();
					Element e_module = (Element) moduleit.next();
					String mname = getAttribute(e_module, "name");
					String mid = getAttribute(e_module, "id");
					String actionclass = getAttribute(e_module, "actionclass");
					module.setName(mname);
					module.setId(mid);
					module.setActionclass(actionclass);
					Iterator operationit = e_module.elementIterator("operation");
					while (operationit.hasNext()) {
						operation = new Operation();
						Element e_operation = (Element) operationit.next();
						String oname = getAttribute(e_operation, "name");
						String ocnname = getAttribute(e_operation, "cnname");
						String ospecialparam = getAttribute(e_operation, "specialparam");
						String oid = getAttribute(e_operation, "id");
						String oprivilegevalue = getAttribute(e_operation,"privilegevalue");
						String showpos = getAttribute(e_operation,"showpos");
						String needconfirm = getAttribute(e_operation,"needconfirm");
						String selectcount = getAttribute(e_operation,"selectcount");
						
						if(!oprivilegevalue.matches("\\d+"))
							continue;
						operation.setName(oname);
						operation.setCnname(ocnname);
						operation.setId(oid);
						operation.setSpecialparam(ospecialparam);
						operation.setShowpos(showpos);
						operation.setPrivilegevalue(Integer.parseInt(oprivilegevalue));
						operation.setNeedconfirm(needconfirm);
						if(selectcount == null || !selectcount.matches("\\d+"))
							selectcount = "0";
						operation.setSelectcount(selectcount);
						
						module.addOperation(operation);
						module.addOperationToList(operation);
					}
					tmodule.addModule(module);
					tmodule.addModuleToList(module);
				}
				modules.add(tmodule);
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

	public ArrayList getModules() {
		return modules;
	}
	public static void main(String[] args){
		
	}
}
