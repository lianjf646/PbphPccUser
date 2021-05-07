package com.youth.banner.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Created by Administrator on 2017/7/28.
 */

public class GlideImageLoader extends ImageLoader{
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        try {
            Glide.with(context).load(path).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
