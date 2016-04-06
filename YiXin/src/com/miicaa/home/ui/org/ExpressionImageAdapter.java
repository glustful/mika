package com.miicaa.home.ui.org;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-30.
 */
public class ExpressionImageAdapter extends BaseAdapter {

    List<Expression> expressionList;
    Context context;
    LayoutInflater lfInflater;

    public ExpressionImageAdapter(Context context,List<Expression> expressionList) {
        this.context = context;
        this.expressionList = expressionList;
        lfInflater = LayoutInflater.from(this.context);
    }

    @Override
	public int getCount() {
        // TODO Auto-generated method stub
        return expressionList.size();
    }

    @Override
	public Object getItem(int position) {
        // TODO Auto-generated method stub
        return expressionList.get(position);
    }

    @Override
	public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        Expression exp = expressionList.get(position);
        if (convertView == null) {
            convertView = lfInflater.inflate(R.layout.expression_list_item, null);
            holder = new ViewHolder();
            holder.iv_id = (ImageView)convertView.findViewById(R.id.iv_id);
            holder.tv_id = (TextView)convertView.findViewById(R.id.tv_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.e("FFF",String.valueOf(exp.getDrableId()));
        Log.e("FFF Name",String.valueOf(exp.getCode()));
        holder.iv_id.setBackgroundResource(exp.getDrableId());

        holder.tv_id.setText(exp.getCode());
        return convertView;
    }

    class ViewHolder{
        ImageView iv_id;
        TextView tv_id;
    }
}
