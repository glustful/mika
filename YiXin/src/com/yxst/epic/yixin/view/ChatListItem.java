package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.org.UserInfo;
import com.readystatesoftware.viewbadger.BadgeView;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.utils.MessageUtils;
import com.yxst.epic.yixin.utils.SmileyParser;

@EViewGroup(R.layout.list_item_chat_list)
public class ChatListItem extends RelativeLayout {

	public static final String TAG = "ChatListItem";

	@ViewById
	ImageView ivIcon;

	@ViewById(R.id.tvTagTo)
	TextView tvRemoteDisplayName;

	@ViewById
	TextView tvContent;

	@ViewById
	TextView tvTime;

	@ViewById
	BadgeView badgeView;
	
//	private BitmapUtils mBitmapUtils;
	
	@DimensionPixelOffsetRes(R.dimen.icon_size_bigger)
	int size;
	
	public ChatListItem(Context context) {
		super(context);
	}

	private final String TIME_ZERO = "0 分钟前";
	private final String TIME_NEAR = "刚刚";
	public void bind(Message message, int unreadCount) {
//		tvRemoteDisplayName.setText(message.getExtRemoteDisplayName());
		setupIcon(message.getExtRemoteUserName());
		String name = !TextUtils.isEmpty(message.getExtRemoteDisplayName()) ? message.getExtRemoteDisplayName() : message.getExtRemoteUserName();
		tvRemoteDisplayName.setText(name);

		tvContent.setText(SmileyParser.getInstance().addSmileySpans(
				message.getContent()));

		tvTime.setText(DateUtils.getRelativeTimeSpanString(
				MessageUtils.getMessageTime(message)/*message.getExtTime()*/, System.currentTimeMillis(),
				DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE));
		if(TIME_ZERO.equals(tvTime.getText().toString()))tvTime.setText(TIME_NEAR);;

		badgeView.setVisibility(unreadCount == 0 ? View.GONE : View.VISIBLE);
		badgeView.setText(String.valueOf(unreadCount));
		
		if (Member.isTypeQun(message.getExtRemoteUserName()) && message.getExtInOut() == DBMessage.INOUT_IN) {
			String rawContent = message.getContent();
			
			String realContent = getRealContent(rawContent);
			
			tvContent.setText(SmileyParser.getInstance().addSmileySpans(realContent));
		} else {
			tvContent.setText(SmileyParser.getInstance().addSmileySpans(
					message.getContent()));
		}
	}
	
	private void setupIcon(String userName) {
//		ivIcon.setImageResource(R.drawable.default_avatar);
		if (Member.isTypeUser(userName)) {
			ivIcon.setImageResource(R.drawable.an_user_head_large);
			String u = userName.substring(0,userName.indexOf("@"));
			Long uid = Long.parseLong(u);
			Log.d("TOKENTOUID", uid.toString());
			String usercode = UserInfo.findByIdForUserCode(uid);
			usercode = usercode == null?"t":usercode;
			Tools.setHeadImgWithoutClick(usercode, ivIcon);
		} else if (Member.isTypeQun(userName)) {
			ivIcon.setImageResource(R.drawable.addfriend_icon_qucikgroup);
		} else if (Member.isTypeApp(userName)) {
			ivIcon.setImageResource(R.drawable.addfriend_icon_rada);
		}
		
	}
//	
//	private void setupIcon(final String userName) {
////		Map<String, Object> map = new HashMap<String, Object>();
////		map.put("getStreamFromMember", true);
//		final Map<String, Object> params = new HashMap<String, Object>();
//		params.put("bt", true);
//		
//		if (Member.isTypeUser(userName)) {
////			mBitmapUtils.configDefaultLoadingImage(R.drawable.ic_default_avata);
////			mBitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_default_avata);
//			
////			ivIcon.setImageResource(R.drawable.ic_default_avata);
//			
////			mBitmapUtils.display(ivIcon, Utils.getAvataUrl(userName, size));
//			DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_default_avata)
//				.showImageForEmptyUri(R.drawable.ic_default_avata)
//				.showImageOnFail(R.drawable.ic_default_avata)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.displayer(new SimpleBitmapDisplayer())
//				.build();
//			final String originalUrl = Utils.getAvataUrl(userName, size);
//			ImageLoader.getInstance().displayImage(originalUrl, ivIcon, options, new SimpleImageLoadingListener() {
//				@Override
//				public void onLoadingComplete(String imageUri, View view,
//						Bitmap loadedImage) {
//					DisplayImageOptions dio = new DisplayImageOptions.Builder()
//						.cacheInMemory(false)
//						.cacheOnDisk(false)
////						.considerExifParams(true)
//						.displayer(new SimpleBitmapDisplayer())
//						.build();
//					
//					String btUrl = Utils.addQueryParams(originalUrl, params);
//					ImageLoader.getInstance().displayImage(btUrl, ivIcon, dio, new SimpleImageLoadingListener(){
//						@Override
//						public void onLoadingComplete(String imageUri, View view,
//								Bitmap loadedImage) {
//							if (loadedImage != null) {
//								Log.d(TAG, "onLoadingComplete() loadedImage:" + loadedImage);
//								ImageSize targetSize = defineTargetSizeForView(view, getMaxImageSize());
//								Log.d(TAG, "onLoadingComplete() targetSize:" + targetSize);
//								String memoryCacheKey = MemoryCacheUtils.generateKey(originalUrl, targetSize);
//								ImageLoader.getInstance().getMemoryCache().put(memoryCacheKey, loadedImage);
//								try {
//									ImageLoader.getInstance().getDiskCache().save(originalUrl, loadedImage);
//								} catch (IOException e) {
//									e.printStackTrace();
//								}
//							}
//						}
//					});
//				}
//			});
//			
//		} else if (Member.isTypeQun(userName)) {
////			ivIcon.setImageResource(R.drawable.ic_default_qun);
//			
//			DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_default_qun)
//				.showImageForEmptyUri(R.drawable.ic_default_qun)
//				.showImageOnFail(R.drawable.ic_default_qun)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
////				.considerExifParams(true)
//				.displayer(new SimpleBitmapDisplayer())
//				.build();
//			ImageLoader.getInstance().displayImage(null, ivIcon, options, null);
//		} else if (Member.isTypeApp(userName)) {
////			mBitmapUtils.configDefaultLoadingImage(R.drawable.ic_default_app);
////			mBitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_default_app);
//			
////			ivIcon.setImageResource(R.drawable.ic_default_app);
//			
////			mBitmapUtils.display(ivIcon, Utils.getAvataUrl(userName, size));
//			DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_default_app)
//				.showImageForEmptyUri(R.drawable.ic_default_app)
//				.showImageOnFail(R.drawable.ic_default_app)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.displayer(new SimpleBitmapDisplayer())
////				.extraForDownloader(map)
//				.build();
//			ImageLoader.getInstance().displayImage(Utils.getAvataUrl(userName, size), ivIcon, options, null);
//		}
//	}
	
	BitmapProcessor bp = new BitmapProcessor() {
		
		@Override
		public Bitmap process(Bitmap bitmap) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	private String getRealContent(String rawContent) {
		try {
			if (rawContent.contains("&&")) {
				String realContent = rawContent.substring(rawContent.indexOf("&&") + 2);
				return realContent;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rawContent;
	}
	
//	private String getNamesContent(String rawContent) {
//		try {
//			String namesContent = rawContent.substring(0, rawContent.indexOf("&&"));
//			return namesContent;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public static ImageSize defineTargetSizeForView(View imageAware, ImageSize maxImageSize) {
		int width = imageAware.getWidth();
		if (width <= 0) width = maxImageSize.getWidth();

		int height = imageAware.getHeight();
		if (height <= 0) height = maxImageSize.getHeight();

		return new ImageSize(width, height);
	}
	
	ImageSize getMaxImageSize() {
		int maxImageWidthForMemoryCache = 0;
		int maxImageHeightForMemoryCache = 0;
		
		Resources resources = getResources();
		DisplayMetrics displayMetrics = resources.getDisplayMetrics();

		int width = maxImageWidthForMemoryCache;
		if (width <= 0) {
			width = displayMetrics.widthPixels;
		}
		int height = maxImageHeightForMemoryCache;
		if (height <= 0) {
			height = displayMetrics.heightPixels;
		}
		return new ImageSize(width, height);
	}
}
