package com.bokun.bkjcb.on_siteinspection.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.BuildConfig;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.Domain.SerializableList;
import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;
import com.bokun.bkjcb.on_siteinspection.View.ImagePreview;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private int CAMERA_IMAGE = 0;
    private int VIDEO_IMAGE = 1;
    private int AUDIO_IMAGE = 2;
    private String vdoName;
    private String imagePath = "picture/";
    private String videoPath = "video/";
    private String audioPath = "audio/";
    private Uri uri = null;
    private File video;
    private File audio;
    private CheckResult result;
    private AlertDialog remarkDialiog;
    private ArrayList<String> imagePaths;
    private ArrayList<String> videoPaths;
    private ArrayList<String> audioPaths;
    private ArrayList<CheckResult> results;
    private int identifier;
    private String comment;
    private int fragmentId;

    @Override
    public View initView() {
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
        }
        return viewHolder.view;
    }

    @Override
    public void initData() {
        initViewData();
        checkSDPremission();
        initListener();
    }

    private void initViewData() {
        String content = getArguments().getString("content");
        fragmentId = getArguments().getInt("id");
        identifier = getArguments().getInt("identifier");
        results = ((SerializableList) getArguments().get("results")).getList();
        if (results.size() < fragmentId) {
            result = results.get(fragmentId);
        } else {
            result = new CheckResult();
            result.setIdentifier(identifier);
            results.add(fragmentId, result);
        }

        int flag = result.getResult();
        if (flag != 2) {
            if (flag == 0) {
                viewHolder.mRdaioGroup.check(R.id.check_result_radio_1);
            } else {
                viewHolder.mRdaioGroup.check(R.id.check_result_radio_2);
            }
        } else {
            viewHolder.mRdaioGroup.check(R.id.check_result_radio_3);
        }
        if (result.getComment() != null) {
            comment = result.getComment();
            viewHolder.comment_word.setText(comment);
        }
        if (result.getImageUrls() != null) {
            initImages(result.getImageUrls(), CAMERA_IMAGE);
        } else {
            imagePaths = new ArrayList<>();
            result.setImageUrls(imagePaths);
        }
        if (result.getVideoUrls() != null) {
            initImages(result.getVideoUrls(), VIDEO_IMAGE);
        } else {
            videoPaths = new ArrayList<>();
            result.setVideoUrls(videoPaths);
        }
        if (result.getAudioUrls() != null) {
            initImages(result.getAudioUrls(), AUDIO_IMAGE);
        } else {
            audioPaths = new ArrayList<>();
            result.setAudioUrls(audioPaths);
        }
        viewHolder.txt_content.setText(content);
    }

    private void initListener() {
        viewHolder.camera_view.setOnClickListener(this);
        viewHolder.video_view.setOnClickListener(this);
        viewHolder.audio_view.setOnClickListener(this);
        viewHolder.btn_remark.setOnClickListener(this);
        viewHolder.commtent_pic.setOnClickListener(this);

        viewHolder.mRdaioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtil.logI("id:" + checkedId);
                int flag = 2;
                if (checkedId == R.id.check_result_radio_1) {
                    flag = 0;
                } else if (checkedId == R.id.check_result_radio_2) {
                    flag = 1;
                }
                result.setResult(flag);
            }
        });
    }

    private void checkSDPremission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            path = Environment.getExternalStorageDirectory() + "/Bokun";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            creatSnackBar(R.string.mis_error_no_permission_sdcard);
        }
    }

    private void initImages(List<String> list, int type) {
        for (int i = 0; i < list.size(); i++) {
            setImage(list.get(i), type);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.check_content_btn_camera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String picName = imagePath + getDateTime()
                        + ".jpg";
                image = new File(path, picName);
                creatFile(image);
                startAction(intent, image, REQUESR_CODE_TAKEPHOTO);
                break;
            case R.id.check_content_btn_audio:
                intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                String mp3Name = audioPath + getDateTime()
                        + ".amr";
                audio = new File(path, mp3Name);
                creatFile(audio);
                startAction(intent, audio, REQUESR_CODE_RECORD);
                break;
            case R.id.check_content_btn_video:
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                // 保存录像到指定的路径
                vdoName = videoPath + getDateTime()
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
                creatImageDialog(viewHolder.commtent_pic, imagePaths, "imagePaths");
                break;
            case R.id.check_content_audio:
                break;
            case R.id.check_content_video:
                creatImageDialog(viewHolder.commtent_video, videoPaths, "videoPaths");
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
            commtent_audio = (LinearLayout) findView(R.id.check_content_audio);
            commtent_pic = (LinearLayout) findView(R.id.check_content_pic);
            commtent_video = (LinearLayout) findView(R.id.check_content_video);
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

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESR_CODE_CROPPHOTO) {
//                Bundle bundle = data.getExtras();
//                LogUtil.logI("bitmap" + (bundle.get("data") == null));
//                Bitmap bitmap = (Bitmap) bundle.get("data");
                setImage(image.getAbsolutePath(), CAMERA_IMAGE);
                imagePaths.add(image.getAbsolutePath());
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
                setImage(video.getAbsolutePath(), VIDEO_IMAGE);
                videoPaths.add(video.getAbsolutePath());
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
                    setImage(null, AUDIO_IMAGE);
                    audioPaths.add(audio.getAbsolutePath());
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

    private void setImage(String path, int type) {
        LinearLayout layout = null;
        ImageView mImageView = new ImageView(getContext());
        Bitmap bitmap = null;
        if (type == CAMERA_IMAGE) {
            layout = viewHolder.commtent_pic;
            bitmap = Utils.compressBitmap(path, 160, 160);
        } else if (type == VIDEO_IMAGE) {
            layout = viewHolder.commtent_video;
            bitmap = Utils.getVideoThumbnail(video.getAbsolutePath());
//                bitmap = Utils.compressBitmap(bitmap);
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 160, 160);
        } else {
            layout = viewHolder.commtent_audio;
            BitmapFactory.decodeResource(getResources(), R.drawable.audio);
        }
        LogUtil.logI("size:" + bitmap.getByteCount());
        mImageView.setImageBitmap(bitmap);
//        viewHolder.image_title.setVisibility(View.VISIBLE);
        layout.addView(mImageView, getLayoutParams());
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
                result.setComment(value);
                viewHolder.comment_word.setText(value);
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        remarkDialiog = builder.create();
        remarkDialiog.show();
        remarkDialiog.setCanceledOnTouchOutside(false);
    }

    private void creatImageDialog(final LinearLayout layout, final List<String> paths, String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final ImagePreview preview = new ImagePreview(getContext());
        preview.setListener(new ImagePreview.ButtonClickListener() {

            @Override
            public void onDelete() {
                layout.removeViewAt(preview.getPosition());
                paths.remove(preview.getPosition());
            }

            @Override
            public void onCancel() {
                if (remarkDialiog != null) {
                    remarkDialiog.dismiss();
                }
            }
        });
        View view;
        if (type.equals("imagePaths")) {
            view = preview.getImagePreviewView(result.getImageUrls());
        } else {
            view = preview.getImagePreviewView(result.getAudioUrls());
        }
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

    private String getDateTime() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
    }

    @NonNull
    private LinearLayout.LayoutParams getLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        return params;
    }

    public void setFragmentId(int id) {
        this.fragmentId = id;
    }

}
