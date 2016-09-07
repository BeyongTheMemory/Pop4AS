/**
 * @author xg
 * 6.1
 */
package com.pop.activity;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pop.R;
import com.pop.enume.ClientCode;
import com.pop.model.UserDto;
import com.pop.request.LoginRequest;
import com.pop.response.user.LoginResponse;
import com.pop.util.CollectionUtil;
import com.pop.util.EncryptUtil;
import com.pop.util.IpUtil;
import com.pop.util.UrlUtil;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


public class LoginActivity extends BaseActivity {
    private Button loginButton = null;
    private TextView registText = null;
    private TextView forgetPwdText = null;
    private TextView informationText = null;
    private EditText usernameText = null;
    private EditText passwordText = null;


    public void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.requestFeature(window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        usernameText = (EditText) findViewById(R.id.username_text);
        passwordText = (EditText) findViewById(R.id.password_text);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new LoginButtonClick(this));

        //注册
        registText = (TextView) findViewById(R.id.regist_text);
        registText.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegistActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }

        });

        //忘记密码
        forgetPwdText = (TextView) findViewById(R.id.forgetpassword_text);
        forgetPwdText.setOnClickListener(new forgetPwdClick());

        //信息
        informationText = (TextView) findViewById(R.id.information_text);

    }


    private class LoginButtonClick implements OnClickListener {
        private Context context;

        public LoginButtonClick(Context context) {
            this.context = context;
        }


        @Override
        public void onClick(View v) {
            RequestParams params = new RequestParams(UrlUtil.getLogin());
            params.setAsJsonContent(true);
            LoginRequest loginRequest = new LoginRequest(usernameText.getText().toString(), EncryptUtil.MD5(passwordText.getText().toString()), IpUtil.getIp(context));
            params.setBodyContent(JSON.toJSONString(loginRequest));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    LoginResponse userDtoResponse = JSONObject.parseObject(result, LoginResponse.class);
                    if (userDtoResponse.getResult() == ClientCode.SUCCESS) {
                        //保存账户信息
                        DbManager db = x.getDb(((MyApplication) getApplication()).getDaoConfig());
                        try {
                            List<UserDto> userDtos = db.selector(UserDto.class).findAll();
                            if (!CollectionUtil.isEmpty(userDtos)) {
                                //清除现有到账户信息
                                db.delete(userDtos);
                            }
                            //保存新的账户信息
                            db.save(userDtoResponse.getUserDto());
                            //保存session信息
                        } catch (DbException e) {
                            Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(x.app(), userDtoResponse.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinished() {

                }
            });
        }

    }

    private class forgetPwdClick implements OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }


}
