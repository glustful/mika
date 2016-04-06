package com.miicaa.home.ui.org;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;

/**
 * Created by LM on 14-5-28.
 */
public class ProgressPeople {
    private Context mContext;
    private View mView;
    private GridView mGridView;
    private int pagerCount;
    private int totalCount;
    public ProgressPeople(Context context,int wCount){
        mContext = context;
        this.totalCount = wCount;
    }
    private  void initUI(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(R.layout.progress_people_layout_view,null);
        mGridView = (GridView)mView.findViewById(R.id.arragment_progress_small_gridview);
        mGridView.setAdapter(new myGridViewAdapter(totalCount,mContext));

    }

    public View getView(){
        return mView;
    }



    public class myGridViewAdapter extends BaseAdapter{

        private int count;
        private Context viewContext;
        public myGridViewAdapter(int count,Context context){
            this.count = count;
            this.viewContext = context;
        }

        @Override
        public int getCount() {
            return this.count;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           ViewHolder holder = new ViewHolder();
            if(view == null){
                view = LayoutInflater.from(viewContext).inflate(R.layout.progress_people_layout_view,null);
                holder.peopleLayout = (LinearLayout)view.findViewById(R.id.progress_people_layout);
                holder.peopleHeadView = (ImageView)view.findViewById(R.id.progress_people_head);
                holder.peopleName = (TextView)view.findViewById(R.id.prgress_people_name);
                view.setTag(holder);
            }else{
                holder =(ViewHolder) view.getTag();
            }
            return view;

        }
        class ViewHolder{
            LinearLayout peopleLayout;
            ImageView peopleHeadView;
            TextView peopleName;
        }
    }
}