package com.miicaa.home.ui.photoGrid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miicaa.home.R;

/**
 * Created by LM on 14-8-14.
 */
public class BigPhotoCheckActivity extends Activity {
    ViewPager mViewPager;
    ImageView delImgView;
    ArrayList<View> pViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_big_check_activity);
        mViewPager = (ViewPager)findViewById(R.id.photo_big_check_viewpager);
        delImgView = (ImageView)findViewById(R.id.photo_big_check_delimg);
        pViews = new ArrayList<View>();
        for (int i = 0; i < Bimp.bmp.size();i++){
            initImgView(Bimp.bmp.get(i));
        }


    }

    protected void initImgView(Bitmap bmp){
        ImageView imageView = new ImageView(BigPhotoCheckActivity.this);
        imageView.setImageBitmap(bmp);
        pViews.add(imageView);
    }

    class BigPhotoAdapter extends PagerAdapter{
        int count;
        List<View> imgList;
        public BigPhotoAdapter(List<View> imgList){
            this.imgList = imgList;
            count = imgList.size()>0?imgList.size():0;
        }
        public void updateImgList(List<View> imgList){
            this.imgList = imgList;
        }
        @Override
        public int getCount() {

            return count;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {// ���view瀵硅薄
            ((ViewPager) arg0).removeView(imgList.get(arg1 % count));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            try{
                ((ViewPager) container).addView(imgList.get(position%count),0);

            }catch (Exception e){
                e.printStackTrace();
            }
            delImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog(position);
                }
            });
            return imgList.get(position%count);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private void dialog(final int position){
       AlertDialog.Builder builder = new AlertDialog.Builder(BigPhotoCheckActivity.this)
                .setMessage("确认删除吗？")
                .setTitle("提示")
                .setPositiveButton("确认",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bimp.bmpMap.remove(Bimp.drr.get(position));
                        Bimp.drr.remove(position);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }
}
