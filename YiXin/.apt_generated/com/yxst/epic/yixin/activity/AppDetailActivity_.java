//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.yxst.epic.yixin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class AppDetailActivity_
    extends AppDetailActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String NICK_NAME_EXTRA = "nickName";
    public final static String DISPLAY_NAME_EXTRA = "displayName";
    public final static String USER_NAME_EXTRA = "userName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_app_detail);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectExtras_();
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

    public static AppDetailActivity_.IntentBuilder_ intent(Context context) {
        return new AppDetailActivity_.IntentBuilder_(context);
    }

    public static AppDetailActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new AppDetailActivity_.IntentBuilder_(fragment);
    }

    public static AppDetailActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new AppDetailActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        tvNickName = ((TextView) hasViews.findViewById(id.tvNickName));
        tvDisplayName = ((TextView) hasViews.findViewById(id.tvDisplayName));
        afterViews();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(NICK_NAME_EXTRA)) {
                nickName = extras_.getString(NICK_NAME_EXTRA);
            }
            if (extras_.containsKey(DISPLAY_NAME_EXTRA)) {
                displayName = extras_.getString(DISPLAY_NAME_EXTRA);
            }
            if (extras_.containsKey(USER_NAME_EXTRA)) {
                userName = extras_.getString(USER_NAME_EXTRA);
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, AppDetailActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, AppDetailActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, AppDetailActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public AppDetailActivity_.IntentBuilder_ flags(int flags) {
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

        public AppDetailActivity_.IntentBuilder_ nickName(String nickName) {
            intent_.putExtra(NICK_NAME_EXTRA, nickName);
            return this;
        }

        public AppDetailActivity_.IntentBuilder_ displayName(String displayName) {
            intent_.putExtra(DISPLAY_NAME_EXTRA, displayName);
            return this;
        }

        public AppDetailActivity_.IntentBuilder_ userName(String userName) {
            intent_.putExtra(USER_NAME_EXTRA, userName);
            return this;
        }

    }

}
