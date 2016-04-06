//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.ui.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import com.yxst.epic.yixin.view.TextViewPartColor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import org.json.JSONObject;

public final class PayMainActivity_
    extends PayMainActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.layout_pay_main_activity);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
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

    public static PayMainActivity_.IntentBuilder_ intent(Context context) {
        return new PayMainActivity_.IntentBuilder_(context);
    }

    public static PayMainActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new PayMainActivity_.IntentBuilder_(fragment);
    }

    public static PayMainActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new PayMainActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        addCount = ((TextViewPartColor) hasViews.findViewById(id.pay_main_people_addcount));
        useState = ((TextView) hasViews.findViewById(id.pay_main_state));
        back = ((Button) hasViews.findViewById(id.pay_cancleButton));
        kind = ((TextView) hasViews.findViewById(id.pay_main_kind));
        useable = ((TextView) hasViews.findViewById(id.pay_main_store_useable));
        peopleCount = ((TextView) hasViews.findViewById(id.pay_main_people_count));
        line = ((View) hasViews.findViewById(id.line));
        headTitle = ((TextView) hasViews.findViewById(id.pay_headTitle));
        btPlus = ((Button) hasViews.findViewById(id.pay_plus_buy));
        accountName = ((TextView) hasViews.findViewById(id.pay_main_account));
        timeout = ((TextView) hasViews.findViewById(id.pay_main_timeout));
        btBuy = ((Button) hasViews.findViewById(id.pay_buy));
        otherBought = ((Button) hasViews.findViewById(id.pay_other_bought));
        btContinue = ((Button) hasViews.findViewById(id.pay_continue_buy));
        used = ((TextView) hasViews.findViewById(id.pay_main_store_used));
        accountMoney = ((TextView) hasViews.findViewById(id.pay_main_money));
        footer = ((LinearLayout) hasViews.findViewById(id.pay_footer));
        lineRight = ((LinearLayout) hasViews.findViewById(id.line_right));
        {
            View view = hasViews.findViewById(id.pay_main_kind);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.currentKind();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_plus_buy);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.plusBuy();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_bill);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.bill();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_buy);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.buy();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_account);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.account();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_receipt);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.receipt();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_other_bought);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.otherBought();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_myfound);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.myFound();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_continue_buy);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.continueBuy();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.pay_cancleButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        PayMainActivity_.this.cancel();
                    }

                }
                );
            }
        }
        initUI();
    }

    @Override
    public void callBackInitData(final JSONObject rootObj) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                PayMainActivity_.super.callBackInitData(rootObj);
            }

        }
        );
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, PayMainActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, PayMainActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, PayMainActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public PayMainActivity_.IntentBuilder_ flags(int flags) {
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

    }

}
