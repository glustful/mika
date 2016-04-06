package com.miicaa.home.ui.report;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.base.request.HttpFileUpload;
import com.miicaa.base.request.UploadFileItem;
import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PicturButton;
import com.miicaa.common.base.PictureLayout;
import com.miicaa.common.base.PictureLayout.OndelWebPicListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.common.base.Utils;
import com.miicaa.common.base.WheelView.WheelAdapter;
import com.miicaa.detail.MatterDetailAcrtivity_;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.data.net.SocketUploadFileTask;
import com.miicaa.home.ui.business.file.SelectRoundActivity_;
import com.miicaa.home.ui.calendar.DateUtil;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.ui.home.MatterCell;
import com.miicaa.home.ui.matter.MatterImgManager;
import com.miicaa.home.ui.org.ArragementLabEdit;
import com.miicaa.home.ui.org.DateTimePopup;
import com.miicaa.home.ui.org.DateTimePopup.DateTimeStyle;
import com.miicaa.home.ui.org.DateTimePopup.OnDateTimeChange;
import com.miicaa.home.ui.org.WheelViewPopup;
import com.miicaa.home.ui.org.WheelViewPopup.OnItemChangeListener;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.photoGrid.Bimp;
import com.miicaa.home.ui.photoGrid.ImageItem;
import com.miicaa.home.ui.photoGrid.PhotoGridContentActivity;
import com.miicaa.home.ui.photoGrid.SelectPictureActivity_;
import com.miicaa.home.ui.picture.PictureHelper;
import com.miicaa.home.ui.report.ReportUtils.RequestCallback;
import com.miicaa.home.ui.widget.UploadWidget;
import com.miicaa.service.ProgressNotifyService;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.FileUtils;
import com.miicaa.utils.fileselect.FileListActivity_;
import com.miicaa.utils.fileselect.MyFileItem;
import com.yxst.epic.yixin.view.InnerScrollView;

@EActivity(R.layout.matter_report_activity)
public class WorkReportActivity extends Activity {

	static String TAG = "WorkReportActivity";
	

	@Override
	public void onBackPressed() {

		back();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		Bimp.drr.clear();
	}

	public static final int ROUNDPEOPLE = 0;
	public static final int COMMENTPEOPLE = 1;
	public static final int RESULT_TIP = 2;
	public static final String SUMMARIZE = "02";
	public static final String PLAN = "01";
	private String allLableId;
	private ArrayList<String> lableEditors = new ArrayList<String>();
	private ArrayList<String> labelIds = new ArrayList<String>();
	ArrayList<Object> falieds;
	ArrayList<SamUser> mRoundUser;
	String mRoundJson = "";
	ArrayList<SamUser> mCommentUser;
	String mCommentJson = "";
	String rightType = "10";
	
	private ProgressDialog progressDialog;
	private UploadWidget mUploadWidget;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		mUploadWidget.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_TIP:
			if(mInfo != null)
                 setResult(RESULT_OK);
			if(data != null ){
			if(data.getBooleanExtra("remove", false)){
				
					if(type.equals(SUMMARIZE)){
						todayJsonArray.remove(index);
						todayList.removeViewAt(index);
						
					}else {
						tomorrowJsonArray.remove(index);
						tomorrowList.removeViewAt(index);
					}
					return;
			}
				
			
			
			String json = data.getStringExtra("json");
			try {
				JSONObject obj = new JSONObject(json);
				if (type.equals(SUMMARIZE)) {
					todayJsonArray.set(index, obj);
					View v = todayList.getChildAt(index);
					TextView tv = (TextView) v.findViewById(R.id.title);

					tv.setText(obj.optString("title"));

					v.setTag(obj);
				} else {
					tomorrowJsonArray.set(index, obj);
					View v = tomorrowList.getChildAt(index);
					TextView tv = (TextView) v.findViewById(R.id.title);

					tv.setText(obj.optString("title"));

					v.setTag(obj);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			break;
		case 1:
			int result = data.getIntExtra("success", 0);
			mRoundUser.clear();
			mRoundJson = "";
			if (result == 2) {
				roundLabel.setText("公开");
				rightType = "00";
			} else if (result == 3) {
				roundLabel.setText("仅点评人可见");
				rightType = "10";
			} else if (result == 1) {
				roundLabel.setText(data.getStringExtra("result"));
				// code = data.getStringExtra("code");
				mRoundJson = data.getStringExtra("json");
				rightType = data.getStringExtra("rightType");
			} else if (result == 4) {
				rightType = "40";
				ArrayList<ContactViewShow> copyDatas = data
						.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
				if (copyDatas == null || copyDatas.size() == 0) {
					roundLabel.setText("");
					mRoundUser.clear();

					return;
				}
				setCopyData(copyDatas, ROUNDPEOPLE);
			}
			break;
		case 4:
			mCommentUser.clear();
			ArrayList<ContactViewShow> copyDatas = data
					.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
			if (copyDatas == null || copyDatas.size() == 0) {
				discuName.setText("");

				return;
			}
			setCopyData(copyDatas, COMMENTPEOPLE);
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
				tagLabel.setText("");
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
			tagLabel.setText(lableString);
		}
		}
	}

	private void setCopyData(ArrayList<ContactViewShow> data, int type) {
		switch (type) {
		case ROUNDPEOPLE:
			mRoundJson = "[";
			for (int i = 0; i < data.size(); i++) {
				mRoundUser.add(new SamUser(data.get(i).getCode(), data.get(i)
						.getName()));
				mRoundJson += "{\"code\":\"" + data.get(i).getCode()
						+ "\",\"name\":\"" + data.get(i).getName() + "\"}";

				if (i < data.size() - 1) {

					mRoundJson += ",";
				}

			}
			if (data.size() > 1) {
				roundLabel.setText(data.get(0).getName() + "等" + data.size()
						+ "个人");
			} else if (data.size() == 1) {
				roundLabel.setText(data.get(0).getName());
			} else {
				roundLabel.setText("");
			}
			mRoundJson += "]";
			break;
		case COMMENTPEOPLE:
			mCommentJson = "[";
			for (int i = 0; i < data.size(); i++) {
				mCommentUser.add(new SamUser(data.get(i).getCode(), data.get(i)
						.getName()));
				mCommentJson += "{\"code\":\"" + data.get(i).getCode()
						+ "\",\"name\":\"" + data.get(i).getName() + "\"}";

				if (i < data.size() - 1) {

					mCommentJson += ",";
				}

			}
			if (data.size() > 1) {
				discuName.setText(data.get(0).getName() + "等" + data.size()
						+ "个人");
			} else if (data.size() == 1) {
				discuName.setText(data.get(0).getName());
			} else {
				discuName.setText("");
			}
			mCommentJson += "]";
			break;
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 202) {
				return;
			}
			Bundle bundle = msg.getData();
			int count = bundle.getInt("count");
			Bitmap bitmap = (Bitmap) msg.obj;
			if (count == -1) {
				showSmallPhoto(bitmap, null, bundle.getString("fid"));
			} else {
				String fileLocal = Bimp.drr.get(count);
				showSmallPhoto(bitmap, fileLocal);
			}
			switch (msg.what) {
			case 1:

				break;
			case 2:
				PicturButton button = new PicturButton(mContext);
				break;

			default:
				break;

			}
		}
	};

	public static final int REPORT_DAY = 0;
	public static final int REPORT_WEEK = 1;
	public static final int REPORT_MONTH = 2;
	public static final int REPORT_CUSTOM = 3;
	public static final String REPORT_TYPE_DAY = "10";
	public static final String REPORT_TYPE_WEEK = "20";
	public static final String REPORT_TYPE_MONTH = "30";
	public static final String REPORT_TYPE_CUSTOM = "40";
	public final static int GRID_PHOTO_CHECK = 0x09;
	public final static int GRID_FILE_CHECK = 0x10;

	@Extra
	int reportType;
	@Extra
	ReportDetailInfo mInfo;
	@Extra
	ArrayList<AccessoryInfo> pngs;
	@Extra
	ArrayList<AccessoryInfo> tfiles;

	@ViewById(R.id.pay_headTitle)
	TextView headTitle;// 标题
	@ViewById(R.id.pay_commitButton)
	Button commit;
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.report_parent_scroll)
	ScrollView parentScroll;
	@ViewById(R.id.report_today_scroll)
	InnerScrollView todayScroll;
	@ViewById(R.id.report_today_list)
	LinearLayout todayList;
	@ViewById(R.id.report_tomorrow_scroll)
	InnerScrollView tomorrowScroll;
	@ViewById(R.id.report_tomorrow_list)
	LinearLayout tomorrowList;
	@ViewById(R.id.report_today_time_label1)
	TextView todayTimeLabel1;
	@ViewById(R.id.report_tomorrow_time_label1)
	TextView tomorrowTimeLabel1;
	@ViewById(R.id.report_today_time_label2)
	TextView todayTimeLabel2;
	@ViewById(R.id.report_tomorrow_time_label2)
	TextView tomorrowTimeLabel2;
	@ViewById(R.id.report_round_people_name)
	TextView roundLabel;
	@ViewById(R.id.report_tag_label)
	TextView tagLabel;
	@ViewById(R.id.report_custom_top)
	LinearLayout customTop;
	@ViewById(R.id.report_custom_name)
	EditText customName;
	@ViewById(R.id.report_today_title_lael)
	TextView todayTitleLabel;
	@ViewById(R.id.report_tomorrow_title_lael)
	TextView tomorrowTitleLabel;
	@ViewById(R.id.report_today_lael)
	TextView todayLabel;
	@ViewById(R.id.report_tomorrow_lael)
	TextView tomorrowLabel;
	@ViewById(R.id.fileAddLayout)
	FrameLayout mAddPhotoLayout;
	@ViewById(R.id.report_discu_people_name)
	TextView discuName;
	@ViewById(R.id.report_today_plus_edit)
	EditText todayPlusEdit;
	@ViewById(R.id.report_tomorrow_plus_edit)
	EditText tomorrowPlusEdit;
	@ViewById(R.id.report_desc)
	EditText desc;

	Context mContext;
	boolean isRefresh = false;
	ArrayList<JSONObject> todayJsonArray;
	ArrayList<JSONObject> tomorrowJsonArray;
	String type = SUMMARIZE;
	int index = 0;
	HashMap<String, Bitmap> webPic = new HashMap<String, Bitmap>();// 网络图片保存后便于展现
	ArrayList<MatterImgManager> imageManagerList;

	@Touch(R.id.topView)
	boolean touch(MotionEvent event, View v) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			 View view = getCurrentFocus();
//			 view.clearFocus();
			Utils.hiddenSoftBorad(mContext);
			//return true;
		}
		return false;
	}


	@Click(R.id.pay_cancleButton)
	void back() {

		if (isRefresh)
			setResult(Activity.RESULT_OK);
		finish();
	}

	@Click(R.id.pay_commitButton)
	void commit() {
		if (checkParam()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("jsonStr", paramStr);

			String url = mContext.getString(R.string.report_create_url);
			if (mInfo != null) {
				url = mContext.getString(R.string.report_update_url);
			}
			ReportUtils.requestList(mContext, new RequestCallback() {

				@Override
				public void callback(ResponseData data) {

					if (data.getResultState() == ResultState.eSuccess) {

						if (mUploadWidget.getFiles().size() < 1 && Bimp.drr.size() < 1) {
							Bimp.drr.clear();
							setResult(RESULT_OK);
							finish();

						} else {
							List<String> pathList = new ArrayList<String>();
							for(MyFileItem item : mUploadWidget.getFiles()){
								pathList.add(item.path);
							}
							pathList.addAll(Bimp.drr);
							Log.d(TAG, "uploadFile size:"+pathList.size());
							Log.d(TAG, "pathList size:"+pathList.size());
							ArrayList<UploadFileItem> uploadFileItemList = FileUtils.getUploadFileItems(pathList, data.getJsonObject()
									.optString("id"), "workReport");
							if(FileUtils.getFilesLength(pathList)/AllUtils.MB > AllUtils.Service_File_Length){
								Intent intent = new Intent(mContext, ProgressNotifyService.class);
								intent.putExtra("title","" );
								intent.putExtra("dataType", AllUtils.reporteType);
								intent.putParcelableArrayListExtra("upload", uploadFileItemList);
								intent.putExtra("url",  mContext
									.getString(R.string.upload_url));
								startService(intent);
								finish();
							}else{
							new HttpFileUpload()
							.setParam(uploadFileItemList, mContext
									.getString(R.string.upload_url), new BaseResopnseData.OnFileResponseListener() {
								
								@Override
								public void onResponseError(String errMsg, String errCode) {
//									mToast.setText(errCode+":"+errMsg);
//									mToast.show();
									PayUtils.showToast(mContext, "上传失败："+errCode+":"+errMsg ,
											2000);
									progressDialog.dismiss();
								}
								
								@Override
								public <T> void onReponseComplete(T result) {
									progressDialog.dismiss();
									setResult(RESULT_OK);
									finish();
								}
								
								@Override
								public void onProgress(float progress, int count, String fileName) {
									
								}
								
								@Override
								public void onPreExecute() {
									progressDialog.show();
								}
								
								@Override
								public void onNoneData() {
									
								}
								
								@Override
								public void onFileReponseFile(List<String> filePaths) {
									if(filePaths.size() > 0){
										PayUtils.showToast(mContext, "上传失败" +filePaths.size()+"个附件",
												2000);
										progressDialog.dismiss();
									}
								}
							}).isContinuous(false)
							.execute();
							}
						}
					} else {
						PayUtils.showToast(mContext, "提交失败：" + data.getMsg(),
								2000);
					}

				}
			}, url, map);
		}
	}

	@Click(R.id.report_today_plus)
	void todayPlus() {
		addTodayItem(todayPlusEdit.getText().toString().trim());
		todayPlusEdit.setText("");
	}

	@Click(R.id.report_tomorrow_plus)
	void tomorrowPlus() {
		addTomorrowItem(tomorrowPlusEdit.getText().toString().trim());
		tomorrowPlusEdit.setText("");
	}

	@Click(R.id.report_today_time_label1)
	void todayClick(View v) {
		todayTime(v);
	}

	@Click(R.id.report_today_time_label2)
	void todayClick1(View v) {

		todayTime(v);
	}

	void todayTime(View v) {
		if (!(v instanceof TextView)) {
			return;
		}
		TextView todayTimeLabel = (TextView) v;
		switch (reportType) {
		case REPORT_DAY:
			selectTime("yyyy.MM.dd", todayTimeLabel, DateTimeStyle.eDate);
			break;
		case REPORT_WEEK:
			selectWeekTime(todayTimeLabel, currentWeekEntity);
			break;
		case REPORT_MONTH:
			selectTime("yyyy.MM", todayTimeLabel, DateTimeStyle.eYearMonth);
			break;
		case REPORT_CUSTOM:
			selectTime("yyyy.MM.dd", todayTimeLabel, DateTimeStyle.eDate);
			break;
		}
	}

	@Click(R.id.report_tomorrow_time_label1)
	void tomorrow(View v) {
		tomorrowTime(v);
	}

	@Click(R.id.report_tomorrow_time_label2)
	void tomorrow1(View v) {
		tomorrowTime(v);
	}

	void tomorrowTime(View v) {
		if (!(v instanceof TextView)) {
			return;
		}
		TextView tomorrowTimeLabel = (TextView) v;
		switch (reportType) {
		case REPORT_DAY:

			selectTime("yyyy.MM.dd", tomorrowTimeLabel, DateTimeStyle.eDate);
			break;
		case REPORT_WEEK:
			selectWeekTime(tomorrowTimeLabel, tomorrowWeekEntity);
			break;
		case REPORT_MONTH:
			selectTime("yyyy.MM", tomorrowTimeLabel, DateTimeStyle.eYearMonth);
			break;
		case REPORT_CUSTOM:
			selectTime("yyyy.MM.dd", tomorrowTimeLabel, DateTimeStyle.eDate);
			break;
		}
	}

	@Click(R.id.report_discu_people)
	void roundPeople() {

		Intent intent = new Intent(mContext, SelectContacter.class);
		Bundle bundle = new Bundle();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> code = new ArrayList<String>();
		if (mCommentUser.size() > 0) {
			for (SamUser s : mCommentUser) {
				name.add(s.getmName());
				code.add(s.getmCode());
			}
			bundle.putStringArrayList("name", name);
			bundle.putStringArrayList("code", code);
		}
		bundle.putString(SelectContacter.how, SelectContacter.DISCU);
		intent.putExtra("bundle", bundle);
		startActivityForResult(intent, 4);
	}

	@Click(R.id.report_round_select)
	void roundSelect() {
		Intent intent = new Intent(mContext, SelectRoundActivity_.class);
		intent.putExtra("rightType", rightType);
		intent.putExtra("json", mRoundJson);
		intent.putExtra("privateTitle", "仅点评人可见");
		intent.putExtra("name", roundLabel.getText().toString());
		Bundle bundle = new Bundle();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> code = new ArrayList<String>();
		if (mRoundUser.size() > 0) {
			for (SamUser s : mRoundUser) {
				name.add(s.getmName());
				code.add(s.getmCode());
			}
			bundle.putStringArrayList("name", name);
			bundle.putStringArrayList("code", code);
		}
		bundle.putString(SelectContacter.how, SelectContacter.ARRANGE);

		bundle.putString("contact", "arrange");
		intent.putExtra("bundle", bundle);
		((Activity) mContext).startActivityForResult(intent, 1);
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}

	@Click(R.id.report_tag_layout)
	void tag() {

		Intent intent = new Intent(mContext, ArragementLabEdit.class);
		Bundle bundle = new Bundle();
		if (lableEditors != null && lableEditors.size() > 0) {
			bundle.putStringArrayList("lableEdit", lableEditors);
			bundle.putStringArrayList("lableId", labelIds);
		}
		intent.putExtra("bundle", bundle);
		startActivityForResult(intent, 10);

	}

	@Click(R.id.report_today_auto)
	void todayAuto() {
		String url = mContext.getString(R.string.report_today_url);
		HashMap<String, String> map = new HashMap<String, String>();
		String today1 = todayTimeLabel1.getText().toString().trim();
		String today2 = todayTimeLabel2.getText().toString().trim();
		switch (reportType) {
		case REPORT_DAY:
			ReportUtils.putDay(today1, map);
			map.put("reportType", REPORT_TYPE_DAY);
			break;
		case REPORT_WEEK:
			ReportUtils.putWeek(currentWeekEntity, map);
			map.put("reportType", REPORT_TYPE_WEEK);
			break;
		case REPORT_MONTH:
			ReportUtils.putMonth(today1, map);
			map.put("reportType", REPORT_TYPE_MONTH);
			break;
		case REPORT_CUSTOM:
			ReportUtils.putCustomDate(today1, today2, map);
			map.put("reportType", REPORT_TYPE_CUSTOM);
			break;
		}
		ReportUtils.requestList(mContext, new RequestCallback() {

			@Override
			public void callback(ResponseData data) {
				addTodayList(data.getJsonArray(), true);

			}
		}, url, map);
	}

	@Click(R.id.report_tomorrow_auto)
	void tomorrowAuto() {
		String url = mContext.getString(R.string.report_tomorrow_url);
		HashMap<String, String> map = new HashMap<String, String>();
		String today1 = tomorrowTimeLabel1.getText().toString().trim();
		String today2 = tomorrowTimeLabel2.getText().toString().trim();
		switch (reportType) {
		case REPORT_DAY:
			ReportUtils.putDay(today1, map);
			map.put("reportType", REPORT_TYPE_DAY);
			break;
		case REPORT_WEEK:
			ReportUtils.putWeek(tomorrowWeekEntity, map);
			map.put("reportType", REPORT_TYPE_WEEK);
			break;
		case REPORT_MONTH:
			ReportUtils.putMonth(today1, map);
			map.put("reportType", REPORT_TYPE_MONTH);
			break;
		case REPORT_CUSTOM:
			ReportUtils.putCustomDate(today1, today2, map);
			map.put("reportType", REPORT_TYPE_CUSTOM);
			break;
		}
		ReportUtils.requestList(mContext, new RequestCallback() {

			@Override
			public void callback(ResponseData data) {
				addTomorrowList(data.getJsonArray(), true);

			}
		}, url, map);
	}

	@AfterTextChange(R.id.report_custom_name)
	void change(Editable et) {
		if (et.toString().length() >= 35) {
			PayUtils.showToast(mContext, "标题长度不能超过35个字", 3000);
		}
	}

	@AfterTextChange(R.id.report_today_plus_edit)
	void change1(Editable et) {
		if (et.toString().length() >= 35) {
			PayUtils.showToast(mContext, "标题长度不能超过35个字", 3000);
		}
	}

	@AfterTextChange(R.id.report_tomorrow_plus_edit)
	void change2(Editable et) {
		if (et.toString().length() >= 35) {
			PayUtils.showToast(mContext, "标题长度不能超过35个字", 3000);
		}
	}

	@AfterInject
	void initData() {
		Log.d(TAG, "initData : mInfo:"+mInfo);
		mContext = this;
		mRoundUser = new ArrayList<SamUser>();
		mCommentUser = new ArrayList<SamUser>();
		todayJsonArray = new ArrayList<JSONObject>();
		tomorrowJsonArray = new ArrayList<JSONObject>();
		falieds = new ArrayList<Object>();
		initWeek();
	}

	@AfterViews
	void initUI() {
		progressDialog = AllUtils.getMaualStyleDialog(this);
		mUploadWidget = new UploadWidget(this);
		mAddPhotoLayout.addView(mUploadWidget.getRootView());
		desc.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		String title = "";
		switch (reportType) {
		case REPORT_DAY:
			title = mContext.getString(R.string.report_day);
			initDayUI();
			break;
		case REPORT_WEEK:
			title = mContext.getString(R.string.report_week);
			initWeekUI();
			break;
		case REPORT_MONTH:
			title = mContext.getString(R.string.report_month);
			initMonthUI();
			break;
		case REPORT_CUSTOM:
			title = mContext.getString(R.string.report_custom);
			initCustomUI();
			break;
		}
		headTitle.setText(title);
		commit.setText("提交");
		commit.setVisibility(View.VISIBLE);
		tomorrowScroll.parentScrollView = this.parentScroll;
		todayScroll.parentScrollView = this.parentScroll;
		todayTimeLabel1.setTag(R.id.tag_self, "today");
		todayTimeLabel2.setTag(R.id.tag_self, "today");
		tomorrowTimeLabel1.setTag(R.id.tag_self, "tomorrow");
		tomorrowTimeLabel2.setTag(R.id.tag_self, "tomorrow");
		if (mInfo != null) {
			headTitle.setText("编辑" + mInfo.getReportType());
			customName.setText(mInfo.getTitle());
			addTodayList(ReportUtils.convertJSON(mInfo.getTodayList()), false);
			addTomorrowList(ReportUtils.convertJSON(mInfo.getTomorrowList()),
					false);
			mCommentUser.clear();
			ArrayList<ContactViewShow> copyDatas = ReportUtils
					.convertCommenter(mInfo.getCommenter());
			if (mInfo.getCommenter() == null || copyDatas.size() == 0) {
				discuName.setText("");

				return;
			}
			desc.setText(mInfo.getDesc());
			setCopyData(copyDatas, COMMENTPEOPLE);
			if (mInfo.getRange().equals("0")) {
				roundLabel.setText("公开");
				rightType = "00";
			} else {
				roundLabel.setText("仅点评人可见");
				rightType = "10";
			}
			String lableString = "";
			allLableId = "";
			this.lableEditors.clear();
			this.lableEditors.addAll(mInfo.getLabels());
			this.labelIds.clear();
			this.labelIds.addAll(mInfo.getLabelsId());
			for (int j = 0; j < mInfo.getLabels().size(); j++) {

				if (j != mInfo.getLabels().size() - 1) {
					lableString = lableString + mInfo.getLabels().get(j) + ",";
					allLableId = allLableId + mInfo.getLabelsId().get(j) + ",";
				} else {
					lableString = lableString + mInfo.getLabels().get(j);
					allLableId = allLableId + mInfo.getLabelsId().get(j);
				}
			}

			tagLabel.setText(lableString);
			if (pngs != null && pngs.size() > 0)
				requestPicture();
		}
	}

	private void initCustomUI() {
		customTop.setVisibility(View.VISIBLE);
		todayTitleLabel.setText("工作总结：");
		todayLabel.setText("总结日期");

		todayTimeLabel2.setTag(todayTimeLabel1);
		todayTimeLabel1.setTag(R.id.tag_relation, todayTimeLabel2);

		tomorrowTitleLabel.setText("工作计划：");
		tomorrowLabel.setText("计划日期");

		tomorrowTimeLabel1.setTag(todayTimeLabel2);
		tomorrowTimeLabel2.setTag(tomorrowTimeLabel1);
		todayTimeLabel2.setTag(R.id.tag_relation, tomorrowTimeLabel1);
		tomorrowTimeLabel1.setTag(R.id.tag_relation, tomorrowTimeLabel2);
		if (mInfo != null) {

			todayTimeLabel1.setText(PayUtils.formatData("yyyy.MM.dd",
					mInfo.getSummriazeStartTime()));

			todayTimeLabel2.setText(PayUtils.formatData("yyyy.MM.dd",
					mInfo.getSummriazeEndTime()));
			tomorrowTimeLabel1.setText(PayUtils.formatData("yyyy.MM.dd",
					mInfo.gettomorrowStartTime()));

			tomorrowTimeLabel2.setText(PayUtils.formatData("yyyy.MM.dd",
					mInfo.getTomorrowEndTime()));
		} else {
			Calendar ca = Calendar.getInstance();
			todayTimeLabel1.setText(PayUtils.formatData("yyyy.MM.dd",
					ca.getTimeInMillis()));
			ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) + 14);
			todayTimeLabel2.setText(PayUtils.formatData("yyyy.MM.dd",
					ca.getTimeInMillis()));
			ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) + 1);
			tomorrowTimeLabel1.setText(PayUtils.formatData("yyyy.MM.dd",
					ca.getTimeInMillis()));
			ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) + 14);
			tomorrowTimeLabel2.setText(PayUtils.formatData("yyyy.MM.dd",
					ca.getTimeInMillis()));
		}
	}

	private void initMonthUI() {
		//customTop.setVisibility(View.GONE);
		todayTitleLabel.setText("工作总结：");
		todayLabel.setText("总结月份");

		todayTimeLabel1.setTag(R.id.tag_relation, tomorrowTimeLabel1);

		tomorrowTitleLabel.setText("工作计划：");
		tomorrowLabel.setText("计划月份");
		tomorrowTimeLabel1.setTag(todayTimeLabel1);

		if (mInfo != null) {
			todayTimeLabel1.setText(PayUtils.formatData("yyyy.MM",
					mInfo.getSummriazeStartTime()));

			tomorrowTimeLabel1.setText(PayUtils.formatData("yyyy.MM",
					mInfo.getTomorrowEndTime()));
		} else {
			todayTimeLabel1.setText(PayUtils.formatData("yyyy.MM",
					new Date().getTime()));
			Calendar ca = new GregorianCalendar();
			ca.set(Calendar.MONTH, ca.get(Calendar.MONTH) + 1);
			tomorrowTimeLabel1.setText(PayUtils.formatData("yyyy.MM",
					ca.getTimeInMillis()));
		}
		removeTextView();
	}

	private void initWeekUI() {
		//customTop.setVisibility(View.GONE);
		if (mInfo != null) {
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(mInfo.getSummriazeStartTime());
			currentWeekEntity = ReportUtils.getCurrentWeekEntity(ca);
			ca.setTimeInMillis(mInfo.getTomorrowEndTime());
			tomorrowWeekEntity = ReportUtils.getCurrentWeekEntity(ca);
		} else {
			currentWeekEntity = ReportUtils.getCurrentWeekEntity(Calendar
					.getInstance());
			
			Calendar ca = Calendar.getInstance();
			ca.set(Calendar.WEEK_OF_MONTH, ca.get(Calendar.WEEK_OF_MONTH) + 1);
			tomorrowWeekEntity = ReportUtils.getCurrentWeekEntity(ca);
		}
		
		todayTitleLabel.setText("工作总结：");
		todayLabel.setText("总结周期");

		todayTimeLabel1.setText(currentWeekEntity.toString());
		todayTimeLabel1.setTag(R.id.tag_relation, tomorrowTimeLabel1);

		tomorrowTitleLabel.setText("工作计划：");
		tomorrowLabel.setText("计划周期");
		tomorrowTimeLabel1.setTag(todayTimeLabel1);

		tomorrowTimeLabel1.setText(tomorrowWeekEntity.toString());

		removeTextView();
	}

	private void initDayUI() {
		//customTop.setVisibility(View.GONE);
		todayTitleLabel.setText("工作总结：");
		todayLabel.setText("总结日期");

		todayTimeLabel1.setTag(R.id.tag_relation, tomorrowTimeLabel1);

		tomorrowTitleLabel.setText("工作计划：");
		tomorrowLabel.setText("计划日期");
		tomorrowTimeLabel1.setTag(todayTimeLabel1);

		if (mInfo != null) {
			todayTimeLabel1.setText(PayUtils.formatData("yyyy.MM.dd",
					mInfo.getSummriazeStartTime()));
			tomorrowTimeLabel1.setText(PayUtils.formatData("yyyy.MM.dd",
					mInfo.getTomorrowEndTime()));
		} else {
			todayTimeLabel1.setText(PayUtils.formatData("yyyy.MM.dd",
					new Date().getTime()));
			tomorrowTimeLabel1.setText(PayUtils.formatData("yyyy.MM.dd",
					new Date().getTime() + 24 * 60 * 60 * 1000));
		}
		removeTextView();
	}

	private void removeTextView() {
		LinearLayout v = (LinearLayout) todayTimeLabel1.getParent();
		v.removeViewAt(1);
		v.removeViewAt(1);
		v = (LinearLayout) tomorrowTimeLabel1.getParent();
		v.removeViewAt(1);
		v.removeViewAt(1);
	}

	private void addTodayList(JSONArray array, boolean isAuto) {

		// if(mInfo==null)
		for (int i = 0; i < todayList.getChildCount(); i++) {
			View child = todayList.getChildAt(i);
			if (child.getTag() != null && child.getTag() instanceof JSONObject
					&& !((JSONObject) child.getTag()).isNull("workId")) {
				todayList.removeView(child);
				todayJsonArray.remove(i);
				--i;
			}
		}
		if (array == null || array.length() < 1) {
			if (isAuto)
				PayUtils.showToast(mContext, "数据为空", 1000);
			return;
		}
		todayScroll.setVisibility(View.VISIBLE);

		View v = LayoutInflater.from(mContext).inflate(
				R.layout.report_cell_view, null);
		if ((todayList.getChildCount() + array.length()) > 3) {
			v.measure(0, 0);
			todayScroll.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, v.getMeasuredHeight() * 3));
		} else {
			todayScroll.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		for (int i = 0; i < array.length(); i++) {
			initListClick(v);

			TextView title = (TextView) v.findViewById(R.id.title);
			title.setText(array.optJSONObject(i).optString("title"));
			if (array.optJSONObject(i).isNull("workId")) {
				title.setTextColor(Color.BLACK);
			} else {
				title.setTextColor(getResources().getColor(
						R.color.workrportblue));
			}
			v.setTag(array.optJSONObject(i));
			todayJsonArray.add(array.optJSONObject(i));
			todayList.addView(v);
			if (i < (array.length() - 1)) {
				v = LayoutInflater.from(mContext).inflate(
						R.layout.report_cell_view, null);
			}
		}
	}

	private void initListClick(final View v) {

		final TextView title = (TextView) v.findViewById(R.id.title);
		ImageButton tip = (ImageButton) v.findViewById(R.id.tip);

		title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (v.getTag() != null && v.getTag() instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) v.getTag();

					if (jsonObject.isNull("workType")
							|| jsonObject.optString("workType").equals(
									MatterCell.WORKREPORTTYPE)) {
						type = jsonObject.optString("type", SUMMARIZE);
						if (type.equals(SUMMARIZE)) {
							index = todayJsonArray.indexOf(jsonObject);
						} else if (type.equals(PLAN)) {
							index = tomorrowJsonArray.indexOf(jsonObject);
						}
						index = index < 0 ? 0 : index;
						if (mInfo != null) {
							ReportItemDetailActivity_.intent(mContext)
							.jsonStr(v.getTag().toString())
							.info(mInfo)
							.isEdit(true)
							.isFinish(true)
							.startForResult(RESULT_TIP);
						}else{
						ReportItemDetailActivity_.intent(mContext)
						.jsonStr(v.getTag().toString())
						.isEdit(true)
						.startForResult(RESULT_TIP);;
						}
						return;
					}
					 MatterDetailAcrtivity_.intent(mContext)
              	   .dataId(jsonObject.optString("workId"))
              	   .dataType(jsonObject.optString("workType"))
              	   .start();
				}

			}
		});

		tip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if(v.getTag()!=null&&v.getTag() instanceof JSONObject){
					
				JSONObject obj = (JSONObject) v.getTag();
				type = obj.optString("type", SUMMARIZE);
				if(type.equals(SUMMARIZE)){
				index = todayJsonArray.indexOf(obj);
				}else if(type.equals(PLAN)){
					index = tomorrowJsonArray.indexOf(obj);
				}
				index = index<0?0:index;
				ReportItemDetailActivity_.intent(mContext)
				.jsonStr(v.getTag().toString())
				.isEdit(true)
				.isFinish(true)
				.info(mInfo)
				.startForResult(RESULT_TIP);;
				}else{
					PayUtils.showToast(mContext, "数据格式出错", 1000);
				}
			}
		});

	}

	private void addTodayItem(String title) {
		if (title == null || title.length() < 1) {
			PayUtils.showToast(mContext, "标题不能为空", 1000);
			return;
		}

		todayScroll.setVisibility(View.VISIBLE);

		View v = LayoutInflater.from(mContext).inflate(
				R.layout.report_cell_view, null);
		if ((todayList.getChildCount() + 1) > 3) {
			v.measure(0, 0);

			todayScroll.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, v.getMeasuredHeight() * 3));
		} else {
			todayScroll.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		initListClick(v);
		TextView tmp = (TextView) v.findViewById(R.id.title);
		tmp.setText(title);
		tmp.setTextColor(Color.BLACK);
		JSONObject obj = new JSONObject();
		try {
			obj.put("title", title);
			obj.put("type", SUMMARIZE);
			obj.put("workId", null);
			obj.put("progress", 0);
			obj.put("usedTime", 0);
			obj.put("theWorkloadOf", 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		v.setTag(obj);
		todayJsonArray.add(0, obj);

		tmp.setVisibility(View.VISIBLE);

		todayList.addView(v, 0);

	}

	private void addTomorrowList(JSONArray array, boolean isAuto) {

		for (int i = 0; i < tomorrowList.getChildCount(); i++) {
			View child = tomorrowList.getChildAt(i);
			if (child.getTag() != null && child.getTag() instanceof JSONObject
					&& !((JSONObject) child.getTag()).isNull("workId")) {
				tomorrowList.removeView(child);
				tomorrowJsonArray.remove(i);
				--i;
			}
		}
		if (array == null || array.length() < 1) {
			if (isAuto)
				PayUtils.showToast(mContext, "数据为空", 1000);
			return;
		}

		tomorrowScroll.setVisibility(View.VISIBLE);

		View v = LayoutInflater.from(mContext).inflate(
				R.layout.report_cell_view, null);
		if ((tomorrowList.getChildCount() + array.length()) > 3) {
			v.measure(0, 0);
			tomorrowScroll.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, v.getMeasuredHeight() * 3));
		} else {
			tomorrowScroll.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		for (int i = 0; i < array.length(); i++) {
			initListClick(v);

			TextView title = (TextView) v.findViewById(R.id.title);
			title.setText(array.optJSONObject(i).optString("title"));
			if (array.optJSONObject(i).isNull("workId")) {
				title.setTextColor(Color.BLACK);
			} else {
				title.setTextColor(getResources().getColor(
						R.color.workrportblue));
			}
			v.setTag(array.optJSONObject(i));
			tomorrowJsonArray.add(array.optJSONObject(i));
			tomorrowList.addView(v);
			if (i < (array.length() - 1)) {
				v = LayoutInflater.from(mContext).inflate(
						R.layout.report_cell_view, null);
			}
		}
	}

	private void addTomorrowItem(String tmp) {

		if (tmp == null || tmp.length() < 1) {
			PayUtils.showToast(mContext, "标题不能为空", 1000);
			return;
		}
		tomorrowScroll.setVisibility(View.VISIBLE);

		tomorrowScroll.setVisibility(View.VISIBLE);

		View v = LayoutInflater.from(mContext).inflate(
				R.layout.report_cell_view, null);
		if ((tomorrowList.getChildCount() + 1) > 3) {
			v.measure(0, 0);
			tomorrowScroll.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, v.getMeasuredHeight() * 3));
		} else {
			tomorrowScroll.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		initListClick(v);
		TextView title = (TextView) v.findViewById(R.id.title);
		title.setText(tmp);
		title.setTextColor(Color.BLACK);
		JSONObject obj = new JSONObject();
		try {
			obj.put("title", tmp);
			obj.put("type", PLAN);
			obj.put("workId", null);
			obj.put("progress", 0);
			obj.put("usedTime", 0);
			obj.put("theWorkloadOf", 0);
			obj.put("planStartTime",
					PayUtils.formatData("yyyy-MM-dd HH:mm:ss", 0));
			obj.put("planEndTime",
					PayUtils.formatData("yyyy-MM-dd HH:mm:ss", 0));
		} catch (JSONException e) {

			e.printStackTrace();
		}
		tomorrowJsonArray.add(0, obj);
		v.setTag(obj);

		title.setVisibility(View.VISIBLE);
		tomorrowList.addView(v, 0);

	}

	Calendar globalCalendar = null;

	private void selectTime(String format, final TextView label,
			final DateTimeStyle style) {

		final SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d = new Date();
		try {
			d = sdf.parse(label.getText().toString().trim());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		globalCalendar = (Calendar) ca.clone();
		Object obj = label.getTag();
		long minDate = 0;
		if (obj != null && obj instanceof TextView) {
			TextView tv = (TextView) obj;
			try {
				minDate = sdf.parse(tv.getText().toString().trim()).getTime();
				Calendar tmp = Calendar.getInstance();
				tmp.setTimeInMillis(minDate);
				switch (reportType) {
				case REPORT_DAY:
					tmp.set(Calendar.DAY_OF_YEAR,
							tmp.get(Calendar.DAY_OF_YEAR) + 1);

					break;
				case REPORT_WEEK:

					break;
				case REPORT_MONTH:
					tmp.set(Calendar.MONTH, tmp.get(Calendar.MONTH) + 1);
					break;
				case REPORT_CUSTOM:
					if (label == tomorrowTimeLabel1)
						tmp.set(Calendar.DAY_OF_YEAR,
								tmp.get(Calendar.DAY_OF_YEAR) + 1);
					break;
				}
				minDate = tmp.getTimeInMillis();

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DateTimeStyle stype = style;
		DateTimePopup.builder(mContext).setDateTimeStyle(stype)
				.setCurrentTime(ca).setMinDate(minDate)
				.setOnMessageListener(new OnMessageListener() {

					@Override
					public void onClick(PopupItem msg) {
						if (msg.mCode.equals("commit")) {
							if (globalCalendar.getTimeInMillis() == ca
									.getTimeInMillis())
								return;

							String message = "";
							if (label.getTag(R.id.tag_self).toString()
									.equals("today")) {
								message = "更改日期会清空已关联的总结工作项，确定继续吗？";
								if (todayJsonArray.size() < 1
										&& tomorrowJsonArray.size() < 1) {
									changeTime(label, sdf);
									return;
								}
								TextView tmp = label;
								boolean flag = false;
								while (true) {
									Object relation = tmp
											.getTag(R.id.tag_relation);
									if (relation != null
											&& relation instanceof TextView) {
										TextView tv = (TextView) relation;
										tmp = tv;
										try {
											Date d = sdf.parse(tv.getText()
													.toString().trim());
											if (globalCalendar
													.getTimeInMillis() >= d
													.getTime()) {
												if (!tv.getTag(R.id.tag_self)
														.toString()
														.equals("today")) {
													message = "更改日期会清空已关联的总结和计划工作项，确定继续吗？";
													flag = true;
													break;
												}
											}
										} catch (Exception e) {

										}
									} else {
										break;
									}

								}
								if (!flag) {
									changeTime(label, sdf);
									return;
								}
							} else {
								message = "更改日期会清空已关联的计划工作项，确定继续吗？";
								if (tomorrowJsonArray.size() < 1) {
									changeTime(label, sdf);
									return;
								}
							}

							new AlertDialog.Builder(mContext)
									.setTitle("警告")
									.setMessage(message)
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													changeTime(label, sdf);

												}

											}).setNegativeButton("取消", null)
									.create().show();

						}
					}

				}).setOnDateTimeChangeListener(new OnDateTimeChange() {

					@Override
					public void onDateTimeChange(Calendar c, DateTimeStyle style) {
						globalCalendar = c;
					}
				}).show(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private void changeTime(TextView label, SimpleDateFormat sdf) {
		label.setText(sdf.format(globalCalendar.getTime()));
		if (label.getTag(R.id.tag_self).toString().equals("today")) {
			removeView(todayList, todayJsonArray);
			if (todayJsonArray.size() < 1) {
				todayScroll.setVisibility(View.GONE);
			}
		} else {
			removeView(tomorrowList, tomorrowJsonArray);

			if (tomorrowJsonArray.size() < 1) {
				tomorrowScroll.setVisibility(View.GONE);
			}
		}
		TextView tmp = label;
		while (true) {
			Object relation = tmp.getTag(R.id.tag_relation);
			if (relation != null && relation instanceof TextView) {
				TextView tv = (TextView) relation;
				try {
					Date d = sdf.parse(tv.getText().toString().trim());
					if (globalCalendar.getTimeInMillis() >= d.getTime()) {
						switch (reportType) {
						case REPORT_DAY:
							globalCalendar
									.set(Calendar.DAY_OF_YEAR, globalCalendar
											.get(Calendar.DAY_OF_YEAR) + 1);

							break;
						case REPORT_WEEK:

							break;
						case REPORT_MONTH:
							globalCalendar.set(Calendar.MONTH,
									globalCalendar.get(Calendar.MONTH) + 1);
							break;
						case REPORT_CUSTOM:
							globalCalendar
									.set(Calendar.DAY_OF_YEAR, globalCalendar
											.get(Calendar.DAY_OF_YEAR) + 1);
							break;
						}
						tv.setText(sdf.format(globalCalendar.getTime()));
						if (tv.getTag(R.id.tag_self).toString().equals("today")) {
							removeView(todayList, todayJsonArray);
							if (todayJsonArray.size() < 1) {
								todayScroll.setVisibility(View.GONE);
							}
						} else {
							removeView(tomorrowList, tomorrowJsonArray);

							if (tomorrowJsonArray.size() < 1) {
								tomorrowScroll.setVisibility(View.GONE);
							}
						}

						tmp = tv;
					} else {
						break;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			} else {
				break;
			}
		}
	}

	ArrayList<WeekEntity> reasons;
	protected WeekEntity currentWeekEntity;
	WeekEntity tomorrowWeekEntity;

	public void selectWeekTime(final View v, final WeekEntity entity) {

		if (reasons == null || reasons.size() < 1) {
			PayUtils.showToast(mContext, "系统正在初始化时间周期，请稍后重试", 2000);
			return;
		}
		if (!(v instanceof TextView)) {
			return;
		}
		final TextView tv = (TextView) v;

		final ArrayList<WeekEntity> copy = new ArrayList<WeekEntity>();

		Object obj = tv.getTag();
		if (obj != null && obj instanceof TextView) {
			TextView tmp = (TextView) obj;
			String item = tmp.getText().toString().trim();
			int start = -1;
			for (int i = 0; i < reasons.size(); i++) {
				if (reasons.get(i).toString().equals(item)) {
					start = i;
					break;
				}
			}

			if (start != -1) {
				copy.addAll(reasons.subList(start + 1, reasons.size()));
			} else {
				copy.addAll(reasons);
			}
		} else {
			copy.addAll(reasons);
		}
		int index = copy.indexOf(entity);
		new WheelViewPopup.WheelViewMenuBuilder(mContext)
				.setAdapter(new TextAdapter(copy)).setTextSize(20)
				.setCurrentItem(index <= 0 ? 0 : index)
				.setOnItemChangeListener(new OnItemChangeListener() {

					@Override
					public void onChange(final WeekEntity item) {
						if (item.toString().equals(entity.toString())) {
							return;
						}
						String message = "";
						if (tv == todayTimeLabel1) {
							message = "更改日期会清空已关联的总结工作项，确定继续吗？";
							if (todayJsonArray.size() < 1
									&& tomorrowJsonArray.size() < 1) {
								changeTime(tv, item, copy);
								return;
							}
							if (currentWeekEntity.getEndDate()
									.getTimeInMillis() > tomorrowWeekEntity
									.getStartDate().getTimeInMillis()) {
								message = "更改日期会清空已关联的总结和计划工作项，确定继续吗？";
							}
						} else {
							message = "更改日期会清空已关联的计划工作项，确定继续吗？";
							if (tomorrowJsonArray.size() < 1) {
								changeTime(tv, item, copy);
								return;
							}
						}
						new AlertDialog.Builder(mContext)
								.setTitle("警告")
								.setMessage(message)
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												changeTime(tv, item, copy);
											}

										}).setNegativeButton("取消", null)
								.create().show();

					}
				}).show();
	}

	private void changeTime(TextView tv, WeekEntity item,
			ArrayList<WeekEntity> copy) {
		tv.setText(item.toString());

		if (tv == todayTimeLabel1) {

			currentWeekEntity = item.clone();
			removeView(todayList, todayJsonArray);
			if (todayJsonArray.size() < 1) {

				todayScroll.setVisibility(View.GONE);
			}
			if (currentWeekEntity.getEndDate().getTimeInMillis() > tomorrowWeekEntity
					.getStartDate().getTimeInMillis()) {
				int index = copy.indexOf(item);
				if (index != -1 && index < (copy.size() - 1)) {
					item = copy.get(index + 1);
				}
				tomorrowTimeLabel1.setText(item.toString());
				removeView(tomorrowList, tomorrowJsonArray);
				if (tomorrowJsonArray.size() < 1) {
					tomorrowScroll.setVisibility(View.GONE);
				}
				tomorrowWeekEntity = item.clone();
			}
		} else if (tv == tomorrowTimeLabel1) {
			tomorrowWeekEntity = item.clone();
			removeView(tomorrowList, tomorrowJsonArray);
			if (tomorrowJsonArray.size() < 1) {
				tomorrowScroll.setVisibility(View.GONE);
			}
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

			SelectPictureActivity_.intent(mContext).imageItems(items)
					.imageItem(item).noOperation(true).start();
		}

		@Override
		public void onAddPictureClick() {
			ArrayList<com.miicaa.common.base.PopupItem> items = new ArrayList<PopupItem>();
			items.add(new PopupItem("选择图片", "pic"));
			items.add(new PopupItem("选择文件", "file"));

			items.add(new PopupItem("取消", "cancel"));
			BottomScreenPopup.builder(mContext).setItems(items)
					.setDrawable(R.drawable.white_color_selector)

					.setMargin(false)
					.setOnMessageListener(new OnMessageListener() {
						@Override
						public void onClick(PopupItem msg) {
							if (msg.mCode.equals("pic")) {
								Intent i = new Intent(mContext,
										PhotoGridContentActivity.class);
								startActivityForResult(i, GRID_PHOTO_CHECK);
								overridePendingTransition(
										R.anim.my_slide_in_right,
										R.anim.my_slide_out_left);
							} else if (msg.mCode.equals("file")) {
								Intent i = new Intent(mContext,
										FileListActivity_.class);
								startActivityForResult(i, GRID_FILE_CHECK);
								overridePendingTransition(
										R.anim.my_slide_in_right,
										R.anim.my_slide_out_left);
							}
						}
					}).show();

		}
	};

	private void showSmallPhoto(Bitmap bitmap, String fileLocal) {
		PicturButton picturButton = new PicturButton(mContext, bitmap,
				fileLocal);
	}

	private void showSmallPhoto(Bitmap bitmap, String fileLocal, String fid) {
		PicturButton picturButton = new PicturButton(mContext, bitmap,
				fileLocal);
	}



	private void setKindsImage(TextView mHeadImg, String ext) {
		String optString = ext.toLowerCase();
		if (optString.equals("txt")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_txt, 0, 0, 0);
		} else if (optString.equals("pdf")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_pdf, 0, 0, 0);
		} else if (optString.equals("doc")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_word, 0, 0, 0);
		} else if (optString.equals("docx")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_word, 0, 0, 0);
		} else if (optString.equals("zip")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_rar, 0, 0, 0);
		} else if (optString.equals("rar")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_rar, 0, 0, 0);
		} else if (optString.equals("ppt") || optString.equals("pptx")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_ppt, 0, 0, 0);
		} else if (optString.equals("xlsx") || optString.equals("xls")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_execl, 0, 0, 0);
		} else {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_normal, 0, 0, 0);
		}

	}

	@Background
	void initWeek() {
		try {
			Calendar ca = Calendar.getInstance();
			int year = ca.get(Calendar.YEAR);
			reasons = DateUtil.printfWeeks(year - 1, year + 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class TextAdapter implements WheelAdapter {

		ArrayList<WeekEntity> items;
		int length = 0;

		public TextAdapter(ArrayList<WeekEntity> reasons) {
			this.items = new ArrayList<WeekEntity>();
			this.items.addAll(reasons);
			for (WeekEntity s : this.items) {
				if (s.toString().length() > length) {
					length = s.toString().length();
				}
			}
		}

		public WeekEntity getWeekEntity(int index) {

			return this.items.get(index);
		}

		@Override
		public int getItemsCount() {

			return this.items.size();
		}

		@Override
		public String getItem(int index) {

			return this.items.get(index).toString();
		}

		@Override
		public int getMaximumLength() {

			return length;
		}

	}

	String paramStr = "";

	private boolean checkParam() {
		String str = "";
		switch (reportType) {
		case REPORT_DAY:
			str = REPORT_TYPE_DAY;
			break;
		case REPORT_WEEK:
			str = REPORT_TYPE_WEEK;
			break;
		case REPORT_MONTH:
			str = REPORT_TYPE_MONTH;
			break;

		case REPORT_CUSTOM:
			str = REPORT_TYPE_CUSTOM;
			break;
		}
		paramStr = "{\"workDTO\":{\"reportType\":\"" + str
				+ "\",\"clientName\":\"phone端\"";
		if (mInfo != null) {
			paramStr += ",\"id\":\"" + mInfo.getId() + "\"";
		}
		String title = customName.getText().toString().trim();
		if (reportType == REPORT_CUSTOM) {
			
			if (title.equals("")) {
				PayUtils.showToast(mContext, "自定义报告标题不能为空", 2000);
				return false;
			}
			

		}
		paramStr += ",\"title\":\"" + title + "\"";
		paramStr += "},\"reportDTO\": {";
		if (mInfo != null) {
			paramStr += "\"id\":\"" + mInfo.getReportId() + "\",";
		}
		if (todayJsonArray.size() < 1 && tomorrowJsonArray.size() < 1) {
			str = "";
			switch (reportType) {
			case REPORT_DAY:
				str = "工作总结和工作计划至少要有一个不为空";
				break;
			case REPORT_WEEK:
				str = "工作总结和工作计划至少要有一个不为空";
				break;
			case REPORT_MONTH:
				str = "工作总结和工作计划至少要有一个不为空";
				break;

			case REPORT_CUSTOM:
				str = "工作总结和工作计划至少要有一个不为空";
				break;
			}
			PayUtils.showToast(mContext, str, 2000);
			return false;
		}
		if (mCommentUser.size() < 1) {
			PayUtils.showToast(mContext, "点评人不能为空", 2000);
			return false;
		}
		String format = PayUtils.formatData(" HH:mm:ss", new Date().getTime());
		// if(todayJsonArray.size()>0){
		String start = "";
		String end = "";
		switch (reportType) {
		case REPORT_DAY:
			start = todayTimeLabel1.getText().toString().trim() + " 00:00:00";
			end = todayTimeLabel1.getText().toString().trim() + " 23:59:59";
			break;
		case REPORT_WEEK:
			start = PayUtils.formatData("yyyy-MM-dd", currentWeekEntity
					.getStartDate().getTimeInMillis())
					+ " 00:00:00";
			end = PayUtils.formatData("yyyy-MM-dd", currentWeekEntity
					.getEndDate().getTimeInMillis())
					+ " 23:59:59";
			break;
		case REPORT_MONTH:
			start = ReportUtils.getMonthFirstDay(todayTimeLabel1.getText()
					.toString().trim());
			end = ReportUtils.getMonthLastDay(todayTimeLabel1.getText()
					.toString().trim());
			break;

		case REPORT_CUSTOM:
			start = todayTimeLabel1.getText().toString().trim() + " 00:00:00";
			end = todayTimeLabel2.getText().toString().trim() + " 23:59:59";
			break;
		}
		start = start.replaceAll("\\.", "-");
		end = end.replaceAll("\\.", "-");
		paramStr += "\"summarizeStartTime\":\"" + start + "\",";
		paramStr += "\"summarizeEndTime\":\"" + end + "\",";
		// }
		// if(tomorrowJsonArray.size()>0){
		{
			start = "";
			end = "";
			switch (reportType) {
			case REPORT_DAY:
				start = tomorrowTimeLabel1.getText().toString().trim()
						+ " 00:00:00";
				end = tomorrowTimeLabel1.getText().toString().trim()
						+ " 23:59:59";
				break;
			case REPORT_WEEK:
				start = PayUtils.formatData("yyyy-MM-dd", tomorrowWeekEntity
						.getStartDate().getTimeInMillis())
						+ " 00:00:00";
				end = PayUtils.formatData("yyyy-MM-dd", tomorrowWeekEntity
						.getEndDate().getTimeInMillis())
						+ " 23:59:59";
				break;
			case REPORT_MONTH:
				start = ReportUtils.getMonthFirstDay(tomorrowTimeLabel1
						.getText().toString().trim());
				end = ReportUtils.getMonthLastDay(tomorrowTimeLabel1.getText()
						.toString().trim());
				break;

			case REPORT_CUSTOM:
				start = tomorrowTimeLabel1.getText().toString().trim()
						+ " 00:00:00";
				end = tomorrowTimeLabel2.getText().toString().trim()
						+ " 23:59:59";
				break;
			}
			start = start.replaceAll("\\.", "-");
			end = end.replaceAll("\\.", "-");
			paramStr += "\"planStartTime\":\"" + start + "\",";
			paramStr += "\"planTimeEnd\":\"" + end + "\",";
		}
		String mdesc = desc.getText().toString().trim();
		if (mdesc.contains("\n"))
			mdesc = mdesc.replaceAll("\n", "<br>");
		if (mdesc != null && !mdesc.equals("")) {
			paramStr += "\"description\":\"" + mdesc + "\",";
		}
		String range = rightType.equals("00") ? "0" : "1";
		paramStr += "\"checkTheRange\":\"" + range + "\"},";
		paramStr += "\"todoUsersSet\": [";
		for (SamUser user : mCommentUser) {
			paramStr += "{\"name\":\"" + user.getmName() + "\",\"value\":\""
					+ user.getmCode() + "\"},";
		}
		paramStr = paramStr.substring(0, paramStr.length() - 1) + "],";
		if (labelIds.size() > 0) {
			paramStr += "\"labelStr\":\"" + allLableId + "\",";
		}

		paramStr += "\"reportListDTO\":[";
		for (JSONObject obj : todayJsonArray) {
			paramStr += obj.toString() + ",";
		}
		for (JSONObject obj : tomorrowJsonArray) {
			paramStr += obj.toString() + ",";
		}
		paramStr = paramStr.substring(0, paramStr.length() - 1) + "]}";

		return true;
	}

	/*
	 * 请求网络图片
	 */
	private void requestPicture() {
		PayUtils.showDialog(mContext);
		PictureHelper helper = new PictureHelper(mInfo.getId(),this);
		helper.setLoadCallBack(new PictureHelper.PictureLoadCallBack() {
			@Override
			public void loadImg(Bitmap bitmap, String fid) {
				if (bitmap != null && fid != null) {

				}
			}

			@Override
			public void errorCall(String msg) {
				Toast.makeText(mContext, "连接错误" + msg, Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void loadComplete() {
			}

			@Override
			public void loadFileComplete(ArrayList<MyFileItem> files,
					ArrayList<String> picutreList) {
				mUploadWidget.initPhotoOrFile(files, picutreList);
				PayUtils.closeDialog();
			}

			
		});
		helper.getAttachement();
	}



	private void removeView(LinearLayout parent, ArrayList<JSONObject> json) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child.getTag() != null && child.getTag() instanceof JSONObject
					&& !((JSONObject) child.getTag()).isNull("workId")) {
				parent.removeView(child);
				json.remove(i);
				--i;
			}
		}
	}

	@Override
	public void finish() {
		Bimp.clearMap();
		super.finish();
	}
	
	
	
	

}
