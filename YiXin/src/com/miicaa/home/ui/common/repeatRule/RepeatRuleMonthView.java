package com.miicaa.home.ui.common.repeatRule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.dataPicker.DataPickerDialog;
import com.miicaa.common.base.numberPicker.NumberPickerDialog;
import com.miicaa.home.R;

/**
 * Created by apple on 13-12-5.
 */
public class RepeatRuleMonthView {
    private Context mContext;
    private View mRootView = null;
    private TextView repeatDayTextView;
    private TextView repeatWeekTextView;
    private TextView repeatTimeTextView;
    private TextView repeatStopTextView;
    private Button repeatDayButton;
    private Button repeatWeekButton;

    private Calendar mDate;
    private RepeatRuleData repeatRuleData;

    private int dayValue = 0 ;

    private int indexValueWeek = 0;
    private int weekValueWeek = 0;
    String[] indexData = {"第一个星期","第二个星期","第三个星期","第四个星期","第五个星期"};
    String[] weekData = {"周一","周二","周三","周四","周五","周六","周日"};
    public RepeatRuleMonthView(Context context, RepeatRuleData repeatRuleData) {
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
        mRootView = inflater.inflate(R.layout.repeat_rule_month, null);

        repeatDayButton = (Button) mRootView.findViewById(R.id.repeat_rule_month_day_button);
        repeatDayButton.setOnClickListener(repeatDayButtonClick);
        repeatWeekButton = (Button) mRootView.findViewById(R.id.repeat_rule_month_week_button);
        repeatWeekButton.setOnClickListener(repeatWeekButtonClick);

        Button repeatTimeButton = (Button) mRootView.findViewById(R.id.repeat_rule_month_times_button);
        repeatTimeButton.setOnClickListener(repeatTimeButtonClick);

        Button repeatStopButton = (Button) mRootView.findViewById(R.id.repeat_rule_month_repeat_stop_button);
        repeatStopButton.setOnClickListener(repeatStopButtonClick);


        HashMap<String, HashMap<String, Object>> repeatData = repeatRuleData.getData();
        HashMap<String, Object> subData = repeatData.get("month");
        Integer repeatTimes = (Integer) subData.get("repeatTimes");
        String repeatStop = (String) subData.get("repeatStop");
        Integer repeatDay = (Integer) subData.get("repeatDay");
        Integer index = (Integer) subData.get("repeatWeekIndex");
        Integer week = (Integer) subData.get("repeatWeekWeek");
        Boolean isDay = (Boolean) subData.get("isDay");

        repeatTimeTextView = (TextView) mRootView.findViewById(R.id.repeat_rule_month_times_textview);
        if (repeatTimes.intValue() > 0) {
            repeatTimeTextView.setText(String.valueOf(repeatTimes.intValue()) + "月重复一次");
        } else {
            repeatTimeTextView.setText("");
        }

        repeatStopTextView = (TextView) mRootView.findViewById(R.id.repeat_rule_month_repeat_stop_textview);
        repeatStopTextView.setText(repeatStop);

        ///
        repeatDayTextView = (TextView) mRootView.findViewById(R.id.repeat_rule_month_day_textview);
        dayValue = repeatDay.intValue();
        if (dayValue > 0) {
            repeatDayTextView.setText("每月"+String.valueOf(dayValue)+"号重复");
        }else{
            repeatDayTextView.setText("");
        }

        ///
        repeatWeekTextView = (TextView) mRootView.findViewById(R.id.repeat_rule_month_week_textview);
        indexValueWeek = index.intValue();
        weekValueWeek = week.intValue();
        if (indexValueWeek > -1 &&weekValueWeek > -1) {
            repeatWeekTextView.setText("每月"+indexData[indexValueWeek]+"的"+weekData[weekValueWeek]+"重复");
        }else{
            indexValueWeek = 0;
            weekValueWeek = 0;
            repeatWeekTextView.setText("");
        }

        if(isDay){
            repeatDayButton.setSelected(true);
            repeatWeekButton.setSelected(false);
        }else{
            repeatDayButton.setSelected(false);
            repeatWeekButton.setSelected(true);
        }
    }

    View.OnClickListener repeatDayButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NumberPickerDialog.EduitBuilder numberPopWin = NumberPickerDialog.builder(mContext);
            numberPopWin.setValue(dayValue);
            numberPopWin.setOnEduitListener(new NumberPickerDialog.OnEduitListener() {
                @Override
                public void onClick(NumberPickerDialog.EduitResult r) {
                    Log.d("EduitResult",String.valueOf(r.mContent));
                    dayValue = r.mContent;
                    repeatDayTextView.setText("每月"+String.valueOf(dayValue)+"号重复");
                    HashMap<String, HashMap<String, Object>> repeatData = repeatRuleData.getData();
                    HashMap<String, Object> subData = repeatData.get("month");
                    subData.put("repeatDay",dayValue);
                    subData.put("isDay",true);
                    setButtonSelect();
                    ((RepeatRuleActivity)mContext).viewShow();
                }
            }).show();


        }
    };

    View.OnClickListener repeatWeekButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DataPickerDialog.EduitBuilder numberPopWin = DataPickerDialog.builder(mContext, indexData,weekData);
            numberPopWin.setValue(indexValueWeek,weekValueWeek);
            numberPopWin.setOnEduitListener(new DataPickerDialog.OnEduitListener() {
                @Override
                public void onClick(DataPickerDialog.EduitResult r) {
                    Log.d("EduitResult",String.valueOf(r.index)+"----"+String.valueOf(r.week));
                    indexValueWeek = r.index;
                    weekValueWeek = r.week;
                    repeatWeekTextView.setText("每月"+indexData[indexValueWeek]+"的"+weekData[weekValueWeek]+"重复");
                    HashMap<String, HashMap<String, Object>> repeatData = repeatRuleData.getData();
                    HashMap<String, Object> subData = repeatData.get("month");
                    subData.put("repeatDay",dayValue);
                    subData.put("repeatWeekIndex",indexValueWeek);
                    subData.put("repeatWeekWeek",weekValueWeek);
                    subData.put("isDay",false);
                    setButtonSelect();
                    ((RepeatRuleActivity)mContext).viewShow();
                }
            }).show();


        }
    };

    View.OnClickListener repeatTimeButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, RepeatRuleSubmitTimeActivity.class);
            Bundle bundle = new Bundle();
            String repeatKey = repeatRuleData.getRepeatKey();
            bundle.putString("repeatKey", repeatKey);
            intent.putExtras(bundle);
            ((Activity) mContext).startActivityForResult(intent, 0);
        }
    };

    private void setButtonSelect(){
        HashMap<String, HashMap<String, Object>> repeatData = repeatRuleData.getData();
        HashMap<String, Object> subData = repeatData.get("month");
        if((Boolean)subData.get("isDay")){
            repeatDayButton.setSelected(true);
            repeatWeekButton.setSelected(false);
        }else{
            repeatDayButton.setSelected(false);
            repeatWeekButton.setSelected(true);
        }
    }

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

    private void upDate() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(mDate.getTime());

        HashMap<String, HashMap<String, Object>> repeatData = repeatRuleData.getData();
        HashMap<String, Object> subData = repeatData.get(repeatRuleData.getRepeatKey());
        subData.put("repeatStop", date);
        repeatStopTextView.setText(date);
//        Toast.makeText(mContext, date, Toast.LENGTH_LONG).show();
        ((RepeatRuleActivity)mContext).viewShow();
    }
}