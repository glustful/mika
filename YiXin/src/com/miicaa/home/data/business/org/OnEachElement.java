package com.miicaa.home.data.business.org;

import android.content.ContentValues;

/**
 * Created by Administrator on 13-12-30.
 */
public interface OnEachElement {
	public TreeElement eachElement(ContentValues row);
	public void afterAddToParent(TreeElement node);
}
