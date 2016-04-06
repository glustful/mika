//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.ui.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class MyFundActivity_
    extends MyFundActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String M_TOTAL_MONEY_EXTRA = "mTotalMoney";
    public final static String M_ACCOUNT_NAME_EXTRA = "mAccountName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.layout_pay_myfund_activity);
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

    public static MyFundActivity_.IntentBuilder_ intent(Context context) {
        return new MyFundActivity_.IntentBuilder_(context);
    }

    public static MyFundActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new MyFundActivity_.IntentBuilder_(fragment);
    }

    public static MyFundActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new MyFundActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        headTitle = ((TextView) hasViews.findViewById(id.pay_headTitle));
        accountMoney = ((TextView) hasViews.findViewById(id.pay_main_money));
        accountName = ((TextView) hasViews.findViewById(id.pay_main_account));
        listView = ((PullToRefreshListView) hasViews.findViewById(id.pay_myfund_list_view));
        back = ((Button) hasViews.findViewById(id.pay_cancleButton));
        {
            View view = hasViews.findViewById(id.pay_cancleButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MyFundActivity_.this.cancel();
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
            if (extras_.containsKey(M_TOTAL_MONEY_EXTRA)) {
                mTotalMoney = extras_.getDouble(M_TOTAL_MONEY_EXTRA);
            }
            if (extras_.containsKey(M_ACCOUNT_NAME_EXTRA)) {
                mAccountName = extras_.getString(M_ACCOUNT_NAME_EXTRA);
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
            intent_ = new Intent(context, MyFundActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, MyFundActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, MyFundActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public MyFundActivity_.IntentBuilder_ flags(int flags) {
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

        public MyFundActivity_.IntentBuilder_ mTotalMoney(double mTotalMoney) {
            intent_.putExtra(M_TOTAL_MONEY_EXTRA, mTotalMoney);
            return this;
        }

        public MyFundActivity_.IntentBuilder_ mAccountName(String mAccountName) {
            intent_.putExtra(M_ACCOUNT_NAME_EXTRA, mAccountName);
            return this;
        }

    }

}