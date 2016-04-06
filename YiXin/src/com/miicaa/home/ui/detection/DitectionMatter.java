package com.miicaa.home.ui.detection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miicaa.common.base.MatterRequest;
import com.miicaa.common.base.SearchFunction;
import com.miicaa.common.base.SearchFunction.OnSearchCallBack;
import com.miicaa.common.base.Tools;
import com.miicaa.detail.MatterDetailAcrtivity_;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.MatterInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.home.ui.org.LableGroup;
import com.miicaa.home.ui.report.ReportDetailActivity_;
import com.miicaa.utils.AllUtils;

/**
 * Created by Administrator on 14-1-21.
 */
public class DitectionMatter extends Activity {

    int i = 1;
    public final  static int LOAD_CREATE = 0x1;
    public final static int LOAD_AGAIN = 0x2;
    public final static int LOAD_CONTINUE = 0x3;
    
    static int MATTERDETAIL = 0X110;
    
    PullToRefreshListView mPullToRefreshList;
    ArrayList<MatterSample> mMatterArray;
    MatterListAdpater mAdpater;
    ListView mListView;
    String mType;
    String url;
    Boolean isRefresh = false;
    RelativeLayout mLoadingLayout ;
    SearchFunction searchFunction;
    HashMap<String,String> paramMap;
    Boolean isSearch = false;
    Boolean isMatterDetailFresh = false;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mType = bundle.getString("type");
        paramMap = new HashMap<String, String>();//初始化参数
        setContentView(R.layout.detection_matter_activity);
        //initSearch();
        initUI();
        initData();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }

    private void loading(String content)
    {
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    private void loaded()
    {
        mLoadingLayout.setVisibility(View.GONE);
    }

    private void initData()
    {
        mMatterArray = new ArrayList<MatterSample>();
        mAdpater = new MatterListAdpater();
        mListView.setAdapter(mAdpater);
        i = 1;
        requestMatter(mType,LOAD_CREATE);

    }
    //初始化paramMap请求参数
    private void initParam(){
        paramMap.put("pageSize", "25");
        if(mType.equals("over")){
           paramMap.put("type", "over");
        }else if(mType.equals("notOver")){
            paramMap.put("type", "notOver");
        }else if(mType.equals("observerOver")){
            paramMap.put("isOver", "1");
        }else if(mType.equals("observerNotOver")){
            paramMap.put("isOver", "0");
        }
        paramMap.put("pageNo",i+"");
    }

    private void requestMatter(String type,int loadHow)
    {
        switch (loadHow){
            case LOAD_CREATE:
                mPullToRefreshList.setLoadingAll(false);
                i = 1;
                mMatterArray.clear();
                loading("");
                break;
            case LOAD_AGAIN:
                mPullToRefreshList.setLoadingAll(false);
                i = 1;
                mMatterArray.clear();
                break;
            case LOAD_CONTINUE:
                break;
        }
        paramMap.put("pageNo",i+"");
        MatterRequest.requestMatterHome(url,paramMap,new MatterRequest.MatterHomeCallBackListener() {
            @Override
            public void callBack(ResponseData data) {
                callBackRequst(data);
            }
        });
    }

    //请求道数据后的回调
    private void callBackRequst(ResponseData data){
        if (data.getResultState() == ResponseData.ResultState.eSuccess) {
            convertToMatter(data.getJsonObject());
        } else {
            Toast.makeText(DitectionMatter.this,data.getMsg()+"",1).show();
            mMatterArray.clear();
            mAdpater.notifyDataSetChanged();
        }
        mPullToRefreshList.onRefreshComplete();
        loaded();
    }

    private void convertToMatter(JSONObject jData)
    {
        if(jData == null)
        {
            mAdpater.notifyDataSetChanged();
            return;
        }
        JSONArray jWorkList = jData.optJSONArray("workList");
        if(jWorkList == null || jWorkList.length() < 1 )
        {
            if (isSearch) {
                if (searchFunction.getPageCount() == 1) {
                    Toast.makeText(DitectionMatter.this,
                            "抱歉，找不到符合条件的事项，请换个搜索条件试试吧！",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(DitectionMatter.this,"哎呀，没有数据啦。",Toast.LENGTH_SHORT).show();
            }
//            mPullToRefreshList.setLoadingAll(true);
            mPullToRefreshList.onRefreshComplete();
            mAdpater.notifyDataSetChanged();
            return;
        }

        ArrayList<MatterInfo> matterArray = new ArrayList<MatterInfo>();

        for(int i =0; i < jWorkList.length(); i++)
        {
            ArrayList<String> labels = new ArrayList<String>();
            JSONObject jRow = jWorkList.optJSONObject(i);
            MatterInfo info = new MatterInfo();

            if(!jRow.isNull("id")){
                info.setId(jRow.optString("id")) ;
            }
            if(!jRow.isNull("title")){
                info.setTitle(jRow.optString("title"));
            }
            if(!jRow.isNull("content")){
                info.setContent(jRow.optString("content"));
            }
            if(!jRow.isNull("arrangeType")){
                info.setArrangeType(jRow.optString("arrangeType"));
            }
            if(!jRow.isNull("peopleNum")){
                info.setPeopleNum(jRow.optLong("peopleNum"));
            }
            if(!jRow.isNull("startTime")){
                info.setStartTime(new Date(jRow.optLong("startTime")));
            }
            if(!jRow.isNull("endTime")){
                info.setEndTime(new Date(jRow.optLong("endTime"))) ;
            }
            if(!jRow.isNull("secrecy")){
                info.setSecrecy(jRow.optString("secrecy"));
            }
            if(!jRow.isNull("creatorCode")){
                info.setCreatorCode(jRow.optString("creatorCode")) ;
            }
            if(!jRow.isNull("creatorName")){
                info.setCreatorName(jRow.optString("creatorName"));
            }
            if(!jRow.isNull("dataVersion")){
                info.setDataVersion(jRow.optString("dataVersion"));
            }
            if (!jRow.isNull("hasAttach")){
                info.setHasAtt(jRow.optBoolean("hasAttach"));
            }
            if(!jRow.isNull("createTime")){
                info.setCreateTime(new Date(jRow.optLong("createTime")));
            }
            if(!jRow.isNull("updateTime")){
                info.setLastUpdateTime(new Date(jRow.optLong("updateTime")));
            }
            if(!jRow.isNull("dataType")){
                info.setDataType(jRow.optString("dataType"));
            }

            if(!jRow.isNull("srcName")){
                info.setSrcName(jRow.optString("srcName"));
            }
            
            if(!jRow.isNull("srcCode")){
            	info.setSrcCode(jRow.optString("srcCode"));
            }

            if(!jRow.isNull("clientName")){
                info.setClientName(jRow.optString("clientName")) ;
            }
            if(!jRow.isNull("status")){
                info.setStatus(jRow.optString("status"));
            }
            if(!jRow.isNull("approveTypeName")){
                info.setApproveTypeName(jRow.optString("approveTypeName")) ;
            }
            if(!jRow.isNull("lastApproveStatus")){
                info.setLastApproveStatus(jRow.optString("lastApproveStatus"));
            }
            if(!jRow.isNull("orgcode")){
                info.setOrgcode(jRow.optString("orgcode"));
            }
            if(!jRow.isNull("repeatId")){
                info.setRepeatId(jRow.optString("repeatId")) ;
            }
            if(!jRow.isNull("repeatStr")){
                info.setRepeatStr(jRow.optString("repeatStr"));
            }
            if(!jRow.isNull("planTime")){
            	info.setPlanTime(jRow.optLong("planTime"));
            }
            if(!jRow.isNull("remindTime")){
            	info.setRemindTime(new Date(jRow.optLong("remindTime")));
            }
            if (jRow.optJSONArray("labels").length()>0){
                for (int j = 0; j < jRow.optJSONArray("labels").length(); j++){
                    JSONObject js = jRow.optJSONArray("labels").optJSONObject(j);
                    labels.add(js.optString("label"));
                }
                info.setLabels(labels);
            }
            mMatterArray.add(new MatterSample(info.getId(), info));
        }
        mAdpater.notifyDataSetChanged();
    }

    private MatterInfo findByArray(ArrayList<MatterInfo> matterInfos, String dataId)
    {
        for(int i =0; i < matterInfos.size(); i++)
        {
            if(matterInfos.get(i).getId().equals(dataId))
            {
                return matterInfos.get(i);
            }
        }
        return null;
    }

    private void initUI()
    {

        mLoadingLayout = (RelativeLayout)findViewById(R.id.detection_matter_id_loading_layout);
        mPullToRefreshList = (PullToRefreshListView) findViewById(R.id.detection_matter_id_list);
        mPullToRefreshList.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshList.setOnRefreshListener(new MyOnRefreshListener2());
        mListView = mPullToRefreshList.getRefreshableView();
        View headView = LayoutInflater.from(this).inflate(R.layout.matter_home_view,null);
       
       mListView.addHeaderView(headView);
       initSearch(headView);
        mListView.setOnItemClickListener(onItemClickListener);
        Button button = (Button) findViewById(R.id.detection_matter_id_back);
        button.setOnClickListener(onBackClick);
        TextView textView = (TextView) findViewById(R.id.detection_matter_id_title);
        if(mType.equals("arrangement")){
            textView.setText("全公司-任务");
            isMatterDetailFresh = true;
        }else if(mType.equals("appoval")){
            textView.setText("全公司-审批");
            isMatterDetailFresh = true;
        }else if(mType.equals("notOver")){
            textView.setText("我的已办-未结束");
            isMatterDetailFresh = true;
        }else if(mType.equals("over")){
            textView.setText("我的已办-已结束");
        }else if(mType.equals("observerNotOver")){
            textView.setText("我关注的-未结束");
            isMatterDetailFresh = true;
        }else if(mType.equals("observerOver")){
            textView.setText("我关注的-已结束");
        }
    }
    //搜索功能的初始化
    private  void initSearch(View view){
        initParam();
        String url = "/home/phone/thing/getallapprove";
        if(mType.equals("arrangement"))
        {
            url = "/home/phone/thing/getallarrange";
        }else if(mType.equals("notOver")||mType.equals("over")){
            url = "/home/phone/thing/getallwork";
        }else if(mType.equals("observerNotOver")||mType.equals("observerOver")){
            url = "/home/phone/thing/getobserver";
        }
        this.url = url;
        searchFunction = new SearchFunction(DitectionMatter.this,url,view,true);
        HashMap<String,String> map = new HashMap<String, String>();
        map.putAll(paramMap);
        searchFunction.setParam(map);
        searchFunction.setHint("查找标题");
        searchFunction.setSearchCallBack(new OnSearchCallBack() {
			
			@Override
			public void textChange(Boolean isText) {
				// TODO Auto-generated method stub
				isSearch = isText;
			}
			
			@Override
			public void search(ResponseData data) {
				// TODO Auto-generated method stub
				 isSearch = true;
	                if (searchFunction.getPageCount() == 1){
	                    mPullToRefreshList.setLoadingAll(false);
	                    i = 1;
	                    mMatterArray.clear();
	                }
	                callBackRequst(data);
			}
			
			@Override
			public void deltext() {
				// TODO Auto-generated method stub
				 requestMatter(mType,LOAD_CREATE);
	                isSearch = false;
			}

			@Override
			public void addMore(ResponseData data) {
				
			}

			@Override
			public void clearRefresh() {
				
			}
		});
      
    }

    //上拉下拉刷新
    class MyOnRefreshListener2 implements PullToRefreshBase.OnRefreshListener2<ListView> {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            isRefresh = true;
            i = 1;
            mMatterArray.clear();
            mAdpater.notifyDataSetChanged();
            if (isSearch){
                searchFunction.setPageCount(1);
                searchFunction.search();
            }else {
                requestMatter(mType,LOAD_AGAIN);
            }

        }
        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            i++;
            paramMap.put("pageNo", i+"");
            if (isSearch) {
                searchFunction.search();
            }else{
                requestMatter(mType, LOAD_CONTINUE);
            }
        }
    }
    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MatterSample sample = mMatterArray.get(i);
            Log.d("DitectionMatter", "onItemClick sample title"+sample.mMatter.getTitle());
            if("report".equals(sample.mMatter.getSrcCode())){
            	ReportDetailActivity_.intent(DitectionMatter.this)
				.dataId(sample.mId)
				.isDiscover(true)
				.start();
            }else{
            MatterDetailAcrtivity_.intent(DitectionMatter.this).dataId(sample.mId)
            .dataType(sample.mMatter.getDataType())
            .titleText(sample.mMatter.getTitle())
            .needFresh(isMatterDetailFresh)
            .isPushMessage(false)
            .startForResult(MATTERDETAIL);
            }
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onSampClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MatterSample sample = (MatterSample)view.getTag();
            if("report".equals(sample.mMatter.getSrcCode())){
            	ReportDetailActivity_.intent(DitectionMatter.this)
				.dataId(sample.mId)
				.isDiscover(true)
				.start();
            }else{
            MatterDetailAcrtivity_.intent(DitectionMatter.this).dataId(sample.mId)
            .dataType(sample.mMatter.getDataType())
            .titleText(sample.mMatter.getTitle())
            .start();
            }
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };



    class MatterSample
    {
        String mId;
        MatterInfo mMatter;
        public MatterSample(String id, MatterInfo matter)
        {
            mId = id;
            mMatter = matter;
        }
    }
    


    class  MatterListAdpater extends BaseAdapter
    {

        @Override
        public int getCount() {
            return mMatterArray.size();
        }

        @Override
        public Object getItem(int i) {
            return mMatterArray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressWarnings("deprecation")
		@Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            final DrawCell draw ;
            if(view == null)
            {
                LayoutInflater inflater = DitectionMatter.this.getLayoutInflater();
                view = inflater.inflate(R.layout.detection_cell_view,null);
                draw = new DrawCell();
                draw.mLableGroup = (LableGroup)view.findViewById(R.id.detection_cell_lableGroup);
                draw.mLayout = (LinearLayout)view.findViewById(R.id.detection_cell_id_layout);
                draw.mHeaedImg = (ImageView) view.findViewById(R.id.detection_cell_id_head);
                draw.mCountText = (TextView) view.findViewById(R.id.detection_cell_id_count);
                draw.mNameText = (TextView) view.findViewById(R.id.detection_cell_id_name);
                draw.mTimeText = (TextView) view.findViewById(R.id.detection_cell_id_time);
                draw.mMonoText = (TextView) view.findViewById(R.id.detection_cell_id_momo);
                draw.mTitleText = (TextView) view.findViewById(R.id.detection_cell_id_title);
                draw.mRemindImg = (ImageView) view.findViewById(R.id.detection_cell_id_remind);
                draw.mPlanImg = (ImageView) view.findViewById(R.id.detection_cell_id_plan);
                draw.mAttImg = (ImageView) view.findViewById(R.id.detection_cell_id_attechment);
                draw.clickLayout = (LinearLayout)view.findViewById(R.id.clickLayout);
                draw.mTimeText.setVisibility(View.GONE);
                view.setTag(draw);
            }
            else
            {
                draw = (DrawCell)view.getTag();
            }

//            view.setOnClickListener(onSampClick);
            final MatterSample sample = mMatterArray.get(i);
            draw.mLayout.setTag(sample);
//            draw.mLayout.setClickable(true);
            if (draw.mLableGroup.getChildCount() > 0){
                draw.mLableGroup.removeAllViews();
            }
            if (sample.mMatter.getLabels()!=null && !sample.mMatter.getLabels().isEmpty()){
                for (int j = 0; j < sample.mMatter.getLabels().size();j++){
                	 String str = sample.mMatter.getLabels().get(j);
                	 View v = AllUtils.getLabelView(DitectionMatter.this, str);
                    draw.mLableGroup.addView(v);
                }
            }
//            draw.mLayout.setOnClickListener(onSampClick);
         
            draw.clickLayout.setClickable(true);
            draw.clickLayout.setTag(sample);
            draw.clickLayout.setOnClickListener(onSampClick);
            
            draw.mNameText.setText(sample.mMatter.getCreatorName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (sample.mMatter.getHasAtt() == true){
                draw.mAttImg.setVisibility(View.VISIBLE);
            }else{
                draw.mAttImg.setVisibility(View.GONE);
            }
            if(sample.mMatter.getLastUpdateTime() != null)
            {
//                draw.mTimeText.setVisibility(View.VISIBLE);
//                draw.mTimeText.setText(format.format(sample.mMatter.getLastUpdateTime()));
            }
            else
            {
//                draw.mTimeText.setVisibility(View.GONE);
            }
            draw.mLayout.setTag(sample);
            draw.mRemindImg.setVisibility(sample.mMatter.getRemindTime() == null ? View.GONE:View.VISIBLE);
            draw.mPlanImg.setVisibility(sample.mMatter.getPlanTime() == null ? View.GONE :View.VISIBLE);
            if(sample.mMatter.getContent()==null || sample.mMatter.getContent().trim().equals("")){
                draw.mMonoText.setVisibility(View.GONE);
            }else {
                draw.mMonoText.setText("描述：" + sample.mMatter.getContent());
                draw.mMonoText.setVisibility(View.VISIBLE);
            }
            draw.mTitleText.setText(sample.mMatter.getTitle());
            Tools.setHeadImg(sample.mMatter.getCreatorCode(),draw.mHeaedImg);
            //这个数字先不要
//            if(sample.mMatter.getPeopleNum() != 0)
//            {
//                draw.mCountText.setVisibility(View.VISIBLE);
//                draw.mCountText.setText(String.valueOf(sample.mMatter.getPeopleNum()));
//            }
//            else
//            {
                draw.mCountText.setVisibility(View.INVISIBLE);
//            }

            return view;
        }

        class DrawCell
        {
            LinearLayout mLayout;
            LinearLayout clickLayout;
            ImageView mHeaedImg;
            TextView mCountText;
            TextView mNameText;
            TextView mTimeText;
            ImageView mRemindImg;
            ImageView mPlanImg;
            ImageView mAttImg;
            TextView mMonoText;
            TextView mTitleText;
            Button guanZhuBtn;
            LableGroup mLableGroup;
        }
    }
    
    
    void cancleGuanzhu(final MatterSample sample){
    	String url = "/home/pc/thing/setobserve";
    	new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				if(data.getResultState() == ResultState.eSuccess){
					if(mMatterArray.contains(sample))
					{
						mMatterArray.remove(sample);
						mAdpater.notifyDataSetChanged();
						
					}
				}else{
					Toast.makeText(DitectionMatter.this, "取消关注失败", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("dataId",sample.mId)
		.addParam("isObserve","0")
		.notifyRequest();
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == MATTERDETAIL){
			switch (resultCode) {
			case FramMainActivity.COMPLETE:
				requestMatter(mType, LOAD_AGAIN);
				break;
			default:
				break;
			}
		}
	}
    
    
    
}
