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
import android.widget.RelativeLayout;
import com.miicaa.home.R.layout;
import com.miicaa.utils.AddMoreListView;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import org.json.JSONArray;

public final class DetailDiscussFragment_
    extends DetailDiscussFragment
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;
    public final static String DATA_ID_ARG = "dataId";
    public final static String TYPE_ARG = "type";
    public final static String DATA_TYPE_ARG = "dataType";
    public final static String CONTEXT_CLSS_ARG = "contextClss";
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
            contentView_ = inflater.inflate(layout.matter_do_normal, container, false);
        }
        return contentView_;
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectFragmentArguments_();
        AfterInject();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static DetailDiscussFragment_.FragmentBuilder_ builder() {
        return new DetailDiscussFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        sendProgressLayout = ((RelativeLayout) hasViews.findViewById(com.miicaa.home.R.id.sendProgressLayout));
        listView = ((AddMoreListView) hasViews.findViewById(com.miicaa.home.R.id.listview));
        afterview();
    }

    private void injectFragmentArguments_() {
        Bundle args_ = getArguments();
        if (args_!= null) {
            if (args_.containsKey(DATA_ID_ARG)) {
                dataId = args_.getString(DATA_ID_ARG);
            }
            if (args_.containsKey(TYPE_ARG)) {
                type = args_.getString(TYPE_ARG);
            }
            if (args_.containsKey(DATA_TYPE_ARG)) {
                dataType = args_.getString(DATA_TYPE_ARG);
            }
            if (args_.containsKey(CONTEXT_CLSS_ARG)) {
                contextClss = args_.getString(CONTEXT_CLSS_ARG);
            }
        }
    }

    @Override
    public void jsonToData(final JSONArray discussarray) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                DetailDiscussFragment_.super.jsonToData(discussarray);
            }

        }
        );
    }

    @Override
    public void setDisscussCount(final OnTabCountListener disListener) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                DetailDiscussFragment_.super.setDisscussCount(disListener);
            }

        }
        );
    }

    @Override
    public void doBackound() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    DetailDiscussFragment_.super.doBackound();
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

        public DetailDiscussFragment build() {
            DetailDiscussFragment_ fragment_ = new DetailDiscussFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

        public DetailDiscussFragment_.FragmentBuilder_ dataId(String dataId) {
            args_.putString(DATA_ID_ARG, dataId);
            return this;
        }

        public DetailDiscussFragment_.FragmentBuilder_ type(String type) {
            args_.putString(TYPE_ARG, type);
            return this;
        }

        public DetailDiscussFragment_.FragmentBuilder_ dataType(String dataType) {
            args_.putString(DATA_TYPE_ARG, dataType);
            return this;
        }

        public DetailDiscussFragment_.FragmentBuilder_ contextClss(String contextClss) {
            args_.putString(CONTEXT_CLSS_ARG, contextClss);
            return this;
        }

    }

}
