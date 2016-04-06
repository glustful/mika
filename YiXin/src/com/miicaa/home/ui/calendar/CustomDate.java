package com.miicaa.home.ui.calendar;

import java.io.Serializable;
import java.util.Calendar;
/**
 * 自定义的日期类

 *
 */
public class CustomDate implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	public int year;
	public int month;
	public int day;
	public int week;
	public State state;
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public CustomDate(int year,int month,int day){
		if(month > 12){
			month = 1;
			year++;
		}else if(month <1){
			month = 12;
			year--;
		}
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	public CustomDate(){
		this.year = DateUtil.getYear();
		this.month = DateUtil.getMonth();
		this.day = DateUtil.getCurrentMonthDay();
	}
	
	public static CustomDate modifiDayForObject(CustomDate date,int day){
		CustomDate modifiDate = new CustomDate(date.year,date.month,day);
		return modifiDate;
	}
	@Override
	public String toString() {
		return year+"-"+month+"-"+day;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}
	
	public long getTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.year, this.month-1, this.day,0,0,0);
		return calendar.getTime().getTime();
	}

	public long getLastTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.year, this.month-1, this.day,23,59,59);
		return calendar.getTime().getTime();
	}
	/**
	 * 
	 * @author huang cell的state 当前月日期，过去的月的日期，下个月的日期，今天，点击的日期
	 * 
	 */
	public enum State {
		CURRENT_MONTH_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY, TODAY, CLICK_DAY;
	}

}
