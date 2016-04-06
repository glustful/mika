package com.miicaa.home.ui.pay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.yxst.epic.yixin.view.TextViewPartColor;

@EActivity(R.layout.layout_pay_main_activity)
public class PayMainActivity extends Activity {
	@ViewById(R.id.pay_headTitle)
	TextView headTitle;// 标题
	@ViewById(R.id.pay_main_account)
	TextView accountName;// 账户名
	@ViewById(R.id.pay_main_money)
	TextView accountMoney;// 账户余额
	@ViewById(R.id.pay_main_state)
	TextView useState;// 套餐使用状态（免费与付费）
	@ViewById(R.id.pay_main_kind)
	TextView kind;// 套餐类型
	@ViewById(R.id.pay_main_timeout)
	TextView timeout;// 使用期限
	@ViewById(R.id.pay_main_people_count)
	TextView peopleCount;// 公司现有人数
	@ViewById(R.id.pay_main_people_addcount)
	TextViewPartColor addCount;// 还可增加的人数
	@ViewById(R.id.pay_main_store_used)
	TextView used;// 存储空间已使用
	@ViewById(R.id.pay_main_store_useable)
	TextView useable;// 存储空间可使用的大小
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.pay_footer)
	LinearLayout footer;
	@ViewById(R.id.pay_buy)
	Button btBuy;
	@ViewById(R.id.pay_continue_buy)
	Button btContinue;
	@ViewById(R.id.pay_plus_buy)
	Button btPlus;
	@ViewById(R.id.pay_other_bought)
	Button otherBought;
	@ViewById(R.id.line)
	View line;
	@ViewById(R.id.line_right)
	LinearLayout lineRight;

	Context mContext;
	ProgressDialog mDialog;
	String mAccountName;
	double mTotalMoney;
	long endTime = 0;
	public double mTmpMoney;

	public static PayMainActivity instance;

	@Click(R.id.pay_cancleButton)
	void cancel() {
		finish();
	}

	/*
	 * 新加功能
	 */
	@Click(R.id.pay_main_kind)
	void currentKind(){
		BoughtPackageActivity_.intent(mContext)
		.type(BoughtPackageActivity.CURRENT_PACKAGE)
		.start();
	}
	
	/*
	 * 新功能完成
	 */
	
	@Click(R.id.pay_other_bought)
	void otherBought() {
		BoughtPackageActivity_.intent(mContext)
		.type(BoughtPackageActivity.BOUGHT_PACKAGE)
		.start();
	}

	@Click(R.id.pay_myfound)
	void myFound() {
		MyFundActivity_.intent(mContext).mAccountName(mAccountName)
				.mTotalMoney(mTotalMoney).start();
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}

	@Click(R.id.pay_bill)
	void bill() {
		skipActivity(BillRecordActivity_.class);
	}

	@Click(R.id.pay_receipt)
	void receipt() {
		skipActivity(ReceiptRecordActivity_.class);
	}

	@Click(R.id.pay_account)
	void account() {
		skipActivity(AccountRechargeActivity_.class);
	}

	@Click(R.id.pay_buy)
	void buy() {
		BuyPackageActivity_.intent(mContext).accountmoney(mTotalMoney)
				.type(BuyPackageActivity.FIRSTBUY).start();
	}

	@Click(R.id.pay_continue_buy)
	void continueBuy() {
		BuyPackageActivity_.intent(mContext).accountmoney(mTotalMoney)
				.type(BuyPackageActivity.CONTINUEBUY).start();

	}

	@Click(R.id.pay_plus_buy)
	void plusBuy() {
		BuyFivePackageActivity_.intent(mContext).endTime(endTime)
		
				.accountmoney(mTotalMoney).start();
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}

	@AfterInject
	void initData() {
		
		mContext = this;
		instance = this;

	}

	@AfterViews
	void initUI() {

		back.setText("我");
		headTitle.setText("支付管理");

		PayUtils.showDialog(mContext);
		requestData(true);
	}

	private void skipActivity(Class<?> mClass) {

		Intent intent = new Intent(mContext, mClass);
		intent.putExtra("accountmoney", mTotalMoney);
		((Activity) mContext).startActivity(intent);
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}

	public void requestData(final boolean isFirst) {
		String url = mContext.getString(R.string.pay_info_url);
		new RequestAdpater() {

			@Override
			public void onReponse(ResponseData data) {
				PayUtils.closeDialog();

				if (data.getResultState() == ResultState.eSuccess) {
					callBackInitData(data.getJsonObject());
				} else {
					Toast.makeText(mContext, "" + data.getMsg(), 100)
							.show();
					if (isFirst) {
						finish();
					}
				}

			}

			@Override
			public void onProgress(ProgressMessage msg) {

			}
		}.setUrl(url).notifyRequest();
	}

	@UiThread
	protected void callBackInitData(JSONObject rootObj) {
		if (rootObj == null) {
			Toast.makeText(mContext, "没有数据", 100).show();
			return;
		}
		if (!rootObj.isNull("isBuySet") && rootObj.optBoolean("isBuySet")) {
			otherBought.setVisibility(View.VISIBLE);
		} else {
			if (!rootObj.isNull("isHasUnUsedFunction") && rootObj.optBoolean("isHasUnUsedFunction")) {
				otherBought.setVisibility(View.VISIBLE);
			} else {
				
				otherBought.setVisibility(View.GONE);
			}
			
		}
		if (!rootObj.isNull("account_name")) {
			mAccountName = rootObj.optString("account_name");
			accountName.setText(mAccountName);
		}
		if (!rootObj.isNull("total_balance")) {
			mTotalMoney = rootObj.optDouble("total_balance");
			mTmpMoney = mTotalMoney;
			accountMoney.setText("￥" + PayUtils.cleanZero(mTotalMoney));
		}
		JSONObject jsonObject = rootObj.optJSONObject("currentSetUseDTO");
		String desc = jsonObject.optString("desc");
		if (jsonObject.optBoolean("decideToPay")||rootObj.optBoolean("isHasFunction")) {
			line.setVisibility(View.GONE);
			lineRight.setVisibility(View.GONE);
			kind.setEnabled(true);
			btBuy.setVisibility(View.GONE);
			btContinue.setVisibility(View.VISIBLE);
			if(jsonObject.optBoolean("decideToPay"))
			btPlus.setVisibility(View.VISIBLE);
			else{
				btPlus.setVisibility(View.GONE);
			}
			useState.setText("当前使用状态（付费版）");
			DisplayMetrics dm = getResources().getDisplayMetrics();
			
			if(desc.length()<15)
			kind.setTextSize(kind.getTextSize()/dm.scaledDensity+2);
			kind.setTextColor(Color.parseColor("#379ed0"));
			if(desc.contains("稳定型")){
				btPlus.setVisibility(View.GONE);
			}
			
				kind.setText(desc);
			
		
			endTime = jsonObject.optLong("endday");
			timeout.setText(PayUtils.formatData("yyyy-MM-dd", endTime));
		} else {
			kind.setEnabled(false);
			line.setVisibility(View.VISIBLE);
			lineRight.setVisibility(View.VISIBLE);
			btBuy.setVisibility(View.VISIBLE);
			btContinue.setVisibility(View.GONE);
			btPlus.setVisibility(View.GONE);
			useState.setText("当前使用状态（免费版）");
			kind.setText(desc);
			timeout.setText("永久");
		}

		peopleCount.setText(jsonObject.optInt("currentPersonNum", 0) + "");

		if (jsonObject.optBoolean("decidePersonLimit")) {
			int pc = jsonObject.optInt("residueUpper", 0);
			if (pc < 0) {
				pc = Math.abs(pc);
				String count = String.valueOf(pc);

				String s = "(已超出" + count + "人)";

				addCount.setStart(4);
				addCount.setEnd(4 + count.length());
				addCount.setText(s);
			} else {
				String count = String.valueOf(pc);

				String s = "(还可新增" + count + "人)";

				addCount.setStart(5);
				addCount.setEnd(5 + count.length());
				addCount.setText(s);
			}
		} else {
			addCount.setStart(0);
			addCount.setEnd(0);
			addCount.setText("(人数不限)");
		}
		used.setText(String.format("%.1f",
				(jsonObject.optDouble("usedSpace", 0) / 1024)));

		if (jsonObject.isNull("leftSpace")) {
			useable.setText("(使用空间不限)");
		} else {
			useable.setText("(还可使用"
					+ String.format("%.1f",
							(jsonObject.optDouble("leftSpace", 0) / 1024))
					+ "G)");
		}
		animationFooter();
	}

	private void animationFooter() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(footer, View.Y,
				footer.getBottom(), footer.getTop());
		anim.setDuration(500);
		anim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				footer.setVisibility(View.VISIBLE);

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
		anim.start();

	}

	@Override
	public void finish() {
		super.finish();
		
		PayUtils.mUserName = "";
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
}
