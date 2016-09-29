package com.pop.activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pop.R;
import com.pop.enume.ClientCode;
import com.pop.model.UserDto;
import com.pop.request.UpdateUserRequest;
import com.pop.response.Response;
import com.pop.response.ResultResponse;
import com.pop.response.qiniu.TokenResponse;
import com.pop.util.MsgType;
import com.pop.util.QiNiuImgType;
import com.pop.util.StringUtil;
import com.pop.util.UrlUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.account_activity)
public class AccoutActivity extends BaseActivity {
    @ViewInject(R.id.modify_username)
    private TextView modifyUsername;
    @ViewInject(R.id.modify_pwd)
    private TextView modifyPwd;
    @ViewInject(R.id.modify_sign)
    private TextView modifySign;
    @ViewInject(R.id.modify_email)
    private TextView modifyEmail;
    @ViewInject(R.id.modify_phone)
    private TextView modifyPhone;
    @ViewInject(R.id.tv_name)
    private TextView name;
    @ViewInject(R.id.tv_sign)
    private TextView sign;
    @ViewInject(R.id.tv_phone)
    private TextView phone;
    @ViewInject(R.id.tv_email)
    private TextView email;
    private UserDto userDto;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String data = bundle.getString("input");
            switch (msg.arg1) {
                case 1://name
                    name.setText(data);
                    userDto.setName(data);
                    break;
                case 2://sign
                    sign.setText(data);
                    userDto.setIntroduction(data);
                    break;
                case 3://phone
                    phone.setText(data);
                    userDto.setPhone(data);
                    break;
                case 4://email
                    email.setText(data);
                    userDto.setEmail(data);
                    break;
            }
            try {
                db.update(userDto);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                AccoutActivity.this.finish();

            }

        });
        modifyUsername.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(AccoutActivity.this)
                        .title("更改昵称")
                        .content("好名字能让别人更容易记住你")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                                InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .inputRangeRes(2, 20, R.color.green)
                        .widgetColor(Color.WHITE)
                        .input(null, name.getText().toString(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                UpdateUserRequest updateUserRequest = new UpdateUserRequest();
                                updateUserRequest.setName(input.toString());
                                updateUserRequest.setBasic(true);
                                updateUserInfo(updateUserRequest,input.toString(),1);
                            }
                        }).show();
            }
        });
        modifySign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(AccoutActivity.this)
                        .title("更新签名")
                        .content("在此写下您的个性签名")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                                InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .inputRangeRes(2, 30, R.color.green)
                        .widgetColor(Color.WHITE)
                        .input(null, sign.getText().toString(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                UpdateUserRequest updateUserRequest = new UpdateUserRequest();
                                updateUserRequest.setIntroduction(input.toString());
                                updateUserRequest.setBasic(true);
                                updateUserInfo(updateUserRequest,input.toString(),2);
                            }
                        }).show();
            }
        });
        modifyPhone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(AccoutActivity.this)
                        .title("修改手机号")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                                InputType.TYPE_CLASS_PHONE)
                        .inputRangeRes(11, 11, R.color.green)
                        .widgetColor(Color.WHITE)
                        .input(null, phone.getText().toString(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                UpdateUserRequest updateUserRequest = new UpdateUserRequest();
                                updateUserRequest.setPhone(Long.parseLong(input.toString()));
                                updateUserRequest.setBasic(false);
                                updateUserInfo(updateUserRequest,input.toString(),3);
                            }
                        }).show();
            }
        });
        modifyEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(AccoutActivity.this)
                        .title("修改邮箱")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                        .widgetColor(Color.WHITE)
                        .input(null, email.getText().toString(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                UpdateUserRequest updateUserRequest = new UpdateUserRequest();
                                updateUserRequest.setEmail(input.toString());
                                updateUserRequest.setBasic(false);
                                updateUserInfo(updateUserRequest,input.toString(),4);
                            }
                        }).show();
            }
        });
        modifyPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AccoutActivity.this, ModifyPwdActivity.class);
                AccoutActivity.this.startActivity(intent);
            }
        });
        init();
    }

    private void init() {
        try {
            userDto = db.selector(UserDto.class).findFirst();
            if (userDto != null) {
                name.setText(userDto.getName());
                if (StringUtil.isNotEmpty(userDto.getIntroduction())) {
                    sign.setText(userDto.getIntroduction());
                }
                if (StringUtil.isNotEmpty(userDto.getPhone())) {
                    phone.setText(userDto.getPhone());
                }
                if (StringUtil.isNotEmpty(userDto.getEmail())) {
                    email.setText(userDto.getEmail());
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void updateUserInfo(UpdateUserRequest user, final String input, final int type) {
        String url = UrlUtil.getUpdateUser();
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(JSON.toJSONString(user));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResultResponse response = JSONObject.parseObject(result, ResultResponse.class);
                if (response.getResult() == ClientCode.SUCCESS) {
                    Message msgMessage = new Message();
                    msgMessage.arg1 = type;
                    Bundle bundle = new Bundle();
                    bundle.putString("input",input);
                    msgMessage.setData(bundle);
                    handler.sendMessage(msgMessage);
                } else {
                    Toast.makeText(x.app(), response.getErrorMsg(), Toast.LENGTH_LONG).show();
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
