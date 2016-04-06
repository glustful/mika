package com.miicaa.common.http;

/**
 * Created by Administrator on 13-9-13.
 */
public class MessageId
{
    public static final int HTTP_RESPONSE_OTHER = 0;
    public static final int HTTP_RESPONSE_ERROR = HTTP_RESPONSE_OTHER + 1;
    public static final int HTTP_RESPONSE_RECEIVING = HTTP_RESPONSE_OTHER + 2;
    public static final int HTTP_RESPONSE_RECEIVE_END = HTTP_RESPONSE_OTHER + 3;
    public static final int HTTP_RESPONSE_UPLOAD_SUCCESS = HTTP_RESPONSE_OTHER + 4;
    public static final int HTTP_TIMEOUT = HTTP_RESPONSE_OTHER - 1;
    public static final int HTTP_RESPONSE_UPLOAD_FAILURE = HTTP_RESPONSE_OTHER + 5;


    public static final int DATA_REQUEST_HAVE_RESPNOSE = 90;

    public static final int FILE_DOWNLOADING = 100;
    public static final int FILE_DOWNLOAD_END = FILE_DOWNLOADING + 1;
    public static final int FILE_DOWNLOAD_ERORR = FILE_DOWNLOADING + 2;
    public static final int FILE_DOWNLOAD_STREAM_GET = FILE_DOWNLOADING + 3;


    private static final int MSG_UI_BEGIN = 110;
    public static final int MSG_SELECTED_AN_USER = MSG_UI_BEGIN + 1;


    private static final int MSG_SENDER_BEGIN = 1000;
    public static final int MSG_SENDER_NETWORK = MSG_SENDER_BEGIN+1;

    public static final int MSG_SENDER_UI = MSG_SENDER_BEGIN + 10;
}
