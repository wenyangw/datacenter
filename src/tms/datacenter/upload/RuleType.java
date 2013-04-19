package tms.datacenter.upload;

import java.util.ArrayList;
import java.util.List;

public class RuleType {
	
	public static List getRuleTypes(){
		List types = new ArrayList();
		types.add("一次");
		types.add("每几天");
		types.add("每几周");
		types.add("每几月");
		
		return types;
	}
	
	public static String getRuleType(int position){
		return (String)getRuleTypes().get(position);
	}
}
