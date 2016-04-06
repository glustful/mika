package com.miicaa.home.ui.matter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.mapcore2d.ap;
import com.miicaa.MainActivity;
import com.miicaa.base.request.response.BaseResopnseData.OnFileResponseListener;
import com.miicaa.base.request.response.BaseResopnseData.OnResponseListener;
import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PicturButton;
import com.miicaa.common.base.PopupItem;
import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.DialogHelper;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.ContactData;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.home.ui.matter.FileViewAdapter.DelpictureListener;
import com.miicaa.home.ui.matter.approveprocess.ApprovalProcessListActivity_;
import com.miicaa.home.ui.matter.approveprocess.ApprovalProcessTypeView;
import com.miicaa.home.ui.matter.approveprocess.ApprovalProcessTypeView.OnProcessChangeListener;
import com.miicaa.home.ui.matter.approveprocess.ApproveFixedGroupActivity_;
import com.miicaa.home.ui.matter.approveprocess.ApproveFixedSingleActivity_;
import com.miicaa.home.ui.menu.SelectPersonInfo;
import com.miicaa.home.ui.org.ArragementLabEdit;
import com.miicaa.home.ui.org.DateTimePopup;
import com.miicaa.home.ui.org.MatterHttp;
import com.miicaa.home.ui.org.StyleDialog;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.photoGrid.Bimp;
import com.miicaa.home.ui.photoGrid.ImageItem;
import com.miicaa.home.ui.photoGrid.PhotoGridContentActivity;
import com.miicaa.home.ui.photoGrid.SelectPictureActivity_;
import com.miicaa.home.ui.picture.PictureHelper;
import com.miicaa.home.ui.report.ReportDetailInfo;
import com.miicaa.home.ui.widget.UploadWidget;
import com.miicaa.service.ProgressNotifyService;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.AuthorityUtils.AuthorityState;
import com.miicaa.utils.fileselect.MyFileItem;
import com.yxst.epic.yixin.MyApplication;

/**
 * Created by Administrator on 14-2-8.
 */
public class MatterEditor extends MainActivity {

	static String TAG = "MatterEditor";

	public final static int GRID_PHOTO_CHECK = 0x09;
	private final static int PROCESS_SELECT = 0x111;
	
	private static int Service_File_Length = 1;
	
	String mId;
	String mParentId;
	String isEditor;
	String modelId;
	String mDataType;
	String mMatterType;
	String mEditType;
	String mApproveContent;
	String mPhotoPath;
	String allLableId;
	int processStep;
	Builder errorDialog;
	//FileViewAdapter adapter;
	ArrayList<Bitmap> bitMaps = new ArrayList<Bitmap>();
	ArrayList<String> fids = new ArrayList<String>();
	ProgressDialog progressDialog;
	ArrayList<String> getPath = new ArrayList<String>();
	ArrayList<String> lableEditors = new ArrayList<String>();
	ArrayList<String> labelIds = new ArrayList<String>();
	ArrayList<ContactData> conData = new ArrayList<ContactData>();
	ArrayList<HashMap<String, String>> labelData = new ArrayList<HashMap<String, String>>();
	HashMap<String, Bitmap> webPic = new HashMap<String, Bitmap>();// 网络图片保存后便于展现

	String mMono;
	ArrayList<SamUser> mTodoUsers;// 审批人
	ArrayList<SamUser> mCopyUsers;// 抄送人
	Date mBeginDate;
	Date mFinshDate;
	String mRepeat;
	boolean isProcess;
	boolean iHomeEnable = false;
	Boolean mAuto = false;
	Boolean mRange = true;
	Boolean mAutoEnable = false;
	
	private int mProcessKey = ApprovalProcessTypeView.FREE;

	TextView redStarToContent;
	TextView mTitleText;
	TextView mNewTitleText;
	EditText mEditTitle;
	EditText mMonoText;
	LinearLayout mMattorLayout;
	TextView mMattorText;
	LinearLayout mBeginLayout;
	TextView mBeginText;
	LinearLayout mEndLayout;
	TextView mEndText;
	LinearLayout mRepeatLayout;
	TextView mRepeatText;
	LinearLayout mAutoLayout;
	TextView mAutoText;
	LinearLayout mRangeLayout;
	TextView mRangeText;
	LinearLayout mCcLayout;
	TextView mCcText;
	LinearLayout mFileAddLayout;
	RelativeLayout mRelativeLayout;
	ScrollView mScrollView;
	LinearLayout mWholeLayout;
	ImageView bgLine;

	LinearLayout lableLayout;
	TextView lableTextView;
	TextView height;
	ContactUtil contactUtil;

	StyleDialog dialog;
	ReportDetailInfo mInfo;
	JSONObject jobj;

	ApprovalProcessTypeView approvalProcessTypeView;
	Button showProcessButton;
	UploadWidget mUploadWidget;
	ArrayList<MyFileItem> files;

//	Toast finishToast;
	Toast mToast;

	@Override
	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		errorDialog = new AlertDialog.Builder(this);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		setContentView(R.layout.matter_editor_activity);
		mProcessKey = getIntent().getIntExtra("process", -1);
		iHomeEnable = getIntent().getBooleanExtra("homeEnable", false);
		modelId = getIntent().getStringExtra("modelId");
		Log.d(TAG, "mProcessKey:"+mProcessKey);
		dialog = (StyleDialog) AllUtils.getMaualStyleDialog(this);
		mTodoUsers = new ArrayList<SamUser>();
		mCopyUsers = new ArrayList<SamUser>();
		mUploadWidget = new UploadWidget(this);
		initIntentData();
		initUI();
		initData();

	}

	@Override
	protected void onRestart() {
		super.onRestart();

	}

	// 添加图片后处理
	private void initBmpCheck() {

		new Thread() {
			@Override
			public void run() {
				ArrayList<MatterImgManager> imageManagerList = new ArrayList<MatterImgManager>();
				for (int i = 0; i < Bimp.drr.size(); i++) {
					try {
						Bitmap bitmap = Bimp.revitionImageSize(Bimp.drr.get(i));
						Bundle bundle = new Bundle();
						bundle.putInt("count", i);
						imageManagerList.add(new MatterImgManager(null, bitmap,
								Bimp.drr.get(i)));
					} catch (IOException e) {

					}
				}
				handler.obtainMessage(1, imageManagerList).sendToTarget();
			}
		}.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ArrayList<MatterImgManager> imageManagerList = (ArrayList<MatterImgManager>) msg.obj;
			Log.d(TAG, "imageManagerList :" + imageManagerList.size());
		}
	};

	private void delBmp() {
		Bimp.drr.clear();

		Bimp.bmpMap.clear();
	}

	private void initIntentData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		mDataType = bundle.getString("dataType");
		mMatterType = bundle.getString("matterType");
		mApproveContent = bundle.getString("content");
		mEditType = bundle.getString("editType");
		mParentId = bundle.getString("parentId");
		
		if (mEditType.equals("01")) {
			mId = "";
		} else {
			mId = bundle.getString("id");
		}
	}

	private void initData() {
		mMono = "";
		mBeginDate = null;
		mFinshDate = null;
		mRepeat = "";
		mAuto = false;
		mRange = true;
		setTodoUser();
		mMonoText.setText(mMono);
		mMonoText.setText(mApproveContent);
		setBeginDate(mBeginDate);
		setFinishDate(mFinshDate);
		setRepeatData(mRepeat);
		setAutoMode(mAuto);
		setRangeMode(mRange);
		setCc(mCopyUsers);
		if (mEditType.equals("02")) {
			requestEditData();
			requestPerson();
			requestPicture();
		} else if (mEditType.equals("03")) {
			mInfo = (ReportDetailInfo) getIntent().getBundleExtra("bundle")
					.getSerializable("info");
			try {
				jobj = new JSONObject(getIntent().getBundleExtra("bundle")
						.getString("item"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mEditTitle.setText(jobj.optString("title"));

			if (jobj == null)
				return;
			if (!jobj.isNull("planStartTime")&&jobj.optLong("planStartTime")>0) {
				mEndLayout.setClickable(true);
				mBeginDate = new Date(jobj.optLong("planStartTime", 0));
				setBeginDate(mBeginDate);
				
			}

			if (!jobj.isNull("planEndTime")&&jobj.optLong("planEndTime")>0){
				mFinshDate = new Date(jobj.optLong("planEndTime", 0));
				setFinishDate(mFinshDate);
				
			}

			mTodoUsers.add(new SamUser(mInfo.getCreatorCode(), mInfo
					.getCreatorName()));

			setTodoUser();

		}

	}

	private void requestEditData() {
		dialog.show();
		String url = "/home/phone/thing/approveread";
		if (mDataType.equals("1")) {
			url = "/home/phone/thing/arrangeread";
		}
		new RequestAdpater() {
			private static final long serialVersionUID = -7168382901064417952L;

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onReponse(ResponseData data) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (data.getResultState() == ResponseData.ResultState.eSuccess) {
					JSONObject jRow = data.getJsonObject();
					Log.d(TAG, "request editData:"+jRow);
					if (!jRow.isNull("content")) {
						mMonoText.setText(jRow.optString("content"));
					}
					if (!jRow.isNull("title")) {
						mEditTitle.setText(jRow.optString("title"));
					}
					if (!jRow.isNull("approveType")) {
						mMatterType = jRow.optString("approveType");
					}
					if (!jRow.isNull("labels")) {
						JSONArray labels = jRow.optJSONArray("labels");
						String labelText = "";
						String labelAllId = "";
						for (int j = 0; j < labels.length(); j++) {
							JSONObject labelObject = labels.optJSONObject(j);
							String labelString = labelObject.optString("label");
							String labelId = labelObject.optString("id");
							lableEditors.add(labelString);
							labelIds.add(labelId);
							if (j != labels.length() - 1) {
								labelString += ",";
								labelId += ",";
							}
							labelText = labelText + labelString;
							labelAllId = labelAllId + labelId;
						}
						lableTextView.setText(labelText);
						allLableId = labelAllId;
					}
					if (mDataType.equals("1")) {

						if (!jRow.isNull("startTime")) {
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm");
							mBeginDate = new Date(jRow.optLong("startTime"));
							String str = format.format(mBeginDate);
							mBeginText.setText(str);
							mEndLayout.setClickable(true);
						}

						if (!mMatterType.equals("0")) {
							if (!jRow.isNull("secrecy")) {
								if (jRow.optString("secrecy").equals("1")) {
									mRange = false;
								} else {
									mRange = true;
								}

								setRangeMode(mRange);
							}
						}

						if (!jRow.isNull("autoFinish")) {
							if (jRow.optString("autoFinish").equals("1")) {
								mAutoEnable = true;
								mAuto = true;
							} else {
								mAutoEnable = false;
								mAuto = false;
							}
							setAutoEnable(mAutoEnable);
						}
						if (!jRow.isNull("endTime")) {
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm");
							mFinshDate = new Date(jRow.optLong("endTime"));
							String str = format.format(mFinshDate);
							mEndText.setText(str);
							mAutoEnable = true;

							setAutoEnable(mAutoEnable);
						}
					} else {

					}

				} else {
					Toast.makeText(MatterEditor.this, "错误" + data.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {
			}
		}.setUrl(url).addParam("dataId", mId).notifyRequest();
	}

	private void requestApprover() {
		Utils.approvalUseData(MatterEditor.this, mId,
				new Utils.CallBackListener() {
					@Override
					public void callBack(ResponseData data) {

					}

					@Override
					public void callBackJson(JSONArray jsonArray) {
						String oCode = "";
						HashMap<String, String> ma = new HashMap<String, String>();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.optJSONObject(i);
							if (!"4".equals(jsonObject.optString("status"))) {
								String name = jsonObject.optString("userName");
								String code = jsonObject.optString("userCode");
								if (code != null && name != null)
									ma.put(code, name);
							}
							Log.d(TAG, "callBackJson map :" + ma);
						}
						for (Map.Entry<String, String> e : ma.entrySet()) {
							mTodoUsers.add(new SamUser(e.getKey(), e.getValue()));
						}
						setTodoUser();
						dialog.dismiss();
					}

					@Override
					public void callbackNull() {
						dialog.dismiss();
					}
				});
	}

	ArrayList<SamUser> mEditCopyPerson = new ArrayList<SamUser>();
	private void requestPerson() {
		String url = "/home/phone/thing/getinvolvepeople";

		new RequestAdpater() {
			@Override
			public void onReponse(ResponseData data) {

				if (data == null) {
					return;
				}
				if (data.getResultState() == ResponseData.ResultState.eSuccess) {
					JSONArray jTodo = data.getJsonObject().optJSONArray(
							"toDoUsers");
					JSONArray jCopy = data.getJsonObject().optJSONArray(
							"copyUsers");
					if (jCopy != null) {
						for (int i = 0; i < jCopy.length(); i++) {
							mEditCopyPerson.add(new SamUser(jCopy.optJSONObject(i)
									.optString("userCode"), jCopy
									.optJSONObject(i).optString("userName")));

						}
						setCc(mEditCopyPerson);
					}
					if (jTodo != null) {
						if ("2".equals(mDataType)) {
							requestApprover();
						} else {
							for (int i = 0; i < jTodo.length(); i++) {
								mTodoUsers
										.add(new SamUser(jTodo.optJSONObject(i)
												.optString("userCode"), jTodo
												.optJSONObject(i).optString(
														"userName")));
							}
							setTodoUser();
							dialog.dismiss();
						}
					}

				} else {
					Toast.makeText(MatterEditor.this, "错误", Toast.LENGTH_SHORT)
							.show();
					dialog.dismiss();
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {
			}
		}.setUrl(url).addParam("dataId", mId).notifyRequest();
	}

	Boolean showLayout;
	/**
	 * 控制上图片总数
	 */
	int mBitmapCount;

	private void requestPicture() {

		PictureHelper helper = new PictureHelper(mId,this);
		helper.setLoadCallBack(new PictureHelper.PictureLoadCallBack() {
			@Override
			public void loadImg(Bitmap bitmap, String fid) {
				if (bitmap != null && fid != null) {

					webPic.put(fid, bitmap);
				}
			}


			@Override
			public void errorCall(String msg) {
				Toast.makeText(MatterEditor.this, "连接错误" + msg,
						Toast.LENGTH_SHORT).show();
				if ("0".equals(mMatterType))
					dialog.dismiss();
			}

			@Override
			public void loadComplete() {
//				mBitmapCount = webPic.size();
//				initWebPic();
			}

			@Override
			public void loadFileComplete(ArrayList<MyFileItem> tmps,
					ArrayList<String> pictureList) {
				mUploadWidget.initPhotoOrFile(tmps, pictureList);
			}

			
		});
		helper.getAttachement();
	}

	private void initWebPic() {
		new AsyncTask<Integer, Integer, ArrayList<MatterImgManager>>() {
			ArrayList<MatterImgManager> imageManagerList = new ArrayList<MatterImgManager>();
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected ArrayList<MatterImgManager> doInBackground(
					Integer... params) {
				
				for (Map.Entry<String, Bitmap> e : webPic.entrySet()) {
					String fid = e.getKey();
					Bitmap bitmap = e.getValue();
					MatterImgManager imgManager = new MatterImgManager(fid,
							bitmap, null);
					imageManagerList.add(imgManager);
				}
				return imageManagerList;

			}

			@Override
			protected void onPostExecute(ArrayList<MatterImgManager> result) {
				//adapter.add(result);
				if(mUploadWidget!=null){
//					mUploadWidget.initPhotoOrFile(files, imageManagerList);
				}
				super.onPostExecute(result);
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}

		}.execute(1);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void initUI() {
		mScrollView = (ScrollView) findViewById(R.id.matter_editor_scrollview);
		mWholeLayout = (LinearLayout) findViewById(R.id.matter_editor_whole_layout);
		mTitleText = (TextView) findViewById(R.id.matter_editor_id_title_t);
		mNewTitleText = (TextView) findViewById(R.id.matter_editor_id_title);
		mMonoText = (EditText) findViewById(R.id.matter_editor_id_mono);
		mMonoText.setOnFocusChangeListener(keyBordListener);
		redStarToContent = (TextView) findViewById(R.id.matter_editor_biaozhi);
		if (!"1".equals(mDataType)) {
			redStarToContent.setVisibility(View.VISIBLE);
		}

		mEditTitle = (EditText) findViewById(R.id.matter_editor_id_edittitle);
		mEditTitle.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				Log.d(TAG, "start:"+start+"before:"+before+"count:"+count+"str:"+s);
				if(start > 0 && s.toString().length() == 35 ){
					PayUtils.showToast(MatterEditor.this, "标题不能超过35个字", 3000);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		mBeginLayout = (LinearLayout) findViewById(R.id.matter_editor_id_begin_layout);
		mBeginText = (TextView) findViewById(R.id.matter_editor_id_begin);
		mBeginLayout.setOnClickListener(onBeginClick);

		mEndLayout = (LinearLayout) findViewById(R.id.matter_editor_id_finish_layout);
		mEndText = (TextView) findViewById(R.id.matter_editor_id_finish);
		mEndLayout.setOnClickListener(onFinishClick);

		mRepeatLayout = (LinearLayout) findViewById(R.id.matter_editor_id_repeat_layout);
		mRepeatText = (TextView) findViewById(R.id.matter_editor_id_repeat);
		mRepeatLayout.setOnClickListener(onRepeatClick);
		mAutoLayout = (LinearLayout) findViewById(R.id.matter_editor_id_auto_layout);
		mAutoText = (TextView) findViewById(R.id.matter_editor_id_auto);
		mAutoLayout.setOnClickListener(onAutoClick);
		lableLayout = (LinearLayout) findViewById(R.id.matter_editor_biaoqian_layout);
		lableTextView = (TextView) findViewById(R.id.matter_editor_biaoqian);
		lableLayout.setOnClickListener(onLableClick);
		mRangeLayout = (LinearLayout) findViewById(R.id.matter_editor_id_rang_layout);
		mRangeText = (TextView) findViewById(R.id.matter_editor_id_rang);
		mRangeLayout.setOnClickListener(onRangeClick);

		LinearLayout arrangeLayout = (LinearLayout) findViewById(R.id.matter_editor_id_arrange);
		LinearLayout approvalLayout = (LinearLayout) findViewById(R.id.matter_editor_id_approval);

		height = (TextView) findViewById(R.id.matter_editor_height);
		mFileAddLayout = (LinearLayout) findViewById(R.id.matter_editor_id_att_picture);
		mFileAddLayout.addView(mUploadWidget.getRootView());
		height.setVisibility(View.GONE);

		mRelativeLayout = new RelativeLayout(this);
		mRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		if (mDataType.equals("1")) {
			arrangeLayout.setVisibility(View.VISIBLE);
			approvalLayout.setVisibility(View.GONE);
			mMattorLayout = (LinearLayout) findViewById(R.id.matter_editor_id_mattor_layout);
			mMattorText = (TextView) findViewById(R.id.matter_editor_id_mattor);
			mMattorLayout.setOnClickListener(onMattorClick);
			mCcLayout = (LinearLayout) findViewById(R.id.matter_editor_id_cc_layout);
			mCcText = (TextView) findViewById(R.id.matter_editor_id_cc);
			mCcLayout.setOnClickListener(onCcClick);
			mWholeLayout.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent motionEvent) {
					switch (motionEvent.getAction()) {
					case MotionEvent.ACTION_DOWN:
						try {
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											MatterEditor.this.getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
							return true;
						} catch (Exception e) {

							return false;
						}
					case MotionEvent.ACTION_MOVE:
						try {
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											MatterEditor.this.getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
							return true;
						} catch (Exception e) {
							return false;
						}
					default:
						break;
					}
					return false;
				}

			});
			if ("0".equals(mMatterType)) {
				mMattorLayout.setVisibility(View.GONE);
				mRangeLayout.setVisibility(View.GONE);
				mCcLayout.setVisibility(View.GONE);
				mBeginLayout.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.white_icon_selector));
				mEndLayout.setBackground(getResources().getDrawable(
						R.drawable.white_icon_selector));
				LinearLayout.LayoutParams p = (android.widget.LinearLayout.LayoutParams) mAutoLayout
						.getLayoutParams();
				p.setMargins(0, 0, 0, 0);
				mAutoLayout.setLayoutParams(p);
				mAutoLayout.setBackground(getResources().getDrawable(
						R.drawable.white_icon_selector));
			}

			if (mEditType.equals("01")) {
				if (mParentId != null && !mParentId.equals("")) {
					mTitleText.setText("新建子任务");
					mRangeLayout.setVisibility(View.GONE);
				} else {
					mTitleText.setText("0".equals(mMatterType) ? "新建私事"
							: "新建公事");
				}

			} else if (mEditType.endsWith("03")) {

				mTitleText.setText("0".equals(mMatterType) ? "新建私事"
						: "新建公事");

			} else {
				mTitleText.setText("0".equals(mMatterType) ? "编辑私事" : "编辑公事");
			}
		} else {
			arrangeLayout.setVisibility(View.GONE);
			approvalLayout.setVisibility(View.VISIBLE);
			mMattorLayout = (LinearLayout) findViewById(R.id.matter_editor_id_assess_layout);
			//办理流程
			showProcessButton = (Button)findViewById(R.id.showProcessView);
			showProcessButton.setOnClickListener(onShowProcessClick);
			showProcessButton.setVisibility(mProcessKey == ApprovalProcessTypeView.FIXED ? View.VISIBLE : View.GONE);
			if(MyApplication.getInstance().getAuthority(AuthorityState.eApproveProcess) == 
					AllUtils.NORMAL_User){
				mProcessKey = ApprovalProcessTypeView.FREE;
				if(!mEditType.equals("02")){
					showProcessButton.setVisibility(View.GONE);
				}
			}
			approvalProcessTypeView = (ApprovalProcessTypeView) findViewById(R.id.approvalProcessTypeView);
			approvalProcessTypeView.setVisibility(mProcessKey == ApprovalProcessTypeView.FIXED ? View.VISIBLE : View.GONE);
			approvalProcessTypeView
			.setOnProcessChangeListener(onProcessChangeListener);
			if(mEditType.equals("02")){
				approvalProcessTypeView.clickable(false);
			}
			
			mMattorText = (TextView) findViewById(R.id.matter_editor_id_assess);
			mMattorLayout.setOnClickListener(onMattorClick);
			mCcLayout = (LinearLayout) findViewById(R.id.matter_editor_id_approcc_layout);
			mCcText = (TextView) findViewById(R.id.matter_editor_id_approcc);
			mCcLayout.setOnClickListener(onCcClick);
			if (mEditType.equals("01")) {
				mTitleText.setText("新建审批");
			} else {
				mTitleText.setText("编辑审批");
			}
		}
		Button button = (Button) findViewById(R.id.matter_editor_id_back);
		button.setOnClickListener(onBackClick);
		button = (Button) findViewById(R.id.matter_editor_id_save);
		button.setOnClickListener(onSaveClick);
	}

	// 填写完内容后收起键盘
	View.OnFocusChangeListener keyBordListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View view, boolean b) {
			Utils.hiddenSoftBorad(MatterEditor.this);
		}
	};

	private void delWebPic(final MatterImgManager imageManager) {
		final String fid = imageManager.fid;
		if (fid != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MatterEditor.this)
					.setTitle("提示")
					.setMessage("确定删除吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int i) {
									String url = "/home/proupload/pc/component/upload/delete";
									new RequestAdpater() {

										@Override
										public void onReponse(ResponseData data) {
											Toast.makeText(MatterEditor.this,
													"删除成功", Toast.LENGTH_SHORT)
													.show();
											mBitmapCount--;
											//adapter.remove(imageManager);
											// if (adapter.getCount() == 0){
											// mFileAddLayout.setVisibility(View.GONE);
											// }
										}

										@Override
										public void onProgress(
												ProgressMessage msg) {
											Toast.makeText(MatterEditor.this,
													"删除失败", Toast.LENGTH_SHORT)
													.show();
										}
									}.setUrl(url).addParam("id", fid)
											.notifyRequest();
									dialogInterface.dismiss();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int i) {
									dialogInterface.dismiss();
								}
							});
			builder.show();
		} else {
			//adapter.remove(imageManager);
			Bimp.drr.remove(imageManager.path);
		}
	}

	// 图片上传
	PicturButton.OnPictureListener onPictureListener = new PicturButton.OnPictureListener() {
		@Override
		public void onPictureClick(String msg,Bitmap b) {
			ArrayList<ImageItem> items = new ArrayList<ImageItem>();
			for (String s : Bimp.drr) {
				ImageItem item = new ImageItem();
				item.iamge_id = "";
				item.image_path = s;
				item.thumbnailPath = "";
				items.add(item);
			}
			ImageItem item = new ImageItem();
			item.iamge_id = "";
			item.image_path = msg;
			item.thumbnailPath = "";

			SelectPictureActivity_.intent(MatterEditor.this).imageItems(items)
					.imageItem(item).noOperation(true).start();
		}

		@Override
		public void onAddPictureClick() {
			ArrayList<PopupItem> items = new ArrayList<PopupItem>();
			items.add(new PopupItem("从手机相册选择", "local"));
			items.add(new PopupItem("取消", "cancel"));
			BottomScreenPopup.builder(MatterEditor.this).setItems(items)
					.setDrawable(R.drawable.white_color_selector)

					.setMargin(false)
					.setOnMessageListener(new OnMessageListener() {
						@Override
						public void onClick(PopupItem msg) {
							if ("cancel".equals(msg.mCode)) {
							} else {
								Intent i = new Intent(MatterEditor.this,
										PhotoGridContentActivity.class);
								Log.d(TAG, "bitMapCount:" + mBitmapCount);
								i.putExtra("bitmapCount", mBitmapCount);
								startActivityForResult(i, GRID_PHOTO_CHECK);
							}
						}
					}).show();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if(mUploadWidget!=null){
			mUploadWidget.onActivityResult(requestCode, resultCode, data);
		}
		switch (requestCode) {
		case 2: {
			ArrayList<ContactViewShow> arrData = data
					.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
			if (arrData != null) {
				mTodoUsers.clear();
				ContactUtil.setSelectPeopleData(arrData,
						new ContactUtil.PeopleCallbackListener() {
							@Override
							public void callBack(ArrayList<SamUser> data,
									String user) {
								for (SamUser s : data) {
									mTodoUsers.add(s);
								}
								mMattorText.setText(user);
							}
						});
			}
				
			break;
		}
		case GRID_PHOTO_CHECK:
			//initBmpCheck();
			break;
		case 3: {
			ArrayList<ContactViewShow> appData = data
					.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
			if (appData != null) {
				mTodoUsers.clear();
				ContactUtil.setSelectPeopleData(appData,
						new ContactUtil.PeopleCallbackListener() {
							@Override
							public void callBack(ArrayList<SamUser> data,
									String user) {
								for (SamUser s : data) {
									mTodoUsers.add(s);
								}
								mMattorText.setText(user);
							}
						});
			}

		}
			break;
			
		case PROCESS_SELECT:
			processStep = data.getIntExtra("step", -1);
			ArrayList<SelectPersonInfo> infoList = data.getParcelableArrayListExtra("process");
			String showName = "";
			if(infoList != null){
				mTodoUsers.clear();
				for(SelectPersonInfo info : infoList){
					SamUser user = new SamUser(info.mCode, info.mName);
//					showName += info.mName + ",";
					mTodoUsers.add(user);
				}
				if(infoList.size() >= 1){
					showName += infoList.get(0).mName;
					if(mTodoUsers.size() > 1){
					showName += "等"+mTodoUsers.size()+"个人";
					}
				}
//				if(showName.length() > 0){
//					showName = showName.substring(0, showName.length()-1);
//				}
				mMattorText.setText(showName);
			}
			break;
		case 4: {

			ArrayList<ContactViewShow> copyDatas = data
					.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
			if(copyDatas == null)
				return;
				mCcText.setText("");
				String str = "";
				mCopyUsers.clear();
				for(ContactViewShow copy : copyDatas){
				    mCopyUsers.add(new SamUser(copy.code, copy.name));
				}
//				for(SamUser s : mEditCopyPerson){
//					str += s.getmName();
//				}
				mCopyUsers.addAll(mEditCopyPerson);
				if(mCopyUsers.size() > 0){
					str = mCopyUsers.get(0).getmName()+"等"+mCopyUsers.size()+"个人";
				}
				mCcText.setText(str);
//				ContactUtil.setSelectPeopleData(copyDatas,
//						new ContactUtil.PeopleCallbackListener() {
//							@Override
//							public void callBack(ArrayList<SamUser> data,
//									String user) {
//								for (SamUser s : data) {
//									mCopyUsers.add(s);
//								}
//								mCcText.setText(user);
//							}
//						});
//			setCopyData(copyDatas);

		}
			break;
		case 10: {
			Bundle bundle = data.getBundleExtra("bundle");
			String[] strings = new String[0];
			String[] lableIds = new String[0];
			String lableString = "";
			String lables = "";
			allLableId = "";
			if (bundle.getStringArray("lables") != null) {
				strings = bundle.getStringArray("lables");
			}
			if (bundle.getStringArray("lableId") != null) {
				lableIds = bundle.getStringArray("lableId");
			}

			if (lableEditors.size() > 0) {
				lableEditors.clear();
				labelIds.clear();
				lableTextView.setText("");
			}
			if (strings.length == 0 || lableIds.length == 0) {
				return;
			}
			for (int j = 0; j < strings.length; j++) {
				lableEditors.add(strings[j]);
				labelIds.add(lableIds[j]);
				if (j != strings.length - 1) {
					strings[j] += ",";
					lableIds[j] += ",";
				}
				lableString = lableString + strings[j];
				lables = lables + lableIds[j];
			}
			allLableId = lables;
			lableTextView.setText(lableString);
		}
		}
	}

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";

	}

	public static void deletePhotoAtPathAndName(String path, String fileName) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].getName());
			if (files[i].getName().equals(fileName)) {
				files[i].delete();
			}
		}
	}

	Bitmap decodeSampledBitmapFromStream(InputStream is, int reqWidth,
			int reqHeight) {

		try {
			byte[] bytesIs = readStream(is);
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bytesIs, 0, bytesIs.length, options);
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeByteArray(bytesIs, 0, bytesIs.length,
					options);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readStream(InputStream in) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		while ((len = in.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		in.close();
		return data;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	private void setTodoUser() {
		if (mTodoUsers == null) {
			mMattorText.setText("");
			return;
		}

		String user = "";
//		for (int i = 0; i < mTodoUsers.size(); i++) {
//			user += mTodoUsers.get(i).getmName();
//			if (i != (mTodoUsers.size() - 1)) {
//				user += ",";
//			}
//		}
		if(mTodoUsers.size() >= 1){
			user += mTodoUsers.get(0).getmName();
			if(mTodoUsers.size() > 1){
			user += "等"+mTodoUsers.size()+"个人";
			}
		}
		mMattorText.setText(user);
	}

	private void setBeginDate(Date date) {
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String str = format.format(date);
			mBeginText.setText(str);
		} else {
			mBeginText.setText("");
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void setFinishDate(Date date) {
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String str = format.format(date);
			mEndText.setText(str);
			setAutoEnable(true);
		} else {
			mEndText.setText("");
			setAutoEnable(false);
		}
	}

	private void setRepeatData(String repeatData) {

	}

	private void setAutoMode(Boolean auto) {
		if (!mAutoEnable) {
			mAutoLayout.setClickable(false);
			mAutoText.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
					null);
		} else {
			mAutoLayout.setClickable(true);
			if (auto) {
				mAutoText.setCompoundDrawablesWithIntrinsicBounds(null, null,
						getResources().getDrawable(R.drawable.round_select),
						null);
			} else {
				mAutoText.setCompoundDrawablesWithIntrinsicBounds(null, null,
						getResources().getDrawable(R.drawable.round_noselect),
						null);
			}
		}
	}

	private void setAutoEnable(Boolean enable) {
		mAutoEnable = enable;
		setAutoMode(mAuto);
	}

	private void setRangeMode(Boolean official) {
		if (official) {
			mRangeText.setCompoundDrawablesWithIntrinsicBounds(null, null,
					getResources().getDrawable(R.drawable.round_select), null);
		} else {
			mRangeText
					.setCompoundDrawablesWithIntrinsicBounds(
							null,
							null,
							getResources().getDrawable(
									R.drawable.round_noselect), null);
		}
	}

	private void setCc(ArrayList<SamUser> mCopyUsers) {
		if (mCopyUsers == null) {
			mCcText.setText("");
			return;
		}

		String user = "";
		if(mCopyUsers.size() >= 1){
			user += mCopyUsers.get(0).getmName();
			if(mCopyUsers.size() > 1){
			user += "等"+mCopyUsers.size()+"个人";
			}
		}
		mCcText.setText(user);
	}

	@Override
	public void finish() {
		Bimp.clearMap();
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
	

	View.OnClickListener onMattorClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent intent = new Intent(MatterEditor.this, SelectContacter.class);
			Bundle bundle = new Bundle();

			ArrayList<String> name = new ArrayList<String>();
			ArrayList<String> code = new ArrayList<String>();
			ArrayList<SelectPersonInfo> infoList = new ArrayList<SelectPersonInfo>();
			if (mTodoUsers.size() > 0) {
				for (SamUser s : mTodoUsers) {
					SelectPersonInfo info = new SelectPersonInfo(s.getmName(), s.getmCode());
					infoList.add(info);
					name.add(s.getmName());
					code.add(s.getmCode());
					
				}
				bundle.putStringArrayList("name", name);
				bundle.putStringArrayList("code", code);
			}
			if(mProcessKey == ApprovalProcessTypeView.FIXED){
				
				if(mEditType.equals("02")){
					ApproveFixedGroupActivity_.intent(MatterEditor.this)
					.dataId(mId)
					.mSelectPersonList(infoList)
					.startForResult(PROCESS_SELECT);

				}else{
					ApproveFixedSingleActivity_.intent(MatterEditor.this)
					.dataType(mMatterType)
					.mSelectCodeList(code)
					.startForResult(PROCESS_SELECT);
				}
				
			}else {
			

			if (mDataType.equals("1")) {
				bundle.putString(SelectContacter.how, SelectContacter.ARRANGE);
				intent.putExtra("bundle", bundle);
				startActivityForResult(intent, 2);
			} else {
				bundle.putString(SelectContacter.how,
						SelectContacter.NEWAPPROVE);
				intent.putExtra("bundle", bundle);
				startActivityForResult(intent, 3);
			}
		}
		}
	};

	private void setCopyData(ArrayList<ContactViewShow> data) {
		mCopyUsers.clear();
		String user = "";
		String conUser = "";
		for (int i = 0; i < data.size(); i++) {
			mCopyUsers.add(new SamUser(data.get(i).getCode(), data.get(i)
					.getName()));
			user = data.get(i).getName();
			if (i < data.size() - 1) {
				user += ",";

			}
			conUser = conUser + user;
			mCcText.setText(conUser.toString());
		}
	}

	View.OnClickListener onLableClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {

			Intent intent = new Intent(MatterEditor.this,
					ArragementLabEdit.class);
			Bundle bundle = new Bundle();
			if (lableEditors != null && lableEditors.size() > 0) {
				bundle.putStringArrayList("lableEdit", lableEditors);
				bundle.putStringArrayList("lableId", labelIds);
			}
			bundle.putString("id", mId);
			intent.putExtra("bundle", bundle);
			startActivityForResult(intent, 10);
		}
	};

	View.OnClickListener onBeginClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Utils.hiddenSoftBorad(MatterEditor.this);
			ArrayList<PopupItem> items = new ArrayList<PopupItem>();
			items.add(new PopupItem("清空", "clear"));
			DateTimePopup.DateTimeStyle stype = DateTimePopup.DateTimeStyle.eRemind;
			DateTimePopup
					.builder(MatterEditor.this)
					.setDateTimeStyle(stype)
					.setCancelText("清空")
					.setCommitText("设置")
					.setOnMessageListener(new OnMessageListener() {
						@Override
						public void onClick(PopupItem msg) {
							if (msg.mCode.equals("clear")) {
								mBeginDate = null;
								setBeginDate(mBeginDate);
							} else if (msg.mCode.equals("commit")) {
								panduanBeginDate();
							}

						}
					})
					.setOnDateTimeChangeListener(
							new DateTimePopup.OnDateTimeChange() {
								@Override
								public void onDateTimeChange(Calendar c,
										DateTimePopup.DateTimeStyle style) {
									mBeginDate = c.getTime();
									panduanBeginDate();
									mEndLayout.setClickable(true);
								}
							})
					.show(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	};

	private void panduanBeginDate() {
		if (mBeginDate == null) {
			mBeginDate = new Date(System.currentTimeMillis());
//		} else if (mBeginDate.getTime() < System.currentTimeMillis()) {
//			mToast.setText("开始时间必须大于现在时间");
//			mToast.show();
//			return;
			// mBeginDate = new Date(System.currentTimeMillis());
		} else if (mFinshDate != null
				&& mFinshDate.getTime() < mBeginDate.getTime()) {
			mFinshDate = null;
			setFinishDate(mFinshDate);
		}
		setBeginDate(mBeginDate);
	}

	View.OnClickListener onFinishClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Utils.hiddenSoftBorad(MatterEditor.this);
			ArrayList<PopupItem> items = new ArrayList<PopupItem>();
			items.add(new PopupItem("清空", "clear"));

			DateTimePopup.DateTimeStyle stype = DateTimePopup.DateTimeStyle.eRemind;
			DateTimePopup
					.builder(MatterEditor.this)
					.setDateTimeStyle(stype)
					.setCancelText("清空")
					.setCommitText("设置")
					.setOnMessageListener(new OnMessageListener() {
						@Override
						public void onClick(PopupItem msg) {
							if (msg.mCode.equals("clear")) {
								mFinshDate = null;
								setFinishDate(mFinshDate);
							} else if (msg.mCode.equals("commit")) {
								panduanFinishDate();
							}
						}
					})
					.setOnDateTimeChangeListener(
							new DateTimePopup.OnDateTimeChange() {
								@Override
								public void onDateTimeChange(Calendar c,
										DateTimePopup.DateTimeStyle style) {
									mFinshDate = c.getTime();
									panduanFinishDate();
								}
							})
					.show(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	};

	private void panduanFinishDate() {
		Log.d(TAG, "panduanFinshDate is did");
		if (mFinshDate == null) {
			Log.d(TAG, "mFinshDate is null");
			if (mBeginDate != null) {
				mToast.setText("结束时间必须大于开始时间");
				mToast.show();
			} else {
				mFinshDate = new Date(System.currentTimeMillis());
			}
		} else {
			if (mBeginDate != null && mFinshDate.before(mBeginDate)) {
				mFinshDate = null;
				setFinishDate(mFinshDate);
				mToast.setText("结束时间必须大于开始时间");
				mToast.show();
				return;
			}
		}
		setFinishDate(mFinshDate);
	}

	View.OnClickListener onRepeatClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {

		}
	};

	View.OnClickListener onAutoClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (mAutoEnable) {
				mAuto = !mAuto;
				setAutoMode(mAuto);
			}
		}
	};

	View.OnClickListener onRangeClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			mRange = !mRange;
			if (!mRange) {
				DialogHelper.showArrDiaLog(MatterEditor.this,
						new DialogHelper.ArrDiaLogCallBack() {
							@Override
							public void callBack(int result) {
								if(result==Activity.RESULT_CANCELED){
									mRange = true;
								}else if(result==Activity.RESULT_OK){
								mRange = false;
								}
								setRangeMode(mRange);
							}
						});
			}
			else{
			setRangeMode(mRange);
			}
		}
	};

	ApprovalProcessTypeView.OnProcessChangeListener onProcessChangeListener = new OnProcessChangeListener() {

		@Override
		public void onProcessChange(int key) {
			mProcessKey = key;
//			approvalProcessTypeView.setVisibility(mProcessKey == ApprovalProcessTypeView.FIXED ? View.VISIBLE : View.GONE);
			showProcessButton.setVisibility(mProcessKey == ApprovalProcessTypeView.FIXED ? View.VISIBLE : View.GONE);
		}
	};
	
	View.OnClickListener onShowProcessClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(mId != null && mId.trim().length() > 0){
			ApprovalProcessListActivity_.intent(MatterEditor.this)
			.dataId(mId)
			.start();
			}else if(mMatterType != null && mMatterType.length() > 0){
				ApprovalProcessListActivity_.intent(MatterEditor.this)
				.dataType(mMatterType)
				.start();
			}
		}
	};

	View.OnClickListener onCcClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent intent = new Intent(MatterEditor.this, SelectContacter.class);
			Bundle bundle = new Bundle();
			ArrayList<String> name = new ArrayList<String>();
			ArrayList<String> code = new ArrayList<String>();
			ArrayList<String> editCode = new ArrayList<String>();
			if (mEditCopyPerson.size() > 0) {
				for (SamUser s : mEditCopyPerson) {
					editCode.add(s.getmCode());
				}
				// bundle.putStringArrayList("name",name);
				bundle.putStringArrayList("extraUserCodes", editCode);
			}
			for(SamUser s : mCopyUsers){
				name.add(s.getmName());
				code.add(s.getmCode());
			}
			bundle.putStringArrayList("name", name);
			bundle.putStringArrayList("code", code);
			bundle.putString(SelectContacter.how, SelectContacter.COPY);

			bundle.putString("contact", "arrange");
			intent.putExtra("bundle", bundle);
			startActivityForResult(intent, 4);
		}
	};

	View.OnClickListener onBackClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			delBmp();
			finish();
		}
	};

	View.OnClickListener onSaveClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			AllUtils.hiddenSoftBorad(MatterEditor.this);
			if (mEditType.equals("01")) {
				if (mDataType.equals("1")) {
					saveArrangeCreateData(true);
				} else {
					saveAppvalCteateData(true);
				}
			} else {
				if (mDataType.equals("1")) {
					saveArrangeCreateData(false);
				} else {
					saveAppvalCteateData(false);
				}
			}
			// Bimp.clearMap();
		}

	};

	private void saveAppvalCteateData(final Boolean isCreate) {
		ArrayList<String> appvalFile = (ArrayList<String>) Bimp.drr;
		if(mUploadWidget!=null){
			Log.d(TAG, "approve file size:"+mUploadWidget.getFiles().size());
			for(MyFileItem item:mUploadWidget.getFiles()){
				if(item.fid==null||item.fid.equals("")){
					appvalFile.add(item.path);
				}
			}
		}
		HashMap<String, String> map = new HashMap<String, String>();
		final MatterApproveCommitRequest approveCommitRequest = new MatterApproveCommitRequest();
		approveCommitRequest.addUploadUrl("/home/phone/attach/upload");
		if(isCreate){
			approveCommitRequest.addUrl("/home/phone/thing/addapprove");
		}else{
			approveCommitRequest.addUrl("/home/phone/thing/updateapprove");
			approveCommitRequest.addParam("step", processStep+"");
		}
		final MatterEditResponeData editReponseData = new MatterEditResponeData();
		editReponseData.setOnResponseListener(new OnResponseListener() {
			
			@Override
			public void onResponseError(String errMsg, String errCode) {
				if(isCreate)
				{
				errorDialog.setTitle("新建审批失败").setMessage("新建审批失败。"+errMsg).setTitle("确定")
				.show();
				}else{
				errorDialog.setTitle("编辑审批失败").setMessage("编辑审批失败。"+errMsg).setTitle("确定")
				.show();
				}
//				.setPositiveButton("确定", null).show();
				dialog.dismiss();
			}
			
			@Override
			public void onReponseComplete() {
				approveCommitRequest.infoId = editReponseData.infoId;
				approveCommitRequest.appUn = "approval";
				approveCommitRequest.saveUploadFileParam();
				if(approveCommitRequest.getFilesLength()/AllUtils.MB > AllUtils.Service_File_Length){
					mToast.setText("附件太大，请看进度条！");
					Intent i = new Intent(MatterEditor.this, ProgressNotifyService.class);
					i.putExtra("upload", approveCommitRequest.uploadFileList);
					i.putExtra("url", approveCommitRequest.uploadUrl);
					i.putExtra("title", approveCommitRequest.title);
					i.putExtra("dataType", approveCommitRequest.dataType);
					startService(i);
					mToast.show();
					if((isCreate && !iHomeEnable) || mEditType.equals("03"))
						complete();
						else{
							setResult(RESULT_OK);
							finish();
						}
				}else{
				approveCommitRequest.executeSocketFileRequest(
						editReponseData.onFileResponseListener);
				}
			}
			
			@Override
			public void onProgress(float count) {
				
			}
			
			@Override
			public void onPreExecute() {
				dialog.show();
//				dialog.setMax(100);
			}
			
			@Override
			public void onNoneData() {
				
			}
		});
		
		editReponseData.setOnFileResponseListener(new OnFileResponseListener() {
			
			@Override
			public void onResponseError(String errMsg, String errCode) {
				mToast.setText(errCode+":"+errMsg);
				mToast.show();
				dialog.dismiss();
			}
			
			@Override
			public <T> void onReponseComplete(T result) {
				if(isCreate)
					complete();
					else{
						setResult(RESULT_OK);
						finish();
					}
			}
			
			@Override
			public void onProgress(float progress,int count,String fileName) {
				
			}
			
			@Override
			public void onPreExecute() {
				
			}
			
			@Override
			public void onNoneData() {
				
			}
			
			@Override
			public void onFileReponseFile(List<String> filePaths) {
					mToast.setText(filePaths.size()+"个附件上传失败");
					dialog.dismiss();
			}
		});
		
		if(ApprovalProcessTypeView.FIXED == mProcessKey){
			if(modelId != null)
				approveCommitRequest.addParam("modelId", modelId);
		}
		approveCommitRequest.id = mId;
		approveCommitRequest.title = mEditTitle.getText().toString();
		approveCommitRequest.content = mMonoText.getText().toString();
		approveCommitRequest.approveType = mMatterType;
		approveCommitRequest.creatorCode = AccountInfo.instance().getLastUserInfo()
				.getCode();
		approveCommitRequest.creatorName = AccountInfo.instance().getLastUserInfo()
				.getName();
		approveCommitRequest.labelStr = allLableId;
		approveCommitRequest.clientName = "phone";
		approveCommitRequest.addFilePath(appvalFile);
		if (approveCommitRequest.title.trim().equals("")) {
			errorDialog.setTitle("请输入标题").setMessage("请输入审批的标题。")
					.setPositiveButton("确定", null).show();
			return;
		}

		String content = map.get("content");
		if (approveCommitRequest.content.trim().equals("")) {
			errorDialog.setTitle("请输入描述").setMessage("请输入审批的描述。")
					.setPositiveButton("确定", null).show();
			return;
		}
		String mattorStr = getMattorDto();
		if (mattorStr.equals("")) {
			errorDialog.setTitle("请选择审批人").setMessage("审批人不能为空。")
					.setPositiveButton("确定", null).show();
			return;
		}
		approveCommitRequest.todoUserSet = mattorStr;
		String ccStr = getCcDto();
		approveCommitRequest.copyUserSet = ccStr;
		approveCommitRequest.saveParam();
		approveCommitRequest.executeRequest(editReponseData);
	}

	private void saveArrangeCreateData(final boolean isCreate) {
		ArrayList<String> arrangeFile = (ArrayList<String>) Bimp.drr;
		if(mUploadWidget!=null){
			for(MyFileItem item:mUploadWidget.getFiles()){
				if(item.fid==null||item.fid.equals("")){
					arrangeFile.add(item.path);
				}
			}
		}
		final MatterArrangeCommitRequest matterArrangeCommitRequest = new MatterArrangeCommitRequest();
		matterArrangeCommitRequest.addUploadUrl("/home/phone/attach/upload");
		if(isCreate){
			matterArrangeCommitRequest.addUrl("/home/phone/thing/addarrange");
		}else{
			matterArrangeCommitRequest.addUrl("/home/phone/thing/updatearrange");
		}
		matterArrangeCommitRequest.content = mMonoText.getText().toString();
		matterArrangeCommitRequest.title =  mEditTitle.getText().toString();
		matterArrangeCommitRequest.id = String.valueOf(mId);
		matterArrangeCommitRequest.labelStr = allLableId;
		matterArrangeCommitRequest.clientName = "phone";
		
		matterArrangeCommitRequest.startTime = mBeginText.getText().toString();
		matterArrangeCommitRequest.endTime = mEndText.getText().toString();
		matterArrangeCommitRequest.autoFinish = mAuto ? "1" : "0";
		matterArrangeCommitRequest.creatorCode = AccountInfo.instance().getLastUserInfo()
				.getCode();
		matterArrangeCommitRequest.creatorName = AccountInfo.instance().getLastUserInfo()
				.getName();
		matterArrangeCommitRequest.dataType = "1";
		matterArrangeCommitRequest.mFilePathList = arrangeFile;
		matterArrangeCommitRequest.appUn = "arrange";
		matterArrangeCommitRequest.repeatFlag = "0";
		if(isCreate){
			if (mParentId != null && !mParentId.equals("")) {
				matterArrangeCommitRequest.parentId = mParentId;
				matterArrangeCommitRequest.saveParentId();
			}
		}
		HashMap<String, String> map = null;
		final MatterEditResponeData responeData = new MatterEditResponeData();
		responeData.setOnFileResponseListener(new OnFileResponseListener() {
			
			@Override
			public void onResponseError(String errMsg, String errCode) {
				mToast.setText(errCode+":"+errMsg);
				mToast.show();
				dialog.dismiss();
			}
			
			@Override
			public <T> void onReponseComplete(T result) {
				if(isCreate || mEditType.equals("03"))
					complete();
					else{
						setResult(RESULT_OK);
						finish();
					}
			}
			
			@Override
			public void onProgress(float progress,int count,String fileName) {
				
			}
			
			@Override
			public void onPreExecute() {
			}
			
			@Override
			public void onNoneData() {
				
			}
			
			@Override
			public void onFileReponseFile(List<String> filePaths) {
				mToast.setText(filePaths+"个附件上传失败");
				dialog.dismiss();
			}
		});
		
		responeData.setOnResponseListener( new OnResponseListener() {
			
			@Override
			public void onResponseError(String errMsg, String errCode) {
				if(isCreate){
				errorDialog.setTitle("生成任务失败").setMessage("生成任务失败。"+errMsg)
				.setPositiveButton("确定", null).show();
				}else{
					errorDialog.setTitle("编辑任务失败").setMessage("编辑任务失败。"+errMsg)
					.setPositiveButton("确定", null).show();
				}
		        dialog.dismiss();
			}
			
			@Override
			public void onReponseComplete() {
				matterArrangeCommitRequest.infoId = responeData.infoId;
				matterArrangeCommitRequest.saveUploadFileParam();
				if(matterArrangeCommitRequest.getFilesLength()/AllUtils.MB  > AllUtils.Service_File_Length){
					mToast.setText("附件太大，请看进度条！");
					Intent i = new Intent(MatterEditor.this, ProgressNotifyService.class);
					i.putExtra("upload", matterArrangeCommitRequest.uploadFileList);
					i.putExtra("url", matterArrangeCommitRequest.uploadUrl);
					i.putExtra("title", matterArrangeCommitRequest.title);
					i.putExtra("dataType", matterArrangeCommitRequest.dataType);
					startService(i);
					if(isCreate || mEditType.equals("03"))
						complete();
						else{
							setResult(RESULT_OK);
							finish();
						}
					mToast.show();
				}else{
				matterArrangeCommitRequest.executeSocketFileRequest(responeData.onFileResponseListener);
				}
			}
			
			@Override
			public void onProgress(float count) {
				
			}
			
			@Override
			public void onPreExecute() {
				dialog.show();
			}
			
			@Override
			public void onNoneData() {
				
			}
		});
		if (matterArrangeCommitRequest.title.trim().equals("")) {
			errorDialog.setTitle("请输入标题").setMessage("请输入任务的标题。")
					.setPositiveButton("确定", null).show();
			return;
		}
		if (mEditType.equals("03")) {
			matterArrangeCommitRequest.reportId = mInfo.getReportId();
			matterArrangeCommitRequest.reportListId = jobj.optString("id");
			matterArrangeCommitRequest.saveReportParam();
			matterArrangeCommitRequest.addUrl("/home/phone/thing/addarrange");
		}
		
		if (mMatterType.equals("0")) {
			matterArrangeCommitRequest.arrangeType = "0";
			matterArrangeCommitRequest.savePersonParam();
		} else {
			matterArrangeCommitRequest.arrangeType = "1";
			matterArrangeCommitRequest.copyUser = getCcDto();
			matterArrangeCommitRequest.todoUser = getMattorDto();
			matterArrangeCommitRequest.secrecy = mRange ? "0" : "1";
			if (matterArrangeCommitRequest.todoUser.equals("")) {
				errorDialog.setTitle("请选择办理人").setMessage("办理人不能为空。")
						.setPositiveButton("确定", null).show();
				return;
			}
			matterArrangeCommitRequest.saveOfficalParam();
		}
		matterArrangeCommitRequest.executeRequest(responeData);
	}


	private String getMattorDto() {
		if (mTodoUsers.size() == 0)
			return "";
		JSONArray array = new JSONArray();
		for (SamUser u : mTodoUsers) {
			try {
				JSONObject jPerson = new JSONObject();
				jPerson.put("value", u.getmCode());
				jPerson.put("name", u.getmName());
				array.put(jPerson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return array.toString();
	}

	private String getCcDto() {
		if (mCopyUsers.size() == 0)
			return "";
		JSONArray array = new JSONArray();
		for (int i = 0; i < mCopyUsers.size(); i++) {
			JSONObject jPerson = new JSONObject();
			try {
				jPerson.put("value", mCopyUsers.get(i).getmCode());
				jPerson.put("name", mCopyUsers.get(i).getmName());

			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(jPerson);
		}

		return array.toString();
	}


	void showRequsetError(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void activityYMove() {
		super.activityYMove();
		AllUtils.hiddenSoftBorad(this);
	}
	
	private void complete(){
		Bimp.clearMap();
		Intent i = new Intent(this,FramMainActivity.class);
		i.putExtra("data", FramMainActivity.COMPLETE);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
	
	

}
