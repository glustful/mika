package com.miicaa.home.ui.common.accessory;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人附件列表数据
 * Created by Ronnie on 2014/7/17.
 */
public class PersonAccessory {

    private String userCode;
    private String userName;
    Map<String,SingleDataAccessory> accessory;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, SingleDataAccessory> getAccessory() {
        if(accessory==null){
            accessory = new HashMap<String, SingleDataAccessory>();
        }
        return accessory;
    }

    public void setAccessory(Map<String, SingleDataAccessory> accessory) {
        this.accessory = accessory;
    }
}
