package com.miicaa.home.ui.org;

import android.view.View;

/**
 * Created by LM on 14-7-15.
 */
public  abstract class LabelData {
    private String labelStr;
    private String labelId;
    private View  labelGrView;

    public void setLabelStr(String labelStr){
        this.labelStr = labelStr;
    }
    public void setLabelId(String labelId){
        this.labelId = labelId;

    }

    public void setLabelGrView(View v){
        labelGrView = v;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delDataClick();
            }
        });
    }

    public String getLabelStr(){
        return labelStr;
    }

    public View getLabelGrView(){
        return labelGrView;
    }

    public String getLabelId(){
        return labelId;
    }


        public abstract void delDataClick();

}
