package com.miicaa.home.ui.org;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.Tools;
import com.miicaa.detail.MatterDetailAcrtivity;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;


/**
 * Created by Administrator on 13-12-27.
 */
public class ArrangementFinish extends Activity {
    String mTitle;
    String mId;
    ListView mList;
    PersonAdpater mAdpater;
    ArrayList<PersonData> mPersonDataArray;
    Toast toast;

    @Override
	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrangement_finish_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mTitle = bundle.getString("title");
        mId = bundle.getString("dataId");
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        initUI();
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }

    private void initData() {
        mPersonDataArray = new ArrayList<PersonData>();
        String url = "/home/phone/thing/getarrtodousers";

        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                convertToData(data.getJsonArray());
                mAdpater = new PersonAdpater(mPersonDataArray);
                mList.setAdapter(mAdpater);
            }

            @Override
            public void onProgress(ProgressMessage msg) {
            }
        }.setUrl(url)
                .addParam("dataId", mId)
                .notifyRequest();
    }

    private void convertToData(JSONArray jTodo) {
        if (jTodo == null) {
            return;
        }

        for (int i = 0; i < jTodo.length(); i++) {
            JSONObject jRow = jTodo.optJSONObject(i);
            PersonData data = new PersonData();
            data.mCode = jRow.optString("userCode");
            data.mName = jRow.optString("userName");
            if (!jRow.isNull("star")) {
                data.star = jRow.optInt("star");
            }
            mPersonDataArray.add(data);
        }

    }


    private void initUI() {
        mList = (ListView) findViewById(R.id.arrange_finish_id_list);
        TextView textView = (TextView) findViewById(R.id.arrange_finish_id_titile);
        textView.setText(mTitle + "并点评");
        Button button = (Button) findViewById(R.id.arrange_finish_id_back);
        button.setOnClickListener(onBackClick);
        button = (Button) findViewById(R.id.arrange_finish_id_commit);
        button.setText(mTitle);
        button.setOnClickListener(onCommitClick);
    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    
    View.OnClickListener onCommitClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPersonDataArray != null && !mPersonDataArray.isEmpty()) {
                try {
                    JSONArray array = new JSONArray();
                    for (PersonData p : mPersonDataArray) {
                        JSONObject obj = new JSONObject();
                        obj.put("usercode", p.mCode);
                        obj.put("star", p.star);
                        array.put(obj);
                    }

                    String url = "/home/phone/thing/finisharrange";
                    toast.setText("你的事项已办结");
                    if("终止".equals(mTitle)){
                    	toast.setText("你的事项已终止");
                        url = "/home/phone/thing/stoparrange";
                    }
                    new RequestAdpater() {
                        @Override
                        public void onReponse(ResponseData data) {
                            if (data.getResultState() == ResponseData.ResultState.eSuccess) {//成功响应
                            	
//                            	i.putExtra("refresh", "refresh");
//                            	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            	startActivity(i);
                            	toast.show();
                            	if(MatterDetailAcrtivity.getInstance() != null){
                            		MatterDetailAcrtivity.getInstance().iWasComplete();
                            		MatterDetailAcrtivity.getInstance().refreshthis();
                            		
                            	}
                            	finish();
                            } else {

                            }
                        }

                        @Override
                        public void onProgress(ProgressMessage msg) {
                        }
                    }.setUrl(url)
                            .addParam("dataId", mId)
                            .addParam("userStars", array.toString())
                            .addParam("onlyOne","1")
                            .notifyRequest();
                } catch (Exception e) {

                }
            }
        }
    };

    class PersonData {
        public String mName;
        public String mCode;
        public int star = 3;

    }

    class PersonAdpater extends BaseAdapter {
        ArrayList<PersonData> mDataArray;
        LayoutInflater mInflater;

        public PersonAdpater(ArrayList<PersonData> dataArray) {
            mDataArray = dataArray;
            mInflater = LayoutInflater.from(ArrangementFinish.this);
        }

        @Override
        public int getCount() {
            return mDataArray.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataArray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = mInflater.inflate(R.layout.arrangement_personnel_person_view, null);
            }
            final PersonData data = (PersonData) getItem(i);
            ((TextView) view.findViewById(R.id.personnel_cell_id_name)).setText(data.mName);
            Tools.setHeadImg(data.mCode, ((ImageView) view.findViewById(R.id.personnel_cell_id_head)));
            final ImageView oneStar = (ImageView) view.findViewById(R.id.personnel_cell_id_one_star);
            final ImageView twoStar = (ImageView) view.findViewById(R.id.personnel_cell_id_two_star);
            final ImageView threeStar = (ImageView) view.findViewById(R.id.personnel_cell_id_thr_star);
            final ImageView fourStar = (ImageView) view.findViewById(R.id.personnel_cell_id_fou_star);
            final ImageView fiveStar = (ImageView) view.findViewById(R.id.personnel_cell_id_fir_star);
            View.OnClickListener onOneClick = new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    oneStar.setImageResource(R.drawable.star_large_f);
                    twoStar.setImageResource(R.drawable.star_large_uf);
                    threeStar.setImageResource(R.drawable.star_large_uf);
                    fourStar.setImageResource(R.drawable.star_large_uf);
                    fiveStar.setImageResource(R.drawable.star_large_uf);
                    data.star = 1;
                }
            };
            oneStar.setOnClickListener(onOneClick);

            View.OnClickListener onTwoClick = new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    oneStar.setImageResource(R.drawable.star_large_f);
                    twoStar.setImageResource(R.drawable.star_large_f);
                    threeStar.setImageResource(R.drawable.star_large_uf);
                    fourStar.setImageResource(R.drawable.star_large_uf);
                    fiveStar.setImageResource(R.drawable.star_large_uf);
                    data.star = 2;
                }
            };
            twoStar.setOnClickListener(onTwoClick);

            View.OnClickListener onThreeClick = new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    oneStar.setImageResource(R.drawable.star_large_f);
                    twoStar.setImageResource(R.drawable.star_large_f);
                    threeStar.setImageResource(R.drawable.star_large_f);
                    fourStar.setImageResource(R.drawable.star_large_uf);
                    fiveStar.setImageResource(R.drawable.star_large_uf);
                    data.star = 3;
                }
            };
            threeStar.setOnClickListener(onThreeClick);

            View.OnClickListener onFourClick = new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    oneStar.setImageResource(R.drawable.star_large_f);
                    twoStar.setImageResource(R.drawable.star_large_f);
                    threeStar.setImageResource(R.drawable.star_large_f);
                    fourStar.setImageResource(R.drawable.star_large_f);
                    fiveStar.setImageResource(R.drawable.star_large_uf);
                    data.star = 4;
                }
            };
            fourStar.setOnClickListener(onFourClick);

            View.OnClickListener onFiveClick = new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    oneStar.setImageResource(R.drawable.star_large_f);
                    twoStar.setImageResource(R.drawable.star_large_f);
                    threeStar.setImageResource(R.drawable.star_large_f);
                    fourStar.setImageResource(R.drawable.star_large_f);
                    fiveStar.setImageResource(R.drawable.star_large_f);
                    data.star = 5;
                }
            };
            fiveStar.setOnClickListener(onFiveClick);

            return view;
        }
    }
}