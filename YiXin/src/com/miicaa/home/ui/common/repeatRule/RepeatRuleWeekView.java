package com.miicaa.home.ui.common.repeatRule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.miicaa.common.base.DateHelper;
import com.miicaa.home.R;

/**
 * Created by apple on 13-12-5.
 */
public class RepeatRuleWeekView {
    private Context mContext;
    private View mRootView = null;
    private TextView repeatTimeTextView;
    private TextView repeatStopTextView;
    private Calendar mDate;
    private RepeatRuleData repeatRuleData;

    private Button mondayButton;
    private Button tuesdayButton;
    private Button wednesdayButton;
    private Button thursdayButton;
    private Button fridayButton;
    private Button staurdayButton;
    private Button sundayButton;

    public RepeatRuleWeekView(Context context,RepeatRuleData repeatRuleData) {
        mContext = context;
        this.repeatRuleData = repeatRuleData;
        init();
    }

    public View getRootView() {
        if (mRootView == null) {
            init();
        }
        return mRootView;
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = inflater.inflate(R.layout.repeat_rule_week, null);

        mondayButton = (Button) mRootView.findViewById(R.id.repeat_rule_week_monday);
        mondayButton.setOnClickListener(weekButtonClick);

        tuesdayButton = (Button) mRootView.findViewById(R.id.repeat_rule_week_tuesday);
        tuesdayButton.setOnClickListener(weekButtonClick);

        wednesdayButton = (Button) mRootView.findViewById(R.id.repeat_rule_week_wednesday);
        wednesdayButton.setOnClickListener(weekButtonClick);

        thursdayButton = (Button) mRootView.findViewById(R.id.repeat_rule_week_thursday);
        thursdayButton.setOnClickListener(weekButtonClick);

        fridayButton = (Button) mRootView.findViewById(R.id.repeat_rule_week_friday);
        fridayButton.setOnClickListener(weekButtonClick);

        staurdayButton = (Button) mRootView.findViewById(R.id.repeat_rule_week_staurday);
        staurdayButton.setOnClickListener(weekButtonClick);

        sundayButton = (Button) mRootView.findViewById(R.id.repeat_rule_week_sunday);
        sundayButton.setOnClickListener(weekButtonClick);


        Button repeatTimeButton = (Button) mRootView.findViewById(R.id.repeat_rule_week_times_button);
        repeatTimeButton.setOnClickListener(repeatTimeButtonClick);

        Button repeatStopButton = (Button) mRootView.findViewById(R.id.repeat_rule_week_repeat_stop_button);
        repeatStopButton.setOnClickListener(repeatStopButtonClick);

        repeatTimeTextView = (TextView) mRootView.findViewById(R.id.repeat_rule_week_times_textview);

        HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
        HashMap<String,Object> subData = repeatData.get("week");
        Integer repeatTimes = (Integer)subData.get("repeatTimes");
        String repeatStop = (String)subData.get("repeatStop");
        HashMap<String,Boolean> dayMap = (HashMap<String,Boolean>)subData.get("repeatday");
        if (repeatTimes.intValue()>0){
            repeatTimeTextView.setText(String.valueOf(repeatTimes.intValue())+"周重复一次");
        }else {
            repeatTimeTextView.setText("");
        }
        repeatStopTextView = (TextView) mRootView.findViewById(R.id.repeat_rule_week_repeat_stop_textview);
        repeatStopTextView.setText(repeatStop);
        if (dayMap.get("monday")){
            mondayButton.setSelected(true);
        }else{
            mondayButton.setSelected(false);
        }
        if(dayMap.get("tuesday")){
            tuesdayButton.setSelected(true);
        }else{
            tuesdayButton.setSelected(false);
        }
        if(dayMap.get("wednesday")){
            wednesdayButton.setSelected(true);
        }else{
            wednesdayButton.setSelected(false);
        }
        if(dayMap.get("thursday")){
            thursdayButton.setSelected(true);
        }else{
            thursdayButton.setSelected(false);
        }
        if(dayMap.get("friday")){
            fridayButton.setSelected(true);
        }else{
            fridayButton.setSelected(false);
        }
        if(dayMap.get("staurday")){
            staurdayButton.setSelected(true);
        }else{
            staurdayButton.setSelected(false);
        }
        if(dayMap.get("sunday")){
            sundayButton.setSelected(true);
        }else{
            sundayButton.setSelected(false);
        }
    }

    View.OnClickListener weekButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.isSelected()){
                view.setSelected(false);
            }else {
                view.setSelected(true);
            }
            HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
            HashMap<String,Object> subData = repeatData.get(repeatRuleData.getRepeatKey());
            HashMap<String,Boolean> dayMap = (HashMap<String,Boolean>)subData.get("repeatday");
            if (view.equals(mondayButton)){
                if(view.isSelected()){
                    dayMap.put("monday",true);
                }else {
                    dayMap.put("monday",false);
                }
            }else if(view.equals(tuesdayButton)){
                if(view.isSelected()){
                    dayMap.put("tuesday",true);
                }else {
                    dayMap.put("tuesday",false);
                }
            }else if(view.equals(wednesdayButton)){
                if(view.isSelected()){
                    dayMap.put("wednesday",true);
                }else {
                    dayMap.put("wednesday",false);
                }
            }else if(view.equals(thursdayButton)){
                if(view.isSelected()){
                    dayMap.put("thursday",true);
                }else {
                    dayMap.put("thursday",false);
                }
            }else if(view.equals(fridayButton)){
                if(view.isSelected()){
                    dayMap.put("friday",true);
                }else {
                    dayMap.put("friday",false);
                }
            }else if(view.equals(staurdayButton)){
                if(view.isSelected()){
                    dayMap.put("staurday",true);
                }else {
                    dayMap.put("staurday",false);
                }
            }else if(view.equals(sundayButton)){
                if(view.isSelected()){
                    dayMap.put("sunday",true);
                }else {
                    dayMap.put("sunday",false);
                }
            }
            ((RepeatRuleActivity)mContext).viewShow();
        }
    };

    View.OnClickListener repeatTimeButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, RepeatRuleSubmitTimeActivity.class);
            Bundle bundle = new Bundle();
            String repeatKey = repeatRuleData.getRepeatKey();
            bundle.putString("repeatKey",repeatKey);
            intent.putExtras(bundle);
            ((Activity) mContext).startActivityForResult(intent, 0);
        }
    };

    View.OnClickListener repeatStopButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mDate = DateHelper.getToday();
            new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
				public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    mDate = Calendar.getInstance();
                    mDate.set(i, i2, i3);
                    upDate();
                }
            }, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH), mDate.get(Calendar.DAY_OF_MONTH))
                    .show();
        }
    };

    private void upDate()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(mDate.getTime());

        HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
        HashMap<String,Object> subData = repeatData.get(repeatRuleData.getRepeatKey());
        subData.put("repeatStop",date);
        repeatStopTextView.setText(date);
//        Toast.makeText(mContext, date, Toast.LENGTH_LONG).show();
        ((RepeatRuleActivity)mContext).viewShow();
    }
}