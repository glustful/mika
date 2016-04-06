package com.miicaa.home.ui.photoGrid;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.miicaa.home.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@EActivity(R.layout.scaner_photo)
public class ScanerPhotoActivity extends Activity {

	@ViewById(R.id.big_picture_back_button)
	Button backButton;
	@ViewById(R.id.title)
	TextView titleText;
	@ViewById(R.id.save_big_picture)
	Button saveButton;
	@ViewById(R.id.photo_check_pager)
	ViewPager viewPager;

	@Extra
	ArrayList<Bitmap> photos;
	@Extra
	Bitmap photo;
	@Extra
	int pageNum;
	SeletPictureAdapter adapter;

	@AfterInject
	void afterInject() {
		
		adapter = new SeletPictureAdapter();
		
	}

	@AfterViews
	void aferView() {
		
			titleText.setText((pageNum+1) + "/" + photos.size());
		
		saveButton.setVisibility(View.INVISIBLE);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(final int position) {
				
					titleText.setText(position + 1 + "/" + photos.size());
			
				pageNum = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		viewPager.setAdapter(adapter);
		
		 viewPager.setCurrentItem(pageNum);
	}

	@Click(R.id.big_picture_back_button)
	void backClick() {
		finish();
	}

	
	
	class SeletPictureAdapter extends PagerAdapter {
		LayoutInflater inflater;
		Context context;

		public SeletPictureAdapter() {
			context = ScanerPhotoActivity.this;
			inflater = LayoutInflater.from(context);
		}

		public void refresh() {
			if (getCount() > 0)
				this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return photos==null?0:photos.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(final ViewGroup container,
				final int position) {
			View imageLayout = inflater.inflate(R.layout.picture_bigimage_view,
					container, false);
			
			final ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.pictureBigImageView);
			
			imageView.setImageBitmap(photos.get(position));

			container.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

	}
}
