package com.pop.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.pop.R;

public class PersonnalActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.requestFeature(window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personnal_activity);

    }
}