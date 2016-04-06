//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.base.share.round;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import com.miicaa.home.ui.contactList.SamUser;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SelectRoundActivity_
    extends SelectRoundActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String M_USERS_EXTRA = "mUsers";
    public final static String M_KINDS_EXTRA = "mKinds";
    public final static String M_PARAM_EXTRA = "mParam";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.select_round);
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

    public static SelectRoundActivity_.IntentBuilder_ intent(Context context) {
        return new SelectRoundActivity_.IntentBuilder_(context);
    }

    public static SelectRoundActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new SelectRoundActivity_.IntentBuilder_(fragment);
    }

    public static SelectRoundActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new SelectRoundActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        mRootView = ((LinearLayout) hasViews.findViewById(id.rootView));
        title = ((TextView) hasViews.findViewById(id.headTitle));
        cancel = ((Button) hasViews.findViewById(id.cancleButton));
        commit = ((Button) hasViews.findViewById(id.commitButton));
        {
            View view = hasViews.findViewById(id.cancleButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SelectRoundActivity_.this.cancel();
                    }

                }
                );
            }
        }
        initUi();
    }

    @SuppressWarnings("unchecked")
    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(M_USERS_EXTRA)) {
                mUsers = ((ArrayList<SamUser> ) extras_.getSerializable(M_USERS_EXTRA));
            }
            if (extras_.containsKey(M_KINDS_EXTRA)) {
                mKinds = ((ArrayList<RoundKinds> ) extras_.getSerializable(M_KINDS_EXTRA));
            }
            if (extras_.containsKey(M_PARAM_EXTRA)) {
                mParam = ((HashMap<String, String> ) extras_.getSerializable(M_PARAM_EXTRA));
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
            intent_ = new Intent(context, SelectRoundActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, SelectRoundActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, SelectRoundActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public SelectRoundActivity_.IntentBuilder_ flags(int flags) {
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

        public SelectRoundActivity_.IntentBuilder_ mUsers(ArrayList<SamUser> mUsers) {
            intent_.putExtra(M_USERS_EXTRA, ((Serializable) mUsers));
            return this;
        }

        public SelectRoundActivity_.IntentBuilder_ mKinds(ArrayList<RoundKinds> mKinds) {
            intent_.putExtra(M_KINDS_EXTRA, ((Serializable) mKinds));
            return this;
        }

        public SelectRoundActivity_.IntentBuilder_ mParam(HashMap<String, String> mParam) {
            intent_.putExtra(M_PARAM_EXTRA, ((Serializable) mParam));
            return this;
        }

    }

}