package com.miicaa.home.ui.frame;

import java.util.ArrayList;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.common.base.DatabaseOption;
import com.miicaa.common.base.OnEachRow;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.GroupInfo;
import com.miicaa.home.data.business.org.UnitInfo;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.business.org.UserInfoSql;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.IvtPeople.IvtPeopleActivity;
import com.miicaa.home.ui.contactList.ContactData;
import com.miicaa.home.ui.contactList.ContactList;

/**
 * Created by Administrator on 13-11-25.
 */
public class FrameContacts implements IFrameChild {
    Context mContext;
    View mRootView;
    TextView mLable;
    LayoutInflater mInflater;
    Button mRefreshButton;
    Button mIvtPeopleButton;
    int mLoadingCount = 0;
    private AsyncQueryHandler asyncQuery;

    public FrameContacts(Context context) {
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
        createListView(getAllUserInOrg());
    }

    @Override
    public void setMsg(String msg) {

    }

    private void initUI() {
        mRootView = mInflater.inflate(R.layout.frame_contact_activity, null);
        mRefreshButton = (Button) mRootView.findViewById(R.id.contact_refresh_button);
        mRefreshButton.setOnClickListener(onRefreshlick);
        mLable = (TextView)mRootView.findViewById(R.id.org_person__refresh_lable);
        mIvtPeopleButton = (Button)mRootView.findViewById(R.id.frame_contact_ivt_people_button);
        mIvtPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, IvtPeopleActivity.class);
                mContext.startActivity(intent);
            }
        });
        createListView(getAllUserInOrg());

//		asyncQuery = new MyAsyncQueryHandler(mContext.getContentResolver());
//		Uri uri = Uri.parse("content://com.android.contacts/data/phones");
//		String[] projection = {"_id", "display_name", "data1", "sort_key"};
//		asyncQuery.startQuery(0, null, uri, projection, null, null,
//				"sort_key COLLATE LOCALIZED asc");
    }

    View.OnClickListener onRefreshlick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mRefreshButton.setVisibility(View.INVISIBLE);
            mLable.setText("刷新中...   ");
            mLable.setVisibility(View.VISIBLE);
            mLoadingCount=0;
            //初始化部门
            mLoadingCount++;
            DatabaseOption.getIntance()
                    .setValue(AccountInfo.instance().getLastUserInfo().getCode() + "unitInit", "00");
            new RequestAdpater() {
                @Override
                public void onReponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        UnitInfo.deleteByOrgCode(AccountInfo.instance().getLastUserInfo().getOrgCode());
                        Tools.initUnitsData(data.getJsonArray());
                        DatabaseOption.getIntance()
                                .setValue(AccountInfo.instance().getLastUserInfo().getCode()
                                        + "unitInit", "01");
                    }
                    loaded();
                }

                @Override
                public void onProgress(ProgressMessage msg) {
                }
            }.setUrl("/mobile/mobile/oug/getAll")
                    .addParam("type", "unit")
                    .addParam("orgCode", AccountInfo.instance().getLastOrgInfo().getCode())
                    .notifyRequest();
            //end 初始化部门
            //初始化职位
            mLoadingCount++;
            DatabaseOption.getIntance()
                    .setValue(AccountInfo.instance().getLastUserInfo().getCode() + "groupInit", "00");
            new RequestAdpater() {
                @Override
                public void onReponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        GroupInfo.deleteByOrgCode(AccountInfo.instance().getLastUserInfo().getOrgCode());
                        Tools.initGroupsData(data.getJsonArray());
                        DatabaseOption.getIntance()
                                .setValue(AccountInfo.instance().getLastUserInfo().getCode()
                                        + "groupInit", "01");
                    }
                    loaded();

                }

                @Override
                public void onProgress(ProgressMessage msg) {
                }
            }.setUrl("/mobile/mobile/oug/getAll")
                    .addParam("type", "group")
                    .addParam("orgCode", AccountInfo.instance().getLastOrgInfo().getCode())
                    .notifyRequest();
            //end 初始化职位
            //初始化人员
            mLoadingCount++;
            DatabaseOption.getIntance()
                    .setValue(AccountInfo.instance().getLastUserInfo().getCode() + "userInit", "00");
            new RequestAdpater() {
                @Override
                public void onReponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        UserInfo.deleteByOrgCode(AccountInfo.instance().getLastUserInfo().getOrgCode());
                        Tools.initUsersData(data.getJsonArray());
                        DatabaseOption.getIntance()
                                .setValue(AccountInfo.instance().getLastUserInfo().getCode()
                                        + "userInit", "01");
                    }
                    loaded();

                }

                @Override
                public void onProgress(ProgressMessage msg) {
                }
            }.setUrl("/mobile/mobile/oug/getAll")
                    .addParam("type", "user")
                    .addParam("orgCode", AccountInfo.instance().getLastOrgInfo().getCode())
                    .notifyRequest();
            //end 初始化人员
            //初始化部门人员
            mLoadingCount++;
            DatabaseOption.getIntance()
                    .setValue(AccountInfo.instance().getLastUserInfo().getCode() + "unitUserInit", "00");
            new RequestAdpater() {
                @Override
                public void onReponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        Tools.initUnitUsersData(data.getJsonArray());
                        DatabaseOption.getIntance()
                                .setValue(AccountInfo.instance().getLastUserInfo().getCode()
                                        + "unitUserInit", "01");
                    }
                    loaded();

                }

                @Override
                public void onProgress(ProgressMessage msg) {
                }
            }.setUrl("/mobile/mobile/oug/getAll")
                    .addParam("type", "unitUser")
                    .addParam("orgCode", AccountInfo.instance().getLastOrgInfo().getCode())
                    .notifyRequest();
            //end 初始化部门人员
            mLoadingCount++;
            DatabaseOption.getIntance()
                    .setValue(AccountInfo.instance().getLastUserInfo().getCode() + "groupUserInit", "00");
            new RequestAdpater() {
                @Override
                public void onReponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        Tools.initGroupUsersData(data.getJsonArray());
                        DatabaseOption.getIntance()
                                .setValue(AccountInfo.instance().getLastUserInfo().getCode()
                                        + "groupUserInit", "01");
                    }
                    loaded();

                }

                @Override
                public void onProgress(ProgressMessage msg) {
                }
            }.setUrl("/mobile/mobile/oug/getAll")
                    .addParam("type", "groupUser")
                    .addParam("orgCode", AccountInfo.instance().getLastOrgInfo().getCode())
                    .notifyRequest();

        }
    };

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
                ((ArrayList<ContactData>) cbData).add(contactData);
            }
        }, list);
        return list;
    }


    private void createListView(ArrayList<ContactData> contactDataArrayList) {

        LinearLayout rootLayout = (LinearLayout) mRootView.findViewById(R.id.frame_contact_activity_root_layout);
        rootLayout.removeAllViews();

        ContactList contactList = new ContactList(mContext, contactDataArrayList,"newList",null,ContactList.MUTILSELECT);
        View list = contactList.getRootView();
        rootLayout.addView(list);
    }

    private void loaded() {
        mLoadingCount--;
        if (mLoadingCount <= 0) {
            //mLoadingLayout.setVisibility(View.GONE);
            mLable.setText("");
            mLable.setVisibility(View.INVISIBLE);
            mRefreshButton.setVisibility(View.VISIBLE);
            refresh();
        }
    }
}
