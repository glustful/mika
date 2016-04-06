package com.miicaa.common.base;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by LM on 14-9-2.
 * 搜索数据用
 */

public class ClassicSearchFunction  {

    String searchText;
    Button delText;
    EditText text;
    Context mContext;
    String url;
    View searchView;
    HashMap<String,String> paramMap;
    int pageCount;
    ProgressDialog progressDialog;
    OnSearchCallBack listener;
    Button serachButton ;

    public ClassicSearchFunction(Context context,String url){
        mContext = context;
        paramMap = new HashMap<String, String>();
        progressDialog = new ProgressDialog(context);

        setProgressDialog();
        this.url = url;
      
        searchView  = LayoutInflater.from(context).inflate(R.layout.classic_search_layout,null);
        delText = (Button)searchView.findViewById(R.id.classic_search_del_view);
        text = (EditText)searchView.findViewById(R.id.classic_search_text);
         serachButton = (Button)searchView.findViewById(R.id.classic_cancel_button);
        
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

    //搜索内容改变进行的操作
    TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            searchText = charSequence.toString();
            if (!searchText.trim().equalsIgnoreCase("")){
            	
               	 serachButton.setVisibility(View.VISIBLE);
               	 serachButton.setEnabled(true);
               
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
//        this.paramMap = paramMap;
    	this.paramMap.putAll(paramMap);

    }


    public  void search(){
        paramMap.put("searchText",searchText);
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

    //未完全退出后要清除原来的搜索字符

}
