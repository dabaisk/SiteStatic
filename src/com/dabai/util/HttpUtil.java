package com.dabai.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.dabai.main.Main;

public class HttpUtil {
	static Logger log = Logger.getLogger(HttpUtil.class);

	void test() {
		String strUrl = "";
		System.out.println(strUrl);
		if (!isSite(strUrl)) {
			log.info("非法链接：" + strUrl);
			if (strUrl.lastIndexOf(Main.site) > -1) {
				strUrl = strUrl.substring(Main.site.length());
				System.out.println(strUrl);
				if (!isSite(strUrl)) {
					strUrl = Main.site + "/" + strUrl;
					System.out.println(strUrl);
				}
				if (isSite(strUrl)) {
					log.info("智能匹配为：" + strUrl);
				} else
					return;

			} else
				return;
		}
	}


	public static boolean isSite(String url) {
		url = url.replaceAll("\\\\", "/");
		System.out.println(url);
		// 不允许中文正则
		// ^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$
		/** 第一条正则匹配非首页 */
		Pattern pattern = Pattern.compile(
				"^?([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]|[^x00-xff]+))+.?([A-Za-z0-9-~\\/]|[^x00-xff])+.?([A-Za-z0-9-~\\/]|[^x00-xff])+:?[0-9]?(/?\\?[A-Za-z0-9-~\\/]|[^x00-xff])+$");
		/** 匹配首页规则 */
		Pattern pattern2 = Pattern.compile(
				"^?([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]|[^x00-xff]+))+.?([A-Za-z0-9-~\\/]|[^x00-xff])+.?([A-Za-z0-9-~\\/]|[^x00-xff])+:?[0-9]?$");

		/** http 或者https只允许出现在开始出 */

		if (pattern.matcher(url).matches()) {
			int https = url.toLowerCase().lastIndexOf("https://");
			int http = url.toLowerCase().lastIndexOf("http://");
			if ((http == 0 && https == -1) || (http == -1 && https == 0)) {
				return true;
			}
		} else if (pattern2.matcher(url).matches()) {
			return true;
		}
		return false;
	}

	/** 禁止访问500 400 页面 */
	public static boolean isClick(String code) {
		String[] errCodes = { "500", "400", "404" };
		for (String string : errCodes) {
			if (string.equals(code)) {
				log.info("\n" + code);
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
			log.info("非法链接：" + strUrl);
			if (strUrl.lastIndexOf(Main.site) > -1) {
				strUrl = strUrl.substring(Main.site.length());
				if (!isSite(strUrl)) {
					strUrl = Main.site + "/" + strUrl;
				}
				if (isSite(strUrl)) {
					log.info("智能匹配为：" + strUrl);
				} else
					return;

			} else
				return;
		}
		synchronized (Main.class) {
			/** 检查url是否进入预下载队列 */
			if (Main.isRepeat(strUrl)) {
				log.info("重复队列  跳过");
				return;
			}
		}

		try {
			// 创建一个url对象来指向 该网站链接 括号里()装载的是该网站链接的路径
			log.info("抓取：" + strUrl);
			URL url = new URL(strUrl);
			URLConnection rulConnection = url.openConnection();
			HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
			httpUrlConnection.setConnectTimeout(300000);
			httpUrlConnection.setReadTimeout(300000);
			httpUrlConnection.connect();
			String code = new Integer(httpUrlConnection.getResponseCode()).toString();
			// 抓取之前解析一面状态码
			if (!isClick(code)) {
				log.info("web资源无法访问:" + url);
				return;
			}
			// InputStreamReader 是一个输入流读取器 用于将读取的字节转换成字符
			InputStreamReader isr = new InputStreamReader(httpUrlConnection.getInputStream(), "utf-8"); // 统一使用utf-8
			// 使用 BufferedReader 来读取 InputStreamReader 转换成的字符
			BufferedReader br = new BufferedReader(isr);
			String htmlBody = "";
			int strRead = -1; // new 一个字符串来装载 BufferedReader 读取到的内容
			// 如果 BufferedReader 读到的内容不为空

			while ((strRead = br.read()) > -1) {
				htmlBody += (char) strRead;
			}
			htmlSplit(htmlBody);
			String path = "";
			FileUtil fileUtil = new FileUtil();
			path = FileUtil.isWin() ? "c:/page/www.kleep.xyz" : "/yjdata/www/www_tomcat/ROOT/html";
			if (0 == isHome) {
				// 写入文件
				path += url.getPath();
				fileUtil.createFile(path);
				fileUtil.writeTxtFile(htmlBody, path);
			} else {
				path += "/index.html";
				fileUtil.createFile(path);
				fileUtil.writeTxtFile(htmlBody, path);
			}
			br.close();
		} catch (IOException e) {
			// 如果出错 抛出异常
			e.printStackTrace();
		}
	}

	static void htmlSplit(String htmlBody) {
		String[] suffixs = { "html", "jpg", "jpeg", "gif", "png", "js", "css" };
		String[] prefixs = { "href", "src", "src", "src", "src", "src", "href" };
//		String[] suffixs = { "html", "jpg", "jpeg", "gif", "png", "js", "css" };
//		String[] prefixs = { "href", "src", "src", "src", "src", "src", "href" };
		for (int i = 0; i < suffixs.length; i++) {
			String suffix = suffixs[i];
			String prefix = prefixs[i];
			String[] strBodys = htmlBody.split("." + suffix + "\"");
			for (String string : strBodys) {
				String html = string.substring(string.lastIndexOf(prefix + "=\"") + 6) + "." + suffix;
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
