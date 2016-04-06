package com.miicaa.home.ui.home;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.checkwork.CheckScreenValue;
import com.miicaa.home.ui.discover.ScreenResult;

public abstract class TopScreenView extends RelativeLayout{

	static String TAG = "ScreenView";
	Integer moreRowY;
	View rootView;
	LinearLayout oneRowLayout;
	GridView oneRowGrid;
	ImageButton oneRowBtn;
	LayoutInflater inflater;
	
	BaseAdapter adapter;
	View moreRowView;
	Context context;
	RemoveScreenTypeListener removeScreenTypeListener;
	int rowY;
	ArrayList<CheckScreenValue> values;
	
	public TopScreenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public TopScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public TopScreenView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	@SuppressLint("InflateParams")
	private void init(){
		initContext(context);
		inflater = LayoutInflater.from(context);
		rootView = inflater.inflate(R.layout.matter_screen_view, null);
		oneRowLayout = (LinearLayout)rootView.findViewById(R.id.oneRowLayout);
		oneRowGrid = (GridView)rootView.findViewById(R.id.oneRowGrid);
		oneRowBtn = (ImageButton)rootView.findViewById(R.id.oneRowBtn);
		oneRowBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				moreRowWindow.showAsDropDown(TopScreenView.this, 10, rowY);
			}
		});
		addView(rootView);
		values = new ArrayList<CheckScreenValue>();
		afterInject();
		afterViews();
	}
	
	void afterInject(){
		values = new ArrayList<CheckScreenValue>();
	}
	void afterViews(){
		adapter = getAdapter();
		if(adapter != null){
			oneRowGrid.setAdapter(adapter);
			oneRowGrid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					CheckScreenValue vaule = values.get(position);
					removeKey(vaule);
				}
			});
		}
		initThis();
	}
	
	public TopScreenView openOrCreateView(){
		initObjects();
		values.clear();
		values.addAll(getScreenTypes());
		initThis();
		refreshAdapter();
		moreRowViewRefresh();
		return this;
	}
	
	public abstract void initObjects();
	public abstract BaseAdapter getAdapter();
	public abstract void refreshAdapter();
	public abstract ArrayList<CheckScreenValue> getScreenTypes();
	public abstract void removeType(CheckScreenValue type);
	public abstract void removeType(int position);
	public abstract void initContext(Context context);
	
	private void initThis(){
		this.setSizeChangeListener(new SizeChangeListener() {
			
			@Override
			public void sizeChanged(int w, int h, int oldw, int oldh) {
				rowY = -h;
			}
		});
		initMoreRow();
		refreshThis();
	}
	
	private void refreshThis(){
		if(values.size() == 0){
			this.setVisibility(View.GONE);
			if(moreRowWindow != null && moreRowWindow.isShowing())
				moreRowWindow.dismiss();
		}
		else{
			this.setVisibility(View.VISIBLE);;
		}
	}
	
	public void removeKey(CheckScreenValue key){
		values.remove(key);
		refreshThis();
		moreRowViewRefresh();
		removeType(key);
		if(removeScreenTypeListener != null)
			removeScreenTypeListener.removeType();
	}
	
	
	
	
//	@Click(R.id.oneRowBtn)
	void oneRowClick(){
		moreRowWindow.showAsDropDown(TopScreenView.this, 10, rowY);
	}
	

	PopupWindow moreRowWindow ;
	ScreenViewGroup moreRowGroup;
	@SuppressLint("InflateParams")
	void initMoreRow(){
		moreRowView = inflater.inflate(R.layout.matter_screen_pop_view, null);
	    moreRowWindow = new PopupWindow(moreRowView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    moreRowGroup = (ScreenViewGroup)moreRowView.findViewById(R.id.moreRowView);
		moreRowView.findViewById(R.id.moreRowBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				moreRowWindow.dismiss();
			}
		});
		moreRowWindow.setFocusable(true);
		moreRowWindow.setBackgroundDrawable(new BitmapDrawable());
		moreRowWindow.setTouchInterceptor(new OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "moreRowWindow is touch with"+event.getAction()+"========"+MotionEvent.ACTION_OUTSIDE);
				if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
					moreRowWindow.dismiss();
				return false;
			}
		});
		moreRowWindow.setOutsideTouchable(true);
	}
	
	private void moreRowViewRefresh(){
		assert moreRowGroup != null;
		moreRowGroup.removeAllViews();
		if(values == null){
			return;
		}
		for(int i = 0; i < values.size(); i ++){
			CheckScreenValue value = values.get(i);
			moreRowGroup.addView(moreRowAdded(value));
		}
		Log.d(TAG, "moreRowViewRefresh group chlidren size"+moreRowGroup.getChildCount());
	}
	
	@SuppressLint("InflateParams")
	private View moreRowAdded(CheckScreenValue value){
		final View convertView = inflater.inflate(R.layout.matter_screen_text, null);
		TextView textBtn = (TextView)convertView.findViewById(R.id.textBtn);
		textBtn.setText(value.mValue);
		textBtn.setTag(value);
		textBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckScreenValue key = (CheckScreenValue)v.getTag();
				removeKey(key);
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
    	if(l != null)
        l.sizeChanged(w, h, oldw, oldh);  
        super.onSizeChanged(w, h, oldw, oldh);  
    }  
  
    public interface SizeChangeListener {  
        public void sizeChanged(int w, int h, int oldw, int oldh);  
    }  
    
    public interface RemoveScreenTypeListener{
    	Object initObjects();
    	void removeType();
    }
   
    public void setRemoveScreenTypeListener(RemoveScreenTypeListener listener){
    	this.removeScreenTypeListener = listener;
    }
    
    public RemoveScreenTypeListener getRemoveTypeListener(){
    	return this.removeScreenTypeListener;
    }

	public TopScreenView createView(ScreenResult mScreenResult) {
		// TODO Auto-generated method stub
		return null;
	}

    
   
	
}
