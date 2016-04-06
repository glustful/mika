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
public class RepeatRuleDayView {
    private Context mContext;
    private View mRootView = null;
    private TextView repeatTimeTextView;
    private TextView repeatStopTextView;
    private Calendar mDate;
    private RepeatRuleData repeatRuleData;
    public RepeatRuleDayView(Context context,RepeatRuleData repeatRuleData) {
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
        mRootView = inflater.inflate(R.layout.repeat_rule_day, null);

        Button repeatTimeButton = (Button) mRootView.findViewById(R.id.repeat_rule_day_times_button);
        repeatTimeButton.setOnClickListener(repeatTimeButtonClick);

        Button repeatStopButton = (Button) mRootView.findViewById(R.id.repeat_rule_day_repeat_stop_button);
        repeatStopButton.setOnClickListener(repeatStopButtonClick);

        repeatTimeTextView = (TextView) mRootView.findViewById(R.id.repeat_rule_day_times_textview);

        HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
        HashMap<String,Object> subData = repeatData.get("day");
        Integer repeatTimes = (Integer)subData.get("repeatTimes");
        String repeatStop = (String)subData.get("repeatStop");
        if (repeatTimes.intValue()>0){
            repeatTimeTextView.setText(String.valueOf(repeatTimes.intValue())+"天重复一次");
        }else {
            repeatTimeTextView.setText("");
        }

        repeatStopTextView = (TextView) mRootView.findViewById(R.id.repeat_rule_day_repeat_stop_textview);
        repeatStopTextView.setText(repeatStop);
    }

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
//            try
//            {
//                Date tempDate = new SimpleDateFormat("yyyy-MM-dd").parse(mDateText.getText().toString());
//                mDate.setTime(tempDate);
//            }
//            catch (ParseException e)
//            {
//                e.printStackTrace();
//            }

            new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
				public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    mDate = Calendar.getInstance();
                    mDate.set(i, i2, i3);
                    upDateBirthday();
                }
            }, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH), mDate.get(Calendar.DAY_OF_MONTH))
                    .show();
        }
    };
    private void upDateBirthday()
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