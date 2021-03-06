//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.ui.discover.crm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import com.miicaa.home.ui.menu.ScrrenViewGroup;
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
public final class CRMScreenMenu_
    extends CRMScreenMenu
    implements HasViews, OnViewChangedListener
{

    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public CRMScreenMenu_(Context context) {
        super(context);
        init_();
    }

    public CRMScreenMenu_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public CRMScreenMenu_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static CRMScreenMenu build(Context context) {
        CRMScreenMenu_ instance = new CRMScreenMenu_(context);
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
            inflate(getContext(), layout.crm_screen_menu, this);
            onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static CRMScreenMenu build(Context context, AttributeSet attrs) {
        CRMScreenMenu_ instance = new CRMScreenMenu_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static CRMScreenMenu build(Context context, AttributeSet attrs, int defStyle) {
        CRMScreenMenu_ instance = new CRMScreenMenu_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        bottomLayout = ((RelativeLayout) hasViews.findViewById(id.bottomLayout));
        screenViewGroup = ((ScrrenViewGroup) hasViews.findViewById(id.screenViewGroup));
        origiatorTextView = ((TextView) hasViews.findViewById(id.origiatorTextView));
        {
            View view = hasViews.findViewById(id.origiatorTextView);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CRMScreenMenu_.this.origiatorViewClick(view);
                    }

                }
                );
            }
        }
        afterView();
    }

}
