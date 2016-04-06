package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;
import org.androidannotations.annotations.res.StringRes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.yxst.epic.yixin.R;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.model.ObjectContent;
import com.yxst.epic.yixin.data.dto.model.ObjectContentImage;
import com.yxst.epic.yixin.data.dto.response.ResponseUpload;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.model.Content;
import com.yxst.epic.yixin.model.ContentNormal;
import com.yxst.epic.yixin.model.ContentQun;
import com.yxst.epic.yixin.utils.SmileyParser;
import com.yxst.epic.yixin.utils.ThumbnailUtils;

@EViewGroup(R.layout.list_item_chat_item_img_from)
public class ChatItemImgFromView extends ChatItem {

	private static final String TAG = "ChatItemImgFromView";

	@ViewById
	ImageView ivImg;

	@ViewById
	View layoutProgress;

	@ViewById
	ProgressBar pbSendingStatus;

	@ViewById
	TextView tvSendingPercent;

	@DimensionPixelOffsetRes(R.dimen.chat_item_img_from_iv_maxSize)
	int ivMaxSize;

	@StringRes(R.string.format_url_download_image2)
	String urlFormat;

	public ChatItemImgFromView(Context context) {
		super(context);
	}

	@Override
	protected void bind(Message message) {

//		tvName.setText("需要服务器中转加入用户名");
		Content content = Content.createContent(message.getExtRemoteUserName(), message.getContent());
		if (Member.isTypeQun(message.getExtRemoteUserName())) {
			ContentQun contentQun = (ContentQun) content;
			
			tvName.setVisibility(View.VISIBLE);
			tvName.setText(contentQun.member != null ? contentQun.member.DisplayName : null);
		} else {
			tvName.setVisibility(View.GONE);
			tvName.setText(message.getExtRemoteUserName());
		}
		
		BitmapLoadCallBack<View> callBack = new DefaultBitmapLoadCallBack<View>() {
			@Override
			public void onPreLoad(View container, String uri,
					BitmapDisplayConfig config) {
				super.onPreLoad(container, uri, config);
				// Log.d(TAG, "callBack onPreLoad()");

				layoutProgress.setVisibility(View.VISIBLE);
				tvSendingPercent.setText(null);
				// pbSendingStatus.setVisibility(View.VISIBLE);
				// tvSendingPercent.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadStarted(View container, String uri,
					BitmapDisplayConfig config) {
				super.onLoadStarted(container, uri, config);
				// Log.d(TAG, "callBack onLoadStarted()");

				layoutProgress.setVisibility(View.VISIBLE);
				tvSendingPercent.setText(null);
				// pbSendingStatus.setVisibility(View.VISIBLE);
				// tvSendingPercent.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoading(View container, String uri,
					BitmapDisplayConfig config, long total, long current) {
				super.onLoading(container, uri, config, total, current);
				// Log.d(TAG, "callBack onLoading()");

				tvSendingPercent.setText((int) (100 * current / total) + "%");
			}

			@Override
			public void onLoadCompleted(View container, String uri,
					Bitmap bitmap, BitmapDisplayConfig config,
					BitmapLoadFrom from) {
				super.onLoadCompleted(container, uri, bitmap, config, from);
				// Log.d(TAG, "callBack onLoadCompleted()");

				layoutProgress.setVisibility(View.GONE);
				// pbSendingStatus.setVisibility(View.GONE);
				// tvSendingPercent.setVisibility(View.GONE);
			}

			@Override
			public void onLoadFailed(View container, String uri,
					Drawable drawable) {
				super.onLoadFailed(container, uri, drawable);
				// Log.d(TAG, "callBack onLoadFailed()");

				layoutProgress.setVisibility(View.GONE);
				// pbSendingStatus.setVisibility(View.GONE);
				// tvSendingPercent.setVisibility(View.GONE);
			}
		};

		ObjectContentImage objectContent = ObjectContent.readValue(
				message.getObjectContent(), ObjectContentImage.class);

		int[] scaledSize = getScaledSize(objectContent);

		setupSize(scaledSize);

		String url = getFormatUrl(objectContent, scaledSize);
		if (TextUtils.isEmpty(url)) {
			url = getRawUrl(message);
		}
		Log.d(TAG, "uri:" + url);
		bitmapUtils.display(ivImg, url, callBack);
	}

	private void setupSize(int[] scaledSize) {
		if (scaledSize != null) {
			ViewGroup.LayoutParams params = ivImg.getLayoutParams();
			params.width = scaledSize[0];
			params.height = scaledSize[1];
			ivImg.setLayoutParams(params);
		}
	}

	private int[] getScaledSize(ObjectContentImage objectContent) {
		if (objectContent != null) {
			return ThumbnailUtils.getScaledSize(false, objectContent.width,
					objectContent.height, ivMaxSize, ivMaxSize);
		}
		return null;
	}

	private String getFormatUrl(ObjectContentImage objectContent,
			int[] scaledSize) {
		if (objectContent != null && scaledSize != null) {
			ResponseUpload responseUpload = objectContent.responseUpload;
			if (responseUpload != null) {
				try {
					// return String.format(urlFormat, responseUpload.fileUrl,
					// responseUpload.fileName, scaledSize[0],
					// scaledSize[1]);
//					responseUpload.fileUrl = "http://115.29.107.77:5083/3,01896857d237";;
					String fileUrl = responseUpload.fileUrl != null ? responseUpload.fileUrl
							.replaceFirst(",", "/") : null;
					String extention = '.' + objectContent.fileExtention;
					return String.format(urlFormat, fileUrl, extention,
							scaledSize[0], scaledSize[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private static String getRawUrl(Message message) {
		String fileUrl = message.getMediaId();
		String uri = TextUtils.isEmpty(fileUrl) ? null : "http://" + fileUrl;
		return uri;
	}

	BitmapUtils bitmapUtils;

	public void setBitmapUtils(BitmapUtils bitmapUtils) {
		this.bitmapUtils = bitmapUtils;
	}

}
