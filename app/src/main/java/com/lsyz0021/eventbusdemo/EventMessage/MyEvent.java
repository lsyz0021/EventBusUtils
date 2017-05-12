package com.lsyz0021.eventbusdemo.EventMessage;

/**
 * 作者： lcw on 2016/7/7.
 * 博客： http://blog.csdn.net/lsyz0021/
 */
public class MyEvent {

	public static class Message {

		public String msg;

		public Message(String msg) {
			this.msg = msg;
		}
	}


	public static class OtherMessage {
		public String msg;

		public OtherMessage(String msg) {
			this.msg = msg;
		}
	}


}
