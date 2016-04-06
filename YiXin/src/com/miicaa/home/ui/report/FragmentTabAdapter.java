package com.miicaa.home.ui.report;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class FragmentTabAdapter implements View.OnClickListener{
    private List<Fragment> fragments; // 一个tab页面对应一个Fragment
    private ArrayList<Button> rgs; // 用于切换tab
    private FragmentActivity fragmentActivity; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id

    private int currentTab = 0; // 当前Tab页面索引

    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

    public FragmentTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, ArrayList<Button> rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();
        rgs.get(0).setSelected(true);
        for(Button b:rgs){
        	
        	b.setOnClickListener(this);
        }


    }

   
    /**
     * 切换tab
     * @param idx
     */
    private void showTab(int idx){
        for(int i = 0; i < fragments.size(); i++){
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);

            if(idx == i){
                ft.show(fragment);
            }else{
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取一个带动画的FragmentTransaction
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        // 设置切换动画
        /*if(index > currentTab){
            ft.setCustomAnimations(R.anim.my_slide_in_left, R.anim.my_slide_out_left);
        }else{
            ft.setCustomAnimations(R.anim.my_slide_in_right, R.anim.my_slide_out_right);
        }*/
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment(){
        return fragments.get(currentTab);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     *  切换tab额外功能功能接口
     */
    static class OnRgsExtraCheckedChangedListener{
        public void OnRgsExtraCheckedChanged(int checkedId){

        }
    }

	@Override
	public void onClick(View v) {
		if(!(v instanceof Button))
			return;
		Button bn = (Button) v;
		 for(int i = 0; i < rgs.size(); i++){
			 
	            if(rgs.get(i).getId() == v.getId()){
	            	
	            	if(currentTab==i){
	            		((SuperFragment)getCurrentFragment()).doBackground();
	   				 return;
	            	}
	                Fragment fragment = fragments.get(i);
	                FragmentTransaction ft = obtainFragmentTransaction(i);

	                getCurrentFragment().onPause(); // 暂停当前tab


	                if(fragment.isAdded()){

	                    fragment.onResume(); // 启动目标tab的onResume()
	                }else{
	                    ft.add(fragmentContentId, fragment);
	                }
	                showTab(i); // 显示目标tab
	                ft.commit();
	                bn.setSelected(true);
	            	ImageView iv = (ImageView) bn.getTag();
	            	iv.setVisibility(View.VISIBLE);
	                // 如果设置了切换tab额外功能功能接口
	                if(null != onRgsExtraCheckedChangedListener){
	                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(v.getId());
	                }

	            }else{
	            	rgs.get(i).setSelected(false);
	            	
	            	ImageView iv = (ImageView) rgs.get(i).getTag();
	            	iv.setVisibility(View.INVISIBLE);
	            }
	        }
		
	}

}
