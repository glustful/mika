package com.miicaa.common.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miicaa.home.R;



/**
 * Created by Administrator on 13-12-6.
 */
public class PicturButton
{
    public Boolean mIsPicture;
    private Context mContext;
    View mRootView = null;
    public ImageView mPicture;
    Bitmap mBitmap;
    private int pictureButtonSize;
    OnPictureListener mOnPicturListener = null;
    public String mPath;

    public PicturButton(Context context)
    {
    	pictureButtonSize = (int) 
    			context.getResources().getDimension(R.dimen.picture_button_size);
        mContext = context;
        mIsPicture = false;
        mPath = "";
        mBitmap = null;
        initUI(-1);
    }
    
    
    public PicturButton(Context context,int icon,boolean takePicture)
    {
    	pictureButtonSize = (int) 
    			context.getResources().getDimension(R.dimen.picture_button_size);
        mContext = context;
        mIsPicture = false;
        mPath = "";
        mBitmap = null;
        mPicture = new ImageView(mContext);
        mPicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
       // mPicture.setImageDrawable(mContext.getResources().getDrawable(R.drawable.an_add_icon));
        mPicture.setBackgroundDrawable(mContext.getResources().getDrawable(icon));
        ViewGroup.LayoutParams mPhotoLayoutp = new ViewGroup.LayoutParams(pictureButtonSize,
        		pictureButtonSize);
        mPicture.setLayoutParams(mPhotoLayoutp);
        mPicture.setClickable(true);
        mPicture.setOnClickListener(onClickListener);
        mPicture.setVisibility(View.VISIBLE);
        mRootView = mPicture;
    }

    public PicturButton(Context context, int icon)
    {
    	pictureButtonSize = (int) 
    			context.getResources().getDimension(R.dimen.picture_button_size);
        mContext = context;
        mIsPicture = false;
        mPath = "";
        mBitmap = null;
        initUI(icon);
    }

    public PicturButton(Context context, Bitmap bitmap, String path)
    {
    	pictureButtonSize = (int) 
    			context.getResources().getDimension(R.dimen.picture_button_size);
        mContext = context;
        mIsPicture = true;
        mPath = path;
        mBitmap = bitmap;
        initUI(-1);
    }
    
    @SuppressWarnings("deprecation")
	public PicturButton(Context context,String path){
    	pictureButtonSize = (int) 
    			context.getResources().getDimension(R.dimen.picture_button_size);
    	mContext = context;
    	mIsPicture = true;
    	if(path != null)
    	{
    		mPath = path;
    	}
    
    	mPicture = new ImageView(mContext);
        ViewGroup.LayoutParams mPhotoLayoutp = new ViewGroup.LayoutParams(pictureButtonSize,
        		pictureButtonSize);
        mPicture.setLayoutParams(mPhotoLayoutp);
        mPicture.setClickable(true);
        mPicture.setOnClickListener(onClickListener);
        mPicture.setVisibility(View.VISIBLE);
        mRootView = mPicture;
    }

    public View getRootView()
    {
        return mRootView;
    }

    private void initUI(int icon)
    {
        mPicture = new ImageView(mContext);
        mPicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if(icon > 0)
        {
            mPicture.setImageDrawable(mContext.getResources().getDrawable(icon));
            mPicture.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.an_add_bg_normal));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(pictureButtonSize,
        		pictureButtonSize);
        mPicture.setLayoutParams(params);
    }
        else if(mBitmap == null)
        {
            mPicture.setImageDrawable(mContext.getResources().getDrawable(R.drawable.an_add_icon));
            mPicture.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.an_add_bg_normal));
            ViewGroup.LayoutParams mPhotoLayoutp = new ViewGroup.LayoutParams(pictureButtonSize,
            		pictureButtonSize);
            mPicture.setLayoutParams(mPhotoLayoutp);
        }else{
            mPicture.setImageBitmap(mBitmap);
            ViewGroup.LayoutParams mPhotoLayoutp = new ViewGroup.LayoutParams(pictureButtonSize,
            		pictureButtonSize);
            mPicture.setLayoutParams(mPhotoLayoutp);
        }
        mPicture.setClickable(true);
        mPicture.setOnClickListener(onClickListener);
        mPicture.setVisibility(View.VISIBLE);
        mRootView = mPicture;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mOnPicturListener == null)
            {
                return;
            }
            if(mIsPicture)
            {
            	if(mPath != null)
                mOnPicturListener.onPictureClick(mPath,mBitmap);
                
            }
            else
            {
                mOnPicturListener.onAddPictureClick();
            }
        }
    };

    public void setImageListerner(OnPictureListener onPictureListener)
    {
        mOnPicturListener = onPictureListener;
    }




    public interface OnPictureListener
    {
        public void onPictureClick(String msg,Bitmap bp);
        public void onAddPictureClick();
        
    }
}
