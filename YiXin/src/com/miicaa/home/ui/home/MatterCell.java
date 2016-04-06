package com.miicaa.home.ui.home;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.Tools;
import com.miicaa.detail.MatterDetailAcrtivity_;
import com.miicaa.home.R;
import com.miicaa.home.ui.org.LableGroup;
import com.miicaa.home.ui.report.ReportDetailActivity_;
//import com.miicaa.home.ui.report.ReportDetailActivity_;
import com.miicaa.utils.AllUtils;


	 public class MatterCell extends BaseAdapter{
//       String mId;
//       String mConut;
//       String mTitle;
//       String mCode;
//       String mMono;
//       String mProgress;
//       String mDiscuss;
//       String mDynamic;
//       Date mTime;
       MatterType mType;
       public static final String WORKREPORTTYPE = "9";
       int mClass;
       int count ;
       int in = 0;
       int resId;
//       ContentValues cellData;

       ArrayList<JSONObject> jsdata;
       boolean isPlan;
       boolean isRemind;
       boolean isAcessory;
       Context mContext;

       LayoutInflater layoutInflater;

       //HashMap<Integer,Boolean> lableAddMap = new HashMap<Integer, Boolean>();

       public MatterCell(Context context,ArrayList<JSONObject> data,MatterType type) {
//           super(context,resousce,data);
    	   jsdata = new ArrayList<JSONObject>();
           jsdata.addAll(data);
           this.mContext = context;
           this.mType = type;
           layoutInflater = LayoutInflater.from(context);
           LableGroup saveLbaleGroup;
               //这个是什么东西不知道，先注释一下
//           if (mType != MatterType.eBulletin) {
//               if (cellData.getAsString(MatterInfoSql.col_name_data_type).equals("assessment")) {
//                   mClass = 1;
//               } else {
//                   mClass = 0;
//               }
//           }
       }
       
       public void refresh(ArrayList<JSONObject> data){
    	   this.jsdata.clear();
    	   jsdata.addAll(data);
    	   this.notifyDataSetChanged();
       }


       private void initDataJson(ViewHolder holder,int position) throws Exception {
           {
//               JSONObject jsonObject = getItem(position);
               JSONObject jsonObject = jsdata.get(position);
               if (mType == MatterType.eDo) {
                   //mCountText.setVisibility(View.VISIBLE);
                   //mCountText.setText(15);//这个东西是未读消息数，以后再弄
                   holder.mWeakImg.setVisibility(View.GONE);
                   holder.mTitleText.setText(jsonObject.optString("creatorName"));
                   String time = DateHelper.getShowUpdateDate(jsonObject.optLong("createTime"));
                   holder.mDateText.setText(DateHelper.getShowUpdateDate(jsonObject.optLong("createTime")));
                   holder.mMonoText.setText(jsonObject.optString("title"));
//                   mContentText.setText("描述：" + jsonData.optString("content"));
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

       @SuppressWarnings("deprecation")
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
                    	   String str = jsonObject.optJSONArray("labels").optJSONObject(j).optString("label");
                    	   linearLayout = (LinearLayout) AllUtils.getLabelView(mContext, str);
                           holder.lableGroup.addView(linearLayout);
                       }

                   }

           }catch (Exception e){
               e.printStackTrace();
           }  
           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (mType == MatterType.eDo || mType == MatterType.eDone) {
                	   if(jsonObject.optString("dataType").equals(WORKREPORTTYPE)){
                		   ReportDetailActivity_.intent(mContext)
                		   .dataId(jsonObject.optString("id"))
                		   .startForResult(100);
                	   }else{
                	   MatterDetailAcrtivity_.intent(mContext)
                	   .dataId(jsonObject.optString("id"))
                	   .dataType(jsonObject.optString("dataType"))
                	   .operateGroup(jsonObject.optString("operateGroup"))
                	   .status(jsonObject.optString("status"))
                	   .titleText(jsonObject.optString("title"))
                	   .isPushMessage(false)
                	   .needFresh(true)
                	   .startForResult(100);
                	   }

                   } else if (mType == MatterType.eBulletin) {
                       // 公告activity;
//                       Intent intent = new Intent(mContext, NoticeActivity.class);
//                       mContext.startActivity(intent);
//                       ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
                   }
               }
           });
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

