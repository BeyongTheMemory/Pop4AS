package com.pop.activity;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

import com.alibaba.fastjson.JSON;
import com.pop.R;
import com.pop.enume.ClientCode;
import com.pop.enume.PopModelLayoutEnume;
import com.pop.enume.PopTypeEnum;
import com.pop.request.NewPopRequest;
import com.pop.response.ResultResponse;
import com.pop.response.qiniu.TokenResponse;
import com.pop.util.MsgType;
import com.pop.util.QiNiuImgType;
import com.pop.util.StringUtil;
import com.pop.util.UrlUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import me.xiaopan.switchbutton.SwitchButton;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author xg
 *         6.7
 */
@ContentView(R.layout.newpop_photo_activity)
public class NewPopPhotoActivity extends BaseActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_POPCHOSE = 2;
    private String imageURL;
    private int popType = 1;//泡泡类型
    @ViewInject(R.id.pop_text)
    private TextView popText;//泡泡类型文字描述
    @ViewInject(R.id.writePrivacy_text)
    private TextView writePrivacyText;//投放按钮
    @ViewInject(R.id.appPhpto_imageView)
    private ImageView addImageView;//图片
    @ViewInject(R.id.words_edit)
    private EditText wordsEdit;//输入框内容
    @ViewInject(R.id.move_switch)
    private SwitchButton moveSwitch;//漂浮泡泡
    @ViewInject(R.id.anonymous_switch)
    private SwitchButton anonymousSwitch;//匿名
    @ViewInject(R.id.onlyOnce_switch)
    private SwitchButton onlyOnceSwitch;//阅后即焚


    private CircleProgressDialog circleProgressDialog;

    private LinkedBlockingQueue<String> linkedBlockingQueue;//转移token

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case MsgType.SUCCESS:
                    circleProgressDialog.dismiss();
                    NewPopPhotoActivity.this.finish();
                    break;
                case MsgType.FALI:
                    Toast.makeText(x.app(), "投放失败,请重试!", Toast.LENGTH_LONG).show();
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
        circleProgressDialog.setText("投放中...");
        addImageView.setOnClickListener(new SelectListener());
        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPopPhotoActivity.this.finish();
            }
        });
        RelativeLayout popLayout = (RelativeLayout) findViewById(R.id.pop_layout);
        popLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(NewPopPhotoActivity.this, PopChoseActivity.class);
                startActivityForResult(intent, RESULT_LOAD_POPCHOSE);
            }
        });
        writePrivacyText.setOnClickListener(new UploadListener());
    }
//        cancelText = (TextView) findViewById(R.id.cancel_text);
//        cancelText.setOnClickListener(new OnClickListener() {
//
//
//            public void onClick(View v) {
//                NewPop_photo_Activity.this.finish();
//
//            }
//
//        });
//        editText = (EditText) findViewById(R.id.words_edit);
//        writePrivacy = (TextView) findViewById(R.id.writePrivacy_text);
//        //selectBtn = (Button) findViewById(R.id.select);
//        //uploadBtn = (Button) findViewById(R.id.upload);
//        //addImageView = (ImageView) findViewById(R.id.imgView);
//        addImageView = (ImageView) findViewById(R.id.appPhpto_imageView);
//        addImageView.setOnClickListener(new SelectListener());
//        writePrivacy.setOnClickListener(new UploadListener());
//        //selectBtn.setOnClickListener(new SelectListener());
//        //uploadBtn.setOnClickListener(new UploadListener());
//   }

    private class SelectListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }

    }

    //
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            imageURL = picturePath;

            cursor.close();

            addImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else if (requestCode == RESULT_LOAD_POPCHOSE && resultCode == RESULT_OK && null != data) {
            PopModelLayoutEnume popModelLayoutEnume = (PopModelLayoutEnume) data.getSerializableExtra("popModelLayoutEnume");
            popText.setText(popModelLayoutEnume.getName());

        }
    }

    //
    private class UploadListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            circleProgressDialog.showDialog();
//            // TODO Auto-generated method stub
            UploadThread uploadThread = new UploadThread();
            new Thread(uploadThread).start();
//            UploadManager uploadManager = new UploadManager();
//            String data = imageURL;
//            String key = null;
//            String token = "dr56VGibvHi2T2sUs7t_nFI384KvdrPqjI2XeCzv:lwRw2ifXRQ3r3mQEsWzhugB-GT4=:eyJzY29wZSI6ImhlYWRpbWciLCJkZWFkbGluZSI6MTQ3MjA0OTI4Nn0=";
//            uploadManager.put(data, key, token,
//                    new UpCompletionHandler() {
//                        @Override
//                        public void complete(String key, ResponseInfo info, JSONObject res) {
//                            //res包含hash、key等信息，具体字段取决于上传策略的设置。
//                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
//                        }
//                    }, null);

//            final Communication com = new Communication();
//            final Map<String, Object> map = new HashMap<String, Object>();
//
//            Log.v("zuihi", "zuihi:" + latitude + "," + longitude + "," + altitude + ",");
//
//            map.put("username", "a");
//            map.put("latitude", latitude);
//            map.put("logitude", longitude);
//            map.put("height", altitude);
//            map.put("info", editText.getText().toString());
//            final String url = "http://121.40.120.82:8080/PopService/UploadPictureServlet";
//            final String imageName = "tupian";
//            new Thread() {
//                public void run() {
//                    String result = com.communication02(url, map, imageURL, imageName);
//                    System.out.println(result);
//
//                    //Message msg = handler.obtainMessage();
//                }
//            }.start();
//            NewPop_photo_Activity.this.finish();
        }

    }

//			@Override
//			public boolean onCreateOptionsMenu(Menu menu) {
//				// Inflate the menu; this adds items to the action bar if it is present.
//				getMenuInflater().inflate(R.menu.main, menu);
//				return true;
//			}


    private class UploadThread implements Runnable {
        public void run() {
            //压缩
            Luban.get(NewPopPhotoActivity.this)
                    .load(new File(imageURL))                     //传人要压缩的图片
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
                                                    //更新头像地址
                                                    String url = "";
                                                    try {
                                                        url = res.getString("hash");
                                                    } catch (JSONException e) {
                                                        sendEroMsg();
                                                        e.printStackTrace();
                                                    }
                                                    throwPop(url);

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
                            } finally {
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
                        TokenResponse tokenResponse = com.alibaba.fastjson.JSONObject.parseObject(result, TokenResponse.class);
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


        private void throwPop(final String url) {
            //经纬度
            SharedPreferences sharedPreferences = getSharedPreferences("loacation",
                    Activity.MODE_PRIVATE);
            double latitude = Double.parseDouble(sharedPreferences.getString("latitude", ""));
            double longitude = Double.parseDouble(sharedPreferences.getString("longitude", ""));
            double altitude = Double.parseDouble(sharedPreferences.getString("altitude", ""));
            //构造请求对象
            RequestParams params = new RequestParams(UrlUtil.getNewPop());
            params.setAsJsonContent(true);
            NewPopRequest newPopRequest = new NewPopRequest();
            newPopRequest.setType(PopTypeEnum.word_photo_person);
            newPopRequest.setLatitude(latitude);
            newPopRequest.setLongitude(longitude);
            newPopRequest.setAltitude(altitude);
            newPopRequest.setModel(PopModelLayoutEnume.getType(popText.getText().toString()));
            newPopRequest.setImgUrl(url);
            newPopRequest.setMessage(wordsEdit.getText().toString());
            newPopRequest.setIsShowy(moveSwitch.isChecked() ? 1 : 0);
            newPopRequest.setOnlyOnce(onlyOnceSwitch.isChecked() ? 1 : 0);
            newPopRequest.setAnonymous(anonymousSwitch.isChecked() ? 1 : 0);
            params.setBodyContent(JSON.toJSONString(newPopRequest));
            //发送请求
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {

                    ResultResponse response = com.alibaba.fastjson.JSONObject.parseObject(result, ResultResponse.class);
                    if (response.getResult() == ClientCode.SUCCESS) {
                        Toast.makeText(x.app(),"投放成功!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(x.app(), response.getErrorMsg(), Toast.LENGTH_LONG).show();
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


    private void sendEroMsg() {
        Message msgMessage = new Message();
        msgMessage.arg1 = MsgType.FALI;
        handler.sendMessage(msgMessage);
    }
}


