package com.miicaa.home.ui.contactList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.common.base.Debugger;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.OnFinish;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.org.LableGroup;
import com.miicaa.home.ui.orgTreeList.OrgPersonListActivity;
import com.miicaa.home.ui.person.PersonHome;
import com.miicaa.home.ui.person.PersonHome_;


/**
 * Created by apple on 13-11-27.
 */
public class ContactList {
	
	static String TAG = "ContactList";
	
	private final static int CONTACT_DATA =  0x90;

	public static ContactList instance;
	public static final int SINGLESELECT = 0;
	public static final int MUTILSELECT = 1;
	int type;
	private Context mContext;
	private View mRootView = null;
	private ContactIndexListView letterListView;
	private EditText searchText;
	private Button searchClearButton;
	public  TextView overlay;
	private LinearLayout contentListLayout;

    private OnGetContacterListener
            onGetApproveListener;
    private ListView mListView;
    private ArrayList<String> arrangerName;
    private ArrayList<String> arrangeUserCode;
    private String openType;
    private String approveName;
    private int SHOW_BIG = 0X0;
    private int NOT_SHOW =0X1;
	//    private contactListAdapter listAdapter;
	ArrayList<ContactData> contentArray;//初始化的全部内容数据
	ArrayList<ContactData> contactListArray;//联系人的数据
	ArrayList<ContactData> trendsArray;//动态
    ArrayList<ContactData> arrangeList;
    ArrayList<ContactData> groupData;
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	ArrayList<LinearLayout> firstContactSearchLayoutArray;//初始化的全部layout数据
	ArrayList<LinearLayout> contactSearchLayoutArray;//搜索后的layout数据
    ArrayList<HeadGroup> headGroups;
    ArrayList<ContactData> contactDatas;
    ArrayList<ContactData> tmpDatas;
    MyListPersonAdapter listPersonAdapter;
    HashMap<Integer,Integer> scrollY;
    HashMap<Integer,CheckBox> checkMap;
    HashMap<Integer,Boolean> checkWhat;
    HashMap<Integer,Boolean> isBigWord;
    ArrayList<ContactViewShow> viewShows;
    
    
    ArrayList<String> cannotUidList;
    RelativeLayout serchLayout;
    private LableGroup mViewGroup;
	@SuppressLint("UseSparseArrays")
	public ContactList(Context context, ArrayList<ContactData> dataArrayParam,String openType,ArrayList<ContactViewShow> viewShows,int type) {
		instance = this;
		mContext = context;		
		this.type = type;
        this.openType = openType;
		contactListArray = dataArrayParam;
		alphaIndexer = new HashMap<String, Integer>();
        scrollY = new HashMap<Integer, Integer>();
        arrangerName = new ArrayList<String>();
        headGroups = new ArrayList<HeadGroup>();
        contactDatas = new ArrayList<ContactData>();
        tmpDatas = new ArrayList<ContactData>();
        arrangeUserCode = new ArrayList<String>();
		firstContactSearchLayoutArray = new ArrayList<LinearLayout>();
		contactSearchLayoutArray = new ArrayList<LinearLayout>();
        arrangeList = new ArrayList<ContactData>();
        checkMap = new HashMap<Integer, CheckBox>();
        checkWhat = new HashMap<Integer, Boolean>();
        isBigWord = new HashMap<Integer, Boolean>();
        this.viewShows = viewShows;
		initOverlay();//初始化中间的文字
		init();
	}
	

    private void setTopView(final int position,String code,final ArrayList<ContactViewShow> vs){//设置顶部的名字
        if (vs == null){
            return;
        }
        for (int i = 0; i < vs.size();i++){
            if (code.equals(vs.get(i).getCode())){
                vs.get(i).setViewShow(setViewGroup(vs.get(i).getName()));
                checkWhat.put(position,true);
                final  int p = i;
                final ContactViewShow v = vs.get(i);
                vs.get(i).getShowView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeTopView(v);
                    }
                });
            }
        }
    }

	public View getRootView() {
//		if (mRootView == null) {
//			init();
//		}
		return mRootView;
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mRootView = inflater.inflate(R.layout.contact_list_view, null);

		letterListView = (ContactIndexListView) mRootView.findViewById(R.id.contact_index_list);
		letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());//点击侧边栏事件
		mListView = (ListView)mRootView.findViewById(R.id.contact_list_view_list);
		serchLayout = (RelativeLayout) inflater.inflate(R.layout.contact_list_headview, null);
        mListView.addHeaderView(serchLayout);
		serchLayout.setFocusable(true);
        serchLayout.setFocusableInTouchMode(true);
		dataCreate();
		searchText = (EditText) serchLayout.findViewById(R.id.contact_list_search_cell_edit);
		searchText.setHint("查找姓名");
		searchText.setFocusable(true);
		searchText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				contactSearchLayoutArrayCreate();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
        mViewGroup = (LableGroup)mRootView.findViewById(R.id.contact_list_view_group);
		searchClearButton = (Button) serchLayout.findViewById(R.id.contact_list_search_cell_button);
		searchClearButton.setOnClickListener(searchClearButtonClick);
		cellCreate();
        
        listPersonAdapter= new MyListPersonAdapter();
        mListView.setAdapter(listPersonAdapter);

//        exList();
	}


	private void cellCreate() {


            Date startDate = new Date(System.currentTimeMillis());//获取当前时间
        ArrayList<ContactData> groupData =new ArrayList<ContactData>();
        for (int position = 0; position < contentArray.size(); position++) {//这里显示列表

            if (contentArray != null && contentArray.size() > 0) {
                if (contentArray.get(position).getDataType().equals("person")) {
                    final ContactData data = contentArray.get(position);
                    contactDatas.add(data);
                    setTopView(contactDatas.size()-1,data.getUserCode(), viewShows);
                }

            }

        }

	}

    private View setViewGroup(String name){
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setMinimumHeight(40);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout sRelativeLayout = new RelativeLayout(mContext);
        ViewGroup.LayoutParams rParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        sRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.labelbg));
        sRelativeLayout.setLayoutParams(rParams);;
        TextView textView = new TextView(mContext);
        RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tParams.setMargins(5, 0, 5, 0);
        tParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(tParams);
        textView.setText(name);
        sRelativeLayout.addView(textView);
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(20, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(sRelativeLayout);
        linearLayout.addView(relativeLayout);
        mViewGroup.addView(linearLayout);
        ((View)mViewGroup.getParent()).setPadding(10, 10, 10, 10);
        return linearLayout;
    }

    public ArrayList<ContactViewShow> getArrangeCon(){
        return viewShows;
    }
    
    public void setCannotUid(ArrayList<String> c){
    	cannotUidList = c;
//    	listPersonAdapter.refresh();
    }
    
    ArrayList<String> mCannotUserCode;
    public void setCannotUserCode(ArrayList<String> code){
    	this.mCannotUserCode = code;
    }


    private ArrayList<ContactData> resetCon() {
    	ArrayList<ContactData> c = new ArrayList<ContactData>();
        for (int position = 0; position < contentArray.size(); position++) {//这里显示列表
            if (contentArray != null && contentArray.size() > 0) {
                //TODO:目前要person，以后完善
                if (contentArray.get(position).getDataType().equals("person")) {
                    final ContactData data = contentArray.get(position);
                    c.add(data);
                }
            }
        }
        return c;
    }

    private void contactSearchLayoutArrayCreate() {
        contactDatas.clear();
        checkWhat.clear();
        checkMap.clear();
        isBigWord.clear();
		contactSearchLayoutArray.clear();
        if (searchText.getText().length() < 1) {
            contactDatas.addAll(resetCon());
            letterListView.setVisibility(View.VISIBLE);
            searchClearButton.setVisibility(View.GONE);
            listPersonAdapter.notifyDataSetChanged();
            return;
        }
        for (int i = 0; i < contentArray.size(); i++) {
			ContactData data = contentArray.get(i);

				if (data.getDataType().equals("person") && !data.getPingyinKey().equals("#")) {
					letterListView.setVisibility(View.GONE);
					searchClearButton.setVisibility(View.VISIBLE);

					// 拼音首字母
					String quanpinFrist = data.getQuanPingFirst().trim().toUpperCase();
					String searchTextStr = searchText.getText().toString().trim().toUpperCase();
					char[] quanpinFirstChar = quanpinFrist.toCharArray();
					char[] searchTextChar = searchTextStr.toCharArray();

					// 汉字
					String quanpinCN = data.getName().trim();
					String searchTextStrCN = searchText.getText().toString().trim();
					char[] quanpinCharCN = quanpinCN.toCharArray();
					char[] searchTextCharCN = searchTextStrCN.toCharArray();

					// 全拼
					String quanpin = data.getQuanPing().trim().toUpperCase();
					char[] quanpinChar = quanpin.toCharArray();

					// 全拼
					// 1、第一行是拼音首字母的检索。
					// 2、第二行是汉字的检索。
					// 3、第三行全拼的检索
					if (quanpinFrist.equals(searchTextStr) || quanpinFirstChar == searchTextChar || getCharArrayEquals(quanpinFirstChar, searchTextChar) || (quanpinFrist.indexOf(searchTextStr) != -1) ||
							quanpinCN.equals(searchTextStrCN) || quanpinCharCN == searchTextCharCN || getCharArrayEquals(quanpinCharCN, searchTextCharCN) || (quanpinCN.indexOf(searchTextStrCN) != -1) ||
							quanpin.equals(searchTextStr) || quanpinChar == searchTextChar || getCharArrayEquals(quanpinChar, searchTextChar) || (quanpin.indexOf(searchTextStr) != -1)) {
//						contactSearchLayoutArray.add(firstContactSearchLayoutArray.get(i));
                        contactDatas.add(data);
					}
				}

		}
//		cellShow();
        listPersonAdapter.notifyDataSetChanged();
	}



	private void cellShow() {
		for (int position = 0; position < firstContactSearchLayoutArray.size(); position++) {
			Log.d("position---:", String.valueOf(position));
			if (firstContactSearchLayoutArray.get(position) != null) {
				firstContactSearchLayoutArray.get(position).setVisibility(View.GONE);
			}
		}
		for (int position = 0; position < contactSearchLayoutArray.size(); position++) {//显示顶部大写字母
			if (contactSearchLayoutArray.get(position) != null) {
				contactSearchLayoutArray.get(position).setVisibility(View.VISIBLE);
				TextView headTitle = (TextView) contactSearchLayoutArray.get(position).findViewById(R.id.contact_list_cell_headTitle);
				if (headTitle != null) {
					if (searchText.getText().length() < 1 && headTitle.getText().length() > 0) {
						headTitle.setVisibility(View.VISIBLE);
					} else {
						headTitle.setVisibility(View.GONE);
					}
				}
			}
		}
	}

	View.OnClickListener searchClearButtonClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			searchText.setText("");
		}
	};
	View.OnClickListener contactListPersonCellClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			String tag = (String) view.getTag();

			Intent intent = new Intent(mContext, PersonHome.class);
			Bundle bundle = new Bundle();
			bundle.putString("userCode", tag);
			intent.putExtra("bundle", bundle);
			mContext.startActivity(intent);
			((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
//
		}
	};
	
	
	View.OnClickListener contactListOrgTreeCellClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			String type = view.getTag().toString();
			if (type.equals("unit")) {
				OrgPersonListActivity.showOrgTree(mContext,
                        "通讯录",
                        OrgPersonListActivity.OrgType.UNIT_USER,
                        OrgPersonListActivity.SelectType.SINGLE,
                        null,
                        new OnFinish() {
                            @Override
                            public void onSuccess(JSONObject res) {

                            }

                            @Override
                            public void onFailed(String msg) {

                            }
                        });
			} else if (type.equals("group")) {
				OrgPersonListActivity.showOrgTree(mContext,
						"通讯录",
						OrgPersonListActivity.OrgType.GROUP_USER,
						OrgPersonListActivity.SelectType.SINGLE,
						null,
						new OnFinish() {
							@Override
							public void onSuccess(JSONObject res) {
								Debugger.d("aaa", "selected finish!!!");
							}

							@Override
							public void onFailed(String msg) {

							}
						});
			}
		}
	};

	private boolean getCharArrayEquals(char[] quanpinFirstChar, char[] searchTextChar) {
		if (searchTextChar.length < quanpinFirstChar.length) {
			for (int i = 0; i < searchTextChar.length; i++) {
				if (searchTextChar[i] != quanpinFirstChar[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == CONTACT_DATA ){
				contactDatas.clear();
			    contactDatas.addAll(resetCon());
				listPersonAdapter.refresh();
			}
			super.handleMessage(msg);
		}
		
	};
	
	public void refreshList(final ArrayList<ContactData> list){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				contactListArray.clear(); 
			    contactListArray.addAll(list);
				dataCreate();
				mHandler.obtainMessage(CONTACT_DATA).sendToTarget();
			}
		}).start();

	}
	
//	private void  collectArray(){
//		contentArray = new ArrayList<ContactData>();
//		
//	}

//	ArrayList<Object> arrayObject = new ArrayList<Object>();
	private void dataCreate() {
		contentArray = new ArrayList<ContactData>();
		ArrayList<ContactData> tmpContentArray = new ArrayList<ContactData>();

		for (int i = 0; i < contactListArray.size(); i++) {
			tmpContentArray.add(contactListArray.get(i));
		}
//		重新进行一次排序
		for (int i = 0; i < letterListView.letterStrings.length; i++) {
			for (int j = 0; j < tmpContentArray.size(); j++) {
				ContactData data = tmpContentArray.get(j);
				String quanpinFrist = data.getPingyinKey();
				if (letterListView.letterStrings[i].equals(quanpinFrist)) {
					contentArray.add(tmpContentArray.get(j));//侧边字母和联系人首字母相等那么就添加到contentArray里
					
				}
			}
		}
		alphaIndexer = new HashMap<String, Integer>();
		for (int i = 0; i < contentArray.size(); i++) {
			// 当前汉语拼音首字母
			String currentStr = getAlpha(contentArray.get(i).getPingyinKey());//得到大写字母
			// 上一个汉语拼音首字母，如果不存在为“ ”
			String previewStr = (i - 1) >= 0 ? getAlpha(contentArray.get(i - 1)
					.getPingyinKey()) : " ";//首字母
			if (!previewStr.equals(currentStr)) {
				String name = getAlpha(contentArray.get(i).getPingyinKey());
				alphaIndexer.put(name, i);
			}
		}
	}

	private ArrayList<ContactData> trendsDataCreate() {
		ArrayList<ContactData> trendsArray = new ArrayList<ContactData>();
		for (int i = 0; i < 4; i++) {
			ContactData trends = new ContactData();
			trends.setPingyinKey("★");
			trends.setName("动态提醒" + i);
			trends.setDataType("trends");
			trendsArray.add(trends);
		}
		return trendsArray;
	}

	private class LetterListViewListener implements
            ContactIndexListView.OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(final String s, final boolean isShow) {
			overlay.setText(s);
			if (isShow) {
				overlay.setVisibility(View.VISIBLE);
			} else {
				overlay.setVisibility(View.GONE);
			}
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				if (position == 0) {
					mListView.scrollTo(0, 0);
				} else {
//					contentScroll.smoothScrollTo(0, firstContactSearchLayoutArray.get(position).getTop() + (int) (50 * mContext.getResources().getDisplayMetrics().density));
                    int i = getFirstPosition(s);
                    mListView.setSelection(i);
				}

//                Toast.makeText(mContext,"--------" + String.valueOf(position)+"--------"+firstContactSearchLayoutArray.get(position).getTop(), Toast.LENGTH_LONG).show();
			}
		}
	}

    private int getFirstPosition(String s){
        int positon = 0;
        for (int i = 0 ; i < contactDatas.size();i++){
            String str = getAlpha(contactDatas.get(i).getPingyinKey());
            if (s.equalsIgnoreCase(str)){
                positon = i;
                break;
            }
        }
        return positon;
    }

	// 获得汉语拼音首字母
	private String getAlpha(String str) {
		if (str == null) {
			return "★";
		}

		if (str.trim().length() == 0) {
			return "★";
		}
		char c = str.trim().substring(0, 1).charAt(0);//首字母
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else if (String.valueOf(c).equals("#")) {
			return "#";
		} else if (String.valueOf(c).equals("★")) {
			return "★";
		} else {
			return "#";
		}
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		overlay = (TextView) inflater.inflate(R.layout.contact_list_center_text, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}
	
	

    class MyListPersonAdapter extends BaseAdapter{


        MyListPersonAdapter(){


        }
        
        void refresh(){
        	this.notifyDataSetChanged();
        }
        
        void refresh(ArrayList<ContactData> c){
//        	contactDatas.clear();
//        	contactDatas.addAll(c);
        	refresh();
        }
        @Override
        public int getCount() {
            return contactDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ContactData contactData = contactDatas.get(i);
            String previewStr = (i-1)>=0?getAlpha(contactDatas.get(i-1).getPingyinKey()):"";//前一个首字母
            String currStr = contactData.getPingyinKey();//后一个名字的首字母
            ChildViewHolder childViewHolder;
            if (view == null){
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.contact_list_cell, null);
                childViewHolder = new ChildViewHolder();
                childViewHolder.contactName = (TextView)view.findViewById(
                        R.id.contact_list_cell_contactName);
                childViewHolder.headImg = (ImageView)view.findViewById(
                        R.id.contact_list_cell_headPhoto);
                childViewHolder.mCheckBox = (CheckBox)view.findViewById(
                        R.id.contact_list_cell_check);
                childViewHolder.personButton = (Button)view.findViewById(
                        R.id.contact_list_cell_button);
                childViewHolder.headTitleView = (TextView)view.findViewById(
                        R.id.contact_list_cell_headTitle);
                view.setTag(childViewHolder);
            }else{
                childViewHolder = (ChildViewHolder)view.getTag();
            }
            final ChildViewHolder holder = childViewHolder;
            if (!previewStr.equals(currStr)){
            	 holder.headTitleView.setVisibility(View.VISIBLE);
                 holder.headTitleView.setText(currStr);//显示大字母
            }else{
            	holder.headTitleView.setVisibility(View.GONE);
            }
            holder.contactName.setText(contactData.getName());
            holder.mCheckBox.setChecked(contactData.isSelect());
            Tools.setHeadImgWithoutClick(contactData.getUserCode(),childViewHolder.headImg);
            holder.personButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                	contactData.setSelect(!contactData.isSelect());
                	holder.mCheckBox.setChecked(contactData.isSelect());
                	if(type==MUTILSELECT){
                	
                	if(contactData.isSelect()){
                		arrangeList.add(contactData);
                        ContactViewShow show = addNewViewShow(contactData);
                        viewShows.add(show);
                	}else{
                		for (int j = 0 ; j < viewShows.size();j++){
                            if (viewShows.get(j).getCode().equals(contactData.getUserCode())){
                                mViewGroup.removeView(viewShows.get(j).getShowView());
                                viewShows.remove(viewShows.get(j));
                                if(mViewGroup.getChildCount()==0){
                                	((View)mViewGroup.getParent()).setPadding(0, 0, 0, 0);
                                }
                            }
                        }
                        arrangeList.remove(contactData);
                	}
                	}else{
                		
                		for(ContactData cd:contactDatas){
                			cd.setSelect(false);
                		}
                		contactData.setSelect(true);
                		arrangeList.clear();
                		viewShows.clear();
                		mViewGroup.removeAllViews();((View)mViewGroup.getParent()).setPadding(0, 0, 0, 0);
                    	if(contactData.isSelect()){
                    		arrangeList.add(contactData);
                            ContactViewShow show = addNewViewShow(contactData);
                            viewShows.add(show);
                    	}
                    	MyListPersonAdapter.this.notifyDataSetChanged();
                	}
                    if(selectChangeListener != null){
                    	selectChangeListener.onSelectChange(viewShows.size());
                    }
                }
            });
        
            if (ContactUtil.USER_SELECT.equalsIgnoreCase(openType)){
                childViewHolder.mCheckBox.setVisibility(View.VISIBLE);
                if((cannotUidList != null && cannotUidList.contains(String.valueOf(contactData.getUid())))
                		|| (mCannotUserCode != null && mCannotUserCode.contains(String.valueOf(contactData.getUserCode())))){
                	childViewHolder.mCheckBox.setEnabled(false);
                	childViewHolder.mCheckBox.setButtonDrawable(R.drawable.select_kuang_cannot);
                	childViewHolder.personButton.setEnabled(false);
                }else{
                	childViewHolder.mCheckBox.setEnabled(true);
                	childViewHolder.personButton.setEnabled(true);
                	childViewHolder.mCheckBox.setButtonDrawable(R.drawable.select_kuang_selector);
                }
              
            }else{
                childViewHolder.personButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    	
                    	PersonHome_.intent(mContext)
                    	.mUserCode(contactData.getUserCode())
                    	.start();
                        ((Activity) mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
                    }
                });
            }
            return view;
        }
    }

    public void setOnGetApproveListener(OnGetContacterListener
                                                onGetApproveListener){
        this.onGetApproveListener = onGetApproveListener;
    }

    public interface OnGetContacterListener{
        public void getApprover(ContactData approveName);
    }
    
    
    ContactViewShow addNewViewShow(ContactData data){
    	final ContactViewShow show = new ContactViewShow();
    	  show.name = data.getName();
          show.code = data.getUserCode();
          show.uid = String.valueOf(data.getUid());
          show.uidName = show.uid+"@user";
          show.setViewShow(setViewGroup(data.getName()));
          show.getShowView().setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  removeTopView(show);
              }
          });
          return show;
    }


    class HeadGroup{

        String headTitle;
        ArrayList<ContactData>  groupData = new ArrayList<ContactData>();
        HeadGroup(String title,ArrayList<ContactData> data){
            headTitle = title;
            groupData = data;
        }
    }


    class ChildViewHolder{//通讯录人的holder
                    TextView contactName;
                    CheckBox mCheckBox;
                    Button personButton;
                    ImageView headImg ;
                    TextView treadsText;
        TextView headTitleView;
    }

    private void removeTopView(ContactViewShow show){
        mViewGroup.removeView(show.getShowView());
        viewShows.remove(show);
        if(mViewGroup.getChildCount()==0){
        	((View)mViewGroup.getParent()).setPadding(0, 0, 0, 0);
        }
        for (int i =0;i<contactDatas.size();i++){
            if (show.getCode().equals(contactDatas.get(i).getUserCode())) {
            	contactDatas.get(i).setSelect(false);
            	listPersonAdapter.notifyDataSetChanged();
            }
        }
        if(selectChangeListener != null){
        	selectChangeListener.onSelectChange(viewShows.size());
        }

    }
    
    
    Boolean isContains(ArrayList<String> aList, String b){
    	for(String s : aList){
    		if(s.equals(b))
    			return true;
    	}
    	return false;
    }
    
    public ContactList setSelectChangeListener(OnSelectChangeListener listener){
    	this.selectChangeListener = listener;
    	return this;
    }
    
    private OnSelectChangeListener selectChangeListener;
    public interface OnSelectChangeListener{
		void onSelectChange(int selectCount);
	}
   

}