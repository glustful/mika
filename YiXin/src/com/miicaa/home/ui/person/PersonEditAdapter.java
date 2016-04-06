package com.miicaa.home.ui.person;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.person.PersonDepartEdit.OnCheckChangeListener;

public class PersonEditAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<TreeElement> datas;
	private LayoutInflater inflater;
	private OnCheckChangeListener listener;

	public PersonEditAdapter(Context context, ArrayList<TreeElement> datas,
			OnCheckChangeListener l) {
		this.context = context;
		this.datas = datas;
		this.inflater = LayoutInflater.from(this.context);
		this.listener = l;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			view = this.inflater.inflate(R.layout.person_edit_listi_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.title.setCompoundDrawablesWithIntrinsicBounds(
				null,
				null,
				null, null);
		
		TreeElement treeElement = datas.get(position);
		if (treeElement.getParent() != null) {
			holder.space.setVisibility(View.VISIBLE);
			if (!treeElement.isHasChild())
				holder.title.setCompoundDrawablesWithIntrinsicBounds(null,
						null, null, null);
			else
				holder.title.setCompoundDrawablesWithIntrinsicBounds(
						null,
						null,
						this.context.getResources().getDrawable(
								R.drawable.an_text_right_next), null);
		} else {
			holder.space.setVisibility(View.GONE);
			if (treeElement.isHasChild()) {
				if (treeElement.isExpanded())
					holder.title.setCompoundDrawablesWithIntrinsicBounds(
							null,
							null,
							this.context.getResources().getDrawable(
									R.drawable.an_down_flag), null);
				else {
					holder.title.setCompoundDrawablesWithIntrinsicBounds(
							null,
							null,
							this.context.getResources().getDrawable(
									R.drawable.an_up_flag), null);
				}
			}
		}
		holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(buttonView.getTag().toString().equals(""+position)){
				datas.get(position).setChecked(isChecked);
				listener.onCheckChanged(datas.get(position), isChecked);
				}
			}
		});
		holder.title.setText(treeElement.getCaption());// 设置标题
		holder.check.setTag(""+position);
		holder.check.setChecked(treeElement.isChecked());
		return view;
	}

	class ViewHolder {
		public ViewHolder(View view) {
			this.title = (TextView) view
					.findViewById(R.id.person_depart_tvTitle);
			this.check = (CheckBox) view
					.findViewById(R.id.person_depart_cbCheckBox);

			this.space = view.findViewById(R.id.person_depart_space);
		}

		TextView title;
		CheckBox check;
		View space;
	}

	public void refresh(ArrayList<TreeElement> jsonObjects) {

		this.datas.clear();
		this.datas.addAll(jsonObjects);
		this.notifyDataSetChanged();

	}

}
