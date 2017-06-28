package com.dabai.util;

import java.util.ArrayList;
import java.util.List;

/**************************************************************************
 * $RCSfile: $  $Revision: $  $Date:  $
 *
 * $Log: $
 **************************************************************************/
/*
 * Copyright 2008 gxlu, Inc. All rights reserved.
 * File name : TesMain.java
 * Created on : 2012-9-25 下午2:35:56
 * Creator : Administrator
 */

/**
 * <pre>
 * Description : TODO
 * &#64;author Administrator
 * </pre>
 */
public class TesMain {

	private boolean flag = true;

	private List<Integer> l1 = new ArrayList<Integer>();
	private List<Integer> l2 = new ArrayList<Integer>();
	private List<Integer> l3 = new ArrayList<Integer>();
	private List<Integer> alllist = new ArrayList<Integer>();

	private List<Integer> l4 = new ArrayList<Integer>();

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	private TesMain() {

	}

	public void test1() {
		R r1 = new R(l1);
		Thread t1 = new Thread(r1);

		R r2 = new R(l2);
		Thread t2 = new Thread(r2);

		R r3 = new R(l3);
		Thread t3 = new Thread(r3);

		t1.start();
		t2.start();
		t3.start();
		while (this.isFlag()) {
			if (t1.getState().equals(Thread.State.TERMINATED) && t2.getState().equals(Thread.State.TERMINATED)
					&& t3.getState().equals(Thread.State.TERMINATED)) { // 判断三个线程是否都
																		// 结束</span>

				// 合并三个结果
				for (Integer i : l1) {
					alllist.add(i);
				}
				for (Integer i : l2) {
					alllist.add(i);
				}
				for (Integer i : l3) {
					alllist.add(i);
				}
				// System.out.println("alllist is : " + alllist );
				// this.setFlag(false) ; // 当上面三个线程都终止时，才会进入，
				// System.out.println("-----");
				break;
			}
		}
	}

	private class R implements Runnable {

		private List<Integer> list = null;

		public R(List<Integer> list_) {
			this.list = list_;
		}

		public void run() {
			for (int i = 0; i < 30000; i++) {
				list.add(new Integer(i));
			}
		}

		public List<Integer> getList() {
			return this.list;
		}

	}

	private class R2 implements Runnable {

		private List<Integer> list = null;

		public R2(List<Integer> list_) {
			this.list = list_;
		}

		public void run() {
			for (int i = 0; i < 90000; i++) {
				list.add(new Integer(i));
			}
		}

		public List<Integer> getList() {
			return this.list;
		}

	}

	public void test2() {
		R2 r1 = new R2(l4);
		Thread t1 = new Thread(r1);
		t1.start();
		while (this.isFlag()) {
			if (t1.getState().equals(Thread.State.TERMINATED)) // 判断线程是否结束</span>
			{
				// System.out.println(" l4list is : " + l4 );
				this.setFlag(false);
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		long c1 = System.currentTimeMillis();
		TesMain tm = new TesMain();

		tm.test1(); // 三个线程各完成5万条
		// tm.test2() ; // 一个线程完成15万条
		long c2 = System.currentTimeMillis() - c1;
		System.out.println(" time is : " + c2 / (1000.000));

	}

	public void run() {
		// TODO Auto-generated method stub

	}

}