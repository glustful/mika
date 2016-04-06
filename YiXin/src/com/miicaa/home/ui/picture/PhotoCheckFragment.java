package com.miicaa.home.ui.picture;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.org.StyleDialog;

/**
 * Created by LM on 14-7-8.
 */
@SuppressLint("ValidFragment")
public class PhotoCheckFragment extends Fragment {
    String  mFid;
    View mRootView;
    ImageView pagerView;
    Context mContext;
    StyleDialog dialog;
    OnLoadedListener onLoadedListener;
    HashMap<String,SoftReference<Drawable>> imageCache;
    Bitmap oldBitMap = null;
    private  final static int LOADED = 1 ;
    private  final static int LOADF = 2;
    @SuppressLint("ValidFragment")
	public PhotoCheckFragment(Context context,String fid){
        mContext = context;
        mFid = fid;
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = getActivity().getLayoutInflater().inflate(R.layout.photo_check_fragment_layout,null);
        pagerView = (ImageView)mRootView.findViewById(R.id.photo_check_image);
        dialog = new StyleDialog(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup parent = (ViewGroup)mRootView.getParent();
        if(parent !=null){
            parent.removeAllViewsInLayout();
        }
        return mRootView;
    }

    public void setBitMap(){
        if (oldBitMap == null){
            loadingBigPicture();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            String eMsg = "";
            switch(msg.what){
                case LOADED:
                    SoftReference<Drawable> reference = imageCache.get("cache");//若没有内存则回收图片
                    Drawable drawable = reference.get();
                    pagerView.setImageDrawable(drawable);
                    new PhotoViewAttacher(pagerView);
                    break;
                case LOADF:
                    eMsg = "数据加载失败！";
                    break;
                default:
                    break;

            }
            onLoadedListener.onLoad(eMsg);
        }

    };

    public void loadingBigPicture(){
        if (imageCache.get("cache") != null){
            SoftReference<Drawable> reference = imageCache.get("cache");//若没有内存则回收图片
            Drawable drawable = reference.get();
            pagerView.setImageDrawable(drawable);
            return;
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        new Thread() {
            @Override
            public void run() {
                new RequestAdpater() {
                    @Override
                    public void onReponse(ResponseData data) {
                        Message msg = new Message();
                        if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                            try {
                                InputStream is = data.getmIs();
//                                BitmapFactory.Options options = new BitmapFactory.Options();
//                                options.inSampleSize = 2;
//                                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                                Drawable drawable = Drawable.createFromStream(is,"src");
                                imageCache.put("cache",new SoftReference<Drawable>(drawable));
//                                msg = handler.obtainMessage(0, drawable);
                                msg.what = LOADED;
                                handler.sendMessage(msg);
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            msg.what = LOADF;
                            handler.sendMessage(msg);
                        }
                    }
                    @Override
                    public void onProgress(ProgressMessage msg) {

                    }
                }.setUrl("/home/proupload/pc/component/upload/download")
                        .setRequestMethod(RequestAdpater.RequestMethod.eGet)
                        .setRequestType(RequestAdpater.RequestType.eFileStream)
                        .addParam("id", mFid)
                        .notifyRequest();
            }
        }.start();
    }

    public interface OnLoadedListener{
        public void onLoad(String msg);
    }

    public void setOnLoadListener(OnLoadedListener onLoadListener){
        this.onLoadedListener = onLoadListener;
    }
}
