package com.miicaa.home.ui.announcement;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;

public class AnnounceAdapter extends BaseAdapter {

     


     ArrayList<JSONObject> jsdata;
    
     boolean isAcessory;
     Context mContext;

     LayoutInflater layoutInflater;

  

     public AnnounceAdapter(Context context,ArrayList<JSONObject> data) {

  	   jsdata = new ArrayList<JSONObject>();
         jsdata.addAll(data);
         this.mContext = context;
         
         layoutInflater = LayoutInflater.from(context);
         
            
     }
     
     public void refresh(ArrayList<JSONObject> data){
  	   this.jsdata.clear();
  	   jsdata.addAll(data);
  	   this.notifyDataSetChanged();
     }


     private void initDataJson(ViewHolder holder,int position) throws Exception {
         {

             JSONObject jsonObject = jsdata.get(position);
            
                
                 holder.mTitleText.setText(jsonObject.optString("publishUserName"));
                
                 holder.mDateText.setText(DateHelper.getShowUpdateDate(jsonObject.optLong("publishDate")));
                 holder.mMonoText.setText(jsonObject.optString("title"));

                 

                 String creatorCode = jsonObject.optString("publishUserCode");
                 Tools.setHeadImg(creatorCode,holder.mHeadImg);
                 long start = jsonObject.optLong("publishDate");
                 long end = jsonObject.optLong("endDate", 0);
                 long current = new Date().getTime();
                 if(current>start&&current<end){
                	 holder.mContentText.setVisibility(View.VISIBLE); 
                 }else{
                	 holder.mContentText.setVisibility(View.GONE);
                 }
                if(jsonObject.isNull("readedId")){
                	 holder.mWeakImg.setVisibility(View.VISIBLE);
                }else{
                	 holder.mWeakImg.setVisibility(View.GONE);
                }
                 if (!jsonObject.isNull("attachList")&&jsonObject.optJSONArray("attachList").length()>0) {
                    
                         holder.mAccessoryImg.setVisibility(View.VISIBLE);
                     }else {
                         holder.mAccessoryImg.setVisibility(View.GONE);
                     }
                 

            

         }
     }
     @Override
     public int getCount() {
         return jsdata.size();
     
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
     public View getView( final int position, View view, ViewGroup viewGroup) {
         ViewHolder holder = new ViewHolder();
       
         if(view == null) {
             view = layoutInflater.inflate(R.layout.announcement_item_view, null);
             view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.matter_do_cell_selector));
            
             holder.mHeadImg = (ImageView) view.findViewById(R.id.matter_cell_id_head);
             holder.mWeakImg = (ImageView) view.findViewById(R.id.matter_cell_id_weak_notify);
             holder.mCountText = (TextView) view.findViewById(R.id.matter_cell_id_count);
             holder.mTitleText = (TextView) view.findViewById(R.id.matter_cell_id_title);
             holder.mDateText = (TextView) view.findViewById(R.id.matter_cell_id_time);
             holder.mMonoText = (TextView) view.findViewById(R.id.matter_cell_id_momo);
             holder.mContentText = (TextView) view.findViewById(R.id.matter_cell_id_content);
            
             holder.mAccessoryImg = (ImageView) view.findViewById(R.id.matter_cell_id_accessory);
             view.setTag(holder);
         }
         else {
             holder =(ViewHolder) view.getTag();
         }
         try {
             initDataJson(holder,position);
                

         }catch (Exception e){
             e.printStackTrace();
         }  
         view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                AnnouncementDetailActivity_.intent(mContext)
                .jsonStr(jsdata.get(position).toString())
                .startForResult(1);;
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

         ImageView mAccessoryImg;

       
     }
}