package com.dabai.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtil {

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean createFile(String fileName) {
		boolean flag = false;
		try {
			File file = new File(fileName);
			// 先创建目录
			File mulu = new File(file.getParent());
			// 如果文件夹不存在则创建
			if (!mulu.exists() && !mulu.isDirectory()) {
				System.out.println("//不存在");
				mulu.mkdir();
			}
			if (!file.exists()) {
				file.createNewFile();
				flag = true;
			}
		} catch (IOException e) {
			/** 如果拋出异常，创建多层目录 */
			new File(new File(fileName).getParent()).mkdirs();
		}
		return true;
	}

	public static boolean writeTxtFile(String content, String fileName) {

		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(fileName);
			o.write(content.getBytes("utf-8"));
			o.close();
			// mm=new RandomAccessFile(fileName,"rw");
			// mm.writeBytes(content);
			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
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
		System.out.println(flag + "文件已写入：" + fileName);
		return flag;
	}
}
