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
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class ScheduActivity extends BaseActivity {

    private CircleProgressDialog circleProgressDialog;
    private UserDto userDto;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.shedu_activity);

        ImageView mImageView = (ImageView) findViewById(R.id.welcomimageView1);
        AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        mAlphaAnimation.setDuration(5000);//持续5秒还无法完成登陆的话跳到登陆界面
        mImageView.setAnimation(mAlphaAnimation);

        circleProgressDialog = new CircleProgressDialog(this);
        circleProgressDialog.setText("登录中...");
        //自动登录
        try {
            userDto = db.selector(UserDto.class).findFirst();
            if (userDto != null) {//自动登录
                RequestParams params = new RequestParams(UrlUtil.getLogin());
                params.setAsJsonContent(true);
                LoginRequest loginRequest = new LoginRequest(userDto.getAccount(), userDto.getPassword(), IpUtil.getIp(ScheduActivity.this));
                params.setBodyContent(JSON.toJSONString(loginRequest));
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        LoginResponse userDtoResponse = JSONObject.parseObject(result, LoginResponse.class);
                        if (userDtoResponse.getResult() == ClientCode.SUCCESS) {
                            Intent intent = new Intent();
                            intent.setClass(ScheduActivity.this, MainActivity.class);
                            ScheduActivity.this.startActivity(intent);
                            ScheduActivity.this.finish();
                        } else {
                            toLogin();
                            Toast.makeText(x.app(), userDtoResponse.getErrorMsg(), Toast.LENGTH_LONG).show();
                        }
                        circleProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        circleProgressDialog.dismiss();
                        toLogin();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                        circleProgressDialog.dismiss();
                        toLogin();
                    }

                    @Override
                    public void onFinished() {
                    }
                });
            }else {
                toLogin();
            }

        } catch (DbException e) {
            e.printStackTrace();
            toLogin();
        }


        mAlphaAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                //登录的dialog
                if (userDto != null) {
                    circleProgressDialog.showDialog();
                } else {
                    toLogin();
                }
            }
        });
    }


    private void toLogin() {
        Intent mIntent = new Intent(ScheduActivity.this, LoginActivity.class);//直接进主界面
        startActivity(mIntent);
        ScheduActivity.this.finish();
    }
}