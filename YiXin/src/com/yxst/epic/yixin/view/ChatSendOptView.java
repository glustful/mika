package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.miicaa.home.R;

@EViewGroup(R.layout.view_chat_send_opt)
public class ChatSendOptView extends RelativeLayout {

	public ChatSendOptView(Context context) {
		super(context);
	}

	public ChatSendOptView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChatSendOptView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setVisible(boolean b) {
		this.setVisibility(b ? View.VISIBLE : View.GONE);
	}
}
