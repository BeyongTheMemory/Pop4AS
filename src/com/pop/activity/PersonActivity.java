package com.pop.activity;


import com.alibaba.fastjson.JSONObject;
import com.pop.R;
import com.pop.activity.widget.CircleImageView;
import com.pop.enume.ClientCode;
import com.pop.model.UserDto;
import com.pop.response.qiniu.TokenResponse;
import com.pop.response.user.UrlResponse;
import com.pop.util.FileUtil;
import com.pop.util.MsgType;
import com.pop.util.QiNiuImgType;
import com.pop.util.StringUtil;
import com.pop.util.UrlUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author xg
 *         6.7
 */
@ContentView(R.layout.person_activity)
public class PersonActivity extends BaseActivity {
    private Button backButton = null;
    private Button optionButton = null;
    @ViewInject(R.id.signature_text)
    private EditText signatureText;
    @ViewInject(R.id.headPhoto_image)
    private CircleImageView headPhotoImage;
    private File imgFile;
    private UserDto userDto;
    private static int RESULT_LOAD_IMAGE = 1;

    @ViewInject(R.id.username_textView)
    private EditText usernameTextView;

    private CircleProgressDialog circleProgressDialog;

    private LinkedBlockingQueue<String> linkedBlockingQueue;//转移token


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case MsgType.SUCCESS:
                    circleProgressDialog.dismiss();
                    headPhotoImage.setImageDrawable(Drawable.createFromPath(imgFile.getAbsolutePath()));
                    break;
                case MsgType.FALI:
                    Toast.makeText(x.app(), "设置失败,请重试!", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkedBlockingQueue = new LinkedBlockingQueue<>();
        circleProgressDialog = new CircleProgressDialog(this);
        circleProgressDialog.setText("上传中...");
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

        RelativeLayout accountLayout = (RelativeLayout) findViewById(R.id.account_layout);
        accountLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PersonActivity.this, AccoutActivity.class);
                PersonActivity.this.startActivity(intent);
            }
        });

        headPhotoImage.setOnClickListener(new SelectListener());
        init();
    }

    private void init() {
        try {
            userDto = db.selector(UserDto.class).findFirst();
            if (userDto != null) {
                usernameTextView.setText(userDto.getName());
                if (StringUtil.isNotEmpty(userDto.getIntroduction())) {
                    signatureText.setText(userDto.getIntroduction());
                }
                if (StringUtil.isNotEmpty(userDto.getHeadUrl())) {
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.default_head);
                    ImageOptions imageOptions = new ImageOptions.Builder()
                            .setIgnoreGif(false)
                            // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                            //.setUseMemCache(false)
                            .setLoadingDrawable(drawable)
                            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                            .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                            .build();

                    x.image().bind(headPhotoImage, userDto.getHeadUrl(), imageOptions);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private class SelectListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            i.putExtra("crop", "true");

            // aspectX aspectY 是宽高的比例
            i.putExtra("aspectX", 1);
            i.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            i.putExtra("outputX", headPhotoImage.getWidth());
            i.putExtra("outputY", headPhotoImage.getHeight());
            i.putExtra("return-data", true);
            //
            FileUtil.initFile();
            imgFile = new File(FileUtil.basicPath + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名
            i.putExtra("output", Uri.fromFile(imgFile));  // 专入目标文件
            i.putExtra("outputFormat", "JPEG"); //输入文件格式
            Intent wrapperIntent = Intent.createChooser(i, "先择头像"); //开始 并设置标题
            startActivityForResult(wrapperIntent, RESULT_LOAD_IMAGE);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            circleProgressDialog.showDialog();
            UploadImgThread uploadImgThread = new UploadImgThread();
            new Thread(uploadImgThread).start();
        }
    }

    private class UploadImgThread implements Runnable {
        public void run() {
            //压缩
            Luban.get(PersonActivity.this)
                    .load(imgFile)                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() { //设置回调

                        @Override
                        public void onStart() {
                            //压缩开始前调用，可以在方法内启动 loading UI
                            //获取token
                            getToken();
                        }

                        @Override
                        public void onSuccess(File file) {
                            //压缩成功后调用，返回压缩后的图片文件
                            //上传图片
                            UploadManager uploadManager = new UploadManager();
                            File data = file;
                            String key = null;
                            String token;
                            try {
                                token = linkedBlockingQueue.take();
                                if (StringUtil.isNotEmpty(token)) {
                                    uploadManager.put(data, key, token,
                                            new UpCompletionHandler() {
                                                @Override
                                                public void complete(String key, ResponseInfo info, org.json.JSONObject res) {
                                                    //res包含hash、key等信息，具体字段取决于上传策略的设置。
                                                    Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                                                    //更新头像地址
                                                    String url = "";
                                                    try {
                                                        url = res.getString("hash");
                                                    } catch (JSONException e) {
                                                        sendEroMsg();
                                                        e.printStackTrace();
                                                    }
                                                    updateHead(url);

                                                }
                                            }, null);
                                } else {
                                    sendEroMsg();
                                }
                            } catch (InterruptedException e) {
                                sendEroMsg();
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            //当压缩过去出现问题时调用,还是将token消费掉
                            e.printStackTrace();
                            try {
                                String token = linkedBlockingQueue.take();
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }finally {
                                sendEroMsg();
                            }
                        }
                    }).launch();    //启动压缩
        }

        private void getToken() {
            RequestParams params = new RequestParams(UrlUtil.getQiNiuToken(QiNiuImgType.head));
            params.setAsJsonContent(true);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        TokenResponse tokenResponse = JSONObject.parseObject(result, TokenResponse.class);
                        if (tokenResponse.getResult() == ClientCode.SUCCESS) {
                            linkedBlockingQueue.put(tokenResponse.getToken());
                        } else {
                            linkedBlockingQueue.put("");
                            Toast.makeText(x.app(), tokenResponse.getErrorMsg(), Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException e) {
                        sendEroMsg();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    try {
                        linkedBlockingQueue.put("");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    try {
                        linkedBlockingQueue.put("");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinished() {
                }
            });
        }


        private void updateHead(final String url) {
            RequestParams params = new RequestParams(UrlUtil.getUploadHead(url));
            params.setAsJsonContent(true);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    //更新本地缓存
                    try {
                        UrlResponse urlResponse = JSONObject.parseObject(result, UrlResponse.class);
                        if (urlResponse.getResult() == ClientCode.SUCCESS) {
                            userDto.setHeadUrl(urlResponse.getUrl());
                            db.update(userDto);
                        } else {
                            Toast.makeText(x.app(), urlResponse.getErrorMsg(), Toast.LENGTH_LONG).show();
                        }

                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    Message msgMessage = new Message();
                    msgMessage.arg1 = MsgType.SUCCESS;
                    handler.sendMessage(msgMessage);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Message msgMessage = new Message();
                    msgMessage.arg1 = MsgType.FALI;
                    handler.sendMessage(msgMessage);
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Message msgMessage = new Message();
                    msgMessage.arg1 = MsgType.FALI;
                    handler.sendMessage(msgMessage);
                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinished() {
                }
            });
        }
    }

    private void sendEroMsg(){
        Message msgMessage = new Message();
        msgMessage.arg1 = MsgType.FALI;
        handler.sendMessage(msgMessage);
    }



}
