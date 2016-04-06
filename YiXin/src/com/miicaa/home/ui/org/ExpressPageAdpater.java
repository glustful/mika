package com.miicaa.home.ui.org;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

/**
 * Created by Administrator on 13-12-30.
 */
public class ExpressPageAdpater  extends PagerAdapter {

    ArrayList<GridView> grids;
    public ExpressPageAdpater(ArrayList<GridView> grids) {
        this.grids = grids;
    }
    
    public void refresh(ArrayList<GridView> grids){
    	this.grids = grids;
    	this.notifyDataSetChanged();
    }

    public void setGrids(ArrayList<GridView> grids) {
        this.grids = grids;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return grids.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(grids.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(grids.get(position));
        return grids.get(position);
    }
}
