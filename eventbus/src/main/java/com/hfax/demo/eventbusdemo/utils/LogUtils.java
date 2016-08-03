package com.hfax.demo.eventbusdemo.utils;

import android.util.Log;

/**
 * 作者： lcw on 2016/7/8.
 * 博客： http://blog.csdn.net/lsyz0021/
 */
public class LogUtils {
	private static boolean isShow = true;

	private LogUtils() {
	}

	public static void v(String tag, String msg) {
		if (isShow) {
			Log.v(tag, msg);
		}
	}


}

