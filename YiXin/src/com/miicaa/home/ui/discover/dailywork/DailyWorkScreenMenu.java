package com.miicaa.home.ui.discover.dailywork;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.miicaa.home.BaseKeyVaule;
import com.miicaa.home.R;
import com.miicaa.home.ui.discover.ScreenMenu;
import com.miicaa.home.ui.discover.ScreenMenuListener;
import com.miicaa.home.ui.menu.BaseScreenView.OnBaseScreenClickLinstener;
import com.miicaa.home.ui.menu.ScrrenViewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.discover_menu_view)
public class DailyWorkScreenMenu extends ScreenMenu{
	
Context mContext;
	
	@ViewById(R.id.screenViewGroup)
	ScrrenViewGroup screenViewGroup;
	@ViewById(R.id.origiatorTextView)
	TextView origiatorTextView;
	@ViewById(R.id.bottomLayout)
	RelativeLayout bottomLayout;
	
	public DailyWorkScreenMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public DailyWorkScreenMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public DailyWorkScreenMenu(Context context) {
		super(context);
		mContext = context;
	}
	

	
	@AfterViews
	void afterView(){
		screenViewGroup.addKeyValueList(getKeyValueList());
		bottomLayout.findViewById(R.id.completeBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onScreenMenuListener != null)
					onScreenMenuListener.onCompleteButtonClick(v);
			}
		});
		bottomLayout.findViewById(R.id.clearBtn).setVisibility(View.GONE);
	}

	
	@Click(R.id.origiatorTextView)
	void origiatorViewClick(View v){
		if(onScreenMenuListener != null)
			onScreenMenuListener.onOrigaintorButtonClick(v);
	}
	
	
	public void refreshScreenViewGroup(OnBaseScreenClickLinstener listener){
		screenViewGroup.refresh(listener);
	}
	
	public void setorigiatorText(String text){
		origiatorTextView.setText(text);
	}
	

	private List<BaseKeyVaule> getKeyValueList(){
			BaseKeyVaule kv1 = new BaseKeyVaule("日报", "10");
			BaseKeyVaule kv2 = new BaseKeyVaule("周报", "20");
			BaseKeyVaule kv3 = new BaseKeyVaule("月报", "30");
			BaseKeyVaule kv4 = new BaseKeyVaule("自定义报告", "40");
			List<BaseKeyVaule> kvList = new ArrayList<BaseKeyVaule>();
			kvList.add(kv1);
			kvList.add(kv2);
			kvList.add(kv3);
			kvList.add(kv4);
			return kvList;
	}
	
	public void refreshViewChage(List<BaseKeyVaule> kvList,String text){
		screenViewGroup.refreshViewChange(kvList);
		setorigiatorText(text);
	}
	
	
	public ScreenMenuListener onScreenMenuListener;
	public void setOnScreenMenuListener(ScreenMenuListener listener){
		onScreenMenuListener = listener;
	}
	

	
}
