package com.pop.activity;

import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class AccoutActivity  extends Activity{
	public void onCreate(Bundle savedInstanceState){
		Window window = this.getWindow();
       window.requestFeature(window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.account_activity);
		Button backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				AccoutActivity.this.finish();

			}

		});
		
	}
}
