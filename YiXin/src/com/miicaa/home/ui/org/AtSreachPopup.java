package com.miicaa.home.ui.org;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 14-1-2.
 */
public class AtSreachPopup
{
    protected PopupWindow mPopupWindow = null;
    protected View mRoot = null;
    protected LayoutInflater mInflater;
    protected Context mContext = null;
    private static AtSreachBuilder mBuilder;
    protected EditText mContentEdit;
    protected TextView mSearchText;
    protected ListView mList;
    protected RelativeLayout headView;
    protected ArrayList<AtUserInfo> mAtListData;
    protected AtSearchListAdpeter mAdpeter;
    private OnSreachListener mOnSreachListener;
    private PopupWindow.OnDismissListener mOnDismissListener;
    
    AtUserInfo mInputAtUser = null;
    RequestAdpater mNetAdpater;

    public static AtSreachBuilder builder(Context context)
    {
        mBuilder = new AtSreachBuilder(context);
        return mBuilder;
    }

    private AtSreachPopup(Context context)
    {
        mContext = context;
        init();
    }

    public void show()
    {
        Rect frame = new Rect();

        int statusBarHeight = frame.top;

        Activity activity = (Activity)mContext;
        View view = activity.getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(frame);
        int height = 0;
        height = view.getHeight();
        mPopupWindow.setAnimationStyle(R.style.toast_anim);
        mPopupWindow.showAsDropDown(view,0,0 - height);
        mContentEdit.setText("");
    }

    private void init()
    {
        if (null != mPopupWindow && null != mRoot)
            return;
        mInflater = LayoutInflater.from(mContext);
        mRoot = mInflater.inflate(R.layout.at_sreach_activity,null);
        
        headView =(RelativeLayout)mRoot.findViewById(R.id.headView);
        TextView title = (TextView)headView.findViewById(R.id.headTitle);
        title.setText("选择@的人");
        headView.findViewById(R.id.commitButton).setVisibility(View.GONE);
        headView.findViewById(R.id.cancleButton).setVisibility(View.VISIBLE);
        headView.findViewById(R.id.cancleButton).setOnClickListener(onBackClick);
        mContentEdit = (EditText)mRoot.findViewById(R.id.at_sreach_id_edit);
        mContentEdit.addTextChangedListener(textChangeWatcher);

        mSearchText = (TextView)mRoot.findViewById(R.id.at_sreach_id_title);

        mList = (ListView)mRoot.findViewById(R.id.at_sreach_id_search_list);
        mList.setOnItemClickListener(onItemClickListener);

        mPopupWindow = new PopupWindow(mRoot, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
        mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(onDismissListener);

        mAtListData = new ArrayList<AtUserInfo>();
        mAdpeter = new AtSearchListAdpeter(mAtListData);
        mList.setAdapter(mAdpeter);



        requestAdpater.setUrl("/home/phone/discussion/getuser")
                .addParam("keyWord", "")
                .notifyRequest();
    }

    RequestAdpater requestAdpater = new RequestAdpater() {
        @Override
        public void onReponse(ResponseData data) {
            mAtListData.clear();
            if(data.getResultState() == ResponseData.ResultState.eSuccess)
            {
                JSONArray jDataArray = data.getJsonArray();
                if(jDataArray != null && jDataArray.length() > 0)
                {
                    for (int i = 0; i < jDataArray.length(); i++)
                    {
                        JSONObject jItem = jDataArray.optJSONObject(i);
                        if (jItem == null)
                            continue;
                        AtUserInfo atuser = new AtUserInfo();
                        atuser.mAtId = jItem.optString("id");
                        atuser.mAtType= jItem.optString("type");
                        atuser.mAtCode= jItem.optString("code");
                        atuser.mAtName= jItem.optString("name");
                        atuser.mAtDescription= jItem.optString("description");
                        atuser.mAtOrgCode= jItem.optString("orgCode");
                        atuser.noteModify();
                        mAtListData.add(atuser);
                    }
                }
            }

            if(mInputAtUser != null && mInputAtUser.mIsShow && mAtListData.size() == 0)
            {
                mAtListData.add(mInputAtUser);
            }
            mAdpeter.notifyDataSetChanged();
        }

        @Override
        public void onProgress(ProgressMessage msg) {

        }
    };

    public void setOnSreachListener(OnSreachListener onSreachListener)
    {
        mOnSreachListener = onSreachListener;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismiss)
    {
        mOnDismissListener = onDismiss;
    }

    private void searchAtList(String condi)
    {
        if(mInputAtUser == null)
        {
            mInputAtUser = new AtUserInfo();
            mInputAtUser.mAtType = "custom";
            mInputAtUser.mAtCode = "";
            mInputAtUser.mAtDescription = "@";
            mInputAtUser.mAtOrgCode = "";
            mInputAtUser.mAtId = "";
            mInputAtUser.noteModify();
        }
        mInputAtUser.mAtName = condi;
        mInputAtUser.noteModify();
        requestAdpater.changeParam("keyWord",condi).notifyRequest();
    }

    private void hiddenSoftBorad()
    {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            AtUserInfo result = mAtListData.get(i);
            mPopupWindow.dismiss();
            if(mOnSreachListener != null)
            {
                mOnSreachListener.onClick(result);
            }
            mBuilder = null;
        }
    };

    PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            if(mOnDismissListener != null)
            {
                mOnDismissListener.onDismiss();
            }
        }
    };

    TextWatcher textChangeWatcher = new TextWatcher()
    {
        int flag;
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {
            flag = i3;

            String a=charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

            String scon = mContentEdit.getText().toString();
            searchAtList(scon);

        }

        @Override
        public void afterTextChanged(Editable editable)
        {

        }
    };

    View.OnClickListener onBackClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            mPopupWindow.dismiss();
            mBuilder = null;
        }
    };


    public class AtUserInfo
    {
        public String mAtType;
        public String mAtCode;
        public String mAtName;
        public String mAtDescription;
        public String mAtOrgCode;
        public String mAtAvatar;
        public String mAtId;
        public String mAtShowName;
        public Boolean mIsShow = true;

        public void noteModify()
        {
            if(mAtName == null || mAtName.length() < 1)
            {
                mIsShow = false;
            }
            else
            {
                mIsShow = true;
            }

            if(mAtType.equals("unit")|| mAtType.equals("group"))
            {
                mAtShowName = "[" + mAtDescription + "]"+mAtName;
            }
            else if(mAtType.equals("user"))
            {
                mAtShowName = mAtName + "(" + mAtDescription + ")";
            }
            else
            {
                mAtShowName = mAtDescription + mAtName;
            }

        }
    }


    public interface OnSreachListener
    {
        public void onClick(AtUserInfo r);
    }

    public static class AtSreachBuilder
    {
        AtSreachPopup mSreach;

        public AtSreachBuilder (Context context)
        {
            mSreach = new AtSreachPopup(context);
        }

        public AtSreachBuilder setOnSreachListener(OnSreachListener onSreachListener)
        {
            mSreach.setOnSreachListener(onSreachListener);
            return this;
        }

        public AtSreachBuilder setOnDismissListener(PopupWindow.OnDismissListener onDismiss)
        {
            mSreach.setOnDismissListener(onDismiss);
            return this;
        }

        public void show()
        {
            mSreach.show();
        }
    }

    public class AtSearchListAdpeter extends BaseAdapter
    {
        private ArrayList<AtUserInfo> mDatas;

        public AtSearchListAdpeter( ArrayList<AtUserInfo> datas)
        {
            mDatas = datas;
        }

        @Override
        public int getCount()
        {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            DiscussCellDraw draw;
            if(view == null)
            {
                view = mInflater.inflate(R.layout.at_search_list_item_view,null);
                draw = new DiscussCellDraw();

                draw.mUserView = (TextView)view.findViewById(R.id.msg_sreach_cell_id_name);

                view.setTag(draw);
            }
            else
            {
                draw = (DiscussCellDraw)view.getTag();
            }
            draw.mUserView.setText(mDatas.get(i).mAtShowName);
            return view;
        }

        public class DiscussCellDraw
        {
            TextView mUserView;
        }


    }
}
