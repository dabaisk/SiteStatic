package com.dabai.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.dabai.main.Main;

public class HttpUtil {

	private static ArrayList<String> readList = new ArrayList<String>();// 已读取URL

	public static boolean isSite(String url) {
		// 不允许中文正则
		// ^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$
		Pattern pattern = Pattern.compile(
				"^?([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]|[^x00-xff]+).)+([A-Za-z0-9-~\\/]|[^x00-xff])+$");

		/** http 或者https只允许出现在开始出 */

		if (pattern.matcher(url).matches()) {
			int https = url.toLowerCase().lastIndexOf("https://");
			int http = url.toLowerCase().lastIndexOf("http://");
			if ((http == 0 && https == -1) || (http == -1 && https == 0)) {
				return true;
			}
		}
		return false;
	}

	/** 禁止访问500 400 页面 */
	public static boolean isClick(String code) {
		String[] errCodes = { "500", "400", "404" };
		for (String string : errCodes) {
			if (string.equals(code)) {
				System.out.print("\n" + code);
				return false;
			}
		}
		return true;
	}

	/**
	 * 读取页面信息
	 * 
	 * @param strUrl
	 *            抓取页面地址
	 * @param isHome
	 *            是否是首页
	 */
	public static void readHTML(String strUrl, int isHome) {

		if (!isSite(strUrl)) {
			System.out.println("非法链接,智能跳过：" + strUrl);
			return;
		}
		if (Main.isRepeat(strUrl)) {
			return;
		}
		synchronized (Main.site) {

			/** 防止多线程重新加载页面 */
			for (String string : readList) {
				if (string.equals(strUrl)) {
					return;
				}
			}
			readList.add(strUrl);
		}
		try {
			// 创建一个url对象来指向 该网站链接 括号里()装载的是该网站链接的路径
			URL url = new URL(strUrl);
			URLConnection rulConnection = url.openConnection();
			HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
			httpUrlConnection.setConnectTimeout(300000);
			httpUrlConnection.setReadTimeout(300000);
			httpUrlConnection.connect();
			String code = new Integer(httpUrlConnection.getResponseCode()).toString();
			// 抓取之前解析一面状态码
			if (!isClick(code)) {
				System.out.println("web资源无法访问:" + url);
				return;
			}
			// InputStreamReader 是一个输入流读取器 用于将读取的字节转换成字符
			InputStreamReader isr = new InputStreamReader(httpUrlConnection.getInputStream(), "utf-8"); // 统一使用utf-8
			// 编码模式
			// 使用 BufferedReader 来读取 InputStreamReader 转换成的字符
			BufferedReader br = new BufferedReader(isr);
			String htmlBody = "";
			String strRead = ""; // new 一个字符串来装载 BufferedReader 读取到的内容
			// 如果 BufferedReader 读到的内容不为空

			while ((strRead = br.readLine()) != null) {
				htmlBody += strRead;
			}
			htmlSplit(htmlBody);
			/**
			String[] strBodys = htmlBody.split(".html\"");
			for (String string : strBodys) {
				String html = string.substring(string.lastIndexOf("href=\"") + 6) + ".html";
				if (html.lastIndexOf("\"") > 0) {
					continue;
				} else if (html.lastIndexOf(".com") > 0) {
					continue;
				}
				Main.add(html);
			}
*/
			String path = "c:/page/www.kleep.xyz";
			if (0 == isHome) {
				// 写入文件
				path += url.getPath();
				FileUtil.createFile(path);
				FileUtil.writeTxtFile(htmlBody, path);
			} else {
				path += "/index.html";
				FileUtil.createFile(path);
				FileUtil.writeTxtFile(htmlBody, path);
			}
			// 当读完数据后 记得关闭 BufferReader
			br.close();
		} catch (IOException e) {
			// 如果出错 抛出异常
			e.printStackTrace();
		}
	}

	static void htmlSplit(String htmlBody) {
		String[] suffix = { "html", "shtml" };
		for (int i = 0; i < suffix.length; i++) {
			String[] strBodys = htmlBody.split("." + suffix[i]+"\"");
			for (String string : strBodys) {
				String html = string.substring(string.lastIndexOf("=\"") + 2) + "." + suffix[i] + "";

				if (html.lastIndexOf("\"") > 0) {
					continue;
				} else if (html.lastIndexOf(".com") > 0) {
					continue;
				}
				Main.add(html);
			}
		}
	}
}
