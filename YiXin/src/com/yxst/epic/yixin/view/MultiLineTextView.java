package com.yxst.epic.yixin.view;

import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.FontMetrics;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MultiLineTextView extends TextView {
	@SuppressLint("NewApi") @Override
	protected void onDraw(Canvas canvas) {
	
	        try{	        
	        for (int i = 0, j = 0; i < m_iRealLine; i++, j++)  
	        {  
	            canvas.drawText((String)(m_String.elementAt(i)), 0,  m_iFontHeight+m_iFontHeight * j, getPaint());  
	        }
	        }catch(Exception e){
	        	
	        }
	}

	Context context;
	int screenW;
	int padding;
	int m_iRealLine=0; 
	Vector    m_String=new Vector(); 
	int m_iTextWidth;
	int m_iFontHeight;

	public MultiLineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		 screenW = ((Activity) context).getWindowManager()
					.getDefaultDisplay().getWidth();
		
         
		 padding = 100*(int)(context.getResources().getDisplayMetrics().density+0.5f);
		 m_iTextWidth = screenW
					- padding;	
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Layout layout = getLayout();
		
		if (layout != null) {
			
			int height = getHeight(this.getText().toString())+getCompoundPaddingBottom()+getCompoundPaddingTop();
			int width = getMeasuredWidth();  
			
			setMeasuredDimension(width, height);
		}
	}
	
	public int getHeight(String string){
	
		 char ch;  
	        int w = 0;  
	        int istart = 0;  
	          m_String.clear();
	          m_iRealLine = 0;
	       	        				
	        TextPaint mPaint = getPaint();
	        FontMetrics fm = mPaint.getFontMetrics();         
	        m_iFontHeight = (int) Math.ceil(fm.descent - fm.ascent);// + (int)LineSpace;//计算字体高度（字体高度＋行间距）  
	        
	        for (int i = 0; i < string.length(); i++)  
	        {  
	            ch = string.charAt(i);  
	            float[] widths = new float[1];  
	            String srt = String.valueOf(ch);  
	            mPaint.getTextWidths(srt, widths);  
	  
	            if (ch == '\n'){  
	                m_iRealLine++;  
	                m_String.addElement(string.substring(istart, i));  
	                istart = i + 1;  
	                w = 0;  
	            }else{  
	                w += (int) (Math.ceil(widths[0]));  
	                if (w > m_iTextWidth){  
	                    m_iRealLine++;  
	                    m_String.addElement(string.substring(istart, i));  
	                    istart = i;  
	                    i--;  
	                    w = 0;  
	                }else{  
	                    if (i == (string.length() - 1)){  
	                        m_iRealLine++;  
	                        m_String.addElement(string.substring(istart, string.length()));  
	                    }  
	                }  
	            }  
	        }  
	        int m_iTextHeight=(m_iRealLine)*m_iFontHeight+10;  
	       
	        
	       return m_iTextHeight;
	}
	

	
	
}
