package com.miicaa.detail;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.detail.DetailDiscussAdapter.OnDelDiscussListener;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.org.MatterHttp;
import com.miicaa.home.ui.org.MatterHttp.OnMatterResult;
import com.miicaa.utils.AddMoreListView;
import com.miicaa.utils.AddMoreListView.OnRefreshLoadingMoreListener;
import com.miicaa.utils.AllUtils;
import com.yxst.epic.yixin.view.ResizeLayout;

@EFragment(R.layout.matter_do_normal)
public class DetailDiscussFragment extends Fragment implements OnDiscussButtonClickListener,OnDelDiscussListener{
	Integer pagesize = 20;
	Integer pagenum;
	OnTabCountListener disListener;
	Html.ImageGetter imageGetter;
	public static DetailDiscussFragment instance;
	DetailDiscussAdapter dadapter;
//	DeatilTrendsAdapter tadapter;
	 ArrayList<String> codes = new ArrayList<String>();
	ArrayList<DetailContentInfo> infos = new ArrayList<DetailContentInfo>();
//	ArrayList<DetailTrendsInfo> tinfos = new ArrayList<DetailTrendsInfo>();
//	ListView list ;
	
	
@ViewById(R.id.listview)
AddMoreListView listView;
@ViewById(R.id.sendProgressLayout)
RelativeLayout sendProgressLayout;
@FragmentArg
String dataId;
@FragmentArg
String type;
@FragmentArg
String dataType;
@FragmentArg
String contextClss;
@SuppressWarnings("deprecation")
@AfterInject
void AfterInject(){

}
@AfterViews
void afterview(){
	
	instance = this;
	if(infos.size() == 0){
	doBackound();
	}
 imageGetter = setExpressIcon(getActivity(),null);
 listView.setFooterDividersEnabled(true);
 if(AllUtils.discuss.equalsIgnoreCase(type)){
//	 footview.setVisibility(View.VISIBLE);
	 dadapter = new DetailDiscussAdapter(getActivity(),type,imageGetter);
	 dadapter.setOnDiscussButtonClickListener(this);
	 dadapter.setOnDelDiscussListener(this);
	 listView.setAdapter(dadapter);
	 refreshD(dadapter.getCount());
 }else{
//	 footview.setVisibility(View.GONE);;
//	 tadapter = new DeatilTrendsAdapter(getActivity());
//	 listView.setAdapter(tadapter);
//	 refreshT();
 }
// doBackound();
 listView.setOnRefreshListener(morelistener);
 
// footview.setOnDiscussClickListener(this);
//	 footView.addView(new DiscussFootView_(footView.getContext()));
 

}

public static DetailDiscussFragment getInstance(){

	return instance;
}

void nodoDiscuss(Boolean yes){
	if(yes){
//		footview.setVisibility(View.GONE);
	}
}

@SuppressWarnings("serial")
@Background
void doBackound(){
	
	String url = null;
	if(AllUtils.discuss.equalsIgnoreCase(type))
		url = "/home/phone/thing/getdiscussion";
	else if(AllUtils.trends.equalsIgnoreCase(type))
		url = "/home/phone/thing/getactivitylst";
	else
		return;
	
	pagenum = pagenum == null||pagenum == 1 ? 1:pagenum+1;
	new RequestAdpater() {
		
		@Override
		public void onReponse(ResponseData data) {
			
			if(data.getResultState() == ResultState.eSuccess){
				jsonToData(data.getJsonArray());
			}else{
				AllUtils.netIsNotGeiLi(getActivity());
			}
			
		}
		
		@Override
		public void onProgress(ProgressMessage msg) {
			// TODO Auto-generated method stub
			
		}
	}.setUrl(url)
	.addParam("dataId",dataId)
	.addParam("pageNo",pagenum.toString())
	.addParam("pageSize",pagesize.toString())
	.notifyRequest();
}



@UiThread
void jsonToData(JSONArray discussarray){
//	listView.onRefreshComplete();
//	listView.onLoadMoreComplete(false);
	if(discussarray == null || discussarray.length() == 0){
		listView.onLoadMoreComplete(true);
		return;	
	}
	if(listView.getTag(R.id.tag_json)!=null && listView.getTag(R.id.tag_json).toString().equals(discussarray.toString())){
		return;
	}
	listView.setTag(R.id.tag_json, discussarray.toString());
	if(AllUtils.discuss.equalsIgnoreCase(type)){
	for(int i = 0;i < discussarray.length(); i++){
		JSONObject discuss = discussarray.optJSONObject(i);
		DetailContentInfo info = new DetailContentInfo(discuss).save();
		infos.add(info);
		}
	
	refreshD(discussarray.length());
	}
	sendProgressLayout.setVisibility(View.GONE);

}

private void refresh(){
	infos.clear();
	pagenum = 1;
	doBackound();
}


void refreshD(int count){
	if(dadapter != null){
		if(count < 20){
			listView.onLoadMoreComplete(true);
			}else{
				listView.onLoadMoreComplete(false);
			}
		dadapter.refresh(infos);
	}
}

//void refreshT(){
//	if(tadapter != null){
//		tadapter.refresh(tinfos);
//		AddMoreListView.setListViewHeightBasedOnChildren(listView);
//		}
//}

//分页，上拉加载更多
AddMoreListView.OnRefreshLoadingMoreListener morelistener = new OnRefreshLoadingMoreListener() {
	
	@Override
	public void onRefresh() {
		
	}
	
	@Override
	public void onLoadMore() {
		doBackound();
	}
};


@Override
public void onDiscussClick(String dataId) {
	
}

@Override
public void nameClick(int position,String name) {
	MatterDetailAcrtivity.getInstance().discussFootView.setName(position,name);
}


public void sendDiscuss(String content){
	if(content == null || "".equals(content.trim()))
		return;
	sendProgressLayout.setVisibility(View.VISIBLE);
	MatterHttp.requestDiscuss(new OnMatterResult() {
		
		@Override
		public void onSuccess(String msg, Object obj) {
			if(onDelDiscussListener != null)
				onDelDiscussListener.adddiscuss();
			resetDiscuss();
		}
		
		@Override
		public void onFailure(String msg) {
			
		}
	}, dataId, dataType, Tools.getDiscussText(content), Tools.getDiscussHTML(content));
}

void sendDiscussDiscuss(int position,String content){
	MatterHttp.addDiscussDiscuss(new OnMatterResult() {
		
		@Override
		public void onSuccess(String msg, Object obj) {
			// TODO Auto-generated method stub
			resetDiscuss();
		}
		
		@Override
		public void onFailure(String msg) {
			// TODO Auto-generated method stub
			
		}
	}, infos.get(position).id,  Tools.getDiscussText(content), Tools.getDiscussHTML(content));
}

void resetDiscuss(){
	pagenum = null;
	infos.removeAll(infos);
	doBackound();
}

@UiThread
public void setDisscussCount(OnTabCountListener disListener){
	this.disListener = disListener;
}



public static Html.ImageGetter setExpressIcon(final Context context,final TextView tv){
Html.ImageGetter imageGetter = new Html.ImageGetter() {
    @Override
    public Drawable getDrawable(String source) {
        int id = R.drawable.e_1;
        if (source.endsWith("/i_f02.gif")) {
            id = R.drawable.e_2;
        } else if (source.endsWith("/i_f03.gif")) {
            id = R.drawable.e_3;
        } else if (source.endsWith("/i_f04.gif")) {
            id = R.drawable.e_4;
        } else if (source.endsWith("/i_f05.gif")) {
            id = R.drawable.e_5;
        } else if (source.endsWith("/i_f06.gif")) {
            id = R.drawable.e_6;
        } else if (source.endsWith("/i_f07.gif")) {
            id = R.drawable.e_7;
        } else if (source.endsWith("/i_f08.gif")) {
            id = R.drawable.e_8;
        } else if (source.endsWith("/i_f09.gif")) {
            id = R.drawable.e_9;
        } else if (source.endsWith("/i_f10.gif")) {
            id = R.drawable.e_10;
        } else if (source.endsWith("/i_f11.gif")) {
            id = R.drawable.e_11;
        } else if (source.endsWith("/i_f12.gif")) {
            id = R.drawable.e_12;
        } else if (source.endsWith("/i_f13.gif")) {
            id = R.drawable.e_13;
        } else if (source.endsWith("/i_f14.gif")) {
            id = R.drawable.e_14;
        } else if (source.endsWith("/i_f15.gif")) {
            id = R.drawable.e_15;
        } else if (source.endsWith("/i_f16.gif")) {
            id = R.drawable.e_16;
        } else if (source.endsWith("/i_f17.gif")) {
            id = R.drawable.e_17;
        } else if (source.endsWith("/i_f18.gif")) {
            id = R.drawable.e_18;
        } else if (source.endsWith("/i_f19.gif")) {
            id = R.drawable.e_19;
        } else if (source.endsWith("/i_f20.gif")) {
            id = R.drawable.e_20;
        } else if (source.endsWith("/i_f21.gif")) {
            id = R.drawable.e_21;
        } else if (source.endsWith("/i_f22.gif")) {
            id = R.drawable.e_22;
        } else if (source.endsWith("/i_f23.gif")) {
            id = R.drawable.e_23;
        } else if (source.endsWith("/i_f24.gif")) {
            id = R.drawable.e_24;
        } else if (source.endsWith("/i_f25.gif")) {
            id = R.drawable.e_25;
        } else if (source.endsWith("/i_f26.gif")) {
            id = R.drawable.e_26;
        } else if (source.endsWith("/i_f27.gif")) {
            id = R.drawable.e_27;
        } else if (source.endsWith("/i_f28.gif")) {
            id = R.drawable.e_28;
        } else if (source.endsWith("/i_f29.gif")) {
            id = R.drawable.e_29;
        } else if (source.endsWith("/i_f30.gif")) {
            id = R.drawable.e_30;
        } else if (source.endsWith("/i_f31.gif")) {
            id = R.drawable.e_31;
        } else if (source.endsWith("/i_f32.gif")) {
            id = R.drawable.e_32;
        } else if (source.endsWith("/i_f33.gif")) {
            id = R.drawable.e_33;
        } else if (source.endsWith("/i_f34.gif")) {
            id = R.drawable.e_34;
        } else if (source.endsWith("/i_f35.gif")) {
            id = R.drawable.e_35;
        } else if (source.endsWith("/i_f36.gif")) {
            id = R.drawable.e_36;
        } else if (source.endsWith("/i_f037.gif")) {
            id = R.drawable.e_37;
        } else if (source.endsWith("/i_f38.gif")) {
            id = R.drawable.e_38;
        } else if (source.endsWith("/i_f39.gif")) {
            id = R.drawable.e_39;
        } else if (source.endsWith("/i_f40.gif")) {
            id = R.drawable.e_40;
        } else if (source.endsWith("/i_f41.gif")) {
            id = R.drawable.e_41;
        } else if (source.endsWith("/i_f42.gif")) {
            id = R.drawable.e_42;
        } else if (source.endsWith("/i_f43.gif")) {
            id = R.drawable.e_43;
        } else if (source.endsWith("/i_f44.gif")) {
            id = R.drawable.e_44;
        } else if (source.endsWith("/i_f45.gif")) {
            id = R.drawable.e_45;
        } else if (source.endsWith("/i_f46.gif")) {
            id = R.drawable.e_46;
        } else if (source.endsWith("/i_f47.gif")) {
            id = R.drawable.e_47;
        } else if (source.endsWith("/i_f48.gif")) {
            id = R.drawable.e_48;
        } else if (source.endsWith("/i_f49.gif")) {
            id = R.drawable.e_49;
        } else if (source.endsWith("/i_f50.gif")) {
            id = R.drawable.e_50;
        } else if (source.endsWith("/i_f51.gif")) {
            id = R.drawable.e_51;
        }
        Drawable d = context.getResources().getDrawable(id);
        if(tv!=null){
        	TextPaint mPaint = tv.getPaint();
        	FontMetrics fm = mPaint.getFontMetrics();         
	        int m_iFontHeight = (int) Math.ceil(fm.descent - fm.ascent);
	        d.setBounds(0, 0, m_iFontHeight, m_iFontHeight);
        }else{
        
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        }
        return d;
    }
    
};
return imageGetter;
}
@Override
public void deldiscuss() {
	refresh();
	Log.d("DetailDiscussFragment", "delete discuss is complete!");
	if(onDelDiscussListener != null)
		onDelDiscussListener.deldiscuss();
}


OnDelDiscussListener onDelDiscussListener;
public void setOnDelDiscussListener(OnDelDiscussListener listener){
	this.onDelDiscussListener = listener;
}
@Override
public void adddiscuss() {
	// TODO Auto-generated method stub
	
}


}

