package com.pop.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.pop.R;

public class MyPhotoActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {  
		Window window = this.getWindow();
       window.requestFeature(window.FEATURE_NO_TITLE);
	      super.onCreate(savedInstanceState);  
	      setContentView(R.layout.myphoto_activity);
	}
}