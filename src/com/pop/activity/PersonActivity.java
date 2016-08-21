package com.pop.activity;


import com.pop.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author xg
 *         6.7
 */
public class PersonActivity extends Activity {
    private Button backButton = null;
    private Button optionButton = null;
    private EditText signatureText = null;


//	private TextView optionText = null;
//	private RelativeLayout personLayout = null;
//	private RelativeLayout myPhotoLayout = null;
//	private RelativeLayout myCollectLayout = null;
//	private RelativeLayout goodPopLayout = null;
//	private RelativeLayout individuationLayout = null;

    public void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.requestFeature(window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.person_activity);
        backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                PersonActivity.this.finish();

            }

        });

        optionButton = (Button) findViewById(R.id.option_button);
        optionButton.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PersonActivity.this, OptionActivity.class);
                PersonActivity.this.startActivity(intent);

            }

        });
        signatureText = (EditText) findViewById(R.id.signature_text);
        signatureText.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 得到焦点时弹出提示
                } else {
                    // 失去焦点时上传信息
                }
            }
        });

        RelativeLayout accountLayout = (RelativeLayout) findViewById(R.id.account_layout);
        accountLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PersonActivity.this, AccoutActivity.class);
                PersonActivity.this.startActivity(intent);
            }
        });
//		      optionText = (TextView)findViewById(R.id.option_text);
//		      optionText.setOnClickListener(new OnClickListener(){
//
//
//					public void onClick(View v) {
//						Intent intent = new Intent();
//						intent.setClass(PersonActivity.this,OptionActivity.class);
//						PersonActivity.this.startActivity(intent);
//
//					}
//
//				});
//		      personLayout = (RelativeLayout)findViewById(R.id.personLayout);
//		      personLayout.setOnClickListener(new OnClickListener(){
//
//		  		public void onClick(View v) {
//		  			  Intent intent = new Intent();
//		     			     intent.setClass(PersonActivity.this,AccoutActivity.class);
//		     			     PersonActivity.this.startActivity(intent);
//		  		}
//
//		  	});
//
//		      myPhotoLayout = (RelativeLayout)findViewById(R.id.myPhotoLayout);
//		      myPhotoLayout.setOnClickListener(new OnClickListener(){
//
//		  		public void onClick(View v) {
//		  			  Intent intent = new Intent();
//		     			     intent.setClass(PersonActivity.this,MyPhotoActivity.class);
//		     			     PersonActivity.this.startActivity(intent);
//		  		}
//
//		  	});
//
//		      myCollectLayout = (RelativeLayout)findViewById(R.id.myCollectLayout);
//		      myCollectLayout.setOnClickListener(new OnClickListener(){
//
//		  		public void onClick(View v) {
//		  			  Intent intent = new Intent();
//		     			     intent.setClass(PersonActivity.this,MyCollectActivity.class);
//		     			     PersonActivity.this.startActivity(intent);
//		  		}
//
//		  	});
//
//		      goodPopLayout = (RelativeLayout)findViewById(R.id.goodPopLayout);
//		      goodPopLayout.setOnClickListener(new OnClickListener(){
//
//		  		public void onClick(View v) {
//		  			  Intent intent = new Intent();
//		     			     intent.setClass(PersonActivity.this,TopPopActivity.class);
//		     			     PersonActivity.this.startActivity(intent);
//		  		}
//
//		  	});
//
//
//		      individuationLayout = (RelativeLayout)findViewById(R.id.individuationLayout);
//		      individuationLayout.setOnClickListener(new OnClickListener(){
//
//		  		public void onClick(View v) {
//		  			  Intent intent = new Intent();
//		     			     intent.setClass(PersonActivity.this,PersonnalActivity.class);
//		     			     PersonActivity.this.startActivity(intent);
//		  		}
//
//		  	});
//

    }

}
