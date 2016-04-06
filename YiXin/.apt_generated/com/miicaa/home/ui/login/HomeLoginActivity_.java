//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import com.yxst.epic.yixin.data.dto.response.LoginResponse;
import com.yxst.epic.yixin.data.rest.IMInterface_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class HomeLoginActivity_
    extends HomeLoginActivity
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
        setContentView(layout.home_login_activity);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        myRestClient = new IMInterface_();
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

    public static HomeLoginActivity_.IntentBuilder_ intent(Context context) {
        return new HomeLoginActivity_.IntentBuilder_(context);
    }

    public static HomeLoginActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new HomeLoginActivity_.IntentBuilder_(fragment);
    }

    public static HomeLoginActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new HomeLoginActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        mPwdText = ((EditText) hasViews.findViewById(id.login_id_pwd));
        delMailButton = ((ImageButton) hasViews.findViewById(id.delMailBtn));
        mErrorText = ((TextView) hasViews.findViewById(id.login_id_err));
        mAutoCheck = ((CheckBox) hasViews.findViewById(id.login_id_auto));
        mEmailText = ((EditText) hasViews.findViewById(id.login_id_email));
        mLoadingLayout = ((RelativeLayout) hasViews.findViewById(id.login_id_loading_layout));
        delPassWordButton = ((ImageButton) hasViews.findViewById(id.delPwdBtn));
        mLoginBtn = ((Button) hasViews.findViewById(id.login_id_login));
        {
            View view = hasViews.findViewById(id.delMailBtn);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        HomeLoginActivity_.this.delMailClick(view);
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.delPwdBtn);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        HomeLoginActivity_.this.delPwdClick(view);
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.loginRegister);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        HomeLoginActivity_.this.registerClick(view);
                    }

                }
                );
            }
        }
        {
            final TextView view = ((TextView) hasViews.findViewById(id.login_id_pwd));
            if (view!= null) {
                view.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        HomeLoginActivity_.this.passwordTextChange(s, view, before, start, count);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                }
                );
            }
        }
        {
            final TextView view = ((TextView) hasViews.findViewById(id.login_id_email));
            if (view!= null) {
                view.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        HomeLoginActivity_.this.mailTextChange(s, view, before, start, count);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                }
                );
            }
        }
        crateData();
    }

    @Override
    public void errorToLogin() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                HomeLoginActivity_.super.errorToLogin();
            }

        }
        );
    }

    @Override
    public void loginComplete(final LoginResponse response) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                HomeLoginActivity_.super.loginComplete(response);
            }

        }
        );
    }

    @Override
    public void requestLogin(final String eMail, final String pwd, final Boolean auto) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                HomeLoginActivity_.super.requestLogin(eMail, pwd, auto);
            }

        }
        );
    }

    @Override
    public void refreshContact() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    HomeLoginActivity_.super.refreshContact();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void loginYouxin(final String name, final String pswd) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    HomeLoginActivity_.super.loginYouxin(name, pswd);
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
            intent_ = new Intent(context, HomeLoginActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, HomeLoginActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, HomeLoginActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public HomeLoginActivity_.IntentBuilder_ flags(int flags) {
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