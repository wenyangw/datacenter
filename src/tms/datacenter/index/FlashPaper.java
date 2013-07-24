package tms.datacenter.index;

public class FlashPaper extends Thread{
	public static boolean convert(String swfName,String documentName){
		int sleepTime = 0;
		Runtime pro = Runtime.getRuntime();
		try{
			String converter = "C:\\Program Files (x86)\\Macromedia\\FlashPaper 2\\FlashPrinter.exe "+ documentName +" -o "+swfName;
			 Process p = pro.exec(converter);
			 p.waitFor();
//			Thread.sleep(sleepTime);
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
