package com.miicaa.detail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.common.base.Utils;
import com.miicaa.detail.ApprovalFootView.OnApprovalClickLintener;
import com.miicaa.detail.DetailDiscussAdapter.OnDelDiscussListener;
import com.miicaa.detail.DiscussFootView.OnDiscussClickListener;
import com.miicaa.detail.OldMatterTop.GetMatterInfoListener;
import com.miicaa.detail.ProgressFootView.OnProgressClickListener;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.business.matter.MatterInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.calendar.FramCalendarActivity;
import com.miicaa.home.ui.common.accessory.AccessoryListActivity;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.ui.home.FragmentInfo;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.home.ui.matter.MatterEditor;
import com.miicaa.home.ui.matter.approveprocess.ApprovalProcessListActivity_;
import com.miicaa.home.ui.matter.approveprocess.ApprovalProcessTypeView;
import com.miicaa.home.ui.matter.approveprocess.ApproveFixedGroupActivity_;
import com.miicaa.home.ui.menu.FragmentToPlan.SelectInf;
import com.miicaa.home.ui.menu.SelectPersonInfo;
import com.miicaa.home.ui.org.ArrangementFinish;
import com.miicaa.home.ui.org.ArrangementPersonnel;
import com.miicaa.home.ui.org.ArrangementPlan_;
import com.miicaa.home.ui.org.DateTimePopup;
import com.miicaa.home.ui.org.DateTimePopup.DateTimeStyle;
import com.miicaa.home.ui.org.DateTimePopup.OnDateTimeChange;
import com.miicaa.home.ui.org.MessageType;
import com.miicaa.home.ui.org.StyleDialog;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.report.ReportDetailActivity;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.AuthorityUtils.AuthorityState;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.view.ResizeLayout;
import com.yxst.epic.yixin.view.ResizeLayout.OnResizeListener;

@EActivity
public class MatterDetailAcrtivity extends FragmentActivity implements
		OnPageChangeListener, OnDiscussClickListener, OnApprovalClickLintener,
		OnProgressClickListener,
		com.miicaa.detail.DetailScrollView.OnScrollListener {
	public static final int SUBTASKREQUEST = 0;
	private static final int MATTEDITOR = 0x110;
	private static final int PROCESS_NEXT = 0x111;
	private static final int PROCESS_CHANGE = 0x112;
	public static MatterDetailAcrtivity instance;
	ArrayList<FragmentInfo> infos;
	ArrayList<TodoPersonInf> pInfos;
	ArrayList<Fragment> fs;    
	ProgressDialog progressDialog;
	static String tag = "MatterDetailAcrtivity";
	public static String progressStatus;
	String arrangePublicOrSecretTypes;
	ArrayList<PopupItem> popitems;	
	PopupItem cancelPop = null;
	/*
	 * 存储下角标要到达的位置
	 */
	HashMap<Integer, Integer> bottomTag;
	ArrayList<Button> tabView;
	ArrayList<ImageView> tabFlags;
	OldMatterTop ot;
	/**
	 */
	Boolean isComplete = false;
	View contentview;
	Button moreButton;
	TextView title;
	Button cancleButton;
	LinearLayout doProgress;
	Button progressChar;
	LinearLayout doDiscuss;
	Button discusssChar;
	LinearLayout doTrends;
	Button trendsChar;
	ImageView bottomFlag1;
	ImageView bottomFlag2;
	ImageView bottomFlag3;
	RelativeLayout topTab;
	RelativeLayout manualTab;
	DetailScrollView scrollview;
	public ApprovalFootView approvalFootView;
	public DiscussFootView discussFootView;
	public ProgressFootView progressFootView;
	ResizeLayout whole;
	LinearLayout fragmentReplace;
	LinearLayout contentLayout;
	android.support.v4.app.FragmentManager FM; 
	FragmentTransaction ts;
	
	Integer attCount;
	TabSpec tab1;
	TabSpec tab2;
	TabSpec tab3;
	@Extra
	String dataType;
	@Extra
	String dataId;
	@Extra
	String status;
	@Extra
	String operateGroup;
	@Extra
	Boolean isAlaready;
	@Extra
	String taskType;
	@Extra
	String titleText;
	@Extra
	Boolean checkIt;
	@Extra
	Boolean needFresh;
	@Extra
	boolean isPushMessage;

	@AfterInject
	void afterinject() {
		initProgressDialog();
		instance = this;
		pInfos = new ArrayList<TodoPersonInf>();
		tabView = new ArrayList<Button>();
		tabFlags = new ArrayList<ImageView>();
		infos = new ArrayList<FragmentInfo>();
		fs = new ArrayList<Fragment>();
		ot = new OldMatterTop(this, dataId);
		bottomTag = new HashMap<Integer, Integer>();
		contentview = ot.mDetailCell.getView();
	}

	void initProgressDialog() {
		progressDialog = new StyleDialog(MatterDetailAcrtivity.this);
		progressDialog.setCanceledOnTouchOutside(false);

	}

	public static MatterDetailAcrtivity getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		 FM = getSupportFragmentManager();
		MyApplication.getInstance().addActivity(this);
		init();
	}
	

	@Override
	protected void onNewIntent(Intent intent) {
		String str = intent.getStringExtra("refresh");
		if (isRefreshthis(str)) {
			refreshthis();
		}
		super.onNewIntent(intent);
	}

	Boolean isRefreshthis(String refresh) {
		return "refresh".equals(refresh);
	}

	void init() {
		setContentView(R.layout.matter_detail_activity);
		contentLayout = (LinearLayout) findViewById(R.id.matter_detail_content);
		moreButton = (Button) findViewById(R.id.moreButton);
		moreButton.setVisibility(View.INVISIBLE);
		moreButton.setOnClickListener(moreBClickListener);
		cancleButton = (Button) findViewById(R.id.cancleButton);
		manualTab = (RelativeLayout) findViewById(R.id.manualTab);
        topTab = (RelativeLayout) findViewById(R.id.holdTopTab);
		doProgress = (LinearLayout) topTab.findViewById(R.id.doProgress);
		progressChar = (Button) topTab.findViewById(R.id.progressChar);
		doDiscuss = (LinearLayout) topTab.findViewById(R.id.doDiscuss);
		discusssChar = (Button) topTab.findViewById(R.id.discussChar);
		doTrends = (LinearLayout) topTab.findViewById(R.id.doTrends);
		trendsChar = (Button) topTab.findViewById(R.id.trendsChar);
		bottomFlag1 = (ImageView) topTab.findViewById(R.id.bottom_flag1);
		bottomFlag2 = (ImageView) topTab.findViewById(R.id.bottom_flag2);
		bottomFlag3 = (ImageView) topTab.findViewById(R.id.bottom_flag3);
		progressFootView = (ProgressFootView) findViewById(R.id.progressFoot);
		discussFootView = (DiscussFootView) findViewById(R.id.discussFoot);
		approvalFootView = (ApprovalFootView) findViewById(R.id.approvalFoot);
		progressFootView.setOnProgressClickListener(this);
		discussFootView.setOnDiscussClickListener(this);
		approvalFootView.setonApprovalClickListener(this);
		fragmentReplace = (LinearLayout) findViewById(R.id.fragmentadd);
		scrollview = (DetailScrollView) findViewById(R.id.scrollview);
		/*
		 * 初始时滚动条不滚动
		 */
		scrollview.smoothScrollTo(0, 0);
		scrollview.setOnScrollListener(this);
		whole = (ResizeLayout) findViewById(R.id.whole);
		tabFlags.add(bottomFlag1);
		tabFlags.add(bottomFlag2);
		tabFlags.add(bottomFlag3);
		cancleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		title = (TextView) findViewById(R.id.matter_detail_title);
		whole.setVisibility(View.GONE);
		whole.setOnResizeListener(new OnResizeListener() {

			@Override
			public void OnResize(int w, int h, int oldw, int oldh) {
				Boolean b = h < oldh;
				discussFootView.setIsHidden(b);
			}
		});
		whole.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						onScroll(scrollview.getScrollY());
					}
				});
		try{
		progressDialog.show();
		}catch(Exception e){
			
		}
		initView();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}

	void initView() {
		setTitle(titleText);
		// 进展或审批
		contentLayout.addView(contentview);
		if (AllUtils.arrangeType.equals(dataType)) {
			progressChar.setText("任务");
		} else if (AllUtils.approvalType.equals(dataType)) {
			progressChar.setText("审批");
		}

		tabView.add(progressChar);
		tabView.add(discusssChar);
		tabView.add(trendsChar);

		for (int i = 0; i < tabView.size(); i++) {
			final int position = i;
			tabView.get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(fs.size()<1)
						return;
					ChangeFrag(position);
					tabViewChange(position);
				}
			});
		}

		initTab();
		uithread();

	}

	Bundle argp;
	Bundle argdiscuss;
	Bundle argtrends;

	void initTab() {
		argp = new Bundle();
		argp.putString("dataId", dataId);
		if (AllUtils.arrangeType.equals(dataType)) {

		} else if (AllUtils.approvalType.equalsIgnoreCase(dataType)) {

		}

		argdiscuss = new Bundle();
		argdiscuss.putString("dataId", dataId);
		argdiscuss.putString("type", AllUtils.discuss);
		argdiscuss.putString("dataType", dataType);
		argtrends = new Bundle();
		argtrends.putString("type", AllUtils.trends);
		argtrends.putString("dataId", dataId);
		argtrends.putString("dataType", dataType);
	}

	void uithread() {

		ot.setGetMatterInfoListener(new GetMatterInfoListener() {

			@Override
			public void getMatterInfo(MatterInfo info) {
				/*
				 * 附件个数
				 */
				setTitle(info.getTitle());
				arrangePublicOrSecretTypes = info.getArrangeType();
				if ("已办结".equals(info.getTodoStatusStr())) {
					iWasComplete();
				}
				initNum(info);
				whole.setVisibility(View.VISIBLE);
				/*
				 * 是否可以填写计划时间和提醒时间
				 */
				howShowPlanNRemindTime(info.getTodoStatus(), info.getTodoId());
				/*
				 * 重新获取operateGroup
				 */
				operateGroup = info.getOperateGroup();
				if (AllUtils.arrangeType.equals(dataType)) {
					writeProgress(info.getArrangeType(), info.getStatus());
					progressStatus = info.getStatus();
				} else if (AllUtils.approvalType.equals(dataType)) {
					approvalFootView.setPopItems(initPopItems());
				}
				argp.putString("operateGroup", operateGroup);
				progressFootView.howShow();
				approvalFootView.howShow();
				fs.addAll(setFragments(info));
				if(taskType != null && "discussion".equals(taskType)){
						ChangeFrag(1);
						tabViewChange(1);
					}else{
						ChangeFrag(0);
					}
				progressDialog.dismiss();
				initPopitem(info);
				if(popitems.size()>0){
					moreButton.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void getAttachmentData(ArrayList<AccessoryInfo> attechs) {
				if(popitems.size()>0){
					moreButton.setVisibility(View.VISIBLE);
				}
				if (attechs == null || attechs.size() < 1){
					
					return;
				}
				PopupItem item = new PopupItem("所有附件", "attachment");
				if (popitems.size() > 1)
					popitems.add(1, item);
				else {
					popitems.add(item);
				}
			}

			@Override
			public void setTodoPerson(JSONArray todos) {
				ArrayList<String> names = new ArrayList<String>();
		    	if(todos == null ||todos.length() == 0)
		    		return;
//		    	HashMap<String, String> ma = new HashMap<String, String>();
		        for (int i = 0; i < todos.length(); i ++) {
		            JSONObject jsonObject = todos.optJSONObject(i);
		            Log.d(tag, "setTodoPerson json:"+jsonObject);
		            if (jsonObject != null) {
		            	if(!"4".equals(jsonObject.optString("status"))){
		                String name = jsonObject.optString("userName");
		                String code = jsonObject.optString("userCode");
		                TodoPersonInf inf = new TodoPersonInf(name, code);
		                inf.status = jsonObject.optString("status");
		                pInfos.add(inf);
		            }
		            }
		        }
//		        for(Map.Entry<String, String> e : ma.entrySet()){
//		        	names.add(e.getValue());
//		        	pInfos.add(new TodoPersonInf(e.getValue(), e.getKey()));
//		        }
		    	
			}

			@Override
			public void onResponseFailed(String msg) {
				PayUtils.showToast(MatterDetailAcrtivity.this, msg, 3000);
				finish();
			}
		}); 

		if (AllUtils.arrangeType.equals(dataType)) {
			infos.add(new FragmentInfo(DetailProgressFragment_.class, argp));
			progressFootView.setVisibility(View.VISIBLE);

		} else if (AllUtils.approvalType.equalsIgnoreCase(dataType)) {

			infos.add(new FragmentInfo(DetailApprovalFragment_.class, argp));
			approvalFootView.setVisibility(View.VISIBLE);

		}

		infos.add(new FragmentInfo(DetailDiscussFragment_.class, argdiscuss));

		infos.add(new FragmentInfo(DetailTrendFragment_.class, argtrends));
		ot.requestMatter(dataType);
		tabViewChange(0);

	}

	protected void initPopitem(MatterInfo info) {
		popitems = new ArrayList<PopupItem>();
		PopupItem item = null;
		Log.d(tag, "info.getObServe():"+info.getObServe());
		if ("1".equals(info.getObServe())){ 
			item = new PopupItem("取消关注", "observe0");
			popitems.add(item);
			}
		else if ("0".equals(info.getObServe())){
			item = new PopupItem("关注", "observe1");
		popitems.add(item);}
		
		if(AllUtils.arrangeType.equals(dataType)
				&&(info.getCreatorCode().equals(AccountInfo.instance().getLastUserInfo().getCode())
				||info.IsTodoUser())
				&&"01".equals(info.getTodoStatus())
				&&AllUtils.PAYFOR_USER == 
				MyApplication.getInstance().getAuthority(AuthorityState.eSubTask)){
			item = new PopupItem("子任务", "subTask");
			popitems.add(item);
		}
		
		String mOperateGroup = info.getOperateGroup();
		boolean isDelete = false, isEditor = false, isPersonnel = true;
		boolean isTerminateion = false;
		if ("11".equals(mOperateGroup) || "13".equals(mOperateGroup)
				|| "21".equals(mOperateGroup)  ) {
			isDelete = true;
			isEditor = true;

			if ("1".equalsIgnoreCase(info.getArrangeType())) {

				isTerminateion = true;
				

			} else {
				isPersonnel = false;
			}
			if(!info.getAutoFinish().equals("1")){
			item = new PopupItem("办结", "finish");
			
			popitems.add(item);
			}
		}
		
		if(info.getCreatorCode().equals(
				AccountInfo.instance().getLastUserInfo().getCode())
				&&info.getStatus().equals("01")){
			isTerminateion = true;
			
		}
		if(isTerminateion){
			item = new PopupItem("终止", "termination");
			popitems.add(item);
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
		
		if("93".equals(mOperateGroup) || "91".equals(mOperateGroup)){
			isEditor = true;
		}
		

		// 办结还有审批人选择
		if ("02".equals(mOperateGroup) || "03".equals(mOperateGroup)
				|| "90".equals(mOperateGroup)
				||"93".equalsIgnoreCase(mOperateGroup)
				||"91".equalsIgnoreCase(mOperateGroup)) {
			item = new PopupItem("下一审批人", "selectpeople");
			popitems.add(item);
			
			item = new PopupItem("添加抄送", "addround");
			popitems.add(item);
		}
		if("02".equals(mOperateGroup) || "03".equals(mOperateGroup)
				||"92".equals(mOperateGroup) || "93".equals(mOperateGroup)){
			item = new PopupItem("办结", "finish");
			popitems.add(item);
			
		}
		if ("01".equalsIgnoreCase(mOperateGroup)
				|| "81".equalsIgnoreCase(mOperateGroup)
				|| "04".equalsIgnoreCase(mOperateGroup)
				|| "82".equalsIgnoreCase(mOperateGroup)
				|| "85".equalsIgnoreCase(mOperateGroup)
				|| "87".equalsIgnoreCase(mOperateGroup)
				|| "88".equalsIgnoreCase(mOperateGroup)
				|| "86".equalsIgnoreCase(mOperateGroup)
				) {
			item = new PopupItem("修改审批人", "modifypeople");
			popitems.add(item);

			item = new PopupItem("添加抄送", "addround");
			popitems.add(item);
		}

		
		if (isEditor) {
			item = new PopupItem("编辑", "edit");
			popitems.add(item);
		}
		if (isDelete) {
			item = new PopupItem("删除", "delete");
			popitems.add(item);
		}
		if (isPersonnel && ("1".equals(info.getArrangeType()) || AllUtils.approvalType.equals(dataType))) {
			String str = null;
				str = "查看办理人和抄送人";
				item = new PopupItem(str, "personnel");
			if (popitems.size() > 0)
				popitems.add(0, item);
			else {
				popitems.add(item);
			}
			
//			审批流程
			if(info.getIsFixedProcess()){
			item = new PopupItem("查看流程", "process");
			popitems.add(0,item);
			}
		}
	}

	void ChangeFrag(int position) {
		if(fs.size()<1)
			return;
		if (fs.get(position).isAdded()) {
			return;
		}
		try {
			ts = FM.beginTransaction();
			ts.replace(R.id.fragmentadd, fs.get(position));
			ts.commitAllowingStateLoss();
		} catch (Exception e) {

		} finally {
			scrollview.smoothScrollTo(0, 0);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		tabViewChange(arg0);

	}

	@SuppressLint("NewApi")
	void tabViewChange(int position) {
		approvalFootView.setVisibility(View.GONE);
		progressFootView.setVisibility(View.GONE);
		discussFootView.setVisibility(View.GONE);
		for (int i = 0; i < tabView.size(); i++) {
			Button btn = tabView.get(i);
			ImageView flag = tabFlags.get(i);
			if (i == position) {
				btn.setSelected(true);
				flag.setVisibility(View.VISIBLE);
				if(position == 0 && AllUtils.approvalType.equals(dataType)){
					approvalFootView.howShow();
				}
			} else {
				btn.setSelected(false);
				flag.setVisibility(View.GONE);
			}
		}
		switch (position) {
		case 0:
			if (AllUtils.approvalType.equals(dataType)) {
				approvalFootView.setVisibility(View.VISIBLE);
			} else if (AllUtils.arrangeType.equals(dataType)) {
				progressFootView.setVisibility(View.VISIBLE);
			}
			break;
		case 1:
			discussFootView.setVisibility(View.VISIBLE);
			break;
		case 2:
			discussFootView.setVisibility(View.GONE);
		default:
			break;
		}

	}

	OnClickListener moreBClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			clickMoreButton();
		}
	};

	private void clickMoreButton() {
		if (popitems == null || popitems.size() < 1) {
			//Toast.makeText(this, "数据解析出错", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!popitems.get(popitems.size()-1).mCode.equals("cancel"))
		 {
			cancelPop = new PopupItem("取消", "cancel");
			popitems.add(cancelPop);
		}
		BottomScreenPopup.builder(this).setItems(popitems)
				
				.setMargin(false)
				.setOnMessageListener(new OnMessageListener() {
					@Override
					public void onClick(PopupItem msg) {
						
						if (msg.mCode.equals("observe0")) {
							guanzhu(false);
						} else if (msg.mCode.equals("observe1")) {
							guanzhu(true);
						}else if(msg.mCode.equals("termination")){
							if(AllUtils.arrangeType.equals(dataType)){
							onTermination();
							}else if(AllUtils.approvalType.equals(dataType)){
								onApproveTermination();
							}
						}else if(msg.mCode.equals("finish")){
							onDoFinish();
						}else if(msg.mCode.equals("selectpeople")){
							if(ot.mInfo.getIsFixedProcess()){
								selectProcessApproval(PROCESS_NEXT);
							}else{
							selectApproval(Utils.APPROVAL_USER_NEXT,SelectContacter.APPROVE);
							}
						}else if(msg.mCode.equals("modifypeople")){
							if(ot.mInfo.getIsFixedProcess()){
								selectProcessApproval(PROCESS_CHANGE);
							}else{
							selectApproval(Utils.APPROVAL_USER_CHANGE,SelectContacter.changeApp);
							}
						}else if(msg.mCode.equals("addround")){
							addCopyPeople();
						}else if(msg.mCode.equals("personnel")){
							onPersonnel();
						}else if(msg.mCode.equals("edit")){
							onEditor();
						}else if(msg.mCode.equals("delete")){
							onDelete();
						}else if(msg.mCode.equals("attachment")){
							onAttchment();
						}else if(msg.mCode.equals("process")){
							onApprovalProcess();
						}else if(msg.mCode.equals("subTask")){
							Intent intent = new Intent(MatterDetailAcrtivity.this, MatterEditor.class);
					        Bundle bundle = new Bundle();
					        bundle.putString("dataType",dataType);
					        bundle.putString("matterType",ot.mInfo.getArrangeType());
					        bundle.putString("editType","01");
					        bundle.putString("id","");
					        bundle.putString("parentId", ot.mInfo.getId());
					        intent.putExtra("bundle",bundle);
					        startActivityForResult(intent, SUBTASKREQUEST);
						}
					}
				}).show();

	}

	public void DeleteRefreshthis() {
		infos.clear();
		fs.clear();
		pInfos.clear();
		ot.fileInfos.clear();
		ot.pngInfos.clear();
		try{
		String str = discusssChar.getText().toString();
		int num = Integer.parseInt(str.substring(str.indexOf("(")+1, str.indexOf(")")));
		num = num -1;
		discusssChar.setText("评论("+num+")");
		}catch(Exception e){
			e.printStackTrace();
		}
		uithread();
	}
	
	public void clean(){
		infos.clear();
		fs.clear();
		pInfos.clear();
		ot.fileInfos.clear();
		ot.pngInfos.clear();
	}
	
	public void refreshthis() {
		clean();
		
		uithread();
	}

	public void refreshProgress() {
		DetailProgressFragment.getInstance().doBackground();
	}

	public ArrayList<PopupItem> initPopItems() {
		ArrayList<PopupItem> items = new ArrayList<PopupItem>();
		approvalFootView.nodoApprove(false);
		if ("06".equals(operateGroup) || "81".equals(operateGroup)
				|| "82".equals(operateGroup)) {
			items.add(new PopupItem("同意", MessageType.eAgree.toString()));
			items.add(new PopupItem("不同意", MessageType.eDisAgree.toString()));
		} else if ("83".equals(operateGroup) || "87".equals(operateGroup)
				|| "88".equals(operateGroup)) {
			items.add(new PopupItem("会签通过", MessageType.eJointlyPass.toString()));
			items.add(new PopupItem("会签不通过", MessageType.eJoinlyMiss.toString()));
		} else if ("84".equals(operateGroup) || "85".equals(operateGroup)
				|| "86".equals(operateGroup)) {
			items.add(new PopupItem("同意", MessageType.eAgree.toString()));
			items.add(new PopupItem("不同意", MessageType.eDisAgree.toString()));
			items.add(new PopupItem("会签通过", MessageType.eJointlyPass.toString()));
			items.add(new PopupItem("会签不通过", MessageType.eJoinlyMiss.toString()));
		} else {
			approvalFootView.nodoApprove(true);
		}
		items.add(new PopupItem("取消", "cancel"));
		return items;
	}

	int dicussNum = 0;
	void initNum(MatterInfo info) {
		if (AllUtils.arrangeType.equals(dataType)) {
			String str = "进展" + "(" + info.getProgressNum() + ")";
			progressChar.setText(str);
		} else if (AllUtils.approvalType.equals(dataType)) {
			String str = "审批" + "(" + info.getFlowRecordNum() + ")";
			progressChar.setText(str);
		}

		String str1 = "评论" + "(" + info.getDiscussionNum() + ")";
		discusssChar.setText(str1);
		this.dicussNum = info.getDiscussionNum().intValue();

		String str2 = "动态" + "(" + info.getActivityNum() + ")";
		trendsChar.setText(str2);
	}

	void writeProgress(String arrangeType, String state) {
		if (DetailProgressFragment.PUBLIC_ARRANGE.equals(arrangeType)) {
			if ("12".equals(operateGroup) || "13".equals(operateGroup)) {
				progressFootView.nodoProgress(false);
			} else {
				progressFootView.nodoProgress(true);
			}
		} else if (DetailProgressFragment.SECRET_ARRANGE
				.equalsIgnoreCase(arrangeType)) {
			/*
			 * 不允许填写评论
			 */
			if (!"01".equals(state)) {
				progressFootView.nodoProgress(true);
			}
			doDiscuss.setVisibility(View.INVISIBLE);
			doTrends.setVisibility(View.INVISIBLE);
		}

	}

	/*
	 * 是否显示计划时间
	 */
	void howShowPlanNRemindTime(String status, String todoid) {
		if (status != null && "01".equalsIgnoreCase(status) && todoid != null) {
			progressFootView.nodoPlanNRemindTime(false);
			approvalFootView.nodoPlanNRemindTime(false);
		} else {
			progressFootView.nodoPlanNRemindTime(true);
			approvalFootView.nodoPlanNRemindTime(true);
		}
	}

	@Background
	void toRemindTo(MatterInfo mInfo) {
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
	Date rDate;
	
	private void cancelRemind(String todoId){
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("todoId", todoId);
		param.put("onlyOne", "1");
		updateTime(param, "/home/phone/remind/cancelremind",true);
	}

	@UiThread
	void toRemind(final HashMap<String, String> params) {
		ArrayList<PopupItem> items = new ArrayList<PopupItem>();
		items.add(new PopupItem("清除", "clean"));
		DateTimeStyle stype = DateTimeStyle.eRemind;
		DateTimePopup.builder(MatterDetailAcrtivity.this)
				.setDateTimeStyle(stype).setItems(items)
				.setOnMessageListener(new OnMessageListener() {
					@Override
					public void onClick(PopupItem msg) {
						if ("clean".equals(msg.mCode)) {
							cancelRemind(params.get("todoId"));
						}else if("clear".equals(msg.mCode)){
							//取消按钮事件
						}
						else if ("commit".equals(msg.mCode)) {
							if (rDate != null) {
								String timeString = "";
								Date date = new Date();
								if (rDate.getTime() <= date.getTime()) {
									Toast.makeText(MatterDetailAcrtivity.this,
											"提醒时间要大于现在的时间！", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								timeString = AllUtils.getnormalTime(rDate
										.getTime());
								params.put("remindTime", timeString);
								updateTime(params,
										"/home/phone/remind/setremind",false);
							} else {
								Log.d(tag, "rDate ==== nulll");
							}
						}
					}
				}).setOnDateTimeChangeListener(new OnDateTimeChange() {

					@Override
					public void onDateTimeChange(Calendar c, DateTimeStyle style) {
						rDate = c.getTime();
						Log.d(tag,
								"hhhraDate ==== "
										+ (rDate != null ? rDate.getTime()
												: "null"));

					}
				}).show(Gravity.BOTTOM, 0, 0);
	}

	void updateTime(HashMap<String, String> params, String url,final Boolean isDel) {
		new RequestAdpater() {

			@Override
			public void onReponse(ResponseData data) {
				String str = isDel ? "清除" : "设置";
				if (data.getResultState() == ResultState.eSuccess) {
					/*
					 * 刷新提醒时间
					 */
					clean();
					ot.requestMatter(dataType);
					Toast.makeText(MatterDetailAcrtivity.this,
							str + "提醒时间成功！" + data.getMsg(), Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(MatterDetailAcrtivity.this,
							str + "提醒时间失败，请检查网络！" + data.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {
			}
		}.setUrl(url).addParam(params).notifyRequest();
	}

	ArrayList<Fragment> setFragments(MatterInfo mInfo) {
		ArrayList<Fragment> fs = new ArrayList<Fragment>();
		for (int i = 0; i < infos.size(); i++) {
			Fragment f = new Fragment();
			switch (i) {
			case 0:
				if (infos.get(i).class_.getName().equals(
						DetailProgressFragment_.class.getName())) {
					DetailProgressFragment_ pf = new DetailProgressFragment_();
					if (mInfo != null)
						infos.get(i).arg.putSerializable("mInfo", mInfo);
					pf.setArguments(infos.get(i).arg);
					fs.add(pf);
				} else {
					DetailApprovalFragment_ af = new DetailApprovalFragment_();
					if (mInfo != null)
						infos.get(i).arg.putSerializable("mInfo", mInfo);
					af.setArguments(infos.get(i).arg);
					fs.add(af);
				}
				break;
			case 1:
				DetailDiscussFragment df = (DetailDiscussFragment) Fragment.instantiate(this, DetailDiscussFragment_.class.getName(), infos.get(i).arg);;
				df.setOnDelDiscussListener(new OnDelDiscussListener() {
					
					@Override
					public void deldiscuss() {
						dicussNum --;
						discusssChar.setText("评论"+"("+dicussNum+")");
					}

					@Override
					public void adddiscuss() {
						dicussNum ++;
						discusssChar.setText("评论"+"("+dicussNum+")");
					}
				});
				fs.add(df);
				break;
			case 2:
				DetailTrendFragment tf = new DetailTrendFragment_();
				tf.setArguments(infos.get(i).arg);
				fs.add(tf);
				break;
			default:
				break;

			}

		}
		return fs;
	}

	@Override
	public void progressClick() {
		DiscussProgressActivity_.intent(MatterDetailAcrtivity.this)
				.dataid(dataId).arrangeType(arrangePublicOrSecretTypes).start();
	}

	@Override
	public void approvalClick(PopupItem msg) {
		DetailApprovalFragment.getInstance().approvalClick(msg,ot.mInfo.getIsFixedProcess());
	}

	@Override
	public void approvalClickWithOutItems() {
	}

	@Override
	public void planClick() {
		ArrangementPlan_.intent(MatterDetailAcrtivity.this)
				.clientcode(ot.mInfo.getClientName()).dataid(dataId)
				.todoid(ot.mInfo.getTodoId())
				.beginTimeStr(AllUtils.getnormalTime(ot.mInfo.getPlanTime()))
				.endTimeStr(AllUtils.getnormalTime(ot.mInfo.getPlanTimeEnd()))
				.start();
	}

	@Override
	public void remindClick() {
		if (AllUtils.arrangeType.equals(dataType)) {
			toRemindTo(ot.mInfo);
		} else if (AllUtils.approvalType.equals(dataType)) {
			toRemindTo(ot.mInfo);
		}
	}

	@Override
	public void sendClick(String content) {
		DetailDiscussFragment.getInstance().sendDiscuss(content);
	}

	@Override
	public void addClick() {
	}

	@Override
	public void sendDiscussDiscussClick(int position, String content) {
		DetailDiscussFragment.getInstance().sendDiscussDiscuss(position,
				content);
	}

	public void iWasComplete() {
		isComplete = true;
	}

	@OnActivityResult(FramMainActivity.COMPLETE)
	void onResultca(int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			refreshthis();
			break;
		default:
			break;
		}
	}
	
	@OnActivityResult(PROCESS_NEXT)
	void processNextReuslt(int resultCode,Intent data){
		if(resultCode != RESULT_OK)
			return;
		ArrayList<SelectPersonInfo> infoList = data.getParcelableArrayListExtra("process");
		int step = data.getIntExtra("step", -1);
		if(infoList.size() == 0)
			return;
		JSONArray nextJsonArray= new JSONArray();
		for(SelectPersonInfo info : infoList){
			JSONObject json = new JSONObject();
			try {
				json.put("name",info.mName);
				json.put("value",info.mCode);
				nextJsonArray.put(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
		}
		String url_next = "/home/phone/thing/selectnextapprover";
        HashMap<String,String> paramMap_next = new HashMap<String, String>();
        paramMap_next.put("dataId",dataId);
        paramMap_next.put("todoUserSet",nextJsonArray.toString());
        paramMap_next.put("step", step+"");
        Utils.requestUsual(paramMap_next,url_next,new Utils.CallBackListener() {
            @Override
            public void callBack(ResponseData data) {
                if (data.getResultState().equals(ResponseData.ResultState.eSuccess)){
                    //finish();
                    if(MatterDetailAcrtivity.getInstance()!=null){
                    	MatterDetailAcrtivity.getInstance().refreshthis();
                    }

                }
            }

            @Override
            public void callBackJson(JSONArray jsonArray) {

            }

			@Override
			public void callbackNull() {
			}
        });
	}
	
	@OnActivityResult(PROCESS_CHANGE)
	void onProcessChangeResult(int resultCode,Intent data){
		if(RESULT_OK == resultCode){
			ArrayList<SelectPersonInfo> infoList = data.getParcelableArrayListExtra("process");
			int step = data.getIntExtra("step", -1);
			if(infoList.size() == 0)
				return;
			JSONArray changeJsonArray = new JSONArray();
			for(SelectPersonInfo info : infoList){
				JSONObject json = new JSONObject();
				try {
					json.put("name", info.mName);
					json.put("value", info.mCode);
					changeJsonArray.put(json);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			String url_change = "/home/phone/thing/changeapprover";
            HashMap<String,String> paramMap_change = new HashMap<String, String>();
            paramMap_change.put("dataId",dataId);
            paramMap_change.put("todoUserSet",changeJsonArray.toString());
            paramMap_change.put("step", step+"");
            Log.d(tag, "onProcessChangeResult param:"+paramMap_change);
            Utils.requestUsual(paramMap_change,url_change,new Utils.CallBackListener() {
                @Override
                public void callBack(ResponseData data) {
                    if (data.getResultState().equals(ResponseData.ResultState.eSuccess)){
                    	Log.d(tag, "requestUsual success!!");
                    	refreshthis();
                    }else{
                        	Log.d(tag, "requestUsual : error"+data.getCode() + data.getMsg());
                    }
                }

                @Override
                public void callBackJson(JSONArray jsonArray) {

                }

				@Override
				public void callbackNull() {
				}
            });
		
		}
	}
	
	@OnActivityResult(SUBTASKREQUEST)
	void onResultTask(int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			
			refreshthis();
			 if(MatterDetailAcrtivity.getInstance()!=null){
             	MatterDetailAcrtivity.getInstance().refreshthis();
             }
			break;
		default:
			break;
		}
	}

	@OnActivityResult(Utils.APPROVAL_USER_CHANGE)
	void onResultC(int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			ArrayList<ContactViewShow> changeDatas = data.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
            JSONArray changeJsonArray;
            if (changeDatas == null){
                return;
            }
            changeJsonArray = new JSONArray();

            for (int i = 0; i < changeDatas.size(); i++){
                try {

                    JSONObject changePerJson = new JSONObject();
                    String userName_change = changeDatas.get(i).getName();
                    String userCode_change = changeDatas.get(i).getCode();
                    changePerJson.put("value",userCode_change);
                    changePerJson.put("name",userName_change);
                    changeJsonArray.put(changePerJson);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            String url_change = "/home/phone/thing/changeapprover";
            HashMap<String,String> paramMap_change = new HashMap<String, String>();
            paramMap_change.put("dataId",dataId);
            paramMap_change.put("todoUserSet",changeJsonArray.toString());
            Utils.requestUsual(paramMap_change,url_change,new Utils.CallBackListener() {
                @Override
                public void callBack(ResponseData data) {
                    if (data.getResultState().equals(ResponseData.ResultState.eSuccess)){
                        if(MatterDetailAcrtivity.getInstance()!=null){
                        	MatterDetailAcrtivity.getInstance().refreshthis();
                        }
                    }
                }

                @Override
                public void callBackJson(JSONArray jsonArray) {

                }

				@Override
				public void callbackNull() {
				}
            });
		}
	}
	
	@OnActivityResult(Utils.APPROVAL_USER_NEXT)
	void onResultN(int resultCode,Intent data){
		if(resultCode == RESULT_OK){
			//下一审批人

            ArrayList<ContactViewShow> nextDatas = data.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
            JSONArray nextJsonArray = new JSONArray();
            for (int i = 0; i < nextDatas.size(); i++){
                JSONObject nextPerJson = new JSONObject();
                String userName_next = nextDatas.get(i).getName();
                String userCode_next = nextDatas.get(i).getCode();
                try {
                    nextPerJson.put("value", userCode_next);
                    nextPerJson.put("name",userName_next);
                }catch (Exception e){

                }
                nextJsonArray.put(nextPerJson);
            }
            String url_next = "/home/phone/thing/selectnextapprover";
            HashMap<String,String> paramMap_next = new HashMap<String, String>();
            paramMap_next.put("dataId",dataId);
            paramMap_next.put("todoUserSet",nextJsonArray.toString());
            Utils.requestUsual(paramMap_next,url_next,new Utils.CallBackListener() {
                @Override
                public void callBack(ResponseData data) {
                    if (data.getResultState().equals(ResponseData.ResultState.eSuccess)){
                        //finish();
                        if(MatterDetailAcrtivity.getInstance()!=null){
                        	MatterDetailAcrtivity.getInstance().refreshthis();
                        }

                    }
                }

                @Override
                public void callBackJson(JSONArray jsonArray) {

                }

				@Override
				public void callbackNull() {
				}
            });
		}
	}
	
	@OnActivityResult(Utils.CCAL_USER_ADD)
	void onResultA(int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			 ArrayList<ContactViewShow> copyDatas = data.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);

             if (copyDatas == null) {
                 return;
             }
                final  JSONArray jsonArray = new JSONArray();
                 ContactUtil.setSelectPeopleData(copyDatas, new ContactUtil.PeopleCallbackListener() {
                     @Override
                     public void callBack(ArrayList<SamUser> data, String user) {
                         for (SamUser s : data) {
                             JSONObject jPerson = new JSONObject();
                             String codeAdd = s.getmCode();
                             String nameAdd = s.getmName();
                             try {
                                 jPerson.put("value", codeAdd);
                                 jPerson.put("name", nameAdd);
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                             jsonArray.put(jPerson);
                         }
                     }
                 });

             HashMap<String,String> paramMap_ccadd = new HashMap<String, String>();
             paramMap_ccadd.put("copyUsersSet",jsonArray.toString());
             paramMap_ccadd.put("dataId",dataId);
             String url = "/home/phone/thing/addcopyusers";
             Utils.requestUsual(paramMap_ccadd,url,new Utils.CallBackListener() {
                 @Override
                 public void callBack(ResponseData data) {
                     //finish();
                     if(MatterDetailAcrtivity.getInstance()!=null){
                     	MatterDetailAcrtivity.getInstance().refreshthis();
                     }
                 }

                 @Override
                 public void callBackJson(JSONArray jsonArray) {

                 }

					@Override
					public void callbackNull() {
					}
             });
		}
	}
	
	@OnActivityResult(0)
	void onResult(int resultCode, Intent data) {
		if (resultCode == 100) {
			refreshthis();
		}
	}
	
	@OnActivityResult(MATTEDITOR)
	void onEditorResult(int resultCode,Intent data){
		if(resultCode == RESULT_OK){
			refreshthis();
		}
	}
	
	void guanzhu(final Boolean guanzhu){
    	String url = "/home/pc/thing/setobserve";
    	HashMap<String, String> param = new HashMap<String, String>();
    	param.put("dataId", dataId);
    	
    	if("1".equals(dataType))
    		param.put("observeType", "任务");
    	else
    		param.put("observeType", "审批");
    	
    	if(guanzhu){
    		
    		param.put("isObserve", "1");
    	}
    	else{
    		
    		param.put("isObserve", "0");
    	}
    	
    	
    	new RequestAdpater(){

			@Override
			public void onReponse(ResponseData data) {
				
				if(data.getResultState() == ResultState.eSuccess){
					refreshthis();
				}else{
					Toast.makeText(MatterDetailAcrtivity.this, "切换失败", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
    		
    	}.setUrl(url)
    	.addParam(param)
    	.notifyRequest();
    }
	
	private void onApproveTermination(){
		StopApproveActivity_.intent(this)
		.dataId(dataId)
		.startForResult(FramMainActivity.COMPLETE);
	}
	
	private void onTermination(){
		if (ot.mInfo.getArrangeType().equalsIgnoreCase("1")) {
            Intent intent = new Intent(instance, ArrangementFinish.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", "终止");
            bundle.putString("dataId", dataId);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }else {
            new AlertDialog.Builder(instance)
                    .setTitle("您确定终止吗？")
                    .setNegativeButton("终止",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String url = "/home/phone/thing/stoparrange";
                            new RequestAdpater() {
                                @Override
                                public void onReponse(ResponseData data) {
                                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {//成功响应
                                        boolean needRefresh = MatterDetailAcrtivity.instance != null;
                                        if (needRefresh) {
                                        	MatterDetailAcrtivity.instance.refreshthis();
                                        }
                                    } else {
                                    }
                                }

                                @Override
                                public void onProgress(ProgressMessage msg) {
                                }
                            }.setUrl(url)
                                    .addParam("dataId", dataId)
                                    .addParam("onlyOne","1")
                                    .notifyRequest();
                        }
                    })
                    .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
	}
	
	private void onDoFinish(){
		if (ot.mInfo.getDataType().equals("1") &&
                ot.mInfo.getArrangeType().equalsIgnoreCase("1")) {
            Intent intent = new Intent(instance, ArrangementFinish.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", "办结");
            bundle.putString("dataId", dataId);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        } else {
            String title = "";
             String url = "";
            if ("1".equalsIgnoreCase(ot.mInfo.getDataType())&&ot.mInfo.getArrangeType().equalsIgnoreCase("0")){
                title = "您确定办结该任务？";
                url = "/home/phone/thing/finisharrange";
            }else{
                title = "您确定办结该审批？";
                url = "/home/phone/thing/finishapprove";

            }
            final String urlFinsh = url;
            new AlertDialog.Builder(instance)
                    .setTitle(title)
                    .setNegativeButton("办结", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            final String urlF = urlFinsh;
                            new RequestAdpater() {
                                @Override
                                public void onReponse(ResponseData data) {
                                	refreshthis();
                                }

                                @Override
                                public void onProgress(ProgressMessage msg) {
                                }
                            }.setUrl(urlF)
                                    .addParam("dataId", dataId)
                                    .notifyRequest();
                        }
                    })
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
	}
	
	private void selectProcessApproval(int requestCode){
		ArrayList<SelectPersonInfo> infoList = new ArrayList<SelectPersonInfo>();
		 if (pInfos.size() > 0) {
	            for (TodoPersonInf s : pInfos) {
	            	SelectPersonInfo info = new SelectPersonInfo(s.mName, s.mCode);
	            	String status = s.status;
	            	if("1".equals(status) || "2".equals(status)){
	            		info.checkEnable = false;
	            	}else{
	            		info.checkEnable = true;
	            	}
	            	Log.d(tag, "selectProcessApproval info:"+info);
	            	infoList.add(info);
	            }
	        }
		ApproveFixedGroupActivity_.intent(this)
		.dataId(dataId)
		.mSelectPersonList(infoList)
		.startForResult(requestCode);
	}
	
	//下一审批人选择
    private  void selectApproval(final int id,final String selectId){
        final Intent intent = new Intent();
        intent.setClass(instance, SelectContacter.class);
        Bundle bundle = new Bundle();
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> code = new ArrayList<String>();
        if (pInfos.size() > 0) {
            for (TodoPersonInf s : pInfos) {
                name.add(s.mName);
                code.add(s.mCode);
            }
            bundle.putStringArrayList("name",name);
            bundle.putStringArrayList("code",code);
           
        }
        bundle.putString(SelectContacter.how, selectId);
        intent.putExtra("bundle",bundle);
       
                startActivityForResult(intent,id);
           
    }

    private void addCopyPeople(){
    	progressDialog.show();
    	String url = "/home/phone/thing/getinvolvepeople";
    	String id = dataId;
    	new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				Intent intent = new Intent(instance, SelectContacter.class);
		        Bundle bundle = new Bundle();
		        bundle.putString(SelectContacter.how, SelectContacter.COPY);
		        intent.putExtra("bundle",bundle);
				progressDialog.dismiss();
				Log.d(tag, "addCopyPeople response:"+data.getCode()+data.getMsg()+data.getJsonArray()+data.getJsonObject()
						+data.getResultState());
				if(data.getResultState() == ResultState.eSuccess){
					JSONObject result = data.getJsonObject();
					JSONArray copyUserJsonArray = result.optJSONArray("copyUsers");
					ArrayList<String> userCodes = new ArrayList<String>();
					if(copyUserJsonArray != null && copyUserJsonArray.length() > 0){
						for(int i = 0; i < copyUserJsonArray.length(); i++){
							JSONObject copyUser = copyUserJsonArray.optJSONObject(i);
							String userCode = copyUser.optString("userCode");
							userCodes.add(userCode);
						}
					}
					bundle.putStringArrayList("extraUserCodes", userCodes);
					startActivityForResult(intent,Utils.CCAL_USER_ADD);
				}else{
					
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("dataId",id)
		.notifyRequest();
    }
    
    
    private void onPersonnel(){
    	Intent intent = new Intent(instance, ArrangementPersonnel.class);
        Bundle bundle = new Bundle();
        bundle.putString("dataId", dataId);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
    }
    
    private void onEditor(){
    	 if (ot.mInfo.getDataType().equals("1")) {
             Intent intent = new Intent(instance, MatterEditor.class);
             Bundle bundle = new Bundle();
             bundle.putString("id", ot.mInfo.getId());
             if (ot.mInfo.getArrangeType().equals("1")) {
                 bundle.putString("matterType", "1");
             } else {
                 bundle.putString("matterType", "0");
             }
             bundle.putString("dataType", ot.mInfo.getDataType());
             bundle.putString("editType", "02");
             intent.putExtra("bundle", bundle);
             startActivityForResult(intent,MATTEDITOR);
             overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);


         } else {
             Intent intent = new Intent(instance, MatterEditor.class);
             Bundle bundle = new Bundle();
             intent.putExtra("process", ot.mInfo.getIsFixedProcess() ? 
            		 ApprovalProcessTypeView.FIXED : ApprovalProcessTypeView.FREE);
             bundle.putString("id", ot.mInfo.getId());
             bundle.putString("dataType", ot.mInfo.getDataType());
             bundle.putString("editType", "02");
             intent.putExtra("bundle", bundle);
             startActivityForResult(intent,MATTEDITOR);
             overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
         }
    }
    
    
    private void onDelete(){
    	final Toast delToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    	String title = "确认删除这个审批？";
        final String urlAppro = "/home/phone/thing/deleteapprove";
        final String urlArran = "/home/phone/thing/deletearrange";
        if (ot.mInfo.getDataType().equals("1")) {
            title = "确认删除这个任务？";
        }
        new AlertDialog.Builder(instance)
                .setTitle(title)
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        new RequestAdpater() {
                            @Override
                            public void onReponse(ResponseData data) {
                                JSONObject jsonObject = data.getMRootData();
                                Boolean succeed = jsonObject.optBoolean("data");
                                if (succeed) {
                                    delToast.setText("你的事项已删除");
                                    delToast.show();
                                    iWasComplete();
                                    finish();
                                } else {
                                	delToast.setText("删除失败"+data.getMsg());
                                	delToast.show();
                                }
                            }

                            @Override
                            public void onProgress(ProgressMessage msg) {
                            }
                        }.setUrl("1".equals(ot.mInfo.getDataType()) ? urlArran : urlAppro)
                                .addParam("dataId", dataId)
                                .addParam("onlyOne", "1")
                                .notifyRequest();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })
                .show();
    }
    
    private void onAttchment(){
    	Intent intent = new Intent(instance, AccessoryListActivity.class);
        intent.putExtra("png", ot.pngInfos);
        intent.putExtra("file", ot.fileInfos);
        System.out.println(ot.pngInfos.size()+"===="+ot.fileInfos.size());
        Bundle bundle = new Bundle();
        bundle.putString("dataId", dataId);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
    }
    
    private void onApprovalProcess(){
    	ApprovalProcessListActivity_.intent(this)
    	.dataId(dataId)
    	.start();
    	overridePendingTransition(R.anim.push_down_in, R.anim.push_top_out);
    }
	@Override
	public void onScroll(int scrollY) {
		int holdTop = Math.max(scrollY, manualTab.getTop());
		topTab.layout(0, holdTop, topTab.getWidth(),
				holdTop + topTab.getHeight());
	}
	
	private void setTitle(String title){
//		if(title != null){
//		if(title.trim().length() > 5){
//			title = title.substring(0, 5)+"...";
//		}
		this.title.setText(title);
//		}

	}

	@Override
	public void finish() {
		if(isPushMessage){
			Intent i = new Intent(MatterDetailAcrtivity.this, FramMainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if(isComplete)
			i.putExtra("data", FramMainActivity.COMPLETE);
		startActivity(i);
		
		}else if(isComplete){
			setResult(FramMainActivity.COMPLETE);
		}else{
			setResult(Activity.RESULT_OK);
		}
			MyApplication.getInstance().removeActivity(this);
	  super.finish();
	}
	
	
	
}
