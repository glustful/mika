package com.miicaa.detail;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.common.accessory.AccessoryFileListActivity;
import com.miicaa.home.ui.picture.BorwsePicture_;
import com.miicaa.home.ui.picture.PictureHelper;
import com.miicaa.home.ui.picture.PictureHelper.FirstPictureLoadListener;
import com.miicaa.utils.FileUtils;
import com.miicaa.utils.ViewHolder;

public class DetailProgressDetailAdapter extends BaseAdapter{

	static String TAG = "DetailProgressDetailAdapter";
	
	Context context;
	ArrayList<DetailList> infos;
	 String[] items;
	OnDiscussButtonClickListener listener;
	String dataid;
	String progressId;
	Boolean showDelete;
	int num;
	 public DetailProgressDetailAdapter(Context context) {
		 this.context = context;
		 items = new String[]{"删除"};
	}
	 
	 public void showDelete(Boolean h){
		 showDelete = h;
	 }
	 
	 public void refresh(ArrayList<DetailList> infos){
		 this.infos = infos;
		 this.notifyDataSetChanged();
	 }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos==null?0:infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final DetailList info = infos.get(position);
		convertview = convertview == null ?
				LayoutInflater.from(context).inflate(R.layout.matter_do_progress_detail, null):convertview;
				TextView content = ViewHolder.get(convertview, R.id.content);
				LinearLayout file = ViewHolder.get(convertview, R.id.file);
				TextView filename = ViewHolder.get(convertview, R.id.filename);
				final LinearLayout img = ViewHolder.get(convertview, R.id.img);
				final ImageView imgView = ViewHolder.get(convertview, R.id.imgView);
				TextView imgname = ViewHolder.get(convertview, R.id.imgname);
				TextView time = ViewHolder.get(convertview, R.id.time);
				TextView complete = ViewHolder.get(convertview, R.id.complete);
				TextView from = ViewHolder.get(convertview, R.id.from);
				ImageButton talk = ViewHolder.get(convertview, R.id.progressTalk);
				TextView talknum = ViewHolder.get(convertview, R.id.progressTalkNum);
				talknum.setText("("+info.discussnum+")");
				talk.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ProgressDiscussActivity_.intent(context)
						.detailList(info)
						.start();
					}
				});
				
				file.setVisibility(info.articles==null?View.GONE:View.VISIBLE);
				img.setVisibility(info.imgs==null?View.GONE:View.VISIBLE);
				if(info.imgs == null)
				Log.d(TAG, "infoimgs Null"+"---");
				if(info.imgs != null){
					final String firstFid = info.imgs.get(0).fileid;
					Bitmap bitmap = FileUtils.geInstance().getLittleImg(firstFid);
					if(bitmap != null){
						imgView.setImageBitmap(bitmap);
					}else{
					PictureHelper.requestFirstPic(info.imgs.get(0).fileid, new FirstPictureLoadListener() {
						
						@Override
						public void loadPic(Bitmap map) {
							if(map == null){
								Log.d(TAG, "maybe the internet is out !"+map);
							}else
								FileUtils.geInstance().saveLittleBmp(map, firstFid);
							    imgView.setImageBitmap(map);
						}
						});
					}
				}
				img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ArrayList<String> fids = new ArrayList<String>();
						for(ResourcesImage img : info.imgs){
							fids.add(img.fileid);
						}
						BorwsePicture_.intent(context)
						.fileIds(fids)
						.mId(info.id)
						.start();
					}
				});
				
				file.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						 Intent intent = new Intent(view.getContext(),AccessoryFileListActivity.class);
				            Bundle bundle = new Bundle();
				            bundle.putString("id",info.id);
				            intent.putExtra("bundle",bundle);
				            context.startActivity(intent);
					}
				});
				complete.setVisibility("1".equals(info.isfinish)?View.VISIBLE:View.GONE);
				
				content.setText(info.content);
				
				
				filename.setText(info.articles != null?info.articles.get(0).title+"."+
				info.articles.get(0).ext+"等"+info.articles.size()+"个附件":"");
				
				
				
				imgname.setText(info.imgs != null?info.imgs.get(0).title+"."+
				info.imgs.get(0).ext+"等"+info.imgs.size()+"张图":"");
				time.setText(info.createtime);
				convertview.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View arg0) {
						// TODO Auto-generated method stub
							// TODO Auto-generated method stub
							dataid = info.dataid;
							progressId = info.id;
							num = position;
							dialogShow(info.usercode);
						return true;
					}
				});
				return convertview;
				
				
		
	}
	
	void dialogShow(String usercode){
		if(MatterDetailAcrtivity.progressStatus==null ||!"01".equals(MatterDetailAcrtivity.progressStatus) || 
				usercode == null || 
				!AccountInfo.instance().getLastUserInfo().getCode().equals(usercode)){
			return;
		}
		AlertDialog.Builder builder = new  AlertDialog.Builder(context)
		.setTitle("提示")
		.setItems(items, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int position) {
				// TODO Auto-generated method stub
			  if(0 == position){
				    dialog.dismiss();
					showWhatDoprogress();
				}
			}
			
			
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int position) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();
	}

	void showWhatDoprogress(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("确认要删除进展吗？")
				.setNegativeButton("确认",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface diaolog, int position) {
						// TODO Auto-generated method stub
						delProgress();
					}
				
				}).setPositiveButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int position) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		builder.show();
	}
	
	void delProgress(){
		String url = "/home/phone/thing/deleteprogress";
		new RequestAdpater(){

			@Override
			public void onReponse(ResponseData data) {
				// TODO Auto-generated method stub
				Toast toast = Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT);
				if(data.getResultState() == ResultState.eSuccess){
					toast.show();
					MatterDetailAcrtivity.getInstance().refreshthis();
//					infos.remove(num);
//					refresh(infos);
				}else{
					toast.setText("删除失败");
					toast.show();
				}
				
			}

			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}.setUrl(url)
		.addParam("dataId",dataid)
		.addParam("progressId",progressId)
		.addParam("clientCode","")
		.notifyRequest();
		
	}
	
	
	public void setOnDiscussClickListener(OnDiscussButtonClickListener listener){
		this.listener = listener;
	}
	
	
	public interface OnImgAddBackListener{
		void  imgAddBack();
	}
	
	OnImgAddBackListener onImgAddBackListener;
	public void setOnImgAddBackListener(OnImgAddBackListener listener){
		this.onImgAddBackListener = listener;
	}
	

}
