//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ContinueProgressActivity_
    extends ContinueProgressActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String DATA_ID_EXTRA = "dataId";
    public final static String USER_NAME_EXTRA = "userName";
    public final static String USER_CODE_EXTRA = "userCode";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.continue_progress_activity);
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

    public static ContinueProgressActivity_.IntentBuilder_ intent(Context context) {
        return new ContinueProgressActivity_.IntentBuilder_(context);
    }

    public static ContinueProgressActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new ContinueProgressActivity_.IntentBuilder_(fragment);
    }

    public static ContinueProgressActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new ContinueProgressActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        contentEdit = ((EditText) hasViews.findViewById(id.cEdit));
        head = ((RelativeLayout) hasViews.findViewById(id.headView));
        afterViews();
        afterView();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(DATA_ID_EXTRA)) {
                dataId = extras_.getString(DATA_ID_EXTRA);
            }
            if (extras_.containsKey(USER_NAME_EXTRA)) {
                userName = extras_.getString(USER_NAME_EXTRA);
            }
            if (extras_.containsKey(USER_CODE_EXTRA)) {
                userCode = extras_.getString(USER_CODE_EXTRA);
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
            intent_ = new Intent(context, ContinueProgressActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, ContinueProgressActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, ContinueProgressActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public ContinueProgressActivity_.IntentBuilder_ flags(int flags) {
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

        public ContinueProgressActivity_.IntentBuilder_ dataId(String dataId) {
            intent_.putExtra(DATA_ID_EXTRA, dataId);
            return this;
        }

        public ContinueProgressActivity_.IntentBuilder_ userName(String userName) {
            intent_.putExtra(USER_NAME_EXTRA, userName);
            return this;
        }

        public ContinueProgressActivity_.IntentBuilder_ userCode(String userCode) {
            intent_.putExtra(USER_CODE_EXTRA, userCode);
            return this;
        }

    }

}
