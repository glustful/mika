package com.miicaa.home.ui.person;

import java.io.File;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.ui.org.StyleDialog;

/**
 * Created by Administrator on 13-11-27.
 */
public class PersonHeadEdit extends Activity
{
	
	private static String TAG = "PersonHeadEdit";
	
    String mUserCode;
    String mUserId;

    StyleDialog dialog;

    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    ImageView mImg;
    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_head_edit_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mUserCode = bundle.getString("userCode");
        mUserId = bundle.getString("userId");
        initUI();
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left,R.anim.my_slide_out_right);
    }

    private void initUI()
    {
        Button button = (Button)findViewById(R.id.person_head_id_back);
        button.setOnClickListener(onBackClick);
        LinearLayout layout = (LinearLayout)findViewById(R.id.person_head_id_camera_layout);
        layout.setOnClickListener(onCameraClick);
        layout = (LinearLayout)findViewById(R.id.send_to_id_local_layout);
        layout.setOnClickListener(onLocalClick);

        dialog = new StyleDialog(PersonHeadEdit.this);
        dialog.setCanceledOnTouchOutside(false);
        mImg = (ImageView)findViewById(R.id.imageView);
    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            PersonHeadEdit.this.finish();
        }
    };

    View.OnClickListener onCameraClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            Intent  intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(Tools.hasSdCard())
            {
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(UserAccount.getLocalDir("image/"),IMAGE_FILE_NAME)));
                startActivityForResult(intentFromCapture,
                        CAMERA_REQUEST_CODE);
            }
        }
    };



    View.OnClickListener onLocalClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent intentFromGallery = new Intent();
            intentFromGallery.setType("image/*");
            intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case IMAGE_REQUEST_CODE:
                if(resultCode == RESULT_OK)
                {
                    startPhotoZoom(data.getData());
                }
                break;
            case CAMERA_REQUEST_CODE:
                if(resultCode == RESULT_OK)
                {
                    if (Tools.hasSdCard())
                    {
                        File tempFile = new File(UserAccount.getLocalDir("image/")+ IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    }
                    else
                    {
                        Toast.makeText(PersonHeadEdit.this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }
                }

                break;
            case RESULT_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null)
                {
                    getImageToView(data);
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //intent.setClassName("com.android.camera", "com.android.camera.CropImage");
        Log.d(TAG, "getPath URi"+Uri.fromFile(new File(getPath(this, uri))));
//        Log.d(TAG, "原生Uri:"+uri);
        uri = Uri.fromFile(new File(getPath(this, uri)));
        intent.setData(uri);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //intent.putExtra("noFaceDetection", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private void getImageToView(Intent data)
    {
        Bundle extras = data.getExtras();
        
        Log.d(TAG, "galley bundle:"+extras);

        if (extras != null)
        {
            Bitmap photo = extras.getParcelable("data");
            uploadHeadImg(photo);
            //Drawable drawable = new BitmapDrawable(photo);
            //mImg.setImageDrawable(drawable);
        }
    }

    public void uploadHeadImg(final Bitmap bitmap){

    	Log.d(TAG, "bitmap:"+bitmap);
        dialog.show();
        new RequestAdpater(){
            @Override
            public void onReponse(ResponseData data) {
            	Log.d(TAG, "response data:"+data.getJsonObject()+"message:"+data.getMsg()+
            			"code:"+data.getCode());
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                	
                  setHeadImg(PersonHeadEdit.this, mUserCode);
                }
            }@Override
             public void onProgress(ProgressMessage msg) {

            }
        }.setUrl("/home/phone/personcenter/uploadhead")
                .setRequestMethod(RequestAdpater.RequestMethod.ePost1)
                .setRequestType(RequestAdpater.RequestType.eFileUp)
                .setBitmap(bitmap)
                .notifyRequest();

    }

    private  void setHeadImg(final Context context,String userCode){


        String url = "/home/pc/personcenter/showhead";
        String fileName = userCode + ".jpg";
        new RequestAdpater(){
            @Override
            public void onReponse(ResponseData data) {
            	Log.d(TAG, "response data:"+data.getJsonObject()+"message:"+data.getMsg()+
            			"code:"+data.getCode());
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {

                    dialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                }else {
                    dialog.dismiss();
                    Toast.makeText(context,data.getMsg()+"",1).show();

                }
            }@Override
             public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .setRequestMethod(RequestAdpater.RequestMethod.eGet)
                .setRequestType(RequestAdpater.RequestType.eFileDown)
                .addParam("usercode", userCode)
                .setLocalDir(UserAccount.getLocalDir("imgCache/"))
                .setFileName(fileName)
                .notifyRequest();
    }
    
    //获取图库后的路径
    
    @TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
            String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

//    OnImgHttpResponse onImgHttpResponse = new OnImgHttpResponse() {
//        @Override
//        public void onResponse(ImageView view, Bitmap bp, String tag) {
//
//        }
//
//        @Override
//        public void onUpload(ImgHttpMessage msg)
//        {
//            //Toast.makeText(PersonHeadEdit.this,msg.mResMsg,1000).show();
//            if(msg.mResCode == MessageId.HTTP_RESPONSE_UPLOAD_SUCCESS)
//            {
//                finish();
//            }
//        }
//    };
}