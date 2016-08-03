package com.hfax.demo.eventbusdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hfax.demo.eventbusdemo.EventMessage.MessageEvent;
import com.hfax.demo.eventbusdemo.EventMessage.OtherMessage;
import com.hfax.demo.eventbusdemo.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者： lcw on 2016/7/5.
 * 博客： http://blog.csdn.net/lsyz0021/
 */
public class FristFragment extends Fragment {

	private TextView text1;
	private TextView text2;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_view, container, false);

		text1 = (TextView) view.findViewById(R.id.tv_fragment_text1);
		text2 = (TextView) view.findViewById(R.id.tv_fragment_text2);
		return view;
	}

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
	public void MessageEvent(MessageEvent event) {
		text1.setText(event.message);
	}

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
	public void MessageEvent(OtherMessage event) {
		text2.setText(event.message);
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBusUtils.register(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBusUtils.unregister(this);
	}
}
