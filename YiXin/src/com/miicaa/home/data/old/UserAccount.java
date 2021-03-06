package com.miicaa.home.data.old;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;

/**
 * Created by Administrator on 13-11-22.
 * 
 */
public class UserAccount {
    //服务器地址
//	private static String yixinServerHost = "pushcomet.miicaa.com";
	
	//public static String mSeverHost = "https://www.miicaa.com"; 
//    public static String mServerIp = "115.29.226.14";
	public static String mSeverHost = "http://192.168.1.16";
	//public static String mSeverHost = "http://192.168.1.23";
	//public static String mSeverHost = "http://192.168.0.9:8080";
    private static String mLocalDir = Environment.getExternalStorageDirectory().getPath() + "/miicaa/";

    private UserInfo mCurrentUser;
    private ArrayList<OrgInfo> mOrgOthers;   
    public String mEmail;

    private static UserAccount mInstance;

    public static UserAccount getInstance() {
        if (mInstance == null) {
            synchronized (UserAccount.class) {
                if (mInstance == null) {
                    mInstance = new UserAccount();
                }
            }

        }
        return mInstance;
    }

    public static String getSeverHost() {
        return mSeverHost;
    }

    public static String getLocalDir(String subDir) {
        String dirPath = mLocalDir + subDir;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }
    

    private UserAccount() {
        mCurrentUser = null;
        mOrgOthers = new ArrayList<OrgInfo>();
    }

    public int changeCurrentAccount(String account) {
        mOrgOthers.clear();
        mCurrentUser = null;
        return readCurrentAccount(account);
    }

    private int readCurrentAccount(String account) {
        int res = -1;
        if (account == null || account.length() < 0) {
            return res;
        }

        try {
            JSONObject jRoot = new JSONObject(account);
            Boolean success = jRoot.optBoolean("succeed");
            if (success) {
                JSONObject jUser = jRoot.optJSONObject("data");
                String name = jUser.optString("userName");
                String code = jUser.optString("userCode");
                mCurrentUser = new UserInfo(name, code);
                JSONObject jCurrOrg = jUser.optJSONObject("currentOrg");
                String orgCode = "";
                if (jCurrOrg != null) {
                    orgCode = jCurrOrg.optString("code");
                    OrgInfo orgInfo = new OrgInfo();
                    orgInfo.mOrgCode = jCurrOrg.optString("code");
                    orgInfo.mOrgName = jCurrOrg.optString("name");
                    mCurrentUser.setOrg(orgInfo);
                }
                JSONArray jOrgArray = jUser.optJSONArray("orgs");
                if (jOrgArray != null) {
                    for (int i = 0; i < jOrgArray.length(); i++) {
                        JSONObject jOrg = jOrgArray.optJSONObject(i);
                        if (jOrg == null) {
                            continue;
                        }
                        OrgInfo orgInfo = new OrgInfo();
                        orgInfo.mOrgCode = jOrg.optString("code");
                        orgInfo.mOrgName = jOrg.optString("name");
                        mOrgOthers.add(orgInfo);
                    }
                }
                res = 1;
            } else {
                res = 0;
            }

        } catch (JSONException e) {
            res = -1;
        }

        return res;
    }

    public UserInfo getCurrentUser() {
        return mCurrentUser;
    }

    public int orgSize() {
        return mOrgOthers.size();
    }

    public OrgInfo getOrg(int i) {
        return mOrgOthers.get(i);
    }

    protected ArrayList<OrgInfo> getOrgOther() {
        return mOrgOthers;
    }


}
