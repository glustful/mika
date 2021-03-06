//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.miicaa.home.ui.checkwork;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.miicaa.home.R.id;
import com.miicaa.home.R.layout;
import com.miicaa.home.view.AttachmentView;
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
public final class CheckWorkPersonDetailView_
    extends CheckWorkPersonDetailView
    implements HasViews, OnViewChangedListener
{

    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public CheckWorkPersonDetailView_(Context context) {
        super(context);
        init_();
    }

    public CheckWorkPersonDetailView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public CheckWorkPersonDetailView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static CheckWorkPersonDetailView build(Context context) {
        CheckWorkPersonDetailView_ instance = new CheckWorkPersonDetailView_(context);
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
            inflate(getContext(), layout.checkwork_persondetail_view, this);
            onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        infalter = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static CheckWorkPersonDetailView build(Context context, AttributeSet attrs) {
        CheckWorkPersonDetailView_ instance = new CheckWorkPersonDetailView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static CheckWorkPersonDetailView build(Context context, AttributeSet attrs, int defStyle) {
        CheckWorkPersonDetailView_ instance = new CheckWorkPersonDetailView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        signWhereTextButton = ((Button) hasViews.findViewById(id.signWhereTextBtn));
        beizhuButton = ((Button) hasViews.findViewById(id.beizhuBtn));
        attachmentView = ((AttachmentView) hasViews.findViewById(id.attachementView));
        signTextView = ((TextView) hasViews.findViewById(id.signTextView));
        {
            View view = hasViews.findViewById(id.beizhuBtn);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CheckWorkPersonDetailView_.this.beizhuBtnClick(view);
                    }

                }
                );
            }
        }
    }

}
