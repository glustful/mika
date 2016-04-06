package com.miicaa.detail;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.common.base.Tools;
import com.miicaa.detail.DiscussFootView.OnDiscussClickListener;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.org.MatterHttp;
import com.miicaa.home.ui.org.MatterHttp.OnMatterResult;
import com.miicaa.utils.AllUtils;
import com.yxst.epic.yixin.view.ResizeLayout;
import com.yxst.epic.yixin.view.ResizeLayout.OnResizeListener;

@SuppressWarnings("deprecation")
@EActivity(R.layout.detail_progress_discuss)
public class ProgressDiscussActivity extends Activity  implements OnDiscussClickListener,OnDiscussButtonClickListener{
	Integer pageNum = 0;
	Integer pageSize = 10;
	DetailDiscussAdapter adapter;
	ArrayList<DetailContentInfo> contentInfo;
	ArrayList<String> codes;
@ViewById
ResizeLayout wholeLayout;
//@ViewById(R.id.head_img)
//ImageView head;
//@ViewById(R.id.head_name)
//TextView name;
//@ViewById(R.id.content)
//TextView content;
//@ViewById(R.id.file)
//LinearLayout file;
//@ViewById(R.id.filename)
//TextView fileName;
//@ViewById(R.id.img)
//LinearLayout image;
//@ViewById(R.id.imgname)
//TextView imageName;
//@ViewById(R.id.discussCount)
//TextView discussCount;
//@ViewById(R.id.time)
//TextView time;
//@ViewById(R.id.complete)
//TextView complete;
//@ViewById(R.id.from)
//TextView form;
//@ViewById(R.id.progressTalk)
//ImageButton talk;
@ViewById(R.id.discussList)
PullToRefreshListView  listview;
//@ViewById(R.id.talknum)
//TextView talknum;
@ViewById(R.id.discussFoot)
DiscussFootView footview;
//@ViewById(R.id.writeCancle)
//Button cancle;
//@Extra
//String dataId;
@Extra
DetailList detailList;
//String dataId;
//DetailList detailList;

@AfterInject
void aferInject(){
	contentInfo = new ArrayList<DetailContentInfo>();
	codes = new ArrayList<String>();
	adapter = new DetailDiscussAdapter(ProgressDiscussActivity.this,
			AllUtils.discuss, DetailDiscussFragment.setExpressIcon(ProgressDiscussActivity.this,null));
	adapter.setOnDiscussButtonClickListener(this);
	
	requestdiscuss();
	
}

@AfterViews
void afterView(){
//	Intent intent = getIntent();
//	detailList = (DetailList) intent.getSerializableExtra("detailList");
//	dataId = intent.getStringExtra("dataId");
	adapter.setIsInTop(true, detailList);
	setDiscussNum(0);
	wholeLayout.setOnResizeListener(new OnResizeListener() {
		
		@Override
		public void OnResize(int w, int h, int oldw, int oldh) {
			// TODO Auto-generated method stub
			Boolean b = h < oldh;
			footview.setIsHidden(b);
		}
	});
	footview.setOnDiscussClickListener(this);
	listview.setMode(Mode.PULL_UP_TO_REFRESH);
	listview.setOnRefreshListener(new AddDiscussListener());
	ListView list = listview.getRefreshableView();
	list.setAdapter(adapter);
	list.setHeaderDividersEnabled(false);
	
	initViews();
}

@Click(R.id.writeCancle)
void cancle(){
	MatterDetailAcrtivity.getInstance().refreshProgress();
	finish();
}

@UiThread
void initViews(){
//	Tools.setHeadImg(detailList.usercode, head);
//	
//	name.setText(detailList.username);
//	content.setText(detailList.content);
//	if(detailList.articles != null && detailList.articles.size() > 0){
//		file.setVisibility(View.VISIBLE);
//		fileName.setText(detailList.articles.get(0).title+"."+detailList.articles.get(0).ext);
//		file.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				 Intent intent = new Intent(ProgressDiscussActivity.this,AccessoryFileListActivity.class);
//		            Bundle bundle = new Bundle();
//		            bundle.putString("id",detailList.id);
//		            intent.putExtra("bundle",bundle);
//		            startActivity(intent);
//			}
//		});
//	}
//	if(detailList.imgs != null && detailList.imgs.size()>0){
//		image.setVisibility(View.VISIBLE);
//		imageName.setText(detailList.imgs.get(0).title+"."+detailList.imgs.get(0).ext);
//		image.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(ProgressDiscussActivity.this,BorwsePicture.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("id", detailList.id);
//				intent.putExtra("bundle", bundle);
//				startActivity(intent);
//			}
//		});
//	}
//	talknum.setText("("+detailList.discussnum+")");
//	time.setText(detailList.createtime != null ?detailList.createtime:"");
//	complete.setVisibility("1".equals(detailList.isfinish)?View.VISIBLE:View.GONE);
//	talk.setOnClickListener(new OnClickListener() {
//		
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			Utils.hiddenSoftBorad(ProgressDiscussActivity.this);
//		}
//	});
}



@Override
public void sendClick(String content) {
	// TODO Auto-generated method stub
	MatterHttp.requestDiscuss(new OnMatterResult() {
		
		@Override
		public void onSuccess(String msg, Object obj) {
			// TODO Auto-generated method stub
			resetDiscuss();
		}
		
		@Override
		public void onFailure(String msg) {
			// TODO Auto-generated method stub
			
		}
	}, detailList.id, "3",Tools.getDiscussText(content), Tools.getDiscussHTML(content));
}
@Override
public void addClick() {
	// TODO Auto-generated method stub
	
}

class AddDiscussListener implements PullToRefreshBase.OnRefreshListener2<ListView>{

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		requestdiscuss();
	}
	
}


void requestdiscuss(){
	String url = "/home/phone/thing/getdiscussion";
//	pageNum = pageNum + (pageNum!=null?pageNum:1);
	pageNum += 1;
	new RequestAdpater() {
		
		@Override
		public void onReponse(ResponseData data) {
			// TODO Auto-generated method stub
			if(data.getResultState() == ResultState.eSuccess){
				listview.onRefreshComplete();
				JSONArray discuss = data.getJsonArray();
				jsonToData(discuss);
			}else{
				Toast.makeText(ProgressDiscussActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
			}
		}
		
		@Override
		public void onProgress(ProgressMessage msg) {
			// TODO Auto-generated method stub
			
		}
	}.setUrl(url)
	.addParam("dataId",detailList.id)
	.addParam("pageNo",pageNum+"")
	.addParam("pageSize",pageSize+"")
	.notifyRequest();
} 

void jsonToData(JSONArray discuss){
	if(discuss == null || discuss.length()==0 && contentInfo.size() > 0){
		Toast.makeText(ProgressDiscussActivity.this, "已经没有更多数据！", Toast.LENGTH_SHORT).show();
		return;
	}
	for(int i = 0;i <discuss.length();i++){
		JSONObject oj = discuss.optJSONObject(i);
		DetailContentInfo info = new DetailContentInfo(oj).save();
		contentInfo.add(info);
	}
	adapter.refresh(contentInfo);
	setDiscussNum(contentInfo.size());
//	talknum.setText("("+contentInfo.size()+")");
}


void resetDiscuss(){
	pageNum = 0;
	contentInfo.clear();
	requestdiscuss();
}

@Override
public void onDiscussClick(String dataId) {
	// TODO Auto-generated method stub
	
}

@Override
public void nameClick(int position,String name) {
	// TODO Auto-generated method stub
	footview.setName(position,name);
}

/*
 * 设置进展评论的条数
 */

void setDiscussNum(int position){
//	String str = "对进展的评论"+"("+position+")";
//	discussCount.setText(str);
}


@Override
public void sendDiscussDiscussClick(int position, String content) {
	// TODO Auto-generated method stub
	/*
	 * 所有都view都绑定在list的adapter上，所以这里的查找就要-1
	 */
MatterHttp.addDiscussDiscuss(new OnMatterResult() {
		
		@Override
		public void onSuccess(String msg, Object obj) {
			// TODO Auto-generated method stub
			resetDiscuss();
		}
		
		@Override
		public void onFailure(String msg) {
			// TODO Auto-generated method stub
			
		}
	}, contentInfo.get(position-1).id,  Tools.getDiscussText(content), Tools.getDiscussHTML(content));
}
	
}
