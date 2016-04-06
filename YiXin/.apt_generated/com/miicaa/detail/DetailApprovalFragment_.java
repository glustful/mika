//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miicaa.home.R.layout;
import com.miicaa.home.data.business.matter.MatterInfo;
import com.miicaa.utils.AddMoreListView;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import org.json.JSONArray;

public final class DetailApprovalFragment_
    extends DetailApprovalFragment
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;
    public final static String M_INFO_ARG = "mInfo";
    public final static String OPERATE_GROUP_ARG = "operateGroup";
    public final static String DATA_ID_ARG = "dataId";
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
            contentView_ = inflater.inflate(layout.matter_do_approve, container, false);
        }
        return contentView_;
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectFragmentArguments_();
        beginRequset();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static DetailApprovalFragment_.FragmentBuilder_ builder() {
        return new DetailApprovalFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        listview = ((AddMoreListView) hasViews.findViewById(com.miicaa.home.R.id.apprlistview));
        afterView();
    }

    private void injectFragmentArguments_() {
        Bundle args_ = getArguments();
        if (args_!= null) {
            if (args_.containsKey(M_INFO_ARG)) {
                mInfo = ((MatterInfo) args_.getSerializable(M_INFO_ARG));
            }
            if (args_.containsKey(OPERATE_GROUP_ARG)) {
                operateGroup = args_.getString(OPERATE_GROUP_ARG);
            }
            if (args_.containsKey(DATA_ID_ARG)) {
                dataId = args_.getString(DATA_ID_ARG);
            }
        }
    }

    @Override
    public void refresh() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                DetailApprovalFragment_.super.refresh();
            }

        }
        );
    }

    @Override
    public void setApprovalCountListener(final OnTabCountListener apprListener) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                DetailApprovalFragment_.super.setApprovalCountListener(apprListener);
            }

        }
        );
    }

    @Override
    public void jsonToData(final JSONArray json) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    DetailApprovalFragment_.super.jsonToData(json);
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

        public DetailApprovalFragment build() {
            DetailApprovalFragment_ fragment_ = new DetailApprovalFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

        public DetailApprovalFragment_.FragmentBuilder_ mInfo(MatterInfo mInfo) {
            args_.putSerializable(M_INFO_ARG, mInfo);
            return this;
        }

        public DetailApprovalFragment_.FragmentBuilder_ operateGroup(String operateGroup) {
            args_.putString(OPERATE_GROUP_ARG, operateGroup);
            return this;
        }

        public DetailApprovalFragment_.FragmentBuilder_ dataId(String dataId) {
            args_.putString(DATA_ID_ARG, dataId);
            return this;
        }

    }

}
