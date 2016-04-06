package com.miicaa.home.ui.matter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.MatterRequest;
import com.miicaa.common.base.SearchFunction;
import com.miicaa.common.base.Tools;
import com.miicaa.common.http.HttpMessage;
import com.miicaa.common.http.MessageId;
import com.miicaa.common.http.OnResponseListener;
import com.miicaa.common.http.RequestPackage;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.home.MatterType;
import com.miicaa.home.ui.org.LableGroup;


/**
 * Created by Administrator on 13-12-4.
 */
public class MatterHome   {
    public static final int IS_CREATE = 0x1;
    int i = 1 ;
    ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
    MatterCell matterCell;
    Context mContext;
    String mTypeParam;
    String mSrcCode = "";
    String mCounts = "";
    LinearLayout mRootView = null;
    LayoutInflater mInflater;
    PullToRefreshListView mChildrenLayout;

    ListView mListView;
    Boolean isRefresh = true;
    SearchFunction searchFunction;
    public MatterHead getmHead() {
        return mHead;
    }
    MatterHead mHead = null;
    int mNo = 0;
    Boolean isSearch = false;
    final HashMap<String,String> paramMap = new HashMap<String, String>();//get的参数


    public MatterHome(Context context, String typeParam, int no, MatterHead head) {
        mContext = context;
        mTypeParam = typeParam;
        mInflater = LayoutInflater.from(mContext);
        mHead = head;
        mNo = no;
        String url = "/home/phone/thing/getallwork";
        //初始化搜索
        searchFunction = new SearchFunction(mContext,url,true);

//        searchFunction.setSearchCallbacListener(new SearchFunction.SearchCallback() {
//            @Override
//            public void callback(ResponseData data) {
//
//                if (searchFunction.getPageCount() == 1){
//                    i = 1;
//                    jsonObjects.clear();
//                    mChildrenLayout.setLoadingAll(false);
//                }
//                isSearch = true;
//                callBackInRequest(data,true,new MatterCallBackListener() {
//                    @Override
//                    public void callBack() {
//
//                    }
//                });
//
//            }
//        });
//        searchFunction.setTextChangeCallBack(new SearchFunction.TextChangeCallBack() {
//            @Override
//            public void callBack(Boolean isS) {
//                isSearch = isS;
//            }
//        });
//        searchFunction.setDelContentCallback(new SearchFunction.DelContentCallback() {
//            @Override
//            public void callBack() {
//                requestMatter(IS_CREATE,true,new MatterCallBackListener() {
//                    @Override
//                    public void callBack() {
//
//                    }
//                });
//                isSearch = false;
//            }
//        });
        initUI();
    }


    public View getRootView() {
        return mRootView;
    }

    public String getTypeParam() {
        return mTypeParam;
    }

    public int getNo() {
        return mNo;
    }

    public void refresh() {
        //requestMatter(mTypeParam);
    }

    public void setMsg(String msg) {

    }

    public void setSelected(boolean selected) {
        mHead.setSelected(selected);
    }

    private void initUI() {
        mRootView = new LinearLayout(mContext);
        mRootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView.setOrientation(LinearLayout.VERTICAL);
        //搜索框框
        View searchView = searchFunction.getSearchView();
        mRootView.addView(searchView);
        Context context = mRootView.getContext();
        ListView.LayoutParams params = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mChildrenLayout  = new PullToRefreshListView(context, PullToRefreshBase.Mode.BOTH, PullToRefreshBase.AnimationStyle.FLIP);
        mChildrenLayout.setLayoutParams(params);
        mChildrenLayout.setOnRefreshListener(new MyOnRefreshListener2());
        mRootView.addView(mChildrenLayout);
        mListView =mChildrenLayout.getRefreshableView();
        matterCell = new MatterCell(mRootView.getContext(),jsonObjects,MatterType.eDo);
        mListView.setFastScrollEnabled(true);
        mListView.setFadingEdgeLength(0);


        mListView.setAdapter(matterCell);

    }





    class refreshData extends AsyncTask<Void,Void,String[]>{

        @Override
        protected String[] doInBackground(Void... voids) {
//            try {


                if (isRefresh) {
                    i = 1;
                    jsonObjects.clear();
                    matterCell.notifyDataSetChanged();
                    mChildrenLayout.setLoadingAll(false);
                    if (isSearch){
                        searchFunction.setPageCount(1);
                        searchFunction.search();
                    }else {
                        getTabCount(mSrcCode);
                        requestMatter(IS_CREATE, false,new MatterCallBackListener() {
                            @Override
                            public void callBack() {

                            }
                        });
                    }

                } else {

                    if (isSearch == true){
                        searchFunction.search();
                    }else {
                        i++;
                        paramMap.put("pageNo", i+"");
                        requestMatter(0, false,new MatterCallBackListener() {
                            @Override
                            public void callBack() {

                            }
                        });
                    }
                }
//                Thread.sleep(1000);
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
            return new String[0];
        }
        @Override
        protected void onPostExecute(String[] result) {


//            super.onPostExecute(result);

        }
    }

    class MyOnRefreshListener2 implements PullToRefreshBase.OnRefreshListener2<ListView> {


        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            String label = DateUtils.formatDateTime(mContext,
                    System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);

            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            isRefresh = true;
            jsonObjects.clear();
            matterCell.notifyDataSetChanged();
            new refreshData().execute();

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            // ��������
            isRefresh = false;
            new refreshData().execute();
        }

    }

    public void getTabCount(String srcCode) {

        String url = "/home/phone/thing/gettodocount";
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {

                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    try {
                        JSONObject counts = data.getJsonObject();
                        if (mTypeParam.equals("")) {
                            String count = counts.getString("totalCount");
                            mHead.setMatterCount(Integer.parseInt(count));
                            mCounts = counts.getString("totalCount");;

                        }

                        if (mTypeParam.equals("01")) {
                            String count = counts.getString("todayCount");
                            mHead.setMatterCount(Integer.parseInt(count));
                            mCounts = counts.getString("todayCount");

                        }

                        if (mTypeParam.equals("02")) {
                            String count = counts.getString("tomorrowCount");
                            mHead.setMatterCount(Integer.parseInt(count));
                            mCounts = counts.getString("tomorrowCount");

                        }

                        if (mTypeParam.equals("03")) {
                            String count = counts.getString("futureCount");
                            mHead.setMatterCount(Integer.parseInt(count));
                            mCounts = counts.getString("futureCount");

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //TODO:失败了要弹通知
                }
            }//得到有多少数据

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("srcCode", srcCode)
                .notifyRequest();
    }

    public void setmCounts(String counts){
        mCounts = counts;
    }
    //设置msrcCode
    public void setmSrcCode(String srcCode){
        mSrcCode = srcCode;
        paramMap.put("srcCode", mSrcCode);
    }
    //设置获取数据参数
    public void setParam(String doIng){
        paramMap.put("pageSize", "20");
        paramMap.put("pageNo",i+"");
        paramMap.put("type", doIng);
        paramMap.put("srcCode", mSrcCode);
        paramMap.put("viewType", mTypeParam);
        HashMap<String,String> map = new HashMap<String, String>();
        map.putAll(paramMap);
        searchFunction.setParam(map);

    }



    //IS_CRAETE代表刷新，搜索后要进入执行的,isProgress为true时，会显示加载动画
    public void requestMatter(int doWhat, final Boolean isProgress,
                              final MatterCallBackListener matterCallBackListener) {
        if (Integer.parseInt(mCounts) == 0){
            return;
        }
        switch (doWhat) {
            case IS_CREATE:
                searchFunction.clearSearch();
                i = 1;
                jsonObjects.clear();
                mChildrenLayout.setLoadingAll(false);

                break;
            default:

                break;
        }

        final String url = "/home/phone/thing/getallwork";
        MatterRequest.requestMatterHome(url,paramMap,new MatterRequest.MatterHomeCallBackListener() {
            @Override
            public void callBack(ResponseData data) {
                callBackInRequest(data,false,matterCallBackListener);

            }
        });

    }
    //执行完数据得到后回调
    private void callBackInRequest(ResponseData data,Boolean search,MatterCallBackListener callBackListener) {
        if (data.getResultState() == ResponseData.ResultState.eSuccess) {
        try {

                if (data.getJsonObject().length() == 0) {
                    if(isSearch && searchFunction.getPageCount() == 1){
                        jsonObjects.clear();
                        Toast.makeText(mContext,"抱歉，找不到符合条件的事项，请换个搜索条件试试吧！",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext,"哎呀，没有数据啦",
                                Toast.LENGTH_SHORT).show();
                    }

                    matterCell.notifyDataSetChanged();
                    mChildrenLayout.onRefreshComplete();

                    return;
                }
            if (search){

            }
                JSONArray workList = data.getJsonObject().getJSONArray("workList");
                ArrayList<JSONObject> jsObject = new ArrayList<JSONObject>();

                if (workList != null && workList.length() > 0) {
                    for (int j = 0; j < workList.length(); j++) {
                        jsObject.add(workList.optJSONObject(j));
                    }
                    jsonObjects.addAll(jsObject);
                    matterCell.notifyDataSetChanged();
                    mChildrenLayout.onRefreshComplete();
                    if (jsonObjects.size() == Integer.parseInt(mCounts)) {
                        mChildrenLayout.setLoadingAll(true);
                        if (i >1) {
                            Toast.makeText(mContext, "哎呀，没有数据啦",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    mChildrenLayout.onRefreshComplete();
                    Toast.makeText(mContext, "未得到数据，请检查您的网络后重试",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                callBackListener.callBack();


            }catch(Exception e){
                e.printStackTrace();
            }
        }else {
        mChildrenLayout.onRefreshComplete();
        callBackListener.callBack();
        Toast.makeText(mContext,data.getMsg()+"",1).show();
        //TODO:失败了要弹通知
    }
 }


    private void setLables(ArrayList<String> lables){
        LinearLayout lablesLinear = new LinearLayout(mContext);
        lablesLinear.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        lablesLinear.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0 ; i < lables.size(); i++){
            TextView lableText = new TextView(mContext);
            lableText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lableText.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bar1));
            lableText.setText(lables.get(i));
        }
    }

    private void testDo() {
        //这部分先不要缓存了
//        TodoInfo.init();
//        MatterInfo.init();
//        NoticeInfo.init();
//        CommentInfo.init();
//        AccessoryInfo.init();
//        ArrayList<ContentValues> contentValuesesTodo = TodoInfo.todoList(mTypeParam);
//        for (int i = 0; i < contentValuesesTodo.size(); i++) {
//            addCellData(new MatterCell(contentValuesesTodo.get(i), MatterType.eDo, null));
//        }
        if (mTypeParam.equals("all")) {
//            ArrayList<ContentValues> contentValuesesToNotice = TodoInfo.toNoticeList();//公告
//            if (contentValuesesToNotice.size() > 0) {
//                addCellData(new MatterCell(contentValuesesToNotice.get(0), MatterType.eBulletin, contentValuesesToNotice));
//            }
//
//
//            ArrayList<ContentValues> contentValuesesToOver = TodoInfo.toOverList();//已办
//            if (contentValuesesToOver.size() > 0) {
//                addCellData(new MatterCell(contentValuesesToOver.get(0), MatterType.eLast, contentValuesesToOver));
//            }
        }


//        ArrayList<ContentValues> contentValuesesTodone = TodoInfo.todoneList(mTypeParam);
//        for (int i = 0; i < contentValuesesTodone.size(); i++) {
//            addCellData(new MatterCell(contentValuesesTodone.get(i), MatterType.eDone, null));
//        }

    }


    private void handleReponseData(String tag, byte[] revedData) {
        if (tag.equals("matterRefresh")) {
            //等待修改
            mChildrenLayout.removeAllViews();
            //responseInitData(revedData);
        }

    }

    OnResponseListener onResponseListener = new OnResponseListener() {
        @Override
        public void OnResponse(RequestPackage reqPackage, HttpMessage msg) {
            switch (msg.mResCode) {
                case MessageId.HTTP_RESPONSE_RECEIVE_END: {
                    handleReponseData(reqPackage.mTagString, msg.receivedData);
                    break;
                }
                default: {
                    handleReponseData(reqPackage.mTagString, msg.receivedData);
                    break;
                }
            }
        }
    };

   

    class MatterCell extends BaseAdapter{
//        String mId;
//        String mConut;
//        String mTitle;
//        String mCode;
//        String mMono;
//        String mProgress;
//        String mDiscuss;
//        String mDynamic;
//        Date mTime;
        MatterType mType;
        int mClass;
        int count ;
        int in = 0;
        int resId;
//        ContentValues cellData;

        ArrayList<JSONObject> jsdata;
        boolean isPlan;
        boolean isRemind;
        boolean isAcessory;
        Context context;

        LayoutInflater layoutInflater;

        //HashMap<Integer,Boolean> lableAddMap = new HashMap<Integer, Boolean>();

        public MatterCell(Context context,ArrayList<JSONObject> data,MatterType type) {
//            super(context,resousce,data);
            jsdata = data;
            this.context = context;
            this.mType = type;
            layoutInflater = LayoutInflater.from(context);
            LableGroup saveLbaleGroup;
                //这个是什么东西不知道，先注释一下
//            if (mType != MatterType.eBulletin) {
//                if (cellData.getAsString(MatterInfoSql.col_name_data_type).equals("assessment")) {
//                    mClass = 1;
//                } else {
//                    mClass = 0;
//                }
//            }
        }

//        public MatterCell(ContentValues data, MatterType type, ArrayList<ContentValues> contentValueses) {
//            cellData = data;
//            mType = type;
//            if (mType != MatterType.eBulletin) {
//                if (cellData.getAsString(MatterInfoSql.col_name_data_type).equals("assessment")) {
//                    mClass = 1;
//                } else {
//                    mClass = 0;
//                }
//            }
//            if (cellData.getAsString(TodoInfoSql.col_name_plan_time).length() > 0) {
//                isPlan = true;
//            }
//            if (cellData.getAsString(TodoInfoSql.col_name_remind_str).length() > 0) {
//                isRemind = true;
//            }
//            if (cellData.getAsString(TodoInfoSql.col_name_plan_time).length() > 0) {
////                isAcessory = true;
//            }
////            initUI();
////            initData();
//        }

//        public View getRootView() {
//            return mRootView;
//        }
//
//        private void initUI() {
//            mRootView = mInflater.inflate(R.layout.matter_cell_view, null);
////            mRootView.setOnClickListener(onItemClick);
//            mHeadImg = (ImageView) mRootView.findViewById(R.id.matter_cell_id_head);
//            mWeakImg = (ImageView) mRootView.findViewById(R.id.matter_cell_id_weak_notify);
//            mCountText = (TextView) mRootView.findViewById(R.id.matter_cell_id_count);
//            mTitleText = (TextView) mRootView.findViewById(R.id.matter_cell_id_title);
//            mDateText = (TextView) mRootView.findViewById(R.id.matter_cell_id_time);
//            mMonoText = (TextView) mRootView.findViewById(R.id.matter_cell_id_momo);
//            mContentText = (TextView) mRootView.findViewById(R.id.matter_cell_id_content);
//
//            mRemindImg = (ImageView) mRootView.findViewById(R.id.matter_cell_id_remind);
//            mPlanImg = (ImageView) mRootView.findViewById(R.id.matter_cell_id_plan);
//            mAccessoryImg = (ImageView) mRootView.findViewById(R.id.matter_cell_id_accessory);
//        }

        private void initDataJson(ViewHolder holder,int position) throws Exception {
            {
//                JSONObject jsonObject = getItem(position);
                JSONObject jsonObject = jsdata.get(position);
                if (mType == MatterType.eDo) {
                    //mCountText.setVisibility(View.VISIBLE);
                    //mCountText.setText(15);//这个东西是未读消息数，以后再弄
                    holder.mWeakImg.setVisibility(View.GONE);
                    holder.mTitleText.setText(jsonObject.optString("creatorName"));
                    holder.mDateText.setText(DateHelper.getShowUpdateDate(jsonObject.optLong("createTime")));
                    holder.mMonoText.setText(jsonObject.optString("title"));
//                    mContentText.setText("描述：" + jsonData.optString("content"));
                    holder.mContentText.setVisibility(View.GONE);

                    String creatorCode = jsonObject.optString("creatorCode");
                    Tools.setHeadImg(creatorCode,holder.mHeadImg);
                    if (jsonObject.isNull("planTime")) {
                        holder.mPlanImg.setVisibility(View.GONE);
                    }else{
                        holder.mPlanImg.setVisibility(View.VISIBLE);
                    }
                    if (jsonObject.isNull("remindTime")) {
                        holder.mRemindImg.setVisibility(View.GONE);
                    }else{
                        holder.mRemindImg.setVisibility(View.VISIBLE);
                    }
                    if (!jsonObject.isNull("hasAttach")) {
                        if (jsonObject.optBoolean("hasAttach") == true) {
                            holder.mAccessoryImg.setVisibility(View.VISIBLE);
                        }else {
                            holder.mAccessoryImg.setVisibility(View.GONE);
                        }
                    }

                }

            }
        }
        @Override
        public int getCount() {
            count = jsdata.size();
          return count;
        }

        @Override
        public Object getItem(int i) {
            return jsdata.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView( int position, View view, ViewGroup viewGroup) {
            ViewHolder holder = new ViewHolder();
            LinearLayout linearLayout = new LinearLayout(mContext);
            final JSONObject jsonObject = jsdata.get(position);
            if(view == null) {
                view = layoutInflater.inflate(R.layout.matter_cell_view, null);
                view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.matter_do_cell_selector));
                holder.lableGroup = (LableGroup)view.findViewById(R.id.matter_cell_view_label_group);
                holder.mHeadImg = (ImageView) view.findViewById(R.id.matter_cell_id_head);
                holder.mWeakImg = (ImageView) view.findViewById(R.id.matter_cell_id_weak_notify);
                holder.mCountText = (TextView) view.findViewById(R.id.matter_cell_id_count);
                holder.mTitleText = (TextView) view.findViewById(R.id.matter_cell_id_title);
                holder.mDateText = (TextView) view.findViewById(R.id.matter_cell_id_time);
                holder.mMonoText = (TextView) view.findViewById(R.id.matter_cell_id_momo);
                holder.mContentText = (TextView) view.findViewById(R.id.matter_cell_id_content);
                holder.mRemindImg = (ImageView) view.findViewById(R.id.matter_cell_id_remind);
                holder.mPlanImg = (ImageView) view.findViewById(R.id.matter_cell_id_plan);
                holder.mAccessoryImg = (ImageView) view.findViewById(R.id.matter_cell_id_accessory);
                view.setTag(holder);
            }
            else {
                holder =(ViewHolder) view.getTag();
            }
            try {
                initDataJson(holder,position);
                    if (jsonObject.optJSONArray("labels")==null || jsonObject.optJSONArray("labels").length()<1) {
                        if (holder.lableGroup.getChildCount() != 0) {
                            holder.lableGroup.removeAllViews();
                        }

                    }else {
                        if (holder.lableGroup.getChildCount() != 0){
                            holder.lableGroup.removeAllViews();
                        }
                        for (int j = 0; j< jsonObject.optJSONArray("labels").length(); j++) {
                            linearLayout = new LinearLayout(mContext);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            linearLayout.setLayoutParams(params);
                            linearLayout.setMinimumHeight(40);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            RelativeLayout sRelativeLayout = new RelativeLayout(mContext);
                            ViewGroup.LayoutParams rParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            sRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.labelbg));
                            sRelativeLayout.setLayoutParams(rParams);
                            TextView textView = new TextView(mContext);
                            RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            tParams.setMargins(5,0,5,0);
                            tParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                            textView.setLayoutParams(tParams);
                            textView.setText(jsonObject.optJSONArray("labels").optJSONObject(j).optString("label"));
                            sRelativeLayout.addView(textView);
                            RelativeLayout relativeLayout = new RelativeLayout(mContext);
                            relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(20, ViewGroup.LayoutParams.WRAP_CONTENT));
                            linearLayout.addView(sRelativeLayout);
                            linearLayout.addView(relativeLayout);
                            holder.lableGroup.addView(linearLayout);
                        }

                    }

            }catch (Exception e){
                e.printStackTrace();
            }
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mType == MatterType.eDo || mType == MatterType.eDone) {
//
//                        Intent intent = new Intent(mContext, ArrangementDetailActivity.class);
//                        Bundle bundle = new Bundle();
//                        try {
//                            bundle.putString("type", jsonObject.optString("dataType"));
//                            bundle.putString("dataId", jsonObject.optString("id"));
//                            bundle.putString("status", jsonObject.optString("status"));
//                            bundle.putString("operateGroup", jsonObject.optString("operateGroup"));
//                            //bundle.putBoolean("isCache", true);
//                            intent.putExtra("bundle", bundle);
//                            mContext.startActivity(intent);
//                            ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    } else if (mType == MatterType.eBulletin) {
//                        // 公告activity;
//                        Intent intent = new Intent(mContext, NoticeActivity.class);
//                        mContext.startActivity(intent);
//                        ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
//                    }
//                }
//            });
            Log.v("this is ",view.toString());
            return view;
        }
        class ViewHolder{
            ImageView mHeadImg;
            ImageView mWeakImg;
            TextView mCountText;
            TextView mTitleText;
            TextView mDateText;
            TextView mMonoText;
            TextView mContentText;

            ImageView mRemindImg;
            ImageView mPlanImg;
            ImageView mAccessoryImg;

            LableGroup lableGroup;
        }
    }
    public interface MatterCallBackListener{
        public void callBack();
    }
}
