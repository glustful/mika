package com.miicaa.home.ui.enterprise;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.ui.pay.AlipayUtils;
import com.miicaa.home.ui.pay.PayUtils;

public class CommandCall {

	private static final String alipay = "00";
	private static final String weixin = "40";
	private boolean isMyService = false;
	Context mContext;
	String url;
	HashMap<String, String> params = new HashMap<String, String>();

	public CommandCall(Context context, String command) {
		this.isMyService = false;
		this.mContext = context;
		String[] tmp = command.split("&");
		if (tmp == null || tmp.length < 1)
			return;
		for (String s : tmp) {
			String[] kv = s.split("=");
			if (kv != null && kv.length == 2) {
				params.put(kv[0], kv[1]);
			}
		}
	}

	public CommandCall(Context context, JSONObject command) {
		this.isMyService = true;
		this.mContext = context;
		params.put("action", "pay");
		params.put("itemId", command.optString("itemId"));
		params.put("amont", command.optDouble("amount") + "");
		params.put("payWay", command.optString("payWay"));
		params.put("orderId", command.optString("id"));
	}

	public void call() {
		if (params.size() < 1) {
			PayUtils.showToast(mContext, "参数不能为空", 3000);
			return;
		}
		if (params.get("action").equals("pay")) {

			callPay();
		}else if(params.get("action").equals("call")){
			callPhone();
		}
	}

	private void callPhone() {
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+params.get("number")));  
        mContext.startActivity(intent);  
		
	}

	private void callPay() {
		final HashMap<String, String> ps = new HashMap<String, String>();

		if (!params.containsKey("payWay")) {

			String payWay = alipay;
			
			ps.put("itemId", params.get("id"));
			ps.put("amont", params.get("price"));
			if (params.get("source").equals("alipay"))
				payWay = alipay;
			else if (params.get("source").equals("weixin")) {
				payWay = weixin;
			}
			ps.put("payWay", payWay);
		} else {
			ps.putAll(params);
		}
		PayUtils.showDialog(mContext);
		new RequestAdpater() {

			@Override
			public void onReponse(ResponseData data) {
				PayUtils.closeDialog();
				if (data.getResultState() == ResultState.eSuccess) {
					url = mContext.getString(R.string.enterprice_question_url)+data.getJsonObject().optString("orderid");
					String title = data.getJsonObject().optString("subject");
					String body = data.getJsonObject().optString("subject");
					if (ps.get("payWay").equals(alipay)) {
						new AlipayUtils(mContext,CommandCall.this).alipay(data.getJsonObject(),
								Float.parseFloat(ps.get("amont")), title, body);
					} else if (ps.get("payWay").equals(weixin)) {
						new AlipayUtils(mContext,CommandCall.this).wxPay(data.getJsonObject(),
								Float.parseFloat(ps.get("amont")), body);
					}
				} else {
					PayUtils.showToast(mContext, "" + data.getMsg(), 3000);
				}

			}

			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub

			}
		}.setUrl(mContext.getString(R.string.enterprice_order_url))
				.addParam(ps).notifyRequest();
	}

	public void callBack() {
		if(url ==null || url.equals(""))
			return;
		if(isMyService){
			EnterpriceMainActivity_.intent(mContext)
			.url(url)
			.start();
			return;
		}
		if(mContext instanceof EnterpriceMainActivity){
			EnterpriceMainActivity activity = (EnterpriceMainActivity) mContext;
			activity.mWebView.loadUrl(UserAccount.mSeverHost+url);
		}
	}
}
