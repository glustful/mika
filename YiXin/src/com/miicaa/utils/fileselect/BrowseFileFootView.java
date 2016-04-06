package com.miicaa.utils.fileselect;

import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.business.file.BusinessFileActivity;
import com.miicaa.home.ui.business.file.BusinessFileNORActivity;
import com.miicaa.home.ui.business.file.BusinessFileSearchActivity;
import com.miicaa.home.ui.picture.PhotoCheck_;

@EViewGroup(R.layout.browse_file_foot_view)
public class BrowseFileFootView extends LinearLayout{

	@ViewById(R.id.saveButton)
	Button saveBtn;
	@ViewById(R.id.editButton)
	Button editBtn;
	@ViewById(R.id.moveButton)
	Button moveBtn;
	@ViewById(R.id.delButton)
	Button delBtn;
//	
	String name;
	String mId;
	JSONObject json;
	Context context;
	
	
	
	
	public BrowseFileFootView(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
		this.context = context;
	}

	public BrowseFileFootView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public BrowseFileFootView(Context context) {
		super(context);
		this.context = context;
	}
	
	public void setFileInfo(String name ,String id){
		this.name = name != null ? name : "";
		this.mId = id != null ? name : "";
		
	}
	
	public void setFileInfo(boolean isManager,JSONObject json){
		this.json = json;
		this.name = json.optString("name");
		this.mId = json.optString("id");
		if(!isManager){
			String creator = json.optString("userCode");
			String code = AccountInfo.instance().getLastUserInfo().getCode();
			if(!creator.equals(code)){
				this.setVisibility(View.GONE);
				isManager = true;
			}else{
				this.setVisibility(View.VISIBLE);
			}
		}
	}
//	
	@AfterViews
	void afterView(){
		name = name != null ? name : "";
		mId = mId != null ? mId : "";
	}
	
	@Click(R.id.saveButton)
	void saveClick(){
		
	}
	@Click(R.id.editButton)
	void editClick(){
		
		String des = "";
		if (!json.isNull("describ")) {
			des = json.optString("describ");
		}

		Intent intent = new Intent(context, BusinessFileNORActivity.class);
		intent.putExtra("type", "describ");
		intent.putExtra("describ", des);
		intent.putExtra("id", json.optString("id"));
		intent.putExtra("rightType", json.optString("rightType"));
		JSONArray arr = json.optJSONArray("rightList");
		if(arr != null)
		intent.putExtra("json", arr.toString());
		((Activity) context).startActivityForResult(intent,
				BusinessFileActivity.EDITFILE_CODE);
		((Activity) context).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}
	@Click(R.id.moveButton)
	void moveClick(){
		Intent intent = new Intent(context, DirFileListActivity_.class);
		intent.putExtra("fileId", mId);
		((Activity) context).startActivityForResult(intent,BusinessFileActivity.MOVETOFILE_CODE);
		((Activity) context).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
	}
	@Click(R.id.delButton)
	void delClick(){
		new AlertDialog.Builder(context)
		.setTitle("删除")
		.setMessage("确认是否删除？")
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				secondRemove();
				
			}
		})
		.setNegativeButton("取消", null)
		.create()
		.show();
		

	}
	
	private void secondRemove(){
		String url = context.getString(R.string.file_remove_url);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", mId);
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				try {
					if (data.getResultState() == ResultState.eSuccess) {
						if(context instanceof PhotoCheck_){
							((PhotoCheck_)context).deleteItem();
						}else{
							BusinessFileActivity.getIntance().isRefresh();
							if(BusinessFileSearchActivity.getIntance() != null)
								BusinessFileSearchActivity.getIntance().isRefresh();
							((Activity)context).finish();
						}
						

					} else {
						Toast.makeText(context, data.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub

			}
		}.setUrl(url).addParam(map).notifyRequest();
	}

}
