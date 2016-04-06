package com.miicaa.home.ui.home;


import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.home.MatterScreenViewAdapter.OnRemoveTypeListener;
import com.miicaa.home.ui.menu.ScreenType;

@EViewGroup(R.layout.matter_screen_view)
public class MatterScreenView extends RelativeLayout{

	static String TAG = "MatterScreenView";
	Integer moreRowY;
	@ViewById(R.id.oneRowLayout)
	LinearLayout oneRowLayout;
	@ViewById(R.id.oneRowGrid)
	GridView oneRowGrid;
	@ViewById(R.id.oneRowBtn)
	ImageButton oneRowBtn;
	@SystemService
	LayoutInflater inflater;
	
	View moreRowView;
	Context context;
	MatterScreenViewAdapter adapter;
	ScreenType screenType;
	RemoveScreenTypeListener removeScreenTypeListener;
	int rowY;
	
	
	public MatterScreenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public MatterScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public MatterScreenView(Context context) {
		super(context);
		this.context = context;
	}
	
	@AfterInject
	void afterInject(){
		screenType = ScreenType.getInstance();
//		screenType.removeAllTypes();
		initTypes();
		initText();
	}
	@AfterViews
	void afterViews(){
		initThis();
		adapter = new MatterScreenViewAdapter(context);
		adapter.setOnRemoveTypeListener(new OnRemoveTypeListener() {
			@Override
			public void removeType() {
//				Log.d(TAG, "removeType......");
				if(removeScreenTypeListener != null){
					removeScreenTypeListener.removeType(screenType);
				}
				refreshView();
			}
		});
		oneRowGrid.setAdapter(adapter);
		initMoreRow();
	}
	
	
	
	private void initThis(){
		this.setSizeChangeListener(new SizeChangeListener() {
			
			@Override
			public void sizeChanged(int w, int h, int oldw, int oldh) {
				rowY = -h;
			}
		});
		
		if(matterTypes.size() == 0){
			this.setVisibility(View.GONE);
			if(moreRowWindow != null &&moreRowWindow.isShowing())
				moreRowWindow.dismiss();
		}
		else{
			this.setVisibility(View.VISIBLE);;
		}
	}
	
	String approvalCodesStr;
	String arrangeCodesStr;
	String createUsersStr;
	String todoUsersStr;
	String beginTime;
	String endTime;
	String srcCodesStr;
	String reportStr;
	
	private void initTypes(){
		approvalCodesStr = screenType.getApprovalCodesStr();
		arrangeCodesStr = screenType.getArrangeCodesStr();
		createUsersStr = screenType.getCreateUserStr();
		todoUsersStr  = screenType.getTodoUserStr();
		beginTime = screenType.getBeginTime();
		endTime = screenType.getEndTime();
		srcCodesStr = screenType.getsrcCodesStr();
		reportStr = screenType.getReportNamesStr();
	}
	
	private void initText(){
		initScrrenTypes("工作报告:", reportStr, MatterScreenType.ReportCodes);
		initScrrenTypes("事项类型:", srcCodesStr,MatterScreenType.SrcCodes);
		initScrrenTypes("任务类型:", arrangeCodesStr,MatterScreenType.ArrangeCodes);
		initScrrenTypes("审批类型:", approvalCodesStr,MatterScreenType.ApprovalCodes);
		initScrrenTypes("创建人:", createUsersStr,MatterScreenType.CreateUsers);
		initScrrenTypes("办理人:", todoUsersStr,MatterScreenType.TodoUsers);
		initScrrenTypes("开始时间:", beginTime,MatterScreenType.BeginTime);
		initScrrenTypes("结束时间:", endTime,MatterScreenType.EndTime);
	}
	
	ArrayList<MatterScreenType> matterTypes = new ArrayList<MatterScreenType>(); 
	private void initScrrenTypes(String str,String type,String mode){
		MatterScreenType screenType = addMatterTypes(str, type, mode);
//		Log.d(TAG, "initScrrenTypes screenType:"+screenType.content);
		if(screenType != null)
		{
			Log.d(TAG, "initScrrenTypes screenType not Null:"+screenType.content);
			matterTypes.add(screenType);
		}
			
	}
	
	private MatterScreenType addMatterTypes(String str,String type,String mode){
		
		String content = getStr(str, type);
//		Log.d(TAG, "addMatterTypes content:"+content);
		if(content != null){
			return new MatterScreenType(content, mode);
		}
			return null;
	}
	
	private String getStr(String content,String type){
		content = (type == null || "".equals(type.trim())) ? null : (content + type);
		return content;
	}
	
	public void clearTypes(){
		screenType.removeAllTypes();
		refreshView();
	}
	
	@Click(R.id.oneRowBtn)
	void oneRowClick(){
		moreRowWindow.showAsDropDown(MatterScreenView.this, 10, rowY);
	}
	
	public void refreshView(){
		matterTypes.clear();
		initTypes();
		initText();
		moreRowViewRefresh(moreRowGroup, matterTypes);
		initThis();
		adapter.refresh(matterTypes);
	}

	PopupWindow moreRowWindow ;
	ScreenViewGroup moreRowGroup;
	void initMoreRow(){
		moreRowView = inflater.inflate(R.layout.matter_screen_pop_view, null);
	    moreRowWindow = new PopupWindow(moreRowView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    moreRowGroup = (ScreenViewGroup)moreRowView.findViewById(R.id.moreRowView);
	    moreRowViewRefresh(moreRowGroup,matterTypes);
		moreRowView.findViewById(R.id.moreRowBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				moreRowWindow.dismiss();
			}
		});
		moreRowWindow.setFocusable(true);
		moreRowWindow.setBackgroundDrawable(new BitmapDrawable());
		moreRowWindow.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				Log.d(TAG, "moreRowWindow is touch with"+event.getAction()+"========"+MotionEvent.ACTION_OUTSIDE);
				if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
					moreRowWindow.dismiss();
				return false;
			}
		});
		moreRowWindow.setOutsideTouchable(true);
	}
	
	private void moreRowViewRefresh(ScreenViewGroup group,ArrayList<MatterScreenType> matterTypes){
		assert group != null;
		group.removeAllViews();
		for(MatterScreenType type : matterTypes){
			group.addView(moreRowAdded(type));
		}
	}
	
	private View moreRowAdded(final MatterScreenType matterType){
		View convertView = LayoutInflater.from(context).inflate(R.layout.matter_screen_text, null);
		TextView textBtn = (TextView)convertView.findViewById(R.id.textBtn);
		textBtn.setText(matterType.content);
		textBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					matterType.removeType(screenType);
					if(removeScreenTypeListener != null){
						removeScreenTypeListener.removeType(screenType);
					}
				}catch(Exception e){
					Log.d(TAG, "remove wrong !");
				}finally{
					refreshView();
				}
			}
		});
		return convertView;
	}
	
	public void setMoreRowY(int y){
		moreRowY = y;
	}
	
	
	SizeChangeListener l;  
	  
    public void setSizeChangeListener(SizeChangeListener orlExt) {  
        l = orlExt;  
    }  
  
    @Override  
    public void onSizeChanged(int w, int h, int oldw, int oldh) {  
        l.sizeChanged(w, h, oldw, oldh);  
        super.onSizeChanged(w, h, oldw, oldh);  
    }  
  
    public interface SizeChangeListener {  
        public void sizeChanged(int w, int h, int oldw, int oldh);  
    }  
    
    public interface RemoveScreenTypeListener{
    	void removeType(ScreenType screenType);
    }
   
    public void setRemoveScreenTypeListener(RemoveScreenTypeListener listener){
    	this.removeScreenTypeListener = listener;
    }
    
    public void onRestart(){
    	screenType = ScreenType.getInstance();
    	Log.d(TAG, "onRestart screentype:"+screenType.getApprovalCodesStr()+
		screenType.getArrangeCodesStr()+
		screenType.getCreateUserStr()+
		screenType.getTodoUserStr()+
		screenType.getBeginTime()+
		screenType.getEndTime()+
		screenType.getsrcCodesStr()+
		screenType.getReportNamesStr());
    }
	
}
