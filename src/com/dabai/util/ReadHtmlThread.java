package com.dabai.util;

import java.util.Date;

import com.dabai.main.Main;

public class ReadHtmlThread implements Runnable {
	private String url;

	public void run() {
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
		System.out.println("抓取结束 ,抓取到链接：" + count + "，耗时：" + s + "秒\t总链接数" + Main.size);

	}

	public ReadHtmlThread() {
		super();
	}

	public ReadHtmlThread(String url) {
		this.url = url;
	}

}
