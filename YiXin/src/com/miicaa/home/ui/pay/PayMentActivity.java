package com.miicaa.home.ui.pay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;

@EActivity(R.layout.layout_pay_payment_activity)
public class PayMentActivity extends Activity {

	@Override
	protected void onResume() {
		
		super.onResume();
		Utils.hiddenSoftBorad(mContext);
	}

	@Extra
	String type;
	@Extra
	double accountmoney;
	@Extra
	String packageJSON;
	@Extra
	String fiveJSON;
	@Extra
	int mTimeLength;
	@Extra
	int mCount;
	@Extra
	double mTotal;
	@Extra
	double packageValue;
	@Extra
	double fiveValue;
	

	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.pay_headTitle)
	TextView headTitle;
	@ViewById(R.id.payment_money)
	TextView moneyTotal;
	@ViewById(R.id.payment_account_money)
	TextView accountMoneyTotal;

	@ViewById(R.id.convertView)
	View convertView;
	
	@Click(R.id.payment_recharge)
	void recharge(){
		AccountRechargeActivity_.intent(mContext)
		.start();
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}

	@Click(R.id.payment_commit)
	void commit() {
		if (isCommit()) {
			PayUtils.showDialog(mContext);
			new RequestAdpater() {

				@Override
				public void onReponse(ResponseData data) {
					PayUtils.closeDialog();
					if (data.getResultState() == ResultState.eSuccess) {
						paySuccess();
					} else {
						PayUtils.showToast(mContext, "支付失败:" + data.getMsg(),
								2000);
					}
				}

				@Override
				public void onProgress(ProgressMessage msg) {

				}
			}.setUrl(mContext.getString(R.string.pay_payment_url))
					.addParam("jsonStr", postStr()).notifyRequest();
		}
	}

	@Click(R.id.pay_cancleButton)
	void cancel() {
		finish();
	}

	Context mContext;

	@AfterInject
	void initData() {
		
		this.mContext = this;
	}

	@AfterViews
	void initUI() {

		back.setText("返回");
		headTitle.setText("支付");
		accountMoneyTotal.setText(mContext.getString(R.string.symbol)+PayUtils.cleanZero(String.format("%.2f", accountmoney)));
		moneyTotal.setText(mContext.getString(R.string.symbol)
				+ PayUtils.cleanZero(String.format("%.2f", mTotal)));
		if(type.equals(String.valueOf(TableLayoutPackageView.TYPE_DAY))){
			ViewHolder holder = new ViewHolder();
			holder.kind = (TextView) convertView
					.findViewById(R.id.pay_package_kind);
			holder.people = (TextView) convertView
					.findViewById(R.id.pay_package_people);
			holder.store = (TextView) convertView
					.findViewById(R.id.pay_package_store);
			holder.timelength = (TextView) convertView
					.findViewById(R.id.pay_package_timelength);
			holder.lenght_label = (TextView) convertView
					.findViewById(R.id.pay_package_timelength_label);
			holder.start = (TextView) convertView
					.findViewById(R.id.pay_package_timestart);
			
			holder.start.setGravity(Gravity.CENTER);
			holder.end = (TextView) convertView
					.findViewById(R.id.pay_package_timeend);
			holder.end.setGravity(Gravity.CENTER);
			try {
				initData(holder,null);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		else{
			requestTime();
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}

	boolean isCommit() {

		if (mTotal <= 0) {

			PayUtils.showToast(mContext, "金额不能为空", 1000);
			return false;
		}

		return true;
	}

	private String postStr() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String postStr = "[";
		try {
			if (fiveJSON != null && !fiveJSON.equals("")) {

				BigDecimal bd = new BigDecimal(fiveValue);
				fiveValue = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				JSONObject obj = new JSONObject(fiveJSON);
				String tmp = "{\"fiveSetUseDTO\":{";
				map.put("orderType", "00");
				map.put("perMonth", obj.optDouble("perMonth", 0));
				map.put("perDay", obj.optDouble("perDay", 0));

				map.put("quality", mTimeLength);

				map.put("renewal", "0");
				map.put("setId", obj.optString("id"));
				map.put("setType", type);
				map.put("totalPay", fiveValue);
				map.put("fiveSetNumber", mCount);
				for (Map.Entry<String, Object> item : map.entrySet()) {

					tmp += "\"" + item.getKey() + "\":";
					if (item.getValue() instanceof String) {
						tmp += "\"" + item.getValue() + "\",";
					} else {
						tmp += item.getValue() + ",";
					}
				}
				tmp = tmp.substring(0, tmp.length() - 1) + "}},";
				postStr += tmp;
			} else {
				String tmp = "{\"fiveSetUseDTO\":null},";
				postStr += tmp;
			}
			if (packageJSON != null && !packageJSON.equals("")) {
				map.clear();
				JSONObject obj = new JSONObject(packageJSON);
				BigDecimal bd = new BigDecimal(packageValue);
				packageValue = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				map.put("orderType", "00");
				map.put("perMonth", obj.optDouble("perMonth", 0));
				map.put("perYear", obj.optDouble("perYear", 0));
				map.put("quality", mTimeLength);
				map.put("renewal", "0");
				map.put("setId", obj.optString("id"));
				map.put("setType", type);
				map.put("totalPay", packageValue);
				postStr += "{\"defaultSetUseDTO\":";
				String tmp = "{";
				for (Map.Entry<String, Object> item : map.entrySet()) {

					tmp += "\"" + item.getKey() + "\":";
					if (item.getValue() instanceof String) {
						tmp += "\"" + item.getValue() + "\",";
					} else {
						tmp += item.getValue() + ",";
					}
				}
				tmp = tmp.substring(0, tmp.length() - 1) + "}},";
				postStr += tmp;
			} else {
				String tmp = "{\"defaultSetUseDTO\":null},";
				postStr += tmp;
			}

			map.clear();
			String tmp = "{\"order\":{";
			map.put("allocateStatus", "02");
			map.put("goodType", "00");
			BigDecimal bd = new BigDecimal(mTotal);
			mTotal = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			map.put("needPay", mTotal);
			for (Map.Entry<String, Object> item : map.entrySet()) {

				tmp += "\"" + item.getKey() + "\":";
				if (item.getValue() instanceof String) {
					tmp += "\"" + item.getValue() + "\",";
				} else {
					tmp += item.getValue() + ",";
				}
			}
			tmp = tmp.substring(0, tmp.length() - 1) + "}}]";
			postStr += tmp;
		
		} catch (Exception e) {
			e.printStackTrace();
			postStr = "";
		}
		return postStr;

	}

	protected void paySuccess() {
		
		AccountRechargeSuccess_.intent(mContext)
		.money(mTotal)
		.method(AccountRechargeSuccess.METHOD_PAY)
		.start();
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);

	}

	private void initData(ViewHolder holder, JSONObject jsonObject) throws JSONException {
		
		if (packageJSON != null && !packageJSON.equals("")) {
			JSONObject pack = new JSONObject(packageJSON);
			String name = pack.optString("name");
			if (fiveJSON != null && !fiveJSON.equals("")) {
				JSONObject five = new JSONObject(fiveJSON);
				name += "+" + five.optString("name") + "(" + mCount + "个)";
				Spannable WordtoSpan = new SpannableString(name);

				WordtoSpan.setSpan(
						new AbsoluteSizeSpan((int) holder.kind.getTextSize() - 4),
						name.lastIndexOf("("), name.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY),
						name.lastIndexOf("("), name.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.kind.setText(WordtoSpan);
				if (!name.contains("稳定型")) {
					holder.people.setText("<="
							+ (pack.optInt("upperLimit", 0) + mCount*five.optInt("upperLimit", 0)));
				} else {
					holder.people.setText("不限");
				}
			}else{
				holder.kind.setText(name);
				if (!name.contains("稳定型")) {
					holder.people.setText("<="
							+ (pack.optInt("upperLimit", 0) ));
				} else {
					holder.people.setText("不限");
				}
			}
			
			

		
			
			if (!pack.isNull("space")) {
				holder.store.setText(String.format("%.1f",
						pack.optDouble("space") / 1024));

			} else {
				holder.store.setText("不限");
			}

		} else {
			if (fiveJSON != null && !fiveJSON.equals("")) {
				JSONObject five = new JSONObject(fiveJSON);
				String name = "5人包" + "(" + mCount
						+ "个)";
				Spannable WordtoSpan = new SpannableString(name);

				WordtoSpan.setSpan(
						new AbsoluteSizeSpan((int) holder.kind.getTextSize() - 4),
						name.lastIndexOf("("), name.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY),
						name.lastIndexOf("("), name.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.kind.setText(WordtoSpan);
				

				holder.people.setText((mCount * five.optInt("upperLimit", 0)) + "");

				holder.store.setText("不限");

			}
		}
		long nowTime = 0;
		if(type.equals(String.valueOf(TableLayoutPackageView.TYPE_DAY))&&jsonObject==null){
			nowTime = new Date().getTime();
		}else{
			nowTime = jsonObject.optLong("nowTime", 0);
		}
		holder.start.setText(PayUtils.formatData("yyyy-MM-dd",
				nowTime));
		Calendar ca =Calendar.getInstance(Locale.CHINESE);
		ca.setTimeInMillis(nowTime);
		if (type.equals("0")) {
			holder.lenght_label.setText("开通时长（月）：");
			ca.set(Calendar.MONTH, ca.get(Calendar.MONTH)+mTimeLength);
		} else if (type.equals("1")) {
			holder.lenght_label.setText("开通时长（年）：");
			ca.set(Calendar.YEAR, ca.get(Calendar.YEAR)+mTimeLength);
		} else if (type.equals("2")) {
			holder.lenght_label.setText("开通时长（天）：");
			
			ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR)-1+mTimeLength);
			
		}
		 holder.end.setText(PayUtils.formatData("yyyy-MM-dd", ca.getTimeInMillis()));
		holder.timelength.setText(""+mTimeLength);
	}

	class ViewHolder {
		TextView kind;
		TextView people;
		TextView store;
		TextView lenght_label;
		TextView timelength;
		TextView start;
		TextView end;
	}
	
	private void requestTime(){
		PayUtils.showDialog(mContext);
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				
				PayUtils.closeDialog();
				if(data.getResultState()==ResultState.eSuccess){
					ViewHolder holder = new ViewHolder();
					holder.kind = (TextView) convertView
							.findViewById(R.id.pay_package_kind);
					holder.people = (TextView) convertView
							.findViewById(R.id.pay_package_people);
					holder.store = (TextView) convertView
							.findViewById(R.id.pay_package_store);
					holder.timelength = (TextView) convertView
							.findViewById(R.id.pay_package_timelength);
					holder.lenght_label = (TextView) convertView
							.findViewById(R.id.pay_package_timelength_label);
					holder.start = (TextView) convertView
							.findViewById(R.id.pay_package_timestart);
					
					holder.start.setGravity(Gravity.CENTER);
					holder.end = (TextView) convertView
							.findViewById(R.id.pay_package_timeend);
					holder.end.setGravity(Gravity.CENTER);
					try {
						initData(holder,data.getJsonObject());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(data.getResultState()==ResultState.eException){
					ViewHolder holder = new ViewHolder();
					holder.kind = (TextView) convertView
							.findViewById(R.id.pay_package_kind);
					holder.people = (TextView) convertView
							.findViewById(R.id.pay_package_people);
					holder.store = (TextView) convertView
							.findViewById(R.id.pay_package_store);
					holder.timelength = (TextView) convertView
							.findViewById(R.id.pay_package_timelength);
					holder.lenght_label = (TextView) convertView
							.findViewById(R.id.pay_package_timelength_label);
					holder.start = (TextView) convertView
							.findViewById(R.id.pay_package_timestart);
					
					holder.start.setGravity(Gravity.CENTER);
					holder.end = (TextView) convertView
							.findViewById(R.id.pay_package_timeend);
					holder.end.setGravity(Gravity.CENTER);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						Date d = sdf.parse(data.getStringData());
						JSONObject json = new JSONObject("{\"nowTime\":"+d.getTime()+"}");
						initData(holder,json);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					PayUtils.showToast(mContext, ""+data.getMsg(), 1000);
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}
		.setUrl(mContext.getString(R.string.pay_getTime_url))
		.notifyRequest();
	}
}
