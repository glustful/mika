//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.yxst.epic.yixin.adapter;

import android.content.Context;

public final class ChatDetailAdapter_
    extends ChatDetailAdapter
{

    private Context context_;

    private ChatDetailAdapter_(Context context) {
        context_ = context;
        init_();
    }

    public static ChatDetailAdapter_ getInstance_(Context context) {
        return new ChatDetailAdapter_(context);
    }

    private void init_() {
        context = context_;
    }

    public void rebind(Context context) {
        context_ = context;
        init_();
    }

}
