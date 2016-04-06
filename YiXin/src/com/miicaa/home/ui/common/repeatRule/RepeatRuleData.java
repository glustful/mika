package com.miicaa.home.ui.common.repeatRule;

import java.util.HashMap;

/**
 * Created by apple on 13-12-6.
 */
public class RepeatRuleData {
    private String repeatKey = "";
    private HashMap<String ,HashMap<String,Object>> data;
    public void setRepeatKey(String repeatKey) {
        this.repeatKey = repeatKey;
        if (this.repeatKey.equals("no")){

        }
    }

    public String getRepeatKey() {
        return repeatKey;
    }

    public void setData(HashMap<String, HashMap<String, Object>> data) {
        this.data = data;
    }

    public HashMap<String, HashMap<String, Object>> getData() {
        return data;
    }
}
