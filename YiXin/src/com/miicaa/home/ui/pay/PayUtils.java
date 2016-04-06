package com.miicaa.home.ui.pay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.pay.AccountRechargeActivity.OnResultListener;

public class PayUtils {

	static ProgressDialog progressDialog;

	public static void showDialog(Context context) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("miicaa");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("正在准备数据，请稍后...");
		progressDialog.show();
	}

	public static void closeDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatData(String string, long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(string);
		return sdf.format(new Date(time));

	}

	public static String mUserName = "";
	protected static String mCallPhone = "";
	protected static String mPhone = "";
	protected static String mEmail = "";

	public static void requestUserInfo(Context context,
			final OnResultListener onResultListener) {
		String mUserCode = AccountInfo.instance().getLastUserInfo().getCode();
		String url = "/home/phone/personcenter/getpersoninfo";
		showDialog(context);
		new RequestAdpater() {
			@Override
			public void onReponse(ResponseData data) {
				closeDialog();
				if (data.getResultState() == ResponseData.ResultState.eSuccess) {

					try {
						mUserName = "";

						mCallPhone = "";
						mPhone = "";

						mEmail = "";

						JSONObject jData = data.getJsonObject();
						JSONObject juser = jData.optJSONObject("user");
						if (juser != null) {

							mUserName = juser.optString("name");
							if (mUserName != null && mUserName.equals("null")) {
								mUserName = "";
							}

							mPhone = juser.optString("phone");
							if (mPhone != null && mPhone.equals("null")) {
								mPhone = "";
							}
							mCallPhone = juser.optString("cellphone");
							if (mCallPhone != null && mCallPhone.equals("null")) {
								mCallPhone = "";
							}

							mEmail = juser.optString("email");
							if (mEmail != null && mEmail.equals("null")) {
								mEmail = "";
							}

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					onResultListener.onSuccess(null);
				} else {
					// TODO:失败了要弹通知
					onResultListener.onFaild(data.getMsg());
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {

			}
		}.setUrl(url).addParam("userCode", mUserCode).notifyRequest();
	}

	public static void requestBankInfo(Context context,
			final OnResultListener onResultListener) {

		String url = context.getString(R.string.pay_bank_info_url);
		showDialog(context);
		new RequestAdpater() {
			@Override
			public void onReponse(ResponseData data) {
				closeDialog();

				if (data.getResultState() == ResponseData.ResultState.eSuccess) {

					onResultListener.onSuccess(data.getJsonArray()
							.optJSONObject(0));
				} else {
					// TODO:失败了要弹通知
					onResultListener.onFaild(data.getMsg());
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {

			}
		}.setUrl(url)

		.notifyRequest();
	}

	private static Runnable r = new Runnable() {
		@Override
		public void run() {
			if (mToast != null)
				mToast.cancel();
		}
	};

	private static Handler mhandler = new Handler();

	public static void showToast(Context mContext, String text, int duration) {
		if(duration<3000){
			duration = 3000;
		}
		mhandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else{
			mToast = Toast.makeText(mContext, text, 3);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		}
		
		mhandler.postDelayed(r, duration);

		mToast.show();
	}

	static Toast mToast;

	public static boolean matchers(String input, int type) {
		String phone = "(\\d{11})|^((\\d{7,9})|(\\d{4}|\\d{3})-(\\d{7,9})|(\\d{4}|\\d{3})-(\\d{7,9})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,9})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))";
		String email = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern pattern = null;
		switch (type) {
		case 1:
			return true;

		case 2:
			pattern = Pattern.compile(email);
			break;
		}
		if (pattern == null)
			return false;
		return pattern.matcher(input).matches();

	}

	public static String cleanZero(double d) {
		if (d == 0)
			return "0";
		String s = String.format("%.2f", d);

		while (s.contains(".") && (s.endsWith("0") || s.endsWith("."))) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	public static String cleanZero(String format) {
		if(format.equals("0"))
			return "0";
		while (format.contains(".") && (format.endsWith("0") || format.endsWith("."))) {
			format = format.substring(0, format.length() - 1);
		}
		return format;
	}
}
