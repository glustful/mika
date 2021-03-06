//package com.yxst.epic.yixin.view;
//
//import org.androidannotations.annotations.EViewGroup;
//import org.androidannotations.annotations.ViewById;
//import org.androidannotations.annotations.res.ColorRes;
//import org.androidannotations.annotations.res.ColorStateListRes;
//
//import android.content.Context;
//import android.content.res.ColorStateList;
//import android.util.AttributeSet;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.miicaa.home.R;
//import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Operation;
//
//@EViewGroup(R.layout.list_item_app_102_operation)
//public class App102Operation extends RelativeLayout {
//
//	/*
//	 * @ViewById View viewBackground;
//	 * 
//	 * @ViewById View viewDivider;
//	 */
//
//	@ViewById
//	TextView tvContent;
//
//	// @DrawableRes(R.drawable.list_selector)
//	// Drawable listSelector;
//	//
//	// @DrawableRes(R.drawable.list_selector_spinner)
//	// Drawable listSelectorSpinner;
//
//	public App102Operation(Context context) {
//		super(context);
//	}
//
//	public App102Operation(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public App102Operation(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	public void bind(Operation operation) {
//		if (operation == null) {
//			tvContent.setText(null);
//			this.setBackgroundResource(R.drawable.list_selector);
//			return;
//		}
//		
//		tvContent.setText(operation.content);
//
//		if (operation.operationList != null && operation.operationList.size() != 0) {
//			this.setBackgroundResource(R.drawable.list_selector_spinner);
//		} else {
//			this.setBackgroundResource(R.drawable.list_selector);
//		}
//	}
//
//	@ColorStateListRes(R.color.list_item_app_102_operation_text)
//	ColorStateList list_item_app_102_operation_text;
//	
//	@ColorStateListRes(R.color.list_item_app_102_operation_more_text)
//	ColorStateList list_item_app_102_operation_more_text;
//	
//	public void bindStyle(int numColumns, int position) {
//		if (position < numColumns) {
//			tvContent.setTextColor(list_item_app_102_operation_text);
//			layoutRoot.setBackgroundResource(R.drawable.list_item_app_102_operation_selector);
//		} else {
//			tvContent.setTextColor(list_item_app_102_operation_more_text);
//			layoutRoot.setBackgroundResource(R.drawable.list_item_app_102_operation_more_selector);
//		}
//	}
//}
