package com.miicaa.common.base.numberPicker;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.miicaa.home.R;


public class NumberPickerDialog{


    protected Context mContext = null;
    protected PopupWindow mPopupWindow = null;
    protected View mRoot = null;
    protected LayoutInflater mInflater;
    private OnEduitListener mOnEduitListener;
    private static EduitBuilder mBuilder;
    private EduitResult resule;
    private NumberPicker numberPicker;
    private Button comButton;
    public static EduitBuilder builder(Context context) {

        mBuilder = new EduitBuilder(context);
        return mBuilder;
    }

    private NumberPickerDialog(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        if (null != mPopupWindow && null != mRoot)
            return;
        resule = new EduitResult();

        mInflater = LayoutInflater.from(mContext);
        mRoot = mInflater.inflate(R.layout.number_picker_dialog, null);

        numberPicker = (NumberPicker)mRoot.findViewById(R.id.number_picker_dialog_numberPicker);
        Log.d("numberPicker",numberPicker.toString());
        numberPicker.setMaxValue(31);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setFocusable(true);
        numberPicker.setFocusableInTouchMode(true);

//        comButton = (Button)mRoot.findViewById(android.R.id.number_picker_dialog_numberPicker);
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
            resule.mContent = numberPicker.getValue();
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
    public void setValue(int value){
        numberPicker.setValue(value);
    }
    public void close() {
        mPopupWindow.dismiss();
        mBuilder = null;
    }
    public void setOnEduitListener(OnEduitListener onEduitListener) {
        mOnEduitListener = onEduitListener;
    }

    public class EduitResult {
        public int mContent;
    }

    public interface OnEduitListener {
        public void onClick(EduitResult r);
    }

    public static class EduitBuilder {
        NumberPickerDialog mEduit;

        public EduitBuilder(Context context) {
            mEduit = new NumberPickerDialog(context);
        }

        public EduitBuilder setOnEduitListener(OnEduitListener onEduitListener) {
            mEduit.setOnEduitListener(onEduitListener);
            return this;
        }

        public void show() {
            mEduit.show();
        }

        public void setValue(int value) {
            mEduit.setValue(value);
        }

        public void close() {
            mEduit.close();
        }
    }
}
