package com.miicaa.home.ui.guidepage;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.miicaa.home.R;
import com.miicaa.home.ui.home.FragmentInfo;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.home.ui.login.HomeLoginActivity_;
import com.miicaa.service.VersionBroadCaseReceiver;
import com.miicaa.service.VersionPanduanService;
import com.miicaa.utils.AllUtils;

/**
 * Created by LM on 14-7-23.
 */
public class GuidePageActivity extends Activity {
    ViewPager mViewPager;
    ArrayList<FragmentInfo> fInfo;
    int isFirstRun = 0;
    /*
     * 2.0.0对应06
     */
    public final static int ncode = 23;
    private final static String GUIDE_INFO = "guideInfo";
    public final static String versionName = "2.0.4";
    MyPagerAdapter pagerAdapter;
    SharedPreferences sharedPreferences;
    List<ImageView> mBottomXiaoyuandian;//
    LinearLayout mBottomYuandianLayout;//
    Context mContext;
    VersionBroadCaseReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent sIntent = new Intent(this, VersionPanduanService.class);
        sIntent.putExtra("version", true);
        startService(sIntent);
        AllUtils.cancelAllNotification(this);
        mContext = this;
        fInfo = new ArrayList<FragmentInfo>();
        mBottomXiaoyuandian = new ArrayList<ImageView>();//初始化底部小圆点
        Boolean isLookAgain = getIntent().getBooleanExtra("reset",false);
        sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        isFirstRun = sharedPreferences.getInt(GUIDE_INFO, 0);
        setContentView(R.layout.guide_page_activity);
        mViewPager = (ViewPager)findViewById(R.id.guide_page_activity_viewpager);
        if(isFirstRun != ncode|| isLookAgain){
        	/*
        	 * 新用户安装或者返回查看此页的时候
        	 */
        	if(isFirstRun == 0){
//        		String mid = MidService.getMid(getApplicationContext());
//                StatConfig.setDebugEnable(true);
//                StatService.trackCustomEvent(GuidePageActivity.this,"onCreate","");
        	}
        	initBottom(6);
        }else if(isFirstRun == ncode){
        	 Intent intent = new Intent(GuidePageActivity.this, FramMainActivity.class);
             startActivity(intent);
             finish();
        }else{
        	/*
        	 * 用户更新
        	 */
//        	for(int count = 0 ; count < 4;count++){
//        		Bundle arg = new Bundle();
//        		arg.putInt("count", count);
//        		fInfo.add(new FragmentInfo(GuideNewFragment_.class, arg));
//        	}
//        	initBottom(4);
        }
        /*
         * 存入新版本的值
         */
        isFirst();
        pagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0 ; i < pagerAdapter.getCount() ; i++){
                    if (i == position){
                        ((ImageView)mBottomYuandianLayout.getChildAt(i)).setImageDrawable(getResources().getDrawable(R.drawable.page_focused));
                    }else{
                        ((ImageView)mBottomYuandianLayout.getChildAt(i)).setImageDrawable(getResources().getDrawable(R.drawable.page_unfocused));
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottom(int count){//初始化底部小圆点
        mBottomYuandianLayout = (LinearLayout)findViewById(R.id.guide_page_bottom);
        for (int i = 0; i < count; i++){
            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.chat_dot_wh),
                    (int)getResources().getDimension(R.dimen.chat_dot_wh));
            params.weight = 1;
            ImageView bottomIv = new ImageView(this);
//            params.leftMargin = R.dimen.chat_dot_margin_lr;
//            params.rightMargin = R.dimen.chat_dot_margin_lr;

            bottomIv.setLayoutParams(params);
            if (i == 0){
                bottomIv.setImageDrawable(getResources().getDrawable(R.drawable.page_focused));
            }else {
                bottomIv.setImageDrawable(getResources().getDrawable(R.drawable.page_unfocused));
            }

            mBottomXiaoyuandian.add(bottomIv);
            mBottomYuandianLayout.addView(bottomIv);
        }
    }


    @Override
    protected void onResume() {
       super.onResume();
    }

    @Override
    protected void onStart() {
    	 mReceiver = new VersionBroadCaseReceiver();
         IntentFilter intentFilter = new IntentFilter(AllUtils.version_reciver);
         registerReceiver(mReceiver, intentFilter);
        super.onStart();
    }
    
    

    @Override
	protected void onStop() {
    	unregisterReceiver(mReceiver);
		super.onStop();
	}

	private void isFirst(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(GUIDE_INFO,ncode);
        editor.commit();

    }


    
    
    
//    Boolean isUpdate(){
//    	Boolean isupdate = false;
//    	try {
//			 isupdate = isFirstRun != ncode && "2.0.0".equals(MyApplication.getInstance().getVersionCode());
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	return isupdate;
//    }
    
    @Override
	public void finish() {
		super.finish();
	}



	class MyPagerAdapter extends PagerAdapter{

		LayoutInflater inflater;
		public MyPagerAdapter() {
			inflater = LayoutInflater.from(GuidePageActivity.this);
		}
		
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
//			super.destroyItem(container, position, object);
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("InflateParams")
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
//			return super.instantiateItem(container, position);
			View convertView = inflater.inflate(R.layout.guide_page_fragment, null);
//			ImageView bgView = (ImageView) convertView.findViewById(R.id.guide_page_background);
			ImageView mFitView = (ImageView)convertView.findViewById(R.id.guidePageFitView);
			int viewResId = 0;
			int bgResId = 0;
			switch (position) {
			case 0:
				viewResId = R.drawable.guide_01;
				bgResId = R.drawable.guide_bg_01;
//				setImageDrawable(mFitView,R.drawable.guide_01);
//				setImageBackground(mFitView, R.drawable.guide_bg_01);
				break;
			case 1:
				viewResId = R.drawable.guide_02;
				bgResId = R.drawable.guide_bg_02;
//				setImageDrawable(mFitView,R.drawable.guide_02);
//				setImageBackground(mFitView, R.drawable.guide_bg_02);
				break;
			case 2:
				viewResId = R.drawable.guide_03;
				bgResId = R.drawable.guide_bg_03;
//				setImageDrawable(mFitView,R.drawable.guide_03);
//				setImageBackground(mFitView, R.drawable.guide_bg_03);
				break;
			case 3:
				viewResId = R.drawable.guide_04;
				bgResId = R.drawable.guide_bg_04;
//				setImageDrawable(mFitView,R.drawable.guide_04);
//				setImageBackground(mFitView, R.drawable.guide_bg_04);
				break;
			case 4:
				viewResId = R.drawable.guide_05;
				bgResId = R.drawable.guide_bg_05;
//				setImageDrawable(mFitView,R.drawable.guide_05);
//				setImageBackground(mFitView, R.drawable.guide_bg_05);
				break;
			case 5:
				viewResId = R.drawable.guide_06;
				bgResId = R.drawable.guide_bg_06;
//				setImageDrawable(mFitView,R.drawable.guide_06);
//				setImageBackground(mFitView, R.drawable.guide_bg_06);
				mFitView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						HomeLoginActivity_.intent(GuidePageActivity.this)
						.start();
						finish();
					}
				});
				break;

			default:
				throw new AndroidRuntimeException("maybe you add the view more??");
			}
			Resources resources = getResources();
			resources.getDrawable(viewResId);
			mFitView.setImageDrawable(resources.getDrawable(viewResId));
			mFitView.setBackgroundDrawable(resources.getDrawable(bgResId));
			container.addView(convertView, 0);
			return convertView;
			
		}

		@Override
		public int getCount() {
			return 6;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
		
		private void setImageDrawable(ImageView imageView,int resId){
			Resources resources = getResources();
			resources.getDrawable(resId);
			imageView.setImageDrawable(resources.getDrawable(resId));
		}
		
		private void setImageBackground(ImageView imageView,int resId){
			Resources resources = getResources();
			resources.getDrawable(resId);
			imageView.setImageDrawable(resources.getDrawable(resId));
		}
     
    }
}
