//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.yxst.epic.yixin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.miicaa.home.R.layout;
import com.yxst.epic.yixin.adapter.ChatAdapter_;
import com.yxst.epic.yixin.data.dto.response.GetAppOpertionListResponse;
import com.yxst.epic.yixin.data.dto.response.PushResponse;
import com.yxst.epic.yixin.data.rest.IMInterface_;
import com.yxst.epic.yixin.rest.OperationRest_;
import com.yxst.epic.yixin.rest.ServiceResult;
import com.yxst.epic.yixin.view.ChatSendOptView;
import com.yxst.epic.yixin.view.FooterView;
import com.yxst.epic.yixin.view.RcdView;
import com.yxst.epic.yixin.view.ResizeLayout;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import org.springframework.web.client.RestClientException;

public final class ChatActivity_
    extends ChatActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String LOCAL_USER_NAME_EXTRA = "localUserName";
    public final static String REMOTE_DISPLAY_NAME_EXTRA = "remoteDisplayName";
    public final static String REMOTE_USER_NAME_EXTRA = "remoteUserName";
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.im_activity_chat);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectExtras_();
        mLayoutInflater = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        myRestClient = new IMInterface_();
        operationRest = new OperationRest_();
        mChatAdapter = ChatAdapter_.getInstance_(this);
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

    public static ChatActivity_.IntentBuilder_ intent(Context context) {
        return new ChatActivity_.IntentBuilder_(context);
    }

    public static ChatActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new ChatActivity_.IntentBuilder_(fragment);
    }

    public static ChatActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new ChatActivity_.IntentBuilder_(supportFragment);
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
        listView = ((ListView) hasViews.findViewById(android.R.id.list));
        chat_add = ((ImageButton) hasViews.findViewById(com.miicaa.home.R.id.chat_base_id_add));
        gtitle = ((TextView) hasViews.findViewById(com.miicaa.home.R.id.chat_base_id_title));
        viewRcd = ((RcdView) hasViews.findViewById(com.miicaa.home.R.id.viewRcd));
        layoutRoot = ((ResizeLayout) hasViews.findViewById(com.miicaa.home.R.id.layoutRoot));
        viewFooter = ((FooterView) hasViews.findViewById(com.miicaa.home.R.id.viewFooter));
        viewChatSendOpt = ((ChatSendOptView) hasViews.findViewById(com.miicaa.home.R.id.viewChatSendOpt));
        {
            View view = hasViews.findViewById(com.miicaa.home.R.id.chat_base_id_back);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ChatActivity_.this.click();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(com.miicaa.home.R.id.chat_base_id_add);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ChatActivity_.this.addClick();
                    }

                }
                );
            }
        }
        {
            AdapterView<?> view = ((AdapterView<?> ) hasViews.findViewById(android.R.id.list));
            if (view!= null) {
                view.setOnItemClickListener(new OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ChatActivity_.this.onChatMessagesItemClick(position);
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
            if (extras_.containsKey(LOCAL_USER_NAME_EXTRA)) {
                localUserName = extras_.getString(LOCAL_USER_NAME_EXTRA);
            }
            if (extras_.containsKey(REMOTE_DISPLAY_NAME_EXTRA)) {
                remoteDisplayName = extras_.getString(REMOTE_DISPLAY_NAME_EXTRA);
            }
            if (extras_.containsKey(REMOTE_USER_NAME_EXTRA)) {
                remoteUserName = extras_.getString(REMOTE_USER_NAME_EXTRA);
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
            case  3 :
                ChatActivity_.this.onActivityResultGetImageByCamera(resultCode, data);
                break;
            case  1 :
                ChatActivity_.this.onActivityResultChatDetail(resultCode, data);
                break;
            case  2 :
                ChatActivity_.this.onActivityResultGetImageBySdcard(resultCode, data);
                break;
        }
    }

    @Override
    public void onPostExecuteOpt(final ServiceResult<Object> response) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ChatActivity_.super.onPostExecuteOpt(response);
            }

        }
        );
    }

    @Override
    public void onPostExecuteGetAppOperationList(final GetAppOpertionListResponse response) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ChatActivity_.super.onPostExecuteGetAppOperationList(response);
            }

        }
        );
    }

    @Override
    public void onPreExecuteOpt() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ChatActivity_.super.onPreExecuteOpt();
            }

        }
        );
    }

    @Override
    public void onRestClientExceptionThrown(final RestClientException e) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ChatActivity_.super.onRestClientExceptionThrown(e);
            }

        }
        );
    }

    @Override
    public void onPostExecute(final long dbMsgId, final PushResponse response) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ChatActivity_.super.onPostExecute(dbMsgId, response);
            }

        }
        );
    }

    @Override
    public void doInBackgroundOperation(final String url) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("doInBackgroundOperation", 0, "doInBackgroundOperation") {


            @Override
            public void execute() {
                try {
                    ChatActivity_.super.doInBackgroundOperation(url);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void doInBackgroundGetAppOperationList() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ChatActivity_.super.doInBackgroundGetAppOperationList();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void doInBackgroundSendMsgImg(final String filePath) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ChatActivity_.super.doInBackgroundSendMsgImg(filePath);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void onCompressBitmap(final String filePaht) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ChatActivity_.super.onCompressBitmap(filePaht);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void updateReadWithNoObserver() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ChatActivity_.super.updateReadWithNoObserver();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void doInBackgroundSendMsgVoice(final String filePath, final long voiceLength) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ChatActivity_.super.doInBackgroundSendMsgVoice(filePath, voiceLength);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void doInBackgroundPush(final String content) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ChatActivity_.super.doInBackgroundPush(content);
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
            intent_ = new Intent(context, ChatActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, ChatActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, ChatActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public ChatActivity_.IntentBuilder_ flags(int flags) {
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

        public ChatActivity_.IntentBuilder_ localUserName(String localUserName) {
            intent_.putExtra(LOCAL_USER_NAME_EXTRA, localUserName);
            return this;
        }

        public ChatActivity_.IntentBuilder_ remoteDisplayName(String remoteDisplayName) {
            intent_.putExtra(REMOTE_DISPLAY_NAME_EXTRA, remoteDisplayName);
            return this;
        }

        public ChatActivity_.IntentBuilder_ remoteUserName(String remoteUserName) {
            intent_.putExtra(REMOTE_USER_NAME_EXTRA, remoteUserName);
            return this;
        }

    }

}
