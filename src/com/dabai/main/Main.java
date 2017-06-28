package com.dabai.main;

import java.util.Date;

import com.dabai.util.HttpUtil;
import com.dabai.util.ReadHtmlThread;
import com.dabai.util.TesMain;

/**
 * Main主函数 用于数据采集
 * 
 */
public class Main {

	//线程计时
	public final static  Date startDate=new Date();
	public final static String site = "http://www.kleep.xyz:8080";

	public static String[] htmls = new String[500];
	public static int size = 0;
	public static int readIndex = 0;
	private static int lastSize = 499;

	public static void main(String[] args) {

		long c1 = System.currentTimeMillis();

		ThreadService();

		long c2 = System.currentTimeMillis() - c1;
		System.out.println(" time is : " + c2 / (1000.000));

	}

	public static void ThreadService() {
		Date startDate = new Date();
		HttpUtil.readHTML(site, 1);
		Date endDate = new Date();
		String s = ((endDate.getTime() - startDate.getTime()) / 1000) + "";
		System.out.println("起始页抓取结束 ,抓取到链接：" + size + "，耗时：" + s + "秒");
		int count = 8;
		readIndex = count;
		for (int i = 0; i < count; i++) {
			System.out.println(size);
			ReadHtmlThread readHtml1 = new ReadHtmlThread(htmls[i]);
			new Thread(readHtml1).start();
		}
		
	}

	public static boolean isRepeat(String str) {
		for (String string : htmls) {
			if (str.equals(string)) {
				return true;
			}
		}
		return false;
	}

	public boolean addLast(String str) {
		if (htmls[lastSize] != null) {
			System.out.println("空间已满");
			return false;
		}
		htmls[lastSize] = str;
		lastSize--;
		return true;
	}

	public static boolean add(String str) {
		if (isRepeat(str)) {
			return false;
		}
		htmls[size] = str;
		size++;
		return true;

	}

}
