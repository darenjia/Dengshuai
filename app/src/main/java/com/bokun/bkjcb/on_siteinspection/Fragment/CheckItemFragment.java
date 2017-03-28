package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.BuildConfig;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by BKJCB on 2017/3/23.
 */

public class CheckItemFragment extends BaseFragment implements View.OnClickListener {

    private ViewHolder viewHolder;
    private String path;
    private File sdcardTempFile;
    private int REQUESR_CODE_TAKEPHOTO = 2;
    private int REQUESR_CODE_CROPPHOTO = 1;
    private int REQUESR_CODE_RECORD = 3;
    private int REQUESR_CODE_VIDEO = 4;
    private String vdoName;
    private String imagePath = "picture/";
    private String videoPath = "video/";
    private String audioPath = "audio/";
    Uri uri = null;
    private File video;
    private File audio;

    @Override
    public View initView() {
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
        }
        return viewHolder.view;
    }

    @Override
    public void initData() {
        String content = getArguments().getString("txt");
        viewHolder.camera_view.setOnClickListener(this);
        viewHolder.video_view.setOnClickListener(this);
        viewHolder.audio_view.setOnClickListener(this);
        viewHolder.txt_content.setText(content);
        path = "/mnt/sdcard/Bokun/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.check_content_btn_camera:
//                Intent intent = new Intent(getContext(), TestActivity.class);
//                startActivityForResult(intent, 1);
//                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                // 启动相机
//                startActivityForResult(intent1, 1);
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String picName = imagePath + (new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()))
                        + ".jpg";
                LogUtil.logI(path);
                sdcardTempFile = new File(path + picName);
                /*intent.putExtra("output", Uri.fromFile(sdcardTempFile));
                startActivityForResult(intent, 100);*/
                startAction(intent, sdcardTempFile, REQUESR_CODE_TAKEPHOTO);
                break;
            case R.id.check_content_btn_audio:
                intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                String mp3Name = imagePath + (new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()))
                        + ".jpg";
                audio = new File(path + mp3Name);
                startAction(intent, audio, REQUESR_CODE_RECORD);
                break;
            case R.id.check_content_btn_video:
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.addCategory("android.intent.category.DEFAULT");

                // 保存录像到指定的路径
                vdoName = videoPath + (new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()))
                        + ".mp4";
                video = new File(path + vdoName);
               /* Uri uri = Uri.fromFile(video);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                startActivityForResult(intent, REQUESR_CODE_VIDEO);*/
                startAction(intent, video, REQUESR_CODE_VIDEO);
                break;
            case R.id.check_content_btn_remark:
                break;
        }
    }

    public class ViewHolder {
        private TextView txt_content;
        private RadioGroup mRdaioGroup;
        private ImageView camera_view;
        private ImageView audio_view;
        private ImageView video_view;
        private Button btn_remark;
        private TextView comment_word;
        private LinearLayout commtent_pic;
        private LinearLayout commtent_video;
        private LinearLayout commtent_audio;
        private View view;

        public ViewHolder() {
            LogUtil.logI("new ViewHolder");
            initWidgets();
        }

        private void initWidgets() {
            view = LayoutInflater.from(getContext()).inflate(R.layout.check_detial_view, null, false);
            txt_content = (TextView) findView(R.id.txt_content);
            camera_view = (ImageView) findView(R.id.check_content_btn_camera);
            comment_word = (TextView) findView(R.id.check_content_info);
            commtent_audio = (LinearLayout) findView(R.id.check_content_audio);
            commtent_pic = (LinearLayout) findView(R.id.check_content_pic);
            commtent_video = (LinearLayout) findView(R.id.check_content_video);
            video_view = (ImageView) findView(R.id.check_content_btn_video);
            audio_view = (ImageView) findView(R.id.check_content_btn_audio);
            btn_remark = (Button) findView(R.id.check_content_btn_remark);

        }

        private View findView(int id) {
            return view.findViewById(id);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1 && requestCode == 1) {
//            String s = data.getStringExtra("name");
//            viewHolder.comment_word.setText(s);
//        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESR_CODE_CROPPHOTO) {//对应第一种方法
                /**
                 * 通过这种方法取出的拍摄会默认压缩，因为如果相机的像素比较高拍摄出来的图会比较高清，
                 * 如果图太大会造成内存溢出（OOM），因此此种方法会默认给图片尽心压缩
                 */
                Bundle bundle = data.getExtras();
//                String path = bundle.getString("picpath");
                LogUtil.logI("bitmap" + (bundle.get("data") == null));
                Bitmap bitmap = (Bitmap) bundle.get("data");
//                Bitmap bitmap = getCutBitmap(sdcardTempFile.getAbsolutePath());

//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                bitmap = Utils.compressBitmap(sdcardTempFile.getAbsolutePath());
                ImageView mImageView = new ImageView(getContext());
                mImageView.setImageBitmap(bitmap);
                LogUtil.logI("size:" + bitmap.getByteCount());
                viewHolder.commtent_pic.addView(mImageView, params);
            } else if (requestCode == REQUESR_CODE_TAKEPHOTO) {
                LogUtil.logI("拍完进入剪裁");
                Intent intent = new Intent("com.android.camera.action.CROP");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", sdcardTempFile);
                    intent.setDataAndType(contentUri, "image/*");
                } else {
                    intent.setDataAndType(Uri.fromFile(sdcardTempFile), "image/*");
                }
                intent.putExtra("output", uri);
                intent.putExtra("crop", "true");
//                intent.putExtra("aspectX", 1);// 裁剪框比例
//                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 150);// 输出图片大小
                intent.putExtra("outputY", 150);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUESR_CODE_CROPPHOTO);
            } else if (requestCode == REQUESR_CODE_VIDEO) {
//                Bitmap bitmap = (Bitmap) bundle.get("data");
//                Bitmap bitmap = getCutBitmap(sdcardTempFile.getAbsolutePath());
                File file = video;
                LogUtil.logI(file.getAbsolutePath() + file.length());
                Bitmap bitmap = Utils.getVideoThumbnail(file.getAbsolutePath());
                bitmap = Utils.compressBitmap(bitmap);
                ImageView mImageView = new ImageView(getContext());
                mImageView.setImageBitmap(bitmap);
                LogUtil.logI("size:" + bitmap.getByteCount());
                viewHolder.commtent_video.addView(mImageView, params);
            } else if (requestCode == REQUESR_CODE_RECORD) {
                String filePath;
                try {
                    Uri uri = data.getData();
                    Cursor cursor = getActivity().getContentResolver()
                            .query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                    filePath = cursor.getString(index);
                    LogUtil.logI(filePath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Utils.copyFile(filePath, audio.getAbsolutePath());
                if (audio != null && audio.length() != 0) {
                    ImageView mImageView = new ImageView(getContext());
                    mImageView.setImageResource(R.drawable.audio);
                    viewHolder.commtent_audio.addView(mImageView, params);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startAction(Intent intent, File file, int type) {
        if (currentapiVersion > 22) {
            if (type == REQUESR_CODE_TAKEPHOTO || type == REQUESR_CODE_VIDEO) {
                LogUtil.logI("int:" + ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA));
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    jumpToAction(intent, file, type);
                } else {
                    creatSnackBar(R.string.mis_error_no_permission_camera);
                }
            } else {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    jumpToAction(intent, file, type);
                } else {
                    creatSnackBar(R.string.mis_error_no_permission_audio);
                }
            }
        } else {
            jumpToAction(intent, file, type);
        }
    }

    private void jumpToAction(Intent intent, File file, int type) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            LogUtil.logI("file:" + (file != null && file.exists()));
            if (file != null) {
             /*获取当前系统的android版本号*/
                LogUtil.logI("currentapiVersion====>" + currentapiVersion);
                if (currentapiVersion < 24) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, type);
                } else {
                    ContentValues contentValues = new ContentValues(1);
                    if (type == REQUESR_CODE_TAKEPHOTO) {
                        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                        uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    } else if (type == REQUESR_CODE_VIDEO) {
                        contentValues.put(MediaStore.Video.Media.DATA, file.getAbsolutePath());
                        uri = getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                    } else if (type == REQUESR_CODE_RECORD) {
                        contentValues.put(MediaStore.Audio.Media.DATA, file.getAbsolutePath());
                        uri = getContext().getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, type);
                }
            } else {
                Toast.makeText(getActivity(), R.string.mis_error_image_not_exist, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.mis_msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    private void creatSnackBar(int text) {
        Snackbar.make(getView(), text, Snackbar.LENGTH_SHORT).setAction("设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                getActivity().startActivity(intent);
            }
        }).show();
    }

}
