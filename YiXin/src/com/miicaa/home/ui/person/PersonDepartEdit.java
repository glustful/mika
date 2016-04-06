package com.miicaa.home.ui.person;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.MatterRequest;
import com.miicaa.common.base.MatterRequest.MatterHomeCallBackListener;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.org.LableGroup;

public class PersonDepartEdit extends FragmentActivity implements
		OnClickListener {
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private LableGroup lableGroup;
	private Context mContext;
	private String rightType = "";
	private TextView title;
	public ProgressDialog progressDialog;
	private Bundle bundle;
	private ArrayList<View> views = new ArrayList<View>();
	ArrayList<TreeElement> trees = new ArrayList<TreeElement>();
	private HashMap<String, String> param;
	private String type = "unit";
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);

		setContentView(R.layout.person_edit_main);
		bundle = getIntent().getBundleExtra("bundle");
		this.mContext = this;
		rightType = getIntent().getStringExtra("rightType");
		if (rightType == null || rightType.equals(""))
			rightType = null;
		initButton();
		initParam();
		initFragment();
	}

	private void initParam() {
		param = new HashMap<String, String>();
		if (rightType == null) {
			param.put("userId", bundle.getString("userId"));
			param.put("userCode", bundle.getString("userCode"));
		}
		String ctrees = bundle.getString("data");

		if (ctrees == null) {

			return;
		}
		try {
			JSONArray tmp = new JSONArray(ctrees);
			if (tmp != null) {
				for (int i = 0; i < tmp.length(); i++) {
					JSONObject jDepart = tmp.optJSONObject(i);
					String id = jDepart.optString("id");
					String name = jDepart
							.optString((rightType == null) ? "name"
									: "targetName");
					String code = jDepart
							.optString((rightType == null) ? "code"
									: "targetCode");
					TreeElement e = new TreeElement(id, name, code);
					setViewGroup(e);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initButton() {
		findViewById(R.id.person_depart_back).setOnClickListener(this);
		findViewById(R.id.person_depart_commit).setOnClickListener(this);
		lableGroup = (LableGroup) findViewById(R.id.person_list_view_group);
		title = (TextView) findViewById(R.id.person_depart_title);
		type = bundle.getString("type");
		if (type != null && type.equals("unit")) {
			title.setText("选择部门");
		} else {
			title.setText("选择职位");
		}
	}

	private void initFragment() {

		Fragment f = Fragment.instantiate(this,
				PersonDepartEditFragmetn_.class.getName(), bundle);
		fragments.add(f);

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.person_fragment_main, f);

		ft.commit();

	}

	private void changeFragment() {
		Fragment f = Fragment.instantiate(this,
				PersonDepartEditFragmetn_.class.getName(), bundle);
		fragments.add(f);

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.my_slide_in_right,
				R.anim.my_slide_out_left);
		ft.replace(R.id.person_fragment_main, f);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}

	public void setViewGroup(TreeElement element) {
		for (View v : views) {
			if (v.getTag().toString().equals(element.getValue()))
				return;
		}
		LinearLayout linearLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 5, 0, 5);
		linearLayout.setLayoutParams(params);
		linearLayout.setMinimumHeight(40);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		RelativeLayout sRelativeLayout = new RelativeLayout(mContext);
		ViewGroup.LayoutParams rParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		sRelativeLayout.setBackgroundColor(mContext.getResources().getColor(
				R.color.labelbg));
		sRelativeLayout.setLayoutParams(rParams);
		;
		TextView textView = new TextView(mContext);
		RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		tParams.setMargins(5, 5, 5, 0);
		tParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		textView.setLayoutParams(tParams);
		textView.setText(element.getCaption());
		sRelativeLayout.addView(textView);
		RelativeLayout relativeLayout = new RelativeLayout(mContext);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(20,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		linearLayout.addView(sRelativeLayout);
		linearLayout.addView(relativeLayout);
		linearLayout.setTag(element.getValue());
		linearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeViewGroup(v);
			}
		});
		views.add(linearLayout);

		lableGroup.addView(linearLayout);
		((View)lableGroup.getParent()).setPadding(10, 10,10, 10);
		trees.add(element);

	}

	public void removeViewGroup(TreeElement element) {
		View view = null;
		int index = 0;
		for (View v : views) {
			if (v.getTag().toString().equals(element.getValue())) {
				view = v;
				index = views.indexOf(v);
				break;
			}
		}

		if (view != null) {
			views.remove(index);
			trees.remove(index);
			lableGroup.removeView(view);
		}
		if(lableGroup.getChildCount()==0){
			((View)lableGroup.getParent()).setPadding(0, 0,0, 0);
		}
	}

	public void removeViewGroup(View view) {

		if (view != null) {
			int index = views.indexOf(view);
			if (index == -1)
				return;
			views.remove(index);
			trees.remove(index);
			lableGroup.removeView(view);
			for (Fragment f : fragments) {
				if (f instanceof PersonDepartEditFragmetn_) {
					((PersonDepartEditFragmetn_) f).refresh(view.getTag()
							.toString());
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_depart_back:
			onBackPressed();
			break;
		case R.id.person_depart_commit:
			if (trees == null || trees.size() < 1) {
				Toast.makeText(this, "请选择后再提交", Toast.LENGTH_SHORT).show();
				return;
			}
			String s = "";
			String name = "";
			String json = "[";
			for (TreeElement e : trees) {
				json += "{\"id\":\"" + e.getId() + "\",\"targetName\":\""
						+ e.getCaption() + "\",\"targetCode\":\""
						+ e.getValue() + "\"},";
				s += e.getValue() + ",";
				//name += e.getCaption() + ",";
			}
			s = s.substring(0, s.length() - 1);
			//name = name.substring(0, name.length() - 1);

			if (rightType != null) {
				if (trees.size() > 1) {
					if(rightType.equals("20")){
					name = (trees.get(0).getCaption() + "等" + trees.size()
							+ "部门");
					}
					else if(rightType.equals("30")){
						name = (trees.get(0).getCaption() + "等" + trees.size()
								+ "职位");
					}
				} else if (trees.size() == 1) {
					name = (trees.get(0).getCaption());
				} 
				json = json.substring(0, json.length() - 1);
				json += "]";
				Intent intent = new Intent();
				intent.putExtra("result", name);
				intent.putExtra("code", s);
				intent.putExtra("json", json);
				intent.putExtra("rightType", rightType);
				setResult(Activity.RESULT_OK, intent);
				finish();
				return;
			}
			progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("miicaa");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage("正在提交，请稍后...");
			progressDialog.show();

			if (type.equals("unit")) {
				param.put("unit", s);
			} else {
				param.put("group", s);
			}

			postData(param);
			break;
		}

	}

	public void intoNextLevel(String value) {
		bundle.putString("code", value);
		changeFragment();
	}

	public void postData(HashMap<String, String> map) {// 请求数据
		String url = "/home/phone/personcenter/edituserinfo";
		MatterRequest.requestMatterHome(url, map,
				new MatterHomeCallBackListener() {

					@Override
					public void callBack(ResponseData data) {
						progressDialog.dismiss();
						// TODO Auto-generated method stub
						if (data.getResultState() == ResultState.eSuccess) {

							clean();

						} else {
							Toast.makeText(PersonDepartEdit.this,
									"网络错误:" + data.getMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

	}

	protected void clean() {
		views.clear();
		trees.clear();
		lableGroup.removeAllViews();
		for (Fragment f : fragments) {
			if (f instanceof PersonDepartEditFragmetn_) {
				PersonDepartEditFragmetn_ p = ((PersonDepartEditFragmetn_) f);
				p.clean(p.jsonObjects);
			}
		}
		fragments.clear();
		this.finish();
	}

	public interface OnCheckChangeListener {
		public void onCheckChanged(TreeElement name, boolean isCheck);
	}
}
