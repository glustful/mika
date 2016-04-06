package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.miicaa.home.R;
import com.miicaa.home.ui.picture.PhotoView;
import com.miicaa.home.ui.picture.PhotoViewAttacher.OnViewTapListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.activity.ChatImagesActivity;
import com.yxst.epic.yixin.data.dto.model.ObjectContent;
import com.yxst.epic.yixin.data.dto.model.ObjectContentImage;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.utils.MessageUtils;

@EViewGroup(R.layout.list_item_chat_item_imgs)	
public class ChatItemImgsView extends ChatItemNullView {
	
	private static final String TAG = "ChatItemImgsView";
	
	@ViewById(R.id.iv_photoview)
	PhotoView mPhotoView;
	
	@ViewById(R.id.layout_loading)
	LinearLayout mLayoutLoading;
	
	@ViewById(R.id.pbloading)
	ProgressBar mProgressBar;
	
	@ViewById(R.id.tv_percent)
	TextView mTvPercennt;
	
	BitmapUtils bitmapUtils;
	
	private Context mContext;
	
	public ChatItemImgsView(Context context) {
		super(context);
		this.mContext = context;
	}
	
	@Override
	protected void bind(Message message) {
		mPhotoView.setOnViewTapListener(new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				Log.i(TAG, "ChatImagesActivity finish");
				((ChatImagesActivity) mContext).finish();
			}
		});
		
		ObjectContentImage objectContent = ObjectContent.readValue(
				message.getObjectContent(), ObjectContentImage.class);
		int inOut = message.getExtInOut();
		String uri = null;
		if (DBMessage.INOUT_IN == inOut) {
			uri = getFormatUrl(objectContent);
		} else if (DBMessage.INOUT_OUT == inOut) {
			String localUserName = MyApplication.getInstance().getLocalUserName();
			if (!MessageUtils.isDeviceSyncMessage(localUserName, message)) {
				uri = getFilePath(objectContent);
				if (!TextUtils.isEmpty(uri)) {
					uri = Scheme.FILE.wrap(uri);
				}
			} else {
				uri = getFormatUrl(objectContent);
			}
		}
		Log.d(TAG, "uri:" + uri);
//		bitmapUtils.display(mPhotoView, uri, callBack);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(new ColorDrawable(Color.parseColor("#33000000")))
			.showImageForEmptyUri(R.drawable.card_photofail)
			.showImageOnFail(R.drawable.card_photofail)
			.cacheInMemory(true)
			.cacheOnDisk(true)
//			.considerExifParams(true)
			.displayer(new SimpleBitmapDisplayer())
			.imageScaleType(ImageScaleType.EXACTLY)
			.build();
		ImageLoader.getInstance().displayImage(uri, mPhotoView, options, callBack2, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				int percent = 100 * current / total;
				mTvPercennt.setText(String.valueOf(percent) + "%");
			}
		});
	}
	
	ImageLoadingListener callBack2 = new SimpleImageLoadingListener() {

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			mLayoutLoading.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
			mLayoutLoading.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			mLayoutLoading.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			mLayoutLoading.setVisibility(View.VISIBLE);
		}
		
	};
	
//	BitmapLoadCallBack<View> callBack = new DefaultBitmapLoadCallBack<View>() {
//		@Override
//		public void onPreLoad(View container, String uri,
//				BitmapDisplayConfig config) {
//			super.onPreLoad(container, uri, config);
//			mLayoutLoading.setVisibility(View.VISIBLE);
//		}
//
//		@Override
//		public void onLoadStarted(View container, String uri,
//				BitmapDisplayConfig config) {
//			super.onLoadStarted(container, uri, config);
//			mLayoutLoading.setVisibility(View.VISIBLE);
//		}
//
//		@Override
//		public void onLoading(View container, String uri,
//				BitmapDisplayConfig config, long total, long current) {
//			Log.d(TAG, "onLoading() current:" + current);
//			super.onLoading(container, uri, config, total, current);
//			int percent = (int) (100 * current / total);
//			mTvPercennt.setText(String.valueOf(percent) + "%");
//		}
//
//		@Override
//		public void onLoadCompleted(View container, String uri,
//				Bitmap bitmap, BitmapDisplayConfig config,
//				BitmapLoadFrom from) {
//			Log.d(TAG, "onLoadCompleted()");
//			super.onLoadCompleted(container, uri, bitmap, config, from);
//			mLayoutLoading.setVisibility(View.GONE);
//		}
//
//		@Override
//		public void onLoadFailed(View container, String uri,
//				Drawable drawable) {
//			super.onLoadFailed(container, uri, drawable);
//			mLayoutLoading.setVisibility(View.GONE);
//		}
//	};
	
	public static String getFormatUrl(ObjectContentImage objectContent) {
		if (objectContent != null) {
			String fileUrl = MessageUtils.getFileUrl(objectContent);
			if (fileUrl != null) {
				fileUrl = fileUrl.replaceFirst(",", "/");
			}
			String extention = '.' + objectContent.fileExtention;
			try {
				return String.format("http://%s/%s", fileUrl, extention);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public static String getFilePath(ObjectContentImage objectContent) {
		if (objectContent != null) {
			return objectContent.filePath;
		}
		return null;
	}
	
	public void setBitmapUtils(BitmapUtils bitmapUtils) {
		this.bitmapUtils = bitmapUtils;
	}

}
