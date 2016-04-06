package com.miicaa.home.ui.calendar;

import com.miicaa.home.data.OnFinish;
import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.service.CacheCtrlSrv;

public class CalendarRequest {

	 // 用户登录
    public static void requestMessage( OnBusinessResponse onResponse,final OnFinish onFinish,String startDate,String finishDate) {
        if (CacheCtrlSrv.isNetBreak) {    // 断网状态下
            
                onFinish.onFailed("当前账户信息与本地缓存的账户信息不一致。");
           
        } else {    // 联网状态下
            calendarRequest(onResponse, startDate, finishDate);
        }
    }
    
    public static Boolean calendarRequest(final OnBusinessResponse onResponse,String startDate,String finishDate)
    {
    	
        if(onResponse == null)
            return false;
        System.out.println("start");
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                onResponse.onResponse(data);
            }

            @Override
            public void onProgress(ProgressMessage msg) {
                onResponse.onProgress(msg);
            }
        }.setUrl("/home/pc/fullcalendar/cal")
            .addParam("planTimeStart", startDate)
            .addParam("planTimeEnd", finishDate)
            .notifyRequest();
    }
}
