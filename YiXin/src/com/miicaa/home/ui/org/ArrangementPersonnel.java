package com.miicaa.home.ui.org;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.home.MatterCell;

/**
 * Created by Administrator on 13-12-25.
 */
public class ArrangementPersonnel extends Activity {

    ArrayList<GroupData> mPersonnelDataArray;
    ExpandableListAdapter mAdpater;
    ExpandableListView mList;
    String mId;
    String type;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrangement_personnel_activity);
        mPersonnelDataArray = new ArrayList<GroupData>();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mId = bundle.getString("dataId");
        type = bundle.getString("type");
        initUI();
        mAdpater = new PersonnelListAdpater(mPersonnelDataArray);
        mList.setAdapter(mAdpater);

        requestData();
        //initData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }

    private void requestData() {
        String url = "/home/phone/thing/getinvolvepeople";

        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {

                convertToData(data.getJsonObject());
            }

            @Override
            public void onProgress(ProgressMessage msg) {
            }
        }.setUrl(url)
                .addParam("dataId", mId)
                .notifyRequest();
    }

    private void convertToData(JSONObject jData) {
        if (jData == null) {
            return;
        }
        JSONArray jTodo = jData.optJSONArray("toDoUsers");
        JSONArray jCopy = jData.optJSONArray("copyUsers");
        if (jTodo != null) {
        	String title = "办理人";
        	if(type!=null && type.equals(MatterCell.WORKREPORTTYPE)){
        		title = "点评人";
        	}
            GroupData group = new GroupData(title, jTodo.length());
            mPersonnelDataArray.add(group);
            for (int i = 0; i < jTodo.length(); i++) {
                JSONObject jRow = jTodo.optJSONObject(i);
                PersonData data = new PersonData(jRow.optString("userName"));
                data.mCode = jRow.optString("userCode");
                data.mStar = jRow.optInt("star");
                group.add(data);
            }

            mList.expandGroup(0);
        }
        if (jCopy != null) {
            GroupData group = new GroupData("抄送人", jCopy.length());
            mPersonnelDataArray.add(group);
            for (int i = 0; i < jCopy.length(); i++) {
                JSONObject jRow = jCopy.optJSONObject(i);
                PersonData data = new PersonData(jRow.optString("userName"));
                data.mCode = jRow.optString("userCode");
                data.mStar = 0;
                group.add(data);
            }
            mList.expandGroup(1);
        }


        //mList.expandGroup(1);
    }

    private void initUI() {
        mList = (ExpandableListView) findViewById(R.id.arrange_personnel_id_list);
        Button button = (Button) findViewById(R.id.arrange_personnel_id_back);
        button.setOnClickListener(onBackClick);
        TextView title = (TextView) findViewById(R.id.title);
        if(type!=null && type.equals(MatterCell.WORKREPORTTYPE)){
        	title.setText("查看点评人");
        }
    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    class GroupData {
        public String mTitle;
        public int mType;
        ArrayList<PersonData> mDataArray;

        public GroupData(String title, int type) {
            mTitle = title;
            mType = type;
            mDataArray = new ArrayList<PersonData>();
        }

        public void add(PersonData data) {
            mDataArray.add(data);
        }

        public int size() {
            return mDataArray.size();
        }

        public PersonData get(int i) {
            return mDataArray.get(i);
        }
    }

    class PersonData {
        public String mName;
        public int mStar;
        public String mCode;

        public PersonData(String name) {
            mName = name;
        }

    }

    public class PersonnelListAdpater extends BaseExpandableListAdapter {

        ArrayList<GroupData> mDataArray;

        public PersonnelListAdpater(ArrayList<GroupData> dataArray) {
            mDataArray = dataArray;
        }

        @Override
        public int getGroupCount() {
            return mDataArray.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return mDataArray.get(i).size();
        }

        @Override
        public Object getGroup(int i) {
            return mDataArray.get(i);
        }

        @Override
        public Object getChild(int i, int i2) {
            return mDataArray.get(i).get(i2);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i2) {
            return i2;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            GroupView groupView = null;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(ArrangementPersonnel.this);
                view = inflater.inflate(R.layout.person_group, null);
                groupView = new GroupView();
                groupView.mTitleText = (TextView) view.findViewById(R.id.title);
                view.setTag(groupView);
            } else {
                groupView = (GroupView) view.getTag();
            }
            GroupData group = mDataArray.get(i);
            groupView.mTitleText.setText(group.mTitle);

            return view;
        }

        @Override
        public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
            CellView cellView = null;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(ArrangementPersonnel.this);
                view = inflater.inflate(R.layout.arrangement_personnel_person_view, null);
                cellView = new CellView();
                cellView.mHeadView = (ImageView) view.findViewById(R.id.personnel_cell_id_head);
                cellView.mNameView = (TextView) view.findViewById(R.id.personnel_cell_id_name);
                cellView.mStarLayout = (LinearLayout) view.findViewById(R.id.personnel_cell_id_star_layout);
                cellView.mOneStarView = (ImageView) view.findViewById(R.id.personnel_cell_id_one_star);
                cellView.mTwoStarView = (ImageView) view.findViewById(R.id.personnel_cell_id_two_star);
                cellView.mThrStarView = (ImageView) view.findViewById(R.id.personnel_cell_id_thr_star);
                cellView.mFouStarView = (ImageView) view.findViewById(R.id.personnel_cell_id_fou_star);
                cellView.mFirStarView = (ImageView) view.findViewById(R.id.personnel_cell_id_fir_star);
                view.setTag(cellView);
            } else {
                cellView = (CellView) view.getTag();
            }

            GroupData group = mDataArray.get(i);
            PersonData person = group.get(i2);
            cellView.mNameView.setText(person.mName);
            Tools.setHeadImg(person.mCode, cellView.mHeadView);
            cellView.mStarLayout.setVisibility(View.GONE);

            if (person.mStar == 0) {
                cellView.mStarLayout.setVisibility(View.GONE);
                cellView.mOneStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mTwoStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mThrStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mFouStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mFirStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
            } else if (person.mStar == 1) {
                cellView.mStarLayout.setVisibility(View.VISIBLE);
                cellView.mOneStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mTwoStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mThrStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mFouStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mFirStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
            } else if (person.mStar == 2) {
                cellView.mStarLayout.setVisibility(View.VISIBLE);
                cellView.mOneStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mTwoStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mThrStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mFouStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mFirStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));

            } else if (person.mStar == 3) {
                cellView.mStarLayout.setVisibility(View.VISIBLE);
                cellView.mOneStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mTwoStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mThrStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mFouStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
                cellView.mFirStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
            } else if (person.mStar == 4) {
                cellView.mStarLayout.setVisibility(View.VISIBLE);
                cellView.mOneStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mTwoStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mThrStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mFouStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mFirStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_uf));
            } else if (person.mStar == 5) {
                cellView.mStarLayout.setVisibility(View.VISIBLE);
                cellView.mOneStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mTwoStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mThrStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mFouStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
                cellView.mFirStarView.setImageDrawable(getResources().getDrawable(R.drawable.star_small_f));
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return false;
        }


        class GroupView {
            TextView mTitleText;
        }

        class CellView {
            ImageView mHeadView;
            TextView mNameView;
            LinearLayout mStarLayout;
            ImageView mOneStarView;
            ImageView mTwoStarView;
            ImageView mThrStarView;
            ImageView mFouStarView;
            ImageView mFirStarView;
        }
    }
}