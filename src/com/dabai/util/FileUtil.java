package com.dabai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

public class FileUtil {
	protected final Logger log = Logger.getRootLogger();

	public static void main(String[] args) {
	}

	public static boolean isWin() {
		String os = System.getProperty("os.name");
		return os.toLowerCase().startsWith("win");
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 * @return 创建状态 true为成功
	 */
	public boolean createFile(String path) {
		boolean flag = false;
		try {
			File file = new File(path);
			// 先创建文件夹
			File folder = new File(file.getParent());
			// 如果文件夹不存在则创建
			if (!folder.exists() && !folder.isDirectory()) {
				log.info("Folder does not exist. Create automatically" + folder.getPath());
				folder.mkdir();
			}
			if (!file.exists()) {
				log.info("File does not exist. Create automatically" + path);
				file.createNewFile();
				flag = true;
			}
		} catch (IOException e) {
			/** 如果拋出异常，创建多层目录 */
			new File(new File(path).getParent()).mkdirs();
		}
		return flag;
	}

	public static String fileRead(String str) {

		int line = 0;
		String FileContent = "";
		try {
			FileInputStream fis = new FileInputStream(str);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			while ((line = isr.read()) != -1) {
				FileContent += (char) line;
			}
			isr.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FileContent;
	}

	public boolean writeTxtFile(String content, String path) {

		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(path);
			o.write(content.getBytes("utf-8"));
			o.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mm != null) {
				try {
					mm.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		log.info("file write status：" + flag + ", file path: " + path);
		return flag;
	}

}
