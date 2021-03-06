//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.yxst.epic.yixin.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miicaa.home.R.layout;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.response.CheckUpdateResponse;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import org.springframework.web.client.RestClientException;

public final class MeFragment_
    extends MeFragment
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;
    public final static String MEMBER_ARG = "member";
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public View findViewById(int id) {
        if (contentView_ == null) {
            return null;
        }
        return contentView_.findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.fragment_me, container, false);
        }
        return contentView_;
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectFragmentArguments_();
        afterInject();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static MeFragment_.FragmentBuilder_ builder() {
        return new MeFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        tvUser = ((TextView) hasViews.findViewById(com.miicaa.home.R.id.tvUser));
        tvName = ((TextView) hasViews.findViewById(com.miicaa.home.R.id.tv_name));
        {
            View view = hasViews.findViewById(com.miicaa.home.R.id.rv_user);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MeFragment_.this.onClickUserInfo(view);
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(com.miicaa.home.R.id.btnLogout);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MeFragment_.this.onClickBtnLogout(view);
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(com.miicaa.home.R.id.btnCheckUpdate);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MeFragment_.this.onClickCheckUpdate(view);
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(com.miicaa.home.R.id.btnAbout);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MeFragment_.this.onClickAbout(view);
                    }

                }
                );
            }
        }
        afterViews();
    }

    private void injectFragmentArguments_() {
        Bundle args_ = getArguments();
        if (args_!= null) {
            if (args_.containsKey(MEMBER_ARG)) {
                member = ((Member) args_.getSerializable(MEMBER_ARG));
            }
        }
    }

    @Override
    public void checkUpdateOnPostExecute(final CheckUpdateResponse response) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                MeFragment_.super.checkUpdateOnPostExecute(response);
            }

        }
        );
    }

    @Override
    public void onRestClientExceptionThrown(final RestClientException e) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                MeFragment_.super.onRestClientExceptionThrown(e);
            }

        }
        );
    }

    @Override
    public void checkUpdateOnPreExecute() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                MeFragment_.super.checkUpdateOnPreExecute();
            }

        }
        );
    }

    @Override
    public void checkUpdateDoInBackground() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    MeFragment_.super.checkUpdateDoInBackground();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public MeFragment build() {
            MeFragment_ fragment_ = new MeFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

        public MeFragment_.FragmentBuilder_ member(Member member) {
            args_.putSerializable(MEMBER_ARG, member);
            return this;
        }

    }

}
