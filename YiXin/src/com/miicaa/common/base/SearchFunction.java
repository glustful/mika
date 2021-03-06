package com.miicaa.common.base;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by LM on 14-9-2.
 * 搜索数据用
 */

public class SearchFunction  {

    public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
		this.text.setText(searchText);
	}

	String searchText;
    Button delText;
    EditText text;
    Context mContext;
    String url;
    View searchView;
    HashMap<String,String> paramMap;
    int pageCount;
    protected ProgressDialog progressDialog;
    OnSearchCallBack listener;
    Button serachButton ;
    boolean isCustom = false;
    String customName = "";
    boolean isShow = false;
    public SearchFunction(Context context,String url,boolean isShow){
        mContext = context;
        paramMap = new HashMap<String, String>();
        progressDialog = new ProgressDialog(context);

        setProgressDialog();
        this.url = url;
       this.isShow = isShow;
        searchView  = LayoutInflater.from(context).inflate(R.layout.matter_home_view,null);
        delText = (Button)searchView.findViewById(R.id.matter_search_del_view);
        text = (EditText)searchView.findViewById(R.id.matter_search_text);
        text.setImeActionLabel("搜索", EditorInfo.IME_ACTION_SEARCH);
        text.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        text.setSingleLine();
        text.setOnEditorActionListener(new OnEditorActionListener(){
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
					switch(actionId){
						case  EditorInfo.IME_ACTION_SEARCH: //actionDone 事件时提交登录
							readySearch();
							break;
					}				
				return false;
			}});
         serachButton = (Button)searchView.findViewById(R.id.matter_search_button);
         if(isShow)
         serachButton.setVisibility(View.GONE);
         else{
        	 serachButton.setVisibility(View.GONE);
         }
        serachButton.setOnClickListener(searchButtonClickListener);
        text.addTextChangedListener(searchWatcher);
        delText.setOnClickListener(delClickListener);
    }

    public SearchFunction(Context context, String url,
			View view, boolean isShow) {
    	 mContext = context;
         paramMap = new HashMap<String, String>();
         progressDialog = new ProgressDialog(context);

         setProgressDialog();
         this.url = url;
         this.isShow = isShow;
         searchView  = view;
         delText = (Button)searchView.findViewById(R.id.matter_search_del_view);
         text = (EditText)searchView.findViewById(R.id.matter_search_text);
         text.setImeActionLabel("搜索", EditorInfo.IME_ACTION_SEARCH);
         text.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
         text.setSingleLine();
         text.setOnEditorActionListener(new OnEditorActionListener(){
 			@Override
 			public boolean onEditorAction(TextView v, int actionId,
 					KeyEvent event) {
 				// TODO Auto-generated method stub
 					switch(actionId){
 						case  EditorInfo.IME_ACTION_SEARCH: //actionDone 事件时提交登录
 							readySearch();
 							break;
 					}				
 				return false;
 			}});
          serachButton = (Button)searchView.findViewById(R.id.matter_search_button);
          if(isShow)
              serachButton.setVisibility(View.GONE);
              else{
             	 serachButton.setVisibility(View.INVISIBLE);
              }
         serachButton.setOnClickListener(searchButtonClickListener);
         text.addTextChangedListener(searchWatcher);
         delText.setOnClickListener(delClickListener);
	}

	private void setProgressDialog(){
        progressDialog.setMessage("正在搜索，请稍后...");
        progressDialog.setTitle("搜索");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    //搜索数据linstener
    View.OnClickListener searchButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (searchText == null || searchText.equalsIgnoreCase("")){
                Toast.makeText(mContext, "请输入搜索的内容", 100).show();
                return;
            }
            Utils.hiddenSoftBorad(mContext);

           setPageCount(1);
           progressDialog.show();
            search();


        }
    };
    
    private void readySearch(){
    	 if (searchText == null || searchText.equalsIgnoreCase("")){
             Toast.makeText(mContext, "请输入搜索的内容", 100).show();
             return;
         }
         Utils.hiddenSoftBorad(mContext);

        setPageCount(1);
        progressDialog.show();
         search();

    }

    //搜索内容改变进行的操作
    TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            searchText = charSequence.toString();
            if (!searchText.trim().equalsIgnoreCase("")){
            	
                delText.setVisibility(View.VISIBLE);
                listener.textChange(true);
            }else{
                delText.setVisibility(View.GONE);
              
                
                listener.textChange(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    //删除搜索内容
    View.OnClickListener delClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            text.setText("");
            listener.deltext();
        }
    };
    //数据参数
    public void setParam(HashMap<String,String> paramMap){

    	this.paramMap.putAll(paramMap);

    }
    
    public void setCustomParam(HashMap<String, String> param,String name){
    	customName = name;
    	isCustom = true;
    	this.paramMap.clear();
    	this.paramMap.putAll(param);
    }


    public  void search(){
    	if(isCustom){
    		paramMap.put(customName, searchText);
    	}else{
    		Log.d("SearchFunction", "searchText:"+searchText);
        paramMap.put("searchText",searchText);
    	}
    	Log.d("SearchFunction", "param:"+paramMap+"url:"+url);
        new RequestAdpater(){

            @Override
            public void onReponse(ResponseData data) {

                    if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                    if(pageCount > 1){
                    	listener.addMore(data);
                    }else{
                    listener.search(data);
                    }
                pageCount ++;
                paramMap.put("pageNo", pageCount+"");
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam(paramMap)
                .notifyRequest();
    }

    //分页
    public void setPageCount(int count){
        pageCount = count;
        paramMap.put("pageNo", pageCount+"");

    }

    public int getPageCount(){
        return pageCount;
    }

   
    
    public View getSearchView(){
        return searchView;
    }

  
    
    public interface OnSearchCallBack{
    	void search(ResponseData data);
    	void addMore(ResponseData data);
    	void textChange(Boolean isText);
    	void deltext();
    	void clearRefresh();
    }
    
    public void setSearchCallBack(OnSearchCallBack listener){
    	this.listener = listener;
    }

    //清空搜索数据
    public void clearSearch(){
        if (!text.getText().toString().trim().equalsIgnoreCase("")) {
            text.setText("");
            if(listener != null){
            	listener.clearRefresh();
            }
        }
    }

	public void setHint(String string) {
		text.setHint(string);
		
	}

	public void cleanFocus() {
		this.text.clearFocus();
		
	}

    //未完全退出后要清除原来的搜索字符

}
