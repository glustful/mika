package com.miicaa.detail;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.miicaa.home.ui.person.PersonHome_;

public class AtClickSpan extends ClickableSpan{
    String userCode;
    Context context;
    public AtClickSpan(String userCode,final Context context){
        this.userCode = userCode;
        this.context = context;
    }
    @Override
    public void onClick(View view) {
//        Intent intent = new Intent(getActivity(), PersonHome.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userCode",userCode);
//        intent.putExtra("bundle",bundle);
//        ((Activity)getActivity()).startActivity(intent);
    	if(userCode != null && userCode.length()>0)
    	PersonHome_.intent(context).mUserCode(userCode).start();
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        //设置没有下划线
        ds.setUnderlineText(false);
       
       //设置颜色高亮
        ds.setARGB(255, 0, 71, 112);
    }


}