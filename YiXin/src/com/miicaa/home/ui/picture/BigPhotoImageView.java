package com.miicaa.home.ui.picture;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Toast;

import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.org.StyleDialog;

/**
 * Created by LM on 14-7-8.
 */
public class BigPhotoImageView extends ImageView {
    StyleDialog dialog;
    private  final static int LOADED = 1 ;
    private  final static int LOADF = 2;
    Context mContext;
    String mFid;
    public BigPhotoImageView(Context context,String fid) {
        super(context);
        mContext = context;
        mFid = fid;
        dialog = new StyleDialog(context);
        loadingBigPicture();
    }

    public BigPhotoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BigPhotoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case LOADED:
                    Bitmap mBitmap = (Bitmap)msg.obj;
                    setImageBitmap(mBitmap);
                    new PhotoViewAttacher(BigPhotoImageView.this);
//                mBitmap.recycle();
//                System.gc();
                    mBitmap = null;
                    break;
                case LOADF:
                    Toast.makeText(mContext, "数据加载失败！", 1).show();
                    break;
                default:
                    break;

            }
            dialog.dismiss();
        }

    };

    private void loadingBigPicture(){
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
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 2;
                                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                                msg = handler.obtainMessage(0, bitmap);
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
}
