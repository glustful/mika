//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class StopApproveActivity_
    extends StopApproveActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String DATA_ID_EXTRA = "dataId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_checkwork_beizhu);
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

    public static StopApproveActivity_.IntentBuilder_ intent(Context context) {
        return new StopApproveActivity_.IntentBuilder_(context);
    }

    public static StopApproveActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new StopApproveActivity_.IntentBuilder_(fragment);
    }

    public static StopApproveActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new StopApproveActivity_.IntentBuilder_(supportFragment);
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
        editText = ((EditText) hasViews.findViewById(id.editText));
        zishuTextView = ((TextView) hasViews.findViewById(id.zishuTextView));
        {
            final TextView view = ((TextView) hasViews.findViewById(id.editText));
            if (view!= null) {
                view.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        StopApproveActivity_.this.onTextChangesOnEditText(s, view, before, start, count);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                }
                );
            }
        }
        afterView();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(DATA_ID_EXTRA)) {
                dataId = extras_.getString(DATA_ID_EXTRA);
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
            intent_ = new Intent(context, StopApproveActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, StopApproveActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, StopApproveActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public StopApproveActivity_.IntentBuilder_ flags(int flags) {
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

        public StopApproveActivity_.IntentBuilder_ dataId(String dataId) {
            intent_.putExtra(DATA_ID_EXTRA, dataId);
            return this;
        }

    }

}
