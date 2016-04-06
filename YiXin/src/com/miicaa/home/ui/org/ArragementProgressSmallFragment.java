package com.miicaa.home.ui.org;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;


/**
 * Created by LM on 14-5-27.
 */
@SuppressLint("ValidFragment")
public class ArragementProgressSmallFragment extends Fragment {
    private int totalCount;
    private View mView;
    private GridView mGridView;
    private Context mContext;

    @SuppressLint("ValidFragment")
	public  ArragementProgressSmallFragment(Context context,int wCount){
        mContext = context;
        totalCount = wCount;

    }

    @Override
    public void onCreate(Bundle savedInstanstate){
        super.onCreate(savedInstanstate);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.arragement_progress_small_fragment,null);
        mGridView = (GridView)mView.findViewById(R.id.arragment_progress_small_gridview);
        mGridView.setAdapter(new myGridViewAdapter(totalCount,mContext));

    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstancestate){
        ViewGroup parent = (ViewGroup)mView.getParent();
        if(parent !=null){
            parent.removeAllViewsInLayout();
        }
        return mView;
//        return inflater.inflate(R.layout.arragenment_people_fragment,container,false);
    }
    public class myGridViewAdapter extends BaseAdapter {

        private int count;
        private Context viewContext;
        private Boolean isSeleted = false;
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
                holder.peopleLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        if (isSeleted == false) {
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    view.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.item_press_color));
                                    isSeleted = true;
                                    break;
                            }
                        }else{
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    view.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.item_white_color));
                                    isSeleted = false;
                                    break;
                            }
                        }
                        return true;
                    }
                });
                view.setTag(holder);
            }else{
                holder =(ViewHolder) view.getTag();
            }
            return view;

        }
//        View.OnTouchListener peopleListener = new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return false;
//            }
//        }
        class ViewHolder{
            LinearLayout peopleLayout;
            ImageView peopleHeadView;
            TextView peopleName;
        }
    }
}
