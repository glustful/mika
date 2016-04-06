package com.miicaa.home.ui.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.common.base.Utils;
import com.miicaa.home.HiddenSoftActivity;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.matter.MatterBuilder;
import com.miicaa.home.ui.org.DateTimePopup;
import com.miicaa.home.ui.org.DateTimePopup.DateTimeStyle;
import com.miicaa.home.ui.org.DateTimePopup.OnDateTimeChange;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.report.ReportUtils.RequestCallback;

@EActivity(R.layout.report_item_detail_view)
public class ReportItemDetailActivity extends HiddenSoftActivity {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		case 0:
			finish();
			break;

		}
	}
	

	@Extra
	ReportDetailInfo info;
	@Extra
	String jsonStr;
	@Extra
	boolean isEdit;
	@Extra
	boolean isFinish;// 是否是生成的报告修改

	@ViewById(R.id.pay_headTitle)
	TextView headTitle;// 标题
	@ViewById(R.id.pay_commitButton)
	Button commit;
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.report_item_detail_title)
	EditText title;
	@ViewById(R.id.report_item_detail_progress)
	EditText progress;
	@ViewById(R.id.report_item_detail_time)
	EditText time;
	@ViewById(R.id.report_item_detail_percent)
	EditText percent;
	@ViewById(R.id.report_item_detail_progress_label)
	TextView progressLabel;
	@ViewById(R.id.report_item_detail_time_label)
	TextView timeLabel;
	@ViewById(R.id.report_item_detail_percent_label)
	TextView percentLabel;
	@ViewById(R.id.report_item_detail_progress_label1)
	TextView progressLabel1;
	@ViewById(R.id.report_item_detail_time_label1)
	TextView timeLabel1;
	@ViewById(R.id.report_item_detail_percent_label1)
	TextView percentLabel1;
	@ViewById(R.id.planLayout)
	LinearLayout planLayout;
	@ViewById(R.id.planStart)
	TextView planStart;
	@ViewById(R.id.planEnd)
	TextView planEnd;
	@ViewById(R.id.progressLayout)
	LinearLayout progLayout;
	@ViewById(R.id.desc)
	EditText descript;
	@ViewById(R.id.totalcount)
	TextView totalCount;
	protected int requestCode = 0;

	@AfterTextChange(R.id.desc)
	void textChange(){
		totalCount.setText(descript.getText().length()+"");
	}
	@Click(R.id.planStart)
	void start() {
		selectTime("yyyy.MM.dd HH:mm:ss", planStart, DateTimeStyle.eDateTime);
	}

	@Click(R.id.planEnd)
	void end() {
		selectTime("yyyy.MM.dd HH:mm:ss", planEnd, DateTimeStyle.eDateTime);
	}
	
	@AfterTextChange(R.id.report_item_detail_title)
	void titleChange(Editable et){
		if (et.toString().length() >= 35) {
			PayUtils.showToast(mContext, "标题长度不能超过35个字", 3000);
		}
	}

	@AfterTextChange(R.id.report_item_detail_progress)
	void textChange(TextView et) {
		String str = et.getText().toString().trim();
		while (str.startsWith("0") && str.length() > 1) {
			str = str.substring(1, str.length());
			et.setText(str);
			if (et instanceof EditText) {
				EditText ed = (EditText) et;
				ed.setSelection(str.length());
			}
		}
		int value = 0;
		try {
			value = Integer.parseInt(str);
		} catch (Exception e) {
			value = 0;
			// et.setText("0");
		}
		if (value < 0) {
			et.setText("0");
			et.clearFocus();
			Utils.hiddenSoftBorad(mContext);
		} else if (value > 100) {
			et.setText("100");
			et.clearFocus();
			Utils.hiddenSoftBorad(mContext);
		}
	}

	@AfterTextChange(R.id.report_item_detail_percent)
	void textChange1(TextView et) {
		String str = et.getText().toString().trim();
		while (str.startsWith("0") && str.length() > 1) {
			str = str.substring(1, str.length());
			et.setText(str);
			if (et instanceof EditText) {
				EditText ed = (EditText) et;
				ed.setSelection(str.length());
			}
		}
		int value = 0;
		try {
			value = Integer.parseInt(str);
		} catch (Exception e) {
			value = 0;
			// et.setText("0");
		}

		if (value < 0) {
			et.setText("0");
			et.clearFocus();
			Utils.hiddenSoftBorad(mContext);
		} else if (value > 100) {
			et.setText("100");
			et.clearFocus();
			Utils.hiddenSoftBorad(mContext);
		}
	}

	@AfterTextChange(R.id.report_item_detail_time)
	void timeChange(TextView et) {
		String str = et.getText().toString().trim();
		while (!str.startsWith("0.") && str.startsWith("0") && str.length() > 1) {
			str = str.substring(1, str.length());
			et.setText(str);
			if (et instanceof EditText) {
				EditText ed = (EditText) et;
				ed.setSelection(str.length());
			}
		}
		float value = 0;
		try {
			value = Float.parseFloat(str);
		} catch (Exception e) {
			value = 0;
			// et.setText("0");
		}
		if (value < 0) {
			et.setText("0");
			et.clearFocus();
			Utils.hiddenSoftBorad(mContext);
		}
		if (str.contains(".") && !str.endsWith(".")
				&& str.lastIndexOf(".") < str.length() - 2) {
			
			et.setText(PayUtils.cleanZero(str.substring(0, str.length() - 1)));
			et.clearFocus();
			Utils.hiddenSoftBorad(mContext);
		}

	}

	@Click(R.id.pay_cancleButton)
	void cancel(View v) {
		if (!(commit instanceof Button))
			return;
		Button b = commit;
		if (b.getText().toString().trim().equals("提交")) {

			b.setText("...");
			progress.setVisibility(View.GONE);
			progressLabel.setVisibility(View.VISIBLE);
			progressLabel1.setHint("");
			time.setVisibility(View.GONE);
			timeLabel.setVisibility(View.VISIBLE);
			timeLabel1.setHint("");
			percent.setVisibility(View.GONE);
			percentLabel.setVisibility(View.VISIBLE);
			percentLabel1.setHint("");
			planEnd.setClickable(false);
			planStart.setClickable(false);
			title.setEnabled(false);
			descript.setEnabled(false);
			if (!jsonObj.isNull("planStartTime")
					&& jsonObj.optLong("planStartTime") > 0)
				planStart.setText(PayUtils.formatData("yyyy.MM.dd HH:mm:ss",
						jsonObj.optLong("planStartTime", 0)));
			else {
				planStart.setText("");
			}
			if (!jsonObj.isNull("planEndTime")
					&& jsonObj.optLong("planEndTime") > 0)
				planEnd.setText(PayUtils.formatData("yyyy.MM.dd HH:mm:ss",
						jsonObj.optLong("planEndTime", 0)));
			else {
				planEnd.setText("");
			}
			return;
		}
		finish();
	}

	@Click(R.id.pay_commitButton)
	void more(View v) {
		if (!(v instanceof Button))
			return;
		Button b = (Button) v;
		if (b.getText().toString().trim().equals("提交")) {
			commit();
			return;
		}
		ArrayList<com.miicaa.common.base.PopupItem> items = new ArrayList<PopupItem>();
		if (info == null || (!info.getStatus().equals("02")&&AccountInfo.instance().getLastUserInfo().getCode().equals(info.getCreatorCode()))) {
			items.add(new PopupItem("编辑详细信息", "edit"));
			items.add(new PopupItem("删除工作项", "delete"));
		}
		if (isFinish && type.equals(WorkReportActivity.PLAN)
				&& jsonObj.isNull("workId")
				&& jsonObj.optString("createdArrangement", "").equals("04")
				&& hasRight()) {
			items.add(new PopupItem("生成任务", "arrange"));
		}
		items.add(new PopupItem("取消", "cancel"));
		BottomScreenPopup.builder(mContext).setItems(items)
				.setDrawable(R.drawable.white_color_selector)

				.setMargin(false).setOnMessageListener(new OnMessageListener() {
					@Override
					public void onClick(PopupItem msg) {
						if (msg.mCode.equals("edit")) {
							title.setEnabled(true);
							descript.setEnabled(true);
							commit.setText("提交");
							progress.setVisibility(View.VISIBLE);
							progress.setText(progressLabel.getText());
							progressLabel.setVisibility(View.GONE);
							progressLabel1.setHint("小时");
							time.setVisibility(View.VISIBLE);
							time.setText(timeLabel.getText());
							timeLabel.setVisibility(View.GONE);
							timeLabel1.setHint("小时");
							percent.setVisibility(View.VISIBLE);
							percent.setText(percentLabel.getText());
							percentLabel.setVisibility(View.GONE);
							percentLabel1.setHint("小时");
							planEnd.setClickable(true);
							planStart.setClickable(true);
						} else if (msg.mCode.equals("delete")) {
							remove();
						} else if (msg.mCode.equals("arrange")) {
							Intent intent = new Intent(mContext,
									MatterBuilder.class);
							Bundle b = new Bundle();
							b.putString("dataType", "1");
							b.putString("arrangeType", jsonObj.optString("id"));
							b.putSerializable("info", info);
							b.putString("item", jsonStr);
							intent.putExtra("bundle", b);
							startActivityForResult(intent, requestCode);
						}
					}
				}).show();
	}

	private boolean hasRight() {
		Log.d("ReportItemDetailActivity", "hasRight() info:" + info);
		if (info == null)
			return false;
		String code = AccountInfo.instance().getLastUserInfo().getCode();
		if (code.equals(info.getCreatorCode()))
			return true;
		try {
			JSONArray arr = new JSONArray(info.getCommenter());
			for (int i = 0; i < arr.length(); i++) {
				if (code.equals(arr.optJSONObject(i).optString("userCode"))) {
					return true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void commit() {
		if (jsonObj == null) {
			return;

		}
		if (title.getText().toString().trim().equals("")) {
			PayUtils.showToast(mContext, "标题不能为空", 1000);
			return;
		}
		try {

			String s = time.getText().toString().trim();
			jsonObj.put("usedTime",
					(s == null || s.equals("")) ? 0 : Float.parseFloat(s));
			s = percent.getText().toString().trim();
			jsonObj.put("theWorkloadOf", (s == null || s.equals("")) ? 0
					: Integer.parseInt(s));
			s = title.getText().toString().trim();
			jsonObj.put("title", s);
			jsonObj.put("remark", descript.getText().toString().trim());
			if (type.equals(WorkReportActivity.PLAN)) {
				String beginTimeStr = planStart.getText().toString().trim();
				String endTimeStr = planEnd.getText().toString().trim();
				if (!isCorret(beginTimeStr, endTimeStr))
					return;
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy.MM.dd HH:mm:ss");
				s = planStart.getText().toString().trim();
				if (s != null && s.length() > 0)
					jsonObj.put("planStartTime", sdf.parse(beginTimeStr)
							.getTime());

				s = planEnd.getText().toString().trim();
				if (s != null && s.length() > 0)
					jsonObj.put("planEndTime", sdf.parse(endTimeStr).getTime());

			} else {
				s = progress.getText().toString().trim();
				jsonObj.put("progress", (s == null || s.equals("")) ? 0
						: Integer.parseInt(s));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {

		}
		if (isFinish && !jsonObj.isNull("id")) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("reportListId", jsonObj.optString("id"));
			map.put("title", jsonObj.optString("title"));
			map.put("remark", jsonObj.optString("remark"));
			map.put("progress", String.valueOf(jsonObj.optInt("progress")));
			map.put("usedTime", String.valueOf(jsonObj.optDouble("usedTime")));
			map.put("theWorkloadOf",
					String.valueOf(jsonObj.optInt("theWorkloadOf")));
			map.put("planStartTime",
					jsonObj.isNull("planStartTime") ? "" : PayUtils.formatData(
							"yyyy-MM-dd HH:mm:ss",
							jsonObj.optLong("planStartTime")));
			map.put("planEndTime",
					jsonObj.isNull("planEndTime") ? "" : PayUtils.formatData(
							"yyyy-MM-dd HH:mm:ss",
							jsonObj.optLong("planEndTime")));
			ReportUtils.requestList(mContext, new RequestCallback() {

				@Override
				public void callback(ResponseData data) {
					Intent data1 = new Intent();
					data1.putExtra("json", jsonObj.toString());
					setResult(RESULT_OK, data1);
					finish();

				}
			}, mContext.getString(R.string.report_update_item_url), map);
		} else {
			Intent data = new Intent();
			data.putExtra("json", jsonObj.toString());
			setResult(RESULT_OK, data);
			finish();
		}
	}

	Context mContext;
	JSONObject jsonObj;
	String type = WorkReportActivity.SUMMARIZE;

	@AfterInject
	void initData() {
		this.mContext = this;
		try {
			jsonObj = new JSONObject(jsonStr);
		} catch (Exception e) {
			jsonObj = null;
		}
	}

	@AfterViews
	void initUI() {
		headTitle.setText("工作项详细信息");
		commit.setText("...");
		if (isEdit)
			commit.setVisibility(View.VISIBLE);
		else {
			commit.setVisibility(View.INVISIBLE);
		}

		if (jsonObj == null) {
			clean();
		} else {
			if (!jsonObj.isNull("type")) {
				type = jsonObj.optString("type", WorkReportActivity.SUMMARIZE);
			}
			boolean isShow = false;
			if (info == null || (!info.getStatus().equals("02")&&AccountInfo.instance().getLastUserInfo().getCode().equals(info.getCreatorCode()))) {
				isShow = true;
			}


			if (isFinish && type.equals(WorkReportActivity.PLAN)
					&& jsonObj.isNull("workId")
					&& jsonObj.optString("createdArrangement", "").equals("04")
					&& hasRight()) {
				isShow = true;
			}
			if (isShow) {
				commit.setVisibility(View.VISIBLE);
			} else {
				commit.setVisibility(View.INVISIBLE);
			}
			if (!jsonObj.isNull("title")) {
				title.setText(jsonObj.optString("title"));
			} else {
				title.setText("标题为空");
			}
			
			if (!jsonObj.isNull("remark")) {
				descript.setText(jsonObj.optString("remark"));
			} else {
				descript.setText("");
			}


			if (!jsonObj.isNull("usedTime"))
				timeLabel.setText(""
						+ PayUtils.cleanZero(jsonObj.optDouble("usedTime", 0)));
			else {
				timeLabel.setText("0");
			}
			if (!jsonObj.isNull("theWorkloadOf"))
				percentLabel.setText("" + jsonObj.optInt("theWorkloadOf", 0));
			else {
				percentLabel.setText("0");
			}
			
			if (type.equals(WorkReportActivity.SUMMARIZE)) {
				// isFinish = false;
				progLayout.setVisibility(View.VISIBLE);
				planLayout.setVisibility(View.GONE);
				if (!jsonObj.isNull("progress"))
					progressLabel.setText(String.valueOf(jsonObj.optInt(
							"progress", 0)));
				else {
					progressLabel.setText("0");
				}
			} else {
				progLayout.setVisibility(View.GONE);
				planLayout.setVisibility(View.VISIBLE);
				if (!jsonObj.isNull("planStartTime")
						&& jsonObj.optLong("planStartTime") > 0)
					planStart.setText(PayUtils.formatData(
							"yyyy.MM.dd HH:mm:ss",
							jsonObj.optLong("planStartTime", 0)));
				else {
					planStart.setText("");
				}
				if (!jsonObj.isNull("planEndTime")
						&& jsonObj.optLong("planEndTime") > 0)
					planEnd.setText(PayUtils.formatData("yyyy.MM.dd HH:mm:ss",
							jsonObj.optLong("planEndTime", 0)));
				else {
					planEnd.setText("");
				}
				planEnd.setTag(planStart);
				planStart.setTag(R.id.tag_relation, planEnd);
				planEnd.setClickable(false);
				planStart.setClickable(false);
			}
		}
	}

	private void clean() {
		progressLabel.setText("0");
		timeLabel.setText("0");
		percentLabel.setText("0");
	}

	Calendar globalCalendar;

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
		Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		globalCalendar = (Calendar) ca.clone();
		Object obj = label.getTag();
		long minDate = new Date().getTime();
		if (obj != null && obj instanceof TextView) {
			TextView tv = (TextView) obj;
			try {
				minDate = sdf.parse(tv.getText().toString().trim()).getTime();

			} catch (ParseException e) {

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

							label.setText(sdf.format(globalCalendar.getTime()));

							TextView tmp = label;
							while (true) {
								Object relation = tmp.getTag(R.id.tag_relation);
								if (relation != null
										&& relation instanceof TextView) {
									TextView tv = (TextView) relation;
									try {
										Date d = sdf.parse(tv.getText()
												.toString().trim());
										if (globalCalendar.getTimeInMillis() > d
												.getTime()) {
											tv.setText(sdf
													.format(globalCalendar
															.getTime()));
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
					}

				}).setOnDateTimeChangeListener(new OnDateTimeChange() {

					@Override
					public void onDateTimeChange(Calendar c, DateTimeStyle style) {
						globalCalendar = c;
					}
				}).show(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	protected void remove() {
		new AlertDialog.Builder(mContext).setTitle("删除").setMessage("确认是否删除？")
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (isFinish && !jsonObj.isNull("id")) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("reportListId", jsonObj.optString("id"));

							ReportUtils.requestList(
									mContext,
									new RequestCallback() {

										@Override
										public void callback(ResponseData data) {
											Intent data1 = new Intent();
											data1.putExtra("json",
													jsonObj.toString());
											data1.putExtra("remove", true);
											setResult(RESULT_OK, data1);
											finish();

										}
									},
									mContext.getString(R.string.report_delete_item_url),
									map);
						} else {
							Intent data = new Intent();
							data.putExtra("json", jsonObj.toString());
							data.putExtra("remove", true);
							setResult(RESULT_OK, data);
							finish();
						}

					}
				}).setNegativeButton("取消", null).create().show();

	}

	private boolean isCorret(String beginTimeStr, String endTimeStr) {
		Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		if (beginTimeStr.length() != 0 || endTimeStr.length() != 0) {
			Long begin = getNormalDate(beginTimeStr) != null ? getNormalDate(
					beginTimeStr).getTime() : null;
			Long end = getNormalDate(endTimeStr) != null ? getNormalDate(
					endTimeStr).getTime() : null;

			Long now = System.currentTimeMillis();

			if (end != null && begin != null) {
				if (end < begin) {
					toast.setText("结束时间必须在开始时间之后");
					toast.show();
					return false;
				}
			} else {
				if (end == null)
					toast.setText("请输入结束时间");
				else
					toast.setText("请输入开始时间");
				toast.show();
				return false;
			}
			if (now > begin) {
				toast.setText("开始时间必须大于现在时间");
				toast.show();
				return false;
			}
		} else {

		}
		return true;
	}

	private Date getNormalDate(String str) {
		if (str.length() == 0) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		Date date = new Date();
		try {
			date = format.parse(str);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return date;
	}
}
