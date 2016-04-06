package com.miicaa.home.ui.discover;

import java.util.List;

import com.miicaa.home.BaseKeyVaule;
import com.miicaa.home.ui.menu.BaseScreenView.OnBaseScreenClickLinstener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class ScreenMenu extends LinearLayout{
	public ScreenMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	Context mContext;
	
	
	public abstract void refreshScreenViewGroup(OnBaseScreenClickLinstener listener);
	
	public abstract void setorigiatorText(String text);
	

	public ScreenMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public ScreenMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	/*private List<BaseKeyVaule> getKeyValueList(){
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
	}*/
	
	public abstract void refreshViewChage(List<BaseKeyVaule> kvList,String text);
	
	
	public ScreenMenuListener onScreenMenuListener;
	public void setOnScreenMenuListener(ScreenMenuListener listener){
		onScreenMenuListener = listener;
	}
	

	
}
