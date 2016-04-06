package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.miicaa.home.R;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.utils.MessageUtils;

@EViewGroup
public abstract class ChatItem extends RelativeLayout {

	public static final String TAG = "ChatItem";
	
	public static final long TIME_SOON = 5 * 60 * 1000;

	@ViewById(R.id.tv_time)
	TextView tvTime;

	@ViewById(R.id.ivIcon)
	ImageView ivIcon;
	
	@ViewById
	TextView tvName;
	
	@DimensionPixelOffsetRes(R.dimen.icon_size_small)
	int size;
	
	protected Message message;
	
	private BitmapUtils mIconBitmapUtils;
	
	public ChatItem(Context context) {
		super(context);
	}

	public ChatItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChatItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void bind(Message message, Long lastMessageTime) {
		this.message = message;
		this.bind(message);
		this.bindLastMessage(lastMessageTime);
		
		if (tvTime != null) {
			Long currentMessageTime = MessageUtils.getMessageTime(message);
			tvTime.setText(DateUtils.getRelativeDateTimeString(getContext(),
					currentMessageTime/*message.getExtTime()*/, System.currentTimeMillis(),
					DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE));
		}
		
		if (ivIcon != null) {
//			bindIcon(message);
//			ivIcon.setOnClickListener(this);
		}
	}
	
//	@Override
//	public void onClick(View v) {
//		String userName = null;
//		String nickName = null;
//		
//		
//		
//		Content content = Content.createContent(message.getExtRemoteUserName(), message.getContent());
//		
//		if (Member.isTypeQun(message.getExtRemoteUserName())) {
//			ContentQun contentQun = (ContentQun) content;
//			
//			if (DBMessage.INOUT_IN == message.getExtInOut()) {
//				userName = contentQun.member.UserName;
//				nickName = contentQun.member.NickName;
//			} else {
//				userName = message.getExtLocalUserName();
//			}
//		} else {
//			if (DBMessage.INOUT_IN == message.getExtInOut()) {
//				userName = message.getExtRemoteUserName();
//				nickName = message.getExtRemoteDisplayName();
//			} else {
//				userName = message.getExtLocalUserName();
//			}
//		}
//		
//		Member member = new Member();
//		member.UserName = userName;
//		member.NickName = nickName;
//		ContactDetailActivity_.intent(getContext()).userName(userName).member(member).start();
//	}
	
	protected abstract void bind(Message message);
	
//	protected void bindIcon(Message message) {
//		final Map<String, Object> params = new HashMap<String, Object>();
//		params.put("bt", true);
//		
//		int inOut = message.getExtInOut();
//		String userName = null;
//		Content content = null;
//		if (inOut == DBMessage.INOUT_OUT) {
//			content = Content.createContent(message.getExtLocalUserName(), message.getContent());
//			if (Member.isTypeQun(message.getExtLocalUserName())) {
//				ContentQun contentQun = (ContentQun) content;
//				userName = contentQun.member.UserName;
//			} else {
//				userName = message.getExtLocalUserName();
//			}
//		} else {
//			content = Content.createContent(message.getExtRemoteUserName(), message.getContent());
//			if (Member.isTypeQun(message.getExtRemoteUserName())) {
//				ContentQun contentQun = (ContentQun) content;
//				userName = contentQun.member.UserName;
//			} else {
//				userName = message.getExtRemoteUserName();
//			}
//		}
//		
////		mIconBitmapUtils.display(ivIcon, Utils.getAvataUrl(userName, size));
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.ic_default_avata_mini)
//			.showImageForEmptyUri(R.drawable.ic_default_avata_mini)
//			.showImageOnFail(R.drawable.ic_default_avata_mini)
//			.cacheInMemory(true)
//			.cacheOnDisk(true)
//			.displayer(new SimpleBitmapDisplayer())
//			.build();
//		final String finalUserName = userName;
//		final String originalUrl = Utils.getAvataUrl(finalUserName, size);
//		
//		ImageLoader.getInstance().displayImage(originalUrl, ivIcon, options, new SimpleImageLoadingListener() {
//			@Override
//			public void onLoadingComplete(String imageUri, View view,
//					Bitmap loadedImage) {
//				DisplayImageOptions dio = new DisplayImageOptions.Builder()
//					.cacheInMemory(false)
//					.cacheOnDisk(false)
//					.displayer(new SimpleBitmapDisplayer())
//					.build();
//				
//				String btUrl = Utils.addQueryParams(originalUrl, params);
//				ImageLoader.getInstance().displayImage(btUrl, ivIcon, dio, new SimpleImageLoadingListener(){
//					@Override
//					public void onLoadingComplete(String imageUri, View view,
//							Bitmap loadedImage) {
//						if (loadedImage != null) {
//	//						Log.d(TAG, "onLoadingComplete() loadedImage:" + loadedImage);
////							ImageSize targetSize = new ImageSize(size, size);
//							ImageSize targetSize = defineTargetSizeForView(view, getMaxImageSize());
//							Log.d(TAG, "onLoadingComplete() targetSize:" + targetSize);
//							String memoryCacheKey = MemoryCacheUtils.generateKey(originalUrl, targetSize);
//							ImageLoader.getInstance().getMemoryCache().put(memoryCacheKey, loadedImage);
//							try {
//								ImageLoader.getInstance().getDiskCache().save(originalUrl, loadedImage);
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				});
//			}
//		});
//	}
	
	protected void bindLastMessage(Long lastMessageTime) {
		// Log.d(TAG, "bindLastMessage() lastExtTime:" + lastExtTime);
		// Log.d(TAG, "bindLastMessage() nowExtTime:" + message.getExtTime());
		//
		// if (lastExtTime != null) {
		// Log.d(TAG, "bindLastMessage() time:" + (message.getExtTime() -
		// lastExtTime));
		// }

		if (lastMessageTime == null) {
			tvTime.setVisibility(View.VISIBLE);
		} else {
			if (/*message.getExtTime()*/MessageUtils.getMessageTime(message) - lastMessageTime > TIME_SOON) {
				tvTime.setVisibility(View.VISIBLE);
			} else {
				tvTime.setVisibility(View.GONE);
			}
		}
	}

	public void setIconBitmapUtils(BitmapUtils bitmapUtils) {
		mIconBitmapUtils = bitmapUtils;
	}
	
	public void refresh() {
		
	}
	
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
