//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.ui.enterprise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class EnterpriceMainActivity_
    extends EnterpriceMainActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String URL_EXTRA = "url";
    public final static String TITLE_EXTRA = "title";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.enterpaice_main_view);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectExtras_();
        initData();
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

    public static EnterpriceMainActivity_.IntentBuilder_ intent(Context context) {
        return new EnterpriceMainActivity_.IntentBuilder_(context);
    }

    public static EnterpriceMainActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new EnterpriceMainActivity_.IntentBuilder_(fragment);
    }

    public static EnterpriceMainActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new EnterpriceMainActivity_.IntentBuilder_(supportFragment);
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
        mWebView = ((WebView) hasViews.findViewById(id.webview));
        titleButton = ((TextView) hasViews.findViewById(id.pay_headTitle));
        rightButton = ((Button) hasViews.findViewById(id.pay_commitButton));
        leftButton = ((Button) hasViews.findViewById(id.pay_cancleButton));
        {
            View view = hasViews.findViewById(id.pay_cancleButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        EnterpriceMainActivity_.this.cancel();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_commitButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        EnterpriceMainActivity_.this.commit();
                    }

                }
                );
            }
        }
        initUI();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(URL_EXTRA)) {
                url = extras_.getString(URL_EXTRA);
            }
            if (extras_.containsKey(TITLE_EXTRA)) {
                title = extras_.getString(TITLE_EXTRA);
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
            intent_ = new Intent(context, EnterpriceMainActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, EnterpriceMainActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, EnterpriceMainActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public EnterpriceMainActivity_.IntentBuilder_ flags(int flags) {
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

        public EnterpriceMainActivity_.IntentBuilder_ url(String url) {
            intent_.putExtra(URL_EXTRA, url);
            return this;
        }

        public EnterpriceMainActivity_.IntentBuilder_ title(String title) {
            intent_.putExtra(TITLE_EXTRA, title);
            return this;
        }

    }

}
