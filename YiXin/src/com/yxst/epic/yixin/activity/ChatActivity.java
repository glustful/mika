package com.yxst.epic.yixin.activity;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.afinal.simplecache.ACache;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.miicaa.home.R;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.utils.AllUtils;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.adapter.ChatAdapter;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Operation;
import com.yxst.epic.yixin.data.dto.model.ObjectContentImage;
import com.yxst.epic.yixin.data.dto.model.ObjectContentVoice;
import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.data.dto.request.GetAppOpertionListRequest;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.GetAppOpertionListResponse;
import com.yxst.epic.yixin.data.dto.response.PushResponse;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.db.MessageContentProvider;
import com.yxst.epic.yixin.fragment.OperationsMoreDialogFragment;
import com.yxst.epic.yixin.fragment.OperationsMoreDialogFragment_;
import com.yxst.epic.yixin.listener.OnOperationClickListener;
import com.yxst.epic.yixin.loader.ChatLoader;
import com.yxst.epic.yixin.model.Content;
import com.yxst.epic.yixin.model.ContentNormal;
import com.yxst.epic.yixin.model.ContentQun;
import com.yxst.epic.yixin.rest.IMInterfaceProxy;
import com.yxst.epic.yixin.rest.OperationRest;
import com.yxst.epic.yixin.rest.ServiceRequest;
import com.yxst.epic.yixin.rest.ServiceResult;
import com.yxst.epic.yixin.service.MsgService;
import com.yxst.epic.yixin.utils.CacheUtils;
import com.yxst.epic.yixin.utils.MessageUtils;
import com.yxst.epic.yixin.utils.ThumbnailUtils;
import com.yxst.epic.yixin.utils.Utils;
import com.yxst.epic.yixin.view.ChatSendOptView;
import com.yxst.epic.yixin.view.FooterTextView;
import com.yxst.epic.yixin.view.FooterView;
import com.yxst.epic.yixin.view.RcdView;
import com.yxst.epic.yixin.view.ResizeLayout;

@EActivity(R.layout.im_activity_chat)
public class ChatActivity extends ActionBarActivity implements
		RestErrorHandler, OnOperationClickListener {

	protected static final String TAG = "ChatActivity";

	@Extra
	String localUserName;

	@Extra
	String remoteUserName;

	@Extra
	String remoteDisplayName;

	
	// @Extra
	// Member remoteMember;

	@ViewById
	ResizeLayout layoutRoot;
	
	@ViewById(R.id.chat_base_id_title)
	TextView gtitle;
	
	@ViewById(R.id.chat_base_id_add)
	ImageButton chat_add;

	@ViewById(android.R.id.list)
	ListView listView;

	@ViewById
	FooterView viewFooter;

	// @ViewById
	// FooterTextView viewFooterText;
	


	@RestService
	IMInterface myRestClient;
	
	@SystemService
	LayoutInflater mLayoutInflater;

	@Bean
	ChatAdapter mChatAdapter;

	@ViewById
	RcdView viewRcd;

	@ViewById
	ChatSendOptView viewChatSendOpt;

	@Click(R.id.chat_base_id_back)
	void click(){
		finish();
	}
	
	@Click(R.id.chat_base_id_add)
	void addClick(){
		if (Member.isTypeApp(remoteUserName)) {
			AppDetailActivity_.intent(this).userName(remoteUserName)
					.displayName(remoteDisplayName).start();
			
		} else {
			
			ChatDetailActivity_
					.intent(this)
					.localUserName(localUserName)
					.localMember(
							MyApplication.getInstance().getLocalMember())
					.remoteUserName(remoteUserName)
					.remoteDisplayName(remoteDisplayName)
					.startForResult(REQUEST_CODE_CHAT_DETAIL);
		}
	}
	// @Bean
	// MyErrorHandler myErrorHandler;
	
	//TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);
		//title =(TextView) mLayoutInflater.inflate(R.layout.action_title, null);
		super.onCreate(savedInstanceState);
		if(getActionBar() != null)
		getActionBar().hide();
		
	}

	@Override
	protected void onDestroy() {
		MyApplication.getInstance().removeActivity(this);
		doInBackgroundOperationCancle = true;
		BackgroundExecutor.cancelAll("doInBackgroundOperation", true);
		doInBackgroundGetAppOperationListCanceld = true;
		BackgroundExecutor.cancelAll("doInBackgroundGetAppOperationList", true);

		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);

		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume()");
		// Bundle args = new Bundle();
		getSupportLoaderManager().restartLoader(0, null, mLoaderCallbacks);

		// Utils.cancelNotification(this);

		updateTitle();

		updateReadWithNoObserver();

		super.onResume();
	}

	private void updateTitle() {
		//final ActionBar bar = getSupportActionBar();
		//bar.setTitle("返回");
//		title.setText(remoteDisplayName);
		Log.e("localUserName", localUserName+"-------"+remoteUserName);
		String displayName = DBManager.getInstance(this).getChatDisplayName(
				localUserName, remoteUserName);
		Log.d(TAG, "remoteDisplayName"+remoteDisplayName);
		if (displayName != null) {
			//title.setText(displayName);
			gtitle.setText(displayName);
			remoteDisplayName = displayName;
		}else{
			//title.setText(remoteDisplayName);
			gtitle.setText(remoteDisplayName);
		}
		if (Member.isTypeUser(remoteUserName)) {
			
			chat_add.setImageResource(R.drawable.actionbar_particular_icon);
		} else if (Member.isTypeQun(remoteUserName)) {
			
			chat_add.setImageResource(R.drawable.actionbar_facefriend_icon);
		}
	}

	@Override
	protected void onStop() {
		getContentResolver().unregisterContentObserver(mContentObserver);

		super.onStop();
	}

	@Override
	public void onBackPressed() {
		if (viewFooter.viewFooterText.isSoftInputShowing()) {

		}
		super.onBackPressed();
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chat, menu);

		MenuItem item = menu.findItem(R.id.action_contact_detail);

		if (Member.isTypeUser(remoteUserName)) {
			item.setIcon(R.drawable.actionbar_particular_icon);
		} else if (Member.isTypeQun(remoteUserName)) {
			item.setIcon(R.drawable.actionbar_facefriend_icon);
		}
//
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_contact_detail:
			if (Member.isTypeApp(remoteUserName)) {
				AppDetailActivity_.intent(this).userName(remoteUserName)
						.displayName(remoteDisplayName).start();
			} else {
				ChatDetailActivity_
						.intent(this)
						.localUserName(localUserName)
						.localMember(
								MyApplication.getInstance().getLocalMember())
						.remoteUserName(remoteUserName)
						.remoteDisplayName(remoteDisplayName)
						.startForResult(REQUEST_CODE_CHAT_DETAIL);
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}*/

	@AfterInject
	void afterInject() {
		myRestClient = IMInterfaceProxy.create(10 * 1000);
		myRestClient.setRestErrorHandler(this);

		operationRest.setRestErrorHandler(this);
		
		mChatAdapter.setOnOperationClickListener(this);
	}

	@AfterViews
	void afterViews() {
		Log.d(TAG, "afterViews() localUserName:" + localUserName);
		Log.d(TAG, "afterViews() remoteUserName:" + remoteUserName);
		Log.d(TAG, "afterViews() remoteDisplayName:" + remoteDisplayName);

		viewFooter.setOnOperationClickListener(this);
		viewFooter.bind(remoteUserName, null);

		viewFooter.viewFooterText.setRcdView(viewRcd);

		registerForContextMenu(listView);

		//final ActionBar bar = getSupportActionBar();
		/*bar.setTitle("返回");
		title.setText(remoteDisplayName);

		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		 flags =  flags ^ ActionBar.DISPLAY_SHOW_CUSTOM;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setDisplayOptions(change, flags);*/
		 //bar.setDisplayShowHomeEnabled( false );
	       // bar.setDisplayShowCustomEnabled(true);
		//ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
				//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				//| Gravity.CENTER;
		//bar.setCustomView(title, lp);

		layoutRoot.setOnResizeListener(new ResizeLayout.OnResizeListener() {
			@Override
			public void OnResize(int w, int h, int oldw, int oldh) {
				boolean isSoftInputShowing = h < oldh;
				viewFooter.viewFooterText
						.setSoftInputShowing(isSoftInputShowing);
			}
		});

		initViewFooter();
		viewFooter.viewFooterText.setOnEventListener(mOnFooterTextViewEvent);

		listView.setAdapter(mChatAdapter);
		listView.setOnScrollListener(new PauseOnScrollListener(mChatAdapter
				.getBitmapUtils(), false, false/* true */));

		getSupportLoaderManager().initLoader(0, null, mLoaderCallbacks);
		
		if (remoteUserName != null && Member.isTypeApp(remoteUserName)) {
			doInBackgroundGetAppOperationList();
		}
	}

	FooterTextView.OnFooterTextViewEventListener mOnFooterTextViewEvent = new FooterTextView.OnFooterTextViewEventListener() {
		@Override
		public void onBtnSendClick(View v, String content) {
			if(content.length() <= 0){
				return ;
			}
			doInBackgroundPush(content);
		}

		@Override
		public void onClickTypeSelectImagePick() {
			startImagePick();
		}

		@Override
		public void onClickTypeSelectImageCapture() {
			startActionCamera();
		}

		@Override
		public void onVoiceRcd(String filePath, long voiceLength) {
			doInBackgroundSendMsgVoice(filePath, voiceLength);
		}
	};

	private ContentObserver mContentObserver = new ContentObserver(
			new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			Log.d(TAG, "onChange(boolean)");
			updateReadWithNoObserver();
			listView.setSelection(listView.getCount());

			updateTitle();
		};
	};

	@Background
	void updateReadWithNoObserver() {
		getContentResolver().unregisterContentObserver(mContentObserver);
		DBManager.getInstance(this).updateRead(localUserName, remoteUserName);
		getContentResolver().registerContentObserver(
				MessageContentProvider.CONTENT_URI, true, mContentObserver);
	}

	@Background
	void doInBackgroundPush(String content) {
		Msg msg = new Msg();
		msg.ClientMsgId = UUID.randomUUID().toString();
		msg.MsgType = Msg.MSG_TYPE_NORMAL;
		msg.FromUserName = localUserName;
		msg.ToUserName = remoteUserName;
		msg.Content = content;
		msg.ToDisplayName = remoteDisplayName;

		// long dbMsgId = saveDB(msg);
		// push(dbMsgId, msg);
		MsgService.getMsgWriter(this).sendMsg(msg);
	}

	@UiThread
	void onPostExecute(long dbMsgId, PushResponse response) {
		Log.d(TAG, "onPostExecute() response:" + response);

		Message dbMsg = DBManager.getInstance(ChatActivity.this).load(dbMsgId);

		if (response == null) {
			dbMsg.setExtStatus(DBMessage.STATUS_ERROR);
			DBManager.getInstance(ChatActivity.this).update(dbMsg);
			return;
		}

		if (response.BaseResponse.Ret == BaseResponse.RET_SUCCESS) {
			dbMsg.setExtStatus(DBMessage.STATUS_SUCCESS);
			DBManager.getInstance(ChatActivity.this).update(dbMsg);
		} else {
			dbMsg.setExtStatus(DBMessage.STATUS_ERROR);
			DBManager.getInstance(ChatActivity.this).update(dbMsg);
		}

	}

	@UiThread
	@Override
	public void onRestClientExceptionThrown(RestClientException e) {
		viewChatSendOpt.setVisible(false);
//		Toast.makeText(this, "访问失败", Toast.LENGTH_SHORT).show();
	}

	private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			return new ChatLoader(ChatActivity.this, localUserName,
					remoteUserName);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			mChatAdapter.changeCursor(cursor);
			listView.setSelection(listView.getCount());
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			mChatAdapter.changeCursor(null);
		}

	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.activity_chat_context_menu, menu);
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
		Message message = mChatAdapter.readEntity((Cursor) mChatAdapter.getItem(info.position));
		
		if (Msg.MSG_TYPE_NORMAL != message.getMsgType()) {
			menu.removeItem(R.id.copy_message);
		} 
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Message message = mChatAdapter.readEntity((Cursor) mChatAdapter
				.getItem(menuInfo.position));

		switch (item.getItemId()) {
		case R.id.copy_message:
			if (message.getExtInOut() == DBMessage.INOUT_IN) {
				Content content = Content.createContent(message.getExtRemoteUserName(), message.getContent());
				if (Member.isTypeQun(message.getExtRemoteUserName())) {
					ContentQun contentQun = (ContentQun) content;
					
					ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					cm.setText(contentQun.realContent);
				} else {
					ContentNormal contentNormal = (ContentNormal) content;
					
					ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					cm.setText(contentNormal.content);
				}
			} else {
				ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				cm.setText(message.getContent());
			}
			
			return true;
		case R.id.delete_message:
			DBManager.getInstance(this).deleteMessage(message);
			return true;

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// }

	private static final int REQUEST_CODE_CHAT_DETAIL = 1;
	private static final int REQUEST_CODE_IMAGE_GET_CONTENT = 2;
	private static final int REQUEST_CODE_IMAGE_CAPTURE = 3;
	private static final int REQUEST_CODE_APP_DETAIL = 4;

	@OnActivityResult(REQUEST_CODE_CHAT_DETAIL)
	void onActivityResultChatDetail(int resultCode, Intent data) {
		if (RESULT_OK == resultCode) {
			finish();
		}
	}

	@OnActivityResult(REQUEST_CODE_IMAGE_GET_CONTENT)
	void onActivityResultGetImageBySdcard(int resultCode, Intent data) {
		if (RESULT_OK == resultCode) {
			Uri uri = data.getData();
			Log.d(TAG, "onActivityResultGetImageBySdcard() uri:" + uri);

			String filePath = getFilePathByMediaUri(this, uri);
			Log.d(TAG, "onActivityResultGetImageBySdcard() filePath:"
					+ filePath);
//			Toast.makeText(this, "图片Uri:" + filePath, Toast.LENGTH_SHORT)
//					.show();

			onCompressBitmap(filePath);
		}
	}

	private static String getFilePathByMediaUri(Context context, Uri uri) {
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		// Log.d(TAG,
		// "getFilePathByMediaUri() cursor:"
		// + DatabaseUtils.dumpCursorToString(cursor));
		if (cursor == null) {
			return null;
		}
		try {
			cursor.moveToFirst();
			String _data = cursor.getString(1);
			return _data;
		} finally {
			cursor.close();
		}
	}

	@OnActivityResult(REQUEST_CODE_IMAGE_CAPTURE)
	void onActivityResultGetImageByCamera(int resultCode, Intent data) {
		if (RESULT_OK == resultCode) {
			Log.d(TAG, "onActivityResultGetImageByCamera() mCurrentPhotoFile:"
					+ mCurrentPhotoFile);
//			Toast.makeText(this, "图片Uri:" + mCurrentPhotoFile,
//					Toast.LENGTH_SHORT).show();

			onCompressBitmap(mCurrentPhotoFile.getPath());
		}
	}

	@Background
	void onCompressBitmap(String filePaht) {
		Bitmap thumbnailBitmap = ThumbnailUtils
				.createImageThumbnailYouXin(filePaht);
		Log.d(TAG, "onCompressBitmap() thumbnailBitmap:" + thumbnailBitmap);
		if (thumbnailBitmap != null) {
			Log.d(TAG,
					"onCompressBitmap() thumbnailBitmap:["
							+ thumbnailBitmap.getWidth() + "x"
							+ thumbnailBitmap.getHeight() + "]");
			File imageOutFileDir = Utils.ensureIMSubDir(this,
					Utils.FILE_PATH_SUB_DIR_IMAGE_OUT);
			// Utils.createNoMediaFile(imageOutFileDir);
			File imageOutFile = new File(imageOutFileDir,
					System.currentTimeMillis() + ".jpg");
			ThumbnailUtils.compress(thumbnailBitmap, imageOutFile, 68);
			thumbnailBitmap.recycle();

			doInBackgroundSendMsgImg(imageOutFile.getPath());
		}
	}

	@Background
	void doInBackgroundSendMsgImg(String filePath) {
		Msg msg = new Msg();
		msg.ClientMsgId = UUID.randomUUID().toString();
		msg.MsgType = Msg.MSG_TYPE_IMAGE;
		msg.FromUserName = localUserName;
		msg.ToUserName = remoteUserName;
		msg.Content = "[图片]";
		msg.ToDisplayName = remoteDisplayName;

		BitmapFactory.Options options = ThumbnailUtils.getOptions(filePath);

		ObjectContentImage objectContent = new ObjectContentImage();
		objectContent.filePath = filePath;
		objectContent.fileExtention = MimeTypeMap
				.getFileExtensionFromUrl(filePath);
		objectContent.fileMimeType = MimeTypeMap.getSingleton()
				.getMimeTypeFromExtension(objectContent.fileExtention);
		objectContent.width = options.outWidth;
		objectContent.height = options.outHeight;

		msg.setObjectContent(Msg.MSG_TYPE_IMAGE, objectContent);

		Message message = DBMessage.retriveMessageFromMsg(msg,
				DBMessage.INOUT_OUT);
		MessageUtils.sendMediaMsg(this, message);
	}

	@Background
	void doInBackgroundSendMsgVoice(String filePath, long voiceLength) {

		Msg msg = new Msg();
		msg.ClientMsgId = UUID.randomUUID().toString();
		msg.MsgType = Msg.MSG_TYPE_VOICE;
		msg.FromUserName = localUserName;
		msg.ToUserName = remoteUserName;
		msg.Content = "[语音]";
		msg.ToDisplayName = remoteDisplayName;

		ObjectContentVoice objectContent = new ObjectContentVoice();
		objectContent.filePath = filePath;
		objectContent.fileExtention = MimeTypeMap
				.getFileExtensionFromUrl(filePath);
		objectContent.fileMimeType = MimeTypeMap.getSingleton()
				.getMimeTypeFromExtension(objectContent.fileExtention);
		objectContent.voiceLength = voiceLength;

		msg.setObjectContent(Msg.MSG_TYPE_VOICE, objectContent);

		Message message = DBMessage.retriveMessageFromMsg(msg,
				DBMessage.INOUT_OUT);
		MessageUtils.sendMediaMsg(this, message);
	}

	/**
	 * 选择图片
	 */
	private void startImagePick() {
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// intent.addCategory(Intent.CATEGORY_OPENABLE);
		// intent.setType("image/*");
		// intent.putExtra("return-data", true);
		// intent.putExtra("scale", true);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(Intent.createChooser(intent, "选择图片"),
				REQUEST_CODE_IMAGE_GET_CONTENT);

	}

	private File mCurrentPhotoFile;

	/**
	 * 相机拍照
	 */
	private void startActionCamera() {
		mCurrentPhotoFile = getOutPutFile(this);
		Log.d(TAG, "startActionCamera() mCurrentPhotoFile:" + mCurrentPhotoFile);

		if (mCurrentPhotoFile != null) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
		} else {
			Toast.makeText(this, "请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
		}
	}

	// 拍照保存的绝对路径
	private static File getOutPutFile(Context context) {
		File youxinDir = Utils.ensureIMSubDir(context,
				Utils.FILE_PATH_SUB_DIR_IMAGE_CAPTURE);
		if (youxinDir != null) {
			File file = new File(youxinDir, getPhotoFileName());
			return file;
		}
		return null;
	}

	/**
	 * Create a file name for the icon photo using current time.
	 */
	private static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss", Locale.getDefault());
		return dateFormat.format(date) + ".jpg";
	}

	@RestService
	OperationRest operationRest;

	boolean doInBackgroundOperationCancle = false;
	
	@Background(id = "doInBackgroundOperation", serial = "doInBackgroundOperation")
	void doInBackgroundOperation(String url) {
		Log.d(TAG, "doInBackgroundOperation() url:" + url);
		onPreExecuteOpt();

		operationRest.setRootUrl(url);
		// ServiceResult<Object> response = operationRest.get();
		ServiceRequest request = new ServiceRequest();
		request.baseRequest = CacheUtils.getBaseRequest(this);
		ServiceResult<Object> response = operationRest.post(request);
		Log.d(TAG, "doInBackgroundOperation() response:" + response);

		onPostExecuteOpt(response);
	}

	@UiThread
	void onPreExecuteOpt() {
		viewChatSendOpt.setVisible(true);
	}

	@UiThread
	void onPostExecuteOpt(ServiceResult<Object> response) {
		Log.d(TAG, "onPostExecuteOpt() response:" + response);
		viewChatSendOpt.setVisible(false);
		
		if (doInBackgroundOperationCancle) {
			return;
		}
		
		if (response == null) {
			Toast.makeText(this, "操作失败", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!response.isSucceed()) {
			Utils.showToastShort(this, response.getMsg());
		}
	}

	@Override
	public void onOperationClick(Operation operation) {
		Log.d(TAG, "onOperationClick() operation:" + operation);
//		if (Operation.MSG_TYPE_BROWSER.equals(operation.msgType)) {
			H5Activity_.intent(this).remoteDisplayName(remoteDisplayName)
					.url(operation.action).start();
//		} else if (Operation.MSG_TYPE_REST.equals(operation.msgType)) {
//			doInBackgroundOperation(operation.action);
//		}
	}

	@Override
	public void onOperationsClick(List<Operation> operations) {
//		Log.d(TAG, "onOperationsClick() operations:" + operations);
		OperationsMoreDialogFragment fragment = OperationsMoreDialogFragment_
				.builder().build();
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		operations = new ArrayList<Operation>(operations.subList(2, operations.size()));
		args.putSerializable("operations", (Serializable) operations);
		fragment.setArguments(args);
		fragment.setOnOperationClickListener(this);
		fragment.show(getSupportFragmentManager(),
				OperationsMoreDialogFragment.class.getName());
	}
	
	boolean doInBackgroundGetAppOperationListCanceld = false;
	
	@Background
	void doInBackgroundGetAppOperationList() {
		Log.d(TAG, "doInBackgroundGetAppOperationList() remoteUserName:" + remoteUserName);

		BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		GetAppOpertionListRequest request = new GetAppOpertionListRequest();
		request.BaseRequest = baseRequest;
		request.username = remoteUserName;
		
		Log.d(TAG, "doInBackgroundGetAppOperationList() request:" + request);
//		GetAppOpertionListResponse response = myRestClient.getAppOpertionList(request);
//		Log.d(TAG, "doInBackgroundGetAppOperationList() response:" + response);
//
//		onPostExecuteGetAppOperationList(response);
	}

	@UiThread
	void onPostExecuteGetAppOperationList(
			GetAppOpertionListResponse response) {
		Log.d(TAG, "onPostExecuteGetAppOperationList() response:" + response);
		
		if (doInBackgroundGetAppOperationListCanceld) {
			return;
		}
		
		if (response == null) {
			Toast.makeText(this, "获取应用菜单失败", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
//			Toast.makeText(this, response.BaseResponse.ErrMsg,
//					Toast.LENGTH_SHORT).show();
			Toast.makeText(this, "获取应用菜单失败", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String key = "GetAppOpertionList_" + remoteUserName;
		GetAppOpertionListResponse responseCache = (GetAppOpertionListResponse) ACache.get(this).getAsObject(key);
		if (!isEqueals(response, responseCache)) {
			viewFooter.bind(remoteUserName, response.opertionList);
			ACache.get(this).put(key, response);
		}
	}
	
	private boolean isEqueals(GetAppOpertionListResponse r1, GetAppOpertionListResponse r2) {
		String str1 = Utils.writeValueAsString(r1);
		String str2 = Utils.writeValueAsString(r2);
		if (str1 != null && str1.equals(str2)) {
			return true;
		}
		return false;
	}
	
	private void initViewFooter() {
		String key = "GetAppOpertionList_" + remoteUserName;
		GetAppOpertionListResponse response = (GetAppOpertionListResponse) ACache.get(this).getAsObject(key);
		if (response != null && response.BaseResponse.Ret == BaseResponse.RET_SUCCESS) {
			viewFooter.bind(remoteUserName, response.opertionList);
		}
	}
	
	@ItemClick(android.R.id.list)
	void onChatMessagesItemClick(int position) {
		Message message = mChatAdapter.readEntity((Cursor) mChatAdapter.getItem(position));
		if (message.getMsgType() == Msg.MSG_TYPE_IMAGE) {
			ChatImagesActivity_.intent(ChatActivity.this)
					.localUserName(message.getExtLocalUserName())
					.remoteUserName(message.getExtRemoteUserName())
					.remoteDisplayName(message.getExtRemoteDisplayName())
					.currentMsgId(message.getId())
					.start();
		}
	}
	
	
	float moveY = 0;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.d("FramMainActionBarActivity", "dispatchTouchEvent ev :"+ev.getAction());
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
			Log.d(TAG, "dispatchTouchEvent actionDown touchY:"+ev.getY());
		moveY =  ev.getY();
		}
		if(ev.getAction() == MotionEvent.ACTION_MOVE ){
			Log.d(TAG, "dispatchTouchEvent actionmove touchY:"+ev.getY());
			if(moveY - ev.getY() > 100 || ev.getY() - moveY > 100)
			activityYMove();
//			touchY = (int) ev.getY();
		}
		return super.dispatchTouchEvent(ev);
	}
	
	protected void activityYMove(){
		AllUtils.hiddenSoftBorad(this);
	}

	@Override
	public void finish() {
		if(mChatAdapter != null){
			mChatAdapter.stopPlayVoice(true);
		}
		Intent i = new Intent(ChatActivity.this, FramMainActivity.class);
		i.putExtra("fanye", 1);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		super.finish();
	}
	
	

}
