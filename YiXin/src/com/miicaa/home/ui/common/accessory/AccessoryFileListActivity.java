package com.miicaa.home.ui.common.accessory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.utils.fileselect.BrowseFileActivity_;

/**
 * Created by apple on 13-12-30.
 */
public class AccessoryFileListActivity extends Activity {
    private Context mContext;
    private ArrayList<HashMap<String, Object>> processData;
    private LinearLayout contentLayout;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accessory_file_list_activity);
        contentLayout = (LinearLayout) findViewById(R.id.accessory_file_list_activity_content_layout);
        mContext = AccessoryFileListActivity.this;
        Bundle bundle = getIntent().getBundleExtra("bundle");
        mId = bundle.getString("id");
        getAttachement();
        Button returnButton = (Button) findViewById(R.id.accessory_file_list_activity_back_button);
        returnButton.setOnClickListener(returnButtonClick);
    }

    private void dataCtreat(JSONArray jsonArray) {
        processData = new ArrayList<HashMap<String, Object>>();
            if(jsonArray == null || jsonArray.length() == 0){
                return;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, Object> dataHashMap = new HashMap<String, Object>();
                AccessoryInfo info = new AccessoryInfo();
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                info.setTitle(jsonObject.optString("title"));
                info.setExt(jsonObject.optString("ext"));
                info.setFileId(jsonObject.optString("fileId"));
                if ("png".equalsIgnoreCase(info.getExt())||"jpg".equalsIgnoreCase(info.getExt())||"gif".equalsIgnoreCase(info.getExt())||"bmp".equalsIgnoreCase(info.getExt())) {
                    continue;
                }
                dataHashMap.put("name", info.getTitle().toString() + "." + info.getExt());
                dataHashMap.put("fileType", info.getExt());
                dataHashMap.put("fileId", info.getFileId());
                processData.add(dataHashMap);
            }
            viewCtreat();

    }

    private void viewCtreat() {
        contentLayout.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < processData.size(); i++) {
            HashMap<String, Object> dataHashMap = processData.get(i);
            View itemView = layoutInflater.inflate(R.layout.accessory_file_list_cell, null);
            TextView nameTextView = (TextView) itemView.findViewById(R.id.accessory_file_list_cell_name_textview);
            nameTextView.setText((String) dataHashMap.get("name"));
            String fileTypeString = (String) dataHashMap.get("fileType");
            if(fileTypeString != null){
                fileTypeString = fileTypeString.toLowerCase();
            }else{
                fileTypeString = "";
            }
            ImageView icoImage = (ImageView) itemView
                    .findViewById(R.id.accessory_file_list_cell_file_image);
            if (fileTypeString.equals("xlsx") || fileTypeString.equals("xls")) {
                icoImage.setImageDrawable(mContext.getResources().getDrawable(
                        R.drawable.accessory_file_ico_execl));
            } else if (fileTypeString.equals("docx") || fileTypeString.equals("doc")) {
                icoImage.setImageDrawable(mContext.getResources().getDrawable(
                        R.drawable.accessory_file_ico_word));
            } else if (fileTypeString.equals("pptx") || fileTypeString.equals("ppt")) {
                icoImage.setImageDrawable(mContext.getResources().getDrawable(
                        R.drawable.accessory_file_ico_ppt));
            } else if (fileTypeString.equals("pdf")) {
                icoImage.setImageDrawable(mContext.getResources().getDrawable(
                        R.drawable.accessory_file_ico_pdf));
            } else if (fileTypeString.equals("rar")||fileTypeString.equals("zip")) {
                icoImage.setImageDrawable(mContext.getResources().getDrawable(
                        R.drawable.accessory_file_ico_rar));
            } else if (fileTypeString.equals("txt")||fileTypeString.equals("log")) {
                icoImage.setImageDrawable(mContext.getResources().getDrawable(
                        R.drawable.accessory_file_ico_txt));
            } else {
                icoImage.setImageDrawable(mContext.getResources().getDrawable(
                        R.drawable.accessory_file_ico_normal));
            }

            Button cellButton = (Button) itemView.findViewById(R.id.accessory_file_list_cell_button);
            cellButton.setTag(String.valueOf(i));
            cellButton.setOnClickListener(cellButtonClick);

            contentLayout.addView(itemView);
        }
    }

    public void getAttachement(){
        String url = "/home/phone/thing/getsingleattach";//附件的网络位置

        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {

                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                   dataCtreat(data.getJsonArray());
                } else {

                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {
            }
        }.setUrl(url)
                .addParam("dataId",mId)
                .notifyRequest();
    }

    View.OnClickListener cellButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        	
        	
            int index = Integer.parseInt(view.getTag().toString());
            HashMap<String, Object> dataHashMap = processData.get(index);
            
            String fileId = (String)dataHashMap.get("fileId");
            String fileName = (String)dataHashMap.get("name");
            BrowseFileActivity_.intent(mContext)
            .name(fileName)
            .mId(fileId)
            .start();
            
//            String fileString = Environment.getExternalStorageDirectory()+"/miicaa/download/";
//            fileString += dataHashMap.get("name");
//            File file = new File(fileString);
//            final File downFile = file;
//            new RequestAdpater() {
//                @Override
//                public void onReponse(ResponseData data) {
//                    if(data.getResultState() == ResponseData.ResultState.eSuccess){
//                        Intent intent = new Intent("android.intent.action.VIEW");
//                        intent.addCategory("android.intent.category.DEFAULT");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        Uri uri = Uri.fromFile(downFile);
//                        intent.setDataAndType(uri, "application/*");
//                        startActivityForResult(intent,0);
//                    }else{
//                        Toast.makeText(AccessoryFileListActivity.this,"文件下载失败。",1).show();
//                    }
//                }
//
//                @Override
//                public void onProgress(ProgressMessage msg) {
//
//                }
//            }.setUrl("/home/proupload/pc/component/upload/download")
//                    .setRequestMethod(RequestAdpater.RequestMethod.eGet)
//                    .setRequestType(RequestAdpater.RequestType.eFileDown)
//                    .addParam("id", fileId)
//                    .setLocalDir(UserAccount.getLocalDir("download/"))
//                    .setFileName(fileName)
//                    .notifyRequest();

//            if(!file.exists()){
//                Toast.makeText(AccessoryFileListActivity.this,"找不到文件。",Toast.LENGTH_SHORT).show();
//                return;
//            }

        }
    };

    View.OnClickListener returnButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
    public static class OpenFiles {
        //android获取一个用于打开HTML文件的intent
        public static Intent getHtmlFileIntent(File file)
        {
            Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uri, "text/html");
            return intent;
        }
        //android获取一个用于打开图片文件的intent
        public static Intent getImageFileIntent(File file)
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "image/*");
            return intent;
        }
        //android获取一个用于打开PDF文件的intent
        public static Intent getPdfFileIntent(File file)
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            return intent;
        }
        //android获取一个用于打开文本文件的intent
        public static Intent getTextFileIntent(File file)
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "text/plain");
            return intent;
        }

        //android获取一个用于打开音频文件的intent
        public static Intent getAudioFileIntent(File file)
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "audio/*");
            return intent;
        }
        //android获取一个用于打开视频文件的intent
        public static Intent getVideoFileIntent(File file)
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "video/*");
            return intent;
        }


        //android获取一个用于打开CHM文件的intent
        public static Intent getChmFileIntent(File file)
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/x-chm");
            return intent;
        }


        //android获取一个用于打开Word文件的intent
        public static Intent getWordFileIntent(File file)
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/msword");
            return intent;
        }
        //android获取一个用于打开Excel文件的intent
        public static Intent getExcelFileIntent(File file)
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            return intent;
        }
        //android获取一个用于打开PPT文件的intent
        public static Intent getPPTFileIntent(File file)
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            return intent;
        }
        //android获取一个用于打开apk文件的intent
        public static Intent getApkFileIntent(File file)
        {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),  "application/vnd.android.package-archive");
            return intent;
        }


    }
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }
}


