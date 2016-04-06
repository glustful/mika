package com.miicaa.home.ui.person;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.miicaa.common.base.MatterRequest;
import com.miicaa.common.base.MatterRequest.MatterHomeCallBackListener;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.person.PersonDepartEdit.OnCheckChangeListener;

//@EFragment(R.layout.home_fram_matter_fragment)
@EFragment(R.layout.person_edit_depart)
public class PersonDepartEditFragmetn extends Fragment {

	

	public ProgressDialog progressDialog;
	private PersonDepartEdit activity;

	private String url = "";

	@AfterInject
	void afterInject() {

		
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle("miicaa");
		progressDialog.setCanceledOnTouchOutside(false);
		/*
		 * 初始化请求参数
		 */
		this.activity = (PersonDepartEdit) getActivity();
		Bundle bundle = getArguments();
		url = bundle.getString("url");
		String type = bundle.getString("type");
		if(type.equals("unit")){
			paramMap = new HashMap<String, String>();
			paramMap.put("unitCode", bundle.getString("code"));
		}else{
			paramMap = new HashMap<String, String>();
			paramMap.put("groupCode", bundle.getString("code"));
		}
		

		requestFirst();

	}

	@ViewById(R.id.person_depart_lvListView)
	ListView listView;

	
	
	void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
		TreeElement element = this.jsonObjects.get(position);
		if (!element.isHasChild()) {
			element.setChecked(!element.isChecked());
			((CheckBox)((LinearLayout)view).getChildAt(1)).setChecked(element.isChecked());
			return;
		}

		if (element.isExpanded()) {
			element.setExpanded(false);

			ArrayList<TreeElement> temp = new ArrayList<TreeElement>();

			for (int i = position + 1; i < this.jsonObjects.size(); i++) {
				if (element.getLevel() >= this.jsonObjects.get(i).getLevel()) {
					break;
				}
				temp.add(this.jsonObjects.get(i));
			}
			this.jsonObjects.removeAll(temp);
			adapter.notifyDataSetChanged();
		} else {
			if(element.getChildList().size()<1){
				this.activity.intoNextLevel(element.getValue());
				return;
			}
			element.setExpanded(true);
			int level = element.getLevel();
			int nextLevel = level + 1;

			ArrayList<TreeElement> tempList = element.getChildList();

			for (int i = tempList.size() - 1; i > -1; i--) {
				TreeElement element1 = tempList.get(i);
				element1.setLevel(nextLevel);
				element1.setExpanded(false);
				this.jsonObjects.add(position + 1, element1);
			}
			adapter.notifyDataSetChanged();
		}

	}

	@AfterViews
	void initData() {

		adapter = new PersonEditAdapter(this.getActivity(), jsonObjects,listener);
		initViews();
	}

	HashMap<String,String> paramMap;
	PersonEditAdapter adapter;
	ArrayList<TreeElement> jsonObjects = new ArrayList<TreeElement>();// 所有要解析的数据

	private OnCheckChangeListener listener = new OnCheckChangeListener() {
		
		@Override
		public void onCheckChanged(TreeElement element,boolean isCheck) {
			if(isCheck){
				activity.setViewGroup(element);
			}else{
				activity.removeViewGroup(element);
			}
			
		}
	};
	
	void initViews() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListItemClick(parent,view,position,id);
				
			}
		});
		listView.setFastScrollEnabled(false);
		listView.setFadingEdgeLength(0);
		listView.setAdapter(adapter);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void requestData(HashMap<String, String> map) {// 请求数据
		
		MatterRequest.requestMatterHome(url, map,
				new MatterHomeCallBackListener() {

					@Override
					public void callBack(ResponseData data) {
						progressDialog.dismiss();
						// TODO Auto-generated method stub
						if (data.getResultState() == ResultState.eSuccess) {
							
							callBackInRequest(data.getJsonArray());

						} else {
							Toast.makeText(getActivity(),
									"网络错误:" + data.getMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

	}

	void showWhat0(int code, String msg) {
		if (code == -1) {
			Toast.makeText(getActivity(), "没有数据！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), "网络错误！请稍后再试！" + msg,
					Toast.LENGTH_SHORT).show();
		}

	}

	/*
	 * 刷新啊，什么的就用这个来获取数据吧
	 */
	void requestOut() {
		resetList();
		showRefresh();
		requestData(paramMap);
	}

	// 数据解析
	void callBackInRequest(JSONArray workList) {

		if (workList != null && workList.length() > 0) {
			
			ArrayList<TreeElement> elements = new ArrayList<TreeElement>();
			try {
				for (int j = 0; j < workList.length(); j++) {
					JSONObject obj = workList.optJSONObject(j);
					String id = obj.getString("id");
					String code = obj.getString("code");
					String hasSub = obj.getString("hasSub");
					String caption = obj.getString("name");
					JSONArray units = obj.getJSONArray("units");
					boolean isHasChild = (hasSub == null || hasSub
							.equals("null")) ? (units == null || units.length() < 1) ? false
							: true
							: hasSub.equals("1") ? true : false;

					TreeElement element = new TreeElement(id, caption, code,
							isHasChild, false);
					for(TreeElement e:this.activity.trees){
						if(e.getValue().equals(code)){
							element.setChecked(true);
						}
					}
					if (units != null) {
						for (int i = 0; i < units.length(); i++) {
							JSONObject cobj = units.optJSONObject(i);
							String cid = cobj.getString("id");
							String ccode = cobj.getString("code");
							String chasSub = cobj.getString("hasSub");
							String ccaption = cobj.getString("name");
							JSONArray cunits = cobj.getJSONArray("units");
							boolean cisHasChild = (chasSub == null || chasSub
									.equals("null")) ? (cunits == null || cunits
									.length() < 1) ? false : true : chasSub
									.equals("1") ? true : false;
							TreeElement celement = new TreeElement(cid,
									ccaption, ccode, cisHasChild, false);
							for(TreeElement e:this.activity.trees){
								if(e.getValue().equals(ccode)){
									celement.setChecked(true);
								}
							}
							element.addChild(celement);

						}
					}

					elements.add(element);
					if (j == 0 && element.isHasChild()) {
						element.setExpanded(true);
						int level = element.getLevel();
						int nextLevel = level + 1;

						ArrayList<TreeElement> tempList = element
								.getChildList();

						for (int i = tempList.size() - 1; i > -1; i--) {
							TreeElement element1 = tempList.get(i);
							element1.setLevel(nextLevel);
							element1.setExpanded(false);
							elements.add(1, element1);
						}
					}
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			adapter.refresh(elements);
		} else {
			Toast.makeText(getActivity(), "没有数据！", Toast.LENGTH_SHORT).show();
		}

		// Toast.makeText(getActivity(),"没有数据啦！",1).show();
		// TODO:失败了要弹通知

		// matterCell.refresh(jsonObjects);

	}

	

	// 如果遇到刷新则要删除所有list数据
	void resetList() {
		jsonObjects.clear();

	}
	
	public void clean(ArrayList<TreeElement> result){
		if(result == null)
			return;
		for(TreeElement e:result){
			e.setChecked(false);
			if(e.isHasChild()){
				clean(e.getChildList());
			}
		}
	}
	

	public void refresh(String string) {
		for(TreeElement e:jsonObjects){
			if(e.getValue().equals(string)){
				e.setChecked(false);
				break;
			}
			if(e.isHasChild()){
				ArrayList<TreeElement> child = e.getChildList();
				
				if(child == null){
					break;
				}
				for(TreeElement ce:child){
					if(ce.getValue().equals(string)){
						ce.setChecked(false);
						break;
					}
				}
			}
			
		}
		if(this.isVisible()){
			this.adapter.notifyDataSetChanged();
		}
		
	}

	void showRefresh() {
		progressDialog.setMessage("正在刷新，请稍后...");
		progressDialog.show();
	}

	/*
	 * 第一次启动时
	 */
	void requestFirst() {
		resetList();
		showRefresh();
		requestData(paramMap);
	}

}
