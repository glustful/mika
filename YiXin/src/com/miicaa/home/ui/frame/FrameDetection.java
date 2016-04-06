package com.miicaa.home.ui.frame;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.miicaa.base.RoundImage.CircularImage;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.EntirpiseInfo;
import com.miicaa.home.provider.EnterpiceProvider;
import com.miicaa.home.ui.announcement.AnnouncementActivity_;
import com.miicaa.home.ui.business.file.BusinessFileActivity;
import com.miicaa.home.ui.calendar.DitectionCalendar;
import com.miicaa.home.ui.checkwork.CheckWorkActivity_;
import com.miicaa.home.ui.detection.DitectionMatter;
import com.miicaa.home.ui.discover.DiscoverActivity;
import com.miicaa.home.ui.discover.DiscoverActivity_;
import com.miicaa.home.ui.discover.DiscoverStateFactory;
import com.miicaa.home.ui.enterprise.EnterpriceMainActivity_;
import com.miicaa.home.ui.enterprise.EnterpriseLocation;
//import com.miicaa.home.ui.enterprise.EnterpriceMainActivity;
//import com.miicaa.home.ui.enterprise.EnterpriceMainActivity_;
import com.miicaa.home.ui.org.ArragementLabEdit;

/**
 * Created by Administrator on 13-11-25.	
 */
public class FrameDetection implements IFrameChild
{
	private static String TAG = "FrameDetection";
	
    Context mContext;
    View mRootView;
    FrameLayout announcement_lastUser;
    CircularImage announcement_lastUser_img;
    LayoutInflater mInflater;
    View additiveView;

    @SuppressWarnings("deprecation")
	public FrameDetection(Context context)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        initUI();
    }
    @Override
    public View getRootView()
    {
        return mRootView;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void setMsg(String msg) {

    }

    public void setEnterprise(){
    	int locationType = EnterpriseLocation.getInstance().getLocationType();
   if(locationType == EnterpriseLocation.YunanReadedSuccess
		   ||locationType == EnterpriseLocation.YunnanNoReaded
		   ||locationType == EnterpriseLocation.YunnanReadedCancel){
          additiveView.setVisibility(View.VISIBLE);
    }else{
          	additiveView.setVisibility(View.GONE);
      }
    }
    
    private void initUI()
    {
        mRootView = mInflater.inflate(R.layout.frame_detection_activity,null);
        announcement_lastUser = (FrameLayout) mRootView.findViewById(R.id.last_user);
        announcement_lastUser_img = (CircularImage) mRootView.findViewById(R.id.last_user_img);
        announcement_lastUser.setVisibility(View.INVISIBLE);
        additiveView = mRootView.findViewById(R.id.frame_detect_id_enterprice);
        Button buttonArrange = (Button) mRootView.findViewById(R.id.frame_detect_id_orrange);
        buttonArrange.setOnClickListener(onArrangeClick);
        Button buttonApprove = (Button)mRootView.findViewById(R.id.frame_detect_id_approval);
        buttonApprove.setOnClickListener(onAppovalClick);
        //全公司的
        Button buttonNotOver = (Button)mRootView.findViewById(R.id.frame_detect_id_notover);
        buttonNotOver.setOnClickListener(onNotOverClick);
        Button buttonOver = (Button)mRootView.findViewById(R.id.frame_detect_id_over);
        buttonOver.setOnClickListener(onOverClick);
        Button buttonCal = (Button) mRootView.findViewById(R.id.frame_detect_id_calendar);
        buttonCal.setOnClickListener(onCalendarClick);
        Button buttonBusFile = (Button) mRootView.findViewById(R.id.frame_detect_id_businesssfile);
        buttonBusFile.setOnClickListener(onBusinesssFileClick);
        /*工作报告*/
        mRootView.findViewById(R.id.frame_detect_id_report).setOnClickListener(onReportClick);
        //CRM
        mRootView.findViewById(R.id.frame_detect_id_crm).setOnClickListener(onCRMClick);
        //我关注的
        Button buttonObserverNotOver = (Button)mRootView.findViewById(R.id.frame_detect_id_observer_notover);
        buttonObserverNotOver.setOnClickListener(onObserverNotOverClick);
        Button buttonObserverOver = (Button)mRootView.findViewById(R.id.frame_detect_id_observer_over);
        buttonObserverOver.setOnClickListener(onObserverOverClick);
        Button buttonCheckWork = (Button)mRootView.findViewById(R.id.frame_detect_id_checkwork);
        buttonCheckWork.setOnClickListener(onCheckWorkClick);
        
        Button label = (Button)mRootView.findViewById(R.id.label);
        label.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, ArragementLabEdit.class);
				mContext.startActivity(intent);
			}
		});
        
        //公告点击事件
        
        mRootView.findViewById(R.id.frame_detect_id_announcement).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AnnouncementActivity_.intent(mContext)
				.start();
				announcement_lastUser.setVisibility(View.INVISIBLE);
			}
		});
        
      //企业服务点击事件
        mRootView.findViewById(R.id.frame_detect_id_enterprice).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EnterpriceMainActivity_.intent(mContext)
				.start();				
			}
		});
    }

    View.OnClickListener onArrangeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DitectionMatter.class);
            Bundle bundle = new Bundle();
            bundle.putString("type","arrangement");
            intent.putExtra("bundle", bundle);
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onAppovalClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DitectionMatter.class);
            Bundle bundle = new Bundle();
            bundle.putString("type","appoval");
            intent.putExtra("bundle", bundle);
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };
    View.OnClickListener onNotOverClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        	Intent intent = new Intent(mContext, DitectionMatter.class);
            Bundle bundle = new Bundle();
            bundle.putString("type","notOver");
            intent.putExtra("bundle", bundle);
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };
    View.OnClickListener onCalendarClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DitectionCalendar.class);
           
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };
    
    View.OnClickListener onBusinesssFileClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, BusinessFileActivity.class);
           
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };
    View.OnClickListener onOverClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DitectionMatter.class);
            Bundle bundle = new Bundle();
            bundle.putString("type","over");
            intent.putExtra("bundle", bundle);
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };
    View.OnClickListener onReportClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			DiscoverActivity_.intent(mContext)
			.discoverType(DiscoverStateFactory.DAILY_WORK)
			.start();
		}
	};
	
	 View.OnClickListener onCRMClick = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DiscoverActivity_.intent(mContext)
				.discoverType(DiscoverStateFactory.CRM)
				.start();
			}
		};

    View.OnClickListener onObserverNotOverClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DitectionMatter.class);
            Bundle bundle = new Bundle();
            bundle.putString("type","observerNotOver");
            intent.putExtra("bundle", bundle);
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };
    View.OnClickListener onObserverOverClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DitectionMatter.class);
            Bundle bundle = new Bundle();
            bundle.putString("type","observerOver");
            intent.putExtra("bundle", bundle);
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };
    View.OnClickListener onCheckWorkClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CheckWorkActivity_.intent(mContext)
			.start();
		}
	};

	public void setAnnouncementNotify(JSONObject object) {
		EntirpiseInfo entirpiseInfo = new EntirpiseInfo();
		entirpiseInfo.userCode = AccountInfo.instance().getLastUserInfo().getCode();
		entirpiseInfo.userEmail = AccountInfo.instance().getLastUserInfo().getEmail();
		entirpiseInfo.locationType = EnterpriseLocation.getInstance().getLocationType();
		if(object==null){
			entirpiseInfo.gonggaoCount = -1;
			announcement_lastUser.setVisibility(View.INVISIBLE);
		}else{
			entirpiseInfo.gonggaoCount = 1;
		announcement_lastUser.setVisibility(View.VISIBLE);
		Tools.setHeadImgWithoutClick(object.optString("publishUserCode"), announcement_lastUser_img);
		}
		EnterpriseLocation.getInstance().setGonggaoCount(entirpiseInfo.gonggaoCount);
		EntirpiseInfo.saveOrUpdateGonggao(mContext, entirpiseInfo);
		mContext.getContentResolver().notifyChange(EnterpiceProvider.CONTENT_URI, null);
	}
	
	

}
