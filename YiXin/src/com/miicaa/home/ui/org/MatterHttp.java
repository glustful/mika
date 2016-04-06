package com.miicaa.home.ui.org;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.matter.CommentInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.home.MatterCell;
import com.miicaa.home.ui.widget.UploadWidget;
import com.miicaa.utils.AllUtils;

/**
 * Created by Administrator on 14-1-16.
 */
public class MatterHttp

{

    public  static int i = 0 ;
//    private static StyleDialog progressDialog;
//    private static Context mContext;
    public MatterHttp(Context context){
//        mContext =context;
//       progressDialog = AllUtils.getMaualStyleDialog(context);
    }
    public static Boolean requestArrageCreate(final OnMatterResult onResult,
                                              final ArrayList<String> fName,
                                              final HashMap<String, String> map,
                                              String todoUser,
                                              String ccUser,
                                              String repeatFlag)

    {
    	Log.d("MatterHttp", "requestArrageCreate param:"+map);
    	final ArrayList<String> fileName = new ArrayList<String>();
    	fileName.addAll(fName);
//    	if(progressDialog == null){
//    		progressDialog = AllUtils.getMaualStyleDialog(context);
//    	}
//        progressDialog.show();
        if(onResult == null)
            return false;
        System.out.println(map);
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                if(data.getResultState() == ResponseData.ResultState.eSuccess)
                {
                    JSONObject jsonObject = data.getMRootData();
                        String infoId = jsonObject.optString("data");
                    if(infoId != null&&fileName.size() != 0){
                        uploadAtt(onResult,fileName,"arrange",infoId);
                        return;
                    }
                    onResult.onSuccess("成功",null);

                }
                else
                {
                    onResult.onFailure("失败");
//                    progressDialog.dismiss();
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl("/home/phone/thing/addarrange")
                .addParam("clientCode", AccountInfo.instance().getClientId())
                .addParam(map)
                .addParam("todoUsersSet", todoUser)
                .addParam("copyUsersSet", ccUser)
                .addParam("repeatFlag", repeatFlag)
                .notifyRequest();

    }

    public static   Boolean requestApproveCreate(final OnMatterResult onResult,
                                                final  ArrayList<String> fileName,
                                               final HashMap<String, String> map,
                                               String todoUser,
                                               String ccUser)
    {
//        progressDialog.show();
        if(onResult == null)
            return false;
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                if(data.getResultState() == ResponseData.ResultState.eSuccess)
                {
                    JSONObject jsonObject = data.getMRootData();
                    String infoId = jsonObject.optString("data");
                    if(infoId != null&&fileName.size() != 0){
                        uploadAtt(onResult,fileName,"approval",infoId);
                        return;
                    }
                    onResult.onSuccess("成功",null);

                }
                else
                {
                    onResult.onFailure("失败");
//                    progressDialog.dismiss();
                }

            }

            @Override
            public void onProgress(ProgressMessage msg) {
                //onResponse.onProgress(msg);
            }
        }.setUrl("/home/phone/thing/addapprove")
                .addParam("clientCode", AccountInfo.instance().getClientId())
                .addParam(map)
                .addParam("todoUsersSet", todoUser)
                .addParam("copyUsersSet", ccUser)
                .notifyRequest();
    }

    public static Boolean requestApproveEditor(final OnMatterResult onResult,
                                               final  ArrayList<String> fileName,
                                               final HashMap<String, String> map,
                                               String todoUser,
                                               String ccUser){
//        progressDialog.show();
        if(onResult == null)
            return false;
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                if(data.getResultState() == ResponseData.ResultState.eSuccess)
                {
                    JSONObject jsonObject = data.getMRootData();
                    String infoId = jsonObject.optString("data");
                    if(infoId != null&&fileName.size() != 0){
                        uploadAtt(onResult,fileName,"approval",infoId);
                        return;
                    }
                    onResult.onSuccess("成功",null);
//                    if (progressDialog != null){
////                        progressDialog.dismiss();
//                    }
                }
                else
                {

//                    progressDialog.dismiss();
                    onResult.onFailure("失败");
                }

            }

            @Override
            public void onProgress(ProgressMessage msg) {
                //onResponse.onProgress(msg);
            }
        }.setUrl("/home/phone/thing/updateapprove")
                .addParam("clientCode", AccountInfo.instance().getClientId())
                .addParam(map)
                .addParam("todoUsersSet", todoUser)
                .addParam("copyUsersSet", ccUser)
                .notifyRequest();

    }

    public static Boolean requestArrageEditor(final OnMatterResult onResult,
                                              final ArrayList<String> fileName,
                                              final HashMap<String, String> map,
                                              String todoUser,
                                              String ccUser,
                                              String repeatFlag)
    {
        if(onResult == null)
            return false;
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
//                progressDialog.show();
                //onResponse.onResponse(data);
                if(data.getResultState() == ResponseData.ResultState.eSuccess)
                {
                    JSONObject jsonObject = data.getMRootData();
                    String infoId = jsonObject.optString("data");
                    if(infoId != null && fileName.size() != 0){
                        uploadAtt(onResult,fileName,"arrange",infoId);
                        return;
                    }
                    onResult.onSuccess("成功",null);
                }
                else
                {
                    onResult.onFailure("失败");
//                    progressDialog.dismiss();
                }

            }

            @Override
            public void onProgress(ProgressMessage msg) {
                //onResponse.onProgress(msg);
            }
        }.setUrl("/home/phone/thing/updatearrange")
                .addParam("clientCode", AccountInfo.instance().getClientId())
                .addParam(map)
                .addParam("todoUsersSet", todoUser)
                .addParam("copyUsersSet", ccUser)
                .addParam("repeatFlag", repeatFlag)
                .notifyRequest();
    }

    public static Boolean requestArrageProgress(final OnMatterResult onResult,
                                              final UploadWidget uploadWidget,
                                              String id,
                                              String content,
                                              String finish,
                                              String haveFile,final String type)
    {
        if(onResult == null)
            return false;
        String url = "/home/phone/thing/addprogress";
        String dataId = "dataId";
        if(type!=null&&type.equals(MatterCell.WORKREPORTTYPE)){
        	url = "/home/phone/workReport/addComment";
        	dataId = "workId";
        }
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
            	
                if(data.getResultState() == ResponseData.ResultState.eSuccess)
                {
                	 if(type!=null&&type.equals(AllUtils.reporteType)){
                		 String infoId = data.getJsonObject().optString("id");
                		 if(infoId != null && uploadWidget.hasFile()) {
                             uploadWidget.upload(infoId, "workReport",type);
                             return;
                         }
                         onResult.onSuccess("成功",null);
                         return;
                	 }
                    JSONArray array = data.getJsonArray();
                    JSONObject jsonObject = array.optJSONObject(0);
                    String infoId = jsonObject.optString("id");
                    JSONObject jData = array.optJSONObject(0);
					CommentInfo commentInfo = toCommfromByJson(jData);
					if(commentInfo != null)
					{
					    if(infoId != null && uploadWidget.hasFile()) {
					    	uploadWidget.upload(infoId, "arrange",type);
					        return;
					    }
					    onResult.onSuccess("成功",commentInfo);
					}
					else
					{
					    onResult.onFailure("失败");
					}
                }
                else
                {
                    onResult.onFailure(data.getMsg());
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {
            }
        }.setUrl(url)
                .addParam("clientCode", AccountInfo.instance().getClientId())
                .addParam(dataId, id)
                .addParam("content", content)
                .addParam("finish", finish)
                .addParam("haveFile", haveFile)
                .notifyRequest();
    }

    public static Boolean requestDiscuss(final OnMatterResult onResult,
                                                String dataId,
                                                String dataType,
                                                String content,
                                                String contentHtml)
    {

        if(onResult == null)
            return false;
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
            	System.out.println(data.getMRootData());
                if(data.getResultState() == ResponseData.ResultState.eSuccess)
                {
                	
                    JSONArray array = data.getJsonArray();
                    if(array != null)
                    {
                        JSONObject jData = array.optJSONObject(0);
                        JSONObject jsonObject = array.optJSONObject(0);
                        String infoId = jsonObject.optString("id");
                        if(jData != null)
                        {
                            CommentInfo commentInfo = toCommfromByJson(jData);
                            if(commentInfo != null)
                            {
//                                if(infoId != null && progressName.size() != 0) {
//                                    uploadAtt(onResult, progressName, "arrange", infoId);
//                                    return;
//                                }
                                onResult.onSuccess("成功",commentInfo);
                            }
                            else
                            {
                                onResult.onFailure("失败");
                            }
                        }
                        else
                        {
                            onResult.onFailure("失败");
                        }
                    }
                    else
                    {
                        onResult.onFailure("失败");
                    }

                }
                else
                {
                    onResult.onFailure(data.getMsg());
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {
                //onResponse.onProgress(msg);
            }
        }.setUrl("/home/phone/discussion/adddiscuss")
                .addParam("clientCode", AccountInfo.instance().getClientId())
                .addParam("dataId", dataId)
                .addParam("dataType", dataType)
                .addParam("content", content)
                .addParam("contentHtml", contentHtml)
                .notifyRequest();
    }
    
    public static Boolean addDiscussDiscuss(final OnMatterResult onResult,
            String discussionId,
            String content,
            String contentHtml){
    	 if(onResult == null)
             return false;
         return new RequestAdpater() {
             @Override
             public void onReponse(ResponseData data) {
                 if(data.getResultState() == ResponseData.ResultState.eSuccess)
                 {
                     JSONArray array = data.getJsonArray();
                     if(array != null)
                     {
                         JSONObject jData = array.optJSONObject(0);
                         JSONObject jsonObject = array.optJSONObject(0);
                         String infoId = jsonObject.optString("id");
                         if(jData != null)
                         {
                             CommentInfo commentInfo = toCommfromByJson(jData);
                             if(commentInfo != null)
                             {
//                                 if(infoId != null && progressName.size() != 0) {
//                                     uploadAtt(onResult, progressName, "arrange", infoId);
//                                     return;
//                                 }
                                 onResult.onSuccess("成功",commentInfo);
                             }
                             else
                             {
                                 onResult.onFailure("失败");
                             }
                         }
                         else
                         {
                             onResult.onFailure("失败");
                         }
                     }
                     else
                     {
                         onResult.onFailure("失败");
                     }

                 }
                 else
                 {
                     onResult.onFailure(data.getMsg());
                 }
             }

             @Override
             public void onProgress(ProgressMessage msg) {
                 //onResponse.onProgress(msg);
             }
         }.setUrl("/home/phone/discussion/adddiscuss")
                 .addParam("clientCode", AccountInfo.instance().getClientId())
                 .addParam("discussionId", discussionId)
                 .addParam("content", content)
                 .addParam("contentHtml", contentHtml)
                 .notifyRequest();
    } 


    public static Boolean requestApprove(final OnMatterResult onResult,
                                         String dataId,
                                         String suggestion,
                                         String status,
                                         String todoUserSet,
                                         String copyUsersSet)
    {
        if(onResult == null)
            return false;
        return new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                if(data.getResultState() == ResponseData.ResultState.eSuccess)
                {
                    JSONArray array = data.getJsonArray();
                    if(array != null)
                    {
                        JSONObject jData = array.optJSONObject(0);
                        CommentInfo commentInfo = toCommfromByJson(jData);
                        if(commentInfo != null)
                        {
                            onResult.onSuccess("成功",commentInfo);
                        }
                        else
                        {
                            onResult.onFailure("失败");
                        }
                    }
                    else
                    {
                        onResult.onFailure("失败");
                    }
                }
                else
                {
                    onResult.onFailure(data.getMsg());
                }
                //onResponse.onResponse(data);
            }

            @Override
            public void onProgress(ProgressMessage msg) {
                //onResponse.onProgress(msg);
            }
        }.setUrl("/home/phone/thing/approve")
                .addParam("clientCode", AccountInfo.instance().getClientId())
                .addParam("dataId", dataId)
                .addParam("suggestion", suggestion)
                .addParam("status", status)
                .addParam("todoUserSet",todoUserSet)
                .addParam("copyUsersSet", copyUsersSet)
                .notifyRequest();
    }

    private static CommentInfo toCommfromByJson(JSONObject jData)
    {
        if(jData == null)
            return null;
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setId(jData.optString("id"));
        commentInfo.setDataId(jData.optString("dataId"));
        commentInfo.setCreateTime(new Date(jData.optLong("createTime")));
        commentInfo.setEndTime(new Date(jData.optLong("endTime")));
        commentInfo.setUserCode(jData.optString("userCode"));
        commentInfo.setUserName(jData.optString("userName"));
        commentInfo.setStatus(jData.optString("status"));
        commentInfo.setDataType(jData.optString("dataType"));
        commentInfo.setContent(jData.optString("content"));

        return commentInfo;
    }

    public interface OnMatterResult
    {
        public void onSuccess(String msg, Object obj);
        public void onFailure(String msg);
    }

    public interface  OnMatterResponse{
        public void onResponse();
        public void onProgress();
    }
    //上传附件
    public  static void uploadAtt (final OnMatterResult onMatterResult,final ArrayList<String> filePath, String appUn,String infoId) {
            if (filePath == null) {
                return;
            }
        i = 0;
            for (int j = 0; j < filePath.size(); j++) {
                String fileName = getFileName(filePath.get(j));
                new RequestAdpater() {
                    @Override
                    public void onReponse(ResponseData data) {
                        if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                            i++;
                            if(i == filePath.size()){
                                onMatterResult.onSuccess("成功",null);
                            }
                        }else
                        {
                        	onMatterResult.onFailure(data.getMsg());
                        }
                    }

                    @Override
                    public void onProgress(ProgressMessage msg) {

                    }
                }.setUrl("/home/phone/attach/upload")
                        .setRequestMethod(RequestAdpater.RequestMethod.ePost1)
                        .setRequestType(RequestAdpater.RequestType.eFileUp)
                        .addParam("infoId", infoId)
                        .addParam("appUn", appUn)
                        .addParam("fileName",fileName)
                        .setAttPath(filePath.get(j))
                        .notifyRequest();
            }
        }

    //从文件路径中得到文件名
    private  static String getFileName(String path){
        int start = path.lastIndexOf("/");
        if(start != -1 ){
            return path.substring(start+1);
        }
        return null;
    }



}
