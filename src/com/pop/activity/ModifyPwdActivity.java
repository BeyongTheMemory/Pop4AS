/**
 * ע�����
 *
 * @author xg
 * 6.1
 */
package com.pop.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pop.R;
import com.pop.activity.widget.MyEditText;
import com.pop.enume.ClientCode;
import com.pop.model.UserDto;
import com.pop.request.LoginRequest;
import com.pop.request.RegistRequest;
import com.pop.request.UpdatePwdRequest;
import com.pop.request.UpdateUserRequest;
import com.pop.response.Response;
import com.pop.response.ResultResponse;
import com.pop.response.user.LoginResponse;
import com.pop.util.CollectionUtil;
import com.pop.util.EncryptUtil;
import com.pop.util.IpUtil;
import com.pop.util.UrlUtil;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

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

@ContentView(R.layout.modify_pwd_activity)
public class ModifyPwdActivity extends BaseActivity {
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
        circleProgressDialog.setText("修改中...");
        usernameText.setShowWords("请输入原密码");
        passwordText.setShowWords("5~15位之间,区分大小写");
        surePasswordText.setShowWords("请再次输入密码");

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ModifyPwdActivity.this, LoginActivity.class);
                ModifyPwdActivity.this.startActivity(intent);
                ModifyPwdActivity.this.finish();
            }
        });

        usernameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//失去焦点
                    String oldPassword = usernameText.getText().toString();
                    if (checkPassword(oldPassword)) {
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
                        Toast.makeText(ModifyPwdActivity.this, "两次输入的密码不一致!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        registButton.setOnClickListener(new regisButtonClick());
    }



    private boolean checkPassword(String password) {
        if (password == null || password.length() < 5) {
            Toast.makeText(this, "密码长度不足5位!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private class regisButtonClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if(!(checkPassword(usernameText.getText().toString()) && checkPassword(passwordText.getText().toString()))){
                return;
            }
            if (!passwordText.getText().toString().equals(surePasswordText.getText().toString())){
                Toast.makeText(ModifyPwdActivity.this, "两次输入的密码不一致!", Toast.LENGTH_SHORT).show();
                return;
            }
            circleProgressDialog.showDialog();

            RequestParams params = new RequestParams(UrlUtil.getUpdatePwd());
            params.setAsJsonContent(true);
            UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest(EncryptUtil.MD5(usernameText.getText().toString()),EncryptUtil.MD5(passwordText.getText().toString()));
            params.setBodyContent(JSON.toJSONString(updatePwdRequest));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Response response = JSONObject.parseObject(result, ResultResponse.class);
                    if (response.getResult() == ClientCode.SUCCESS) {
                        try {
                            UserDto userDto = db.selector(UserDto.class).findFirst();
                            userDto.setPassword(EncryptUtil.MD5(passwordText.getText().toString()));
                            db.update(userDto);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
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
