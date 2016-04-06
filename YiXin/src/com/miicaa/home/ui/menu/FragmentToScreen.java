package com.miicaa.home.ui.menu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.BaseKeyVaule;
import com.miicaa.home.R;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.menu.BaseScreenView.OnBaseScreenClickLinstener;
import com.miicaa.home.ui.menu.Screenview.ScreenClickLinstener;
import com.miicaa.home.ui.org.DateTimePopup;
import com.miicaa.home.ui.org.DateTimePopup.DateTimeStyle;
import com.miicaa.home.ui.org.DateTimePopup.OnDateTimeChange;
import com.miicaa.home.ui.org.LableGroup;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.ViewHolder;
/**
 * @author LM
 *
 */
public class FragmentToScreen {
	static String TAG = "FragmentToScreen";
	Context mContext;
	View rootView; 
	Date date;
	ScreenType screentype;
	Screenview screenArrange;
	Screenview screenApproval;
	Screenview screenPublic;
	Screenview screenSecret;
	Screenview screenReport;
	ScrrenViewGroup reportGroup;
	TextView screenWho;
	TextView screenEditor;
	TextView screenBegin;
	TextView screenEnd;
	Button complete;
	Button clearType;
	//GridView viewgroup;
	LableGroup viewGroup;
	//GridAdapter adapter;
	ArrayList<ScreenApproveType> sctype;
	ArrayList<SelectPersonInfo> peopleInfos;
	ArrayList<SelectPersonInfo> tmpPeopleInfos;
	ArrayList<SelectPersonInfo> tmpEditorInfos;
	ArrayList<SelectPersonInfo> editorInfos;
	public static FragmentToScreen instance;
	public final static String ALL = "";
	public final static String APPROVAL ="approval";
	public final static String ARRANGE = "arrange";
	public final static String REPORT = "report";
	public final static int SELECTPEOPLE = 0x11;
	public final static int SELECTEDITOR = 0x12;
	OnSrcCodeChange codeChange;
	
	
public FragmentToScreen(Context context) {
	mContext = context;
	instance = this;
	//adapter = new GridAdapter(context);
	peopleInfos = new ArrayList<SelectPersonInfo>();
	editorInfos = new ArrayList<SelectPersonInfo>();
	tmpPeopleInfos = new ArrayList<SelectPersonInfo>();
	tmpEditorInfos = new ArrayList<SelectPersonInfo>();
	sctype = new ArrayList<ScreenApproveType>();
	screentype = ScreenType.getInstance();
	rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_to_screen, 
			null); 
	initViews();
	}



public static FragmentToScreen getInstance(){
	return instance;
}
void initViews(){
	try{
		
   reportGroup = (ScrrenViewGroup)rootView.findViewById(R.id.report_group);
   List<BaseKeyVaule> reportKV = new ArrayList<BaseKeyVaule>();
   reportKV.add(new BaseKeyVaule("日报", "10"));
   reportKV.add(new BaseKeyVaule("周报", "20"));
   reportKV.add(new BaseKeyVaule("月报", "30"));
   reportKV.add(new BaseKeyVaule("自定义报告", "40"));
   reportGroup.addKeyValueList(reportKV);
   reportGroup.refresh(new OnBaseScreenClickLinstener() {
	
	@Override
	public <T extends BaseKeyVaule> void sreenCancle(T type) {
		screentype.removeReportType(type);
	}
	
	@Override
	public <T extends BaseKeyVaule> void screenClick(T type) {
		screentype.addReportType(type);
	}
});
 
  screenReport = (Screenview)rootView.findViewById(R.id.screen_to_report);
 screenReport.setContent("工作报告");
 screenReport.setScreenClickLinstener(new ScreenClickLinstener() {
	
	@Override
	public void sreenCancle() {
		screentype.cancleSrcCode(REPORT);
	}
	
	@Override
	public void screenClick() {
		screentype.addSrcCode(REPORT);
	}
});
 screenArrange = (Screenview)rootView.findViewById(R.id.screen_to_arrange);
 screenArrange.setContent("任务");
 screenArrange.setScreenClickLinstener(new ScreenClickLinstener() {
	
	@Override
	public void sreenCancle() {
		screentype.cancleSrcCode(ARRANGE);
	}
	
	@Override
	public void screenClick() {
		screentype.addSrcCode(ARRANGE);
	}
});

 screenApproval = (Screenview)rootView.findViewById(R.id.screen_to_approval);
 screenApproval.setContent("审批");
 screenApproval.setScreenClickLinstener(new ScreenClickLinstener() {
	
	@Override
	public void sreenCancle() {
		screentype.cancleSrcCode(APPROVAL);
	}
	
	@Override
	public void screenClick() {
		screentype.addSrcCode(APPROVAL);
	}
});

 screenPublic = (Screenview)rootView.findViewById(R.id.screen_to_public);
 screenPublic.setContent("公事");
 screenPublic.setScreenClickLinstener(new ScreenClickLinstener() {
	
	@Override
	public void sreenCancle() {
		screentype.cancleArrangeCode("1");
	}
	
	@Override
	public void screenClick() {
		screentype.addArrangeCode("1");
	}
});

 screenSecret = (Screenview)rootView.findViewById(R.id.screen_to_secret);
 screenSecret.setContent("私事");
 screenSecret.setScreenClickLinstener(new ScreenClickLinstener() {
	
	@Override
	public void sreenCancle() {
		screentype.cancleArrangeCode("0");
	}
	
	@Override
	public void screenClick() {
		screentype.addArrangeCode("0");
	}
});

 screenWho = (TextView)rootView.findViewById(R.id.screen_edit_to_who);
 //跳至选择人员界面
 screenWho.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View view) {
		toPeople(SELECTPEOPLE);
	}
});
 
 screenEditor = (TextView)rootView.findViewById(R.id.screenEditeditor);
 screenEditor.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		toPeople(SELECTEDITOR);
	}
});
 
 screenBegin = (TextView)rootView.findViewById(R.id.screen_edit_to_begintime);
 screenBegin.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		setScreenTime(ModeTime.begin);
	}
});
 viewGroup = (LableGroup) rootView.findViewById(R.id.approval_group);
 //viewgroup = (GridView)rootView.findViewById(R.id.approval);
// viewgroup.setAdapter(adapter);
 screenEnd =(TextView)rootView.findViewById(R.id.screen_edit_to_endtime) ;
 screenEnd.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		setScreenTime(ModeTime.end);
	}
});
 
 complete = (Button)rootView.findViewById(R.id.complete);
 
 
 complete.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		peopleInfos.clear();
		peopleInfos.addAll(tmpPeopleInfos);
		editorInfos.clear();
		editorInfos.addAll(tmpEditorInfos);
		screentype.saveScreenCondition();
		codeChange.setSrcCode(screentype);
	}
});
 
 rootView.findViewById(R.id.clearType).setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		screentype.removeAllTmpTypes();
		tmpPeopleInfos.clear();
		tmpEditorInfos.clear();
		clearTmpType(screenApproval);
		clearTmpType(screenArrange);
		clearTmpType(screenPublic);
		clearTmpType(screenSecret);
		clearTmpType(screenReport);
		reportGroup.refreshViewChange(screentype.getTmpReportList());
		clearTmpAppTypes();
		screenWho.setText("");
		screenEditor.setText("");
		screenBegin.setText("");
		screenEnd.setText("");
//		oldWhat();
	}
});
 
	}catch(NotFoundException e){
		e.printStackTrace();
	}
}

public void oldWhat(){
	ArrayList<String> srcList = screentype.getSrcList();
	ArrayList<String> arrangeList = screentype.getArrangeList();
	oldTo(srcList, ARRANGE, screenArrange);
	oldTo(srcList, APPROVAL, screenApproval);
	oldTo(arrangeList, "1", screenPublic);
	oldTo(arrangeList, "0", screenSecret);
	oldTo(srcList,REPORT,screenReport);
	reportGroup.refreshViewChange(screentype.getTmpReportList());
	screenWho.setText(oldToPeople(screentype.getCreateUserStr()));
	screenEditor.setText(oldToPeople(screentype.getTodoUserStr()));
	screenBegin.setText(screentype.getBeginTime() == null ? "" :screentype.getBeginTime());
	screenEnd.setText(screentype.getEndTime() == null ?"":screentype.getEndTime());
	refreshApprType();
}

public void covertTmp(){
	tmpPeopleInfos.clear();
	tmpPeopleInfos.addAll(peopleInfos);
	tmpEditorInfos.clear();
	tmpEditorInfos.addAll(editorInfos);
	screentype.convertTmpCondition();
}

void oldTo(ArrayList<String> sList,String type,Screenview view){
	if(sList.contains(type))
		view.setIsSelect(true);
	else
		view.setIsSelect(false);
}

private void clearTmpType(Screenview view){
	view.setIsSelect(false);
}


private void clearTmpAppTypes(){
	for(int i = 0 ; i < viewGroup.getChildCount(); i++){
		View v =  viewGroup.getChildAt(i);
		Screenview screenview = (Screenview) v.findViewById(R.id.apprscreen);
		screenview.setIsSelect(false);
	}
}

public void addApprScreen(final ScreenApproveType type){
	sctype.add(type);
	
}

public void clearApprScreen(){
	if(sctype != null)
	sctype.clear();
}

void toPeople(int requestCode){
	Intent intent = new Intent(mContext, SelectContacter.class);
	Bundle bundle = null;
	if(requestCode == SELECTPEOPLE){
		 if(screentype.getTmpCreateUser().size() == 0){
			 tmpPeopleInfos.clear();
		 }
		 bundle = oldPeopleSend(tmpPeopleInfos);
		 bundle.putString("how", SelectContacter.STARTROUND);
	}
	else if(requestCode == SELECTEDITOR){
		if(screentype.getTmpTodoUser().size() == 0){
			tmpEditorInfos.clear();
		}
		bundle = oldPeopleSend(tmpEditorInfos);
		 bundle.putString("how", SelectContacter.ARRANGE);
	}
	intent.putExtra("bundle", bundle);
    ((Activity) mContext).startActivityForResult(intent,requestCode);
}

Bundle oldPeopleSend(ArrayList<SelectPersonInfo> info){
	Bundle bundle = new Bundle();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> code = new ArrayList<String>();
    Log.d(TAG, "oldPeopleSend info:"+info.size());
	 if (info.size() > 0) {
	        for (SelectPersonInfo s : info) {
	            name.add(s.mName);
	            code.add(s.mCode);
	        }
	        bundle.putStringArrayList("name",name);
	        bundle.putStringArrayList("code",code);
	        Log.d(TAG, "oldPeopleSend name:"+name+"code:"+code);

	    }
	 return bundle;
}

public void refreshApprType(){
	//adapter.refresh(sctype);
	initLabel(sctype);
}


public View getScreenView(){
	return rootView != null ?rootView:null;
}


/*
 * 设置筛选时间
 */
void setScreenTime(final ModeTime mode){
	 date = null;
     ArrayList<PopupItem> items = new ArrayList<PopupItem>();
     items.add(new PopupItem("清空", "clear"));
     DateTimePopup.DateTimeStyle stype = DateTimePopup.DateTimeStyle.eDate;
     DateTimePopup.builder(mContext)
     .setDateTimeStyle(stype)
     .setItems(items)
     .setOnMessageListener(new OnMessageListener() {
		@Override
		public void onClick(PopupItem msg) {
			if("clear".equals(msg.mCode)){
				date = null;
			}else if("commit".equals(msg.mCode)){
				if(date == null ){
					date = new Date(System.currentTimeMillis());
				}
			}
			getTime(mode, date);
		}
	})
	.setOnDateTimeChangeListener(new OnDateTimeChange() {
		
		@Override
		public void onDateTimeChange(Calendar c, DateTimeStyle style) {
			date = c.getTime();
			getTime(mode, date);
		}
	}).show(Gravity.BOTTOM, 0, 0);
}


void getTime(ModeTime mode,Date date){
	String time = date !=null?AllUtils.getYearTime(date.getTime()):"";
	if(mode == ModeTime.begin){
		if(date == null){
			time = "";
		}
		screenBegin.setText(time);
		screentype.setbeginTime(time == ""?null:time);
	}else {
		if(date == null){
			time = "";
		}
		screenEnd.setText(time);
		screentype.setendTime(time == ""?null:time);
	}
	
}

enum ModeTime{
	begin,
	end;
}

public interface OnSrcCodeChange{
	void setSrcCode(ScreenType type);
}

public void setOnSrcCodeChange(OnSrcCodeChange change){
	this.codeChange = change;
}

private void initLabel(ArrayList<ScreenApproveType> tmp){
	if(tmp==null && tmp.size()<1)
		return;
	
	ArrayList<String> approvalList = new ArrayList<String>();
	ArrayList<ScreenApproveType> text = new ArrayList<ScreenApproveType>();
	approvalList = screentype.getApprovalList();
	text.addAll(tmp);
	viewGroup.removeAllViews();
	for(final ScreenApproveType item:text){
		
		View convertview = LayoutInflater.from(mContext).inflate(R.layout.screen_grid,null);
			
		Screenview sc = ViewHolder.get(convertview, R.id.apprscreen);
		sc.setContent(item.type);
		sc.setScreenClickLinstener(new ScreenClickLinstener() {
			
			@Override
			public void sreenCancle() {
				screentype.cancleApprovalCode(item.type);
			}
			
			@Override
			public void screenClick() {
				screentype.addApprovalCode(item.type);
				
			}
		});
		oldTo(approvalList, item.type, sc);
		viewGroup.addView(convertview);
	}
}

class GridAdapter extends BaseAdapter{

	Context context;
	ArrayList<String> approvalList;
	ArrayList<ScreenApproveType> text;
	public  GridAdapter(Context context) {
		this.context = context;
		this.text = new ArrayList<ScreenApproveType>();
		approvalList = new ArrayList<String>();
	}
	public void refresh(ArrayList<ScreenApproveType> text){
		this.text.clear();
		this.text.addAll(text);
		approvalList = screentype.getApprovalList();
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return text.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup viewgroup) {
		if(convertview == null){
			convertview = LayoutInflater.from(context).inflate(R.layout.screen_grid,null);
			
		}
		Screenview sc = ViewHolder.get(convertview, R.id.apprscreen);
		sc.setContent(text.get(position).type);
		sc.setScreenClickLinstener(new ScreenClickLinstener() {
			
			@Override
			public void sreenCancle() {
				screentype.cancleApprovalCode(text.get(position).type);
			}
			
			@Override
			public void screenClick() {
				screentype.addApprovalCode(text.get(position).type);
				
			}
		});
		oldTo(approvalList, text.get(position).type, sc);
		return convertview;
	}
	
}

/*
 * 显示人名
 */
public SelectStrPeople setScreenName(ArrayList<SelectPersonInfo> infos,ArrayList<SelectPersonInfo> sname){
	assert infos != null;
	infos.clear();
	infos.addAll(sname);
	ArrayList<String> codes = new ArrayList<String>();
	ArrayList<String> strs = new ArrayList<String>();
	for(SelectPersonInfo n : sname){
    codes.add(n.mCode);
	strs.add(n.mName);
	}
	String name = AllUtils.listToString(strs);
	SelectStrPeople people = new SelectStrPeople();
	people.names =  name;
	people.codes = codes;
	return people;
}

SelectStrPeople createPeople = new SelectStrPeople();
SelectStrPeople todoPeople = new SelectStrPeople();
public void setCreatorName(ArrayList<SelectPersonInfo> name){
	assert name != null;
	SelectStrPeople people = setScreenName(tmpPeopleInfos, name);
	screentype.setcreateUser(people.codes,people.names);
	screenWho.setText(people.names);
}
public void setEditorName(ArrayList<SelectPersonInfo> name){
	assert name != null;
	SelectStrPeople people = setScreenName(tmpEditorInfos,name);
	screentype.settodoUser(people.codes,people.names);
	screenEditor.setText(people.names);
}

private String oldToPeople(String types){
	   return types != null ? types :"";
}

private void oldToTodoPeople(ArrayList<SelectPersonInfo> peoples,ArrayList<String> codes){
	for(SelectPersonInfo p : peoples){
		if(!codes.contains(p.mCode))
			peoples.remove(p);
	}
	setEditorName(peoples);
}

class SelectStrPeople{
	public ArrayList<String> codes;
	public String names;
}

public void onRestart(){
	screentype = ScreenType.getInstance();
}

}
