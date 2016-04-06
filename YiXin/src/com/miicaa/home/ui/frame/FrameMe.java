package com.miicaa.home.ui.frame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.cast.PushMessage;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.OrgInfo;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.ui.enterprise.EnterpriceMainActivity;
import com.miicaa.home.ui.enterprise.EnterpriceMainActivity_;
import com.miicaa.home.ui.pay.PayMainActivity_;
import com.miicaa.home.ui.person.PersonHome;
import com.miicaa.home.ui.person.PersonUnitChange;
import com.miicaa.utils.AllUtils;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.view.TextViewPartColor;

/**
 * Created by Administrator on 13-11-25.
 */
public class FrameMe implements IFrameChild {
    Context mContext;
    View mRootView;
    LayoutInflater mInflater;
    Button mCompButton;
    Button mPayButton;
    Button mDiscuss;
    Button mSettingButton;
    Button mExitButton;
    TextViewPartColor mExpir;
    TextView version;
    private Animation mPageInAnim = null;


    public FrameMe(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        initUI();
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public void refresh() {
        UserInfo userInfo = AccountInfo.instance().getLastUserInfo();
        OrgInfo lastOrgInfo = AccountInfo.instance().getLastOrgInfo();
        if (lastOrgInfo != null) {
            mCompButton.setText(lastOrgInfo.getName());
        }
    }

    @Override
    public void setMsg(String msg) {

    }

    private void initUI() {
        mRootView = mInflater.inflate(R.layout.frame_me_activity, null);
        mCompButton = (Button) mRootView.findViewById(R.id.frame_me_id_comp_name);
        mCompButton.setOnClickListener(onCompClick);
        mExpir = (TextViewPartColor) mRootView.findViewById(R.id.frame_me_id_package_expir);
        mExpir.setOnClickListener(onExpirClick);
        mPayButton = (Button) mRootView.findViewById(R.id.frame_me_id_pay);
        mPayButton.setOnClickListener(onPayClick);
        mDiscuss = (Button) mRootView.findViewById(R.id.frame_me_id_discuss);
        mDiscuss.setOnClickListener(onDiscussClick);
        mSettingButton = (Button) mRootView.findViewById(R.id.frame_me_id_setting);
        mSettingButton.setOnClickListener(onSettingClick);
        //暂时屏蔽此功能
        mSettingButton.setVisibility(View.INVISIBLE);
        mExitButton = (Button) mRootView.findViewById(R.id.frame_me_id_exit);
        mExitButton.setOnClickListener(onExitClick);
        version = (TextView)mRootView.findViewById(R.id.version);
        
        try {
        	String v = MyApplication.getInstance().getVersionCode();
            version.setText(v);
			
		} catch (Exception e) {
			
			e.printStackTrace();
        
		}
        UserInfo userInfo = AccountInfo.instance().getLastUserInfo();
        OrgInfo lastOrgInfo = AccountInfo.instance().getLastOrgInfo();
        if (lastOrgInfo != null) {
            mCompButton.setText(lastOrgInfo.getName());
        }
    }

    private void loadUserHeadImg() {
        UserInfo userInfo = AccountInfo.instance().getLastUserInfo();
       
    }

    View.OnClickListener onUserClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, PersonHome.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", AccountInfo.instance().getLastUserInfo().getCode());
            intent.putExtra("bundle", bundle);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onCompClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, PersonUnitChange.class);
            ((Activity) mContext).startActivityForResult(intent, 99);
            ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
//            listener.changeCommp();
        }
        
    };
    
    View.OnClickListener onPayClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        	 Intent intent = new Intent(mContext, PayMainActivity_.class);
             ((Activity) mContext).startActivity(intent);
             ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };
    
    View.OnClickListener onDiscussClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        	String url = mContext.getString(R.string.miicaadiscuss);
	    	Intent intent = new Intent();        
	        intent.setAction("android.intent.action.VIEW");    
	        Uri content_url = Uri.parse(url);   
	        intent.setData(content_url);  
	        ((Activity)mContext).startActivity(intent);
             ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };


    View.OnClickListener onSettingClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        	
        }
    };

    View.OnClickListener onExitClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        	view.setEnabled(false);
        	AllUtils.hiddenSoftBorad(mContext);
          //  Activity activity = (Activity) mContext;
            new Thread(){
            	public void run(){
            		 MyApplication.getInstance().onReLogin();
                     PushMessage.stopPushMessage(mContext);//停止消息推送服务
            	}
            }.start();
         //   activity.finish();
        }
    };
    
    OnClickListener onExpirClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EnterpriceMainActivity_.intent(mContext)
			.url("/home/phone/message/getalertmessage")
			.title("产品到期提醒")
			.start();
			
		}
	};

	public void showPay(int visiablity) {
		mPayButton.setVisibility(visiablity);
		
	}

	public void showPackageExpir(int visible) {
		mExpir.setVisibility(visible);
		
	}
    
    
}
