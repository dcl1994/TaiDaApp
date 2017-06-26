package utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.siyann.taidaapp.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by szjdj on 2017-03-21.
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         * 使用glide加载网络图片
         */
        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.plugin_nopicture)    //占位图
                .error(R.drawable.plugin_pictures)          //错误图
                .into(imageView);
    }
}
