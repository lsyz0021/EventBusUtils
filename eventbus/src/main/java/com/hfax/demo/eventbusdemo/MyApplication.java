package com.hfax.demo.eventbusdemo;

import android.app.Application;

import com.bandeng.MyEventBusIndex;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者： lcw on 2016/7/6.
 * 博客： http://blog.csdn.net/lsyz0021/
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 启用EventBus3.0加速功能
		EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();

	}
}
