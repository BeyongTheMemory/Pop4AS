/**
 * ע�����
 *
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
import com.pop.request.RegistRequest;
import com.pop.response.Response;
import com.pop.response.ResultResponse;
import com.pop.response.user.LoginResponse;
import com.pop.activity.widget.MyEditText;
import com.pop.util.CollectionUtil;
import com.pop.util.EncryptUtil;
import com.pop.util.IpUtil;
import com.pop.util.UrlUtil;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ContentView(R.layout.regist_activity)
public class RegistActivity extends BaseActivity {
    @ViewInject(R.id.username_text)
    private MyEditText usernameText;
    @ViewInject(R.id.password_text)
    private MyEditText passwordText;
    @ViewInject(R.id.sure_password_text)
    private MyEditText surePasswordText;
    @ViewInject(R.id.regist_button)
    private Button registButton;
    @ViewInject(R.id.back_button)
    private Button backButton;
    private CircleProgressDialog circleProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        circleProgressDialog = new CircleProgressDialog(this);
        circleProgressDialog.setText("注册中...");
        usernameText.setShowWords("首位为字母,5~15位之间,区分大小写");
        passwordText.setShowWords("5~15位之间,区分大小写");
        surePasswordText.setShowWords("请再次输入密码");

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(RegistActivity.this, LoginActivity.class);
                RegistActivity.this.startActivity(intent);
                RegistActivity.this.finish();
            }
        });

        usernameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//失去焦点
                    String username = usernameText.getText().toString();
                    if (checkUsername(username)) {
                    }
                }
            }
        });
        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//失去焦点
                    String password = passwordText.getText().toString();
                    if (checkPassword(password)) {
                    }
                }
            }
        });
        surePasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//失去焦点
                    String password = passwordText.getText().toString();
                    String passwordSure = surePasswordText.getText().toString();
                    if (password.equals(passwordSure)) {
                    } else {
                        Toast.makeText(RegistActivity.this, "两次输入的密码不一致!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        registButton.setOnClickListener(new regisButtonClick());
    }


    private boolean checkUsername(String userName) {
        if (userName == null || userName.length() < 5) {
            Toast.makeText(this, "账号长度不足5位!", Toast.LENGTH_SHORT).show();
            return false;
        }
        Pattern p = Pattern
                .compile("^[a-zA-Z]");
        Matcher m = p.matcher(userName);
        if (!m.find()) {
            Toast.makeText(this, "第一位必须为英文字母!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkPassword(String password) {
        if (password == null || password.length() < 5) {
            Toast.makeText(this, "密码长度不足5位!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login(){
        RequestParams params = new RequestParams(UrlUtil.getLogin());
        params.setAsJsonContent(true);
        LoginRequest loginRequest = new LoginRequest(usernameText.getText().toString(), EncryptUtil.MD5(passwordText.getText().toString()), IpUtil.getIp(RegistActivity.this));
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
                        Toast.makeText(x.app(), "注册成功!已使用注册账号登陆", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setClass(RegistActivity.this, MainActivity.class);
                        RegistActivity.this.startActivity(intent);
                        RegistActivity.this.finish();
                    } catch (DbException e) {
                        Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(x.app(), userDtoResponse.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
                circleProgressDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                circleProgressDialog.dismiss();
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                circleProgressDialog.dismiss();
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                circleProgressDialog.dismiss();
            }
        });
    }


    private class regisButtonClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if(!(checkUsername(usernameText.getText().toString()) && checkPassword(passwordText.getText().toString()))){
                return;
            }
            if (!passwordText.getText().toString().equals(surePasswordText.getText().toString())){
                Toast.makeText(RegistActivity.this, "两次输入的密码不一致!", Toast.LENGTH_SHORT).show();
                return;
            }
            circleProgressDialog.showDialog();

            RequestParams params = new RequestParams(UrlUtil.getRegist());
            params.setAsJsonContent(true);
            RegistRequest registRequest = new RegistRequest(usernameText.getText().toString(), EncryptUtil.MD5(passwordText.getText().toString()));
            params.setBodyContent(JSON.toJSONString(registRequest));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Response response = JSONObject.parseObject(result, ResultResponse.class);
                    if (response.getResult() == ClientCode.SUCCESS) {
                        //自动登陆
                        login();
                    } else {
                        Toast.makeText(x.app(), response.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    circleProgressDialog.dismiss();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                    circleProgressDialog.dismiss();
                }

                @Override
                public void onFinished() {
                    circleProgressDialog.dismiss();
                }

            });
        }
    }
}
