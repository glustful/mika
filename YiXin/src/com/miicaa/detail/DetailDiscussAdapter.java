package com.miicaa.detail;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
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

import com.miicaa.common.base.Tools;
import com.miicaa.common.base.Utils;
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
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.FileUtils;
import com.miicaa.utils.ViewHolder;

public class DetailDiscussAdapter extends BaseAdapter{
	
	static String TAG = "DetailDiscussAdapter";
	
	private static int HEAD = 0;
	private static int CONTENT = 1;
	ArrayList<DetailContentInfo> infos;
	Html.ImageGetter expressGetter;
	Context context;
	String type;
	OnDiscussButtonClickListener listener;
	Boolean isInTop = false;
	DetailList detailList;
	View outView;
	
	public DetailDiscussAdapter(Context context,String type,Html.ImageGetter expressGetter) {
		infos = new ArrayList<DetailContentInfo>();
		this.type = type;
		this.context = context;
		this.expressGetter = expressGetter;
		
	}
	public void refresh(ArrayList<DetailContentInfo> infos){
		this.infos.clear();
		this.infos.addAll(infos);
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if(isInTop){
			return infos != null ? infos.size()+1:1;
		}
		return infos != null ? infos.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public int getItemViewType(int position) {
		if(isInTop)
		return position == 0 ? HEAD : CONTENT;
		
		return CONTENT;
	}
	@Override
	public int getViewTypeCount() {
	   
	   return isInTop ? 1 : 2;
	}
	@Override
	public View getView(final int position, View convertview, ViewGroup viewgroup) {
		final ArrayList<String> codes = new ArrayList<String>();
		int cType = getItemViewType(position);
		if (cType == HEAD){
			convertview = LayoutInflater.from(context).inflate(R.layout.progress_discuss_top, null);
			ImageView headImg = (ImageView)convertview.findViewById(R.id.head_img);
			TextView headName = (TextView)convertview.findViewById(R.id.head_name);
			TextView content = (TextView)convertview.findViewById(R.id.content);
			LinearLayout file = (LinearLayout)convertview.findViewById(R.id.file);
			TextView filename = (TextView)convertview.findViewById(R.id.filename);
			LinearLayout image = (LinearLayout)convertview.findViewById(R.id.img);
			final ImageView imageView = (ImageView)convertview.findViewById(R.id.imageView);
			TextView imgname =(TextView)convertview.findViewById(R.id.imgname);
			TextView time = (TextView)convertview.findViewById(R.id.time);
			TextView complete = (TextView)convertview.findViewById(R.id.complete);
			TextView from = (TextView)convertview.findViewById(R.id.from);
			ImageButton talkbutton = (ImageButton)convertview.findViewById(R.id.progressTalk);
			TextView talknum = (TextView)convertview.findViewById(R.id.talknum);
			TextView discussCount = (TextView)convertview.findViewById(R.id.discussCount);
			Log.d("DetailDiscussFragment", "detailList.usercode :"+detailList.usercode);
			Tools.setHeadImg(detailList.usercode, headImg);
			headName.setText(detailList.username);
			Log.d("DetailDiscussAdapter content:", detailList.content);
			content.setText(detailList.content);
			if(detailList.articles != null && detailList.articles.size() > 0){
				file.setVisibility(View.VISIBLE);
				filename.setText(detailList.articles.get(0).title+"."+detailList.articles.get(0).ext);
				file.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						 Intent intent = new Intent(context,AccessoryFileListActivity.class);
				            Bundle bundle = new Bundle();
				            bundle.putString("id",detailList.id);
				            intent.putExtra("bundle",bundle);
				            context.startActivity(intent);
					}
				});
			}
			if(detailList.imgs != null && detailList.imgs.size()>0){
					final String firstFid = detailList.imgs.get(0).fileid;
					image.setVisibility(View.VISIBLE);
					Bitmap bitmap = FileUtils.geInstance().getLittleImg(firstFid);
					if(bitmap != null){
						imageView.setImageBitmap(bitmap);
					}else{
					PictureHelper.requestFirstPic(detailList.imgs.get(0).fileid, new FirstPictureLoadListener() {
						
						@Override
						public void loadPic(Bitmap map) {
							if(map == null){
							}else
								FileUtils.geInstance().saveLittleBmp(map, firstFid);
							    imageView.setImageBitmap(map);
						}
						});
					}
				imgname.setText(detailList.imgs.get(0).title+"."+detailList.imgs.get(0).ext);
				image.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						BorwsePicture_.intent(context)
						.fileIds(getFileIds())
						.start();
					}
				});
			}
			talknum.setText(infos != null ?"("+infos.size()+")": detailList.discussnum +"");
			time.setText(detailList.createtime != null ?detailList.createtime:"");
			complete.setVisibility("1".equals(detailList.isfinish)?View.VISIBLE:View.GONE);
			talkbutton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Utils.hiddenSoftBorad(context);
				}
			});
			discussCount.setText("对进展的评论"+(CharSequence) (infos != null ?"("+infos.size()+")" : 
				detailList.discussnum +""));
			
			outView = convertview;
			
		}else if(cType == CONTENT){
			if(convertview == null || convertview.findViewById(R.id.normalTalk) == null){
				convertview = LayoutInflater.from(context).inflate(R.layout.matter_narmal_detail, null);
			} 
			DetailContentInfo inf = null;
			if(isInTop)
                  inf = infos.get(position-1);
			else
				inf = infos.get(position);
			final DetailContentInfo info = inf;
			codes.addAll(setCodes(info.contenthtml));
		ImageView headimg = ViewHolder.get(convertview, R.id.head_img);
		TextView headname = ViewHolder.get(convertview, R.id.head_name);
		
		ImageView talkbutton = ViewHolder.get(convertview, R.id.normalTalk);
		TextView time = ViewHolder.get(convertview, R.id.time);
		TextView from = ViewHolder.get(convertview, R.id.from);
		TextView content = ViewHolder.get(convertview, android.R.id.content);
		talkbutton.setVisibility(AllUtils.discuss.equalsIgnoreCase(type)?View.VISIBLE:View.GONE);
		Tools.setHeadImg(info.usercode, headimg);
		headname.setText(info.username);
		time.setText(info.time);
		Html.TagHandler tagHandler = new Html.TagHandler() {
			
			 int start =0;
			    int end = 0;
			    int i = 0;
			    @Override
			    public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {
			        String code = "";
			            if ("span".equalsIgnoreCase(s)){
			                if (b){
			                    start = editable.length();
			                }else{
			                    end = editable.length();
			                    code = codes.get(i);
			                    String content = editable.subSequence(start,editable.length()).toString();
			                    editable.setSpan(new AtClickSpan(code,context),start,
			                            editable.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			                    i++;
			                }
			            }

			    }
		};
		String str = "";
		if(info.contenthtml.length() > 0){
			
		str = info.contenthtml;
//		Log.d(TAG, "Html content:"+str);
//		Log.d(TAG, "Html.fromHtml:"+Html.fromHtml(str, DetailDiscussFragment.setExpressIcon(context, content), tagHandler));
		 content.setText(Html.fromHtml(str, DetailDiscussFragment.setExpressIcon(context, content), tagHandler));
//		 (str, DetailDiscussFragment.setExpressIcon(context, content), null)
		}else{
			str = info.content;
			content.setText(str);
		}
		
		
		
		 
		
		//表情
		
//		SpannableString ss2 = new SpannableString(Html.fromHtml(info.content, expressGetter, tagHandler));
		
//		sb.append(ss2);
       
        content.setMovementMethod(LinkMovementMethod.getInstance());
        talkbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.nameClick(position,"回复:"+"@"+info.username+" ");
				}
			}
		});
        
        
        final String userCode = info.usercode;
		 final String discussId = info.id;
		 convertview.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(userCode == null || !AccountInfo.instance().getLastUserInfo().getCode().equals(userCode))
					return false;
				delDiscussDialog(info);
				return false;
			}
		});
      
		}
		return convertview;
	}
	
	void delDiscussDialog(final DetailContentInfo info){
		String[] items = {"删除"};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示")
		.setItems(items,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0){
					howDelDiscussDialog(info);
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	
	void howDelDiscussDialog(final DetailContentInfo info){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("删除")
		.setMessage("确认删除吗?")
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				delDiscuss(info);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
	}
	
	void delDiscuss(final DetailContentInfo info){
		String url = "/home/phone/discussion/delete";
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				if(data.getResultState() == ResultState.eSuccess){
					Toast.makeText(context, "删除成功"+data.getMsg(), Toast.LENGTH_SHORT).show();
					infos.remove(info);
					notifyDataSetChanged();
					if(onDelDiscussListener != null){
						onDelDiscussListener.deldiscuss();
					}
//					if(context instanceof ProgressDiscussActivity){
//						MatterDetailAcrtivity.getInstance().refreshthis();
//					}else{
//					MatterDetailAcrtivity.getInstance().DeleteRefreshthis();
//					}
				}else{
					Toast.makeText(context, "删除失败:"+data.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}.setUrl(url)
		.addParam("discussionId",info.id)
		.notifyRequest();
	}
	
	void setHtml(int position){
		
	}
	
	
	public void setOnDiscussButtonClickListener(OnDiscussButtonClickListener listener){
		this.listener = listener;
	}
	
	
	static ArrayList<String> setCodes(String content){
		if(content == null){
			return null;
		}
		ArrayList<String> codes = new ArrayList<String>();
		
		org.jsoup.nodes.Document document = Jsoup.parse(content);

	    Elements elements = document.getElementsByTag("span");
	    for (Element element : elements){
	        String code = element.attr("code");
	        codes.add(code);
	        
	    }
	    return codes;
	}

	public void setIsInTop(Boolean b , DetailList d){
		isInTop = b;
		this.detailList = d;
	}
	
	
	private ArrayList<String> getFileIds(){
		ArrayList<String> fileIds = new ArrayList<String>();
		for(int i = 0 ; i < detailList.imgs.size();i++){
			fileIds.add(detailList.imgs.get(i).fileid);
		}
		return fileIds;
	}
	
	public interface OnDelDiscussListener{
		void deldiscuss();
		void adddiscuss();
	}
	
	OnDelDiscussListener onDelDiscussListener;
	public void setOnDelDiscussListener(OnDelDiscussListener listener){
		this.onDelDiscussListener = listener;
	}
	
	
}
