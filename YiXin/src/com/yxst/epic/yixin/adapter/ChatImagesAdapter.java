package com.yxst.epic.yixin.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.BitmapUtils;
import com.miicaa.home.R;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.db.DaoSession;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.db.MessageDao;
import com.yxst.epic.yixin.utils.XXBitmapUtils;
import com.yxst.epic.yixin.view.ChatItemImgsView;
import com.yxst.epic.yixin.view.ChatItemImgsView_;

@EBean
public class ChatImagesAdapter extends PagerAdapter {
	
	public static final String TAG = "ChatImagesAdapter";
	
	private Context context;
	
	private Cursor mCursor;
	
	private DaoSession daoSession;
	private MessageDao messageDao;
	
	private BitmapUtils bitmapUtils;
	
	private ViewPager mViewPager;
	
	private List<WeakReference<ChatItemImgsView>> viewList;

	public ChatImagesAdapter(Context context) {
		super();
		this.context = context;
		viewList = new ArrayList<WeakReference<ChatItemImgsView>>();
		daoSession = MyApplication.getDaoSession(context);
		messageDao = daoSession.getMessageDao();
		
		bitmapUtils = new XXBitmapUtils(context);
		
		bitmapUtils.configDefaultLoadingImage(new ColorDrawable(Color.parseColor("#33000000")));
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.card_photofail);
	}

	public void onPause() {
		bitmapUtils.pause();
	}
	
	public void onResume() {
		bitmapUtils.resume();
	}
	
	private Long getLastMessageExtTime(Cursor cursor) {
		if (cursor.move(-1)) {
			return cursor.getLong(MessageDao.Properties.ExtTime.ordinal);
		}
		return null;
	}
	
	public Message readEntity(Cursor cursor) {
		return messageDao.readEntity(cursor, 0);
	}
	
	public BitmapUtils getBitmapUtils() {
		return bitmapUtils;
	}

	public void setBitmapUtils(BitmapUtils bitmapUtils) {
		this.bitmapUtils = bitmapUtils;
	}

	@Override
	public int getCount() {
		if (mCursor != null) {
			return mCursor.getCount();
		}
		return 0;
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (mViewPager == null) {
			mViewPager = (ViewPager) container;
		}
		ChatItemImgsView view = null;
		if (viewList.size() > 0) {
			if (viewList.get(0) != null) {
				view = initView(viewList.get(0).get(), position);
				viewList.remove(0);
			}
		}
		view = initView(null, position);
		mViewPager.addView(view);
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ChatItemImgsView view = (ChatItemImgsView) object;
		container.removeView(view);
		viewList.add(new WeakReference<ChatItemImgsView>(view));
	}
	
	private ChatItemImgsView initView(View view, int position) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = ChatItemImgsView_.build(context);
			viewHolder.view = (ChatItemImgsView) view;
			viewHolder.view.setBitmapUtils(bitmapUtils);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		boolean success = mCursor.moveToPosition(position);
		if (success) {
			Message message = messageDao.readEntity(mCursor, 0);
			viewHolder.view.bind(message, getLastMessageExtTime(mCursor));
		}
		return (ChatItemImgsView) view;
	}
	
	private static class ViewHolder {
		ChatItemImgsView view;
	}

	@Override  
	public int getItemPosition(Object object) {  
		return POSITION_NONE;  
	}

	public void setCursor(Cursor cursor){
		this.mCursor = cursor;
	}

}
