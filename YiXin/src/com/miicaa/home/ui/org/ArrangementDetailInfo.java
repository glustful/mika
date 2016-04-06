package com.miicaa.home.ui.org;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.detail.MatterDetailAcrtivity;
import com.miicaa.detail.MatterDetailAcrtivity_;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.business.matter.MatterInfo;
import com.miicaa.home.data.business.matter.TodoInfo;
import com.miicaa.home.ui.calendar.FramCalendarActivity;
import com.miicaa.home.ui.common.accessory.AccessoryFileListActivity;
import com.miicaa.home.ui.picture.BorwsePicture_;
import com.miicaa.home.ui.picture.PictureHelper;
import com.miicaa.home.ui.picture.PictureHelper.FirstPictureLoadListener;
import com.miicaa.home.ui.picture.PictureHelper.OnFirstPicureListener;
import com.miicaa.home.ui.report.ReportDetailActivity_;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.FileUtils;
import com.yxst.epic.yixin.view.TextViewPartColor;

/**
 * Created by Administrator on 14-1-8.
 */
public class ArrangementDetailInfo implements Serializable
{	
	static  String TAG = "ArrangementDetailInfo";
	
	private static final long serialVersionUID = 166748137723341446L;
	private View mRootView;
    LableGroup mViewGroup;

    
    LinearLayout trendsLayout;
    ImageView mHeadImg;
    ImageView mToImg;
    TextView mUserText;
    TextView mPersonCountText;
    TextView mArrgTypeText;
    LinearLayout mLockLayout;
    LinearLayout mRepeatLayout;
    LinearLayout copyPerson;
    TextView mStateText;
    TextView mMonoText;
    TextView mTitleText;
    TextView mFinisText;
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
    ImageView mFile;
    ImageView bigZhang;//大章
    ImageView mSecretLock;
    LinearLayout mParentTask;
    LinearLayout mChildTask;
    TextView mParentTaskTitle;
    TextView mChildTaskCount;
    TextViewPartColor reportTitle;
    MatterInfo mMatInfo = null;
    TodoInfo mTodoInfo = null;
    ArrayList<AccessoryInfo> mAccDatas = null;
    ArrayList<String> pictureFids;

    Context mContext;

    String mId;
    public ArrangementDetailInfo(Context context,String id)
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

        mRootView = inflater.inflate(R.layout.detail_cell_view,null);

        mHeadImg = (ImageView) mRootView.findViewById(R.id.detail_id_head);
        mUserText= (TextView) mRootView.findViewById(R.id.detail_id_name);
        mToImg = (ImageView)mRootView.findViewById(R.id.detail_id_person_img);
        
        reportTitle = (TextViewPartColor) mRootView.findViewById(R.id.detail_id_reportTitle);
        reportTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ReportDetailActivity_.intent(mContext)
				.dataId(mMatInfo.getreportId())
				.isDiscover(false)
				.isPushMessage(false)
				.start();
				
			}
		});
        copyPerson = (LinearLayout)mRootView.findViewById(R.id.copyperson);
        
        copyPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(view.getContext(),ArrangementPersonnel.class);
                Bundle bundle = new Bundle();
                bundle.putString("dataId",mId);
                intent.putExtra("bundle", bundle);
                view.getContext().startActivity(intent);
                ((Activity) view.getContext()).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
            }
        });
        trendsLayout = (LinearLayout)mRootView.findViewById(R.id.trendsState);
        mViewGroup = (LableGroup)mRootView.findViewById(R.id.detail_cell_lable_group);
        mPersonCountText= (TextView) mRootView.findViewById(R.id.detail_id_person_count);
        mArrgTypeText= (TextView) mRootView.findViewById(R.id.detail_id_type);
        mLockLayout= (LinearLayout) mRootView.findViewById(R.id.detail_id_lock_layout);
        mRepeatLayout= (LinearLayout) mRootView.findViewById(R.id.detail_id_repeat_layout);
        mStateText= (TextView) mRootView.findViewById(R.id.detail_id_state);
        mMonoText= (TextView) mRootView.findViewById(R.id.detail_id_momo);
        mTitleText=(TextView) mRootView.findViewById(R.id.detail_id_title);
        mFinisText= (TextView) mRootView.findViewById(R.id.detail_id_finish_time);
        mPlanText= (TextView) mRootView.findViewById(R.id.detail_id_plan_time);
        mNoticeText= (TextView) mRootView.findViewById(R.id.detail_id_notice_time);
        mAttachmentLayout= (LinearLayout) mRootView.findViewById(R.id.detail_id_attachment_layout);
        mPhotoLayout= (RelativeLayout) mRootView.findViewById(R.id.detail_id_photo_layout);
        mPhotoCountText= (TextView) mRootView.findViewById(R.id.detail_id_photo_count);
        littlePhoto = (ImageView)mRootView.findViewById(R.id.detail_id_attachment_picture);
        mFileLayout= (RelativeLayout) mRootView.findViewById(R.id.detail_id_file_layout);
        mFileCountText= (TextView) mRootView.findViewById(R.id.detail_id_file_count);
        mFile = (ImageView)mRootView.findViewById(R.id.detail_id_attachment_file);
        mUpdateText= (TextView) mRootView.findViewById(R.id.detail_id_update_time);
        mSrcText= (TextView) mRootView.findViewById(R.id.detail_id_src);
        bigZhang = (ImageView)mRootView.findViewById(R.id.bigZhang);
        mSecretLock = (ImageView)mRootView.findViewById(R.id.secretLock);
        mParentTask = (LinearLayout) mRootView.findViewById(R.id.parent_task);
        mParentTaskTitle = (TextView) mRootView.findViewById(R.id.parent_task_title);
        mChildTask = (LinearLayout) mRootView.findViewById(R.id.child_task);
        mChildTaskCount = (TextView) mRootView.findViewById(R.id.child_task_count);
        mParentTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(mMatInfo.getParentTask());
					 MatterDetailAcrtivity_.intent(mContext)
		          	   .dataId(jsonObject.optString("id"))
		          	   .dataType(jsonObject.optString("dataType"))
		          	   .operateGroup(jsonObject.optString("operateGroup"))
		          	   .status(jsonObject.optString("status"))
		          	   .titleText(jsonObject.optString("title"))
		          	   .needFresh(true)
		          	   .startForResult(MatterDetailAcrtivity.SUBTASKREQUEST);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
        mChildTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(mContext, FramCalendarActivity.class);
				intent.putExtra("type", "10");
				intent.putExtra("parentId", mMatInfo.getId());
				intent.putExtra("dataType", mMatInfo.getDataType());
				intent.putExtra("arrangeType", mMatInfo.getArrangeType());
				((Activity)mContext).startActivityForResult(intent,MatterDetailAcrtivity.SUBTASKREQUEST);
			}
		});

        mLockLayout.setVisibility(View.GONE);
        mRepeatLayout.setVisibility(View.GONE);
        mAttachmentLayout.setVisibility(View.GONE);
        mPhotoLayout.setVisibility(View.GONE);
        mFileLayout.setVisibility(View.GONE);
        mFinisText.setVisibility(View.GONE);
        mPlanText.setVisibility(View.GONE);
        mNoticeText.setVisibility(View.GONE);
        mParentTask.setVisibility(View.GONE);
        mChildTask.setVisibility(View.GONE);
        reportTitle.setVisibility(View.GONE);
    }

    public void setMatterData(MatterInfo matInfo)
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
        String status = mMatInfo.getLastApproveStatus();
        if("2".equals(status)||"5".equals(status)){
        	bigZhang.setVisibility(View.VISIBLE);
        	bigZhang.setImageDrawable(mContext.getResources().getDrawable(R.drawable.matter_big_agree));
        }else if("1".equals(status)||"6".equals(status)){
        	bigZhang.setVisibility(View.VISIBLE);
        	bigZhang.setImageDrawable(mContext.getResources().getDrawable(R.drawable.matter_big_disagree));
        }else{
        	bigZhang.setVisibility(View.GONE);
        }

        mUserText.setText(mMatInfo.getCreatorName());
        mPersonCountText.setText(String.valueOf(mMatInfo.getPeopleNum()));
        if(mMatInfo.getContent()==null || mMatInfo.getContent().trim().equals("")){
            mMonoText.setVisibility(View.GONE);
        }else {
            mMonoText.setText("描述：\n" + mMatInfo.getContent());
            mMonoText.setVisibility(View.VISIBLE);
        }
        mTitleText.setText(mMatInfo.getTitle());
        Tools.setHeadImg(mMatInfo.getCreatorCode(),mHeadImg);
        mStateText.setText(mMatInfo.getTodoStatusStr());
        
        if(mMatInfo.getEndTime()!=null && getFinishDate(mMatInfo.getEndTime()).length() > 0&&"01".equals(mMatInfo.getTodoStatus()))
        {
            mFinisText.setText(getFinishDate(mMatInfo.getEndTime()));
            mFinisText.setVisibility(View.VISIBLE);
        }
        else
        {
            mFinisText.setVisibility(View.GONE);
        }
        if (mMatInfo.getPlanTime() != null && mMatInfo.getPlanTime()>0 && "01".equalsIgnoreCase(mMatInfo.getTodoStatus())){
        	StringBuilder sb = new StringBuilder();
        	sb.append("我的计划： ");
        	String beginTime = AllUtils.getnormalTime(mMatInfo.getPlanTime());
        	sb.append(beginTime);
        	
        	if(mMatInfo.getPlanTimeEnd() != null && mMatInfo.getPlanTimeEnd() > 0){
        		String endTime = AllUtils.getnormalTime(mMatInfo.getPlanTimeEnd());
        		sb.append(" ~ ");
        		sb.append(endTime);
        	}else{
        		Log.d(TAG, "planEndTime : ........."+"Null");
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
        if(getUpdateDate(mMatInfo.getLastUpdateTime()).length() > 0)
        {
            mUpdateText.setText(getUpdateDate(mMatInfo.getCreateTime()));
        }
        else
        {
            mUpdateText.setVisibility(View.GONE);
        }

        mSrcText.setText("来源:" +mMatInfo.getClientName() +" " +(mMatInfo.getSrcName().equals("安排") ? "任务" 
        		: mMatInfo.getSrcName()));
        if(mMatInfo.getDataType().equals("1"))
        {
            if(mMatInfo.getArrangeType().equals("1"))
            {
                mArrgTypeText.setText("公事");
            }
            else
            {
                mArrgTypeText.setText("私事");
                mLockLayout.setVisibility(View.VISIBLE);
                copyPerson.setVisibility(View.GONE);
            }
            if(mMatInfo.getSecrecy().equals("1"))
            {
                mLockLayout.setVisibility(View.VISIBLE);
            }
            if(mMatInfo.getReportTitle()!=null&&!mMatInfo.getReportTitle().equals("")){
            	reportTitle.setEnd(mMatInfo.getReportTitle().length()+1);
            	reportTitle.setText("由"+mMatInfo.getReportTitle()+"创建");
            	reportTitle.setVisibility(View.VISIBLE);
            }else{
            	reportTitle.setVisibility(View.GONE);
            }
            if(mMatInfo.getChildTaskCount()>0){
            	mChildTask.setVisibility(View.VISIBLE);
            	 mChildTaskCount.setText(mMatInfo.getChildTaskCount()+"个");
            }
            if(mMatInfo.hasParentTask()){
            	mParentTask.setVisibility(View.VISIBLE);
            	try{
            	mParentTaskTitle.setText(new JSONObject(mMatInfo.getParentTask()).optString("title"));
            	}catch(Exception e){
            		
            	}
            }
           
        }
        else
        {
        	mParentTask.setVisibility(View.GONE);
        	mChildTask.setVisibility(View.GONE);
            mArrgTypeText.setText(mMatInfo.getApproveTypeName());
            mStateText.setText(mMatInfo.getTodoStatusStr());
        }
    }

    public String getRemindTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = format.format(date);
        return "提醒我："+dateStr;
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
                    mFinisText.setVisibility(View.VISIBLE);
                    mPlanText.setVisibility(View.VISIBLE);
                    mNoticeText.setVisibility(View.VISIBLE);

                }
                else
                {
                    mStateText.setText("待审核");
                    mFinisText.setVisibility(View.VISIBLE);
                    mPlanText.setVisibility(View.VISIBLE);
                    mNoticeText.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if(mMatInfo.getDataType().equals("1"))
                {
                	
                    mStateText.setText("已办");
                    mFinisText.setVisibility(View.GONE);
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
            return;
        }
        mAttachmentLayout.setVisibility(View.VISIBLE);//如果有附件就将附件的布局显示
        
        int pngCount = PictureHelper.getPictureCount(mAccDatas, new OnFirstPicureListener() {
			
			@Override
			public void beginLoadPic( ArrayList<String> fids) {
				final String fid = fids.get(0);
				pictureFids = fids;
				Bitmap bitmap = FileUtils.geInstance().getLittleImg(fid);
				Log.d(TAG, "out of the bitmap is "+bitmap);
				if(bitmap != null){
					Log.d(TAG, "bitmap is"+ bitmap);
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
        }

        if((mAccDatas.size() - pngCount) > 0)
        {
            mFileLayout.setVisibility(View.VISIBLE);
            mFileCountText.setText(String.valueOf(mAccDatas.size() - pngCount));
            mFileLayout.setOnClickListener(mFileListener);

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
            String str = "已超期" +String.valueOf(0 - offday) + "天";
            int spstart = spbuilder.length()+3;
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
