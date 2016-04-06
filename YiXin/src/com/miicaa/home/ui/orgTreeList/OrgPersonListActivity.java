package com.miicaa.home.ui.orgTreeList;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.data.OnFinish;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.OrgTreeElement;
import com.miicaa.home.ui.contactList.ContactData;

/**
 * Created by apple on 13-11-22.
 */
public class OrgPersonListActivity extends Activity {
	private static OnFinish onFinish = null;
    public static OrgPersonListActivity meT;
	ArrayList<ContactData> trendsArray;//动态
	private boolean isRoot = false;

	public static enum SelectType {NONE, SINGLE, MULTIPLE}

	public static enum OrgType {UNIT, UNIT_USER, GROUP, GROUP_USER}

	public static void showOrgTree(Context context,
								   String title,
								   OrgType orgType,
								   SelectType selectType,
								   ArrayList<OrgTreeElement> select,
								   OnFinish onFinish) {
		OrgPersonListActivity.onFinish = onFinish;
            switch (orgType) {
			case UNIT:
			case UNIT_USER:
				OrgPersonData.getInstance().creatOrgPersonData(AccountInfo.instance().getLastOrgInfo().getUnitTree(), select);
				break;
			case GROUP:
			case GROUP_USER:
				OrgPersonData.getInstance().creatOrgPersonData(AccountInfo.instance().getLastOrgInfo().getGrupTree(), select);
				break;
		}
		Intent itent = new Intent(context, OrgPersonListActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("title", title);
		mBundle.putInt("org_type", orgType.ordinal());
		mBundle.putInt("select_type", selectType.ordinal());
		mBundle.putString("id", "00");
		mBundle.putInt("level", 0);
		itent.putExtras(mBundle);
		context.startActivity(itent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_person_list_activity);
		// 动态初始化数据
		trendsArray = trendsDataCreate();

		Bundle bundle = getIntent().getExtras();
		String title = bundle.getString("title");
		OrgType orgType = OrgType.values()[bundle.getInt("org_type")];//分组
		SelectType selectType = SelectType.values()[bundle.getInt("select_type")];//选择方式
		String id = bundle.getString("id");
		int level = bundle.getInt("level");
		isRoot = (level == 0);

		TextView titileTextView = (TextView) findViewById(R.id.org_person_list_activity_title);
		titileTextView.setText(title);

		viewShow(id, level, orgType, selectType);
        meT = this;
	}

	private ArrayList<ContactData> trendsDataCreate() {
		ArrayList<ContactData> trendsArray = new ArrayList<ContactData>();
		for (int i = 0; i < 4; i++) {
			ContactData trends = new ContactData();
			trends.setPingyinKey("★");
			trends.setName("动态提醒" + i);
			trends.setDataType("trends");
			trendsArray.add(trends);
		}
		return trendsArray;
	}

    private void initSelect(List<OrgTreeElement> list) {
        for(OrgTreeElement element : list) {
            List<OrgTreeElement> children = element.getChildren();
            if(children.size() > 0 ) {
                initSelect(children);
            }
            element.setSelect(false);
        }
    }

	private void viewShow(String id, int level, OrgType orgType, SelectType selectType) {// 数据加载完成后才会进行页面的显示
		LinearLayout rootLayout = (LinearLayout) findViewById(R.id.org_person_list_activity_root_layout);
		rootLayout.removeAllViews();

		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ArrayList<OrgTreeElement> listData = OrgPersonData.getInstance().getReturnDataTree(id, level);
        initSelect(listData);
		OrgPersonList orgPersonList = new OrgPersonList(this, listData, orgType, selectType);
		View list = orgPersonList.getRootView();
		rootLayout.addView(list);

		if (level == -1) {
			LinearLayout contentListLayout = (LinearLayout) findViewById(R.id.org_person_list_activity_trends_content);
			contentListLayout.removeAllViews();
			for (int i = 0; i < trendsArray.size(); i++) {
				View itemView = layoutInflater.inflate(R.layout.contact_list_trends_cell, null);
				TextView contactName = (TextView) itemView.findViewById(R.id.contact_list_trends_cell_contactName);
				contactName.setText(trendsArray.get(i).getName());
				contentListLayout.addView(itemView);
			}
		}

		Button returnButton = (Button) findViewById(R.id.org_person_list_activity_back_button);
		returnButton.setOnClickListener(returnButtonClick);

		Button submitButton = (Button) findViewById(R.id.org_person_list_activity_submit_button);
		submitButton.setOnClickListener(submitButtonClick);

		if (selectType == SelectType.NONE) {
			submitButton.setVisibility(View.GONE);
		}
	}

	View.OnClickListener returnButtonClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			setResult(0);
			finish();
		}
	};
	View.OnClickListener submitButtonClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Log.d("选择的结果--------", OrgPersonData.getInstance().getSelectResultStr());
			//Toast.makeText(OrgPersonListActivity.this, "选择的结果--------" +
					//OrgPersonData.getInstance().getSelectResultStr(), Toast.LENGTH_LONG).show();
			doFinish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 9) {
			doFinish();
		}
	}

	private void doFinish() {
		if (isRoot) {
			if (OrgPersonListActivity.onFinish != null) {
				OrgPersonListActivity.onFinish.onSuccess(null);
				OrgPersonListActivity.onFinish = null;
			}
		} else {
			setResult(9);
		}
		finish();
	}
}
