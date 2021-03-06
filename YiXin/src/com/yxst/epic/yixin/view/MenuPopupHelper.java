/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yxst.epic.yixin.view;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.internal.widget.ListPopupWindow;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.miicaa.home.R;
import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Operation;
import com.yxst.epic.yixin.listener.OnOperationClickListener;

/**
 * Presents a menu as a small, simple popup anchored to another view.
 *
 * @hide
 */
public class MenuPopupHelper implements AdapterView.OnItemClickListener, View.OnKeyListener,
        ViewTreeObserver.OnGlobalLayoutListener, PopupWindow.OnDismissListener{

    private static final String TAG = "MenuPopupHelper";

    static final int ITEM_LAYOUT = R.layout.abc_popup_menu_item_layout;

    private Context mContext;
    private LayoutInflater mInflater;
    private ListPopupWindow mPopup;
    private List<Operation> mMenu;
    private int mPopupMaxWidth;
    private View mAnchorView;
    private ViewTreeObserver mTreeObserver;

    private MenuAdapter mAdapter;

    private OnOperationClickListener mPresenterCallback;
    
    boolean mForceShowIcon;

    private ViewGroup mMeasureParent;

    public MenuPopupHelper(Context context, List<Operation> menu) {
        this(context, menu, null);
    }

    public MenuPopupHelper(Context context, List<Operation> menu,
            View anchorView) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mMenu = menu;

        final Resources res = context.getResources();
        mPopupMaxWidth = Math.max(res.getDisplayMetrics().widthPixels / 2,
                res.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));

        mAnchorView = anchorView;
    }

    public void setAnchorView(View anchor) {
        mAnchorView = anchor;
    }

    public void setForceShowIcon(boolean forceShow) {
        mForceShowIcon = forceShow;
    }

    public void show() {
        if (!tryShow()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
        ListView listView = mPopup.getListView();
        if (listView != null) {
        	listView.setBackgroundColor(Color.WHITE);
        }
    }

    public boolean tryShow() {
        mPopup = new ListPopupWindow(mContext, null, R.attr.popupMenuStyle);
        mPopup.setOnDismissListener(this);
        mPopup.setOnItemClickListener(this);

        mAdapter = new MenuAdapter(mMenu);
        mPopup.setAdapter(mAdapter);
        mPopup.setModal(true);

        View anchor = mAnchorView;
        if (anchor != null) {
            final boolean addGlobalListener = mTreeObserver == null;
            mTreeObserver = anchor.getViewTreeObserver(); // Refresh to latest
            if (addGlobalListener) {
                mTreeObserver.addOnGlobalLayoutListener(this);
            }
            mPopup.setAnchorView(anchor);
        } else {
            return false;
        }

        mPopup.setContentWidth(Math.min(measureContentWidth(mAdapter), mPopupMaxWidth));
        mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        mPopup.show();
        mPopup.getListView().setOnKeyListener(this);
        return true;
    }

    public void dismiss() {
        if (isShowing()) {
            mPopup.dismiss();
        }
    }

    @Override
	public void onDismiss() {
        mPopup = null;
        if (mTreeObserver != null) {
            if (!mTreeObserver.isAlive()) {
                mTreeObserver = mAnchorView.getViewTreeObserver();
            }
            mTreeObserver.removeGlobalOnLayoutListener(this);
            mTreeObserver = null;
        }
    }

    public boolean isShowing() {
        return mPopup != null && mPopup.isShowing();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuAdapter adapter = mAdapter;
        
        dismiss();
        
        if (mPresenterCallback != null) {
        	mPresenterCallback.onOperationClick(adapter.getItem(position));
        }
    }

    @Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_MENU) {
            dismiss();
            return true;
        }
        return false;
    }

    private int measureContentWidth(ListAdapter adapter) {
        // Menus don't tend to be long, so this is more sane than it looks.
        int width = 0;
        View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec =
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec =
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(mContext);
            }
            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            width = Math.max(width, itemView.getMeasuredWidth());
        }
        return width;
    }

    @Override
    public void onGlobalLayout() {
        if (isShowing()) {
            final View anchor = mAnchorView;
            if (anchor == null || !anchor.isShown()) {
                dismiss();
            } else if (isShowing()) {
                // Recompute window size and position
                mPopup.show();
            }
        }
    }

    public void updateMenuView(boolean cleared) {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
    
    public void setCallback(OnOperationClickListener cb) {
        mPresenterCallback = cb;
    }
    
    private class MenuAdapter extends BaseAdapter {

        private List<Operation> mAdapterMenu;

        public MenuAdapter(List<Operation> menu) {
            mAdapterMenu = menu;
        }

        @Override
		public int getCount() {
            return mAdapterMenu.size();
        }

        @Override
		public Operation getItem(int position) {
            return mAdapterMenu.get(position);
        }

        @Override
		public long getItemId(int position) {
            // Since a menu item's ID is optional, we'll use the position as an
            // ID for the item in the AdapterView
            return position;
        }

        @Override
		public View getView(int position, View convertView, ViewGroup parent) {
        	Operation operation = getItem(position);
        	
            if (convertView == null) {
                convertView = mInflater.inflate(ITEM_LAYOUT, parent, false);
            }

//            MenuView.ItemView itemView = (MenuView.ItemView) convertView;
            ListMenuItemView itemView = (ListMenuItemView) convertView;
//            if (mForceShowIcon) {
//                ((ListMenuItemView) convertView).setForceShowIcon(true);
//            }
            ((ListMenuItemView) convertView).setForceShowIcon(mForceShowIcon);
            itemView.initialize(true, operation.content, false, null);
            
            return convertView;
        }
    }
}

