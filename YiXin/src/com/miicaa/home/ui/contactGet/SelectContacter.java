package com.miicaa.home.ui.contactGet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.OnEachRow;
import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.business.org.UserInfoSql;
import com.miicaa.home.ui.contactList.ContactData;
import com.miicaa.home.ui.contactList.ContactList;
import com.miicaa.home.ui.contactList.ContactList.OnSelectChangeListener;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.org.LableGroup;
import com.yxst.epic.yixin.activity.ChatActivity_;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.data.dto.request.CreateQunRequest;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.CreateQunResponse;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.rest.IMInterfaceProxy;
import com.yxst.epic.yixin.utils.CacheUtils;

/**
 * Created by LM on 14-7-22.
 */
public class SelectContacter extends Activity implements RestErrorHandler{
	
	static String TAG = "SelectContacter";
	
    private Button backButton;
    private Button saveButton;
    private LinearLayout rootLinearLayout;
    TextView title;
    private ContactList contactList;
    private LableGroup mViewGroup;
    private ArrayList<ContactViewShow> arrangeNames;
    private ArrayList<ContactViewShow> contactViewShows;
    public final static String COPY ="copy";
    public final static String APPROVE = "apprvoe";
    public final static String ARRANGE = "arrange";
    public final static String changeApp = "changeApp";
    public final static String NEWAPPROVE = "newapprove";
    public final static String QUNLIAO = "qunliao";
    public final static String ADDQUN = "addqun";
    public final static String ROUND = "round";
    public final static String STARTROUND = "startMember";
    public final static String DISCU = "discu";    
    public final static String how = "how";
    String s;
    IMInterface imi;
    @Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			Utils.hiddenSoftBorad(this);
			getCurrentFocus().clearFocus();
		}
		return super.dispatchTouchEvent(ev);
	}
    
    ArrayList<String> names;
    ArrayList<String> codes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        imi = IMInterfaceProxy.create();
        imi.setRestErrorHandler(this);
        
        contactViewShows = new ArrayList<ContactViewShow>();
        Intent intent  = getIntent();
        String str = "";
        Bundle bundle = intent.getBundleExtra("bundle");
        /*
         * 群聊已经选中的人
         */
        ArrayList<String> extraUids = new ArrayList<String>();
       ArrayList<String> extraUserCodes = null;
       int type = bundle.getInt("type", ContactList.MUTILSELECT);
      
        if (bundle != null){
            names = bundle.getStringArrayList("name");
            codes = bundle.getStringArrayList("code");
            if (names!=null){
                for (int i = 0;i < names.size(); i++){
                    ContactViewShow contactViewShow = new ContactViewShow(names.get(i),codes.get(i));
                    contactViewShows.add(contactViewShow);
                    
                }
            } 
            
           extraUids = bundle.getStringArrayList("uids");
           extraUserCodes = bundle.getStringArrayList("extraUserCodes");
           s = bundle.getString("how");
          
            if(ARRANGE.equals(s)){
            	str = "选择办理人";
            }else if(APPROVE.equals(s)){
            	str = "选择下一审批人";
            }else if(COPY.equals(s)){
            	str = "选择抄送人";
            }else if(changeApp.equals(s)){
            	str = "修改审批人";
            }else if(NEWAPPROVE.equals(s)){
            	str = "选择审批人";
            }else if(QUNLIAO.equals(s)){
            	str = "发起群聊";
            }else if(ROUND.equals(s)){
            	str = "选择人员";
           }else if(STARTROUND.equals(s)){
            	str = "选择发起人";
         }else if(ADDQUN.equals(s)){
        	 str = "添加群成员";
         }else if(DISCU.equals(s)){
            	str = "选择点评人";
            	//type = ContactList.SINGLESELECT;        
            	}
        }
        ArrayList<ContactData> contactDatas = getAllUserInOrg();
        setContentView(R.layout.contact_get_activity);
        backButton = (Button)findViewById(R.id.contact_get_back_button);
        title = (TextView)findViewById(R.id.title);
        title.setText(str);
        saveButton = (Button)findViewById(R.id.contact_get_save_button);
//        setSaveButtonClickable(contactViewShows.size() == 0 ? false : true);
        Log.d(TAG, "saveButton clickable:"+saveButton.isClickable());
        rootLinearLayout = (LinearLayout)findViewById(R.id.contact_get_root_layout);
        contactList = new ContactList(SelectContacter.this,contactDatas, ContactUtil.USER_SELECT,contactViewShows,type)
        .setSelectChangeListener(new OnSelectChangeListener() {
			
			@Override
			public void onSelectChange(int selectCount) {
//				setSaveButtonClickable(selectCount == 0 ? false : true);
				 Log.d(TAG, "setSelectChangeListener clickable:"+saveButton.isClickable());
			}
		});
        contactList.setCannotUid(extraUids);
        contactList.setCannotUserCode(extraUserCodes);
        saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                	Log.d(TAG, "saveButton is clicked!");
                	 arrangeNames = contactList.getArrangeCon();
                	if(QUNLIAO.equals(s)){
                		/*
                		 * 创建群聊
                		 */
                		createQun();
                	}else if(ADDQUN.equals(s)){
                		/*
                		 * 添加群成员
                		 */
                		Intent data = new Intent();
                		data.putExtra("members",
    							(Serializable)addMemberList(arrangeNames));
                		setResult(RESULT_OK,data);
                		finish();
                	}else{
                	
                    Intent intent1 = new Intent();
                    intent1.putParcelableArrayListExtra( ContactUtil.SELECT_BACK,arrangeNames);
                    setResult(RESULT_OK,intent1);
                    Utils.hiddenSoftBorad(SelectContacter.this);
                    finish();
                	}
                }
            });

        rootLinearLayout.addView(contactList.getRootView());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    
    
    
    
    
    /*
     * 发起群聊
     */
    ProgressDialog dialog;
	void createQun(){
    	new AsyncTask<String, Integer, CreateQunResponse>(){

    		@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
    			dialog = ProgressDialog.show(SelectContacter.this, "群聊", "正在加载");
				super.onPreExecute();
			}
			@Override
			protected CreateQunResponse doInBackground(
					String... params) {
				// TODO Auto-generated method stub
				if(arrangeNames.size() == 0){
					return null;
				}
		    	BaseRequest request = CacheUtils.getBaseRequest(SelectContacter.this);
		    	CreateQunRequest qunRequest = new CreateQunRequest();
		    	qunRequest.BaseRequest = request;
		    	qunRequest.addMembers(addMemberList(arrangeNames));
		    	qunRequest.Topic = "我的群";
		    	qunRequest.MemberCount = qunRequest.MemberList.size();
		    	Log.d(TAG, "CreateQunRequest:"+qunRequest);
		    	CreateQunResponse response = imi.createQun(qunRequest);
				return response;
			}
			
			@Override
			protected void onProgressUpdate(Integer... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
			}
    		

			@Override
			protected void onPostExecute(CreateQunResponse response) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				super.onPostExecute(response);
				if(response == null){
		    		return;
		    	}
		    	if(response.BaseResponse.Ret != BaseResponse.RET_SUCCESS){
		    		Toast.makeText(SelectContacter.this, response.BaseResponse.ErrMsg,
							Toast.LENGTH_SHORT).show();
		    		return;
		    	}
//		    	Log.d(TAG, "create qun response:"+response);
		    	String localUserName = AccountInfo.instance().getLastUserInfo().getId()+"@user";
		    	Log.d(TAG, "localUserName"+localUserName);
		    	ChatActivity_.intent(SelectContacter.this).localUserName(localUserName)
		    	.remoteUserName(response.ChatRoomName)
		    	.remoteDisplayName(response.Topic)
		    	.start();
			}
    	}.execute();
    }
    
    
    List<Member> addMemberList(ArrayList<ContactViewShow> vs){
    	List<Member> members = new ArrayList<Member>();
    	for(ContactViewShow c : vs){
    		Log.d(TAG,"uid------:"+c.uid);
    		Member m = new Member();
    		m.Uid  = c.uid;
    		m.UserName  = c.uidName;
    		m.NickName = c.name;
    		members.add(m);
    	}
    	return members;
    }
    
    
    
    // 从缓存中查找本单位的所有联系人
    private ArrayList<ContactData> getAllUserInOrg() {
        ArrayList<ContactData> list = new ArrayList<ContactData>();
        UserInfo.usersInOrg(AccountInfo.instance().getLastOrgInfo(), new OnEachRow() {
            @Override
            public void eachRow(ContentValues row, Object cbData) {
            	
                UserInfo user = UserInfoSql.fromRow(row);
                ContactData contactData = new ContactData();
                contactData.setName(user.getName());
                contactData.setUserCode(user.getCode());
                contactData.setDataType("person");
                contactData.setQuanPing(user.getNamePY());
                contactData.setQuanPingFirst(user.getNameFPY());
                contactData.setUid(user.getId());
//                if(user.getId() == 0)
//                Log.d(TAG, user.getId()+"00000-----");
                if(codes != null && codes.contains(contactData.getUserCode()))
                	contactData.setSelect(true);
                ((ArrayList<ContactData>) cbData).add(contactData);
            }
        }, list);
        return list;
    }



	@Override
	public void onRestClientExceptionThrown(RestClientException arg0) {
		// TODO Auto-generated method stub
		if(dialog != null)
		dialog.dismiss();
		Toast.makeText(SelectContacter.this, "创建群失败。"+arg0.getMessage(), Toast.LENGTH_SHORT).show();
	}
	
	private void setSaveButtonClickable(Boolean clickable){
		if(clickable){
			saveButton.setClickable(true);
			saveButton.setTextColor(Color.WHITE);
		}else{
			saveButton.setClickable(false);
			saveButton.setTextColor(getResources().getColor(R.color.savebtn_noclick_color));;
		}
	}
	
	
	
	

}
