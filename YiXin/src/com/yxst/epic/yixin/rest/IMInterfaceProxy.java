package com.yxst.epic.yixin.rest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import android.util.Log;

import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.utils.NetUtils.OnReloginListener;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.Response;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.data.rest.IMInterface_;

public class IMInterfaceProxy implements InvocationHandler {

	private static final String TAG = "IMInterfaceProxy";

	public static IMInterface create(int timeout) {
//		ClientHttpRequestFactory requestFactory;
//		SimpleClientHttpRequestFactory schrf
//		CommonsClientHttpRequestFactory cchrf;
//		HttpComponentsClientHttpRequestFactory hcchrf
		
//		SimpleClientHttpRequestFactory schrf = new SimpleClientHttpRequestFactory();
//		schrf.setConnectTimeout(timeout);
//		schrf.setReadTimeout(timeout);
		
		HttpComponentsClientHttpRequestFactory hcchrf = new HttpComponentsClientHttpRequestFactory();
		hcchrf.setConnectTimeout(timeout);
		hcchrf.setReadTimeout(timeout);
		
		IMInterface myRestClient = new IMInterface_();
		myRestClient.getRestTemplate().setRequestFactory(hcchrf);
		
		return create(myRestClient);
	}
	
	public static IMInterface create() {
		return create(10 * 1000);
	}
	
	private static IMInterface create(IMInterface object) {
		return (IMInterface) Proxy.newProxyInstance(IMInterface.class
				.getClassLoader(), new Class[] { IMInterface.class },
				new IMInterfaceProxy(object));
	}

	private IMInterface mObj;

	public IMInterfaceProxy(IMInterface object) {
		mObj = object;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = method.invoke(mObj, args);

		handleTokenExpire(result);

		return result;
	}

	private void handleTokenExpire(Object result) {
		Log.d(TAG, "handleTokenExpire() isTokenExpire(result):" + isTokenExpire(result));
		if (isTokenExpire(result)) {
//			MyApplication.getInstance().onReLogin();
				MyApplication.getInstance().relogin(new OnReloginListener() {
					
					@Override
					public void success() {
						
					}
					
					@Override
					public void failed() {
						
					}
				});
		}
	}

	private boolean isTokenExpire(Object result) {
		if (result instanceof Response) {
			Response response = (Response) result;
			BaseResponse baseResponse = response.BaseResponse;
//			response.BaseResponse.Ret = BaseResponse.RET_ERROR_TOKEN;
			return baseResponse.Ret == BaseResponse.RET_ERROR_TOKEN;
		}

		return false;
	}

}
