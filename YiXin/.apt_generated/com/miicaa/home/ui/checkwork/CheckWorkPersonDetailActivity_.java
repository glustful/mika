//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.ui.checkwork;

import java.io.Serializable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class CheckWorkPersonDetailActivity_
    extends CheckWorkPersonDetailActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String LEFT_TITLE_TEXT_EXTRA = "leftTitleText";
    public final static String DETAIL_CONTENT_EXTRA = "detailContent";
    public final static String MY_USER_CODE_EXTRA = "myUserCode";
    public final static String NOW_DATE_EXTRA = "nowDate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_checkwork_detail_list);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectExtras_();
        afterInject();
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

    public static CheckWorkPersonDetailActivity_.IntentBuilder_ intent(Context context) {
        return new CheckWorkPersonDetailActivity_.IntentBuilder_(context);
    }

    public static CheckWorkPersonDetailActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new CheckWorkPersonDetailActivity_.IntentBuilder_(fragment);
    }

    public static CheckWorkPersonDetailActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new CheckWorkPersonDetailActivity_.IntentBuilder_(supportFragment);
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
        listView = ((ListView) hasViews.findViewById(id.listView));
        afterView();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(LEFT_TITLE_TEXT_EXTRA)) {
                leftTitleText = extras_.getString(LEFT_TITLE_TEXT_EXTRA);
            }
            if (extras_.containsKey(DETAIL_CONTENT_EXTRA)) {
                detailContent = ((CheckWorkDetailContent) extras_.getSerializable(DETAIL_CONTENT_EXTRA));
            }
            if (extras_.containsKey(MY_USER_CODE_EXTRA)) {
                myUserCode = extras_.getString(MY_USER_CODE_EXTRA);
            }
            if (extras_.containsKey(NOW_DATE_EXTRA)) {
                nowDate = ((Long) extras_.getSerializable(NOW_DATE_EXTRA));
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
            case  2 :
                CheckWorkPersonDetailActivity_.this.signOutResult(resultCode, data);
                break;
            case  1 :
                CheckWorkPersonDetailActivity_.this.signInResult(resultCode, data);
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
            intent_ = new Intent(context, CheckWorkPersonDetailActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, CheckWorkPersonDetailActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, CheckWorkPersonDetailActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public CheckWorkPersonDetailActivity_.IntentBuilder_ flags(int flags) {
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

        public CheckWorkPersonDetailActivity_.IntentBuilder_ leftTitleText(String leftTitleText) {
            intent_.putExtra(LEFT_TITLE_TEXT_EXTRA, leftTitleText);
            return this;
        }

        public CheckWorkPersonDetailActivity_.IntentBuilder_ detailContent(CheckWorkDetailContent detailContent) {
            intent_.putExtra(DETAIL_CONTENT_EXTRA, ((Serializable) detailContent));
            return this;
        }

        public CheckWorkPersonDetailActivity_.IntentBuilder_ myUserCode(String myUserCode) {
            intent_.putExtra(MY_USER_CODE_EXTRA, myUserCode);
            return this;
        }

        public CheckWorkPersonDetailActivity_.IntentBuilder_ nowDate(Long nowDate) {
            intent_.putExtra(NOW_DATE_EXTRA, ((Serializable) nowDate));
            return this;
        }

    }

}