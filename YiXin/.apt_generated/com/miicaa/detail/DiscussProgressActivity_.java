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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class DiscussProgressActivity_
    extends DiscussProgressActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String ARRANGE_TYPE_EXTRA = "arrangeType";
    public final static String TYPE_EXTRA = "type";
    public final static String DATAID_EXTRA = "dataid";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.discuss_progress);
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

    public static DiscussProgressActivity_.IntentBuilder_ intent(Context context) {
        return new DiscussProgressActivity_.IntentBuilder_(context);
    }

    public static DiscussProgressActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new DiscussProgressActivity_.IntentBuilder_(fragment);
    }

    public static DiscussProgressActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new DiscussProgressActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        alReady = ((TextView) hasViews.findViewById(id.already));
        commit = ((Button) hasViews.findViewById(id.writeCommit));
        cancle = ((Button) hasViews.findViewById(id.writeCancle));
        uploadWidget = ((LinearLayout) hasViews.findViewById(id.upload_widget));
        editText = ((EditText) hasViews.findViewById(id.progressEdit));
        checkbox = ((CheckBox) hasViews.findViewById(id.checkbox));
        title = ((TextView) hasViews.findViewById(id.writeTitle));
        totalCount = ((TextView) hasViews.findViewById(id.totalcount));
        {
            View view = hasViews.findViewById(id.writeCommit);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        DiscussProgressActivity_.this.commitClick();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.writeCancle);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        DiscussProgressActivity_.this.cancle();
                    }

                }
                );
            }
        }
        {
            CompoundButton view = ((CompoundButton) hasViews.findViewById(id.checkbox));
            if (view!= null) {
                view.setOnCheckedChangeListener(new OnCheckedChangeListener() {


                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DiscussProgressActivity_.this.change(buttonView, isChecked);
                    }

                }
                );
            }
        }
        {
            final TextView view = ((TextView) hasViews.findViewById(id.progressEdit));
            if (view!= null) {
                view.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        DiscussProgressActivity_.this.onTextChangesOnHelloTextView(s, view, before, start, count);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                }
                );
            }
        }
        afterViews();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(ARRANGE_TYPE_EXTRA)) {
                arrangeType = extras_.getString(ARRANGE_TYPE_EXTRA);
            }
            if (extras_.containsKey(TYPE_EXTRA)) {
                type = extras_.getString(TYPE_EXTRA);
            }
            if (extras_.containsKey(DATAID_EXTRA)) {
                dataid = extras_.getString(DATAID_EXTRA);
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
                DiscussProgressActivity_.this.onResultlc(resultCode, data);
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
            intent_ = new Intent(context, DiscussProgressActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, DiscussProgressActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, DiscussProgressActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public DiscussProgressActivity_.IntentBuilder_ flags(int flags) {
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

        public DiscussProgressActivity_.IntentBuilder_ arrangeType(String arrangeType) {
            intent_.putExtra(ARRANGE_TYPE_EXTRA, arrangeType);
            return this;
        }

        public DiscussProgressActivity_.IntentBuilder_ type(String type) {
            intent_.putExtra(TYPE_EXTRA, type);
            return this;
        }

        public DiscussProgressActivity_.IntentBuilder_ dataid(String dataid) {
            intent_.putExtra(DATAID_EXTRA, dataid);
            return this;
        }

    }

}
