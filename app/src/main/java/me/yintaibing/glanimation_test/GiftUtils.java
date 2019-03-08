package me.yintaibing.glanimation_test;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播礼物模块工具类
 *
 * @author yintaibing
 * @date 2017/12/11
 */
public class GiftUtils {
    public static List<FrameParams> loadFrameParamJsonAsset(Context context, String fileName) {
        String json = readAsset(context, fileName);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        final Gson gson = new Gson();
        int size;
        if (array != null && (size = array.size()) > 0) {
            final List<FrameParams> list = new ArrayList<>(size);
            FrameParams fp;
            for (JsonElement element : array) {
                fp = gson.fromJson(element, FrameParams.class);
                list.add(fp);
            }
            return list;
        }
        return null;
    }

    /**
     * 创建自定义帧动画Drawable
     *
     * @param resources Resources对象
     * @param bitmaps   图片
     * @param oneShot   是否仅播放一次
     * @param params    自定义帧动画Drawable所需参数（若需要的是CustomAnimationDrawable）
     * @return 自定义帧动画Drawable
     */
    public static CustomAnimationDrawable createCustomAnimationDrawable(Resources resources,
                                                                        List<Bitmap> bitmaps,
                                                                        boolean oneShot,
                                                                        FrameParams params) {
        CustomAnimationDrawable drawable = new CustomAnimationDrawable(params);
        addFrames(drawable, resources, bitmaps, params.frameDuration);
        drawable.setOneShot(oneShot);
        return drawable;
    }

    public static void addFrames(AnimationDrawable drawable, Resources resources, List<Bitmap> bitmaps,
                                 int frameDuration) {
        int size;
        if (bitmaps != null && (size = bitmaps.size()) > 0) {
            for (int i = 0; i < size; i++) {
                drawable.addFrame(new BitmapDrawable(resources, bitmaps.get(i)), frameDuration);
            }
        }
    }

    public static String readAsset(Context context, String fileName) {
        try {
            return readFromStream(context.getResources().getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readFromStream(InputStream in) {
        if (in == null) {
            return null;
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
            return new String(out.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
