package com.miicaa.home.ui.discover.crm;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.crm_screen_menu)
public class CRMScreenMenu extends ScreenMenu{
	
Context mContext;
	
	@ViewById(R.id.screenViewGroup)
	ScrrenViewGroup screenViewGroup;
	@ViewById(R.id.origiatorTextView)
	TextView origiatorTextView;
	@ViewById(R.id.bottomLayout)
	RelativeLayout bottomLayout;
	
	public CRMScreenMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public CRMScreenMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public CRMScreenMenu(Context context) {
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
			BaseKeyVaule kv1 = new BaseKeyVaule("待回复", "10");
			BaseKeyVaule kv2 = new BaseKeyVaule("潜在付费用户", "20");
			
			List<BaseKeyVaule> kvList = new ArrayList<BaseKeyVaule>();
			kvList.add(kv1);
			kvList.add(kv2);
			
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
