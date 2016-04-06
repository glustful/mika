//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public final class AttachmentView_
    extends AttachmentView
    implements HasViews, OnViewChangedListener
{

    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public AttachmentView_(Context context) {
        super(context);
        init_();
    }

    public AttachmentView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public AttachmentView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static AttachmentView build(Context context) {
        AttachmentView_ instance = new AttachmentView_(context);
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
            inflate(getContext(), layout.attachment_s_view, this);
            onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static AttachmentView build(Context context, AttributeSet attrs) {
        AttachmentView_ instance = new AttachmentView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static AttachmentView build(Context context, AttributeSet attrs, int defStyle) {
        AttachmentView_ instance = new AttachmentView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        picTextView = ((TextView) hasViews.findViewById(id.picTextView));
        fileTextView = ((TextView) hasViews.findViewById(id.fileTextView));
        fileLayout = ((RelativeLayout) hasViews.findViewById(id.fileLayout));
        fileImageView = ((ImageView) hasViews.findViewById(id.fileImageView));
        picLayout = ((RelativeLayout) hasViews.findViewById(id.picLayout));
        picImageView = ((ImageView) hasViews.findViewById(id.picImageView));
        aftetView();
    }

}
