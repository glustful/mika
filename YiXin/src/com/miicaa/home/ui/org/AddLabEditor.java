package com.miicaa.home.ui.org;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by LM on 14-6-12.
 */
public class AddLabEditor extends Activity {
    String lableId;
    EditText editText;
    Button backButton;
    Button saveButton;
    @Override
	protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intentGet = getIntent();
        final Bundle bundle = intentGet.getBundleExtra("bundle");

        setContentView(R.layout.lable_editor_add);
        backButton =(Button)findViewById(R.id.lable_editor_add_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveButton = (Button)findViewById(R.id.lable_editor_add_save_button);
        editText = (EditText)findViewById(R.id.lable_editor_add_text);
        if (bundle != null){
            if ("editor".equalsIgnoreCase(bundle.getString("edit"))) {
                String text = bundle.getString("lableText");
                if(text!=null){
                    text = text.substring(0,text.lastIndexOf("("));
                }
                editText.setText(text);
                lableId = bundle.getString("lableId");
            }
        }
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (editText.getText().length() >10 ){
                    Toast toast = Toast.makeText(AddLabEditor.this,"标签名称长度不能大于10",1);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    return;
                }
                if ("editor".equalsIgnoreCase(bundle.getString("edit"))){
                    editLable();
                }else if("add".equalsIgnoreCase(bundle.getString("edit"))){
                    addNewLable();
                }

            }
        });
    }

    private  void addNewLable(){
        String url = "/home/phone/thing/addlabel";
        new RequestAdpater(){
            @Override
            public void onReponse(ResponseData data) {
                if(data.getResultState() == ResponseData.ResultState.eSuccess){
                    Toast.makeText(AddLabEditor.this,"上传成功！",1).show();
                    setResult(RESULT_OK);
                    finish();
                }else {
                    Toast toast = Toast.makeText(AddLabEditor.this,"上传错误:"+data.getMsg(),1);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("label",editText.getText().toString())
                .notifyRequest();
    }

    private void editLable(){
        String url = "/home/phone/thing/editlabel";
        new RequestAdpater(){
            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess){
                    Toast.makeText(AddLabEditor.this,"上传成功！",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(AddLabEditor.this,"上传失败"+data.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("id",lableId)
                .addParam("label",editText.getText().toString())
                .notifyRequest();
    }
}
