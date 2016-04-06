package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;
import org.androidannotations.annotations.res.StringRes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.org.UserInfo;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.model.ObjectContent;
import com.yxst.epic.yixin.data.dto.model.ObjectContentImage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.model.Content;
import com.yxst.epic.yixin.model.ContentQun;
import com.yxst.epic.yixin.utils.MessageUtils;
import com.yxst.epic.yixin.utils.ThumbnailUtils;

@EViewGroup(R.layout.list_item_chat_item_img_from)
public class ChatItemImgFromView extends ChatItem {

	private static final String TAG = "ChatItemImgFromView";

	@ViewById
	ImageView ivImg;
	
	@ViewById(R.id.iv_icon)
	ImageView ivIcon;

	@ViewById
	View layoutProgress;

	@ViewById
	ProgressBar pbSendingStatus;

//	@ViewById
//	TextView tvSendingPercent;

	@DimensionPixelOffsetRes(R.dimen.chat_item_img_from_iv_maxSize)
	int ivMaxSize;

	@StringRes(R.string.format_url_download_image2)
	String urlFormat;

	public ChatItemImgFromView(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {

		String userName = "";
//		tvName.setText("需要服务器中转加入用户名");
//		tvName.setText("闇�鏈嶅姟鍣ㄤ腑杞姞鍏ョ敤鎴峰悕");
		Content content = Content.createContent(message.getExtRemoteUserName(), message.getContent());
		if (Member.isTypeQun(message.getExtRemoteUserName())) {
			ContentQun contentQun = (ContentQun) content;
			userName = contentQun.member.UserName;
			tvName.setVisibility(View.VISIBLE);
			tvName.setText(contentQun.member != null ? contentQun.member.DisplayName : null);
		} else {
			userName = message.getExtRemoteUserName();
			tvName.setVisibility(View.GONE);
			tvName.setText(message.getExtRemoteUserName());
		}
		
		if(userName != null && !"".equals(userName)){
			String u = userName.substring(0,userName.indexOf("@"));
			Long uid = Long.parseLong(u);
			Log.d("TOKENTOUID", uid.toString());
			String usercode = UserInfo.findByIdForUserCode(uid);
			usercode = usercode == null?"t":usercode;
			Tools.setHeadImg(usercode, ivIcon);
			}
		Log.d(TAG, "message.getObjectContent():"+message.getObjectContent());
		ObjectContentImage objectContent = ObjectContent.readValue(
				message.getObjectContent(), ObjectContentImage.class);

		int[] scaledSize = getScaledSize(objectContent, ivMaxSize, ivMaxSize);

		setupSize(scaledSize);

		String url = getFormatUrl(objectContent, scaledSize, urlFormat);
		Log.d(TAG, "uri:" + url);
//		bitmapUtils.display(ivImg, url, callBack);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(new ColorDrawable(Color.parseColor("#33000000")))
			.showImageForEmptyUri(R.drawable.card_photofail)
			.showImageOnFail(R.drawable.card_photofail)
			.cacheInMemory(true)
			.cacheOnDisk(true)
//			.considerExifParams(true)
			.displayer(new SimpleBitmapDisplayer())
			.build();
		ImageLoader.getInstance().displayImage(url, ivImg, options, callback1, callback2);
	}

	ImageLoadingListener callback1 = new SimpleImageLoadingListener() {

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			layoutProgress.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
			layoutProgress.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			layoutProgress.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			layoutProgress.setVisibility(View.VISIBLE);
		}
		
	};
	
	ImageLoadingProgressListener callback2 = null;
	
//	BitmapLoadCallBack<View> callBack = new DefaultBitmapLoadCallBack<View>() {
//		@Override
//		public void onPreLoad(View container, String uri,
//				BitmapDisplayConfig config) {
//			super.onPreLoad(container, uri, config);
//			// Log.d(TAG, "callBack onPreLoad()");
//
//			layoutProgress.setVisibility(View.VISIBLE);
////			tvSendingPercent.setText(null);
//			// pbSendingStatus.setVisibility(View.VISIBLE);
//			// tvSendingPercent.setVisibility(View.VISIBLE);
//		}
//
//		@Override
//		public void onLoadStarted(View container, String uri,
//				BitmapDisplayConfig config) {
//			super.onLoadStarted(container, uri, config);
//			// Log.d(TAG, "callBack onLoadStarted()");
//
//			layoutProgress.setVisibility(View.VISIBLE);
////			tvSendingPercent.setText(null);
//			// pbSendingStatus.setVisibility(View.VISIBLE);
//			// tvSendingPercent.setVisibility(View.VISIBLE);
//		}
//
//		@Override
//		public void onLoading(View container, String uri,
//				BitmapDisplayConfig config, long total, long current) {
//			super.onLoading(container, uri, config, total, current);
//			// Log.d(TAG, "callBack onLoading()");
//
////			tvSendingPercent.setText((int) (100 * current / total) + "%");
//		}
//
//		@Override
//		public void onLoadCompleted(View container, String uri,
//				Bitmap bitmap, BitmapDisplayConfig config,
//				BitmapLoadFrom from) {
//			super.onLoadCompleted(container, uri, bitmap, config, from);
//			// Log.d(TAG, "callBack onLoadCompleted()");
//
//			layoutProgress.setVisibility(View.GONE);
//			// pbSendingStatus.setVisibility(View.GONE);
//			// tvSendingPercent.setVisibility(View.GONE);
//		}
//
//		@Override
//		public void onLoadFailed(View container, String uri,
//				Drawable drawable) {
//			super.onLoadFailed(container, uri, drawable);
//			// Log.d(TAG, "callBack onLoadFailed()");
//
//			layoutProgress.setVisibility(View.GONE);
//			// pbSendingStatus.setVisibility(View.GONE);
//			// tvSendingPercent.setVisibility(View.GONE);
//		}
//	};
	
	private void setupSize(int[] scaledSize) {
		if (scaledSize != null) {
			ViewGroup.LayoutParams params = ivImg.getLayoutParams();
			params.width = scaledSize[0];
			params.height = scaledSize[1];
			ivImg.setLayoutParams(params);
		}
	}

	public static int[] getScaledSize(ObjectContentImage objectContent, int targetWidth, int targetHeight) {
		if (objectContent != null) {
			return ThumbnailUtils.getScaledSize(false, objectContent.width,
					objectContent.height, targetWidth, targetHeight);
		}
		return null;
	}

	public static String getFormatUrl(ObjectContentImage objectContent,
			int[] scaledSize, String format) {
		if (objectContent != null && scaledSize != null) {
			String fileUrl = MessageUtils.getFileUrl(objectContent);
			if (fileUrl != null) {
				fileUrl = fileUrl
						.replaceFirst(",", "/");
			}
			String extention = '.' + objectContent.fileExtention;
			try {
				return String.format(format, fileUrl, extention,
						scaledSize[0], scaledSize[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!TextUtils.isEmpty(objectContent.filePath)) {
				return "http://" + objectContent.filePath;
			}
		}

		return null;
	}

	BitmapUtils bitmapUtils;

	public void setBitmapUtils(BitmapUtils bitmapUtils) {
		this.bitmapUtils = bitmapUtils;
	}

}
