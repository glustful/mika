package com.miicaa.home.ui.person;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.miicaa.home.R;
import com.miicaa.home.data.OnFinish;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.account.LoginInfo;
import com.miicaa.home.data.business.org.OrgInfo;
import com.miicaa.home.ui.menu.ScreenType;
import com.miicaa.utils.NetUtils.OnReloginListener;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.push.service.PushCliService;

/**
 * Created by Administrator on 13-11-29.
 */
public class PersonUnitChange extends Activity {
    LinearLayout mList;
    String mSelCode;
    ChangeCommpListener listener;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_unit_change_activity);
        initUI();
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }

    private void initUI() {
        Button button = (Button) findViewById(R.id.person_unit_id_back);
        button.setOnClickListener(onBackClick);

        mList = (LinearLayout) findViewById(R.id.person_unit_id_list);
    }

    private void initData() {
        mSelCode = AccountInfo.instance().getLastUserInfo().getOrgCode();
        for (int i = 0; i < AccountInfo.instance().getOrgs().size(); i++) {
            OrgInfo org = AccountInfo.instance().getOrgs().get(i);
            UnitItem item = null;
            item = new UnitItem(org.getCode(), org.getName());
            if (item == null) {
                continue;
            }
//            if (AccountInfo.instance().getOrgs().size() == 1) {
//                item = new UnitItem(org.getCode(), org.getName());
//            } else if (i == 0) {
//                item = new UnitItem(org.getCode(), org.getName());
//            } else if (i == AccountInfo.instance().getOrgs().size() - 1) {
//                item = new UnitItem(org.getCode(), org.getName());
//            } else {
//                item = new UnitItem(org.getCode(), org.getName());
//            }
        
            mList.addView(item.getRoot());
            if (org.getCode().equals(mSelCode)) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    class UnitItem {
        Button mItemButton;
        View mRoot = null;
        String mCode;
        String mName;
        Boolean mSelected;

        public UnitItem(String code, String name) {
            mCode = code;
            mName = name;
            mSelected = false;
            init();
        }

        public View getRoot() {
            return mRoot;
        }

        private void init() {
            LayoutInflater inflater = LayoutInflater.from(PersonUnitChange.this);
            mRoot = inflater.inflate(R.layout.person_unit_item_view, null);
            mItemButton = (Button) mRoot.findViewById(R.id.person_unit_item_id_name);
            mItemButton.setText(mName);
            mItemButton.setOnClickListener(onItemClick);
        }

        public void setBackground(int bg) {
           // mItemButton.setBackgroundDrawable(getResources().getDrawable(bg));
        }

        public void setSelected(Boolean selected) {
            mSelected = selected;
            mItemButton.setSelected(mSelected);
        }
        
        private void chanegFaied(String msg){
        mItemButton.setClickable(true);
        new AlertDialog.Builder(PersonUnitChange.this)
                .setTitle("切换单位")
                .setMessage("切换单位失败：" + msg)
                .setPositiveButton("确定", null)
                .show();
        }

        View.OnClickListener onItemClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelected) {
                    return;
                }
                mItemButton.setClickable(false);
                for (OrgInfo o : AccountInfo.instance().getOrgs()) {
                    if (o.getCode().equals(mCode)) {
                        LoginInfo mLoginInfo = null;
                        mLoginInfo = LoginInfo.lastLogin();
                        MyApplication account = (MyApplication) getApplicationContext();
//                        PushCliService.stopService(PersonUnitChange.this);
                        mLoginInfo.changeOrg(PersonUnitChange.this,account.getAccountInfo(),o, new OnFinish() {
                            @Override
                            public void onSuccess(JSONObject res) {
                            	MyApplication.getInstance().relogin(new OnReloginListener() {
									
									@Override
									public void success() {
										//清空筛选条件
//										PushCliService.startService(PersonUnitChange.this);
		                            	ScreenType.getInstance().removeAllTypes();
		                            	mItemButton.setClickable(true);
		                                Intent intent = getIntent();
		                                PersonUnitChange.this.setResult(99, intent);
		                                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		                                nm.cancelAll();
		                                finish();
									}
									
									@Override
									public void failed() {
										chanegFaied("");
									}
								});
                            	
                            }

                            @Override
                            public void onFailed(String msg) {
                            	chanegFaied(msg);
                            }
                        });
                    }
                }
            }
        };
        

    }
    
    private void changeFailed(){
    	
    }
    
    public interface ChangeCommpListener{
    	void changeCommp();
    }
    public void setChangeCommpListener(ChangeCommpListener listener){
    	this.listener = listener;
    }
}