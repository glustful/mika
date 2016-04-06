package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.miicaa.home.R;
import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Body;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(R.layout.list_item_app102_body)
public class App102BodyItem extends LinearLayout {

	@ViewById
	TextView tv;
	
	public App102BodyItem(Context context) {
		super(context);
	}

	public void bind(Body item) {
		tv.setText(item.content);
	}

}
