package com.dabai.util;

import java.util.Date;

import org.apache.log4j.Logger;

import com.dabai.main.Main;

public class ReadHtmlThread implements Runnable {
	Logger log = Logger.getLogger(ReadHtmlThread.class);
	private String url;
	
	public ReadHtmlThread() {
		super();
	}

	public ReadHtmlThread(String url) {
		this.url = url;
	}
	
	public void run() {
		log.info("启动线程抓取"+this.hashCode());
		int count = 1;
		HttpUtil.readHTML(Main.site + url, 0);

		for (int i = 1; i < Main.size; i++) {
			if (Main.htmls[Main.readIndex] != null) {
				HttpUtil.readHTML(Main.site + Main.htmls[Main.readIndex], 0);
				Main.readIndex++;
				count++;
			}
		}
		Date endDate = new Date();
		String s = ((endDate.getTime() - Main.startDate.getTime()) / 1000) + "";
		log.info("线程: "+this.hashCode()+" ,抓取结束 ,抓取到链接：" + count 
				+ "，耗时：" + s + "秒\t总链接数" + Main.size);
	}

}
