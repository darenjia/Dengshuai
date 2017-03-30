package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.BuildConfig;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.bokun.bkjcb.on_siteinspection.View.ImagePreview;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by BKJCB on 2017/3/23.
 */

public class CheckItemFragment extends BaseFragment implements View.OnClickListener {

    private ViewHolder viewHolder;
    private String path;
    private File image;
    private int REQUESR_CODE_TAKEPHOTO = 2;
    private int REQUESR_CODE_CROPPHOTO = 1;
    private int REQUESR_CODE_RECORD = 3;
    private int REQUESR_CODE_VIDEO = 4;
    private String vdoName;
    private String imagePath = "picture/";
    private String videoPath = "video/";
    private String audioPath = "audio/";
    private Uri uri = null;
    private File video;
    private File audio;
    private Map<String, String> result;
    private AlertDialog remarkDialiog;

    @Override
    public View initView() {
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
        }
        return viewHolder.view;
    }

    @Override
    public void initData() {
        String content = getArguments().getString("content");
        result = (Map<String, String>) getArguments().get("result");
        viewHolder.camera_view.setOnClickListener(this);
        viewHolder.video_view.setOnClickListener(this);
        viewHolder.audio_view.setOnClickListener(this);
        viewHolder.btn_remark.setOnClickListener(this);
        viewHolder.commtent_pic.setOnClickListener(this);
        viewHolder.txt_content.setText(content);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            path = Environment.getExternalStorageDirectory() + "/Bokun";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            creatSnackBar(R.string.mis_error_no_permission_sdcard);
        }
        viewHolder.mRdaioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtil.logI("id:" + checkedId);
            }
        });
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
                image = new File(path, picName);
                creatFile(image);
                LogUtil.logI(image.getAbsolutePath());
                /*intent.putExtra("output", Uri.fromFile(sdcardTempFile));
                startActivityForResult(intent, 100);*/
                startAction(intent, image, REQUESR_CODE_TAKEPHOTO);
                break;
            case R.id.check_content_btn_audio:
                intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                String mp3Name = audioPath + (new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()))
                        + ".amr";
                audio = new File(path, mp3Name);
                creatFile(audio);
                startAction(intent, audio, REQUESR_CODE_RECORD);
                break;
            case R.id.check_content_btn_video:
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                // 保存录像到指定的路径
                vdoName = videoPath + (new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()))
                        + ".mp4";
                video = new File(path, vdoName);
                creatFile(video);
                startAction(intent, video, REQUESR_CODE_VIDEO);
                break;
            case R.id.check_content_btn_remark:
            case R.id.check_content_info:
                creatEditCommentDialog();
                break;
            case R.id.check_content_pic:
                creatImageDialog();
                break;
            case R.id.check_content_audio:
                break;
            case R.id.check_content_video:
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
        private HorizontalScrollView commtent_pic;
        private HorizontalScrollView commtent_video;
        private HorizontalScrollView commtent_audio;
        private View view;
        private TextView image_title;
        private TextView video_title;
        private TextView audio_title;
        public ViewHolder() {
            LogUtil.logI("new ViewHolder");
            initWidgets();
        }

        private void initWidgets() {
            view = LayoutInflater.from(getContext()).inflate(R.layout.check_detial_view, null, false);
            txt_content = (TextView) findView(R.id.txt_content);
            camera_view = (ImageView) findView(R.id.check_content_btn_camera);
            comment_word = (TextView) findView(R.id.check_content_info);
            commtent_audio = (HorizontalScrollView) findView(R.id.check_content_audio);
            commtent_pic = (HorizontalScrollView) findView(R.id.check_content_pic);
            commtent_video = (HorizontalScrollView) findView(R.id.check_content_video);
            video_view = (ImageView) findView(R.id.check_content_btn_video);
            audio_view = (ImageView) findView(R.id.check_content_btn_audio);
            btn_remark = (Button) findView(R.id.check_content_btn_remark);
            mRdaioGroup = (RadioGroup) findView(R.id.check_result);
            image_title = (TextView) view.findViewById(R.id.check_content_pic_title);
            audio_title = (TextView) view.findViewById(R.id.check_content_ado_title);
            video_title = (TextView) view.findViewById(R.id.check_content_vdo_title);

        }

        private View findView(int id) {
            return view.findViewById(id);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView mImageView = new ImageView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESR_CODE_CROPPHOTO) {
//                Bundle bundle = data.getExtras();
//                LogUtil.logI("bitmap" + (bundle.get("data") == null));
//                Bitmap bitmap = (Bitmap) bundle.get("data");
                Bitmap bitmap = Utils.compressBitmap(image.getAbsolutePath(), 160, 160);
                LogUtil.logI("size:" + bitmap.getByteCount());
                mImageView.setImageBitmap(bitmap);
                viewHolder.image_title.setVisibility(View.VISIBLE);
                viewHolder.commtent_pic.addView(mImageView, params);
                if (result.get("imagePath")==null){
                    result.put("imagePath",image.getAbsolutePath());
                }else {
                    String path = result.get("imagePath")+","+image.getAbsolutePath();
                    result.put("imagePath",path);
                }
            } else if (requestCode == REQUESR_CODE_TAKEPHOTO) {
                LogUtil.logI("拍完进入剪裁");
                Intent intent = new Intent("com.android.camera.action.CROP");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", image);
                    intent.setDataAndType(contentUri, "image/*");
                } else {
                    intent.setDataAndType(Uri.fromFile(image), "image/*");
                }
                intent.putExtra("output", uri);
                intent.putExtra("crop", "true");
//                intent.putExtra("aspectX", 1);// 裁剪框比例
//                intent.putExtra("aspectY", 1);
//                intent.putExtra("outputX", 150);// 输出图片大小
//                intent.putExtra("outputY", 150);
                intent.putExtra("return-data", false);
                startActivityForResult(intent, REQUESR_CODE_CROPPHOTO);
            } else if (requestCode == REQUESR_CODE_VIDEO) {
//                Bitmap bitmap = (Bitmap) bundle.get("data");
//                Bitmap bitmap = getCutBitmap(sdcardTempFile.getAbsolutePath());
                Bitmap bitmap = Utils.getVideoThumbnail(video.getAbsolutePath());
//                bitmap = Utils.compressBitmap(bitmap);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, 160, 160);
                mImageView.setImageBitmap(bitmap);
                LogUtil.logI("size:" + bitmap.getByteCount());
                viewHolder.video_title.setVisibility(View.VISIBLE);
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
                Utils.copyFile(filePath, audio);
                if (audio != null && audio.length() != 0) {
                    mImageView.setImageResource(R.drawable.audio);
                    viewHolder.audio_title.setVisibility(View.VISIBLE);
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

    private void creatEditCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.remark_view, null);
        final EditText editText = (EditText) view.findViewById(R.id.check_result_remark);
        editText.setText(viewHolder.comment_word.getText());
        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText.getText().toString();
                if (remarkDialiog != null) {
                    remarkDialiog.dismiss();
                }
                result.put("remark", value);
                viewHolder.comment_word.setText(value);
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        remarkDialiog = builder.create();
        remarkDialiog.show();
        remarkDialiog.setCanceledOnTouchOutside(false);
    }

    private void creatImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = new ImagePreview(getContext()).getImagePreviewView(result.get("imagePath"));
        builder.setView(view);
        builder.setCancelable(true);
        remarkDialiog = builder.create();
        remarkDialiog.show();
        remarkDialiog.setCanceledOnTouchOutside(false);
    }

    private void creatSnackBar(int text) {
        Snackbar.make(getView(), text, Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                getActivity().startActivity(intent);
            }
        }).show();
    }

    private void creatFile(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
