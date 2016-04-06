//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.base.share.round;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
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
public final class RoundItemView_
    extends RoundItemView
    implements HasViews, OnViewChangedListener
{

    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public RoundItemView_(Context context) {
        super(context);
        init_();
    }

    public RoundItemView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public RoundItemView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static RoundItemView build(Context context) {
        RoundItemView_ instance = new RoundItemView_(context);
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
            inflate(getContext(), layout.select_round_item, this);
            onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static RoundItemView build(Context context, AttributeSet attrs) {
        RoundItemView_ instance = new RoundItemView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static RoundItemView build(Context context, AttributeSet attrs, int defStyle) {
        RoundItemView_ instance = new RoundItemView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        label = ((RadioButton) hasViews.findViewById(id.label));
        content = ((TextView) hasViews.findViewById(id.content));
    }

}
