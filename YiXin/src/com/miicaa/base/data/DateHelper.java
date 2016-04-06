package com.miicaa.base.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	public static long getTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static Date getDate() {
        return Calendar.getInstance().getTime();
    }

    public static Calendar getToday() {
        return Calendar.getInstance();
    }

    public static Calendar getTomorrow() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_MONTH, 1);
        return date;
    }

    public static Calendar getThisFriday() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return date;
    }

    public static Calendar getNextMonday() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        date.add(Calendar.DAY_OF_MONTH, 7);
        return date;
    }

    public static Calendar getLastDay() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, 1);
        date.set(Calendar.DAY_OF_MONTH, 1);
        date.add(Calendar.DAY_OF_MONTH, -1);
        return date;
    }

    public static String getShowUpdateDate(long date) {
        Date src = new Date(date*1000);
        ComputeDay day = new ComputeDay();
        Date today = new Date();
        long between = (today.getTime() - src.getTime()) / 1000;
        int offday = (int) (between / (24 * 3600));
        int todayFormYearStart = (int)((today.getTime()/1000) / (24 * 3600));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatterHour = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatterDayHour = new SimpleDateFormat("MM-dd HH:mm");

        if(Integer.valueOf(String.valueOf(formatterYear.format(src))) < Integer.valueOf(String.valueOf(formatterYear.format(today)))){
            return String.valueOf(formatter.format(src));
        }else if (offday >= 2) {
            //ǰ��
            return String.valueOf(formatterDayHour.format(src));
        } else if (offday >= 1) {
            //����
            return "����" + String.valueOf(formatterHour.format(src));
        } else if (offday >= 0) {
            //����
            if (between < 60) {
                return "�ո�";
            } else if (between < 60 * 60) {
                return String.valueOf(between / 60) + "����ǰ";
            } else {
                return String.valueOf(formatterHour.format(src));
            }
        }
        return "";
    }

    public static ComputeDay getShenyuDate(Date src) {
        ComputeDay day = new ComputeDay();
        Date today = new Date();
        long between = (src.getTime() - today.getTime()) / 1000;
        int offday = (int) (between / (24 * 3600));
        long offsec = between % (24 * 3600);
        if (between >= 0 && offsec > 0) {
            offday += 1;
        }

        if (offday >= 3) {
            day.mBIsShenyu = 0;
            day.mDay = offday;
            day.mpre = "(";
            day.mHuo = ")";
            day.mContent = "ʣ��" + String.valueOf(offday) + "��";
        } else if (offday >= 2) {
            day.mBIsShenyu = 0;
            day.mDay = offday;
            day.mpre = "(";
            day.mHuo = ")";
            day.mContent = "2�����";
        } else if (offday >= 1) {
            day.mBIsShenyu = 0;
            day.mDay = offday;
            day.mpre = "(";
            day.mHuo = ")";
            day.mContent = "���쵽��";
        } else if (offday >= 0) {
            day.mBIsShenyu = 0;
            day.mDay = offday;
            day.mpre = "(";
            day.mHuo = ")";
            day.mContent = "���쵽��";
        } else {
            day.mBIsShenyu = 1;
            day.mDay = 0 - offday;
            day.mpre = "(";
            day.mHuo = ")";
            day.mContent = "����" + String.valueOf(0 - offday) + "��";
        }

        return day;
    }

    public static class ComputeDay {
        public int mBIsShenyu;
        public int mDay;
        public String mpre;
        public String mHuo;
        public String mContent;
    }

}
