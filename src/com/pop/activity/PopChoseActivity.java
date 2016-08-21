package com.pop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


import com.pop.R;
import com.pop.enume.PopModelLayoutEnume;


public class PopChoseActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop_chose_activity);
		RelativeLayout color_pop_layout = (RelativeLayout)findViewById(R.id.color_pop_layout);
		color_pop_layout.setOnClickListener(this);

		RelativeLayout blue_pop_layout = (RelativeLayout)findViewById(R.id.blue_pop_layout);
		blue_pop_layout.setOnClickListener(this);

		RelativeLayout purple_pop_layout = (RelativeLayout)findViewById(R.id.purple_pop_layout);
		purple_pop_layout.setOnClickListener(this);

		RelativeLayout rdylw_pop_layout = (RelativeLayout)findViewById(R.id.rdylw_pop_layout);
		rdylw_pop_layout.setOnClickListener(this);

		RelativeLayout pink_pop_layout = (RelativeLayout)findViewById(R.id.pink_pop_layout);
		pink_pop_layout.setOnClickListener(this);

		RelativeLayout yellow_pop_layout = (RelativeLayout)findViewById(R.id.yellow_pop_layout);
		yellow_pop_layout.setOnClickListener(this);

		RelativeLayout green_pop_layout = (RelativeLayout)findViewById(R.id.green_pop_layout);
		green_pop_layout.setOnClickListener(this);

		RelativeLayout light_blue_pop_layout = (RelativeLayout)findViewById(R.id.light_blue_pop_layout);
		light_blue_pop_layout.setOnClickListener(this);

		RelativeLayout purple_old_pop_layout = (RelativeLayout)findViewById(R.id.purple_old_pop_layout);
		purple_old_pop_layout.setOnClickListener(this);

		RelativeLayout red_old_pop_layout = (RelativeLayout)findViewById(R.id.red_old_pop_layout);
		red_old_pop_layout.setOnClickListener(this);

		RelativeLayout green_old_pop_layout = (RelativeLayout)findViewById(R.id.green_old_pop_layout);
		green_old_pop_layout.setOnClickListener(this);

		RelativeLayout dark_green_old_pop_layout = (RelativeLayout)findViewById(R.id.dark_green_old_pop_layout);
		dark_green_old_pop_layout.setOnClickListener(this);


	}


	@Override
	public void onClick(View v) {
		PopModelLayoutEnume popModelLayoutEnume = PopModelLayoutEnume.getEnum(v.getId());
		Intent intent = new Intent();
		intent.putExtra("popModelLayoutEnume", popModelLayoutEnume);
		setResult(RESULT_OK, intent);
		finish();
	}
}
