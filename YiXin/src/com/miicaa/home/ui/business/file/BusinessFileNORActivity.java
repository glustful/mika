package com.miicaa.home.ui.business.file;

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
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.ui.pay.PayUtils;

public class BusinessFileNORActivity extends Activity implements OnClickListener {

	private  Runnable r = new Runnable() {
        @Override
		public void run() {
        	if(mToast != null)
            mToast.cancel();
        }
    };
    
    private Handler mhandler = new Handler();
    
    public void showToast(Context mContext, String text, int duration) {
        
        mhandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mhandler.postDelayed(r, duration);

        mToast.show();
    }
    
    Toast mToast;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_OK)
			return;
		if(requestCode != 1)
			return;
		int result = data.getIntExtra("success", 0);
		mCopyUsers.clear();
		json = "";
		if(result ==2){
			power.setText("公开");
			rightType = "00";
		}else if(result ==3){
			power.setText("仅自己");
			rightType = "10";
		}
		else if (result == 1) {
			power.setText(data.getStringExtra("result"));
			code = data.getStringExtra("code");
			json = data.getStringExtra("json");
			rightType = data.getStringExtra("rightType");
		} else if (result == 4) {
			rightType = "40";
			ArrayList<ContactViewShow> copyDatas = data
					.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
			if (copyDatas == null || copyDatas.size() == 0) {
				power.setText("");
				mCopyUsers.clear();
				
				return;
			}
			setCopyData(copyDatas);
		}
	}
	
	 private void setCopyData(ArrayList<ContactViewShow>data){
	        mCopyUsers.clear();
	        //String user = "";
	       // String conUser = "";
	       json = "[";
	        for (int i = 0; i < data.size(); i++){
	            mCopyUsers.add(new SamUser(data.get(i).getCode(),data.get(i).getName()));
	            json += "{\"code\":\""+data.get(i).getCode()+"\",\"name\":\""+data.get(i).getName()+"\"}";
	           // user = data.get(i).getName();
	            if (i < data.size()-1){
	               // user += ",";
	                json += ",";
	            }
	           // conUser = conUser + user;
	           // power.setText(conUser.toString());
	        }
	        json += "]";
	        if(data.size()>1){
	    		power.setText(data.get(0).getName()+"等"+data.size()+"个人");
	    		}else if(data.size()==1){
	    			power.setText(data.get(0).getName());
	    		}else{
	    			power.setText("");
	    		}
	    }

	Context mContext;
	Button cancel,commit;
	TextView title,totalCount,power,round;
	EditText edit,descEdit;
	String type = "new";
	String id = "";
	String ext = "";
	String name = "";
	String parentId ="";
	RelativeLayout power_layout;
	LinearLayout desc_layout;
	String rightType = "00";
	String json = "";
	String code = "";
	ArrayList<SamUser> mCopyUsers;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.business_file_new_rename);

		this.mContext = this;
		type = getIntent().getStringExtra("type");
		mCopyUsers = new ArrayList<SamUser>();
		initUI();

	}

	
	private void initUI() {
		desc_layout = (LinearLayout) findViewById(R.id.business_file_desc_layout);
		power_layout = (RelativeLayout) findViewById(R.id.business_file_desc_power);
		
		cancel = (Button) findViewById(R.id.business_file_new_cancel);
		commit = (Button) findViewById(R.id.business_file_new_commitButton);
		//commit.setEnabled(true);
		title = (TextView) findViewById(R.id.business_file_new_headTitle);
		edit = (EditText) findViewById(R.id.business_file_new_edit);
		descEdit = (EditText) findViewById(R.id.business_file_desc_edit);
		totalCount = (TextView) findViewById(R.id.business_file_desc_totalcount);
		power = (TextView) findViewById(R.id.business_file_desc_power_text);
		round = (TextView) findViewById(R.id.business_file_desc_power_round);
		if(type.equals("new")){
			title.setText("新建文件夹");
			edit.setHint("新建文件夹");
			
			parentId = getIntent().getStringExtra("parentId");
			desc_layout.setVisibility(View.GONE);
		}else if(type.equals("rename")){
			title.setText("重命名");
			ext = getIntent().getStringExtra("ext");
			edit.setText(getIntent().getStringExtra("name"));
			id = getIntent().getStringExtra("id");
			desc_layout.setVisibility(View.GONE);
		}else if(type.equals("describ")){
			title.setText("编辑文件");
			edit.setVisibility(View.GONE);
			id = getIntent().getStringExtra("id");
			String text = getIntent().getStringExtra("describ");
			descEdit.setText(text);
			totalCount.setText(text.length()+"");
			rightType = getIntent().getStringExtra("rightType");
			try{
			json = getIntent().getStringExtra("json");
			if(json== null)
				json = "[]";
			JSONArray arr = new JSONArray(json);
			String stmp = "";
			
			if(arr != null && arr.length()>0){
				
			for(int i=0;i<arr.length();i++){
				JSONObject tmp = arr.optJSONObject(i);
				if(rightType.equals("40")){
				SamUser user = new SamUser(tmp.optString("targetCode"), tmp.optString("targetName"));
				mCopyUsers.add(user);
				}
				if(!tmp.isNull("targetName") && stmp.equals("")){
					stmp = tmp.optString("targetName");
					
				}
				
			}
			if(rightType.equals("00") || rightType.equals("10") || arr.length()==1)
				power.setText(stmp);
			else if(rightType.equals("20")){
				power.setText(stmp+"等"+arr.length()+"个部门");
			}else if(rightType.equals("30")){
				power.setText(stmp+"等"+arr.length()+"个职位");;
			}else if(rightType.equals("40")){
				power.setText(stmp+"等"+arr.length()+"个人");
			}
			}else{
				power.setText("");
			}
			
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		cancel.setOnClickListener(this);
		commit.setOnClickListener(this);
		power_layout.setOnClickListener(this);
		descEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				totalCount.setText(s.length()+"");
				//commit.setEnabled(true);
			}
		});
	}


	@Override
	public void onClick(View v) {
		Utils.hiddenSoftBorad(mContext);
		switch(v.getId()){
		case R.id.business_file_new_cancel:
			finish();
			break;
		case R.id.business_file_new_commitButton:
			//commit.setEnabled(false);
			commit();
			break;
		case R.id.business_file_desc_power:
			//commit.setEnabled(true);
			Intent intent = new Intent(mContext, SelectRoundActivity_.class);
			intent.putExtra("rightType", rightType);
			intent.putExtra("json", json);
			intent.putExtra("name", power.getText().toString());
			Bundle bundle = new Bundle();
			ArrayList<String> name = new ArrayList<String>();
			ArrayList<String> code = new ArrayList<String>();
			if (mCopyUsers.size() > 0) {
				for (SamUser s : mCopyUsers) {
					name.add(s.getmName());
					code.add(s.getmCode());
				}
				bundle.putStringArrayList("name", name);
				bundle.putStringArrayList("code", code);
			}
			bundle.putString(SelectContacter.how,
					SelectContacter.ARRANGE);

			bundle.putString("contact", "arrange");
			intent.putExtra("bundle", bundle);
			((Activity) mContext).startActivityForResult(intent, 1);
			((Activity) mContext).overridePendingTransition(
					R.anim.my_slide_in_right, R.anim.my_slide_out_left);
			break;
		}
		
	}

	

	private void commit() {
		Utils.hiddenSoftBorad(mContext);
		if(type.equals("describ")){
			name = descEdit.getText().toString();
		}else{
		name = edit.getText().toString();
		if(name==null || name.equals("")){
			showToast(mContext, "文件名不能为空", 1000);
			
			return;
		}
		}
		if(type.equals("rename")){
			String url = mContext.getString(R.string.file_rename_url);
			HashMap<String,String> map = new HashMap<String, String>();
			if(!ext.equals("")){
				name += "." + ext;
			}
			map.put("id", id);
			map.put("name", name);
			commitParams(url, map);
		}else if(type.equals("new")){
			String url = mContext.getString(R.string.file_new_url);
			HashMap<String,String> map = new HashMap<String, String>();
			map.put("parentId", parentId);
			map.put("name", name);
			map.put("rightType", "00");
			commitParams(url, map);
		}else if(type.equals("describ")){
			String url = mContext.getString(R.string.file_edit_url);
			HashMap<String,String> map = new HashMap<String, String>();
			map.put("id", id);
			map.put("describ", name);
			map.put("rightType", rightType);
			if(!rightType.equals("10") && !rightType.equals("00")){
				if(json==null||json.trim().equals("")){
					PayUtils.showToast(mContext, "查看范围不能为空", 2000);
					return;
				}
			json = json.replaceAll("targetName", "name");
			json = json.replaceAll("targetCode", "code");
			
			map.put("json", json);
			}
			
			commitParams(url, map);
		}
		
	}
	ProgressDialog progressDialog;
	private void commitParams(String url,HashMap<String,String> map){
		String tmp = url+"?";
		for(String key :map.keySet()){
			tmp+= key+"="+map.get(key)+"&";
		}
		System.out.println(tmp);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("miicaa");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("正在提交，请稍后...");
		progressDialog.show();
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				progressDialog.dismiss();
				//commit.setEnabled(true);
				
				if(data.getResultState()==ResultState.eSuccess){
					if(type.equals("new")){
						setResult(Activity.RESULT_OK);
						finish();
					}else if(type.equals("rename")){
						Intent intent = new Intent();
						intent.putExtra("name", name);
						setResult(Activity.RESULT_OK,intent);
						finish();
					}else if(type.equals("describ")){
						Intent intent = new Intent();
						intent.putExtra("name", name);
						try {
							data.getJsonObject().put("rightType", rightType);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//System.out.println("edit="+data.getMRootData());
						intent.putExtra("json", data.getJsonObject().toString());
						setResult(Activity.RESULT_OK,intent);
						finish();
					}
				}else{
					showToast(mContext, "操作失败:"+data.getMsg(), 1000);
					
				}
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
				
			}
		}.addParam(map)
		.setUrl(url)
		.notifyRequest();
	}
	


}
