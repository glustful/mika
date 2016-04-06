package net.simonvt.menudrawer;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;

final class ViewHelper {

    private ViewHelper() {
    }

    @SuppressLint("NewApi")
	public static int getLeft(View v) {
        if (MenuDrawer.USE_TRANSLATIONS) {
            return (int) (v.getLeft() + v.getTranslationX());
        }

        return v.getLeft();
    }

    @SuppressLint("NewApi")
	public static int getTop(View v) {
        if (MenuDrawer.USE_TRANSLATIONS) {
            return (int) (v.getTop() + v.getTranslationY());
        }

        return v.getTop();
    }

    @SuppressLint("NewApi")
	public static int getRight(View v) {
        if (MenuDrawer.USE_TRANSLATIONS) {
            return (int) (v.getRight() + v.getTranslationX());
        }

        return v.getRight();
    }

    @SuppressLint("NewApi")
	public static int getBottom(View v) {
        if (MenuDrawer.USE_TRANSLATIONS) {
            return (int) (v.getBottom() + v.getTranslationY());
        }

        return v.getBottom();
    }

    @SuppressLint("NewApi")
	public static int getLayoutDirection(View v) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return v.getLayoutDirection();
//        }

//        return View.LAYOUT_DIRECTION_LTR;
    	return 0;
    	
    }
}
