package com.miicaa.home.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * @author hacker。e
 * 自动换行的TextView,在某些时候textView会失效不能自动换行，不能随着宽度而换到下一行，
 *
 */

public class WrapTextView extends TextView{

	public WrapTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public WrapTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WrapTextView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		CharSequence text = getText();
		Paint p = getPaint();
		FontMetrics fm = p.getFontMetrics(); 
		float x = 0;
        float baseLine = fm.descent - fm.ascent;
        float y = baseLine;
		p.setAntiAlias(true); 
		float width = getMeasuredWidth();
		String[] strLines = autoSplit(text.toString(), p , width);
		for(String str : strLines){
			canvas.drawText(str, x, y, p);//绘制字体
			y += baseLine + fm.leading;//添加字体间距
		}
		super.onDraw(canvas);
	}
	
	
	/**
     * 自动分割文本
     * @param content 需要分割的文本
     * @param p  画笔，用来根据字体测量文本的宽度
     * @param width 指定的宽度
     * @return 一个字符串数组，保存每行的文本
     */ 
    private String[] autoSplit(String content, Paint p, float width) { 
        int length = content.length(); 
        float textWidth = p.measureText(content); 
        if(textWidth <= width) { 
            return new String[]{content}; 
        } 
         
        int start = 0, end = 1, i = 0; 
        int lines = (int) Math.ceil(textWidth / width); //计算行数 
        String[] lineTexts = new String[lines]; 
        while(start < length) { 
            if(p.measureText(content, start, end) > width) { //文本宽度超出控件宽度时 
                lineTexts[i++] = (String) content.subSequence(start, end); 
                start = end; 
            } 
            if(end == length) { //不足一行的文本 
                lineTexts[i] = (String) content.subSequence(start, end); 
                break; 
            } 
            end += 1; 
        } 
        return lineTexts; 
    } 
	
	
	

}
