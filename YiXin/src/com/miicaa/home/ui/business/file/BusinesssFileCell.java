package com.miicaa.home.ui.business.file;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.ui.picture.PhotoCheck_;
import com.miicaa.utils.fileselect.BrowseFileActivity_;
import com.miicaa.utils.fileselect.DirFileListActivity_;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yxst.epic.yixin.utils.ImageUtils;

public class BusinesssFileCell extends BaseAdapter {

	ImageLoader imageLoader = ImageLoader.getInstance();
	ArrayList<JSONObject> jsdata;
	ArrayList<JSONObject> jsdataCopy;
	boolean isManager = false;
	ArrayList<PopupItem> items = new ArrayList<PopupItem>();
	Context mContext;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	LayoutInflater layoutInflater;
	DisplayImageOptions options;
	boolean isSearch = false;

	public BusinesssFileCell(Context context, ArrayList<JSONObject> data,
			boolean isSearch) {
		jsdata = new ArrayList<JSONObject>();
		jsdata.addAll(data);
		jsdataCopy = data;
		this.isSearch = isSearch;
		this.mContext = context;
		//initPopItem();
		layoutInflater = LayoutInflater.from(context);
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.accessory_file_ico_normal) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.accessory_file_ico_normal) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.accessory_file_ico_normal) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build(); // 创建配置过得DisplayImageOption对象

	}

	private void initPopItem(boolean isdir) {
		items.clear();
		
		PopupItem item;
		if(isdir){
			if(!isSearch){
				item = new PopupItem("上传图片", "pic");
				items.add(item);

				item = new PopupItem("上传文件", "file");
				items.add(item);
			}
			if(isManager){
				item = new PopupItem("移动到", "moveto");
				items.add(item);
				item = new PopupItem("重命名", "rename");
				items.add(item);

				item = new PopupItem("删除", "delete");
				items.add(item);
			}
			item = new PopupItem("取消", "cancel");
			items.add(item);
		}else{
			item = new PopupItem("星标文件", "star");
			items.add(item);
			
		}
		
		
	}

	public void refresh(ArrayList<JSONObject> data, boolean isManager) {
		jsdata.clear();
		jsdata.addAll(data);
		jsdataCopy = data;
		this.isManager = isManager;
		this.notifyDataSetChanged();
	}

	private void initDataJson(ViewHolder holder, int position) throws Exception {

		JSONObject jsonObject = jsdata.get(position);

		holder.mTitleText.setText(jsonObject.optString("name"));
		String time = DateHelper.formatDate("yyyy-MM-dd HH:mm",
				jsonObject.optLong("recordTime"));
		holder.mDateText.setText(time);
		holder.mCreatorText.setText(jsonObject.optString("userName"));
		//
		/*if (isSearch) {
			holder.mStarImg.setEnabled(false);
		}*/
		holder.mStarImg.setVisibility(View.VISIBLE);
		holder.mStarImg.setTag("" + position);
		String star = jsonObject.optString("star");
		if (star == null || star.equals("NONE")) {

			holder.mStarImg.setChecked(false);

		} else {
			holder.mStarImg.setChecked(true);

		}

		if (jsonObject.isNull("describ")) {
			holder.mDescriptText.setVisibility(View.INVISIBLE);
		} else {
			holder.mDescriptText.setVisibility(View.VISIBLE);
			if(!jsonObject.isNull("appSource")){
				holder.mDescriptText.setText("来自"+jsonObject.optString("appSource")+":"+jsonObject.optString("describ"));
			}else{
				holder.mDescriptText.setText(jsonObject.optString("describ"));
			}
		}
		holder.mHeadImg.setImageResource(R.drawable.accessory_file_ico_normal);
		if (!jsonObject.isNull("directory")) {
			if (jsonObject.optBoolean("directory")) {
				holder.mStarImg.setVisibility(View.GONE);
				holder.mHeadImg
						.setImageResource(R.drawable.accessory_file_ico_folder);

			} else {
				
				if(!jsonObject.isNull("fileType") && jsonObject.optString("fileType").equals("20")){
					
					String url = jsonObject.optString("img");
					holder.mHeadImg.setTag(url);
					imageLoader.displayImage(
							url, holder.mHeadImg, options,
							animateFirstListener);
				}else
				if (!jsonObject.isNull("ext")) {
					setKindsImage(holder.mHeadImg, jsonObject);
				}

			}
		} else {
			if(!jsonObject.isNull("fileType") && jsonObject.optString("fileType").equals("20")){
				String url = jsonObject.optString("img");
				holder.mHeadImg.setTag(url);
				imageLoader.displayImage(
						url, holder.mHeadImg, options,
						animateFirstListener);
			}else
			if (!jsonObject.isNull("ext")) {
				setKindsImage(holder.mHeadImg, jsonObject);
			}
		}

		if (jsonObject.isNull("kbSize")) {
			//holder.mSizeText.setText("未知");
			holder.mSizeText.setVisibility(View.GONE);
		} else {
			holder.mSizeText.setVisibility(View.VISIBLE);
			holder.mSizeText.setText("" + jsonObject.optInt("kbSize") + "kb");
		}

	}

	private void setKindsImage(ImageView mHeadImg, JSONObject jsonObject) {
		String optString = jsonObject.optString("ext").toLowerCase();
		if (optString.equals("txt")) {
			mHeadImg.setImageResource(R.drawable.accessory_file_ico_txt);
		} else if (optString.equals("pdf")) {
			mHeadImg.setImageResource(R.drawable.accessory_file_ico_pdf);
		} else if (optString.equals("doc")) {
			mHeadImg.setImageResource(R.drawable.accessory_file_ico_word);
		} else if (optString.equals("docx")) {
			mHeadImg.setImageResource(R.drawable.accessory_file_ico_word);
		} else if (optString.equals("zip")) {
			mHeadImg.setImageResource(R.drawable.accessory_file_ico_rar);
		} else if (optString.equals("rar")) {
			mHeadImg.setImageResource(R.drawable.accessory_file_ico_rar);
		} else if (optString.equals("ppt") || optString.equals("pptx")) {
			mHeadImg.setImageResource(R.drawable.accessory_file_ico_ppt);
		} else if (optString.equals("xlsx") || optString.equals("xls")) {
			mHeadImg.setImageResource(R.drawable.accessory_file_ico_execl);
		} else {
			if (!jsonObject.isNull("fileId")) {
				String url = UserAccount.mSeverHost
						+ "/docbase_srv/staticfile/resize/downLoad?fid="
						+ jsonObject.optString("fileId");
				mHeadImg.setTag(url);
				imageLoader.displayImage(
						url, mHeadImg, options,
						animateFirstListener);
				

			} else
				mHeadImg.setImageResource(R.drawable.accessory_file_ico_normal);
		}

	}

	@Override
	public int getCount() {

		return jsdata.size();
	}

	@Override
	public Object getItem(int i) {
		return jsdata.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		ViewHolder holder = new ViewHolder();
		LinearLayout linearLayout = new LinearLayout(mContext);

		final JSONObject jsonObject = jsdata.get(position);
		if (view == null) {
			view = layoutInflater.inflate(R.layout.business_file_list_item,
					null);
			// view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.matter_do_cell_selector));

			holder.mHeadImg = (ImageView) view
					.findViewById(R.id.business_file_head_img);
			holder.mStarImg = (CheckBox) view
					.findViewById(R.id.business_file_star_img);
			holder.mDescriptText = (TextView) view
					.findViewById(R.id.business_file_descript);
			holder.mTitleText = (TextView) view
					.findViewById(R.id.business_file_title);
			holder.mDateText = (TextView) view
					.findViewById(R.id.business_file_time);
			holder.mSizeText = (TextView) view
					.findViewById(R.id.business_file_size);
			holder.mCreatorText = (TextView) view
					.findViewById(R.id.business_file_creator);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.mStarImg.setOnCheckedChangeListener(null);
		try {
			initDataJson(holder, position);

		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.mStarImg
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						/*if (isSearch)
							return;*/
						
						if (buttonView.getTag().toString()
								.equals("" + position)) {
							toggleStar(buttonView, position);
						}

					}
				});
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				singleClick(position, view);

			}

		});
		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				
				showChoice(position, v);
				return true;
			}
		});
		return view;
	}

	protected void singleClick(int position, View view) {
		selectedPosition = position;
		final JSONObject jsonObject = jsdata.get(position);
		if (!jsonObject.isNull("directory")) {
			if (jsonObject.optBoolean("directory")) {
				if (isSearch) {
					((BusinessFileSearchActivity) mContext)
							.requestFolder(jsonObject);
				} else {
					((BusinessFileActivity) mContext).requestFolder(jsonObject);
				}
				return;
			}
		}
		if(jsonObject.isNull("ext") || !ImageUtils.isImage(jsonObject.optString("ext"))){
			
			Intent intent = new Intent(mContext,BrowseFileActivity_.class);
			intent.putExtra("mId", jsonObject.optString("fileId"));
			intent.putExtra("name", jsonObject.optString("name"));
			intent.putExtra("isAdmin", isManager);
			Log.d("BusinessFileCell", "isManager :"+isManager);
			intent.putExtra("json", jsonObject.toString());
			intent.putExtra("fileType", jsonObject.optString("fileType"));
			((Activity) mContext).startActivityForResult(intent,BusinessFileActivity.MOVETOFILE_CODE);
			((Activity) mContext).overridePendingTransition(
					R.anim.my_slide_in_right, R.anim.my_slide_out_left);
			return ;
		}
		String parentId = "";
		if (isSearch) {
			parentId = jsonObject.optString("parentId");
		} else {
			parentId = ((BusinessFileActivity) mContext).getParentId();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		ArrayList<String> names = new ArrayList<String>();
		map.put("menu", "1");
		map.put("type", "");
		map.put("star", "false");
		if(!parentId.equals("")&&!parentId.equals("all"))
		map.put("navi", parentId);
		Intent intent = new Intent(mContext,PhotoCheck_.class);
		intent.putExtra("names", names);
		intent.putExtra("name", jsonObject.optString("fileId"));
		intent.putExtra("map", map);
		((Activity) mContext).startActivityForResult(intent, BusinessFileActivity.PHOTOCHECK);
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);
		
	}

	protected void showChoice(final int position, final View v) {
		selectedPosition = position;
		final JSONObject jsonObject = jsdata.get(position);
		//ArrayList<PopupItem> temps = new ArrayList<PopupItem>();
		//temps.addAll(items);
		if (!jsonObject.isNull("directory")) {
			if (jsonObject.optBoolean("directory")) {

				
				initPopItem(true);
			} else {
				initPopItem(false);
				String creator = jsonObject.optString("userCode");
				String name = AccountInfo.instance().getLastUserInfo().getCode();
				if(isManager || creator.equals(name)){
					PopupItem item = new PopupItem("编辑", "edit");
					items.add(item);
					item = new PopupItem("移动到", "moveto");
					items.add(item);
				

					item = new PopupItem("删除", "delete");
					items.add(item);
				}
				if (isStar(jsonObject)) {
					items.get(0).mContent = "取消星标";
				} else {
					items.get(0).mContent = "星标文件";
				}
				PopupItem item = new PopupItem("取消", "cancel");
				items.add(item);
				
			}
		} else {
			initPopItem(false);
			String creator = jsonObject.optString("userCode");
			String name = AccountInfo.instance().getLastUserInfo().getCode();
			if(isManager || creator.equals(name)){
				PopupItem item = new PopupItem("编辑", "edit");
				items.add(item);
				item = new PopupItem("移动到", "moveto");
				items.add(item);
			

				item = new PopupItem("删除", "delete");
				items.add(item);
			}
			if (isStar(jsonObject)) {
				items.get(0).mContent = "取消星标";
			} else {
				items.get(0).mContent = "星标文件";
			}
			PopupItem item = new PopupItem("取消", "cancel");
			items.add(item);
		}
		if(items.size()<2)
			return;
		BottomScreenPopup.builder(mContext).setItems(items)
				.setDrawable(R.drawable.white_color_selector)
				
				.setMargin(false).setOnMessageListener(new OnMessageListener() {

					@Override
					public void onClick(PopupItem msg) {
						if (msg.mCode.equals("star")) {
							CheckBox star = (CheckBox) v
									.findViewById(R.id.business_file_star_img);
							star.setChecked(!star.isChecked());
						} else if (msg.mCode.equals("moveto")) {
							moveTo(position);
						} else if (msg.mCode.equals("rename")) {
							rename(position, v);
						} else if (msg.mCode.equals("pic")) {
							BusinessFileActivity.getIntance().uploadPic(jsonObject.optString("id"),jsonObject.optString("name"));
						} else if (msg.mCode.equals("file")) {
							BusinessFileActivity.getIntance().uploadFile(jsonObject.optString("id"),jsonObject.optString("name"));
						} else if (msg.mCode.equals("delete")) {
							remove(position, v);
						} else if (msg.mCode.equals("edit")) {
							editFile(position, v);
						}
					}
				}).show();

	}

	protected void editFile(int position, View v) {
		selectedView = v;
		selectedPosition = position;
		JSONObject obj = jsdata.get(position);
		System.out.println("ori="+obj);
		String des = "";
		if (!obj.isNull("describ")) {
			des = obj.optString("describ");
		}

		Intent intent = new Intent(mContext, BusinessFileNORActivity.class);
		intent.putExtra("type", "describ");
		intent.putExtra("describ", des);
		intent.putExtra("id", obj.optString("id"));
		intent.putExtra("rightType", obj.optString("rightType"));
		JSONArray arr = obj.optJSONArray("rightList");
		if(arr!=null)
		intent.putExtra("json", arr.toString());
		((Activity) mContext).startActivityForResult(intent,
				BusinessFileActivity.EDITFILE_CODE);
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);

	}
	
	protected void moveTo(int position) {
		JSONObject obj = jsdata.get(position);
		selectedPosition = position;
		Intent intent = new Intent(mContext, DirFileListActivity_.class);
		intent.putExtra("fileId", obj.optString("id"));
		((Activity) mContext).startActivityForResult(intent,BusinessFileActivity.MOVETOFILE_CODE);
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);

	}
	
	public void moveToSuccess(){
		jsdata.remove(selectedPosition);
		jsdataCopy.remove(selectedPosition);
	}

	protected void rename(int position, View v) {
		selectedView = v;
		selectedPosition = position;
		JSONObject obj = jsdata.get(position);
		String ext = "";
		String name = obj.optString("name");
		if(obj.isNull("directory") || !obj.optBoolean("directory")){
			if(name.contains(".")){
				ext = name.substring(name.lastIndexOf(".")+1);
				name = name.substring(0, name.lastIndexOf("."));
			}
		}
		Intent intent = new Intent(mContext, BusinessFileNORActivity.class);
		intent.putExtra("type", "rename");
		intent.putExtra("name", name);
		intent.putExtra("ext", ext);
		intent.putExtra("id", obj.optString("id"));
		((Activity) mContext).startActivityForResult(intent,
				BusinessFileActivity.RENAMEFILE_CODE);
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);

	}

	protected void remove(final int position, final View v) {
		new AlertDialog.Builder(mContext)
		.setTitle("删除")
		.setMessage("确认是否删除？")
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				secondRemove(position, v);
				
			}
		})
		.setNegativeButton("取消", null)
		.create()
		.show();
		

	}
	
	protected void secondRemove(final int position, View v){
		selectedView = v;
		selectedPosition = position;
		String url = mContext.getString(R.string.file_remove_url);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", jsdata.get(position).optString("id"));
		new RequestAdpater() {

			@Override
			public void onReponse(ResponseData data) {
				try {
					if (data.getResultState() == ResultState.eSuccess) {
						jsdata.remove(position);
						jsdataCopy.remove(position);
						notifyDataSetChanged();
//						notifyDataSetInvalidated();
						BusinessFileActivity.getIntance().isRefresh();

					} else {
						Toast.makeText(mContext, "删除失败：" + data.getMsg(), 100)
								.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub

			}
		}.setUrl(url).addParam(map).notifyRequest();
	}

	public boolean updateName(String name, int flag) {
		if (selectedView != null) {
			if (flag == 0) {
				ViewHolder holder = (ViewHolder) selectedView.getTag();
				holder.mTitleText.setText(name);
				try {
					jsdata.get(selectedPosition).put("name", name);
					jsdataCopy.get(selectedPosition).put("name", name);
				} catch (Exception e) {

				}
			} else if (flag == 1) {
				JSONObject obj = null;
				try {
					obj = new JSONObject(name);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(obj == null)
					return false;
				jsdata.set(selectedPosition, obj);
				jsdataCopy.set(selectedPosition, obj);
				ViewHolder holder = (ViewHolder) selectedView.getTag();
				holder.mDescriptText.setVisibility(View.VISIBLE);
				holder.mDescriptText.setText(obj.optString("describ"));
				
			}
		}
		return true;
	}

	String flag = "";
	View selectedView = null;
	int selectedPosition = 0;

	private void toggleStar(final CompoundButton view, final int position) {
		flag = "";
		final JSONObject jsonObject = jsdata.get(position);
		String fileId = jsonObject.optString("id");
		String url = mContext.getString(R.string.star_file_url);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", fileId);
		if (!isStar(jsonObject)) {
			flag = "on";
			map.put("flag", flag);
		}
		new RequestAdpater() {

			@Override
			public void onReponse(ResponseData data) {
				try {
					if (data.getResultState() == ResultState.eSuccess) {
						if (flag.equals("on")) {

							jsonObject.put("star", "true");
							jsdataCopy.get(position).put("star", "true");
						} else {

							jsonObject.put("star", "NONE");
							jsdataCopy.get(position).put("star", "NONE");
						}

					} else {
						Toast.makeText(mContext, "操作失败："+data.getMsg(), 100).show();
						notifyDataSetInvalidated();
						//view.setChecked(!view.isChecked());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub

			}
		}.setUrl(url).addParam(map).notifyRequest();
	}

	private boolean isStar(JSONObject obj) {
		if (!obj.isNull("star")) {
			if (obj.optString("star").equals("NONE")) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	class ViewHolder {
		ImageView mHeadImg;
		CheckBox mStarImg;
		TextView mDescriptText;
		TextView mTitleText;
		TextView mDateText;
		TextView mSizeText;
		TextView mCreatorText;

	}

	/**
	 * 图片加载第一次显示监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {

			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				if(imageView.getTag().toString().equals(imageUri))
					imageView.setImageBitmap(loadedImage);
			}
		}
	}
}
