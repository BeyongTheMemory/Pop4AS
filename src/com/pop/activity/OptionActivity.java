package com.pop.activity;


import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author xg
 *         6.7
 */
public class OptionActivity extends Activity {
    private Button backButton = null;

    public void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.requestFeature(window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.option_activity);
        backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                OptionActivity.this.finish();

            }

        });
        RelativeLayout aboutLayout = (RelativeLayout) findViewById(R.id.about_layout);
        aboutLayout.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(OptionActivity.this, AboutActivity.class);
                OptionActivity.this.startActivity(intent);

            }

        });

//	aboutText = (TextView)findViewById(R.id.aboutPopLayout_textView);
//	aboutText.setOnClickListener(new OnClickListener(){
//
//		
//		public void onClick(View v) {
//			  Intent intent = new Intent();
//   			     intent.setClass(OptionActivity.this,AboutActivity.class);
//   			     OptionActivity.this.startActivity(intent);
//		}
//		
//	});

//	accountManagerLayout = (RelativeLayout)findViewById(R.id.accountManagerLayout);
//	accountManagerLayout.setOnClickListener(new OnClickListener(){
//
//		public void onClick(View v) {
//			  Intent intent = new Intent();
//   			     intent.setClass(OptionActivity.this,AccoutActivity.class);
//   			     OptionActivity.this.startActivity(intent);
//		}
//
//	});
//
//	aboutPopLayout = (RelativeLayout)findViewById(R.id.aboutPopLayout);
//	aboutPopLayout.setOnClickListener(new OnClickListener(){
//
//
//		public void onClick(View v) {
//			  Intent intent = new Intent();
//   			     intent.setClass(OptionActivity.this,AboutActivity.class);
//   			     OptionActivity.this.startActivity(intent);
//		}
//
//	});
//

        //noticeAndRemindLayout = (RelativeLayout)findViewById(R.id. noticeAndRemindLayout);
//	 noticeAndRemindLayout.setOnClickListener(new OnClickListener(){
//
//
//		public void onClick(View v) {
//			  Intent intent = new Intent();
//   			     intent.setClass(OptionActivity.this,MessageNoticeActivity.class);
//   			     OptionActivity.this.startActivity(intent);
//		}
//
//	});
//	accountText = (TextView)findViewById(R.id.accountManager_textView);
//	accountText.setOnClickListener(new OnClickListener(){
//	
//		public void onClick(View v) {
//			  Intent intent = new Intent();
//   			     intent.setClass(OptionActivity.this,AccoutActivity.class);
//   			     OptionActivity.this.startActivity(intent);
//		}
//		
//	});
    }

}
