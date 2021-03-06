//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.yxst.epic.yixin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import com.readystatesoftware.viewbadger.BadgeView;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;


/**
 * We use @SuppressWarning here because our java code
 * generator doesn't know that there is no need
 * to import OnXXXListeners from View as we already
 * are in a View.
 * 
 */
@SuppressWarnings("unused")
public final class TabIndicator_
    extends TabIndicator
    implements HasViews, OnViewChangedListener
{

    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public TabIndicator_(Context context) {
        super(context);
        init_();
    }

    public TabIndicator_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public static TabIndicator build(Context context) {
        TabIndicator_ instance = new TabIndicator_(context);
        instance.onFinishInflate();
        return instance;
    }

    /**
     * The mAlreadyInflated_ hack is needed because of an Android bug
     * which leads to infinite calls of onFinishInflate()
     * when inflating a layout with a parent and using
     * the <merge /> tag.
     * 
     */
    @Override
    public void onFinishInflate() {
        if (!alreadyInflated_) {
            alreadyInflated_ = true;
            inflate(getContext(), layout.tab_indicator, this);
            onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static TabIndicator build(Context context, AttributeSet attrs) {
        TabIndicator_ instance = new TabIndicator_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        mBadgeView = ((BadgeView) hasViews.findViewById(id.v_badge));
        mTitleTV = ((TextView) hasViews.findViewById(id.tv_title));
        mIconIV = ((ImageView) hasViews.findViewById(id.iv_icon));
        afterViews();
    }

}
