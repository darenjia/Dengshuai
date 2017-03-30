package com.bokun.bkjcb.on_siteinspection.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bokun.bkjcb.on_siteinspection.R;
import com.bokun.bkjcb.on_siteinspection.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/30.
 */

public class ImagePreview {
    private Context context;
    private List<Bitmap> list;

    public ImagePreview(Context context) {
        this.context = context;
    }

    public View getImagePreviewView(String imagePath) {
        list = new ArrayList<>();
        getBitmap(imagePath);
        View view = View.inflate(context, R.layout.image_pre_view, null);
        final TextView textView = (TextView) view.findViewById(R.id.preview_title);
        textView.setText("1/" + list.size());
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.preview_viewpager);
        viewPager.setAdapter(new PagerAdapter() {
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
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText((position+1) + "/" + list.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    private void getBitmap(String imagePath) {
        String[] paths = imagePath.split(",");
        for (int i = 0; i < paths.length; i++) {
            list.add(Utils.compressBitmap(paths[i]));
        }
    }
}
