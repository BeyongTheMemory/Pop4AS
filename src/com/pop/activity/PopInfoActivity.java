package com.pop.activity;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import com.alibaba.fastjson.JSONObject;
import com.pop.R;
import com.pop.activity.widget.CircleImageView;
import com.pop.enume.ClientCode;
import com.pop.model.PopInfoDto;
import com.pop.response.pop.PopInfoResponse;
import com.pop.util.MsgType;
import com.pop.util.StringUtil;
import com.pop.util.UrlUtil;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


@ContentView(R.layout.popinfo_activity)
public class PopInfoActivity extends BaseActivity {
    @ViewInject(R.id.headPhoto_image)
    private CircleImageView headPhotoImage;

    @ViewInject(R.id.sex_img)
    private ImageView sexImg;

    @ViewInject(R.id.username_textView)
    private TextView usernameTextView;

    @ViewInject(R.id.signature_text)
    private TextView signatureText;

    @ViewInject(R.id.close_ib)
    private ImageButton closeIb;

    @ViewInject(R.id.iv_pic)
    private ImageView ivPic;

    @ViewInject(R.id.tv_content)
    private TextView tvContent;

    @ViewInject(R.id.fire_iv)
    private ImageView fireIv;

    private CircleProgressDialog circleProgressDialog;

    private ImageOptions headImageOptions;

    private ImageOptions picImageOptions;

    private PopInfoDto popInfoDto;

    private Resources res;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case MsgType.SUCCESS:
                    //设置属性
                    x.image().bind(headPhotoImage, popInfoDto.getUserHeadUrl(), headImageOptions);
                    if(StringUtil.isNotEmpty(popInfoDto.getImgUrl())){
                        x.image().bind(ivPic, popInfoDto.getUserHeadUrl(), picImageOptions);
                    }
                    //性别
                    switch (popInfoDto.getSex()){
                        case 1:sexImg.setImageDrawable(res.getDrawable(R.drawable.male));
                            break;
                        case 2:sexImg.setImageDrawable(res.getDrawable(R.drawable.female));
                            break;
                        default:sexImg.setImageDrawable(res.getDrawable(R.drawable.unknown_sex));
                            break;
                    }
                    //阅后即焚
                    if(popInfoDto.getOnlyOnce() > 0){
                        fireIv.setVisibility(View.VISIBLE);
                    }
                    usernameTextView.setText(popInfoDto.getUserName());
                    signatureText.setText(popInfoDto.getUserIntroduction());
                    tvContent.setText(popInfoDto.getMessage());

                    //创建时间
                    //距离
                    circleProgressDialog.dismiss();
                    break;
                case MsgType.FALI:
                    circleProgressDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        circleProgressDialog = new CircleProgressDialog(this);
        circleProgressDialog.setText("获取中...");


    }

    public void init(){
        res = getResources();
        headImageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)
                // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                //.setUseMemCache(false)
                .setLoadingDrawable(res.getDrawable(R.drawable.default_head))
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
        picImageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)
                // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                //.setUseMemCache(false)
                .setLoadingDrawable(res.getDrawable(R.drawable.updating))
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
    }

    public void getPopInfo(){
        circleProgressDialog.showDialog();
        SharedPreferences sharedPreferences = getSharedPreferences("pop",
                Activity.MODE_PRIVATE);
        long popId =sharedPreferences.getLong("id",1);
        RequestParams params = new RequestParams(UrlUtil.getPopInfo());
        params.setAsJsonContent(true);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("popId",popId);
        params.setBodyContent(jsonObject.toJSONString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //更新本地缓存
                    PopInfoResponse popInfoResponse = JSONObject.parseObject(result, PopInfoResponse.class);
                    if (popInfoResponse.getResult() == ClientCode.SUCCESS) {
                        popInfoDto = popInfoResponse.getPopInfoDto();
                        Message msgMessage = new Message();
                        msgMessage.arg1 = MsgType.SUCCESS;
                        handler.sendMessage(msgMessage);
                    } else {
                        Toast.makeText(x.app(), popInfoResponse.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), cex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
            }
        });

    }
}
