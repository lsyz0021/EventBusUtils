package com.hfax.demo.eventbusdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.hfax.demo.eventbusdemo.EventMessage.MessageEvent;
import com.hfax.demo.eventbusdemo.EventMessage.MyEvent;
import com.hfax.demo.eventbusdemo.EventMessage.OtherMessage;
import com.hfax.demo.eventbusdemo.FristFragment;
import com.hfax.demo.eventbusdemo.R;
import com.hfax.demo.eventbusdemo.utils.EventBusUtils;
import com.hfax.demo.eventbusdemo.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends FragmentActivity {

	private TextView text1;
	private TextView text2;
	private String tag = " MainActivity  ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		text1 = (TextView) findViewById(R.id.tv_main_text1);
		text2 = (TextView) findViewById(R.id.tv_main_text2);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fl_main, new FristFragment());
		ft.commit();

//		new Thread(new myRunnable()).start();
	}

	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.btn_send_msg:

//				EventBus.getDefault().post(new MessageEvent("MainActivity发布MessageEvent消息了"));
				new Thread(new myRunnable()).start();
//				EventBusUtils.removeAllStickyEvents();
//				EventBusUtils.removeStickyEvent(MyEvent.Message.class);

				break;
			case R.id.btn_send_othermsg:

//				EventBus.getDefault().post(new OtherMessage("MainActivity发布OtherMessage消息了"));
				EventBusUtils.postSticky(new MyEvent.Message(" 发布UI消息 "));
//				EventBus.getDefault().post(new MyEvent.Message(" 发布UI消息 "));
				LogUtils.v(tag, " 在UI线程发送的post消息 id = " + Thread.currentThread().getId());
				break;
			case R.id.btn_send_reg:

				EventBusUtils.register(this);
				break;
			case R.id.btn_send_open:

				Intent intent = new Intent(this, SecondActivity.class);
				startActivity(intent);
				break;
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
	public void onMessageEventMain(MessageEvent event) {
		text1.setText(event.message);
	}

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
	public void onMessageEventMain(OtherMessage event) {
		text2.setText(event.message);
	}


	/**********************************
	 * 测试线程类型用
	 ************************************/

	/***
	 * 接收事件方法可以通过@Subscribe(priority = 1),priority的值来决定接收事件的顺序,
	 * 数值越高优先级越大,默认优先级为0.(注意这里优先级设置只有在同一个线程模型才有效)
	 */

	/**
	 * 不管从哪个线程发出的事件，MAIN模式都会在UI（主线程）线程执行onMessageEventMain()方法，
	 * <p/>
	 * 在这里可以更新UI的操作，不可以执行耗时的操作
	 */
	@Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
	public void onMessageEventMain(MyEvent.Message event) {
		text2.setText(event.msg);
		LogUtils.v(tag, event.msg + " priority = 1 ……MAIN id = " + Thread.currentThread().getId());
	}

	/**
	 * 事件从哪个线程发布出来的，onMessageEventPost()方法就会在该线程中运行，
	 * <p/>
	 * 如果发送事件的线程是UI线程，则在UI线程执行，
	 * 如果发送事件的线程是子线程，则在该子线程中执行
	 */
	@Subscribe(threadMode = ThreadMode.POSTING, priority = 2,sticky = true)
	public void onMessageEventPost(MyEvent.Message event) {
		if (isMainThread()) {
			text2.setText(event.msg);
			LogUtils.v(tag, event.msg + " priority = 2 ……UI线程 POSTING id = " + Thread.currentThread().getId());
		} else {
			LogUtils.v(tag, event.msg + " priority = 2 ……非UI线程 POSTING id = " + Thread.currentThread().getId());
		}
	}

	/**
	 * 如果发送事件的线程是UI线程，则重新创建新的子线程执行onMessageEventPost()方法，
	 * 因此不能执行更新UI的操作
	 * <p/>
	 * 如果发送事件的线程是子线程，则在该子线程中执行onMessageEventPost()方法
	 */
	@Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 3)
	public void onMessageEventBackground(MyEvent.Message event) {
		if (Looper.myLooper() == Looper.getMainLooper()) {

			text2.setText(event.msg);
			LogUtils.v(tag, event.msg + " priority = 3 ……UI线程 BACKGROUND id = " + Thread.currentThread().getId());
		} else {

			LogUtils.v(tag, event.msg + " priority = 3 ……非UI线程 BACKGROUND id = " + Thread.currentThread().getId());
		}
	}

	/**
	 * 不管从哪个线程发出的事件，ASYNC模式都会创建一个新的子线程来执行onEventAsync()方法，
	 * <p/>
	 * 所以在这里不能执行更新UI的操作，可以执行耗时的操作
	 */
	@Subscribe(threadMode = ThreadMode.ASYNC, priority = 4)
	public void onMessageEventAsync(MyEvent.Message event) {
//		text2.setText(event.msg);	// 不能在这里执行更新ui的操作
		LogUtils.v(tag, event.msg + " priority = 4 ……Async id = " + Thread.currentThread().getId());

		EventBus.getDefault().removeStickyEvent(event);
	}

	/***
	 * 粘性事件，能够收到订阅之前发送的消息。但是它只能收到最新的一次消息，
	 * 比如说在未订阅之前已经发送了多条黏性消息了，然后再订阅只能收到最近的一条消息。
	 */

	/***
	 * 注意：
	 *
	 * @Subscribe 下的方法必须为public
	 * postSticky()发送的粘性消息订阅时必须@Subscribe(sticky = true)否则接收不到
	 * 发送的event事件是object类
	 * @Subscribe(priority = 1) 使用时优先级默认为0，然后只有统一模式下设置优先级才有效果，自己看着合理使用
	 *
	 */
	@Override
	protected void onStart() {
		super.onStart();
		EventBusUtils.register(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBusUtils.unregister(this);
	}

	/**
	 * 判断当前线程是否为主线程
	 */
	public boolean isMainThread() {

		return Looper.myLooper() == Looper.getMainLooper();
	}

	public class myRunnable implements Runnable {

		@Override
		public void run() {

			try {
				URL url = new URL("http://www.baidu.com");

				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				int responseCode = connection.getResponseCode();
				System.out.println("responseCode = " + responseCode);
				if (responseCode == 200) {
					EventBusUtils.post(new MyEvent.Message("子线程发送的消息"));
					LogUtils.v(tag, "在子线程发送的post消息 id = " + Thread.currentThread().getId());
//					System.out.println("网络请求成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("网络请求异常");
			}
		}

	}


}
