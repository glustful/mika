package com.miicaa.home.data.business.account;

import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-12-17.
 */
public class AccountRequest
{
    public static Boolean logionRequest(final OnBusinessResponse onResponse,String email,String pwd)
    {
        if(onResponse == null)
            return false;
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data)
            {
            	
                onResponse.onResponse(data);
            }

            @Override
            public void onProgress(ProgressMessage msg) {
                onResponse.onProgress(msg);
            }
        }.setUrl("/home/phone/login")
        .setSaveSession(true)
            .addParam("email", email)
            .addParam("password", pwd)
            .notifyRequest();
    }

    public static Boolean changeOrgRequest(final OnBusinessResponse onResponse, String orgCode)
    {

        if(onResponse == null)
            return false;
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                onResponse.onResponse(data);
            }

            @Override
            public void onProgress(ProgressMessage msg) {
                onResponse.onProgress(msg);
            }
        }.setUrl("/home/phone/changeorg")
                .addParam("orgCode", orgCode)
                .notifyRequest();
    }
}
