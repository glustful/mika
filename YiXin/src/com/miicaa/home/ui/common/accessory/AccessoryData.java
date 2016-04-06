package com.miicaa.home.ui.common.accessory;

import java.util.HashMap;
import java.util.Map;

/**
 * 附件数据结构
 * Created by Ronnie on 2014/7/17.
 */
public class AccessoryData {
    private Map<String,PersonAccessory> data;

    public Map<String, PersonAccessory> getData() {
        if(data == null){
            data = new HashMap<String, PersonAccessory>();
        }
        return data;
    }

    public void setData(Map<String, PersonAccessory> data) {
        this.data = data;
    }
}
