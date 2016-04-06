package com.miicaa.home.ui.contactList;

import java.io.Serializable;

/**
 * Created by LM on 14-9-10.
 * 保存任务，审批，抄送人的name 和 code
 */
public class SamUser implements Serializable{

        String mCode;
        String mName;

        public SamUser(String code, String name) {
            mCode = code;
            mName = name;
        }

    public String getmCode(){
        return mCode;
    }
    public String getmName(){
        return mName;
    }

}
