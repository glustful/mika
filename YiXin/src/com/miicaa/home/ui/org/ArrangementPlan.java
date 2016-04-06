package com.miicaa.home.ui.org;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.detail.MatterDetailAcrtivity;
import com.miicaa.detail.MatterDetailAcrtivity_;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.org.DateTimePopup.DateTimeStyle;
import com.miicaa.home.ui.org.DateTimePopup.OnDateTimeChange;
import com.miicaa.utils.AllUtils;

/**
 * Created by Administrator on 13-12-24.
 */

@EActivity(R.layout.layout_plan_activity)
public class ArrangementPlan extends Activity {

	static String TAG = "ArrangementPlan";
	
	@ViewById(R.id.headView)
	RelativeLayout headView;
    @ViewById(R.id.beginTime)
    Button beginTimeBtn;
    @ViewById(R.id.endTime)
    Button endTimeBtn;
    
    @Extra
    String clientcode;
    @Extra 
    String todoid;
    @Extra
    String dataid;
    @Extra
    String beginTimeStr;
    @Extra
    String endTimeStr;

    Toast toast;
    String tmpBeginTimeStr;
    String tmpEndTimeStr;
    @AfterInject
    void afterInject(){
    	tmpBeginTimeStr = beginTimeStr;
    	tmpEndTimeStr = endTimeStr;
    	 toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }
    
    @AfterViews
    void afterViews(){
    	beginTimeStr = beginTimeStr != null ? beginTimeStr : "";
    	endTimeStr = endTimeStr != null ?endTimeStr : "";
    	beginTimeBtn.setText(beginTimeStr);
    	endTimeBtn.setText(endTimeStr);
    	TextView title = (TextView)headView.findViewById(R.id.headTitle);
    	title.setText("设置计划时间");
    	Button cancleButton = (Button)headView.findViewById(R.id.cancleButton);
    	cancleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
    	cancleButton.setVisibility(View.VISIBLE);
    	
    	Button commitButton = (Button)headView.findViewById(R.id.commitButton);
    	commitButton.setText("提交");
    	headView.findViewById(R.id.commitButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(beginTimeStr.length() != 0 || endTimeStr.length() != 0){
				Long begin = getNormalDate(beginTimeStr) != null ? getNormalDate(beginTimeStr).getTime() : null;
				Long end = getNormalDate(endTimeStr) != null ? getNormalDate(endTimeStr).getTime() : null;
				Log.d(TAG, "begin and endtime is :"+String.valueOf(begin)+"..."+String.valueOf(end));
				Long now = System.currentTimeMillis();
				if(end != null && begin != null){
				if(end < begin){
					toast.setText("结束时间必须在开始时间之后");
					toast.show();
					return;
				}
				}else{
					if(end == null)
					toast.setText("请输入结束时间");
					else
					toast.setText("请输入开始时间");
					toast.show();
					return;
				}
				 if( now > begin){
				    toast.setText("开始时间必须大于现在时间");
				    toast.show();
					return;
				}
		}
				requestPlan();
			}
		});
    }
    
    
    private Date getNormalDate(String str){
    	if(str.length() == 0){
    		return null;
    	}
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Date date = new Date();
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "string to date failed!"+e.getMessage());
			e.printStackTrace();
		}
    	return date;
    }
    
    @Click(R.id.beginTime)
    void beginClick(){
    	popToTime(CurrentState.eBegin);
    }
    @Click(R.id.endTime)
    void endClick(){
    	popToTime(CurrentState.eEnd);
    }
    @Click(R.id.clearTime)
    void clearTimeClick(){
    	beginTimeStr = "";
    	endTimeStr = "";
    	beginTimeBtn.setText(beginTimeStr);
    	endTimeBtn.setText(endTimeStr);
    }
    
    
    void popToTime(final CurrentState state){
    	ArrayList<PopupItem> items = new ArrayList<PopupItem>();
    	items.add(new PopupItem("清除", "clear"));
    	DateTimePopup.builder(this)
    	.setItems(items)
    	.setDateTimeStyle(DateTimeStyle.eRemind)
    	.setOnDateTimeChangeListener(new OnDateTimeChange() {
			
			@Override
			public void onDateTimeChange(Calendar c, DateTimeStyle style) {
				Date date = c.getTime();
				String time = AllUtils.getnormalTime(date.getTime());
				setTimeInText(time, state);
				
			}
		}).setOnheadViewClickListener(new OnheadViewClickListener() {
			
			@Override
			public void commitClick(int num) {
				
			}
			
			@Override
			public void commitClick(Calendar calendar) {
				Date date = calendar.getTime();
				String time = AllUtils.getnormalTime(date.getTime());
				setTimeInText(time, state);
			}
			
			@Override
			public void cancleClick() {
				
			}
		}).setOnMessageListener(new OnMessageListener() {
			
			@Override
			public void onClick(PopupItem msg) {
				if("clear".equals(msg)){
					setTimeInText("", state);
				}
			}
		})
		.show(Gravity.BOTTOM, 0, 0);
    }  
    
    private void setTimeInText(CurrentState state){
    	if(state == CurrentState.eBegin){
    		if( "".equals(beginTimeStr.trim())||beginTimeStr.equals(tmpBeginTimeStr)){
    			beginTimeStr = AllUtils.getnormalTime(System.currentTimeMillis());
    		}
    		Log.d(TAG, "beginstr in without move the kongjian"+beginTimeStr);
    		beginTimeBtn.setText(beginTimeStr);
    	}else if(state == CurrentState.eEnd){
    		if("".equals(endTimeStr.trim())||endTimeStr.equals(tmpEndTimeStr)){
    			endTimeStr = AllUtils.getnormalTime(System.currentTimeMillis());
    		}
    		endTimeBtn.setText(endTimeStr);
    	}
    }
    
    private void setTimeInText(String timeStr,CurrentState state){
    	if(CurrentState.eBegin.equals(state)){
			beginTimeStr = timeStr;
			beginTimeBtn.setText(beginTimeStr);
		}else if(CurrentState.eEnd.equals(state)){
			endTimeStr = timeStr;
			endTimeBtn.setText(endTimeStr);
		}
    }

    void requestPlan(){
    	String url = "/home/phone/thing/setplantime";
    	Log.d(TAG, "requestPlan beginTime:"+beginTimeStr+"endTime:"+endTimeStr);
    	new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				// TODO Auto-generated method stub
				if(data.getResultState() == ResultState.eSuccess){
					Toast.makeText(ArrangementPlan.this, "设置成功！", Toast.LENGTH_SHORT).show();
					setResult(Activity.RESULT_OK);
				finish();
				if(MatterDetailAcrtivity.getInstance()!=null)
				MatterDetailAcrtivity.getInstance().refreshthis();
				}else{
					Toast.makeText(ArrangementPlan.this, "设置失败.", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("clientCode",clientcode)
		.addParam("todoId",todoid)
		.addParam("dataId", dataid)
		.addParam("planTime", beginTimeStr)
		.addParam("planTimeEnd",endTimeStr)
		.notifyRequest();
    }
    
    enum CurrentState
    {
        eBegin,
        eEnd
    }
}