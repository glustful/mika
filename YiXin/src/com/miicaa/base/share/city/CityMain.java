package com.miicaa.base.share.city;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.miicaa.base.share.ShareMain;
import com.miicaa.base.share.city.ScrollerPickerPopup.OnButtonListener;
import com.miicaa.home.R;
import com.miicaa.home.view.ScrollerStringPicker;

public class CityMain extends ShareMain {

	private int layers = 2;//几级联动
	private LinearLayout mRootLayout;
	public int getLayers() {
		return layers;
	}

	public void setLayers(int layers) {
		this.layers = layers;
	}

	
	ArrayList<Cityinfo> mCityinfos = new ArrayList<Cityinfo>();
	public CityMain(Context mContext) {
		super(mContext);
		initLayout();
		
	}

	private void initLayout() {
		this.mRootLayout = new LinearLayout(mContext);
		this.mRootLayout.setOrientation(LinearLayout.HORIZONTAL);
		this.mRootLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void start(){
		
		if(this.mRootLayout.getParent() != null){
			((ViewGroup)mRootLayout.getParent()).removeAllViews();
		}
		this.mRootLayout.removeAllViews();
		ArrayList<Cityinfo> tmp = mCityinfos;
		for(int i=0;i<layers;i++){
			addScrollPicker(tmp);
			tmp = tmp.get(0).getChildren();
		}
		
		ScrollerPickerPopup.builder(mContext, mRootLayout)
		.setOnButtonListener(new OnButtonListener() {
			
			@Override
			public void onClick(int code) {
				if(code==Dialog.BUTTON_POSITIVE){
					int count = mRootLayout.getChildCount();
					String text = "";
					for(int i=0;i<count;i++){
						ScrollerStringPicker picker = (ScrollerStringPicker) mRootLayout.getChildAt(i);
						text += picker.getSelected().getCity_name()+ " ";
					}
					((TextView)mRootView).setText(text);
				}
				
			}
		})
		.show();
	}

	public void setmCityinfos(ArrayList<Cityinfo> mCityinfos) {
		this.mCityinfos = mCityinfos;
	}

	private void addScrollPicker(ArrayList<Cityinfo> infos) {
		ScrollerStringPicker picker = (ScrollerStringPicker) LayoutInflater.from(mContext).inflate(R.layout.scrollerpicker, null);
		picker.setOnSelectListener(selectListener);
		picker.setData(infos);
		picker.setDefault(1);
		if(this.mRootLayout.getChildCount()>0){
			this.mRootLayout.getChildAt(this.mRootLayout.getChildCount()-1).setTag(picker);
		}
		this.mRootLayout.addView(picker,new LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		
	}
	
	com.miicaa.home.view.ScrollerStringPicker.OnSelectListener selectListener = new com.miicaa.home.view.ScrollerStringPicker.OnSelectListener() {

		@Override
		public void endSelect(ScrollerStringPicker picker,Cityinfo city) {
			// TODO Auto-generated method stub
			if (city.getCity_name()==null || city.getCity_name().equals(""))
				return;
			initNextCity(picker, city);
			
			/*if (temCityIndex != id) {
				String selectDay = provincePicker.getSelectedText();
				if (selectDay == null || selectDay.equals(""))
					return;
				String selectMonth = counyPicker.getSelectedText();
				if (selectMonth == null || selectMonth.equals(""))
					return;
				counyPicker.setData(citycodeUtil.getCouny(couny_map,
						citycodeUtil.getCity_list_code().get(id)));
				counyPicker.setDefault(1);
				int lastDay = Integer.valueOf(cityPicker.getListSize());
				if (id > lastDay) {
					cityPicker.setDefault(lastDay - 1);
				}
			}
			temCityIndex = id;
			Message message = new Message();
			message.what = REFRESH_VIEW;
			handler.sendMessage(message);*/
		}

		@Override
		public void selecting(ScrollerStringPicker picker,Cityinfo city) {
			// TODO Auto-generated method stub

		}
	};
	
	private void initNextCity(ScrollerStringPicker picker,Cityinfo city){
		if(picker.getTag()==null || !(picker.getTag() instanceof ScrollerStringPicker))
			return;
		ScrollerStringPicker next = (ScrollerStringPicker) picker.getTag();
		next.setData(city.getChildren());
		next.setDefault(1);
		initNextCity(next, city.getChildren().get(0));
	}
	
	public static ArrayList<Cityinfo> initBigCity(){
		ArrayList<Cityinfo> citys = new ArrayList<Cityinfo>();
		Cityinfo info = new Cityinfo();
		info.setId("0");
		info.setCity_name("中国大陆");
		citys.add(info);
		info = new Cityinfo();
		info.setId("1");
		info.setCity_name("台湾");
		citys.add(info);
		info = new Cityinfo();
		info.setId("2");
		info.setCity_name("港澳");
		citys.add(info);
		info = new Cityinfo();
		info.setId("3");
		info.setCity_name("其他国家");
		citys.add(info);
		return citys;
	}
}
