package com.miicaa.common.base.dataPicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.miicaa.home.R;



/**
 * Created by apple on 13-12-10.
 */
public class DataPickerDialog {


    protected Context mContext = null;
    protected PopupWindow mPopupWindow = null;
    protected View mRoot = null;
    protected LayoutInflater mInflater;
    private OnEduitListener mOnEduitListener;
    private static EduitBuilder mBuilder;
    private EduitResult resule;
    private DataPicker indexDataPicker;
    private DataPicker weekDataPicker;
    private Button comButton;
    private static String[] indexData;
    private static String[] weekData;
    public static EduitBuilder builder(Context context, String[] indexDataStr,String[] weekDataStr) {
        indexData = indexDataStr;
        weekData = weekDataStr;
        mBuilder = new EduitBuilder(context);
        return mBuilder;
    }

    private DataPickerDialog(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        if (null != mPopupWindow && null != mRoot)
            return;
        resule = new EduitResult();

        mInflater = LayoutInflater.from(mContext);
        mRoot = mInflater.inflate(R.layout.data_picker_dialog, null);

        indexDataPicker = (DataPicker)mRoot.findViewById(R.id.data_picker_dialog_index_dataPicker);
        indexDataPicker.setTextStr(indexData);
        indexDataPicker.setMaxValue(indexData.length-1);
        indexDataPicker.setMinValue(0);
        indexDataPicker.setWrapSelectorWheel(true);
        indexDataPicker.setFocusable(true);
        indexDataPicker.setFocusableInTouchMode(true);

        weekDataPicker = (DataPicker)mRoot.findViewById(R.id.data_picker_dialog_week_dataPicker);
        weekDataPicker.setTextStr(weekData);
        weekDataPicker.setMaxValue(weekData.length-1);
        weekDataPicker.setMinValue(0);
        weekDataPicker.setWrapSelectorWheel(true);
        weekDataPicker.setFocusable(true);
        weekDataPicker.setFocusableInTouchMode(true);

        comButton = (Button)mRoot.findViewById(R.id.data_picker_dialog_com_button);
        comButton.setOnClickListener(comButtonClick);


        mPopupWindow = new PopupWindow(mRoot, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);


    }

    View.OnClickListener comButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resule.index = indexDataPicker.getValue();
            resule.week = weekDataPicker.getValue();
            mOnEduitListener.onClick(resule);
            close();
        }
    };

    public void show() {
        Rect frame = new Rect();

        int statusBarHeight = frame.top;

        Activity activity = (Activity) mContext;
        View view = activity.getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(frame);
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
    public void setValue(int indexValue,int weekValue){
        indexDataPicker.setValue(indexValue);
        weekDataPicker.setValue(weekValue);
    }
    public void close() {
        mPopupWindow.dismiss();
        mBuilder = null;
    }
    public void setOnEduitListener(OnEduitListener onEduitListener) {
        mOnEduitListener = onEduitListener;
    }

    public class EduitResult {
        public int index;
        public int week;
    }

    public interface OnEduitListener {
        public void onClick(EduitResult r);
    }

    public static class EduitBuilder {
        DataPickerDialog mEduit;

        public EduitBuilder(Context context) {
            mEduit = new DataPickerDialog(context);
        }

        public EduitBuilder setOnEduitListener(OnEduitListener onEduitListener) {
            mEduit.setOnEduitListener(onEduitListener);
            return this;
        }

        public void show() {
            mEduit.show();
        }

        public void setValue(int indexValue,int weekValue) {
            mEduit.setValue(indexValue,weekValue);
        }

        public void close() {
            mEduit.close();
        }
    }
}
