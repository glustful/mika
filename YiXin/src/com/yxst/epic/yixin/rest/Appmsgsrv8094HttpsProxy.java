package com.yxst.epic.yixin.rest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import android.util.Log;

import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.Response;
import com.yxst.epic.yixin.data.rest.Appmsgsrv8094;
import com.yxst.epic.yixin.data.rest.Appmsgsrv8094Https;
import com.yxst.epic.yixin.data.rest.Appmsgsrv8094Https_;

public class Appmsgsrv8094HttpsProxy implements InvocationHandler {

	private static final String TAG = "IMInterfaceProxy";

	public static Appmsgsrv8094Https create(int timeout) {
//		ClientHttpRequestFactory requestFactory;
//		SimpleClientHttpRequestFactory schrf
//		CommonsClientHttpRequestFactory cchrf;
//		HttpComponentsClientHttpRequestFactory hcchrf
		
//		SimpleClientHttpRequestFactory schrf = new SimpleClientHttpRequestFactory();
//		schrf.setConnectTimeout(timeout);
//		schrf.setReadTimeout(timeout);
		
		HttpComponentsClientHttpRequestFactory hcchrf = new HttpComponentsClientHttpRequestFactory();
		hcchrf.setHttpClient(new EasyHttpClient());
		hcchrf.setConnectTimeout(timeout);
		hcchrf.setReadTimeout(timeout);
		
		Appmsgsrv8094Https myRestClient = new Appmsgsrv8094Https_();
		myRestClient.getRestTemplate().setRequestFactory(hcchrf);
		
		return create(myRestClient);
	}
	
	public static Appmsgsrv8094Https create() {
		return create(10 * 1000);
	}
	
	private static Appmsgsrv8094Https create(Appmsgsrv8094Https object) {
		return (Appmsgsrv8094Https) Proxy.newProxyInstance(Appmsgsrv8094.class
				.getClassLoader(), new Class[] { Appmsgsrv8094Https.class },
				new Appmsgsrv8094HttpsProxy(object));
	}

	private Appmsgsrv8094Https mObj;

	public Appmsgsrv8094HttpsProxy(Appmsgsrv8094Https object) {
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
			MyApplication.getInstance().onReLogin();
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
