package com.lsyz0021.eventbusdemo.activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lsyz0021.eventbus.EventBusUtils;
import com.lsyz0021.eventbusdemo.EventMessage.MessageEvent;
import com.lsyz0021.eventbusdemo.EventMessage.MyEvent;
import com.lsyz0021.eventbusdemo.EventMessage.OtherMessage;
import com.lsyz0021.eventbusdemo.FristFragment;
import com.lsyz0021.eventbusdemo.R;
import com.lsyz0021.eventbusdemo.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者： lcw on 2016/7/4.
 * 博客： http://blog.csdn.net/lsyz0021/
 */
public class SecondActivity extends FragmentActivity implements View.OnClickListener {

	private TextView tv_text1;
	private TextView tv_text2;
	private Button btn_send;
	public Button btn_open;
	private String tag = " SecondActivity  ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		tv_text1 = (TextView) findViewById(R.id.tv_secondActivity_message1);
		tv_text2 = (TextView) findViewById(R.id.tv_secondActivity_message2);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fl_second, new FristFragment());
		ft.commit();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.btn_second_send_msg:

				EventBus.getDefault().post(new MessageEvent("SecondActivity发布MessageEvent消息了"));
//				EventBus.getDefault().postSticky(new MessageEvent("SecondActivity发布MessageEvent消息了"));

				break;
			case R.id.btn_second_send_otherMsg:

				EventBus.getDefault().post(new OtherMessage("SecondActivity发布OtherMessage消息了"));
//				EventBus.getDefault().postSticky(new OtherMessage("SecondActivity发布OtherMessage消息了"));
				break;
			case R.id.btn_second_open:

				EventBusUtils.register(this);
				break;
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
	public void MessageEvent(MessageEvent event) {
		tv_text1.setText(event.message);
	}

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
	public void MessageEvent(OtherMessage event) {
		tv_text2.setText(event.message);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		EventBusUtils.register(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBusUtils.unregister(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("SecondActivty退出了");
	}

	/**
	 * 事件从哪个线程发布出来的，onMessageEventPost()方法就会在该线程中运行，
	 * <p/>
	 * 如果发送事件的线程是UI线程，则在UI线程执行，
	 * 如果发送事件的线程是子线程，则在该子线程中执行
	 */
	@Subscribe(threadMode = ThreadMode.POSTING, priority = 2, sticky = true)
	public void onMessageEventPost(MyEvent.Message event) {
		if (isMainThread()) {
			tv_text2.setText(event.msg);
			LogUtils.v(tag, event.msg + " priority = 2 ……UI线程 POSTING id = " + Thread.currentThread().getId());
		} else {
			LogUtils.v(tag, event.msg + " priority = 2 ……非UI线程 POSTING id = " + Thread.currentThread().getId());
		}
	}

	/**
	 * 判断当前线程是否为主线程
	 */
	public boolean isMainThread() {

		return Looper.myLooper() == Looper.getMainLooper();
	}

}
