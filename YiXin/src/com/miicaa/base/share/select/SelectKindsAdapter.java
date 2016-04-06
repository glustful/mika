package com.miicaa.base.share.select;

import java.util.ArrayList;

import org.androidannotations.annotations.EBean;

import com.miicaa.common.base.SearchFunction;
import com.miicaa.home.R;
import com.miicaa.home.ui.person.TreeElement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

@EBean
public class SelectKindsAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Kind> kinds;
	private OnCheckChangeListener listener;
	public SelectKindsAdapter(Context context){
		this.mContext = context;
		this.kinds = new ArrayList<Kind>();
		
	}
	
	public void refresh(ArrayList<Kind> tmp){
		this.kinds.clear();
		this.kinds.addAll(tmp);
		this.notifyDataSetInvalidated();
	}
	
	@Override
	public int getCount() {
		
		return this.kinds.size();
	}

	@Override
	public Object getItem(int position) {
		
		return this.kinds.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.person_edit_listi_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				kinds.get(position).setCheck(isChecked);
				if(listener!=null){
					listener.onCheckChanged(kinds.get(position), isChecked);
				}
			}
		});
		holder.title.setText(this.kinds.get(position).getName());
		holder.check.setChecked(this.kinds.get(position).isCheck());
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ViewHolder holder = (ViewHolder) v.getTag();
				holder.check.setChecked(!holder.check.isChecked());
				
			}
		});
		return convertView;
	}
	class ViewHolder {
		public ViewHolder(View view) {
			this.title = (TextView) view
					.findViewById(R.id.person_depart_tvTitle);
			this.check = (CheckBox) view
					.findViewById(R.id.person_depart_cbCheckBox);
			this.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			
		}

		TextView title;
		CheckBox check;
		
	}
	
	public void setListener(OnCheckChangeListener l){
		this.listener = l;
	}
	
	public interface OnCheckChangeListener{
		public void onCheckChanged(Kind name, boolean isCheck);
	}

}
