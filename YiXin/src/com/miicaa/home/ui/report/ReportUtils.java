package com.miicaa.home.ui.report;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.miicaa.common.base.FormFile;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.data.net.SocketHttpRequester;
import com.miicaa.home.data.net.SocketHttpRequester.OnCompleteListener;
import com.miicaa.home.data.net.SocketHttpRequester.OnProgressListener;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.photoGrid.Bimp;
import com.miicaa.utils.fileselect.MyFileItem;

public class ReportUtils {
	
	private static String TAG = "ReportUtils";

	public static void requestList(final Context mContext,
			final RequestCallback callback, String url,
			HashMap<String, String> map) {
		Log.d(TAG, "requestList PARAM:"+map);
		PayUtils.showDialog(mContext);
		new RequestAdpater() {

			@Override
			public void onReponse(ResponseData data) {
				PayUtils.closeDialog();
				System.out.println(data.getMRootData());
				if (data.getResultState() == ResultState.eSuccess) {

					callback.callback(data);
				} else {
					PayUtils.showToast(mContext, "" + data.getMsg(), 1000);
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {

			}
		}.setUrl(url).addParam(map).notifyRequest();
	}

	public interface RequestCallback {
		void callback(ResponseData data);
	}

	public static void putDay(String today1, HashMap<String, String> map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date d = sdf.parse(today1);
			SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd");
			String str = tmp.format(d);
			map.put("startDate", str + " 00:00:00");
			map.put("endDate", str + " 23:59:59");
		} catch (ParseException e) {

			e.printStackTrace();
		}
	}

	public static void putWeek(WeekEntity currentWeekEntity,
			HashMap<String, String> map) {
		SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd");
		String str = tmp.format(currentWeekEntity.getStartDate().getTime());
		map.put("startDate", str + " 00:00:00");
		str = tmp.format(currentWeekEntity.getEndDate().getTime());
		map.put("endDate", str + " 23:59:59");

	}

	public static void putMonth(String today1, HashMap<String, String> map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		today1 += ".01";
		try {
			Date d = sdf.parse(today1);
			SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd");
			String str = tmp.format(d);
			map.put("startDate", str + " 00:00:00");
			Calendar ca = Calendar.getInstance();
			ca.setTime(d);
			ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
			str = tmp.format(ca.getTime());
			map.put("endDate", str + " 23:59:59");
		} catch (ParseException e) {

			e.printStackTrace();
		}

	}
	
	public static String getMonthFirstDay(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		str += ".01";
		try {
			Date d = sdf.parse(str);
			SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd");
			String str1 = tmp.format(d);
			return str1 + " 00:00:00";
			
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return sdf.format(new Date())+" 00:00:00";
	}
	
	public static String getMonthLastDay(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		str += ".01";
		try {
			Date d = sdf.parse(str);
			SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd");
			
			Calendar ca = Calendar.getInstance();
			ca.setTime(d);
			ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
			str = tmp.format(ca.getTime());
			return str + " 23:59:59";
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return sdf.format(new Date())+" 23:59:59";
	}

	public static void putCustomDate(String today1, String today2,
			HashMap<String, String> map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date d = sdf.parse(today1);
			SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd");
			String str = tmp.format(d);
			map.put("startDate", str + " 00:00:00");
			Date d1 = sdf.parse(today2);

			str = tmp.format(d1);
			map.put("endDate", str + " 23:59:59");
		} catch (ParseException e) {

			e.printStackTrace();
		}

	}

	public static WeekEntity getCurrentWeekEntity(Calendar calendar) {

		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		int count = 0;
		int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		boolean flag = false;
		WeekEntity entity = null;
		for (int i = 1; i <= days; i++) {

			if (i == currentDay) {
				flag = true;
			}
			calendar.set(Calendar.DAY_OF_MONTH, i);
			int k = new Integer(calendar.get(Calendar.DAY_OF_WEEK));

			if (k == 1) {// 若当天是周日
				count++;

				Calendar tmp = (Calendar) calendar.clone();
				tmp.set(Calendar.DAY_OF_YEAR,
						calendar.get(Calendar.DAY_OF_YEAR) - 6);

				if (flag) {
					entity = new WeekEntity();
					entity.setIndex(count);
					entity.setStartDate(tmp);
					entity.setEndDate(calendar);
					break;
				}
			}
		}
		if (entity == null) {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return getCurrentWeekEntity(calendar);
		}
		return entity;
	}

	ProgressDialog mDialog;

	public class UploadTask extends AsyncTask<Integer, Integer, String> {
		HashMap<String, String> map;

		String url;
		String path;
		String filename;
		SocketHttpRequester request;
		Context mContext;
		ArrayList<Object> falieds;
		ArrayList<MyFileItem> files;
		Handler mHandler;
		String infoId;
		String appun = "workReport";

		public UploadTask(Context mContext, String url,ArrayList<MyFileItem> files,ArrayList<Object> falieds,Handler mHandler,String infoId,String appun) {

			this.url = url;
			this.mContext = mContext;
			this.files = files;
			this.falieds = falieds;
			this.mHandler = mHandler;
			this.infoId = infoId;
			this.request = new SocketHttpRequester();
			this.appun = appun;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			mDialog.setProgress(values[0]);

		}

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMax(100);
			mDialog.setMessage("正在上传");
			mDialog.setCancelable(true);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.show();

		}

		@Override
		protected void onPostExecute(String result) {

			if (mDialog != null)
				mDialog.dismiss();

			mHandler.sendEmptyMessage(202);
			
		}

		@Override
		protected String doInBackground(Integer... params) {

			for (int i = 0; i < files.size(); i++) {
				
				Log.d(TAG, "uploadFile file:"+files.get(i).path);
				
					final MyFileItem item = files.get(i);
					if(item.fid!=null&&!item.fid.equals("")){
						continue;
					}
					path = item.path;

					filename = item.name;
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.obj = filename;
					handler.sendMessage(msg);
					map = new HashMap<String, String>();
					
					map.put("fileName", filename);
					map.put("infoId", infoId);
					map.put("appUn", appun);

					Log.d(TAG, "doInBackground PARAM:"+map+"url:"+url);

				request.post(url, map, new FormFile[] { new FormFile(new File(
						path), "file", null) }, new OnProgressListener() {

					@Override
					public void onProgress(int progress) {
						onProgressUpdate(progress);

					}
				}, new OnCompleteListener() {

					@Override
					public void onSuccess(String json) {
						// files.remove(path);
					}

					@Override
					public void onFailed() {
						falieds.add(item);
					}
				});
			}
			for (int i = 0; i < Bimp.drr.size(); i++) {

				path = Bimp.drr.get(i);
				filename = path.substring(path.lastIndexOf("/") + 1);
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.obj = filename;
				handler.sendMessage(msg);
				map = new HashMap<String, String>();
				
				map.put("fileName", filename);
				map.put("infoId", infoId);
				map.put("appUn", appun);

				request.post(url, map, new FormFile[] { new FormFile(new File(
						path), "file", null) }, new OnProgressListener() {

					@Override
					public void onProgress(int progress) {
						onProgressUpdate(progress);

					}
				}, new OnCompleteListener() {

					@Override
					public void onSuccess(String json) {
						// Bimp.drr.remove(path);
					}

					@Override
					public void onFailed() {
						falieds.add(path);

					}
				});
			}

			return null;
		}

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mDialog.setMessage(msg.obj.toString());
				break;
			}
		}

	};
	
	 public static int convertType(String type){
	    	if(type.equals(WorkReportActivity.REPORT_TYPE_DAY)){
	    		return WorkReportActivity.REPORT_DAY;
	    	}else if(type.equals(WorkReportActivity.REPORT_TYPE_WEEK)){
	    		return WorkReportActivity.REPORT_WEEK;
	    	}else if(type.equals(WorkReportActivity.REPORT_TYPE_MONTH)){
	    		return WorkReportActivity.REPORT_MONTH;
	    	}else if(type.equals(WorkReportActivity.REPORT_TYPE_CUSTOM)){
	    		return WorkReportActivity.REPORT_CUSTOM;
	    	}else{
	    		return WorkReportActivity.REPORT_CUSTOM;
	    	}
	    }

	public static JSONArray convertJSON(ArrayList<String> list) {
		if(list==null||list.size()<1)
		return null;
		JSONArray arr = new JSONArray();
		for(String s:list){
			try{
				JSONObject obj = new JSONObject(s);
				arr.put(obj);
			}catch(Exception e){
				
			}
		}
		return arr;
	}

	public static ArrayList<ContactViewShow> convertCommenter(String commenter) {
		if(commenter==null)
		return null;
		try {
			JSONArray arr = new JSONArray(commenter);
			ArrayList<ContactViewShow> copy = new ArrayList<ContactViewShow>();
			for(int i=0;i<arr.length();i++){
				ContactViewShow item = new ContactViewShow(arr.optJSONObject(i).optString("userName"), arr.optJSONObject(i).optString("userCode"));
				copy.add(item);
			}
			return copy;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
	}

}
