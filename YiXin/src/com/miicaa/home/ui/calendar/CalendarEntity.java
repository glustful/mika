package com.miicaa.home.ui.calendar;

import java.util.Date;

import android.graphics.Color;

public class CalendarEntity {

	private long planTime = 0;
	private long planTimeEnd = 0;
	private color mColor = color.red;
	private String id;
	private String title;
	private String dataType;
	private String status;
	private String operateGroup;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperateGroup() {
		return operateGroup;
	}

	public void setOperateGroup(String operateGroup) {
		this.operateGroup = operateGroup;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getPlanTime() {
		return planTime;
	}

	public void setPlanTime(long planTime) {
		this.planTime = planTime;
	}

	public long getPlanTimeEnd() {
		return planTimeEnd;
	}

	public void setPlanTimeEnd(long planTimeEnd) {
		this.planTimeEnd = planTimeEnd;
	}

	public int getmColor() {
		if(this.mColor == color.red)
			return Color.RED;
		else if(this.mColor == color.black)
			return Color.BLACK;
		else if(this.mColor == color.gray)
			return Color.GRAY;
		return Color.RED;
	}

	public void setmColor(String status, long endTime) {
		if (status.equals("01")) {
			if (endTime < new Date().getTime()) {
				this.mColor = color.red;
			} else {
				this.mColor = color.black;
			}
		} else
			this.mColor = color.gray;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	enum color {
		red, gray, black
	}

	public boolean isChecked(CustomDate date) {
		long time = date.getTime();
		long endTime = date.getLastTime();
		if (time <= planTime && endTime >= planTime)
			return true;
		if(time <= planTimeEnd && endTime >= planTimeEnd)
			return true;
		if(time > planTime && endTime < planTimeEnd)
			return true;
		return false;
	}
}
