package tms.datacenter.upload;

import java.util.ArrayList;
import java.util.List;

public class RuleType {
	
	public static List getRuleTypes(){
		List types = new ArrayList();
		types.add("һ��");
		types.add("ÿ����");
		types.add("ÿ����");
		types.add("ÿ����");
		
		return types;
	}
	
	public static String getRuleType(int position){
		return (String)getRuleTypes().get(position);
	}
}
