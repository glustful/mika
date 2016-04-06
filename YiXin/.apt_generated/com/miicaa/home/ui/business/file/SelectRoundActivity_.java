//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.ui.business.file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SelectRoundActivity_
    extends SelectRoundActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String RIGHT_TYPE_EXTRA = "rightType";
    public final static String NAME_EXTRA = "name";
    public final static String JSON_EXTRA = "json";
    public final static String PRIVATE_TITLE_EXTRA = "privateTitle";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.business_file_select_round);
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
        round_private = ((RadioButton) hasViews.findViewById(id.round_private));
        round_unit_text = ((TextView) hasViews.findViewById(id.round_unit_text));
        cancel = ((Button) hasViews.findViewById(id.cancleButton));
        round_peopel_text = ((TextView) hasViews.findViewById(id.round_peopel_text));
        round_group = ((RadioButton) hasViews.findViewById(id.round_group));
        round_group_text = ((TextView) hasViews.findViewById(id.round_group_text));
        round_unit = ((RadioButton) hasViews.findViewById(id.round_unit));
        title = ((TextView) hasViews.findViewById(id.headTitle));
        commit = ((Button) hasViews.findViewById(id.commitButton));
        round_public = ((RadioButton) hasViews.findViewById(id.round_public));
        round_peopel = ((RadioButton) hasViews.findViewById(id.round_peopel));
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
        {
            View view = hasViews.findViewById(id.round_private);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SelectRoundActivity_.this.roundPrivate();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.round_peopel_layout);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SelectRoundActivity_.this.roundPeople();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.round_group_layout);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SelectRoundActivity_.this.roundGroup();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.round_public);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SelectRoundActivity_.this.roundPublic();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.round_unit_layout);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SelectRoundActivity_.this.roundUnit();
                    }

                }
                );
            }
        }
        initUi();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(RIGHT_TYPE_EXTRA)) {
                rightType = extras_.getString(RIGHT_TYPE_EXTRA);
            }
            if (extras_.containsKey(NAME_EXTRA)) {
                name = extras_.getString(NAME_EXTRA);
            }
            if (extras_.containsKey(JSON_EXTRA)) {
                json = extras_.getString(JSON_EXTRA);
            }
            if (extras_.containsKey(PRIVATE_TITLE_EXTRA)) {
                privateTitle = extras_.getString(PRIVATE_TITLE_EXTRA);
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

        public SelectRoundActivity_.IntentBuilder_ rightType(String rightType) {
            intent_.putExtra(RIGHT_TYPE_EXTRA, rightType);
            return this;
        }

        public SelectRoundActivity_.IntentBuilder_ name(String name) {
            intent_.putExtra(NAME_EXTRA, name);
            return this;
        }

        public SelectRoundActivity_.IntentBuilder_ json(String json) {
            intent_.putExtra(JSON_EXTRA, json);
            return this;
        }

        public SelectRoundActivity_.IntentBuilder_ privateTitle(String privateTitle) {
            intent_.putExtra(PRIVATE_TITLE_EXTRA, privateTitle);
            return this;
        }

    }

}
