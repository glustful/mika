package com.miicaa.home.ui.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WeekEntity {

	
	@Override
	protected WeekEntity clone() {
		WeekEntity entity = new WeekEntity();
		entity.setIndex(this.index);
		entity.setStartDate((Calendar)this.startDate.clone());
		entity.setEndDate((Calendar)this.endDate.clone());
		return entity;
	}
	@Override
	public boolean equals(Object o) {
		
		if(o instanceof WeekEntity){
			
			WeekEntity item = (WeekEntity) o;
			if(item.toString().equals(this.toString())){
				
				return true;
			}
		}
		return false;
	}

	private Calendar startDate;
	private Calendar endDate;
	private int index;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Calendar getStartDate() {
		return startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = (Calendar) startDate.clone();
	}
	public Calendar getEndDate() {
		return endDate;
	}
	public void setEndDate(Calendar endDate) {
		this.endDate = (Calendar) endDate.clone();
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		SimpleDateFormat mSdf = new SimpleDateFormat("MM.dd");
		return sdf.format(endDate.getTime())+"第"+index+"周 "+mSdf.format(startDate.getTime())+"~"+mSdf.format(endDate.getTime());
	}
	
}
