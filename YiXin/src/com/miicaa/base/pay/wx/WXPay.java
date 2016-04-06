package com.miicaa.base.pay.wx;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.miicaa.home.R;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPay {
	
	 /**
		 * 微信开放平台和商户约定的密钥
		 * 
		 * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
		 */
	    public   String APP_SECRET = ""; // wxd930ea5d5a258f4f
																						// 对应的密钥

		/**
		 * 微信开放平台和商户约定的支付密钥
		 * 
		 * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
		 */
		public   String APP_KEY = "";
		/**
		 * 微信公众平台商户模块和商户约定的密钥     
		 * 
		 * 注意：不能hardcode在客户端，建议genPackage这个过程由服务器端完成
		 */
		public   String PARTNER_KEY = "";
	    
	
	private IWXAPI api;
	private Context mContext;
	private long timeStamp;
	private String nonceStr, packageValue; 
	private PaymentListener mListener;
	private String body;

	private String notify_url = "";

	private String orderid = "";
	private String price;

																																												// 对应的支付密钥

	public WXPay(Context context) {
		this.mContext = context;
		api = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
	}
	
	public void setNeedParams(String app_secret,String app_key,String partner_key,String notify_url,String orderid){
		this.APP_SECRET = app_secret;
		this.APP_KEY = app_key;
		this.PARTNER_KEY = partner_key;
		if(notify_url!=null)
		this.notify_url = notify_url.replaceAll("\\/", "/");
		this.orderid = orderid;
		
	}
	
	public void setOrderInfo(String body,String price){
		this.body = body;
		this.price = price;
		System.out.println("price="+price);
	}
	
	public void pay(PaymentListener l){
		this.mListener = l;
		boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if(!isPaySupported){
			this.mListener.notifyMessage("确认已安装微信客户端，检查版本级别！");
			
			return;
		}
		
		new GetAccessTokenTask().execute();
	}

	private class GetAccessTokenTask extends
			AsyncTask<Void, Void, GetAccessTokenResult> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			
			dialog = ProgressDialog.show(mContext, mContext.getString(R.string.app_tip), mContext.getString(R.string.getting_access_token), true, false);
		}

		@Override
		protected void onPostExecute(GetAccessTokenResult result) {
			
			if(dialog != null)
				dialog.dismiss();;
			if (result.localRetCode == LocalRetCode.ERR_OK) {
				

				GetPrepayIdTask getPrepayId = new GetPrepayIdTask(
						result.accessToken);
				getPrepayId.execute();
			} else {
			
				mListener.failed(String.format(mContext.getString(R.string.get_access_token_fail),result.errMsg));
			}
		}

		@Override
		protected GetAccessTokenResult doInBackground(Void... params) {
			GetAccessTokenResult result = new GetAccessTokenResult();

			String url = String
					.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
							Constants.APP_ID, APP_SECRET);
			

			byte[] buf = Util.httpGet(url);
			if (buf == null || buf.length == 0) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}

			String content = new String(buf);
			result.parseFrom(content);
			return result;
		}
	}
	
	private class GetPrepayIdTask extends AsyncTask<Void, Void, GetPrepayIdResult> {

		ProgressDialog dialog;
		private String accessToken;
		
		public GetPrepayIdTask(String accessToken) {
			this.accessToken = accessToken;
		}
		
		@Override
		protected void onPreExecute() {
			
			dialog = ProgressDialog.show(mContext, mContext.getString(R.string.app_tip), mContext.getString(R.string.getting_prepayid), true, false);
		}

		@Override
		protected void onPostExecute(GetPrepayIdResult result) {
			
			if(dialog != null)
				dialog.dismiss();;
			if (result.localRetCode == LocalRetCode.ERR_OK) {
				
				sendPayReq(result);
			} else {
				
				mListener.failed(String.format(mContext.getString(R.string.get_prepayid_fail),result.errMsg));
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected GetPrepayIdResult doInBackground(Void... params) {

			String url = String.format("https://api.weixin.qq.com/pay/genprepay?access_token=%s", accessToken);
			String entity = genProductArgs();
			
			
			GetPrepayIdResult result = new GetPrepayIdResult();
			
			byte[] buf = Util.httpPost(url, entity);
			if (buf == null || buf.length == 0) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}
			
			String content = new String(buf);
			
			result.parseFrom(content);
			return result;
		}
	}

	private static class GetAccessTokenResult {

		private static final String TAG = "MicroMsg.SDKSample.PayActivity.GetAccessTokenResult";

		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String accessToken;
		public int expiresIn;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				Log.e(TAG, "parseFrom fail, content is null");
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}

			try {
				JSONObject json = new JSONObject(content);
				if (json.has("access_token")) { 
					accessToken = json.getString("access_token");
					expiresIn = json.getInt("expires_in");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					errCode = json.getInt("errcode");
					errMsg = json.getString("errmsg");
					localRetCode = LocalRetCode.ERR_JSON;
				}

			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}
	
private static class GetPrepayIdResult {
		
		private static final String TAG = "MicroMsg.SDKSample.PayActivity.GetPrepayIdResult";
		
		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String prepayId;
		public int errCode;
		public String errMsg;
		
		public void parseFrom(String content) {
			
			if (content == null || content.length() <= 0) {
				Log.e(TAG, "parseFrom fail, content is null");
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}
			
			try {
				JSONObject json = new JSONObject(content);
				if (json.has("prepayid")) { // success case
					prepayId = json.getString("prepayid");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					localRetCode = LocalRetCode.ERR_JSON;
				}
				
				errCode = json.getInt("errcode");
				errMsg = json.getString("errmsg");
				
			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}

	private static enum LocalRetCode {
		ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
	}
	
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	
	
	/**
	 * 建议 traceid 字段包含用户信息及订单信息，方便后续对订单状态的查询和跟踪
	 */
	private String getTraceId() {
		return "crestxu_" + genTimeStamp(); 
	}
	
	/**
	 * 注意：商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一
	 */
	private String genOutTradNo() {
		//Random random = new Random();
		return orderid;//MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	private String genSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		for (; i < params.size() - 1; i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append(params.get(i).getName());
		sb.append('=');
		sb.append(params.get(i).getValue());
		
		String sha1 = Util.sha1(sb.toString());
		//Log.d(TAG, "genSign, sha1 = " + sha1);
		return sha1;
	}
	
	private String genPackage(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(PARTNER_KEY); // 注意：不能hardcode在客户端，建议genPackage这个过程都由服务器端完成
		
		// 进行md5摘要前，params内容为原始内容，未经过url encode处理
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		
		return URLEncodedUtils.format(params, "utf-8") + "&sign=" + packageSign;
	}
	
	private String genProductArgs() {
		JSONObject json = new JSONObject();
		
		try {
			json.put("appid", Constants.APP_ID);
			String traceId = getTraceId();  // traceId 由开发者自定义，可用于订单的查询与跟踪，建议根据支付用户信息生成此id
			json.put("traceid", traceId);
			nonceStr = genNonceStr();
			json.put("noncestr", nonceStr);
			
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("bank_type", "WX"));
			packageParams.add(new BasicNameValuePair("body", body));
			packageParams.add(new BasicNameValuePair("fee_type", "1"));
			packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
			packageParams.add(new BasicNameValuePair("notify_url", notify_url));
			packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
			packageParams.add(new BasicNameValuePair("partner", Constants.PARTNER_ID));
			packageParams.add(new BasicNameValuePair("spbill_create_ip", "196.168.1.1"));
			packageParams.add(new BasicNameValuePair("total_fee", price));
			packageValue = genPackage(packageParams);
			
			json.put("package", packageValue);
			timeStamp = genTimeStamp();
			json.put("timestamp", timeStamp);
			
			List<NameValuePair> signParams = new LinkedList<NameValuePair>();
			signParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			signParams.add(new BasicNameValuePair("appkey", APP_KEY));
			signParams.add(new BasicNameValuePair("noncestr", nonceStr));
			signParams.add(new BasicNameValuePair("package", packageValue));
			signParams.add(new BasicNameValuePair("timestamp", String.valueOf(timeStamp)));
			signParams.add(new BasicNameValuePair("traceid", traceId));
			json.put("app_signature", genSign(signParams));
			
			json.put("sign_method", "sha1");
		} catch (Exception e) {
			//Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}
		
		return json.toString();
	}
	
private void sendPayReq(GetPrepayIdResult result) {
		System.out.println("result="+result.prepayId);
		PayReq req = new PayReq();
		req.appId = Constants.APP_ID;
		req.partnerId = Constants.PARTNER_ID;
		req.prepayId = result.prepayId;
		req.nonceStr = nonceStr;
		req.timeStamp = String.valueOf(timeStamp);
		req.packageValue = "Sign=" + packageValue;
		
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("appkey", APP_KEY));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genSign(signParams);
		
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		
		api.sendReq(req);
		
	}

	public interface PaymentListener{
		public void success();
		public void failed(String msg);
		void notifyMessage(String msg);
	}
	
	/*private AlertDialog mDialog;
	private void showDialog(String msg){
		if(mDialog != null){
			mDialog = null;
		}
		
		View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pay_wxpay_dialog, null);
		
		TextView tvmsg = (TextView) view.findViewById(R.id.pay_wxpay_dialog_msg);
		tvmsg.setText(msg);
		mDialog = new AlertDialog.Builder(mContext)
		.setView(view)
		.setCancelable(false)
		.create();
		mDialog.setCanceledOnTouchOutside(false);
		
		mDialog.show();
	}
	
	private void closeDialog(){
		if(mDialog != null && mDialog.isShowing()){
			mDialog.dismiss();
			mDialog = null;
		}
	}*/
}
