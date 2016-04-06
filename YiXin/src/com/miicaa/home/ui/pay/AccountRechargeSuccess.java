package com.miicaa.home.ui.pay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;

@EActivity(R.layout.layout_pay_account_success)
public class AccountRechargeSuccess extends Activity {

	public static final int METHOD_PAY = 0;
	public static final int METHOD_RECHARGE = 1;

	@Extra
	double money;
	@Extra
	int method;

	@ViewById(R.id.pay_headTitle)
	TextView headTitle;// 标题

	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.recharge_money)
	TextView mMoney;

	@ViewById(R.id.recharge_buy)
	TextView mBuy;
	@ViewById(R.id.recharge_bill)
	TextView mBill;
	@ViewById(R.id.recharge_myfund)
	TextView mMyfund;
	@ViewById(R.id.head_img)
	ImageView img;
	@ViewById(R.id.recharge_success_layout)
	LinearLayout layout;

	@Click(R.id.recharge_buy)
	void buy() {
		if(PayMainActivity.instance == null){
			PayUtils.showToast(this, "请返回支付管理页操作", 2000);
			return;
			
		}
		if(PayMainActivity.instance.mTmpMoney<=0){
			PayUtils.showToast(this, "正在刷新账户余额", 2000);
			return;
		}
		BuyPackageActivity_.intent(this)
		.accountmoney(PayMainActivity.instance.mTmpMoney)
		.type(BuyPackageActivity.CONTINUEBUY)
		.start();
		
		((Activity) this).overridePendingTransition(R.anim.my_slide_in_right,
				R.anim.my_slide_out_left);
		
	}

	@Click(R.id.recharge_myfund)
	void myFund() {
		Intent intent = new Intent(this, MyFundActivity_.class);

		((Activity) this).startActivity(intent);
		((Activity) this).overridePendingTransition(R.anim.my_slide_in_right,
				R.anim.my_slide_out_left);
		
	}

	@Click(R.id.recharge_bill)
	void bill() {
		BillRecordActivity_.intent(this).start();
		((Activity) this).overridePendingTransition(R.anim.my_slide_in_right,
				R.anim.my_slide_out_left);
		
	}

	@Click(R.id.pay_cancleButton)
	void cancel() {
		finish();
	}

	@AfterInject
	void initData() {
		
		if (PayMainActivity.instance != null){
			PayMainActivity.instance.mTmpMoney = 0;
			PayMainActivity.instance.requestData(false);
		}
		if (MyFundActivity.instance != null) {
			MyFundActivity.instance.resetList();
			MyFundActivity.instance.requestMatter();
		}
	}

	@AfterViews
	void initUI() {
		
		headTitle.setText("充值成功");
		back.setText("支付管理");
		mMoney.setText("￥" + PayUtils.cleanZero(String.format("%.2f", money)));
		switch (method) {
		case METHOD_PAY:
			layout.setVisibility(View.GONE);
			mBuy.setVisibility(View.GONE);
			mMyfund.setVisibility(View.GONE);
			mBill.setVisibility(View.VISIBLE);
			img.setBackgroundResource(R.drawable.account_payment_success);
			break;
		case METHOD_RECHARGE:
			layout.setVisibility(View.VISIBLE);
			mBuy.setVisibility(View.VISIBLE);
			mMyfund.setVisibility(View.VISIBLE);
			mBill.setVisibility(View.GONE);
			img.setBackgroundResource(R.drawable.account_recharge_success);
			break;
		default:
			break;
		}
	}

	@Override
	public void finish() {
		super.finish();
		
		PayMainActivity_.intent(this)
		.flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
		.start();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
}
