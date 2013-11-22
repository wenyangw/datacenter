package tms.datacenter.index;

public class FlashPaper extends Thread{
	public static boolean convert(String swfName,String documentName){
		int sleepTime = 0;
		Runtime pro = Runtime.getRuntime();
		try{
			String[] envp = new String[1];       
			String SWFTOOLS_PATH = "C:\\Program Files (x86)\\SWFTools\\";
			envp[0] = "PATH="+SWFTOOLS_PATH;    
			String command = "cmd /c \""+SWFTOOLS_PATH+"pdf2swf\" -z -s flashversion=9 " + documentName + " -o " +swfName ;  
			System.out.println(command);
			Process p = pro.exec(command,envp);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static void main(String[] args){
		boolean res = FlashPaper.convert("C:\\workspace\\datacenter3\\datacenter3\\uploadpic\\5.swf", "C:\\workspace\\datacenter3\\datacenter3\\uploadpic\\5.pdf");
		System.out.println(res);
	}
}
