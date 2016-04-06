package com.miicaa.utils.fileselect;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.utils.ViewHolder;
import com.yxst.epic.yixin.utils.Utils;


@EActivity(R.layout.activity_file_list)
public class DirFileListActivity extends ListActivity{

	static String TAG = "DirFileListActivity";
	
    ArrayList<DirNodeTree> childDirInfos;
    SparseArray<ArrayList<DirNodeTree>> mapDirInfos;
    DirNodeTree dirNodeTree;
    DirInfoAdapter dirInfoAdapter;
	
	@ViewById(R.id.headView)
	RelativeLayout headLayout;
	@ViewById(R.id.dirButton)
	Button dirButton;
	@ViewById(android.R.id.list)
	ListView listView;
	@ViewById(android.R.id.empty)
	TextView empty;
	TextView headTitle;
	@Extra
	String fileId;
	
	@AfterInject
	void afterInject(){
		dirNodeTree = new DirNodeTree();
		childDirInfos = new ArrayList<DirNodeTree>();
		dirInfoAdapter = new DirInfoAdapter();
		mapDirInfos = new SparseArray<ArrayList<DirNodeTree>>();
		requestNetDir();
	}
	
	@AfterViews
	void afterView(){
		empty.setText("");
		headTitle = ((TextView)headLayout.findViewById(R.id.headTitle));
		headTitle.setText("总目录");
		dirButton.setText(">>返回上一级-企业文件");
		headLayout.findViewById(R.id.cancleButton).setVisibility(View.VISIBLE);
		Button commit = (Button)headLayout.findViewById(R.id.commitButton);
		commit.setText("移动");
		commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				moveNetDir();
			}
		});
		setListAdapter(dirInfoAdapter);
	}
	
	DirNodeTree nowNode;
	@ItemClick(android.R.id.list)
	void listClick(int position){
		nowNode = (DirNodeTree)dirInfoAdapter.getItem(position);
		dirInfoAdapter.refresh(nowNode.getChildNodes());
	}
	
	@Click(R.id.cancleButton)
	void cancelClick(){
		finish();
	}
	
	@Click(R.id.dirButton)
	void backUpClick(){
		if(nowNode.getParentNode() == null){
			//Toast.makeText(this, "已经是顶层文件夹", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		nowNode = nowNode.getParentNode();
		ArrayList<DirNodeTree> nodes = nowNode.getChildNodes();
		dirInfoAdapter.refresh(nodes);
	}
	
	private void requestNetDir(){
		String url = "/enterprisedoc/phone/efile/getDirTree";
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				JSONArray array = data.getJsonArray();
				Log.d(TAG, "requestNetDir()'s data is:..."+array.length());
				for(int i = 0; i < array.length();i++){
					JSONObject o = array.optJSONObject(i);
					String dirName = o.optString("name");
					String infoId = o.optString("id");
					DirNodeTree info = new DirNodeTree(dirName, infoId);
					if(fileId.equals(infoId)){
						continue;
					}
					if(!o.isNull("recordTime")){
						info.time = Utils.format(o.optLong("recordTime", 0), "yyyy-MM-dd HH:mm:ss");
					}
					if(!o.isNull("userName")){
						info.creator = o.optString("userName");
					}
					if(o.has("parentId")){
						String parentId = o.optString("parentId");
						info.parentId = parentId;
						childDirInfos.add(info);
					}else{
//					dirInfos.add(info);
					  info.parentId = dirNodeTree.infoId;
					  dirNodeTree.addChildNode(info);
					}
				}
				toDirInfoTree(childDirInfos);
				nowNode = dirNodeTree;
				ArrayList<DirNodeTree> rootNodes = dirNodeTree.getChildNodes();
				dirInfoAdapter.refresh(rootNodes);
			}
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.notifyRequest();
	}
	
	int count = 0;
	private void toDirInfoTree(ArrayList<DirNodeTree> childInfos){
		for(int i = 0 ; i < childInfos.size(); i++){
			dirNodeTree.insertJuniorNode(childInfos.get(i));
		}
	}
	
	private void moveNetDir(){
		String url = "/enterprisedoc/phone/efile/move";
		new RequestAdpater() {
			@Override
			public void onReponse(ResponseData data) {
				if(data.getResultState() == ResultState.eSuccess){
					
				Intent intent = new Intent();
				intent.putExtra("name", nowNode.dirName);
				intent.putExtra("id", nowNode.infoId);
				setResult(RESULT_OK,intent);
				finish();
				}else{
					Toast.makeText(DirFileListActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("target",nowNode.infoId)
		.addParam("id",this.fileId)
		.notifyRequest();
	}
	

		
		class DirInfoAdapter extends BaseAdapter{

			ArrayList<DirNodeTree> infos;
			LayoutInflater inflater;
			
			public DirInfoAdapter() {
				infos = new ArrayList<DirNodeTree>();
				inflater = LayoutInflater.from(DirFileListActivity.this);
			}
			void refresh(ArrayList<DirNodeTree> infos){
				this.infos.clear();
				if(infos != null){
				this.infos.addAll(infos);
				}
				Log.d(TAG, "DirInfoAdapter infos is + ..."+infos+"...");
				headTitle.setText(nowNode.getDirName());
				if(nowNode.getParentNode()==null){
					dirButton.setText(">>返回上一级-企业文件");
				}else{
				dirButton.setText(">>返回上一级-"+nowNode.getParentNode().getDirName());
				}
				notifyDataSetChanged();
			}
			@Override
			public int getCount() {
				return infos != null ? infos.size() : 0;
			}

			@Override
			public Object getItem(int position) {
				return infos.get(position);
			}
			
			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = inflater.inflate(R.layout.list_file_item_view, null);
				}
				DirNodeTree info = infos.get(position);
				ImageView image = ViewHolder.get(convertView, R.id.iamge);
				TextView name = ViewHolder.get(convertView, R.id.name);
				TextView path = ViewHolder.get(convertView, R.id.path);
				LinearLayout layout = ViewHolder.get(convertView, R.id.layout);
				layout.setVisibility(View.VISIBLE);
				TextView time = (TextView) layout.findViewById(R.id.time);
				TextView creator = (TextView) layout.findViewById(R.id.creator);
				 CheckBox checkBox = ViewHolder.get(convertView, R.id.checkBox);
				checkBox.setVisibility(View.GONE);
				path.setVisibility(View.GONE);
				name.setText(info.dirName);
				time.setText(info.getTime());
				creator.setText(info.getCreator());
				image.setImageDrawable(getResources().getDrawable(R.drawable.accessory_file_ico_folder));
				return convertView;
			}
			
		}
		
	}
	
	
	
	
