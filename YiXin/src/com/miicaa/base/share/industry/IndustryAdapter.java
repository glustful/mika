package com.miicaa.base.share.industry;

import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONObject;

import com.miicaa.home.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;

@EBean
public class IndustryAdapter extends SectionedBaseAdapter{

	JSONArray datas = new JSONArray();
	JSONObject industry = new JSONObject();
	JSONObject selected;
	public IndustryAdapter(){
		
	}
    @Override
    public Object getItem(int section, int position) {
        
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        
        return 0;
    }

    @Override
    public int getSectionCount() {
        return datas.length();
    }

    @Override
    public int getCountForSection(int section) {
        return industry.optJSONArray(datas.optJSONObject(section).optString("id")).length();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
       
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.radiobutton, null);
        } else {
            layout = (LinearLayout) convertView;
        }
       RadioButton rb = (RadioButton) layout.findViewById(R.id.cbCheckBox);
       JSONObject obj = industry.optJSONArray(datas.optJSONObject(section).optString("id")).optJSONObject(position);
        rb.setText(obj.optString("name"));
        if(obj==selected){
        	rb.setChecked(true);
        }else{
        	rb.setChecked(false);
        }
        layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selected = (JSONObject) v.getTag();
				
				IndustryAdapter.this.notifyDataSetChanged();
			}
		});
        layout.setTag(industry.optJSONArray(datas.optJSONObject(section).optString("id")).optJSONObject(position));
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItem)).setText(datas.optJSONObject(section).optString("name"));
        return layout;
    }
	public void refresh(org.json.JSONArray classes, JSONObject industry) {
		this.datas = classes;
		this.industry = industry;
		
		this.notifyDataSetInvalidated();
		
	}
	public JSONObject getSelected() {
		// TODO Auto-generated method stub
		return selected;
	}
}
