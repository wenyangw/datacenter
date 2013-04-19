package tms.datacenter.commontools;

import java.io.UnsupportedEncodingException;

public class StringToZn {
	public static String toZn(String inputStr){
		if(inputStr == null || inputStr.trim().length() <= 0)
			return "";
		try {
			return new String(inputStr.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return inputStr;
	}
	public static String toDB(String s)
    {
        if (s == null)
        {
            return "";
        }

        return s.replaceAll("'", "''");
    }
}
