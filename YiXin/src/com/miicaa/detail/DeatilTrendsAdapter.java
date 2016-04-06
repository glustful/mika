package com.miicaa.detail;

import java.util.ArrayList;

import org.xml.sax.XMLReader;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.utils.ViewHolder;

public class DeatilTrendsAdapter extends BaseAdapter{

	Context context;
	ImageGetter imgGetter;
	ArrayList<DetailTrendsInfo> infos;
	 public DeatilTrendsAdapter(Context context) {
		// TODO Auto-generated constructor stub
		 this.context = context;
		 imgGetter = DetailDiscussFragment.setExpressIcon(context,null);
		 
	}
	 
	 public void refresh(ArrayList<DetailTrendsInfo> infos){
		 this.infos = infos;
		 this.notifyDataSetChanged();
	 }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos != null ? infos.size():0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ArrayList<String> codes = new ArrayList<String>();
		DetailTrendsInfo info = infos.get(position);
		codes.addAll(DetailDiscussAdapter.setCodes(info.allInfo));
		
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
			                    editable.setSpan(new AtClickSpan(code, context),start,
			                            editable.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			                    editable.setSpan(new ForegroundColorSpan(Color.BLUE), 
//			                    		start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			                    i++;
			                }
			            }

			    }
		};
		if(convertview == null){
			convertview = LayoutInflater.from(context).inflate(R.layout.matter_narmal_detail, null);
		}
		ImageView headimg = ViewHolder.get(convertview, R.id.head_img);
		TextView  headname = ViewHolder.get(convertview, R.id.head_name);
		ImageView talk = ViewHolder.get(convertview, R.id.normalTalk);
		talk.setVisibility(View.GONE);
		TextView content = ViewHolder.get(convertview, android.R.id.content);
		TextView time = ViewHolder.get(convertview, R.id.time);
		TextView from = ViewHolder.get(convertview, R.id.from);
		Tools.setHeadImg(info.operatorId, headimg);
		headname.setText(info.operatorName);
		
		content.setText(Html.fromHtml(info.allInfo, imgGetter, tagHandler));
		time.setText(info.createTime);
		from.setText(info.clientInfo.equals("安排") ? "任务" : info.clientInfo);
		return convertview;
		
		
	}

}
