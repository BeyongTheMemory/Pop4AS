package com.pop.activity.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {
	public MyEditText(Context context,AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
	}

	public MyEditText(Context context,AttributeSet attrs) {
		super(context,attrs);
	}


	public void setShowWords(String showWords) {
		SpannableString ss = new SpannableString(showWords);
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);
		ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		this.setHint(new SpannedString(ss));
	}
	
	
	

}
