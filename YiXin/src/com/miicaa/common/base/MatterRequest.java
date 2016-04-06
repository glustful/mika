package com.miicaa.common.base;



import java.util.HashMap;

import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by LM on 14-9-2.
 */
public class MatterRequest {
    public static  void requestMatterHome(String url,HashMap<String,String> hParam,
                                          final MatterHomeCallBackListener callBackListener){
    	
        new RequestAdpater(){

            @Override
            public void onReponse(ResponseData data) {

                callBackListener.callBack(data);
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam(hParam)
                .notifyRequest();
    }

    public interface MatterHomeCallBackListener{
        public void callBack(ResponseData data);
    }
}
