package com.pop.activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pop.R;
import com.pop.model.UserDto;
import com.pop.util.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.account_activity)
public class AccoutActivity  extends BaseActivity{
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
	public void onCreate(Bundle savedInstanceState){
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
						.inputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
								InputType.TYPE_TEXT_FLAG_CAP_WORDS)
						.inputRangeRes(2, 20, R.color.green)
						.widgetColor(Color.WHITE)
						.input(null, name.getText().toString(),new MaterialDialog.InputCallback() {
							@Override
							public void onInput(MaterialDialog dialog, CharSequence input) {
								// Do something
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
						.inputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
								InputType.TYPE_TEXT_FLAG_CAP_WORDS)
						.inputRangeRes(2, 30, R.color.green)
						.widgetColor(Color.WHITE)
						.input(null, name.getText().toString(),new MaterialDialog.InputCallback() {
							@Override
							public void onInput(MaterialDialog dialog, CharSequence input) {
								// Do something
							}
						}).show();
			}
		});
		modifyPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				new MaterialDialog.Builder(AccoutActivity.this)
						.title("修改手机号")
						.inputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
								InputType.TYPE_TEXT_FLAG_CAP_WORDS)
						.inputRangeRes(11, 11, R.color.green)
						.widgetColor(Color.WHITE)
						.input(null, name.getText().toString(),new MaterialDialog.InputCallback() {
							@Override
							public void onInput(MaterialDialog dialog, CharSequence input) {
								// Do something
							}
						}).show();
			}
		});
		modifyEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				new MaterialDialog.Builder(AccoutActivity.this)
						.title("修改邮箱")
						.inputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
								InputType.TYPE_TEXT_FLAG_CAP_WORDS)
						.inputRangeRes(11, 11, R.color.green)
						.widgetColor(Color.WHITE)
						.input(null, name.getText().toString(),new MaterialDialog.InputCallback() {
							@Override
							public void onInput(MaterialDialog dialog, CharSequence input) {
								// Do something
							}
						}).show();
			}
		});
		init();
	}
	private void init(){
		try {
			userDto = db.selector(UserDto.class).findFirst();
			if(userDto != null){
				name.setText(userDto.getName());
				if(StringUtil.isNotEmpty(userDto.getIntroduction())){
					sign.setText(userDto.getIntroduction());
				}
				if(StringUtil.isNotEmpty(userDto.getPhone())){
					phone.setText(userDto.getPhone());
				}
				if(StringUtil.isNotEmpty(userDto.getEmail())){
					email.setText(userDto.getEmail());
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
}
