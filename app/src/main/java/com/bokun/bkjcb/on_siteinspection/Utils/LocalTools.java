package com.bokun.bkjcb.on_siteinspection.Utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/20.
 */

public class LocalTools {

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /*
    * List转成字符串
    * */
    public static String changeToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                builder.append(list.get(i));
                if (i != (list.size() - 1)) {
                    builder.append(",");
                }
            }
        } else {
            return null;
        }
        return builder.toString();
    }

    public static ArrayList<String> changeToList(String string) {
        ArrayList<String> list = new ArrayList<>();
        if (string == null) {
            return null;
        }
        String[] strings = string.split(",");
        return list;
    }
}
