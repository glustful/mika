package com.yxst.epic.yixin.view;

import java.lang.ref.WeakReference;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;
import org.androidannotations.annotations.res.StringRes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.yxst.epic.yixin.MyApplication;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.yxst.epic.yixin.data.dto.model.ObjectContent;
import com.yxst.epic.yixin.data.dto.model.ObjectContentImage;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.upload.UploadInfo;
import com.yxst.epic.yixin.upload.UploadManager;
import com.yxst.epic.yixin.upload.UploadManager.ManagerCallBack;
import com.yxst.epic.yixin.utils.MessageUtils;

@EViewGroup(R.layout.list_item_chat_item_img_to)
public class ChatItemImgToView extends ChatItem {

	private static final String TAG = "ChatItemImgToView";

	@ViewById
	ImageView ivImg;

	@ViewById
	View layoutProgress;
	
	@ViewById
	TextView tvUploadPercent;

	@ViewById
	ProgressBar pbUploadStatus;
	
	@ViewById
	ProgressBar pbSendingStatus;
	
	@ViewById
	ImageView ivStatusError;

	@DimensionPixelOffsetRes(R.dimen.chat_item_img_to_iv_maxSize)
	int ivMaxSize;
	
	public ChatItemImgToView(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {
		this.setTag(message.getClientMsgId());
		
		layoutProgress.setVisibility(View.GONE);
		pbSendingStatus.setVisibility(View.GONE);
		ivStatusError.setVisibility(View.GONE);

		final Integer extStatus = message.getExtStatus();
		if (extStatus != null) {
			if (DBMessage.STATUS_PENDING == extStatus
					|| DBMessage.STATUS_SENDING == extStatus) {
				pbSendingStatus.setVisibility(View.VISIBLE);
			} else if (DBMessage.STATUS_MEDIA_UPLOAD_PENDING == extStatus
					|| DBMessage.STATUS_MEDIA_UPLOAD_START == extStatus
					|| DBMessage.STATUS_MEDIA_UPLOAD_LOADING == extStatus) {
//				pbSendingStatus.setVisibility(View.VISIBLE);
				layoutProgress.setVisibility(View.VISIBLE);
			} else if (DBMessage.STATUS_ERROR == extStatus
					|| DBMessage.STATUS_MEDIA_UPLOAD_FAILURE == extStatus
					|| DBMessage.STATUS_MEDIA_UPLOAD_CANCEL == extStatus) {
				ivStatusError.setVisibility(View.VISIBLE);
			} else if (DBMessage.STATUS_SUCCESS == extStatus) {
				
			} else if (DBMessage.STATUS_MEDIA_UPLOAD_SUCCESS == extStatus) {
				
			}
		}
		
//		BitmapLoadCallBack<View> callBack = new DefaultBitmapLoadCallBack<View>() {
//			@Override
//			public void onPreLoad(View container, String uri,
//					BitmapDisplayConfig config) {
//				super.onPreLoad(container, uri, config);
////				Log.d(TAG, "callBack onPreLoad()");
//
//				layoutProgress.setVisibility(View.VISIBLE);
//				if (DBMessage.STATUS_MEDIA_UPLOAD_SUCCESS == extStatus) {
//					tvSendingPercent.setText(null);
//				}
//			}
//
//			@Override
//			public void onLoadStarted(View container, String uri,
//					BitmapDisplayConfig config) {
//				super.onLoadStarted(container, uri, config);
////				Log.d(TAG, "callBack onLoadStarted()");
//
//				layoutProgress.setVisibility(View.VISIBLE);
//				if (DBMessage.STATUS_MEDIA_UPLOAD_SUCCESS == extStatus) {
//					tvSendingPercent.setText(null);
//				}
//			}
//
//			@Override
//			public void onLoading(View container, String uri,
//					BitmapDisplayConfig config, long total, long current) {
//				super.onLoading(container, uri, config, total, current);
////				Log.d(TAG, "callBack onLoading()");
//			}
//
//			@Override
//			public void onLoadCompleted(View container, String uri,
//					Bitmap bitmap, BitmapDisplayConfig config,
//					BitmapLoadFrom from) {
//				super.onLoadCompleted(container, uri, bitmap, config, from);
////				Log.d(TAG, "callBack onLoadCompleted()");
//
//				if (DBMessage.STATUS_SUCCESS == extStatus
//						|| DBMessage.STATUS_ERROR == extStatus
//						|| DBMessage.STATUS_MEDIA_UPLOAD_FAILURE == extStatus) {
//					layoutProgress.setVisibility(View.GONE);
//				}
//			}
//
//			@Override
//			public void onLoadFailed(View container, String uri,
//					Drawable drawable) {
//				super.onLoadFailed(container, uri, drawable);
////				Log.d(TAG, "callBack onLoadFailed()");
//
//				if (DBMessage.STATUS_SUCCESS == extStatus
//						|| DBMessage.STATUS_ERROR == extStatus
//						|| DBMessage.STATUS_MEDIA_UPLOAD_FAILURE == extStatus) {
//					layoutProgress.setVisibility(View.GONE);
//				}
//			}
//		};
		
		String userCode = AccountInfo.instance().getLastUserInfo().getCode();
		Tools.setHeadImg(userCode, ivIcon);
		
		ObjectContentImage objectContent = ObjectContent.readValue(
				message.getObjectContent(), ObjectContentImage.class);
		
		int[] scaledSize = ChatItemImgFromView.getScaledSize(objectContent, ivMaxSize, ivMaxSize);
		
		setupSize(scaledSize);
		
		String localUserName = MyApplication.getInstance().getLocalUserName();
		String uri = null;
		if (!MessageUtils.isDeviceSyncMessage(localUserName, message)) {
			uri = getFilePath(objectContent);
			if (!TextUtils.isEmpty(uri)) {
				uri = Scheme.FILE.wrap(uri);
			}
		} else {
			uri = ChatItemImgFromView.getFormatUrl(objectContent, scaledSize, urlFormat);
		}
		Log.d(TAG, "uri:" + uri);
//		bitmapUtils.display(ivImg, uri/*, callBack*/);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(new ColorDrawable(Color.parseColor("#33000000")))
			.showImageForEmptyUri(R.drawable.card_photofail)
			.showImageOnFail(R.drawable.card_photofail)
			.cacheInMemory(true)
			.cacheOnDisk(true)
//			.considerExifParams(true)
			.displayer(new SimpleBitmapDisplayer())
			.build();
		ImageLoader.getInstance().displayImage(uri, ivImg, options, null, null);
		
		bindUploadInfo(getContext());
	}

	public static String getFilePath(ObjectContentImage objectContent) {
		if (objectContent != null) {
			// Log.d(TAG, "objectContent.filePath:" + objectContent.filePath);
			return objectContent.filePath;
		}
		return null;
	}

	private void setupSize(int[] scaledSize) {
		if (scaledSize != null) {
			ViewGroup.LayoutParams params = ivImg.getLayoutParams();
			params.width = scaledSize[0];
			params.height = scaledSize[1];
			ivImg.setLayoutParams(params);
		}
	}
	
	BitmapUtils bitmapUtils;

	public void setBitmapUtils(BitmapUtils bitmapUtils) {
		this.bitmapUtils = bitmapUtils;
	}
	
	private void bindUploadInfo(Context context) {
		UploadManager uploadManager = MyApplication.getInstance().getUploadManager();
		
		UploadInfo uploadInfo = uploadManager.getUploadInfo(message.getClientMsgId());
		
		if (uploadInfo != null) {
			this.update(uploadInfo);
//			v.refresh();
			
			HttpHandler<String> handler = uploadInfo.getHandler();
			if (handler !=null) {
				RequestCallBack<String> callBack = handler.getRequestCallBack();
				if (callBack instanceof UploadManager.ManagerCallBack) {
					UploadManager.ManagerCallBack managerCallBack = (ManagerCallBack) callBack;
					if (managerCallBack.getBaseCallBack() == null) {
//						managerCallBack.setBaseCallBack(new UploadRequestCallBack());
					}
				}
				callBack.setUserTag(new WeakReference<View>(this));
			}
		}
	}
	
	private UploadInfo uploadInfo;
	
	public void update(UploadInfo uploadInfo) {
		this.uploadInfo = uploadInfo;
		refresh();
	}
	
	@Override
	public void refresh() {
		if (uploadInfo.isUploading()) {
			if (uploadInfo.getFileLength() > 0) {
				tvUploadPercent.setText((int) (uploadInfo.getProgress() * 100 / uploadInfo.getFileLength()) + "%");
			} else {
				tvUploadPercent.setText(0 + "%");
			}
		} else {
			tvUploadPercent.setText(null);
		}
	}
	
	@Click(R.id.ivStatusError)
	void onClickIvStatusError() {
//		Integer extStatus = message.getExtStatus();
//		if (extStatus != null) {
//			if (extStatus == DBMessage.STATUS_MEDIA_UPLOAD_FAILURE) {
//				MessageUtils.sendMediaMsg(getContext(), message);
//			} else if (extStatus == DBMessage.STATUS_ERROR) {
//				MsgService.getMsgWriter(getContext()).sendMessage(message);
//			}
//		}
		MessageUtils.sendMediaMsg(getContext(), message);
	}
	
	@StringRes(R.string.format_url_download_image2)
	String urlFormat;
	
}
