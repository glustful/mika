package com.miicaa.home.ui.common.accessory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.miicaa.home.data.business.matter.AccessoryInfo;

/**
 * Created by Ronnie on 2014/7/17.
 */
public class SingleDataAccessory {
    List<AccessoryInfo> fileArrayList = new ArrayList<AccessoryInfo>();
    List<AccessoryInfo> imageArrayList = new ArrayList<AccessoryInfo>();
    private Date uploadTime;

    public List<AccessoryInfo> getFileArrayList() {
        return fileArrayList;
    }

    public void setFileArrayList(List<AccessoryInfo> fileArrayList) {
        this.fileArrayList = fileArrayList;
    }

    public List<AccessoryInfo> getImageArrayList() {
        return imageArrayList;
    }

    public void setImageArrayList(List<AccessoryInfo> imageArrayList) {
        this.imageArrayList = imageArrayList;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
