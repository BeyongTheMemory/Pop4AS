package com.pop.activity;


import com.pop.R;
import com.pop.activity.widget.CircleImageView;
import com.pop.util.FileUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author xg
 *         6.7
 */
public class PersonActivity extends Activity {
    private Button backButton = null;
    private Button optionButton = null;
    private EditText signatureText = null;
    private CircleImageView headPhotoImage = null;
    private File imgFile;
    private static int RESULT_LOAD_IMAGE = 1;


//	private TextView optionText = null;
//	private RelativeLayout personLayout = null;
//	private RelativeLayout myPhotoLayout = null;
//	private RelativeLayout myCollectLayout = null;
//	private RelativeLayout goodPopLayout = null;
//	private RelativeLayout individuationLayout = null;

    public void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.requestFeature(window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.person_activity);
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
        signatureText = (EditText) findViewById(R.id.signature_text);
        signatureText.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 得到焦点时弹出提示
                } else {
                    // 失去焦点时上传信息
                }
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

        headPhotoImage = (CircleImageView)findViewById(R.id.headPhoto_image);
        headPhotoImage.setOnClickListener(new SelectListener());
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
            imgFile=new File(FileUtil.basicPath+ Calendar.getInstance().getTimeInMillis()+".jpg"); // 以时间秒为文件名
            i.putExtra("output", Uri.fromFile(imgFile));  // 专入目标文件
            i.putExtra("outputFormat", "JPEG"); //输入文件格式
            Intent wrapperIntent = Intent.createChooser(i, "先择头像"); //开始 并设置标题
            startActivityForResult(wrapperIntent, RESULT_LOAD_IMAGE);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            imageURL = picturePath;
//            cursor.close();
//            Bundle bundle = data.getExtras();
//            if (bundle != null) {
//                Bitmap photo = bundle.getParcelable("data");
//                headPhotoImage.setImageBitmap(photo);
//                uploadImg();
//            }
            headPhotoImage.setImageDrawable(Drawable.createFromPath(imgFile.getAbsolutePath()));
            uploadImg();


        }
    }

    private void uploadImg(){
        //网络传输
        //构建loadui
        final Dialog progressDialog = new Dialog(PersonActivity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("头像上传中...");
        //压缩
        Luban.get(this)
                .load(imgFile)                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                        //TODO 压缩开始前调用，可以在方法内启动 loading UI
                        progressDialog.show();
                    }
                    @Override
                    public void onSuccess(File file) {
                        //TODO 压缩成功后调用，返回压缩后的图片文件
                        //上传图片
                        UploadManager uploadManager = new UploadManager();
                        File data = file;
                        String key = null;
                        String token = "dr56VGibvHi2T2sUs7t_nFI384KvdrPqjI2XeCzv:lwRw2ifXRQ3r3mQEsWzhugB-GT4=:eyJzY29wZSI6ImhlYWRpbWciLCJkZWFkbGluZSI6MTQ3MjA0OTI4Nn0=";
                        uploadManager.put(data, key, token,
                                new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject res) {
                                        //res包含hash、key等信息，具体字段取决于上传策略的设置。
                                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                                        progressDialog.dismiss();
                                    }
                                }, null);


                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO 当压缩过去出现问题时调用
                        progressDialog.dismiss();
                    }
                }).launch();    //启动压缩
    }

}
