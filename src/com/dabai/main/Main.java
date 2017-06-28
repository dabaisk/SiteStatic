package com.dabai.main;

import java.util.Date;

import com.dabai.util.HttpUtil;
import com.dabai.util.ReadHtmlThread;

/**
 * Main主函数 用于数据采集
 * 
 */
public class Main {
	/** 记录程序开始时间 */
	public final static Date startDate = new Date();
	/** 配置网站主域名 */
	public final static String site = "http://119.28.13.165:8080";
	/** html链接存储 */
	public static String[] htmls = new String[500];
	/** html链接总数 */
	public static int size = 0;
	/** 多线程统计下载进度 */
	public static int readIndex = 0;

	public static void main(String[] args) {
		while (true) {
			System.out.println("抓取起始页");
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
			 try {
		            Thread.sleep(1800000);
		        } catch (InterruptedException e) {
		            e.printStackTrace(); 
		        }
		}

	}

	/**
	 * 处理
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isRepeat(String str) {
		for (String string : htmls) {
			if (str.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 去重复添加url下载序列
	 * 
	 * @param str
	 * @return
	 */
	public static boolean add(String url) {
		if (isRepeat(url)) {
			return false;
		}
		htmls[size] = url;
		size++;
		return true;

	}

}
