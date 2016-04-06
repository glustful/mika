package com.miicaa.home.ui.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.detail.MatterDetailAcrtivity_;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.business.matter.TodoInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.common.accessory.AccessoryFileListActivity;
import com.miicaa.home.ui.home.MatterCell;
import com.miicaa.home.ui.org.ArrangementPersonnel;
import com.miicaa.home.ui.org.LableGroup;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.picture.BorwsePicture_;
import com.miicaa.home.ui.picture.PictureHelper;
import com.miicaa.home.ui.picture.PictureHelper.FirstPictureLoadListener;
import com.miicaa.home.ui.picture.PictureHelper.OnFirstPicureListener;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.FileUtils;

public class ReportDetailContentView {
	private View mRootView;
    LableGroup mViewGroup;

    LinearLayout container;
    LinearLayout trendsLayout;
    ImageView mHeadImg;
    ImageView mToImg;
    TextView mUserText;
    TextView mPersonNameText;
    TextView mArrgTypeText;
    LinearLayout mLockLayout;
    LinearLayout mRepeatLayout;
    LinearLayout copyPerson;
    TextView mStateText;
  
    TextView mTitleText;
    TextView mDesc;
    
    TextView mPlanText;
    TextView mNoticeText;
    LinearLayout mAttachmentLayout;
    RelativeLayout mPhotoLayout;
    ImageView littlePhoto;
    TextView mPhotoCountText;
    RelativeLayout mFileLayout;
    TextView mFileCountText;
    TextView mUpdateText;
    TextView mSrcText;
    //ImageView mPhoto;
    ImageView mFile;
   
    ReportDetailInfo mMatInfo = null;
    TodoInfo mTodoInfo = null;
    ArrayList<AccessoryInfo> mAccDatas = null;
    ArrayList<String> pictureFids;

    Context mContext;

    String mId;
    public ReportDetailContentView(Context context,String id)
    {
        mContext = context;
        initUI(context);
        mId = id;
        pictureFids = new ArrayList<String>();
    }


    public View getView(){
    	return mRootView;
    }
    private void initUI(Context context )
    {


        LayoutInflater inflater = LayoutInflater.from(context);

        mRootView = inflater.inflate(R.layout.report_detail_content_view,null);
        container = (LinearLayout) mRootView.findViewById(R.id.report_detail_content_item);
        mHeadImg = (ImageView) mRootView.findViewById(R.id.detail_id_head);
        mUserText= (TextView) mRootView.findViewById(R.id.detail_id_name);
        mToImg = (ImageView)mRootView.findViewById(R.id.detail_id_person_img);
        
        copyPerson = (LinearLayout)mRootView.findViewById(R.id.copyperson);
        
        copyPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(view.getContext(),ArrangementPersonnel.class);
                Bundle bundle = new Bundle();
                bundle.putString("dataId",mId);
                bundle.putString("type", MatterCell.WORKREPORTTYPE);
                intent.putExtra("bundle", bundle);
                
                view.getContext().startActivity(intent);
                ((Activity) view.getContext()).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
            }
        });
        trendsLayout = (LinearLayout)mRootView.findViewById(R.id.trendsState);
        mViewGroup = (LableGroup)mRootView.findViewById(R.id.detail_cell_lable_group);
        mPersonNameText= (TextView) mRootView.findViewById(R.id.detail_id_person_name);
        mArrgTypeText= (TextView) mRootView.findViewById(R.id.detail_id_type);
        mLockLayout= (LinearLayout) mRootView.findViewById(R.id.detail_id_lock_layout);
        mRepeatLayout= (LinearLayout) mRootView.findViewById(R.id.detail_id_repeat_layout);
        mStateText= (TextView) mRootView.findViewById(R.id.detail_id_state);
        
        mTitleText=(TextView) mRootView.findViewById(R.id.detail_id_title);
        mDesc = (TextView) mRootView.findViewById(R.id.detail_id_desc);
        mPlanText= (TextView) mRootView.findViewById(R.id.detail_id_plan_time);
        mNoticeText= (TextView) mRootView.findViewById(R.id.detail_id_notice_time);
        mAttachmentLayout= (LinearLayout) mRootView.findViewById(R.id.detail_id_attachment_layout);
        mPhotoLayout= (RelativeLayout) mRootView.findViewById(R.id.detail_id_photo_layout);
        mPhotoCountText= (TextView) mRootView.findViewById(R.id.detail_id_photo_count);
        littlePhoto = (ImageView)mRootView.findViewById(R.id.detail_id_attachment_picture);
        //mPhoto = (ImageView)mRootView.findViewById(R.id.matter_editor_id_picture);
        mFileLayout= (RelativeLayout) mRootView.findViewById(R.id.detail_id_file_layout);
        mFileCountText= (TextView) mRootView.findViewById(R.id.detail_id_file_count);
        mFile = (ImageView)mRootView.findViewById(R.id.detail_id_attachment_file);
        mUpdateText= (TextView) mRootView.findViewById(R.id.detail_id_update_time);
        mSrcText= (TextView) mRootView.findViewById(R.id.detail_id_src);
       


        mLockLayout.setVisibility(View.GONE);
        mRepeatLayout.setVisibility(View.GONE);
        mAttachmentLayout.setVisibility(View.GONE);
        mPhotoLayout.setVisibility(View.GONE);
        mFileLayout.setVisibility(View.GONE);
        
        mPlanText.setVisibility(View.GONE);
        mNoticeText.setVisibility(View.GONE);
    }

    public void setMatterData(ReportDetailInfo matInfo)
    {
    	mViewGroup.removeAllViews();
        mMatInfo = matInfo;

        appendMatter();
    }

    public void setAttachmentData(ArrayList<AccessoryInfo> accDatas)
    {
        mAccDatas = accDatas;
        appendAttachment();
    }
    
    public  int getAttCount(){
    	return mAccDatas != null ? mAccDatas.size() : 0;
    }

    @SuppressWarnings("deprecation")
	public void setLabelGroup(String label){
       View linearLayout = AllUtils.getLabelView(mContext, label);
        mViewGroup.addView(linearLayout);
    }

    public void appendMatter()
    {
        if(mMatInfo == null)
        {
            return;
        }

        if (mMatInfo.getLabels() != null && mMatInfo.getLabels().size() > 0){
            for (int i = 0; i<mMatInfo.getLabels().size();i++){
                String s = mMatInfo.getLabels().get(i);
                setLabelGroup(s);
            }
        }
        

        mUserText.setText(mMatInfo.getCreatorName());
        String name = "";
       JSONArray arr;
	try {
		arr = new JSONArray(mMatInfo.getCommenter());
		mPersonNameText.setText(arr.length()+"");
       
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		mPersonNameText.setText("0");
	}
       
       
        mTitleText.setText(mMatInfo.getTitle());
        if(mMatInfo.getDesc()==null||mMatInfo.getDesc().equals(""))
        	mDesc.setVisibility(View.GONE);
        else{
        	mDesc.setVisibility(View.VISIBLE);
        mDesc.setText("备注：\n"+mMatInfo.getDesc());
        }
        Tools.setHeadImg(mMatInfo.getCreatorCode(),mHeadImg);
        mStateText.setText(mMatInfo.getTodoStatusStr());
        mArrgTypeText.setText(mMatInfo.getReportType());
       
        if (mMatInfo.getPlanTime() != null && mMatInfo.getPlanTime()>0 && "01".equalsIgnoreCase(mMatInfo.getTodoStatus())){
        	StringBuilder sb = new StringBuilder();
        	sb.append("我的计划: ");
        	String beginTime = AllUtils.getnormalTime(mMatInfo.getPlanTime());
        	sb.append(beginTime);
        	
        	if(mMatInfo.getPlanTimeEnd() != null && mMatInfo.getPlanTimeEnd() > 0){
        		String endTime = AllUtils.getnormalTime(mMatInfo.getPlanTimeEnd());
        		sb.append(" ~ ");
        		sb.append(endTime);
        	}else{
        		//Log.d(TAG, "planEndTime : ........."+"Null");
        	}
        	mPlanText.setText(sb);
            mPlanText.setVisibility(View.VISIBLE);
        }else {
            mPlanText.setVisibility(View.GONE);
        }
        if (mMatInfo.getRemindTime() != null && getRemindTime(mMatInfo.getRemindTime()).length()>0 && "01".equalsIgnoreCase(mMatInfo.getTodoStatus())){
            mNoticeText.setText(getRemindTime(mMatInfo.getRemindTime()));
            mNoticeText.setVisibility(View.VISIBLE);
        }else{
            mNoticeText.setVisibility(View.GONE);
        }
        if(getUpdateDate(mMatInfo.getCreateTime()).length() > 0)
        {
            mUpdateText.setText("创建时间："+getUpdateDate(mMatInfo.getCreateTime()));
        }
        else
        {
            mUpdateText.setVisibility(View.GONE);
        }

        mSrcText.setText("来源:" +mMatInfo.getClientName()+" " + mMatInfo.getSrcName());
       
            
            mStateText.setText(mMatInfo.getTodoStatusStr());
            if(mMatInfo.getRange().equals("1")){
            	mLockLayout.setVisibility(View.VISIBLE);
            }else{
            	mLockLayout.setVisibility(View.GONE);
            }
        
        	 addList(mMatInfo);
        
    }
    
   
    private void addList(ReportDetailInfo mMatInfo) {
		
		container.removeAllViews();
		if(mMatInfo.getTodayList().size()>0){
			TextView title = new TextView(mContext);
			title.setBackgroundColor(Color.WHITE);
			switch (ReportUtils.convertType(mMatInfo.getReportType(1))) {
			case WorkReportActivity.REPORT_DAY:
				title.setText("工作总结("+PayUtils.formatData("yyyy.MM.dd", mMatInfo.getSummriazeStartTime())+")：");
				
				break;
			case WorkReportActivity.REPORT_WEEK:
				Calendar ca = Calendar.getInstance();
				ca.setTimeInMillis(mMatInfo.getSummriazeStartTime());
				title.setText("工作总结("+ReportUtils.getCurrentWeekEntity(ca)+")：");
				
				break;
			case WorkReportActivity.REPORT_MONTH:
				title.setText("工作总结("+PayUtils.formatData("yyyy.MM", mMatInfo.getSummriazeStartTime())+")：");
				
				break;
			case WorkReportActivity.REPORT_CUSTOM:
				title.setText("工作总结("+PayUtils.formatData("yyyy.MM.dd", mMatInfo.getSummriazeStartTime())+"~"+PayUtils.formatData("yyyy.MM.dd", mMatInfo.getSummriazeEndTime())+")：");
				
				break;
			}
			container.addView(title,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			for(int i=0;i<mMatInfo.getTodayList().size();i++){
				View v = LayoutInflater.from(mContext).inflate(R.layout.report_cell_view, null);
				initListItem(v,mMatInfo.getTodayList().get(i));
				container.addView(v);
			}
		}
		
		if(mMatInfo.getTomorrowList().size()>0){
			TextView title = new TextView(mContext);
			title.setBackgroundColor(Color.WHITE);
			switch (ReportUtils.convertType(mMatInfo.getReportType(1))) {
			case WorkReportActivity.REPORT_DAY:
				title.setText("工作计划("+PayUtils.formatData("yyyy.MM.dd", mMatInfo.gettomorrowStartTime())+")：");
				
				break;
			case WorkReportActivity.REPORT_WEEK:
				Calendar ca = Calendar.getInstance();
				ca.setTimeInMillis(mMatInfo.getTomorrowEndTime());
				title.setText("工作计划("+ReportUtils.getCurrentWeekEntity(ca)+")：");
				
				break;
			case WorkReportActivity.REPORT_MONTH:
				title.setText("工作计划("+PayUtils.formatData("yyyy.MM", mMatInfo.gettomorrowStartTime())+")：");
				
				break;
			case WorkReportActivity.REPORT_CUSTOM:
				title.setText("工作计划("+PayUtils.formatData("yyyy.MM.dd", mMatInfo.gettomorrowStartTime())+"~"+PayUtils.formatData("yyyy.MM.dd", mMatInfo.getTomorrowEndTime())+")：");
				
				break;
			}
			container.addView(title,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			for(int i=0;i<mMatInfo.getTomorrowList().size();i++){
				View v = LayoutInflater.from(mContext).inflate(R.layout.report_cell_view, null);
				initListItem(v,mMatInfo.getTomorrowList().get(i));
				container.addView(v);
			}
		}
	}


	private void initListItem(final View v, String json) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		v.setTag(jsonObject);
		
		TextView tv = (TextView) v.findViewById(R.id.title);
		tv.setVisibility(View.VISIBLE);
		tv.setText(jsonObject.optString("title"));
		if(jsonObject.isNull("workId")){
			tv.setTextColor(Color.BLACK);
		}else{
			tv.setTextColor(mContext.getResources().getColor(R.color.workrportblue));
		}
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(v.getTag()!=null&&v.getTag() instanceof JSONObject){
					final JSONObject jsonObject = (JSONObject) v.getTag();
					/*System.out.println(jsonObject.toString());
					if(jsonObject.isNull("workType")){
						PayUtils.showToast(mContext, "数据解析出错", 1000);
						return;
					}*/
					
					if(jsonObject.isNull("workType")||jsonObject.optString("workType").equals(MatterCell.WORKREPORTTYPE)){
						ReportItemDetailActivity_.intent(mContext)
						.jsonStr(v.getTag().toString())
						.isEdit(true)
						.isFinish(true)
						.info(mMatInfo)
						.startForResult(ReportDetailActivity.PLANREQUEST);
						return;
					}
					PayUtils.showDialog(mContext);
					String type = jsonObject.optString("workType");
					 String url = "/home/phone/thing/approveread";//审批
				        if ("1".equals(type)) {
				            url = "/home/phone/thing/arrangeread";//任务
				        }
				        new RequestAdpater() {
				            @Override
				            public void onReponse(ResponseData data) {//接收数据
				            	PayUtils.closeDialog();
				                if (data.getResultState() == ResponseData.ResultState.eSuccess) {//成功响应
				                	 MatterDetailAcrtivity_.intent(mContext)
				                	   .dataId(jsonObject.optString("workId"))
				                	   .dataType(jsonObject.optString("workType"))
				                	   
				                	   .startForResult(ReportDetailActivity.PLANREQUEST);
				                   
				                } else {
				                	PayUtils.showToast(mContext, data.getMsg(), 3000);
				                }
				            }

				            @Override
				            public void onProgress(ProgressMessage msg) {
				            }
				        }.setUrl(url)
				                .addParam("dataId", jsonObject.optString("workId"))
				                .notifyRequest();
					
				}
				
			}
		});
		ImageButton tip = (ImageButton) v.findViewById(R.id.tip);
		tip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(v.getTag()!=null&&v.getTag() instanceof JSONObject){
					
					ReportItemDetailActivity_.intent(mContext)
					.jsonStr(v.getTag().toString())
					.isEdit(true)
					.isFinish(true)
					.info(mMatInfo)
					
					.startForResult(ReportDetailActivity.PLANREQUEST);
					}else{
						PayUtils.showToast(mContext, "数据格式出错", 1000);
					}
				
			}
		});
		
	}


	public String getRemindTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = format.format(date);
        return "提醒我:"+dateStr;
    }

    public void appendTodo()
    {
        if(mTodoInfo == null)
        {
            return;
        }
        if(getPlanDate(mTodoInfo.getPlanTime()).length() > 0)
        {
            mPlanText.setText(getPlanDate(mTodoInfo.getPlanTime()));
        }
        else
        {
            mPlanText.setVisibility(View.GONE);
        }

        mNoticeText.setText(mTodoInfo.getRemindStr());
        switch (Integer.parseInt(mTodoInfo.getStatus()))
        {
            case 1:
                if(mMatInfo.getDataType().equals("1"))
                {
                    mStateText.setText("待办");
                   
                    mPlanText.setVisibility(View.VISIBLE);
                    mNoticeText.setVisibility(View.VISIBLE);

                }
                else
                {
                    mStateText.setText("待审核");
                    
                    mPlanText.setVisibility(View.VISIBLE);
                    mNoticeText.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if(mMatInfo.getDataType().equals("1"))
                {
                    mStateText.setText("已办");
                   
                    mPlanText.setVisibility(View.VISIBLE);
                    mNoticeText.setVisibility(View.VISIBLE);
                }
                else
                {
                    mStateText.setText("已审核");

                }
                break;
            case 3:
                if(mMatInfo.getDataType().equals("1"))
                {
                    mStateText.setText("已办");
                }
                else
                {
                    mStateText.setText("已审核");
                }
                break;
            case 4:
                if(mMatInfo.getDataType().equals("1"))
                {
                    mStateText.setText("已终止");
                }
                else
                {
                    mStateText.setText("已终止");
                }
                break;
        }

        if(mMatInfo.getDataType().equals("1"))
        {
            if(mMatInfo.getRepeatStr() != null && mMatInfo.getRepeatStr().length() > 0)
            {
                mRepeatLayout.setVisibility(View.VISIBLE);
            }
            if(mMatInfo.getSecrecy().equals("1"))
            {
                mLockLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void appendAttachment()
    {
        if(mAccDatas.size() == 0)
        {
        	mAttachmentLayout.setVisibility(View.GONE);
            return;
        }
        mAttachmentLayout.setVisibility(View.VISIBLE);//如果有附件就将附件的布局显示
        
        int pngCount = PictureHelper.getPictureCount(mAccDatas, new OnFirstPicureListener() {
			
			@Override
			public void beginLoadPic( ArrayList<String> fids) {
				final String fid = fids.get(0);
				pictureFids = fids;
				Bitmap bitmap = FileUtils.geInstance().getLittleImg(fid);
			//	Log.d(TAG, "out of the bitmap is "+bitmap);
				if(bitmap != null){
					//Log.d(TAG, "bitmap is"+ bitmap);
					littlePhoto.setImageBitmap(bitmap);
					mPhotoLayout.setVisibility(View.VISIBLE);
				}
				else{
				PictureHelper.requestFirstPic(fid, new FirstPictureLoadListener() {
					@Override
					public void loadPic(Bitmap map) {
						if(map != null){
							FileUtils.geInstance().saveLittleBmp(map, fid);
							littlePhoto.setImageBitmap(map);
							mPhotoLayout.setVisibility(View.VISIBLE);
							}
						}
					});
				}
				}
			});
        
        if(pngCount > 0)
        {
            
            mPhotoCountText.setText(String.valueOf(pngCount));
            mPhotoLayout.setOnClickListener(mPhotoListener);
        }else{
        	mPhotoLayout.setVisibility(View.GONE);
        }

        if((mAccDatas.size() - pngCount) > 0)
        {
            mFileLayout.setVisibility(View.VISIBLE);
            mFileCountText.setText(String.valueOf(mAccDatas.size() - pngCount));
            mFileLayout.setOnClickListener(mFileListener);

        }else{
        	mFileLayout.setVisibility(View.GONE);
        }
    }
    
    
    private String getUpdateDate(Date date)
    {
        if(date == null)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = format.format(date);
        String str = dateStr;

        return str;
    }

    private String getNoticeDate(Date date)
    {
        if(date == null)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = format.format(date);
        String str = "提醒我:"+dateStr;

        return str;
    }

    private String getPlanDate(Date date)
    {
        if(date == null)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = format.format(date);
        String str = "我的计划:"+dateStr;

        return str;
    }

    private CharSequence getFinishDate(Date date)
    {
        if(date == null)
            return "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        SpannableStringBuilder spbuilder = new SpannableStringBuilder("");


        Date today = new Date();
        long hasday = (date.getTime()-today.getTime())/1000;
        long between=(date.getTime()/(1000*24*3600))-(today.getTime()/(1000*24*3600));
//        int offday = (int)(between/(24*3600));
        long offday = between;
        long offsec = hasday%(24*3600);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = format.format(date);

        spbuilder.append("应于："+dateStr + "完成");
        spbuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),0,3,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if(between == 0 && offsec > 0)
        {
//            offday += 1;
        }

        spbuilder.append("(");
        int spstartBegin = spbuilder.length();
        spbuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), spstartBegin, spbuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if(offday >= 3)
        {
            String str = "剩余" +String.valueOf(offday) + "天";
            int spstart = spbuilder.length()+2;
            spbuilder.append(str);
            spbuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#00FF33")), spstart, spbuilder.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if(offday >= 2)
        {
            String str = String.valueOf(offday) + "天后到期";
            int spstart = spbuilder.length();
            spbuilder.append(str);
            spbuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#D5D52B")), spstart, spbuilder.length()-4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if(offday >= 1)
        {
            String str = "明天到期" ;
            int spstart = spbuilder.length();
            spbuilder.append(str);
            spbuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#D5D52B")), spstart, spbuilder.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if(offday >= 0)
        {
            String str = "今天到期" ;
            int spstart = spbuilder.length();
            spbuilder.append(str);
            spbuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#D5D52B")), spstart, spbuilder.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else
        {
            String str = "超期" +String.valueOf(0 - offday) + "天";
            int spstart = spbuilder.length()+2;
            spbuilder.append(str);
            spbuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), spstart, spbuilder.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        spbuilder.append(")");
        int spstartEnd = spbuilder.length();
        spbuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), spstartEnd, spbuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spbuilder;
    }
    View.OnClickListener mPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BorwsePicture_.intent(mContext)
            .fileIds(pictureFids)
            .mId(mId)
            .start();
        }
    };
    View.OnClickListener mFileListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(),AccessoryFileListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id",mId);
            intent.putExtra("bundle",bundle);
//            ArrangementDetailActivity.mSelf.startActivity(intent);
            mContext.startActivity(intent);
//            ArrangementDetailActivity.mSelf.overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
        }
    };
}
