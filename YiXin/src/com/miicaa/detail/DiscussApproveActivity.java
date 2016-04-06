package com.miicaa.detail;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.ui.org.MatterHttp;
import com.miicaa.home.ui.org.MatterHttp.OnMatterResult;
import com.miicaa.home.ui.org.StyleDialog;
@EActivity(R.layout.discuss_approve)
public class DiscussApproveActivity extends Activity{

	final static int ADD_APPROVE = 1;
	final static int ADD_COPY = 2;
	final static String AGREE = "2";
	final static String DISAGREE = "1";
	final static String PASS = "5";
	final static String MISS = "6";
	 ArrayList<PersonInfoView> apprInfos;
	 ArrayList<PersonInfoView> copyInfos;
	 String content;
	 StyleDialog dialog;
	@Extra
	String status;
	@Extra
	String groupid;
	@Extra
	String dataid;
	@Extra 
	boolean isProcess;
@ViewById(R.id.writeCancle)
Button cancle;
@ViewById(R.id.writeTitle)
TextView title;
@ViewById(R.id.progressEdit)
EditText contentEdit;
@ViewById(R.id.writeCommit)
Button commit;
@ViewById(R.id.addLayout)
LinearLayout addNextApprove;
@ViewById(R.id.copyLayout)
LinearLayout copyApprove;
@ViewById(R.id.approvers)
TextView approver;
@ViewById(R.id.copyers)
TextView copyer;
@ViewById(R.id.totalcount)
TextView totalCount;

@AfterInject
void afterInject(){
	dialog = new StyleDialog(this);
	dialog.setCanceledOnTouchOutside(false);
	apprInfos = new ArrayList<PersonInfoView>();
	copyInfos = new ArrayList<PersonInfoView>();
}
@SuppressLint("NewApi")
@AfterViews
void afterViews(){
	InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(140);
    contentEdit.setFilters(new InputFilter[]{lengthFilter});
	if(AGREE.equals(status)){
		if(groupid == null || "".equals(groupid.trim())){
			addNextApprove.setVisibility(View.VISIBLE);
		}else{
			addNextApprove.setVisibility(View.GONE);
		}
		String str = "审批同意";
		title.setText(str);
		contentEdit.append(str);
	}else if(DISAGREE.equals(status)){
		addNextApprove.setVisibility(View.GONE);
		copyApprove.setVisibility(View.VISIBLE);
		copyApprove.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_all_selector));
		String str = "审批不同意";
		title.setText(str);
		contentEdit.append(str);
	}else if(PASS.equals(status)){
		addNextApprove.setVisibility(View.VISIBLE);
		String str = "会签通过";
		title.setText(str);
		contentEdit.append(str);
	}else if(MISS.equals(status)){
		addNextApprove.setVisibility(View.GONE);
		copyApprove.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_all_selector));
		String str = "会签不通过";
		title.setText(str);
		contentEdit.setText(str);
	}else if("".equals(status)){
//		contentEdit.setVisibility(View.GONE);
	}
	
	if(isProcess){
		addNextApprove.setVisibility(View.GONE);
	}
}

@Click(R.id.addLayout)
void addClick(){
	 Intent intent = new Intent(DiscussApproveActivity.this, SelectContacter.class);
     Bundle bundle = new Bundle();
     ArrayList<String> name = new ArrayList<String>();
     ArrayList<String> code = new ArrayList<String>();
     if (apprInfos.size() > 0) {
         for (PersonInfoView s : apprInfos) {
             name.add(s.mName);
             code.add(s.mCode);
         }
         bundle.putStringArrayList("name",name);
         bundle.putStringArrayList("code",code);
     }
     bundle.putString(SelectContacter.how, SelectContacter.APPROVE);
     intent.putExtra("bundle",bundle);
     startActivityForResult(intent,ADD_APPROVE);
 
}

@Click(R.id.writeCancle)
void cancle(){
	finish();
}

@Click(R.id.copyLayout)
void copyClick(){
	  Intent intent = new Intent(DiscussApproveActivity.this, SelectContacter.class);
      Bundle bundle = new Bundle();
      ArrayList<String> name = new ArrayList<String>();
      ArrayList<String> code = new ArrayList<String>();
      if (copyInfos.size() > 0) {
          for (PersonInfoView s : copyInfos) {
              name.add(s.mName);
              code.add(s.mCode);
          }
          bundle.putStringArrayList("name",name);
          bundle.putStringArrayList("code",code);
         
      }
      bundle.putString(SelectContacter.how, SelectContacter.COPY);
      intent.putExtra("bundle",bundle);
      startActivityForResult(intent,ADD_COPY);

  }

@Click(R.id.writeCommit)
void commit(){
	
	content = contentEdit.getText().toString();
	if((content == null || "".equals(content.trim()))&&contentEdit.isShown()){
		Toast.makeText(this, "内容不能为空！", Toast.LENGTH_SHORT).show();
		return;
	}
	dialog.show();
	String addApprove = getUserDto(apprInfos);
	String addCopy = getUserDto(copyInfos);
	MatterHttp.requestApprove(new OnMatterResult() {
		
		@Override
		public void onSuccess(String msg, Object obj) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			finish();
			if(MatterDetailAcrtivity.getInstance() != null){
		    MatterDetailAcrtivity.getInstance().iWasComplete();
			MatterDetailAcrtivity.getInstance().refreshthis();
			}
			
//			FramMainActivity.instance.refushMatterFrame();
//			Intent i = new Intent(DiscussApproveActivity.this, FramMainActivity.class);
//			startActivity(i);
			
		}
		
		@Override
		public void onFailure(String msg) {
			// TODO Auto-generated method stub
			Toast.makeText(DiscussApproveActivity.this, "网速不给力，未上传成功,稍后再试", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
		}
	}, dataid, content, status, addApprove, addCopy);
}

@TextChange(R.id.progressEdit)
void onTextChangesOnHelloTextView(CharSequence text, TextView hello, int before, int start, int count) {
    // Something Here
	totalCount.setText(String.valueOf(contentEdit.getText().toString().length()));
 }

@OnActivityResult(ADD_APPROVE)
void onaddApprove(int resultCode,Intent data){
	if(RESULT_OK == resultCode){
	addApprove(data, ADD_APPROVE);
	approver.setText(viewString(apprInfos));
	}
}
@OnActivityResult(ADD_COPY)
void onaddCopy(int resultCode, Intent data){
	if(RESULT_OK == resultCode){
	addApprove(data, ADD_COPY);
	copyer.setText(viewString(copyInfos));
	}
}






//老版本搬过来的代码。垃圾代码
//存着下一审批人和抄送人的信息
class PersonInfoView {
    View mView = null;
    String mName;
    String mCode;
    public PersonInfoView(String name, String code){
    	 mName = name;
         mCode = code;
    	}
    
    
    
}

void addApprove(Intent data,final int add){
	ArrayList<ContactViewShow> appDatas = data.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
    if (appDatas != null && appDatas.size() > 0){
        ContactUtil.setSelectPeopleData(appDatas,new ContactUtil.PeopleCallbackListener() {
            @Override
            public void callBack(ArrayList<SamUser> data, String user) {
                for (SamUser s : data){
                    PersonInfoView personInfoView = new PersonInfoView(s.getmName(),s.getmCode());
                    if(ADD_APPROVE == add){
                    apprInfos.add(personInfoView);
                    }else if(ADD_COPY == add){
                    	copyInfos.add(personInfoView);
                    }
                }
            }
        });
        }
    }

@SuppressWarnings("unused")
 String getUserDto(ArrayList<PersonInfoView> ccArray) {
    if (ccArray == null || ccArray.size() == 0)
        return "";
    JSONArray array = new JSONArray();
    for (int i = 0; i < ccArray.size(); i++) {
        JSONObject jPerson = new JSONObject();
        try {
            jPerson.put("value", ccArray.get(i).mCode);
            jPerson.put("name", ccArray.get(i).mName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(jPerson);
    }


    return array.toString();
}

void todoCommit(){
	
}


String viewString(ArrayList<PersonInfoView> infos){
	String str = "";
	for(PersonInfoView i : infos){
		str += i.mName+",";
	}
	if(str.length() > 0)
	str = str.substring(0, str.length()-1);
	return str;
}

}
