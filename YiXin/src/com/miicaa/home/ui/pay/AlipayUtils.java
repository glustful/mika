package com.miicaa.home.ui.pay;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.miicaa.base.pay.alipay.Alipay;
import com.miicaa.base.pay.alipay.Alipay.AlipayPaymentListener;
import com.miicaa.base.pay.alipay.Result;
import com.miicaa.base.pay.wx.WXPay;
import com.miicaa.base.pay.wx.WXPay.PaymentListener;
import com.miicaa.home.R;
import com.miicaa.home.ui.enterprise.CommandCall;

public class AlipayUtils {
private static final int SDK_PAY_FLAG = 1;
    
	private static final int SDK_CHECK_FLAG = 2;
	
	private static final int WXSDK_PAY_FAILED = 3;
	
	private static final int WXSDK_PAY_NOTIFYMSG = 4;
	public static float mFaceValues= 0f;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				
				String resultStatus = (String) msg.obj;

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					paySuccess();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						
						PayUtils.showToast(mContext, "支付结果确认中", 1000);

					} else {
						
						PayUtils.showToast(mContext, "支付失败="+resultStatus, 1000);

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				
				PayUtils.showToast(mContext, "检查结果为：" + msg.obj, 1000);
				break;
				
			}
			case WXSDK_PAY_FAILED:
				
				PayUtils.showToast(mContext, "失败原因为：" + msg.obj, 1000);
				break;
			case WXSDK_PAY_NOTIFYMSG:
				
				PayUtils.showToast(mContext, "通知消息为：" + msg.obj, 1000);
				break;
			default:
				break;
			}
		};
	};

	public static Context mContext;
	public static Object from = null;
	
	public AlipayUtils(Context context,Object...objects){
		AlipayUtils.mContext = context;
		System.out.println("objects="+objects+"="+objects.length);
		if(objects!=null && objects.length>0){
			from = objects[0];
		}else{
			from = null;
		}
	}
	
	public void alipay(JSONObject jsonObject,float mFaceValue,Object...objects) {
		if(jsonObject==null || jsonObject.isNull("partner")){
			PayUtils.showToast(mContext, "请求出错", 1000);
			return;
		}
		mFaceValues = mFaceValue;
		String title = "余额宝充值";
		String body = "余额宝-账户（充值"+mFaceValue+"元）";
		if(objects!=null && objects.length==2){
			title = objects[0].toString();
			body = objects[1].toString();
		}
		Alipay mAlipay = new Alipay(mContext);
		mAlipay.setNeedParams(jsonObject.optString("partner"), jsonObject.optString("rsa_private"), jsonObject.optString("rsa_public"), jsonObject.optString("notify_url"), jsonObject.optString("orderid"), jsonObject.optString("seller"));
		
		mAlipay.setOrderInfo(title, body, String.valueOf(mFaceValue));
		mAlipay.pay(new AlipayPaymentListener() {
			
			@Override
			public void onPay(Result result) {
				String resultStatus = result.getResultStatus();
				Message msg = Message.obtain();
				msg.what = SDK_PAY_FLAG;
				msg.obj = resultStatus;
				mHandler.sendMessage(msg);
			}
		});
	}
	
	public void wxPay(JSONObject json,float mFaceValue,Object...objects){
		if(json==null || json.isNull("APP_KEY")){
			PayUtils.showToast(mContext, "请求出错", 1000);
			return;
		}
		mFaceValues = mFaceValue;
		mFaceValue = mFaceValue*100;
		
		int tmp = (int) mFaceValue;
		String body = "微信-账户（充值"+mFaceValues+"元）";
		if(objects!=null&&objects.length>0){
			body = objects[0].toString();
		}
		WXPay mPay = new WXPay(mContext);
		mPay.setNeedParams(json.optString("APP_SECRET"), json.optString("APP_KEY"), json.optString("PARTNER_KEY"), json.optString("NOTIFY_URL"), json.optString("orderid"));
		mPay.setOrderInfo(body, String.valueOf(tmp));
		mPay.pay(new PaymentListener() {
			
			@Override
			public void success() {
				
				
			}
			
			@Override
			public void notifyMessage(String msg) {
				
				Message mesg = Message.obtain();
				mesg.what = WXSDK_PAY_NOTIFYMSG;
				mesg.obj = msg;
				mHandler.sendMessage(mesg);
			}
			
			@Override
			public void failed(String msg) {
				Message mesg = Message.obtain();
				
				mesg.what = WXSDK_PAY_FAILED;
				mesg.obj = msg;
				mHandler.sendMessage(mesg);
			}
		});
	}
	
protected void paySuccess() {
		if(from!=null && from instanceof CommandCall){
			CommandCall cc = (CommandCall) from;
			cc.callBack();
			return;
		}
        AccountRechargeSuccess_.intent(mContext)
        .money(mFaceValues)
        .method(AccountRechargeSuccess.METHOD_RECHARGE)
        .start();
        ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
       // ((Activity) mContext).finish();
	}

	
}
