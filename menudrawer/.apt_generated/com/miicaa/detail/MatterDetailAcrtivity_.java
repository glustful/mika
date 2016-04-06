//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.detail;

import java.io.Serializable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class MatterDetailAcrtivity_
    extends MatterDetailAcrtivity
    implements HasViews
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String IS_ALAREADY_EXTRA = "isAlaready";
    public final static String DATA_ID_EXTRA = "dataId";
    public final static String OPERATE_GROUP_EXTRA = "operateGroup";
    public final static String STATUS_EXTRA = "status";
    public final static String DATA_TYPE_EXTRA = "dataType";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    private void init_(Bundle savedInstanceState) {
        injectExtras_();
        afterinject();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static MatterDetailAcrtivity_.IntentBuilder_ intent(Context context) {
        return new MatterDetailAcrtivity_.IntentBuilder_(context);
    }

    public static MatterDetailAcrtivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new MatterDetailAcrtivity_.IntentBuilder_(fragment);
    }

    public static MatterDetailAcrtivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new MatterDetailAcrtivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(IS_ALAREADY_EXTRA)) {
                isAlaready = ((Boolean) extras_.getSerializable(IS_ALAREADY_EXTRA));
            }
            if (extras_.containsKey(DATA_ID_EXTRA)) {
                dataId = extras_.getString(DATA_ID_EXTRA);
            }
            if (extras_.containsKey(OPERATE_GROUP_EXTRA)) {
                operateGroup = extras_.getString(OPERATE_GROUP_EXTRA);
            }
            if (extras_.containsKey(STATUS_EXTRA)) {
                status = extras_.getString(STATUS_EXTRA);
            }
            if (extras_.containsKey(DATA_TYPE_EXTRA)) {
                dataType = extras_.getString(DATA_TYPE_EXTRA);
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case  98 :
                MatterDetailAcrtivity_.this.onResultca(resultCode, data);
                break;
        }
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, MatterDetailAcrtivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, MatterDetailAcrtivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, MatterDetailAcrtivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public MatterDetailAcrtivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent_, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent_, requestCode);
                } else {
                    if (context_ instanceof Activity) {
                        ((Activity) context_).startActivityForResult(intent_, requestCode);
                    } else {
                        context_.startActivity(intent_);
                    }
                }
            }
        }

        public MatterDetailAcrtivity_.IntentBuilder_ isAlaready(Boolean isAlaready) {
            intent_.putExtra(IS_ALAREADY_EXTRA, ((Serializable) isAlaready));
            return this;
        }

        public MatterDetailAcrtivity_.IntentBuilder_ dataId(String dataId) {
            intent_.putExtra(DATA_ID_EXTRA, dataId);
            return this;
        }

        public MatterDetailAcrtivity_.IntentBuilder_ operateGroup(String operateGroup) {
            intent_.putExtra(OPERATE_GROUP_EXTRA, operateGroup);
            return this;
        }

        public MatterDetailAcrtivity_.IntentBuilder_ status(String status) {
            intent_.putExtra(STATUS_EXTRA, status);
            return this;
        }

        public MatterDetailAcrtivity_.IntentBuilder_ dataType(String dataType) {
            intent_.putExtra(DATA_TYPE_EXTRA, dataType);
            return this;
        }

    }

}
