package com.pop.activity.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {
	private boolean judge = true;
	private String showWords = "";
	public MyEditText(Context context) {
		super(context);
	}
	
	public MyEditText(Context context,AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
	}
	
	public MyEditText(Context context,AttributeSet attrs) {
		super(context,attrs);
	}
	
	protected void onDraw(Canvas canvas){
		if(judge){
		Paint paint = new Paint();
		paint.setTextSize(20);
		paint.setColor(Color.GRAY);
		canvas.drawText(showWords, 10, getHeight()/2 + 5, paint);
		}
		else{
			Paint paint = new Paint();
			paint.setTextSize(20);
			paint.setColor(Color.GRAY);
			canvas.drawText("", 10, getHeight()/2 + 5, paint);
		}
		super.onDraw(canvas);
	}
	
	public boolean getJudge(){
		return judge;
	}
	
	
	public void setJudge(boolean judge){
		this.judge = judge;
	}

	public String getShowWords() {
		return showWords;
	}

	public void setShowWords(String showWords) {
		this.showWords = showWords;
	}
	
	
	

}
