package tms.datacenter.test;

import java.util.Calendar;

import tms.datacenter.commontools.DateUtil;

public class DateTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DateTest dt = new DateTest();
		String start = "2012-12-28";
		String last = "2013-01-06";
		System.out.print(DateUtil.daysBetween(DateUtil.stringToDate(start, "yyyy-MM-dd"), DateUtil.stringToDate(last, "yyyy-MM-dd")));
	}

}
