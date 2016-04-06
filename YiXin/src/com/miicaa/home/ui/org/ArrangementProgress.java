package com.miicaa.home.ui.org;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.common.base.LinearMessageBox;
import com.miicaa.common.base.MulFunCellView;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-12-26.
 */
public class ArrangementProgress extends Activity {

    ExpandableListView mList;

    ProgressAdpater mAdpater;
    String mId;
    String mType;
    String mGroupId;//分组用
    ArrayList<List<ProgressData>> mGroupList;//分组
    HashMap<String,String> groupMap;//通过groupId分组




    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupList = new ArrayList<List<ProgressData>>();
        groupMap = new HashMap<String, String>();
        setContentView(R.layout.arrangement_progress_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mId = bundle.getString("dataId");
        mType = bundle.getString("type");
        initUI();
        mAdpater = new ProgressAdpater();
        mList.setAdapter(mAdpater);
        mList.setOnTouchListener(onListTouch);
        requestData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }

    private void requestData() {
        String url = "/home/phone/thing/getdialogueinfo";

        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {

                convertData(data.getJsonArray());
            }

            @Override
            public void onProgress(ProgressMessage msg) {
            }
        }.setUrl(url)
                .addParam("dataId", mId)
                .addParam("type","1".equals(mType)?"progress":"flow_record")
                .notifyRequest();
    }

    private void convertData(JSONArray array) {
        if (array == null) {
            return;
        }
        List<ProgressData> allGroupIds = new ArrayList<ProgressData>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject jrow = array.optJSONObject(i);
            String groupId = jrow.optString("flag");
            groupMap.put(groupId,groupId);//分组
            allGroupIds.add(new ProgressData(jrow,null));
        }
        for (Map.Entry s : groupMap.entrySet() ){
            String groupId = (String)s.getValue();
            List<ProgressData> dataArray;
            dataArray = new ArrayList<ProgressData>();
            for(int i = 0; i < allGroupIds.size(); i++){
                if (allGroupIds.get(i).groupId.equalsIgnoreCase(groupId)){
                    dataArray.add(allGroupIds.get(i));
                }
            }
            mGroupList.add(dataArray);
        }
//        mAdpater = new ProgressAdpater();
//        mList.setAdapter(mAdpater);
//        mList.setOnTouchListener(onListTouch);
        mAdpater.notifyDataSetChanged();
        for (int i = 0; i <mGroupList.size();i++){
            mList.expandGroup(i);
        }
    }


    private void initUI() {
        mList = (ExpandableListView) findViewById(R.id.arrange_progress_id_list);
        Button button = (Button) findViewById(R.id.arrange_progress_id_back);
        button.setOnClickListener(onBackClick);
    }

    View.OnTouchListener onListTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    //Toast.makeText(ArrangementProgress.this,"dddd",100);
                    break;
            }
            return false;
        }
    };

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    //所有审批状态
    class ProgressData {
    	 String group;
         String mName;
         String mCode;
         String mProContent;
         String mTime;
         int mFileCount;
         int mPngCount;
         String mPId;
         String status;
         String groupId;

        public ProgressData(JSONObject jData,String group) {
            mPngCount = 0;
            mFileCount = 0;
            if (jData != null) {
                mName = jData.optString("userName");
                mCode = jData.optString("userCode");
                mProContent = jData.optString("content");
                status = jData.optString("status");
                Date date = new Date(jData.optLong("createTime"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                mTime = format.format(date);
                mPId = jData.optString("id");
                groupId = jData.optString("flag");
                requestData();
            }
        }

        private void requestData() {
            String url = "/home/phone/thing/getworkattach";

            new RequestAdpater() {
                @Override
                public void onReponse(ResponseData data) {

                    getDetailAttechment(data.getJsonArray());
                }

                @Override
                public void onProgress(ProgressMessage msg) {
                }
            }.setUrl(url)
                    .addParam("dataId", mPId)
                    .notifyRequest();
        }

        private void getDetailAttechment(JSONArray jDataArray) {
            if (jDataArray == null) {
                return;
            }

            for (int i = 0; i < jDataArray.length(); i++) {
                JSONObject jrow = jDataArray.optJSONObject(i);
                AccessoryInfo info = new AccessoryInfo();
                info.setId(jrow.optString("id"));
                info.setTitle(jrow.optString("title"));
                info.setExt(jrow.optString("ext"));
                info.setSize(jrow.optLong("size"));
                info.setInfoId(jrow.optString("infoId"));
                info.setFileId(jrow.optString("fileId"));
                if (info.getExt().equals("png")) {
                    mPngCount++;
                } else {
                    mFileCount++;
                }
            }

        }

        public void setView(ProgressAdpater.CellView view) {
            view.mConLayout.setOnLongClickListener(onConLongClick);
            view.mNameText.setText(mName);
            Tools.setHeadImg(mCode, view.mHead);
            if ("0".equals(status) || "1".equals(status) || "3".equals(status) ||
                    "4".equals(status)  || "6".equals(status)){
                //不同意,等待会签，会签不通过
                view.mContentText.setTextColor(Color.RED);
            }else if ("2".equals(status) || "5".equals(status)){//同意和会签通过
                view.mContentText.setTextColor(Color.GREEN);
            }
            if ("null".equalsIgnoreCase(mProContent) ){
                if ("0".equals(status)){
                    view.mContentText.setText("待审批");
                }else if ("3".equals(status)){
                    view.mContentText.setText("未审批");
                }else if ("4".equals(status)){
                    view.mContentText.setText("等待会签结果");
                }
                }
            else {

                view.mContentText.setText(mProContent);
            }
            view.mTimeText.setText(mTime);
            if (mFileCount == 0 && mPngCount == 0) {
                view.mFileLayout.setVisibility(View.GONE);
            } else if (mFileCount > 0 && mPngCount > 0) {
                view.mFileLayout.setVisibility(View.VISIBLE);
                view.mAttLayout.setVisibility(View.VISIBLE);
                view.mPngLayout.setVisibility(View.VISIBLE);
                view.mFileCountText.setText(String.valueOf(mFileCount));
                view.mPngCountText.setText(String.valueOf(mPngCount));
            } else if (mFileCount > 0) {
                view.mFileLayout.setVisibility(View.VISIBLE);
                view.mAttLayout.setVisibility(View.VISIBLE);
                view.mPngLayout.setVisibility(View.GONE);
                view.mFileCountText.setText(String.valueOf(mFileCount));
            } else if (mPngCount > 0) {
                view.mFileLayout.setVisibility(View.VISIBLE);
                view.mAttLayout.setVisibility(View.GONE);
                view.mPngLayout.setVisibility(View.VISIBLE);

                view.mPngCountText.setText(String.valueOf(mPngCount));
            } else {
                view.mFileLayout.setVisibility(View.GONE);
            }
        }

        View.OnLongClickListener onConLongClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ArrayList<PopupItem> items = new ArrayList<PopupItem>();
                items.add(new PopupItem("删除", "call"));
                items.add(new PopupItem("复制", "sendTo"));
                items.add(new PopupItem("继续执行", "copy"));
                LinearMessageBox.builder(ArrangementProgress.this)
                        .setItems(items)
                        .setOnMessageListener(new OnMessageListener() {
                            @Override
                            public void onClick(PopupItem msg) {

                            }
                        })
                        .show();

                return true;
            }
        };
    }

    class ProgressAdpater extends BaseExpandableListAdapter {
        ArrayList<ProgressData> mDataArray;
        ArrayList<MulFunCellView> mOpenViews;
        LayoutInflater mInflater;

        public ProgressAdpater() {

            mOpenViews = new ArrayList<MulFunCellView>();
            mInflater = LayoutInflater.from(ArrangementProgress.this);
        }

//        @Override
//        public int getCount() {
//            return mDataArray.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return mDataArray.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//
//            CellView cellView = null;
//            if (view == null) {
//                cellView = new CellView();
//                view = mInflater.inflate(R.layout.arrange_progress_cell_view, null);
//                cellView.mExMul = (MulFunCellView) view.findViewById(R.id.arrage_pro_cell_id_mul_head);
//                cellView.mMul = (MulFunCellView) view.findViewById(R.id.arrage_pro_cell_id_mul);
//                cellView.mConLayout = (LinearLayout) view.findViewById(R.id.arrage_pro_cell_id_con_layout);
//                cellView.mFunLayout = (LinearLayout) view.findViewById(R.id.arrage_pro_cell_id_fun_layout);
//                cellView.mDelete = (Button) view.findViewById(R.id.arrage_pro_cell_id_delete);
//                cellView.mCopy = (Button) view.findViewById(R.id.arrage_pro_cell_id_copy);
//                cellView.mExc = (Button) view.findViewById(R.id.arrage_pro_cell_id_exc);
//
//                cellView.mHead = (ImageView) view.findViewById(R.id.personnel_cell_id_head);
//                cellView.mNameText = (TextView) view.findViewById(R.id.personnel_cell_id_name);
//                cellView.mContentText = (TextView) view.findViewById(R.id.personnel_cell_id_content);
//                cellView.mTimeText = (TextView) view.findViewById(R.id.personnel_cell_id_date);
//                cellView.mFileCountText = (TextView) view.findViewById(R.id.personnel_cell_id_att_count);
//                cellView.mPngCountText = (TextView) view.findViewById(R.id.personnel_cell_id_png_count);
//                cellView.mFileLayout = (LinearLayout) view.findViewById(R.id.personnel_cell_id_file_layout);
//                cellView.mAttLayout = (RelativeLayout) view.findViewById(R.id.personnel_cell_id_att_layout);
//                cellView.mPngLayout = (RelativeLayout) view.findViewById(R.id.personnel_cell_id_png_layout);
//
//                cellView.mConLayout.setOnTouchListener(onConLayoutTouch);
//                cellView.mMul.setOnMulFunListener(onMulFunListener);
//                cellView.mExMul.setOnMulFunListener(onMulFunListener);
//
//                view.setTag(cellView);
//            } else {
//                cellView = (CellView) view.getTag();
//            }
//
//            mDataArray.get(i).setView(cellView);
//            return view;
//        }

        private void closeFun() {
            for (int i = 0; i < mOpenViews.size(); i++) {
                mOpenViews.get(i).snapToScreen(0);
            }
            mOpenViews.clear();
        }

        View.OnTouchListener onConLayoutTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        closeFun();
                        break;
                }
                return false;
            }
        };

        MulFunCellView.OnMulFunListener onMulFunListener = new MulFunCellView.OnMulFunListener() {
            @Override
            public void openModeChanged(MulFunCellView v, Boolean open) {
                if (open) {
                    mOpenViews.add(v);
                }
            }
        };

        @Override
        public int getGroupCount() {
            int groupCount = mGroupList.size() > 0 ? mGroupList.size() : 0;
            return groupCount;
        }

        @Override
        public int getChildrenCount(int i) {
            int childCount = mGroupList.get(i).size() > 0 ?mGroupList.get(i).size() : 0;
            return childCount;
        }

        @Override
        public Object getGroup(int i) {
            return mGroupList.get(i);
        }

        @Override
        public Object getChild(int i, int i2) {
            return mGroupList.get(i).get(i2);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i2) {
            return i2;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            GroupHolder groupHolder;
            TextView groupTextView;
            if (view == null){
                groupHolder = new GroupHolder();
                view = LayoutInflater.from(ArrangementProgress.this).inflate(R.layout.arrangement_personnel_group_view,null);
//                groupHolder.srcTextView = (TextView)view.findViewById(R.id.personnel_group_id_title);
                view.setTag(groupHolder);
            }else{
               groupHolder = (GroupHolder)view.getTag();
            }
//            groupHolder.srcTextView.setText("");
            return view;
        }

        @Override
        public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
            CellView cellView;
            if (view == null) {
                cellView = new CellView();
                view = mInflater.inflate(R.layout.arrange_progress_cell_view, null);
                cellView.mExMul = (MulFunCellView) view.findViewById(R.id.arrage_pro_cell_id_mul_head);
                cellView.mMul = (MulFunCellView) view.findViewById(R.id.arrage_pro_cell_id_mul);
                cellView.mConLayout = (LinearLayout) view.findViewById(R.id.arrage_pro_cell_id_con_layout);
                cellView.mFunLayout = (LinearLayout) view.findViewById(R.id.arrage_pro_cell_id_fun_layout);
                cellView.mDelete = (Button) view.findViewById(R.id.arrage_pro_cell_id_delete);
                cellView.mCopy = (Button) view.findViewById(R.id.arrage_pro_cell_id_copy);
                cellView.mExc = (Button) view.findViewById(R.id.arrage_pro_cell_id_exc);

                cellView.mHead = (ImageView) view.findViewById(R.id.personnel_cell_id_head);
                cellView.mNameText = (TextView) view.findViewById(R.id.personnel_cell_id_name);
                cellView.mContentText = (TextView) view.findViewById(R.id.personnel_cell_id_content);
                cellView.mTimeText = (TextView) view.findViewById(R.id.personnel_cell_id_date);
                cellView.mFileCountText = (TextView) view.findViewById(R.id.personnel_cell_id_att_count);
                cellView.mPngCountText = (TextView) view.findViewById(R.id.personnel_cell_id_png_count);
                cellView.mFileLayout = (LinearLayout) view.findViewById(R.id.personnel_cell_id_file_layout);
                cellView.mAttLayout = (RelativeLayout) view.findViewById(R.id.personnel_cell_id_att_layout);
                cellView.mPngLayout = (RelativeLayout) view.findViewById(R.id.personnel_cell_id_png_layout);

                cellView.mConLayout.setOnTouchListener(onConLayoutTouch);
                cellView.mMul.setOnMulFunListener(onMulFunListener);
                cellView.mExMul.setOnMulFunListener(onMulFunListener);

                view.setTag(cellView);
            } else {
                cellView = (CellView) view.getTag();
            }
            mGroupList.get(i).get(i2).setView(cellView);
            return view ;
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        protected class CellView {
            MulFunCellView mMul;
            MulFunCellView mExMul;
            LinearLayout mConLayout;
            LinearLayout mFunLayout;
            Button mDelete;
            Button mCopy;
            Button mExc;
            ImageView mHead;
            TextView mNameText;
            TextView mContentText;
            TextView mTimeText;
            TextView mFileCountText;
            TextView mPngCountText;
            LinearLayout mFileLayout;
            RelativeLayout mAttLayout;
            RelativeLayout mPngLayout;
        }
        protected class GroupHolder{
            TextView srcTextView;
        }
    }
    //组的View
}