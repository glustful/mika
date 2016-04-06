package com.miicaa.home.data.service;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.select.Evaluator.IsRoot;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.amap.api.maps2d.model.Tile;
import com.miicaa.common.base.DatabaseOption;
import com.miicaa.detail.MatterDetailAcrtivity_;
import com.miicaa.home.R;
import com.miicaa.home.cast.Remind;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.GroupInfo;
import com.miicaa.home.data.business.org.GroupUserInfo;
import com.miicaa.home.data.business.org.UnitInfo;
import com.miicaa.home.data.business.org.UnitUserInfo;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.business.org.UserInfoSql;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.HomeApplication;
import com.miicaa.home.ui.announcement.AnnouncementActivity_;
import com.miicaa.home.ui.checkwork.CheckWorkActivity_;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.home.ui.report.ReportDetailActivity_;

/**
 * Created by Administrator on 14-2-10.
 */
public class MessageNotify {
	
	static String TAG = "MessageNotify";
    private static MessageNotify mInstance = null;
    private static long lastTime = 0L;
    private static int id = 0;

    public static MessageNotify getInstance() {
        if (mInstance == null) {
            synchronized (MessageNotify.class) {
                if (mInstance == null) {
                    mInstance = new MessageNotify();
                }
            }
        }
        return mInstance;
    }

    @SuppressLint("NewApi")
	public void handMessage(String data, NotificationManager nm, Context context) {
        if (data == null || data.length() < 1) {
            return;
        } else if (data.equals("T")) {
            updateUndo();
        } else {
            try {
                JSONObject jsonData = new JSONObject(data);
                Log.d(TAG, "handMessage jsonData:"+jsonData);
                String dataType = "1";
                boolean isReport = false;
                if((!jsonData.isNull("isReport")&&jsonData.optBoolean("isReport"))
                		|| "report".equals(jsonData.optString("appCode"))){
                	isReport = true;
                }
//                Boolean isReport = !jsonData.isNull("isReport") ? jsonData.optBoolean("isReport") : false;
                boolean isReportComment = false;
                Log.d(TAG, "handMessage jsonObject:"+jsonData);
                String operType = jsonData.optString("operType");
                String taskType = jsonData.optString("taskType");
                StringBuilder title = new StringBuilder();
                String content = null;
//                String creator = jsonData.isNull("creator") ? "" :jsonData.optString("creator");
                String operator =  jsonData.isNull("operator") ? "" :jsonData.optString("operator");
                title.append(operator);
                if("workReport".equals(taskType)){
//                	title.append(creator);
                	if("add".equals(operType)){
                		title.append("请你点评工作报告");
                	}
                	else if("update".equals(operType))
                	{
                		title.replace(0,title.length(),jsonData.optString("content"));
                	}
                	else if("delete".equals(operType))
                	{
                		title.append("删除了工作报告");
                	}
                	else if("complete".equals(operType)){
                		title.append("办结了工作报告");
                	}
                	content = jsonData.optString("title");
                	if(content != null)
                		content = content.replace("\n", " ").replace("\r", " ");
                	
                }else if(isReport){
//                	title.replace(0, title.length(), operator);
                	String str = jsonData.isNull("title") ? "" : jsonData.optString("title");
                	str = str.replace("\n", " ").replace("\r", " ");
                	content = str;
                if("discussion".equals(taskType)){
                	if("add".equals(operType))
                		title.append("评论了工作报告");
                	else if("delete".equals(operType))
                		title.append("删除了一条工作报告评论");
                }else if("remind".equals(taskType)){
                	 str = jsonData.isNull("content") ? "" : jsonData.optString("content");
                	 str = str.replace("\n", " ").replace("\r", " ");
                		title.replace(0, title.length(),"您的工作报告" + str + "该处理了");
                		content = "";
                }else if("addComment".equals(taskType)){
                	isReportComment = true;
                	if(jsonData.isNull("customOperation"))
                	title.append("点评了工作报告");
                	else
                	title.append("删除了工作报告点评");
                }
                }else { 
                if ("add".equals(operType)) {
                    title.append("您有一条新的");
                } else if ("update".equals(operType)) {
                    title.append("您的一条");
                }else if ("delete".equals(operType)){
                    title.append("您的一条");
                }
                if ("arrange".equals(taskType)) {
                    title.append("任务");
                    dataType = "1";
                } else if ("approval".equals(taskType)) {
                    title.append("审批");
                    dataType = "2";
                } else if ("discussion".equals(taskType)) {
                    title.append("评论");
                    dataType = jsonData.optString("dataType");
                } else if ("progress".equals(taskType)) {
                    title.append("进展");
                    dataType = "1";
                }else if("announcement".equals(taskType)){
                	title.append("公告");
                	if(!jsonData.isNull("title"))
                		content = jsonData.optString("title").replaceAll("\n", " ");
                }if ("delete".equals(operType)){
                    title.append("被删除");
                    if (jsonData.isNull("title")){
                        content = null;
                    }else {
                        content = jsonData.optString("title");
                    }if (content != null){
                        content = content.replace("\n"," ").replace("\r"," ");
                    }
                }
                if ("update".equals(operType)) {
                    title.append("有更新");
                }
                if ("remind".equals(taskType)) {
                    dataType = "arrange".equals(jsonData.optString("appCode")) ? "1" : "2";
                    title.replace(0,title.length(),"您有一条新的提醒");
                    content = jsonData.optString("content");
                    if (content != null) {
                        content = content.replace("\n", " ").replace("\r", " ");
                    }

                } else if("attend".equals(taskType)){
                	Log.d(TAG, "attend title:"+jsonData.optString("title"));
                	String titleStr = jsonData.optString("title");
                	titleStr = titleStr!= null ? titleStr : "";
                	content = jsonData.optString("content");
                	content = content != null ? content : "";
                    title.replace(0,title.length(),titleStr);
                }
                }

                Intent intent = new Intent();
                if ("delete".equals(operType) && jsonData.isNull("customOperation") == true){
                    intent.setClass(context,FramMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //如果广播能接收到的话那就让FrameMain去刷新他的页面
//                    Intent intent_update = new Intent(Utils.UPDATE_ACTION);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("update",Utils.RECEIVER_CHANGE);
//                    context.sendBroadcast(intent_update);

                }else if("attend".equals(taskType)){
                	intent = CheckWorkActivity_.intent(context)
                			.get();
                	intent.setData(Uri.parse("custom://"+System.currentTimeMillis()));
                }else if("announcement".equals(taskType)){
                    intent =  AnnouncementActivity_.intent(context)
                     .get();
                }else if("workReport".equals(taskType) || isReport){
                	intent = ReportDetailActivity_.intent(context)
    				.dataId(jsonData.optString("id"))
    				.isDiscover(false)
    				.isPushMessage(true)
    				.get();
//                	intent.setData(Uri.parse("custom://"+System.currentTimeMillis()));
                }else {
                    intent =   MatterDetailAcrtivity_.intent(context).dataId(jsonData.optString("id"))
                    .dataType(dataType)
                    .taskType(taskType)
                    .titleText(content)
                    .isPushMessage(true)
                    .get();
                    intent.setData(Uri.parse("custom://"+System.currentTimeMillis()));
                }
             	Log.d(TAG, "intent :"+intent.toURI());
                PendingIntent contentIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification.Builder builder = new Notification.Builder(context).setContentIntent(contentIntent);
                builder.setAutoCancel(true).setTicker("有新消息").setSmallIcon(R.drawable.ic_launcher);
                if((new Date()).getTime()-lastTime>(10000)) {//10秒只响一次
                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
                    lastTime = (new Date()).getTime();
                }else{
                    builder.setDefaults(Notification.DEFAULT_LIGHTS).setSound(null);
                }
                builder.setContentTitle(title.toString()).setWhen(System.currentTimeMillis());
                if (content != null && !"".equals(content.trim())) {
                    builder.setContentText(content.trim());
                }


                Notification notification = builder.getNotification();
                Intent intent_del = new Intent();
                notification.deleteIntent = PendingIntent.getActivity(context,id,intent_del,PendingIntent.FLAG_UPDATE_CURRENT);
                nm.notify(id, notification);
                if (id >= 2) {
                    id = 0;
                } else {
                    id++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateUndo() {
        String todoSeq = DatabaseOption.getIntance().getValue(AccountInfo.instance().getLastUserInfo().getCode() + "todoSeq");
        if (todoSeq == null || todoSeq.length() == 0) {
            DatabaseOption.getIntance().setValue(AccountInfo.instance().getLastUserInfo().getCode() + "todoSeq", "1");
            todoSeq = "1";
        }
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    JSONArray jArray = data.getJsonArray();
                    if (jArray == null) {
                        return;
                    }

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jItem = jArray.optJSONObject(i);
                        String taskType = jItem.optString("dataType");
                        if (taskType == null) {
                            continue;
                        }
                        if (taskType.equals("oug")) {
                            handOugMessage(jItem);
                        } else if (taskType.equals("arrange")) {

                        } else if (taskType.equals("approval")) {

                        } else if (taskType.equals("todo")) {

                        } else if (taskType.equals("dialogue")) {

                        } else if (taskType.equals("remind")) {

                        }
                        //execUnd(jItem);
                    }

                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl("/mobile/mobile/task/undo")
                .addParam("clientCode", AccountInfo.instance().getClientId())
                .addParam("userCode", AccountInfo.instance().getLastUserInfo().getCode())
                .addParam("seqs", "1")// todoSeq)
                .notifyRequest();
    }


    private void handOugMessage(final JSONObject jItem) {
        if (jItem == null) {
            return;
        }
        String opeType = jItem.optString("operType");
        if (opeType == null) {
            return;
        }
        String param = jItem.optString("param");
        try {
            JSONObject jParam = new JSONObject(param);
            if (opeType.equals("delete")) {
                deleteOug(jItem, jParam);
            } else {
                execOugUndo(jItem, jParam);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void execOugUndo(final JSONObject jItem, final JSONObject jParam) {
        if (jItem == null) {
            return;
        }
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    if (jItem.optString("operType").equals("update")) {
                        if (jParam.optString("dataType").equals("unitUser")) {
                            updataUnitUser(data.getJsonArray(), jItem, jParam);
                        } else if (jParam.optString("dataType").equals("groupUser")) {
                            updataGroupUser(data.getJsonArray(), jItem, jParam);
                        } else if (jParam.optString("dataType").equals("user")) {
                            updataUser(data.getJsonArray(), jItem, jParam);
                        } else if (jParam.optString("dataType").equals("unit")) {
                            updataUnit(data.getJsonObject(), jItem, jParam);
                        } else if (jParam.optString("dataType").equals("group")) {
                            updataGroup(data.getJsonObject(), jItem, jParam);
                        }
                    } else if (jItem.optString("operType").equals("add")) {
                        if (jParam.optString("dataType").equals("unitUser")) {
                            addUnitUser(data.getJsonArray(), jItem, jParam);
                        } else if (jParam.optString("dataType").equals("groupUser")) {
                            addGroupUser(data.getJsonArray(), jItem, jParam);
                        } else if (jParam.optString("dataType").equals("user")) {
                            addUser(data.getJsonArray(), jItem, jParam);
                        } else if (jParam.optString("dataType").equals("unit")) {
                            addUnit(data.getJsonArray(), jItem, jParam);
                        } else if (jParam.optString("dataType").equals("group")) {
                            addGroup(data.getJsonArray(), jItem, jParam);
                        }
                    }

//                    DatabaseOption.getIntance().setValue(AccountInfo.instance().getLastUserInfo().getCode()+"todoSeq",jItem.optString("id"));
//                    Remind remind = new Remind();
//                    remind.sendRemind(HomeApplication.getInstance(), R.drawable.an_home_flag,
//                            "haha",data.getMsg(), FrameMain.class);

                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl("/mobile/mobile/task/execute")
                .addParam("operType", jItem.optString("operType"))
                .addParam("taskType", jItem.optString("dataType"))
                .addParam("param", jItem.optString("param"))
                .addParam("dataId", jItem.optString("dataId"))
                .notifyRequest();
    }

    private void addGroup(final JSONArray jResArray, final JSONObject jItem, final JSONObject jParam) {
        if (jResArray == null) {
            return;
        }
        for (int i = 0; i < jResArray.length(); i++) {
            JSONObject jData = jResArray.optJSONObject(i);
            GroupInfo info = new GroupInfo();
            info.setId(jData.optLong("id"));
            info.setCode(jData.optString("code"));
            info.setCreateTime(new Date(jData.optLong("createTime")));
            info.setFullName(jData.optString("fullName"));
            info.setName(jData.optString("name"));
            info.setOrgCode(jData.optString("orgCode"));
            info.setParentCode(jData.optString("parentCode"));
            info.setSort(jData.optLong("sort"));
            info.setStatus(jData.optLong("status"));
            info.save();
        }
    }

    private void addUnit(final JSONArray jResArray, final JSONObject jItem, final JSONObject jParam) {
        if (jResArray == null) {
            return;
        }
        for (int i = 0; i < jResArray.length(); i++) {
            JSONObject jData = jResArray.optJSONObject(i);
            UnitInfo info = new UnitInfo();
            info.setId(jData.optLong("id"));
            info.setCode(jData.optString("code"));
            info.setCreateTime(new Date(jData.optLong("createTime")));
            info.setFullName(jData.optString("fullName"));
            info.setName(jData.optString("name"));
            info.setOrgCode(jData.optString("orgCode"));
            info.setParentCode(jData.optString("parentCode"));
            info.setSort(jData.optLong("sort"));
            info.setStatus(jData.optLong("status"));
            info.save();
        }
    }


    private void addUser(final JSONArray jResArray, final JSONObject jItem, final JSONObject jParam) {
        if (jResArray == null) {
            return;
        }
        for (int i = 0; i < jResArray.length(); i++) {
            JSONObject jData = jResArray.optJSONObject(i);
            UserInfo info = new UserInfo();
            info.setId(Long.parseLong(jData.optString("id")));
            info.setCode(jData.optString("code"));
            info.setEmail(jData.optString("email"));
            info.setCellphone(jData.optString("cellphone"));
            info.setStatus(jData.optLong("status"));
            info.setAvatar(jData.optString("avatar"));
            info.setCreateTime(new Date(jData.optLong("createTime")));
            info.setOrgCode(jData.optString("orgCode"));
            info.setGender(jData.optString("gender"));
            info.setBirthday(new Date(jData.optLong("birthday")));
            info.setQq(jData.optString("qq"));
            info.setPhone(jData.optString("phone"));
            info.setAddr(jData.optString("addr"));
            info.setName(jData.optString("name"));
            info.save();
            addUnitUser(jData.optJSONArray("unitUser"), jItem, jParam);
            addUser(jData.optJSONArray("unitUser"), jItem, jParam);

        }
    }

    private void addGroupUser(final JSONArray jResArray, final JSONObject jItem, final JSONObject jParam) {
        if (jResArray == null) {
            return;
        }
        for (int i = 0; i < jResArray.length(); i++) {
            JSONObject jData = jResArray.optJSONObject(i);
            GroupUserInfo info = new GroupUserInfo();
            info.setId(jData.optLong("id"));
            info.setUserCode(jData.optString("userCode"));
            info.setUnitCode(jData.optString("unitCode"));
            info.setGroupCode(jData.optString("groupCode"));
            info.setSort(jData.optLong("sort"));
            info.save();
        }
    }

    private void addUnitUser(final JSONArray jResArray, final JSONObject jItem, final JSONObject jParam) {
        if (jResArray == null) {
            return;
        }
        for (int i = 0; i < jResArray.length(); i++) {
            JSONObject jData = jResArray.optJSONObject(i);
            UnitUserInfo info = new UnitUserInfo();
            info.setId(jData.optLong("id"));
            info.setUserCode(jData.optString("userCode"));
            info.setUnitCode(jData.optString("unitCode"));
            info.setSort(jData.optLong("sort"));
            info.save();
        }
    }


    private void updataUnitUser(final JSONArray jResArray, final JSONObject jItem, final JSONObject jParam) {
        JSONArray jUserCodes = jParam.optJSONArray("userCodes");
        if (jUserCodes == null) {
            return;
        }
        for (int i = 0; i < jUserCodes.length(); i++) {
            String userCode = jUserCodes.optString(i);
            UnitUserInfo.deleteByUserCode(userCode);
        }

        if (jResArray == null) {
            return;
        }
        for (int i = 0; i < jResArray.length(); i++) {
            JSONObject jdata = jResArray.optJSONObject(i);
            UnitUserInfo info = new UnitUserInfo();
            info.setId(jdata.optLong("id"));
            info.setUserCode(jdata.optString("userCode"));
            info.setUnitCode(jdata.optString("unitCode"));
            info.setSort(jdata.optLong("sort"));
            info.save();
        }

    }

    private void updataGroupUser(final JSONArray jResArray, final JSONObject jItem, final JSONObject jParam) {
        JSONArray jUserCodes = jParam.optJSONArray("userCodes");
        if (jUserCodes == null) {
            return;
        }
        for (int i = 0; i < jUserCodes.length(); i++) {
            String userCode = jUserCodes.optString(i);
            GroupUserInfo.deleteByUserCode(userCode);
        }

        if (jResArray == null) {
            return;
        }
        for (int i = 0; i < jResArray.length(); i++) {
            JSONObject jdata = jResArray.optJSONObject(i);
            GroupUserInfo info = new GroupUserInfo();
            info.setId(jdata.optLong("id"));
            info.setUserCode(jdata.optString("userCode"));
            info.setUnitCode(jdata.optString("unitCode"));
            info.setGroupCode(jdata.optString("groupCode"));
            info.setSort(jdata.optLong("sort"));
            info.save();
        }


    }

    private void updataUser(final JSONArray jResArray, final JSONObject jItem, final JSONObject jParam) {
        JSONArray jUserCodes = jParam.optJSONArray("userCodes");
        if (jUserCodes == null) {
            return;
        }
        for (int i = 0; i < jUserCodes.length(); i++) {
            String userCode = jUserCodes.optString(i);
            GroupUserInfo.deleteByUserCode(userCode);
        }

        if (jResArray == null) {
            return;
        }
        for (int i = 0; i < jResArray.length(); i++) {
            JSONObject jdata = jResArray.optJSONObject(i);
            GroupUserInfo info = new GroupUserInfo();
            info.setId(jdata.optLong("id"));
            info.setUserCode(jdata.optString("userCode"));
            info.setUnitCode(jdata.optString("unitCode"));
            info.setGroupCode(jdata.optString("groupCode"));
            info.setSort(jdata.optLong("sort"));
            info.save();
        }


    }

    private void updataUnit(final JSONObject jRes, final JSONObject jItem, final JSONObject jParam) {
        if (jRes == null) {
            return;
        }
        //JSONObject jData = jResArray.optJSONObject(i);
        UnitInfo info = new UnitInfo();
        info.setId(jRes.optLong("id"));
        info.setCode(jRes.optString("code"));
        info.setCreateTime(new Date(jRes.optLong("createTime")));
        info.setFullName(jRes.optString("fullName"));
        info.setName(jRes.optString("name"));
        info.setOrgCode(jRes.optString("orgCode"));
        info.setParentCode(jRes.optString("parentCode"));
        info.setSort(jRes.optLong("sort"));
        info.setStatus(jRes.optLong("status"));
        UnitInfo oldInfo = UnitInfo.findById(info.getId());
        info.updateChild(oldInfo.getName());
        info.save();
    }

    private void updataGroup(final JSONObject jRes, final JSONObject jItem, final JSONObject jParam) {
        if (jRes == null) {
            return;
        }
        GroupInfo info = new GroupInfo();
        info.setId(jRes.optLong("id"));
        info.setCode(jRes.optString("code"));
        info.setCreateTime(new Date(jRes.optLong("createTime")));
        info.setFullName(jRes.optString("fullName"));
        info.setName(jRes.optString("name"));
        info.setOrgCode(jRes.optString("orgCode"));
        info.setParentCode(jRes.optString("parentCode"));
        info.setSort(jRes.optLong("sort"));
        info.setStatus(jRes.optLong("status"));
        GroupInfo oldInfo = GroupInfo.findById(info.getId());
        info.updateChild(oldInfo.getName());
        info.save();
    }

    private void deleteOug(final JSONObject jItem, final JSONObject jParam) {
        String dataType = jParam.optString("dataType");
        JSONArray jIds = jParam.optJSONArray("ids");

        if (dataType == null || jIds == null) {
            return;
        } else if (dataType.equals("user")) {
            for (int i = 0; i < jIds.length(); i++) {

                UserInfo info = UserInfo.findById(jIds.optLong(i));
                if (info != null) {
                    UserInfoSql.delete(info);
                }
                UnitUserInfo.delete(info);
                GroupUserInfo.delete(info);
            }
        } else if (dataType.equals("unit")) {
            for (int i = 0; i < jIds.length(); i++) {
                UnitInfo info = UnitInfo.findById(jIds.optLong(i));
                if (info != null) {
                    info.deleteFormDb();
                }
            }
        } else if (dataType.equals("group")) {
            for (int i = 0; i < jIds.length(); i++) {

                GroupInfo info = GroupInfo.findById(jIds.optLong(i));
                GroupUserInfo.deleteGroupUser(info.getCode());
                GroupInfo.deleteByCode(info.getCode());
            }
        }
    }

    private void execUnd(final JSONObject jItem) {
        if (jItem == null) {
            return;
        }
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    DatabaseOption.getIntance().setValue(AccountInfo.instance().getLastUserInfo().getCode() + "todoSeq", jItem.optString("id"));
                    Remind remind = new Remind();
                    remind.sendRemind(HomeApplication.getInstance(), R.drawable.an_home_flag,
                            "haha", data.getMsg(), FramMainActivity.class);

                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl("/mobile/mobile/task/execute")
                .addParam("operType", jItem.optString("operType"))
                .addParam("taskType", jItem.optString("dataType"))
                .addParam("param", jItem.optString("param"))
                .addParam("dataId", jItem.optString("dataId"))
                .notifyRequest();
    }


}
