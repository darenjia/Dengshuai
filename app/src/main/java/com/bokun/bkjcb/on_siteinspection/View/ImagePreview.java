package com.bokun.bkjcb.on_siteinspection.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/30.
 */

public class ImagePreview implements View.OnClickListener {
    private Context context;
    private List<Bitmap> list;
    private ViewPager viewPager;
    private ButtonClickListener listener;
    private PagerAdapter adapter;
    private EditText fileName;
    private List<String> files;
    private TextView textView;
    private Button btnDelete;
    private Button btnCancel;

    public interface ButtonClickListener {
        void onDelete();

        void onCancel();
    }

    public ImagePreview(Context context) {
        this.context = context;
    }

    public View getImagePreviewView(List<String> imagePath) {
        this.list = new ArrayList<>();
        this.files = imagePath;
        getBitmap(imagePath);
        View view = initView();
        initViewDate();
        initListener();
        return view;
    }

    private void initViewDate() {
        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(list.get(position));
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
        viewPager.setAdapter(adapter);
        textView.setText("1/" + list.size());
        fileName.setText(getFileName(0));
    }

    @NonNull
    private View initView() {
        View view = View.inflate(context, R.layout.image_pre_view, null);
        textView = (TextView) view.findViewById(R.id.preview_title);
        fileName = (EditText) view.findViewById(R.id.preview_name);
        btnDelete = (Button) view.findViewById(R.id.preview_delete);
        btnCancel = (Button) view.findViewById(R.id.preview_cancel);
        viewPager = (ViewPager) view.findViewById(R.id.preview_viewpager);
        return view;
    }

    private void initListener() {
        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        fileName.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText((position + 1) + "/" + list.size());
                fileName.setText(getFileName(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOnClickListener(this);
        fileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                s.toString();
                LogUtil.logI("newName:"+s.toString());
            }
        });
        fileName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v==fileName&&!hasFocus){
                    LogUtil.logI("filename lose focus");
                }
            }
        });
    }

    private void getBitmap(List<String> imagePath) {
        Bitmap bitmap = null;
        for (int i = 0; i < imagePath.size(); i++) {
            try {
                bitmap = Utils.compressBitmap(imagePath.get(i));
            } catch (Exception e) {
                bitmap = ThumbnailUtils.createVideoThumbnail(imagePath.get(i), MediaStore.Images.Thumbnails.MICRO_KIND);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_error);
                }
            }
            list.add(bitmap);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.preview_delete) {
            LogUtil.logI("size" + list.size());
            list.remove(viewPager.getCurrentItem());
            adapter.notifyDataSetChanged();
            listener.onDelete();
            if (list.size() == 0) {
                listener.onCancel();
            }
        } else if (v.getId() == R.id.preview_cancel){
            listener.onCancel();
        } else if (v.getId() == R.id.preview_name){
            LogUtil.logI("click name!");
            Toast.makeText(context,"click name!",Toast.LENGTH_SHORT).show();
            fileName.setEnabled(true);
        }else {
            Toast.makeText(context,"click!",Toast.LENGTH_SHORT).show();
        }
    }

    public int getPosition() {
        return viewPager.getCurrentItem();
    }

    public void setListener(ButtonClickListener listener) {
        this.listener = listener;
    }

    public String getFileName(int position){
        String path = files.get(position);
        return path.substring(path.lastIndexOf("/")+1);
    }
}
