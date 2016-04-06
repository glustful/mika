package com.miicaa.home.ui.report;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.miicaa.base.RoundImage.CircularImage;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.common.accessory.AccessoryFileListActivity;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.picture.BorwsePicture_;
import com.miicaa.home.ui.picture.PictureHelper;
import com.miicaa.home.ui.picture.PictureHelper.FirstPictureLoadListener;
import com.miicaa.home.ui.report.ReportUtils.RequestCallback;
import com.miicaa.utils.AddMoreListView;
import com.miicaa.utils.FileUtils;

public class CommentFragment extends SuperFragment {

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	View rootView;
	Bundle mBundle;
	AddMoreListView list;
	LinearLayout progressBar;
	
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		mBundle = getArguments();
		
		doBackground();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.report_comment_fragment_view, null);
            
        }
		progressBar = (LinearLayout) rootView.findViewById(R.id.progressBar);
		list = (AddMoreListView) rootView.findViewById(R.id.list);
		
		return rootView;
	}

	
	
	

	@Override
	public void doBackground(){
		progressBar.setVisibility(View.VISIBLE);
		  String url = getActivity().getString(R.string.report_comment_url);
		  new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				
				if(data.getResultState() == ResultState.eSuccess){
					
					progressBar.setVisibility(View.GONE);
					
					jsonToCache(data);
					
				}else{
					
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("workId",mBundle.getString("dataId"))
		.notifyRequest();
	}
	
	void jsonToCache(ResponseData data){
		
		list.onLoadMoreComplete(true);
		((ReportDetailActivity)getActivity()).setCommentNum(data.getJsonArray().length());
		CommentAdapter adapter = new CommentAdapter(getActivity(), data.getJsonArray());
		list.setAdapter(adapter);
		/*pInfos.clear();
		JSONObject dataobject = data.getJsonObject();
		DetailProgressInfo info = new DetailProgressInfo(dataobject.optJSONArray("progressList"),
				dataobject.optJSONObject("tongji")).save();
		if(info.progressList != null){
		pInfos.addAll(info.progressList);
		}
		
		tongji = info.tongjiInfo;
		if(adapter != null){
		refreshlist(pInfos,true);
		numToDo(tongji);
		}*/
		
	}
	
	class CommentAdapter extends BaseAdapter{

		JSONArray data;
		Context mContext;
		
		public CommentAdapter(Context c,JSONArray arr){
			this.mContext = c;
			this.data = arr;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.optJSONObject(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.report_comment_fragment_item, null);
				holder = new ViewHolder(convertView);
				holder.initUI();
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final JSONObject obj = data.optJSONObject(position);
			initData(holder,obj);
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					if(obj.optString("userCode").equals(AccountInfo.instance().getLastUserInfo().getCode())){
						remove(obj);
						return true;
					}
					return false;
				}
			});
			return convertView;
		}
		
		private void initData(final ViewHolder holder, final JSONObject optJSONObject) {
			
			Tools.setHeadImg(optJSONObject.optString("userCode"),holder.headImg );
			holder.userName.setText(optJSONObject.optString("userName"));
			holder.content.setText(optJSONObject.optString("comment"));
			holder.time.setText(PayUtils.formatData("yyyy-MM-dd HH:mm:ss", optJSONObject.optLong("createTime", 0)));
			if(!optJSONObject.isNull("resourcesImage") &&optJSONObject.optJSONArray("resourcesImage").length()>0){
				holder.img.setVisibility(View.VISIBLE);
				final String firstFid = optJSONObject.optJSONArray("resourcesImage").optJSONObject(0).optString("fileId");
				Bitmap bitmap = FileUtils.geInstance().getLittleImg(firstFid);
				if(bitmap != null){
					holder.imgView.setImageBitmap(bitmap);
				}else{
				PictureHelper.requestFirstPic(firstFid, new FirstPictureLoadListener() {
					
					@Override
					public void loadPic(Bitmap map) {
						if(map == null){
							
						}else
							FileUtils.geInstance().saveLittleBmp(map, firstFid);
						    holder.imgView.setImageBitmap(map);
					}
					});
				}
				holder.imgname.setText(optJSONObject.optJSONArray("resourcesImage").optJSONObject(0).optString("title")+"."+
						optJSONObject.optJSONArray("resourcesImage").optJSONObject(0).optString("ext")+"等"+optJSONObject.optJSONArray("resourcesImage").length()+"张图");
						
			}else{
				holder.img.setVisibility(View.GONE);
			
			}
			holder.img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ArrayList<String> fids = new ArrayList<String>();
					for(int i=0;i<optJSONObject.optJSONArray("resourcesImage").length();i++){
						fids.add(optJSONObject.optJSONArray("resourcesImage").optJSONObject(i).optString("fileId"));
					}
					BorwsePicture_.intent(mContext)
					.fileIds(fids)
					.mId(fids.get(0))
					.start();
				}
			});
			if(!optJSONObject.isNull("resourcesArticle")&&optJSONObject.optJSONArray("resourcesArticle").length()>0){
			holder.file.setVisibility(View.VISIBLE);		
			holder.file.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					 Intent intent = new Intent(view.getContext(),AccessoryFileListActivity.class);
			            Bundle bundle = new Bundle();
			            bundle.putString("id",optJSONObject.optString("id"));
			            intent.putExtra("bundle",bundle);
			            mContext.startActivity(intent);
				}
			});		
			holder.filename.setText(optJSONObject.optJSONArray("resourcesArticle").optJSONObject(0).optString("title")+"."+
					optJSONObject.optJSONArray("resourcesArticle").optJSONObject(0).optString("ext")+"等"+optJSONObject.optJSONArray("resourcesArticle").length()+"个附件");	
			}else{
			holder.file.setVisibility(View.GONE);
			}
					
		}

		class ViewHolder{
			CircularImage headImg;
			TextView userName;
			TextView content;
			LinearLayout img;
			ImageView imgView;
			TextView imgname;
			TextView time;
			LinearLayout file;
			TextView filename;
			View mRootView;
			
			public  ViewHolder(View mRootView) {
				this.mRootView = mRootView;
			}
			
			private void initUI() {
				headImg = (CircularImage) findViewById(R.id.head_img);
				userName = (TextView) findViewById(R.id.head_name);
				content = (TextView) findViewById(R.id.content);
				
				img = (LinearLayout) findViewById(R.id.img);
				imgView = (ImageView) findViewById(R.id.imgView);
				imgname = (TextView) findViewById(R.id.imgname);
				time = (TextView) findViewById(R.id.time);
				file = (LinearLayout) findViewById(R.id.file);
				filename = (TextView) findViewById(R.id.filename);
			}
			
			public View findViewById(int id){
				return mRootView.findViewById(id);
				
			}
		}
		
		
		
	}
	
	protected void remove(final JSONObject obj) {
		new AlertDialog.Builder(getActivity())
		.setTitle("删除")
		.setMessage("确认是否删除？")
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("id", obj.optString("id"));
				ReportUtils.requestList(getActivity(), new RequestCallback() {
					
					@Override
					public void callback(ResponseData data) {
						((ReportDetailActivity)getActivity()).refreshthis();
						PayUtils.showToast(getActivity(), "删除评论成功", 1000);
						doBackground();
						
					}
				}, getString(R.string.report_delete_comment_url), map);
				
			}
		})
		.setNegativeButton("取消", null)
		.create()
		.show();
		

	}
}
