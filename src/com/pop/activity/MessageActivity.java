package com.pop.activity;

import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * @author xg
 *6.7
 */
public class MessageActivity extends Activity{
	private Button backButton = null;
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
       window.requestFeature(window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.message_activity);
		backButton =(Button)findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						MessageActivity.this.finish();
						
						
					}
					
				});
	}

}
