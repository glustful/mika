package com.miicaa.home.data.business.account;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

import android.content.ContentValues;
import android.util.Log;

import com.miicaa.home.data.DataCenter;
import com.miicaa.home.data.business.org.OrgInfo;
import com.miicaa.home.data.business.org.OrgInfoSql;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.service.CacheCtrlSrv;
import com.miicaa.home.data.storage.ConfigDatabase;
import com.miicaa.home.data.storage.LocalPath;
import com.miicaa.home.data.storage.SqlCmd;

/**
 * Created by Administrator on 13-12-17.
 */
public class AccountInfo {
    private static AccountInfo _instance = null;
    private Boolean mMainFrameState;
    
    private String userEmail;
    public String userPassword;
    private OrgInfo lastOrgInfo;
    private UserInfo lastUserInfo;
    private ArrayList<OrgInfo> orgs;
    private String clientId;

    public static AccountInfo instance() {
        synchronized (AccountInfo.class) {
            if (_instance == null) {
                _instance = new AccountInfo();
                _instance.setFrameState(false);
            }
        }
        return _instance;
    }

    protected AccountInfo() {
        orgs = new ArrayList<OrgInfo>();
    }

    public void setFrameState(Boolean state) {
        mMainFrameState = state;
    }

    public Boolean getFrameState() {
        return mMainFrameState;
    }

    public void accountByLogin(LoginInfo info) {
        userEmail = info.getUserEmail();
        userPassword = info.getUserPassword();
        lastOrgInfo = info.getLastOrgInfo();
        lastUserInfo = info.getLastUserInfo();
        loadOrgs();
        if(lastOrgInfo == null || lastUserInfo == null){
        	return;
        }
        DataCenter.intance().setUser(this.lastOrgInfo.getCode(), this.lastUserInfo.getCode());
        CacheCtrlSrv.Update();
    }
    
    public void changeLastUserInfo(UserInfo info){
    	lastUserInfo = info;
    }

    private void loadOrgs() {
        orgs.clear();

        SqlCmd cmd = new SqlCmd(OrgInfoSql.tb_name_org_info);
        cmd.select("*");
        ArrayList<ContentValues> rows = ConfigDatabase.instance().queryCmd(cmd);
        for (ContentValues row : rows) {
            orgs.add(OrgInfoSql.fromRow(row));
        }
    }

   

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public OrgInfo getLastOrgInfo() {
        if (lastOrgInfo == null && LoginInfo.lastLogin() != null) {
            _instance.accountByLogin(LoginInfo.lastLogin());
        }
        return lastOrgInfo;
    }

    public UserInfo getLastUserInfo() {
    	LoginInfo info = LoginInfo.lastLogin();
        if (lastUserInfo == null && info != null) {
            _instance.accountByLogin(info);
        }
        return lastUserInfo;
    }

    public ArrayList<OrgInfo> getOrgs() {
        return orgs;
    }

    public String getClientId() {
        if(clientId!=null && !"".equals(clientId.trim())){
            return clientId;
        }
        try {//尝试从文件读取
            File cli = new File(LocalPath.intance().cacheBasePath + "cliId");
            FileInputStream fin = new FileInputStream(cli);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            String res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
            if (res != null && !"".equals(res.trim())) {
                this.clientId = res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientId;
    }
    
    public String getClientName(){
    	return "phone";
    }

    public void setClientId(String clientId) {
        try {//写入文件，防止对象被销毁后丢失
            File cli = new File(LocalPath.intance().cacheBasePath + "cliId");
            FileOutputStream fout = new FileOutputStream(cli);
            byte[] bytes = clientId.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.clientId = clientId;
    }
}
