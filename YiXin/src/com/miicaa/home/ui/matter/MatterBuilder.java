package com.miicaa.home.ui.matter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.home.ui.matter.approveprocess.ApprovalProcessTypeView;
import com.miicaa.home.ui.report.WorkReportActivity;
import com.miicaa.home.ui.report.WorkReportActivity_;

/**
 * Created by Administrator on 14-2-8.
 */
public class MatterBuilder extends Activity
{
	
	private static String TAG  = "MatterBuilder";
	
    String mDataType;
    LayoutInflater factory;
    LinearLayout layout;
	private String mArrangeType = "";
	
    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matter_builder_activity);
        factory=LayoutInflater.from(this);
        initIntentData();
        initUI();
    }

    private void initIntentData()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mDataType = bundle.getString("dataType");
        mArrangeType  = bundle.getString("arrangeType", "");

    }

    @SuppressWarnings("serial")
	public void requestApproveType() {
        String url = "/home/phone/thing/getapproveTemp";
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {

                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    try {
                        JSONArray approveTypes = data.getJsonArray();

                        Log.d(TAG, "approve onReponse data:"+approveTypes);
                        for(int i =0;i<approveTypes.length();i++) {
                            JSONObject approveType = (JSONObject)approveTypes.get(i);
                            String id = approveType.getString("id");
                            String type = approveType.getString("type");
                            String content = approveType.getString("content");
                            int processKey = approveType.isNull("modelId") ? ApprovalProcessTypeView.FREE : ApprovalProcessTypeView.FIXED;
                            String modelId = approveType.optString("modelId");
//                            Log.d(TAG, "mProcessType:"+mProcessKey + "moudleId:"+approveType.optString("modeId"));
                            MatterApproveType  approveView = new MatterApproveType(type,id,content);
                            approveView.mProcessKey = processKey;
                            approveView.modelId = modelId;
                            layout.addView(approveView.getmRootView());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    //TODO:失败了要弹通知
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {
            }
        }.setUrl(url)
                .notifyRequest();
    }
    
    @SuppressWarnings("serial")
   	public void requestReportType() {
    	View view = factory.inflate(R.layout.matter_report_view, null);
    	layout.addView(view);
      }

    private void initUI()
    {
        TextView titleText = (TextView)findViewById(R.id.matter_builder_id_title);
        LinearLayout arrageLayout = (LinearLayout)findViewById(R.id.matter_builder_id_arrange);
        layout = (LinearLayout)findViewById(R.id.matter_builder_id_layout);
        

        if(mDataType.equals("1"))
        {
            titleText.setText("选择任务类型");
            arrageLayout.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }
        else if(mDataType.equals("2"))
        {
        	requestApproveType();
            titleText.setText("选择审批类型");
            arrageLayout.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
        else if(mDataType.equals("3"))
        {
        	requestReportType();
            titleText.setText("选择工作报告类型");
            arrageLayout.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }

        Button button = (Button)findViewById(R.id.matter_builder_id_back);
        button.setOnClickListener(onBackClick);

        button = (Button)findViewById(R.id.matter_builder_id_official);
        button.setOnClickListener(onOfficialClick);
        button = (Button)findViewById(R.id.matter_builder_id_person);
        button.setOnClickListener(onPersonClick);

    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left,R.anim.my_slide_out_right);
    }

    View.OnClickListener onOfficialClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
        	if(mArrangeType!=null&&!mArrangeType.equals("")){
        		/*HashMap<String, String> map = new HashMap<String, String>();
        		map.put("reportListId", mArrangeType);
        		map.put("arrangeType", mDataType);
        		ReportUtils.requestList(MatterBuilder.this, new RequestCallback() {
					
					@Override
					public void callback(ResponseData data) {
						 if(FramMainActivity.instance != null)
				                FramMainActivity.instance.refushMatterFrame();
						setResult(Activity.RESULT_OK);
						finish();
						
					}
				}, getString(R.string.report_generate_arrange_url), map);*/
        		Bundle b = getIntent().getBundleExtra("bundle");
        		 Intent intent = new Intent(MatterBuilder.this, MatterEditor.class);
                 Bundle bundle = new Bundle();
                 bundle.putString("dataType",mDataType);
                 bundle.putString("matterType","1");
                 bundle.putString("editType","03");
                 bundle.putString("id","");
                 bundle.putString("item",b.getString("item"));
                 bundle.putSerializable("info", getIntent().getBundleExtra("bundle").getSerializable("info"));
                 intent.putExtra("bundle",bundle);
                 startActivityForResult(intent, 1);
                 overridePendingTransition(R.anim.my_slide_in_right,R.anim.my_slide_out_left);
        		return;
        	}
            Intent intent = new Intent(MatterBuilder.this, MatterEditor.class);
            Bundle bundle = new Bundle();
            bundle.putString("dataType",mDataType);
            bundle.putString("matterType","1");
            bundle.putString("editType","01");
            bundle.putString("id","");
            
            intent.putExtra("bundle",bundle);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.my_slide_in_right,R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onPersonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
        	if(mArrangeType!=null&&!mArrangeType.equals("")){
        		/*HashMap<String, String> map = new HashMap<String, String>();
        		map.put("reportListId", mArrangeType);
        		map.put("arrangeType", mDataType);
        		ReportUtils.requestList(MatterBuilder.this, new RequestCallback() {
					
					@Override
					public void callback(ResponseData data) {
						 if(FramMainActivity.instance != null)
				                FramMainActivity.instance.refushMatterFrame();
						setResult(Activity.RESULT_OK);
						finish();
						
					}
				}, getString(R.string.report_generate_arrange_url), map);*/
        		Bundle b = getIntent().getBundleExtra("bundle");
        		 Intent intent = new Intent(MatterBuilder.this, MatterEditor.class);
                 Bundle bundle = new Bundle();
                 bundle.putString("dataType",mDataType);
                 bundle.putString("matterType","0");
                 bundle.putString("editType","03");
                 bundle.putString("id","");
                 bundle.putString("item",b.getString("item"));
                 bundle.putSerializable("info", b.getSerializable("info"));
                 intent.putExtra("bundle",bundle);
                 startActivityForResult(intent, 1);
                 overridePendingTransition(R.anim.my_slide_in_right,R.anim.my_slide_out_left);
        		return;
        	}
            Intent intent = new Intent(MatterBuilder.this, MatterEditor.class);
            Bundle bundle = new Bundle();
            bundle.putString("dataType",mDataType);
            bundle.putString("matterType","0");
            bundle.putString("editType","01");
            bundle.putString("id","");
            intent.putExtra("bundle",bundle);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.my_slide_in_right,R.anim.my_slide_out_left);
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                finish();
//                if(FramMainActivity.instance != null)
//                FramMainActivity.instance.refushMatterFrame();
            }
        }
    }

    class MatterApproveType {
        View mRootView;
        Button mButton;
        String mId;
        String mContent;
        String mType;
        int mProcessKey;
        String modelId;
        View.OnClickListener approveTypeClick = new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MatterBuilder.this, MatterEditor.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",mId);
                bundle.putString("matterType",mType);
                bundle.putString("editType","01");
                bundle.putString("content",mContent);
                bundle.putString("dataType",mDataType);
                intent.putExtra("bundle",bundle);
                intent.putExtra("process", mProcessKey);
                intent.putExtra("modelId", modelId);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.my_slide_in_right,R.anim.my_slide_out_left);
            }
        };

        public MatterApproveType(String type,String id,String content) {
            mRootView = factory.inflate(R.layout.matter_approve_cell_view,null);
            mButton = (Button) mRootView.findViewById(R.id.matter_builder_id_button);
            mButton.setText(type);
            mId = id;
            mContent = content;
            mType = type;
            mButton.setOnClickListener(approveTypeClick);
        }

        public View getmRootView() {
            return mRootView;
        }
    }
    
    public void report(View view){
    	int type = 0;
    	switch(view.getId()){
    	case R.id.matter_builder_id_reportday:
    		type = WorkReportActivity.REPORT_DAY;
    		break;
    	case R.id.matter_builder_id_reportweek:
    		type = WorkReportActivity.REPORT_WEEK;
    		break;
    	case R.id.matter_builder_id_reportmonth:
    		type = WorkReportActivity.REPORT_MONTH;
    		break;
    	case R.id.matter_builder_id_reportcustom:
    		type = WorkReportActivity.REPORT_CUSTOM;
    		break;
    		default:
    			return;
    	}
    	WorkReportActivity_.intent(this)
    	.reportType(type)
    	.startForResult(1);
    	overridePendingTransition(R.anim.my_slide_in_right,R.anim.my_slide_out_left);
    }
}
