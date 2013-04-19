package tms.datacenter.sysmanage;

import java.util.ArrayList;
import java.util.Hashtable;

public class Module {
	private String name;
	private String id;
	private String actionclass;
	private String specialparam;
	private Hashtable operations = null;
	private ArrayList operationsList = null;
	public void addOperation(Operation op){
		if(op == null)
			return;
		String opname = op.getName();
		String specialparam = op.getSpecialparam();
		if(specialparam == null)
			specialparam = "";
		if(operations == null)
			operations = new Hashtable();
		operations.put(opname+"·Ö¸ô·û"+specialparam.trim(), op);
	}
	public void addOperationToList(Operation op){
		if(op == null)
			return;
		if(operationsList == null)
			operationsList = new ArrayList();
		operationsList.add(op);
	}
	public ArrayList getOperationsList() {
		return operationsList;
	}
	public void setOperationsList(ArrayList operationsList) {
		this.operationsList = operationsList;
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
	public String getActionclass() {
		return actionclass;
	}
	public void setActionclass(String actionclass) {
		this.actionclass = actionclass;
	}
	public Hashtable getOperations() {
		return operations;
	}
	public void setOperations(Hashtable operations) {
		this.operations = operations;
	}
}
