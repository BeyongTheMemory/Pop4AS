/**
 * ע�����
 *
 * @author xg
 * 6.1
 */
package com.pop.activity;


import com.pop.R;
import com.pop.servlet.RegistAsyncServlet;
import com.pop.activity.widget.MyEditText;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RegistActivity extends Activity {
    private MyEditText usernameText = null;
    private MyEditText passwordText = null;
    private MyEditText surepasswordText = null;
    private TextView backText = null;
    private TextView informationText = null;
    private Button registButton = null;
    String url = "http://1.utifpop.sinaapp.com/newAccount.php";

    public void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.requestFeature(window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.regist_activity);

        usernameText = (MyEditText) findViewById(R.id.username_text);
        usernameText.setShowWords("�����û���");
        usernameText.invalidate();
        usernameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO �Զ����ɵķ������
                if (hasFocus) {
                    usernameText.setJudge(false);
                    usernameText.invalidate();
                } else {
                    if (usernameText.getText().toString().equals("")) {
                        usernameText.setJudge(true);
                        usernameText.invalidate();
                    }
                }
            }
        });
        //���������
        passwordText = (MyEditText) findViewById(R.id.password_text);
        passwordText.setShowWords("�������룺");
        passwordText.invalidate();
        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO �Զ����ɵķ������
                if (hasFocus) {
                    passwordText.setJudge(false);
                    passwordText.invalidate();
                } else {
                    if (passwordText.getText().toString().equals("")) {
                        passwordText.setJudge(true);
                        passwordText.invalidate();
                    }
                }
            }
        });
        //ȷ�������
        surepasswordText = (MyEditText) findViewById(R.id.sure_password_text);
        surepasswordText.setShowWords("�ٴ��������룺");
        surepasswordText.invalidate();
        surepasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO �Զ����ɵķ������
                if (hasFocus) {
                    surepasswordText.setJudge(false);
                    surepasswordText.invalidate();
                } else {
                    if (surepasswordText.getText().toString().equals("")) {
                        surepasswordText.setJudge(true);
                        surepasswordText.invalidate();
                    }
                }
            }
        });

        //����
        backText = (TextView) findViewById(R.id.back_text);
        backText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO �Զ����ɵķ������
                RegistActivity.this.finish();
            }

        });


        //informationText = (TextView) findViewById(R.id.information_textView);

        registButton = (Button) findViewById(R.id.regist_button);
        registButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (passwordText.getText().toString().equals(surepasswordText.getText().toString())) {
                    RegistAsyncServlet registAsyncServlet = new RegistAsyncServlet(usernameText.getText().toString(), passwordText.getText().toString(), informationText);
                    //Log.v("information","sc");
                    registAsyncServlet.execute(url);
                    //Log.v("information","sc2");
                } else {
                    informationText.setText("������������벻һ�£�");
                }

            }

        });

    }

}
