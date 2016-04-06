package com.miicaa.home.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.miicaa.base.pay.wx.Constants;
import com.miicaa.home.R;
import com.miicaa.home.ui.enterprise.CommandCall;
import com.miicaa.home.ui.pay.AccountRechargeSuccess;
import com.miicaa.home.ui.pay.AccountRechargeSuccess_;
import com.miicaa.home.ui.pay.AlipayUtils;
import com.miicaa.home.ui.pay.PayUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelbase.BaseResp.ErrCode;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        findViewById(R.id.pay_cancleButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch(resp.errCode){
			case ErrCode.ERR_OK:
				paySuccess();
				
				finish();
				break;
				default:
					PayUtils.showToast(this, "支付失败:"+resp.errCode, 2000);
					finish();
					break;
			}
			
		}else{
			finish();
		}
	}
	
	protected void paySuccess() {
		if(AlipayUtils.from!=null && AlipayUtils.from instanceof CommandCall){
			CommandCall cc = (CommandCall) AlipayUtils.from;
			cc.callBack();
			return;
		}
        AccountRechargeSuccess_.intent(this)
        .money(AlipayUtils.mFaceValues)
        .method(AccountRechargeSuccess.METHOD_RECHARGE)
        .start();
       overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
       
	}
}