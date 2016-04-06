package com.miicaa.home.ui.common.repeatRule;

import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.common.base.DateHelper;
import com.miicaa.home.R;

/**
 * Created by apple on 13-12-5.
 */
public class RepeatRuleActivity extends Activity {
    private static final int DAY_TAG = 1;
    private static final int WEEK_TAG = 2;
    private static final int MONTH_TAG = 3;
    private static final int YEAR_TAG = 4;

    private View dayView;
    private View weekView;
    private View monthView;
    private View yearView;

    private Button repeatNoButton;
    private Button repeatDayButton;
    private Button repeatWeekButton;
    private Button repeatMonthButton;
    private Button repeatYearButton;

    private TextView title;

    private RepeatRuleData repeatRuleData;

    int todayDay;
    int todayMonth;
    int todayWeekDay;
    int todayYear;

    String[] indexData = {"第一个星期","第二个星期","第三个星期","第四个星期","第五个星期"};
    String[] weekData = {"周一","周二","周三","周四","周五","周六","周日"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeat_rule_activity);
        Calendar mDate= DateHelper.getToday();
        todayDay = mDate.get(Calendar.DAY_OF_MONTH);
        todayMonth = mDate.get(Calendar.MONTH)+1;
        todayYear = mDate.get(Calendar.YEAR);
        todayWeekDay = mDate.get(Calendar.DAY_OF_WEEK)-1;
        repeatRuleData = new RepeatRuleData();

        HashMap<String,Object> subDataNo = new HashMap<String,Object>();
        subDataNo.put("repeatTimes",Integer.valueOf(0));
        subDataNo.put("repeatStop","");

        HashMap<String,Object> subDataDay = new HashMap<String,Object>();
        subDataDay.put("repeatTimes",Integer.valueOf(1));
        subDataDay.put("repeatStop","");

        HashMap<String,Boolean> dayMap = new HashMap<String, Boolean>();
        dayMap.put("monday",false);
        dayMap.put("tuesday",false);
        dayMap.put("wednesday",false);
        dayMap.put("thursday",false);
        dayMap.put("friday",false);
        dayMap.put("staurday",false);
        dayMap.put("sunday",false);
        switch (todayWeekDay){
            case 0:
                dayMap.put("sunday",true);
                break;
            case 1:
                dayMap.put("monday",true);
                break;
            case 2:
                dayMap.put("tuesday",true);
                break;
            case 3:
                dayMap.put("wednesday",true);
                break;
            case 4:
                dayMap.put("thursday",true);
                break;
            case 5:
                dayMap.put("friday",true);
                break;
            case 6:
                dayMap.put("staurday",true);
                break;
        }
        HashMap<String,Object> subDataWeek = new HashMap<String,Object>();
        subDataWeek.put("repeatTimes",Integer.valueOf(1));
        subDataWeek.put("repeatStop","");
        subDataWeek.put("repeatday",dayMap);

        HashMap<String,Object> subDataMonth = new HashMap<String,Object>();
        subDataMonth.put("repeatTimes",Integer.valueOf(1));
        subDataMonth.put("repeatStop","");
        subDataMonth.put("repeatDay",Integer.valueOf(todayDay));
        subDataMonth.put("repeatWeekIndex",Integer.valueOf(-1));
        subDataMonth.put("repeatWeekWeek",Integer.valueOf(-1));
        subDataMonth.put("isDay",true);

        HashMap<String,Object> subDataYear = new HashMap<String,Object>();
        subDataYear.put("repeatTimes",Integer.valueOf(1));
        subDataYear.put("repeatStop","");
        String today = String.valueOf(todayMonth)+"-"+String.valueOf(todayDay);
        subDataYear.put("repeatStartDay",today);

        HashMap<String ,HashMap<String,Object>> data = new HashMap<String, HashMap<String, Object>>();
        data.put("no",subDataNo);
        data.put("day",subDataDay);
        data.put("week",subDataWeek);
        data.put("month",subDataMonth);
        data.put("year",subDataYear);
        repeatRuleData.setData(data);
        repeatRuleData.setRepeatKey("no");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String repeatKey = bundle.getString("repeatKey");
            int repeatTimes = bundle.getInt("repeatTimes");
            String repeatStop = bundle.getString("repeatStop");
            String repeatStartDay = bundle.getString("repeatStartDay");
            repeatRuleData.setRepeatKey(repeatKey);

            HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
            HashMap<String,Object> bundleData = repeatData.get(repeatKey);
            bundleData.put("repeatTimes",Integer.valueOf(repeatTimes));
            bundleData.put("repeatStop", repeatStop);
            bundleData.put("repeatStartDay", repeatStartDay);

            if(repeatKey.equals("week")){
                String weekStr = bundle.getString("repeatday");
                HashMap<String,Boolean> weekdayMap = new HashMap<String, Boolean>();
                String[] weekArray = weekStr.split("、");
                for(int i = 0;i<weekArray.length;i++){
                    if (weekArray[i].equals("周一")){
                        weekdayMap.put("monday",true);
                    }
                    if(weekArray[i].equals("周二")){
                        weekdayMap.put("tuesday",true);
                    }
                    if(weekArray[i].equals("周三")){
                        weekdayMap.put("wednesday",true);
                    }
                    if(weekArray[i].equals("周四")){
                        weekdayMap.put("thursday",true);
                    }
                    if(weekArray[i].equals("周五")){
                        weekdayMap.put("friday",true);
                    }
                    if(weekArray[i].equals("周六")){
                        weekdayMap.put("staurday",true);
                    }
                    if(weekArray[i].equals("周日")){
                        weekdayMap.put("sunday",true);
                    }
                }
                bundleData.put("repeatday", weekdayMap);
            }
            if(repeatKey.equals("month")){
                Integer repeatDay = bundle.getInt("repeatDay");
                Integer index = bundle.getInt("repeatWeekIndex");
                Integer week = bundle.getInt("repeatWeekWeek");
                Boolean isDay = bundle.getBoolean("isDay");
                bundleData.put("repeatDay", repeatDay);
                bundleData.put("index", index);
                bundleData.put("week", week);
                bundleData.put("isDay", isDay);
            }
        }
        viewShow();
    }

    public void viewShow() {// 数据加载完成后才会进行页面的显示
        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.repeat_rule_activity_content_layout);
        rootLayout.removeAllViews();

        RepeatRuleDayView repeatRuleDayView = new RepeatRuleDayView(RepeatRuleActivity.this, repeatRuleData);
        dayView = repeatRuleDayView.getRootView();
        dayView.setVisibility(View.GONE);
        rootLayout.addView(dayView);

        RepeatRuleWeekView repeatRuleWeekView = new RepeatRuleWeekView(RepeatRuleActivity.this, repeatRuleData);
        weekView = repeatRuleWeekView.getRootView();
        weekView.setVisibility(View.GONE);
        rootLayout.addView(weekView);

        RepeatRuleMonthView repeatRuleMonthView = new RepeatRuleMonthView(RepeatRuleActivity.this, repeatRuleData);
        monthView = repeatRuleMonthView.getRootView();
        monthView.setVisibility(View.GONE);
        rootLayout.addView(monthView);

        RepeatRuleYearView repeatRuleYearView = new RepeatRuleYearView(RepeatRuleActivity.this, repeatRuleData);
        yearView = repeatRuleYearView.getRootView();
        yearView.setVisibility(View.GONE);
        rootLayout.addView(yearView);


        repeatNoButton = (Button) findViewById(R.id.repeat_rule_activity_no_repeat_button);
        repeatNoButton.setOnClickListener(repeatNoButtonClick);

        repeatDayButton = (Button) findViewById(R.id.repeat_rule_activity_day_repeat_button);
        repeatDayButton.setOnClickListener(repeatDayButtonClick);

        repeatWeekButton = (Button) findViewById(R.id.repeat_rule_activity_week_repeat_button);
        repeatWeekButton.setOnClickListener(repeatWeekButtonClick);

        repeatMonthButton = (Button) findViewById(R.id.repeat_rule_activity_month_repeat_button);
        repeatMonthButton.setOnClickListener(repeatMonthButtonClick);

        repeatYearButton = (Button) findViewById(R.id.repeat_rule_activity_year_repeat_button);
        repeatYearButton.setOnClickListener(repeatYearButtonClick);

        ///
        Button returnButton = (Button) findViewById(R.id.repeat_rule_activity_back_button);
        returnButton.setOnClickListener(returnButtonClick);

        Button submitButton = (Button) findViewById(R.id.repeat_rule_activity_save_button);
        submitButton.setOnClickListener(submitButtonClick);

        title = (TextView) findViewById(R.id.repeat_rule_activity_title);
        textchange();

        if(repeatRuleData.getRepeatKey().equals("no")){
            contentViewShow(0);
        }else if(repeatRuleData.getRepeatKey().equals("day")){
            contentViewShow(DAY_TAG);
        }else if(repeatRuleData.getRepeatKey().equals("week")){
            contentViewShow(WEEK_TAG);
        }else if(repeatRuleData.getRepeatKey().equals("month")){
            contentViewShow(MONTH_TAG);
        }else if(repeatRuleData.getRepeatKey().equals("year")){
            contentViewShow(YEAR_TAG);
        }
    }
    private void textchange(){
        if(repeatRuleData.getRepeatKey().equals("no")){
            title.setText("重复摘要：无");
        }else if(repeatRuleData.getRepeatKey().equals("day")){
            HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
            HashMap<String,Object> subData = repeatData.get("day");
            Integer repeatTimes = (Integer)subData.get("repeatTimes");
            String repeatStop = (String)subData.get("repeatStop");
            if (repeatTimes>1){
                if (repeatStop.length()>0){
                    title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"天重复，直到"+repeatStop);
                }else{
                    title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"天重复");
                }
            }else{
                if (repeatStop.length()>0){
                    title.setText("重复摘要：每天重复，直到"+repeatStop);
                }else{
                    title.setText("重复摘要：每天重复");
                }

            }
        }else if(repeatRuleData.getRepeatKey().equals("week")){
            HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
            HashMap<String,Object> subData = repeatData.get("week");
            Integer repeatTimes = (Integer)subData.get("repeatTimes");
            String repeatStop = (String)subData.get("repeatStop");
            HashMap<String,Boolean> dayMap = (HashMap<String,Boolean>)subData.get("repeatday");
            String weekStr = new String();

            if (dayMap.get("monday")){
                weekStr = "周一";
            }
            if(dayMap.get("tuesday")){
                if(weekStr.length()<1){
                    weekStr = "周二";
                }else{
                    weekStr = weekStr+"、周二";
                }
            }
            if(dayMap.get("wednesday")){
                if(weekStr.length()<1){
                    weekStr = "周三";
                }else{
                    weekStr = weekStr+"、周三";
                }
            }
            if(dayMap.get("thursday")){
                if(weekStr.length()<1){
                    weekStr = "周四";
                }else{
                    weekStr = weekStr+"、周四";
                }
            }
            if(dayMap.get("friday")){
                if(weekStr.length()<1){
                    weekStr = "周五";
                }else{
                    weekStr = weekStr+"、周五";
                }
            }
            if(dayMap.get("staurday")){
                if(weekStr.length()<1){
                    weekStr = "周六";
                }else{
                    weekStr = weekStr+"、周六";
                }
            }
            if(dayMap.get("sunday")){
                if(weekStr.length()<1){
                    weekStr = "周日";
                }else{
                    weekStr = weekStr+"、周日";
                }
            }
            if(weekStr.equals("周六、周日")){
                weekStr = "周末";
            }
            if(weekStr.equals("周一、周二、周三、周四、周五")){
                weekStr = "工作日";
            }
            if(weekStr.equals("周一、周二、周三、周四、周五、周六、周日")){
                weekStr = "每天";
            }
            if (repeatTimes>1){
                if (repeatStop.length()>0){
                    title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"周,"+weekStr+"重复，直到"+repeatStop);
                }else{
                    title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"周,"+weekStr+"重复");
                }
            }else{
                if (repeatStop.length()>0){
                    title.setText("重复摘要：每周,"+weekStr+"重复，直到"+repeatStop);
                }else{
                    title.setText("重复摘要：每周,"+weekStr+"重复");
                }

            }
        }else if(repeatRuleData.getRepeatKey().equals("month")){
            HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
            HashMap<String,Object> subData = repeatData.get("month");
            Integer repeatTimes = (Integer)subData.get("repeatTimes");
            String repeatStop = (String)subData.get("repeatStop");
            Integer repeatDay = (Integer) subData.get("repeatDay");
            Integer index = (Integer) subData.get("repeatWeekIndex");
            Integer week = (Integer) subData.get("repeatWeekWeek");
            Boolean isDay = (Boolean) subData.get("isDay");
            if(isDay){
                if (repeatTimes>1){
                    if (repeatStop.length()>0){
                        title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"月,"+repeatDay+"号重复，直到"+repeatStop);
                    }else{
                        title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"月,"+repeatDay+"号重复");
                    }
                }else{
                    if (repeatStop.length()>0){
                        title.setText("重复摘要：每月"+repeatDay+"号重复，直到"+repeatStop);
                    }else{
                        title.setText("重复摘要：每月"+repeatDay+"号重复");
                    }

                }
            }else{
                if (repeatTimes>1){
                    if (repeatStop.length()>0){
                        title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"月,"+indexData[index]+weekData[week]+"重复，直到"+repeatStop);
                    }else{
                        title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"月,"+indexData[index]+weekData[week]+"重复");
                    }
                }else{
                    if (repeatStop.length()>0){
                        title.setText("重复摘要：每月"+indexData[index].substring(0,3)+weekData[week]+"重复，直到"+repeatStop);
                    }else{
                        title.setText("重复摘要：每月"+indexData[index].substring(0,3)+weekData[week]+"重复");
                    }

                }
            }

        }else if(repeatRuleData.getRepeatKey().equals("year")){
            HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
            HashMap<String,Object> subData = repeatData.get("year");
            Integer repeatTimes = (Integer)subData.get("repeatTimes");
            String repeatStop = (String)subData.get("repeatStop");
            String repeatStartDay = (String)subData.get("repeatStartDay");
            if (repeatTimes>1){
                if (repeatStop.length()>0){
                    title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"年"+repeatStartDay+"重复，直到"+repeatStop);
                }else{
                    title.setText("重复摘要：每"+String.valueOf(repeatTimes)+"年"+repeatStartDay+"重复");
                }
            }else{
                if (repeatStop.length()>0){
                    title.setText("重复摘要：每年"+repeatStartDay+"重复，直到"+repeatStop);
                }else{
                    title.setText("重复摘要：每年"+repeatStartDay+"重复");
                }
            }
        }
    }

    View.OnClickListener repeatNoButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            contentViewShow(0);
            viewShow();
        }
    };

    View.OnClickListener repeatDayButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            contentViewShow(DAY_TAG);
            viewShow();
        }
    };

    View.OnClickListener repeatWeekButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            contentViewShow(WEEK_TAG);
            viewShow();
        }
    };

    View.OnClickListener repeatMonthButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            contentViewShow(MONTH_TAG);
            viewShow();
        }
    };

    View.OnClickListener repeatYearButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            contentViewShow(YEAR_TAG);
            viewShow();
        }
    };
    View.OnClickListener returnButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
    View.OnClickListener submitButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(RepeatRuleActivity.this, "", Toast.LENGTH_LONG).show();
        }
    };

    private void contentViewShow(int viewTag) {
        dayView.setVisibility(View.GONE);
        weekView.setVisibility(View.GONE);
        monthView.setVisibility(View.GONE);
        yearView.setVisibility(View.GONE);

        repeatNoButton.setSelected(false);
        repeatDayButton.setSelected(false);
        repeatWeekButton.setSelected(false);
        repeatMonthButton.setSelected(false);
        repeatYearButton.setSelected(false);
        switch (viewTag) {
            case DAY_TAG:
                dayView.setVisibility(View.VISIBLE);
                repeatDayButton.setSelected(true);
                repeatRuleData.setRepeatKey("day");
                break;
            case WEEK_TAG:
                weekView.setVisibility(View.VISIBLE);
                repeatWeekButton.setSelected(true);
                repeatRuleData.setRepeatKey("week");
                break;
            case MONTH_TAG:
                monthView.setVisibility(View.VISIBLE);
                repeatMonthButton.setSelected(true);
                repeatRuleData.setRepeatKey("month");
                break;
            case YEAR_TAG:
                yearView.setVisibility(View.VISIBLE);
                repeatYearButton.setSelected(true);
                repeatRuleData.setRepeatKey("year");
                break;
            default:
                repeatNoButton.setSelected(true);
                dayView.setVisibility(View.GONE);
                weekView.setVisibility(View.GONE);
                monthView.setVisibility(View.GONE);
                yearView.setVisibility(View.GONE);
                repeatRuleData.setRepeatKey("no");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                int repeatTimes = bundle.getInt("repeatTimes");
                String repeatKey = bundle.getString("repeatKey");
                repeatRuleData.setRepeatKey(repeatKey);

                HashMap<String,HashMap<String,Object>> repeatData = repeatRuleData.getData();
                HashMap<String,Object> subData = repeatData.get(repeatKey);
                subData.put("repeatTimes",Integer.valueOf(repeatTimes));
                viewShow();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}