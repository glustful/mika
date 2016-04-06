package com.miicaa.home.ui.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.detail.DetailDiscussAdapter.OnDelDiscussListener;
import com.miicaa.detail.DetailProgressFragment;
import com.miicaa.detail.DetailScrollView;
import com.miicaa.detail.DiscussFootView;
import com.miicaa.detail.DiscussFootView.OnDiscussClickListener;
import com.miicaa.detail.DiscussProgressActivity_;
import com.miicaa.detail.ProgressFootView;
import com.miicaa.detail.ProgressFootView.OnProgressClickListener;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.common.accessory.AccessoryListActivity;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.home.ui.home.MatterCell;
import com.miicaa.home.ui.org.ArrangementPersonnel;
import com.miicaa.home.ui.org.ArrangementPlan_;
import com.miicaa.home.ui.org.DateTimePopup;
import com.miicaa.home.ui.org.DateTimePopup.DateTimeStyle;
import com.miicaa.home.ui.org.DateTimePopup.OnDateTimeChange;
import com.miicaa.home.ui.org.StyleDialog;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.report.DynamicFragment.OnDaynamicChangeListener;
import com.miicaa.home.ui.report.FragmentTabAdapter.OnRgsExtraCheckedChangedListener;
import com.miicaa.home.ui.report.ReportDetailContent.GetMatterInfoListener;
import com.miicaa.utils.AllUtils;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.view.ResizeLayout;

@EActivity(R.layout.report_detail_activity)
public class ReportDetailActivity extends FragmentActivity implements
		com.miicaa.detail.DetailScrollView.OnScrollListener,
		OnProgressClickListener, OnDiscussClickListener {

	public static ReportDetailActivity instance;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == FramMainActivity.COMPLETE) {
			isComplete = true;
			contentControl.requestReport();
			return;
		}
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {

		case COMMENTREQUEST:

			for (Fragment f : fragments) {
				if (f instanceof CommentFragment) {
					CommentFragment cf = (CommentFragment) f;
					cf.doBackground();
					if (!mInfo.getCreatorCode().equals(
							AccountInfo.instance().getLastUserInfo().getCode())) {
						progressFoot.nodoProgress(true);
					} else {
						isComplete = true;
						// cancel();
						// return;
					}
					commentNum++;
					mInfo.setCommentNum(commentNum);
					progressChar.setText("点评(" + commentNum + ")");
					isComplete = true;
				}
				if (f instanceof DynamicFragment) {
					addDynamic(dynamicNum++);
					DynamicFragment Df = (DynamicFragment) f;
					Df.setOnDaynmicChangeListener(new OnDaynamicChangeListener() {

						@Override
						public void onCountChange(int count) {

						}
					});
					if (Df.isAdded())
						Df.doBackground();

				}
			}
			break;
		case PLANREQUEST:

			isComplete = true;
			contentControl.requestReport();
			break;
		case EDITREQUEST:
			isComplete = true;
			contentControl.requestReport();
			for (Fragment f : fragments) {

				if (f instanceof DynamicFragment) {
					addDynamic(dynamicNum++);
					DynamicFragment Df = (DynamicFragment) f;
					if (Df.isAdded())
						Df.doBackground();

				}
			}
			break;
		}
	}

	public static final int COMMENTREQUEST = 0;
	public static final int PLANREQUEST = 1;
	public static final int EDITREQUEST = 2;
	@Extra
	String dataId;
	@Extra
	boolean isDiscover;
	@Extra
	boolean isPushMessage;

	@ViewById(R.id.matter_detail_title)
	TextView title;
	@ViewById(R.id.report_detail_content)
	LinearLayout content;
	@ViewById(R.id.holdTopTab)
	RelativeLayout topTab;
	@ViewById(R.id.manualTab)
	RelativeLayout manualTab;
	@ViewById(R.id.scrollview)
	DetailScrollView scrollview;
	@ViewById
	ResizeLayout whole;
	@ViewById
	ProgressFootView progressFoot;
	@ViewById
	DiscussFootView discussFoot;
	@ViewById
	LinearLayout footer;
	@ViewById(R.id.more)
	RelativeLayout more;

	LinearLayout doProgress;
	Button progressChar;
	LinearLayout doDiscuss;
	Button discusssChar;
	LinearLayout doTrends;
	Button trendsChar;
	ImageView bottomFlag1;
	ImageView bottomFlag2;
	ImageView bottomFlag3;
	ReportDetailContent contentControl;
	ProgressDialog progressDialog;
	Context mContext;
	ArrayList<PopupItem> popitems;
	List<Fragment> fragments = new ArrayList<Fragment>();
	ArrayList<Button> rgs = new ArrayList<Button>();
	ReportDetailInfo mInfo;

	private boolean isComplete;
	private Bundle mBundle;
	protected long dicussNum = 0;
	private long commentNum = 0;
	private long dynamicNum = 0;

	@Click(R.id.moreButton)
	void more() {

		if (popitems.size() > 0) {
			if (!popitems.get(popitems.size() - 1).mCode.equals("cancel")) {

				popitems.add(new PopupItem("取消", "cancel"));
			}
		}
		BottomScreenPopup.builder(this).setItems(popitems)

		.setMargin(false).setOnMessageListener(new OnMessageListener() {
			@Override
			public void onClick(PopupItem msg) {

				if (msg.mCode.equals("finish")) {
					onDoFinish();
				} else if (msg.mCode.equals("edit")) {
					onEditor();
				} else if (msg.mCode.equals("delete")) {
					onDelete();
				} else if (msg.mCode.equals("attachment")) {
					onAttchment();
				} else if (msg.mCode.equals("round")) {
					if (mInfo.getRange().equals("1")) {
						Intent intent = new Intent(mContext,
								ArrangementPersonnel.class);
						Bundle bundle = new Bundle();
						bundle.putString("dataId", dataId);
						bundle.putString("type", MatterCell.WORKREPORTTYPE);
						intent.putExtra("bundle", bundle);
						startActivity(intent);
						return;
					}
					CheckOfRange_.intent(mContext).type(mInfo.getRange())
							.start();
				}
			}
		}).show();

	}

	protected void onEditor() {
		WorkReportActivity_.intent(mContext)
				.reportType(ReportUtils.convertType(mInfo.getReportType(1)))
				.mInfo(mInfo).pngs(contentControl.pngInfos)
				.tfiles(contentControl.fileInfos).startForResult(EDITREQUEST);

	}

	@Click(R.id.cancleButton)
	void cancel() {
		if (isDiscover) {
			setResult(Activity.RESULT_OK);
			finish();
			return;
		}
		Intent i = new Intent(mContext, FramMainActivity.class);
		if (isComplete) {
			i.putExtra("data", FramMainActivity.COMPLETE);
			startActivity(i);
		} else // 若activity栈没有其他activity了
		if (MyApplication.getInstance().isActivityStackTop()) {
			startActivity(i);
		} else {
			finish();
		}
		// finish();
	}

	void initProgressDialog() {
		progressDialog = new StyleDialog(mContext);
		progressDialog.setCanceledOnTouchOutside(false);

	}

	@AfterInject
	void initData() {
		MyApplication.getInstance().addActivity(this);
		this.mContext = this;
		instance = this;
		contentControl = new ReportDetailContent(mContext, dataId);
		initProgressDialog();
		mBundle = new Bundle();
		mBundle.putString("dataId", dataId);
	}

	@AfterViews
	void initUI() {
		more.setVisibility(View.INVISIBLE);
		content.addView(contentControl.mView.getView());
		/*
		 * 初始时滚动条不滚动
		 */
		whole.setVisibility(View.GONE);
		scrollview.smoothScrollTo(0, 0);
		scrollview.setOnScrollListener(this);
		whole.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						onScroll(scrollview.getScrollY());
					}
				});
		initrgs();
		TextView tmp = (TextView) progressFoot.findViewById(R.id.textP);
		tmp.setText("点评");
		initFragment();
		progressFoot.setOnProgressClickListener(this);
		discussFoot.setOnDiscussClickListener(this);
		footer.removeView(discussFoot);
		initCallback();
		try {
			progressDialog.show();
		} catch (Exception e) {

		}
		contentControl.requestReport();
	}

	private void initCallback() {
		contentControl.setGetMatterInfoListener(new GetMatterInfoListener() {

			@Override
			public void getMatterInfo(ReportDetailInfo info) {
				mInfo = info;
				String titleStr = info.getTitle();
				titleStr.replace("\n", "");
				// * 附件个数
				title.setText(titleStr);
				// arrangePublicOrSecretTypes = info.getArrangeType();

				initNum(info);
				whole.setVisibility(View.VISIBLE);

				// * 是否可以填写计划时间和提醒时间
				// if(info.getCreatorCode().equals(AccountInfo.instance().getLastUserInfo().getCode())){
				progressFoot.nodoProgress(true);
				howShowPlanNRemindTime();
				// }
				JSONArray arr;
				try {
					arr = new JSONArray(info.getCommenter());

					for (int i = 0; i < arr.length(); i++) {
						if (AccountInfo
								.instance()
								.getLastUserInfo()
								.getCode()
								.equals(arr.optJSONObject(i).optString(
										"userCode"))) {
							progressFoot.nodoProgress(false);
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// howShowPlanNRemindTime(info.getTodoStatus(),
				// info.getTodoId());

				// * 重新获取operateGroup
				progressFoot.howShow();
				if ("已办结".equals(info.getTodoStatusStr())) {
					isComplete = true;
					progressFoot.setVisibility(View.GONE);
					;
				}
				/*
				 * operateGroup = info.getOperateGroup(); if
				 * (AllUtils.arrangeType.equals(dataType)) {
				 * writeProgress(info.getArrangeType(), info.getStatus());
				 * progressStatus = info.getStatus(); } else if
				 * (AllUtils.approvalType.equals(dataType)) {
				 * approvalFootView.setPopItems(initPopItems()); }
				 * argp.putString("operateGroup", operateGroup);
				 * 
				 * approvalFootView.howShow(); fs.addAll(setFragments(info));
				 * if(taskType != null && "discussion".equals(taskType)){
				 * ChangeFrag(1); }else{ ChangeFrag(0); }
				 */
				if (progressDialog != null && progressDialog.isShowing())
					progressDialog.dismiss();
				//
			}

			@Override
			public void getAttachmentData(ArrayList<AccessoryInfo> attechs) {
				/*
				 * if (attechs == null || attechs.size() < 1) return;
				 */
				initPopitem(mInfo);
				if (popitems != null && popitems.size() > 0) {
					more.setVisibility(View.VISIBLE);
				}
				/*
				 * PopupItem item = new PopupItem("所有附件", "attachment"); if
				 * (popitems.size() > 1) popitems.add(1, item); else {
				 * popitems.add(item); }
				 */
			}

			@Override
			public void setTodoPerson(JSONArray todos) {
				ArrayList<String> names = new ArrayList<String>();
				if (todos == null || todos.length() == 0)
					return;
				HashMap<String, String> ma = new HashMap<String, String>();
				for (int i = 0; i < todos.length(); i++) {
					JSONObject jsonObject = todos.optJSONObject(i);
					if (jsonObject != null) {
						String name = jsonObject.optString("userName");
						String code = jsonObject.optString("userCode");
						if (code != null && name != null)
							ma.put(code, name);
					}
				}
				for (Map.Entry<String, String> e : ma.entrySet()) {
					names.add(e.getValue());
					// pInfos.add(new TodoPersonInf(e.getValue(), e.getKey()));
				}

			}
		});

	}

	protected void initPopitem(ReportDetailInfo info) {
		popitems = new ArrayList<PopupItem>();
		PopupItem item = null;
		if (info.getRange().equals("1")) {
			item = new PopupItem("查看范围", "round");

			popitems.add(item);
		}
		if (contentControl.pngInfos.size() > 0
				|| contentControl.fileInfos.size() > 0) {
			popitems.add(new PopupItem("所有附件", "attachment"));
		}
		String mOperateGroup = info.getOperateGroup();
		boolean isDelete = false, isEditor = false, isPersonnel = true;
		;
		if ("11".equals(mOperateGroup) || "13".equals(mOperateGroup)
				|| "21".equals(mOperateGroup)) {
			isDelete = true;
			isEditor = true;

			if (!info.getAutoFinish().equals("1")) {
				item = new PopupItem("办结", "finish");

				popitems.add(item);
			}
		}

		if ("01".equals(mOperateGroup) || "81".equals(mOperateGroup)
				|| "85".equals(mOperateGroup) || "87".equals(mOperateGroup)
				|| "03".equals(mOperateGroup)) {

			isDelete = true;
			isEditor = true;
			if ("03".equals(mOperateGroup)) {

				isDelete = false;
			}

		}

		// 办结还有审批人选择
		if ("02".equals(mOperateGroup) || "03".equals(mOperateGroup)) {

			item = new PopupItem("办结", "finish");
			popitems.add(item);
		}

		if (isEditor) {
			item = new PopupItem("编辑", "edit");
			popitems.add(item);
		}

		if (isDelete && info.getCommentNum() == 0) {
			item = new PopupItem("删除", "delete");
			popitems.add(item);
		}

	}

	/*
	 * 是否显示计划时间
	 */
	void howShowPlanNRemindTime() {
		String code = AccountInfo.instance().getLastUserInfo().getCode();
		if (code.equals(mInfo.getCreatorCode())) {
			progressFoot.nodoPlanNRemindTime(false);
			return;
		}
		try {
			JSONArray arr = new JSONArray(mInfo.getCommenter());
			for (int i = 0; i < arr.length(); i++) {
				if (code.equals(arr.optJSONObject(i).optString("userCode"))) {
					progressFoot.nodoPlanNRemindTime(false);
					return;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		progressFoot.nodoPlanNRemindTime(true);
	}

	void initNum(ReportDetailInfo info) {
		dicussNum = info.getDiscussionNum();
		commentNum = info.getCommentNum();
		dynamicNum = info.getActivityNum();
		String str = "点评" + "(" + info.getCommentNum() + ")";
		progressChar.setText(str);

		String str1 = "评论" + "(" + info.getDiscussionNum() + ")";
		discusssChar.setText(str1);

		String str2 = "动态" + "(" + info.getActivityNum() + ")";
		trendsChar.setText(str2);
	}

	private void initrgs() {
		bottomFlag1 = (ImageView) topTab.findViewById(R.id.bottom_flag1);
		bottomFlag2 = (ImageView) topTab.findViewById(R.id.bottom_flag2);
		bottomFlag3 = (ImageView) topTab.findViewById(R.id.bottom_flag3);
		doProgress = (LinearLayout) topTab.findViewById(R.id.doProgress);
		progressChar = (Button) topTab.findViewById(R.id.progressChar);
		progressChar.setTag(bottomFlag1);
		doDiscuss = (LinearLayout) topTab.findViewById(R.id.doDiscuss);
		discusssChar = (Button) topTab.findViewById(R.id.discussChar);
		discusssChar.setTag(bottomFlag2);
		doTrends = (LinearLayout) topTab.findViewById(R.id.doTrends);
		trendsChar = (Button) topTab.findViewById(R.id.trendsChar);
		trendsChar.setTag(bottomFlag3);

		rgs.add(progressChar);
		rgs.add(discusssChar);
		rgs.add(trendsChar);
		progressChar.setText("点评(0)");
		bottomFlag2.setVisibility(View.INVISIBLE);
		bottomFlag3.setVisibility(View.INVISIBLE);
	}

	private void initFragment() {
		fragments.add(new CommentFragment());

		DiscussFragment df = new DiscussFragment();
		fragments.add(df);
		DynamicFragment dmF = (DynamicFragment) Fragment.instantiate(this,
				DynamicFragment.class.getName());
		dmF.setOnDaynmicChangeListener(new OnDaynamicChangeListener() {

			@Override
			public void onCountChange(int count) {
				String str = "动态" + "(" + count + ")";
				trendsChar.setText(str);
			}
		});
		fragments.add(dmF);
		df.setOnDelDiscussListener(new OnDelDiscussListener() {

			@Override
			public void deldiscuss() {
				dicussNum--;
				mInfo.setDiscussionNum(dicussNum);
				discusssChar.setText("评论" + "(" + dicussNum + ")");
			}

			@Override
			public void adddiscuss() {
				dicussNum++;
				mInfo.setDiscussionNum(dicussNum);
				discusssChar.setText("评论" + "(" + dicussNum + ")");
			}
		});
		for (Fragment f : fragments) {
			f.setArguments(mBundle);
		}
		FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments,
				R.id.fragmentadd, rgs);
		tabAdapter
				.setOnRgsExtraCheckedChangedListener(new OnRgsExtraCheckedChangedListener() {

					@Override
					public void OnRgsExtraCheckedChanged(int checkedId) {
						switch (checkedId) {
						case R.id.progressChar:
							footer.removeAllViews();
							footer.addView(progressFoot);

							break;
						case R.id.discussChar:
							footer.removeAllViews();
							footer.addView(discussFoot);

							break;
						case R.id.trendsChar:
							footer.removeAllViews();

							break;
						}
					}

				});
	}

	@Override
	public void onScroll(int scrollY) {
		int holdTop = Math.max(scrollY, manualTab.getTop());

		topTab.layout(0, holdTop, topTab.getWidth(),
				holdTop + topTab.getHeight());

	}

	/*
	 * 点评尾部事件
	 * 
	 * @see
	 * com.miicaa.detail.ProgressFootView.OnProgressClickListener#progressClick
	 * ()
	 */
	@Override
	public void progressClick() {
		DiscussProgressActivity_.intent(mContext).dataid(dataId)
				.arrangeType(DetailProgressFragment.REPORT_TYPE)
				.type(MatterCell.WORKREPORTTYPE).startForResult(COMMENTREQUEST);

	}

	@Override
	public void planClick() {
		ArrangementPlan_.intent(mContext).clientcode(mInfo.getClientName())
				.dataid(dataId).todoid(mInfo.getTodoId())
				.beginTimeStr(AllUtils.getnormalTime(mInfo.getPlanTime()))
				.endTimeStr(AllUtils.getnormalTime(mInfo.getPlanTimeEnd()))
				.startForResult(PLANREQUEST);

	}

	@Override
	public void remindClick() {
		toRemindTo(mInfo);

	}

	Date rDate;

	private void cancelRemind(String todoId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("todoId", todoId);
		param.put("onlyOne", "1");
		updateTime(param, "/home/phone/remind/cancelremind", true);
	}

	@Background
	void toRemindTo(ReportDetailInfo mInfo) {
		String startTime = AllUtils
				.getnormalTime(mInfo.getStartTime() == null ? 0 : mInfo
						.getStartTime().getTime());
		String endTime = AllUtils
				.getnormalTime(mInfo.getStartTime() == null ? 0 : mInfo
						.getStartTime().getTime());
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("dataId", dataId);
		params.put("todoId", mInfo.getTodoId());
		params.put("userCode", mInfo.getCreatorCode());
		params.put("srcAppCode", mInfo.getSrcCode());
		params.put("remindType", "1");
		params.put("remindTimeWay", "5");
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("orgcode", mInfo.getOrgcode());
		params.put("content", mInfo.getTitle());

		toRemind(params);
	}

	@UiThread
	void toRemind(final HashMap<String, String> params) {
		ArrayList<PopupItem> items = new ArrayList<PopupItem>();
		items.add(new PopupItem("清除", "clear"));
		DateTimeStyle stype = DateTimeStyle.eRemind;
		DateTimePopup.builder(mContext).setDateTimeStyle(stype).setItems(items)
				.setOnMessageListener(new OnMessageListener() {
					@Override
					public void onClick(PopupItem msg) {
						if ("clear".equals(msg.mCode)) {
							cancelRemind(params.get("todoId"));
						} else if ("commit".equals(msg.mCode)) {
							if (rDate != null) {
								String timeString = "";
								Date date = new Date();
								if (rDate.getTime() <= date.getTime()) {
									Toast.makeText(mContext, "提醒时间要大于现在的时间！",
											Toast.LENGTH_SHORT).show();
									return;
								}
								timeString = AllUtils.getnormalTime(rDate
										.getTime());
								params.put("remindTime", timeString);
								updateTime(params,
										"/home/phone/remind/setremind", false);
							} else {
								// Log.d(tag, "rDate ==== nulll");
							}
						}
					}
				}).setOnDateTimeChangeListener(new OnDateTimeChange() {

					@Override
					public void onDateTimeChange(Calendar c, DateTimeStyle style) {
						rDate = c.getTime();
						Log.d("tag",
								"hhhraDate ==== "
										+ (rDate != null ? rDate.getTime()
												: "null"));

					}
				}).show(Gravity.BOTTOM, 0, 0);
	}

	void updateTime(HashMap<String, String> params, String url,
			final Boolean isDel) {
		new RequestAdpater() {

			@Override
			public void onReponse(ResponseData data) {
				String str = isDel ? "清除" : "设置";
				if (data.getResultState() == ResultState.eSuccess) {
					/*
					 * 刷新提醒时间
					 */
					contentControl.requestReport();
					Toast.makeText(mContext, str + "提醒时间成功！" + data.getMsg(),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext,
							str + "提醒时间失败，请检查网络！" + data.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {
			}
		}.setUrl(url).addParam(params).notifyRequest();
	}

	private void onDelete() {
		final Toast delToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		String title = "确认删除这个工作报告？";
		final String url = mContext.getString(R.string.report_delete_url);

		new AlertDialog.Builder(mContext)
				.setTitle(title)
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
						PayUtils.showDialog(mContext);
						new RequestAdpater() {
							@Override
							public void onReponse(ResponseData data) {
								PayUtils.closeDialog();
								if (data.getResultState() == ResultState.eSuccess) {
									delToast.setText("你的工作报告已删除");
									delToast.show();
									setResult(FramMainActivity.COMPLETE);
									finish();
								} else {
									delToast.setText("删除失败" + data.getMsg());
									delToast.show();
								}
							}

							@Override
							public void onProgress(ProgressMessage msg) {
							}
						}.setUrl(url).addParam("workId", dataId)
								.addParam("clientCode", mInfo.getClientName())
								.notifyRequest();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();

					}
				}).show();
	}

	private void onAttchment() {
		Intent intent = new Intent(mContext, AccessoryListActivity.class);
		intent.putExtra("png", contentControl.pngInfos);
		intent.putExtra("file", contentControl.fileInfos);

		Bundle bundle = new Bundle();
		bundle.putString("dataId", dataId);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.my_slide_in_right,
				R.anim.my_slide_out_left);
	}

	private void onDoFinish() {

		String title = "您确定办结该工作报告？";
		final String urlFinsh = mContext.getString(R.string.report_finish_url);
		new AlertDialog.Builder(mContext)
				.setTitle(title)
				.setNegativeButton("办结", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
						final String urlF = urlFinsh;
						new RequestAdpater() {
							@Override
							public void onReponse(ResponseData data) {
								System.out.println("finish="
										+ data.getMRootData());
								if (data.getResultState() == ResultState.eSuccess) {
									refreshthis();
									for (Fragment f : fragments) {

										if (f instanceof DynamicFragment) {
											addDynamic(dynamicNum++);
											DynamicFragment Df = (DynamicFragment) f;
											if (Df.isAdded())
												Df.doBackground();

										}
									}
								} else {
									PayUtils.showToast(mContext,
											"办结失败：" + data.getMsg(), 1000);
								}
							}

							@Override
							public void onProgress(ProgressMessage msg) {
							}
						}.setUrl(urlF).addParam("workId", dataId)
								.notifyRequest();
					}
				})
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				}).show();

	}

	public void refreshthis() {

		contentControl.fileInfos.clear();
		contentControl.pngInfos.clear();

		contentControl.requestReport();
	}

	/*
	 * 评论尾部事件
	 * 
	 * @see
	 * com.miicaa.detail.DiscussFootView.OnDiscussClickListener#sendClick(java
	 * .lang.String)
	 */
	@Override
	public void sendClick(String content) {

		for (Fragment f : fragments) {
			if (f instanceof DiscussFragment) {
				DiscussFragment df = (DiscussFragment) f;
				df.sendDiscuss(content);
				break;
			}

		}

	}

	@Override
	public void sendDiscussDiscussClick(int position, String content) {

		for (Fragment f : fragments) {
			if (f instanceof DiscussFragment) {
				DiscussFragment df = (DiscussFragment) f;
				df.sendDiscussDiscuss(position, content);
			}

		}
	}

	@Override
	public void addClick() {
		// TODO Auto-generated method stub

	}

	public void addDynamic(long size) {
		try {
			dynamicNum = size;
			mInfo.setActivityNum(dynamicNum);
			trendsChar.setText("动态" + "(" + dynamicNum + ")");
		} catch (Exception e) {

		}
	}

	public void addDiscussNum(int size) {
		try {
			dicussNum = size;
			if (mInfo != null)
				mInfo.setDiscussionNum(dicussNum);
			discusssChar.setText("评论" + "(" + dicussNum + ")");
		} catch (Exception e) {

		}
	}

	public void setCommentNum(int length) {
		try {
			if(length>0){
				for(PopupItem item:popitems){
					if(item.mCode.equals("delete")){
						popitems.remove(item);
						break;
					}
				}
			}
			if (mInfo != null) {
				commentNum = length;
				mInfo.setCommentNum(length);
				progressChar.setText("点评" + "(" + commentNum + ")");
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void finish() {
		if (isPushMessage) {
			Intent i = new Intent(ReportDetailActivity.this,
					FramMainActivity.class);
			i.putExtra("data", FramMainActivity.COMPLETE);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}
		MyApplication.getInstance().removeActivity(this);
		super.finish();
	}

}
