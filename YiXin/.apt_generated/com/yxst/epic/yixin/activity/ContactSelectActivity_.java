//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.yxst.epic.yixin.activity;

import java.io.Serializable;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.miicaa.home.R.layout;
import com.yxst.epic.yixin.adapter.ContactSelectAdapter_;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.response.CreateQunResponse;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import org.springframework.web.client.RestClientException;

public final class ContactSelectActivity_
    extends ContactSelectActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String LOCK_MEMBERS_EXTRA = "lockMembers";
    public final static String LOCAL_USER_NAME_EXTRA = "localUserName";
    public final static String IS_PICK_MODE_EXTRA = "isPickMode";
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_contact_select);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectExtras_();
        mLayoutInflater = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        mContactSelectAdapter = ContactSelectAdapter_.getInstance_(this);
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

    public static ContactSelectActivity_.IntentBuilder_ intent(Context context) {
        return new ContactSelectActivity_.IntentBuilder_(context);
    }

    public static ContactSelectActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new ContactSelectActivity_.IntentBuilder_(fragment);
    }

    public static ContactSelectActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new ContactSelectActivity_.IntentBuilder_(supportFragment);
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
        mHorizontalListView = ((HorizontalListView) hasViews.findViewById(android.R.id.list));
        tvTip = ((TextView) hasViews.findViewById(com.miicaa.home.R.id.tvTip));
        mContentLayout = ((FrameLayout) hasViews.findViewById(com.miicaa.home.R.id.layout_content));
        {
            AdapterView<?> view = ((AdapterView<?> ) hasViews.findViewById(android.R.id.list));
            if (view!= null) {
                view.setOnItemClickListener(new OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ContactSelectActivity_.this.onItemClickListener(((Member) parent.getAdapter().getItem(position)));
                    }

                }
                );
            }
        }
        afterViews();
    }

    @SuppressWarnings("unchecked")
    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(LOCK_MEMBERS_EXTRA)) {
                lockMembers = ((List<Member> ) extras_.getSerializable(LOCK_MEMBERS_EXTRA));
            }
            if (extras_.containsKey(LOCAL_USER_NAME_EXTRA)) {
                localUserName = extras_.getString(LOCAL_USER_NAME_EXTRA);
            }
            if (extras_.containsKey(IS_PICK_MODE_EXTRA)) {
                isPickMode = extras_.getBoolean(IS_PICK_MODE_EXTRA);
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    @Override
    public void onPostExecute(final CreateQunResponse response) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ContactSelectActivity_.super.onPostExecute(response);
            }

        }
        );
    }

    @Override
    public void onRestClientExceptionThrownUI(final RestClientException e) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ContactSelectActivity_.super.onRestClientExceptionThrownUI(e);
            }

        }
        );
    }

    @Override
    public void onPreExecute() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ContactSelectActivity_.super.onPreExecute();
            }

        }
        );
    }

    @Override
    public void doInBackground(final List<Member> list) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ContactSelectActivity_.super.doInBackground(list);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
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
            intent_ = new Intent(context, ContactSelectActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, ContactSelectActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, ContactSelectActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public ContactSelectActivity_.IntentBuilder_ flags(int flags) {
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

        public ContactSelectActivity_.IntentBuilder_ lockMembers(List<Member> lockMembers) {
            intent_.putExtra(LOCK_MEMBERS_EXTRA, ((Serializable) lockMembers));
            return this;
        }

        public ContactSelectActivity_.IntentBuilder_ localUserName(String localUserName) {
            intent_.putExtra(LOCAL_USER_NAME_EXTRA, localUserName);
            return this;
        }

        public ContactSelectActivity_.IntentBuilder_ isPickMode(boolean isPickMode) {
            intent_.putExtra(IS_PICK_MODE_EXTRA, isPickMode);
            return this;
        }

    }

}
