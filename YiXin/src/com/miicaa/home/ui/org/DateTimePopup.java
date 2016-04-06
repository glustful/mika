package com.miicaa.home.ui.org;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-24.
 */
public class DateTimePopup
{
    protected PopupWindow mPopupWindow = null;
    protected Context mContext = null;
    protected OnDateTimeChange mOnDateTimeListener;
    protected PopupWindow.OnDismissListener mOnDismissListener = null;
    DatePicker mDatePick;
    TimePicker mTimePick;
    NumberPicker mNumberPick;
    RelativeLayout timeHead; 
   
    int maxNum;
    int minNum;
    int nowNum;

    ArrayList<PopupItem> mItems = null;
    OnMessageListener mOnMessageListener = null;
    OnheadViewClickListener onheadViewClickListener_ = null;
    OnNumChangeListener onNumChangeListener;

    Calendar mCalender;
    DateTimeStyle mStyle = DateTimeStyle.eDateTime;
    private String cancel = "取消";
    private String commit = "提交";

    public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

	public String getCommit() {
		return commit;
	}

	public void setCommit(String commit) {
		this.commit = commit;
	}
	private static DateTimeMenuBuilder mBuilder;

    public static DateTimeMenuBuilder builder(Context context)
    {
        mBuilder = new DateTimeMenuBuilder(context);
        return mBuilder;
    }

    private DateTimePopup(Context context)
    {
        this.mContext = context;
    }

    private void setDateTimeStyle(DateTimeStyle style)
    {
        mStyle = style;
    }

    private void setOnDateTimeChangeListener(OnDateTimeChange onDateTimeListener)
    {
        mOnDateTimeListener = onDateTimeListener;
    }

    public void setItems(ArrayList<PopupItem> items)
    {
        mItems = items;
    }

    public void setOnMessageListener(OnMessageListener onMessage)
    {
        mOnMessageListener = onMessage;
    }

    private void setOnDissmion(PopupWindow.OnDismissListener onDissmion)
    {
        mOnDismissListener = onDissmion;
    }

    public void show(View view, int x,int y)
    {
        drawContent();
        mPopupWindow.showAsDropDown(view,x, y);

    }

    public void show(int gravity,int x,int y)
    {
        Activity activity = (Activity)mContext;
        drawContent();
        mPopupWindow.showAtLocation(activity.getWindow().getDecorView(),
                gravity,x, y);


    }
    PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            if(mOnDismissListener != null)
            {
                mOnDismissListener.onDismiss();
            }
        }
    };
	private long minDate = 0;
    
    
   
    
    

    public long getMinDate() {
		return minDate;
	}

	public void setMinDate(long minDate) {
		this.minDate = minDate;
	}

	private void drawContent()
    {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        LinearLayout timepickerview=(LinearLayout)inflater.inflate(R.layout.date_time_layout, null);

        mDatePick = (DatePicker) timepickerview.findViewById(R.id.datePicker);
        mTimePick =(TimePicker) timepickerview.findViewById(R.id.timePicker);
        mNumberPick = (NumberPicker)timepickerview.findViewById(R.id.numberPicker);
        timeHead = (RelativeLayout)timepickerview.findViewById(R.id.timeHead);
       
        mCalender = mCalender==null?Calendar.getInstance(TimeZone.getDefault()):mCalender;
      
        int year =mCalender.get(Calendar.YEAR);
        int month=mCalender.get(Calendar.MONTH);
        int day=mCalender.get(Calendar.DAY_OF_MONTH);
        int hour = mCalender.get(Calendar.HOUR_OF_DAY);
        int minute =mCalender.get(Calendar.MINUTE);
        
        mDatePick.init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalender.set(year, monthOfYear, dayOfMonth);
                if (mOnDateTimeListener != null) {
                    mOnDateTimeListener.onDateTimeChange(mCalender, mStyle);
                }
            }
        });
       
        if(minDate>0){
        	Calendar ca = Calendar.getInstance();
        	ca.setTimeInMillis(minDate);
        	ca.set(Calendar.HOUR_OF_DAY, 0);
        	ca.set(Calendar.MINUTE, 0);
        	ca.set(Calendar.SECOND, 0);
        mDatePick.setMinDate(ca.getTimeInMillis());
        }
        mTimePick.setIs24HourView(true);
        mTimePick.setCurrentHour(hour);
        mTimePick.setCurrentMinute(minute);
      
        mTimePick.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                mCalender.set(mCalender.get(Calendar.YEAR),
                        mCalender.get(Calendar.MONTH),
                        mCalender.get(Calendar.DAY_OF_MONTH),
                        hour,
                        minute);
                if (mOnDateTimeListener != null) {
                    mOnDateTimeListener.onDateTimeChange(mCalender, mStyle);
                }
            }
        });
        
        mNumberPick.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				nowNum = newVal;
				if(onNumChangeListener != null){
					onNumChangeListener.onNumChange(newVal);
				}
			}
		});
        
        if(mStyle == DateTimeStyle.eDate)
        {
            mTimePick.setVisibility(View.GONE);
        }else if(mStyle == DateTimeStyle.eYearMonth){
        	mTimePick.setVisibility(View.GONE);
        	hiddenDay(mDatePick);
        }
        else if(mStyle == DateTimeStyle.eTime)
        {
            mDatePick.setVisibility(View.GONE);
        }else if(mStyle == DateTimeStyle.eNumber){
        	mDatePick.setVisibility(View.GONE);
        	mTimePick.setVisibility(View.GONE);
        	mNumberPick.setVisibility(View.VISIBLE);
        	nowNum = minNum;
        	mNumberPick.setMaxValue(maxNum);
        	mNumberPick.setMinValue(minNum);
        	mNumberPick.setValue(nowNum);
        }
        	timeHead.setVisibility(View.VISIBLE);
        	Button cancel = (Button) timeHead.findViewById(R.id.timeCancle);
        	cancel.setText(this.cancel);
        	cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v){
					
					if(mOnMessageListener != null){
						mOnMessageListener.onClick(new PopupItem("清空","clear"));
					mPopupWindow.dismiss();
					}
					if(onheadViewClickListener_ != null){
						mPopupWindow.dismiss();
						onheadViewClickListener_.cancleClick();
					}
					}
			});
        	Button commit = (Button) timeHead.findViewById(R.id.timeCommit);
        	commit.setText(this.commit);
        	commit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mOnMessageListener != null){
						mOnMessageListener.onClick(new PopupItem("提交","commit"));
					}
					if(onheadViewClickListener_ != null){
						if(mStyle == DateTimeStyle.eNumber){
							onheadViewClickListener_.commitClick(nowNum);
						}else{
						onheadViewClickListener_.commitClick(mCalender);
						}
					}
					mPopupWindow.dismiss();
				}
				
			});

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        timepickerview.setLayoutParams(params);



        LinearLayout rootLayout = new LinearLayout(this.mContext);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        
        rootLayout.addView(timepickerview);

        float scale = mContext.getResources().getDisplayMetrics().density;
        if(mItems != null)
        {
            for(int i = 0; i < mItems.size(); i++)
            {
                Button button = new Button(mContext);
                LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int)(40*scale));
                buttonParam.setMargins((int)(8*scale),(int)(8*scale),(int)(8*scale),(int)(16*scale));
                button.setLayoutParams(buttonParam);
                button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bottom_menu_item_selector));
                button.setTextColor(Color.parseColor("#333333"));
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (14.0));
                button.setText(mItems.get(i).mContent);
                button.setTag(mItems.get(i));
                button.setOnClickListener(itemClickListener);
                rootLayout.addView(button);
            }
        }




        rootLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.an_arange_bottom));
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        mPopupWindow = new PopupWindow(rootLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(onDismissListener);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }
    
    View.OnClickListener itemClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            // TODO Auto-generated method stub
            mPopupWindow.dismiss();
            PopupItem item = (PopupItem) view.getTag();

            if(mOnMessageListener != null)
            {
                mOnMessageListener.onClick(item);
            }
            mBuilder = null;
        }
    };

    public static class DateTimeMenuBuilder
    {
        DateTimePopup mMenu;
        public DateTimeMenuBuilder (Context context)
        {
            mMenu = new DateTimePopup(context);
        }

        public DateTimeMenuBuilder setDateTimeStyle(DateTimeStyle style)
        {
            mMenu.setDateTimeStyle(style);
            return this;
        }
        
        public DateTimeMenuBuilder setCancelText(String cancel){
        	mMenu.setCancel(cancel);
        	return this;
        }
        
        public DateTimeMenuBuilder setCommitText(String commit){
        	mMenu.setCommit(commit);
        	return this;
        }

        public DateTimeMenuBuilder setOnDateTimeChangeListener(OnDateTimeChange onDateTimeListener)
        {
            mMenu.setOnDateTimeChangeListener(onDateTimeListener);
            return this;
        }

        public DateTimeMenuBuilder setItems(ArrayList<PopupItem> items)
        {
            mMenu.setItems(items);
            return this;
        }
        
        public DateTimeMenuBuilder setCurrentTime(Calendar ca)
        {
            mMenu.setCurrentTime(ca);
            return this;
        }

        public DateTimeMenuBuilder setOnMessageListener(OnMessageListener onMessage)
        {
            mMenu.setOnMessageListener(onMessage);
            return this;
        }

        public DateTimeMenuBuilder setOnDissmion(PopupWindow.OnDismissListener onDissmion)
        {
            mMenu.setOnDissmion(onDissmion);
            return this;
        }
        public DateTimeMenuBuilder setOnheadViewClickListener(OnheadViewClickListener listener){
        	mMenu.onheadViewClickListener_ = listener;
        	return this;
        }
        public DateTimeMenuBuilder setOnNumChangeListener(OnNumChangeListener listener){
        	mMenu.onNumChangeListener = listener;
        	return this;
        }

        public void show(View v,int x, int y)
        {
            mMenu.show(v,x,y);
        }

        public void show(int gravity,int x, int y)
        {
            mMenu.show(gravity,x,y);
        }
        public DateTimeMenuBuilder setMaxNum(int num){
        	mMenu.maxNum = num;
        	return this;
        }
        public DateTimeMenuBuilder setMinNum(int num){
        	mMenu.minNum = num; 
        	return this;
        }
        
        public DateTimeMenuBuilder setMinDate(long minDate){
        	mMenu.setMinDate(minDate); 
        	return this;
        }
    }

    public interface OnDateTimeChange
    {
        public void onDateTimeChange(Calendar c, DateTimeStyle style);
    }
    
    public interface OnNumChangeListener{
    	void onNumChange(int num);
    }

    public enum DateTimeStyle
    {
        eDate,
        eTime,
        eDateTime,
        eRemind,
        eNone,
        eNumber,
        eYearMonth
    }

	public void setCurrentTime(Calendar ca) {
		this.mCalender = (Calendar) ca.clone();
		
	}
	
	private void hiddenDay(DatePicker dp){
	if (dp != null) {  
        Class c=dp.getClass();  
        Field f;  
        
        try {  
                if(Integer.valueOf(android.os.Build.VERSION.SDK) > 14){  
                    f = c.getDeclaredField("mDaySpinner");  
                    f.setAccessible(true );    
                    LinearLayout l= (LinearLayout)f.get(dp);     
                    l.setVisibility(View.GONE);  
                }else{  
                    f = c.getDeclaredField("mDayPicker");  
                    f.setAccessible(true );    
                    LinearLayout l= (LinearLayout)f.get(dp);     
                    l.setVisibility(View.GONE);  
                }  
        } catch (SecurityException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (NoSuchFieldException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IllegalArgumentException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }    
          
    }   
	}
}
