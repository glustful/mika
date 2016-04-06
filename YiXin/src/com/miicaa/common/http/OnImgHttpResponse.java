package com.miicaa.common.http;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Administrator on 13-10-25.
 */
public interface OnImgHttpResponse
{
    public void onResponse(ImageView view, Bitmap bp, String tag);
    public  void onUpload(ImgHttpMessage msg);
}
