package com.miicaa.home.data.business.account;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.JSONValue;
import com.miicaa.common.base.OnTransaction;
import com.miicaa.home.cast.PushMessage;
import com.miicaa.home.data.OnFinish;
import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.business.org.EntirpiseInfo;
import com.miicaa.home.data.business.org.OrgInfo;
import com.miicaa.home.data.business.org.OrgInfoSql;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.service.CacheCtrlSrv;
import com.miicaa.home.data.storage.ConfigDatabase;
import com.miicaa.home.data.storage.LocalDatabase;
import com.miicaa.home.data.storage.SqlCmd;
import com.miicaa.home.provider.EnterpiceProvider;
import com.miicaa.home.ui.enterprise.EnterpriseLocation;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.service.ContactRefreshService;
import com.miicaa.utils.AllUtils;

/**
 * Created by Administrator on 13-12-19.
 */
public class LoginInfo {
    private static LocalDatabase db;
    static String TAG = "LoginInfo";

    public static void init() {
        db = ConfigDatabase.instance();

        if (!db.isTableExsit(LoginInfoSql.tb_name_login_info)) {
            //创建登录信息表
            db.execCmd(LoginInfoSql.createTable());
        }
    }

    public static void showAll() {
        ArrayList<ContentValues> rows = db.queryCmd(LoginInfoSql.showAll());
        SqlCmd.showRows(rows);
    }

    public static LoginInfo lastLogin() {
        if (db == null) {
            LoginInfo.init();
        }
        ContentValues row = db.firstRow(LoginInfoSql.getLastLogin());
        return LoginInfoSql.fromRow(row);
    }

    public LoginInfo() {
    }

    public LoginInfo(String userEmail,
                     String userPassword,
                     Boolean loginAuto) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.loginAuto = loginAuto;
        this.loginType = "";
        this.lastTime = null;
    }

    private LoginInfo(String userEmail,
                      String userPassword,
                      Boolean loginAuto,
                      String loginType,
                      String lastOrgCode,
                      String lastUserCode) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.loginAuto = loginAuto;
        this.loginType = loginType;
        this.lastTime = null;
        this.lastOrgInfo = OrgInfo.findByCode(lastOrgCode);
        this.lastUserInfo = UserInfo.findByCode(lastUserCode);
    }

    public Boolean check(LoginInfo other) {
        return (this.userEmail.equals(other.userEmail)) &&
                (this.userPassword.equals(other.userPassword)) &&
                (this.loginAuto.equals(other.loginAuto)) &&
                (this.loginType.equals(other.loginType)) &&
                (this.lastTime.equals(other.lastTime)) &&
                (this.lastOrgInfo.getCode().equals(other.lastOrgInfo.getCode())) &&
                (this.lastUserInfo.getCode().equals(other.lastUserInfo.getCode()));
    }

	public Boolean addLogin() {
        this.lastTime = DateHelper.getDate();
        return db.execCmd(LoginInfoSql.insertInto(this));
    }
	
	public Boolean updateLoginAuto(Boolean auto){
		this.loginAuto = auto;
		return db.execCmd(LoginInfoSql.upatDateLoginAuto(auto));
	}

    // 用户登录
    public void login(final Context context,final AccountInfo accountInfo, final OnFinish onFinish) {
        if (CacheCtrlSrv.isNetBreak) {    // 断网状态下
            LoginInfo lastLogin = LoginInfo.lastLogin();
            if (lastLogin != null && lastLogin.loginAuto && lastLogin.check(this)) {
                // 初始化当前用户账户信息
                loginSuccess(context,null, accountInfo);
                onFinish.onSuccess(null);
            } else {
                onFinish.onFailed("当前账户信息与本地缓存的账户信息不一致。");
            }
        } else {    // 联网状态下
            AccountRequest.logionRequest(new OnBusinessResponse() {
                @Override
                public void onProgress(ProgressMessage msg) {
                }

                @Override
                public void onResponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        // 初始化当前用户账户信息
                        loginSuccess(context,data, accountInfo);
                        onFinish.onSuccess(data.getJsonObject());
                    } else {
                        onFinish.onFailed(data.getMsg());
                    }
                }
            }, userEmail, userPassword);
        }
    }

    // 切换单位
    public void changeOrg(final Context context,final AccountInfo accountInfo, OrgInfo info, final OnFinish onFinish) {
        if (CacheCtrlSrv.isNetBreak) {    // 断网状态下
            lastOrgInfo = info;
            lastUserInfo = UserInfo.findByEmailAndOrgCode(userEmail, lastOrgInfo.getCode());
            // 初始化当前用户账户信息
            loginSuccess(context,null, accountInfo);
            onFinish.onSuccess(null);
        } else {    // 联网状态下
            AccountRequest.changeOrgRequest(new OnBusinessResponse() {
                @Override
                public void onProgress(ProgressMessage msg) {
                }

                @Override
                public void onResponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        // 初始化当前用户账户信息
                        loginSuccess(context,data, accountInfo);
                        onFinish.onSuccess(data.getJsonObject());
                    } else {
                        onFinish.onFailed(data.getMsg());
                    }
                }
            }, info.getCode());
        }
    }

    // 登录成功
    private void loginSuccess(final Context context,final ResponseData data, final AccountInfo accountInfo) {
    	PayUtils.mUserName = "";
    	final JSONValue res = JSONValue.from(data.getJsonObject());
    	final JSONValue curOrg = res.getJsonValue("currentOrg");
    	final String userCode = res.getString("userCode");
        final String userName = res.getString("userName");
        Log.d(TAG, "loginSuccess data"+data.getJsonObject());
        if (curOrg != null) {
    	lastOrgInfo = fromJSONValue(curOrg);
        }
    	lastUserInfo = new UserInfo(0, userCode, userName, userEmail, "", 0, "", 0,
                    lastOrgInfo.getCode(), "", 0, "", "", "");
    	
    		
        db.onTransaction(new OnTransaction() {
            @Override
            public void transaction() {
                if (data == null) {
                    loginType = LoginInfoSql.col_val_login_type_local;
                } else {
                    loginType = LoginInfoSql.col_val_login_type_net;
                    if (res.hasValue()) {
                        JSONArray orgs = res.getJsonArray("orgs");//全部单位
                        if (orgs != null) {
                            cacheUserAndOrg(orgs);
                        }
                        
                        
                            UserInfo tUserInfo = UserInfo.findByCode(userCode);
                            if(tUserInfo != null && tUserInfo.getOrgCode()!=null){
                            	/*更新最后登陆信息*/
                            lastUserInfo = tUserInfo;
//                            AccountInfo.instance().changeLastUserInfo(lastUserInfo);
                            }
                            lastUserInfo.save();
                         // 实例化当前账户的详细信息
                            accountInfo.accountByLogin(LoginInfo.this);
                            
                            /*企业服务
                             * 
                             */
                            EntirpiseInfo entirpiseInfo = EntirpiseInfo.findByCodeMail(context, 
                            		AccountInfo.instance().getLastUserInfo().getCode(), 
                            		AccountInfo.instance().getLastUserInfo().getEmail());
                            if(entirpiseInfo == null){
                            entirpiseInfo = new EntirpiseInfo();
                            entirpiseInfo.gonggaoCount = -1;
                            entirpiseInfo.locationType = EnterpriseLocation.getInstance().getLocationType();
                            entirpiseInfo.userCode = AccountInfo.instance().getLastUserInfo().getCode();
                            entirpiseInfo.userEmail = AccountInfo.instance().getLastUserInfo().getEmail();
                            EntirpiseInfo.saveOrUpdate(context, entirpiseInfo);
                            context.getContentResolver().notifyChange(EnterpiceProvider.CONTENT_URI, null);
                            }
                            
                            /*重新绑定userCode与个推clientId的关系，推送接收*/
                            AllUtils.refreshClient(AccountInfo.instance().getClientId());
                            /**
                             * 定时刷新通讯录
                             */
                            ContactRefreshService.init(context);
                            /**
                             * 开启推送服务
                             */
                            PushMessage.initAuroraPush(context);
                    }
                }

                // 记录登录日志
                addLogin();
            }
        });
    }

    private OrgInfo fromJSONValue(JSONValue org) {
        return new OrgInfo(Long.parseLong(org.getString(OrgInfoSql.col_name_id)),
                org.getString(OrgInfoSql.col_name_code),
                org.getString(OrgInfoSql.col_name_name),
                org.getLong(OrgInfoSql.col_name_sort),
                org.getLong(OrgInfoSql.col_name_status),
                org.getString(OrgInfoSql.col_name_desc),
                org.getLong(OrgInfoSql.col_name_create_time));
    }
    
    /*
     * 登录完成删除所有的单位从新保存
     */
    private void cacheUserAndOrg(JSONArray orgs) {
    	OrgInfo del = new OrgInfo();
    	del.deleteAll();
        for (int i = 0; i < orgs.length(); i++) {
            JSONValue org = JSONValue.from(orgs.optJSONObject(i));
            if (org.hasValue()) {
                fromJSONValue(org).save();
            }
        }
    }

    private String userEmail = "";
    private String userPassword = "";
    private Boolean loginAuto = false;
    private String loginType = "";
    private Date lastTime;
    private OrgInfo lastOrgInfo;
    private UserInfo lastUserInfo;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Boolean getLoginAuto() {
        return loginAuto;
    }

    public void setLoginAuto(Boolean loginAuto) {
        this.loginAuto = loginAuto;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public OrgInfo getLastOrgInfo() {
        return lastOrgInfo;
    }

    public void setLastOrgInfo(OrgInfo lastOrgInfo) {
        this.lastOrgInfo = lastOrgInfo;
    }

    public UserInfo getLastUserInfo() {
        return lastUserInfo;
    }

    public void setLastUserInfo(UserInfo lastUserInfo) {
        this.lastUserInfo = lastUserInfo;
    }
}
