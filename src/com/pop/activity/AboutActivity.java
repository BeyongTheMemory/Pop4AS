package com.pop.activity;

import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class AboutActivity extends Activity{
	LinearLayout phone = null;
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
       window.requestFeature(window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.aboutpop_activity);
		Button backButton =(Button)findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener(){


			public void onClick(View v) {
				AboutActivity.this.finish();

			}

		});
		phone = (LinearLayout) findViewById(R.id.layout_phone);
        phone.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:18258467176"));
				startActivity(intent);
			}
			
		});
		
	}

}
