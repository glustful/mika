package com.miicaa.utils.fileselect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.ui.business.file.UploadFileActivity;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.AuthorityUtils.AuthorityState;
import com.miicaa.utils.ViewHolder;
import com.yxst.epic.yixin.MyApplication;

@EActivity(R.layout.activity_file_list)
public class FileListActivity extends ListActivity{
	static String TAG = "FileListActivity";
	
	private final static int allowNormalUser = 20;
	private final static int allowPayForUser = 1024;
	
	@ViewById(R.id.headView)
	RelativeLayout headLayout;
	@ViewById(R.id.dirButton)
	Button dirButton;
	@ViewById(R.id.cancleButton)
	Button cancelButton;
	
	final static long MB = 1024*1024;
	static List<String> selectFileItems = new ArrayList<String>();
	List<MyFileItem> fileItems;
	String[] baimingDan = {"rar","doc","docx","pdf","ppt","pptx","zip","txt","xls","xlsx","mmap"};
	ListFileAdapter adapter;
	String path;
	
	@AfterInject
	void afterInject(){
		path = Environment.getExternalStorageDirectory().getPath();
		fileItems = new ArrayList<MyFileItem>();
		adapter = new ListFileAdapter(this);
	}
	
	@AfterViews
	void afterView(){
		mToast = Toast.makeText(this, "",Toast.LENGTH_SHORT);
		Button commitBtn = (Button)headLayout.findViewById(R.id.commitButton);
		cancelButton.setVisibility(View.VISIBLE);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		((TextView)headLayout.findViewById(R.id.headTitle)).setText("选择文件");
		headLayout.findViewById(R.id.cancleButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		headLayout.findViewById(R.id.cancleButton).setVisibility(View.VISIBLE);
		commitBtn.setText("确认");
		commitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<MyFileItem> items = getSelectFileItem();
				Intent data = new Intent();
				data.putExtra("data", items);
				Log.d(TAG, "select item onClick(View v) :"+"..."+items);
				setResult(RESULT_OK, data);
				if(getIntent().getBooleanExtra("upload", false)){
					data.setClass(FileListActivity.this, UploadFileActivity.class);
					startActivity(data);
				}
				finish();
			}
		});
		dirButton.setVisibility(View.GONE);
		refreshListItems(path);
		setListAdapter(adapter);
	}
	
	private ArrayList<MyFileItem> getSelectFileItem(){
		ArrayList<MyFileItem> items = new ArrayList<MyFileItem>();
		for(MyFileItem item : fileItems){
			if(item.isSelect){
				items.add(item);
			}
		}
		return items;
	}
	
	private void refreshListItems(String path){
		fileItems = buildListForSimpleAdapter(path);
		adapter.notifyDataSetChanged();
	}
	
	private List<MyFileItem> buildListForSimpleAdapter(String path) {  
		this.path = path;
        File[] files = new File(path).listFiles();  
        List<MyFileItem> items = new ArrayList<MyFileItem>();
        List<MyFileItem> dirItems = new ArrayList<MyFileItem>();
        List<MyFileItem> fileItems = new ArrayList<MyFileItem>();
//        MyFileItem item = new MyFileItem();
//        item.name = "/";
//        item.path = "回到根目录";
//        items.add(item);
        MyFileItem item2 = new MyFileItem();
        item2.name = "..";
        item2.path = "返回上一层";
        items.add(item2);
        for(File file : files){
        	if(!file.isDirectory() && !extBaiMingdan(file.getName())){
        		continue;
        	}
//        	int size = (int) (file.length() / MB);
//        	if(size > 20){
//        		continue;
//        	}
        	MyFileItem item_ = new MyFileItem();
        	item_.name = file.getName();
        	item_.path = file.getPath();
        	if(file.isDirectory()){
        		dirItems.add(item_);
        	}else{
        		fileItems.add(item_);
        	}
        }
        items.addAll(dirItems);
        items.addAll(fileItems);
        return items;  
    }
	
	private void goToParent(String path) {  
        File file = new File(path);  
        File str_pa = file.getParentFile();  
        if(str_pa == null){  
            Toast.makeText(this,  
                    "已经是根目录",  
                    Toast.LENGTH_SHORT).show();  
            refreshListItems(path);   
        }else{  
            path = str_pa.getAbsolutePath();  
            refreshListItems(path);   
        }  
    } 
	
	private void gotoRoot(){
		refreshListItems("/");
	}
	
	
	
	@Override
	protected void onDestroy() {
		selectFileItems.clear();
		super.onDestroy();
	}



	class ListFileAdapter extends BaseAdapter{
		LayoutInflater inflater;

		public ListFileAdapter(Context context) {
			Log.d(TAG, "select FileItem in adapter + ..."+selectFileItems.size());
			inflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return fileItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup viewGroup) {
			final MyFileItem item = fileItems.get(position);
			if(convertView == null)
			{
				convertView = inflater.inflate(R.layout.list_file_item_view, null);
			}
			ImageView image = ViewHolder.get(convertView, R.id.iamge);
			TextView name = ViewHolder.get(convertView, R.id.name);
			TextView path = ViewHolder.get(convertView, R.id.path);
			final CheckBox checkBox = ViewHolder.get(convertView, R.id.checkBox);
			
			name.setText(item.name);
			path.setText(item.path);
			final File file = new File(item.path);
			if(position == 0 || file.isDirectory()){
				checkBox.setVisibility(View.GONE);
				image.setImageDrawable(getResources().getDrawable(R.drawable.accessory_file_ico_folder));
			}else{
				checkBox.setVisibility(View.VISIBLE);
				image.setImageDrawable(FileResouceIcon.getResouceIconId(FileListActivity.this, file.getName()));
				if(selectFileItems.contains(item.name+item.path))
				item.isSelect = true;
				else
				item.isSelect = false;
			}
			checkBox.setChecked(item.isSelect);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(position == 0 ){
						goToParent(FileListActivity.this.path);
					}
						else if(file.isDirectory()){
						refreshListItems(item.path);
					}else{
					item.isSelect = !item.isSelect;
					if(item.isSelect){
						if(!allowSelect(item.path)){
							showNotSelect();
							item.isSelect = !item.isSelect;
						}else{
						selectFileItems.add(item.name+item.path);
						}
					}
					else{
						selectFileItems.remove(item.name+item.path);
					}
					checkBox.setChecked(item.isSelect);
				}
				}
			});
			return convertView;
		}
		
		
		
	}
	
	Toast mToast;
	private void showNotSelect(){
		if(AllUtils.NORMAL_User == MyApplication.getInstance().getAuthority(AuthorityState.ePhoto)){
			mToast.setText("您是非付费用户，只能上传"+allowNormalUser+"MB以下的文件");
		}else{
			mToast.setText("最大上传限制为"+allowPayForUser+"MB");
		}
		mToast.show();
	}
	
	 private Boolean allowSelect(String path){
	    	File file = new File(path);
	    	int userAuthority = MyApplication.getInstance().getAuthority(AuthorityState.ePhoto);
	    	if(file.exists()){
	    		if(AllUtils.NORMAL_User == userAuthority){
	    			if(file.length()/AllUtils.MB > allowNormalUser)
	    			return false;
	    		}else if(file.length()/AllUtils.MB > allowPayForUser ){
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	
	private Boolean extBaiMingdan(String fileName){
		assert fileName != null;
		int i = fileName.lastIndexOf(".");
		if(i <= 0 ){
			return false;
		}
		String ext = fileName.substring(i+1, fileName.length());
		for(int count = 0; count < baimingDan.length; count++){
			Log.d(TAG, "baimingdan"+">>>"+baimingDan[count]+">>>"+ext);
			if(baimingDan[count].equals(ext)){
				return true;
			}
		}
		return false;
	}
}
