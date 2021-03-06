//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.ui.menu;

import android.content.Context;
import android.util.AttributeSet;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;


/**
 * We use @SuppressWarning here because our java code
 * generator doesn't know that there is no need
 * to import OnXXXListeners from View as we already
 * are in a View.
 * 
 */
@SuppressWarnings("unused")
public final class ApprScreenView_
    extends ApprScreenView
    implements HasViews
{

    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ApprScreenView_(Context context) {
        super(context);
        init_();
    }

    public ApprScreenView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ApprScreenView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static ApprScreenView build(Context context) {
        ApprScreenView_ instance = new ApprScreenView_(context);
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
            onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static ApprScreenView build(Context context, AttributeSet attrs) {
        ApprScreenView_ instance = new ApprScreenView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ApprScreenView build(Context context, AttributeSet attrs, int defStyle) {
        ApprScreenView_ instance = new ApprScreenView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

}
