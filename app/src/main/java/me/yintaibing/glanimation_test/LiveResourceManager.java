package me.yintaibing.glanimation_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播模块文件资源管理
 *
 * @author yintaibing
 */
public class LiveResourceManager  {
    // /sdcard/package/file/gift
    public static final String GIFT_DIR = "sdcard/com.zhenai.android/file/gift";


    /**
     * 获取bitmap资源
     *
     * @param context         Context对象
     * @param giftID          礼物ID，资源所在父目录名称（不是完整路径）
     * @param fileName        资源文件名
     * @param resourceDensity 图片本身是按什么density制作的
     * @param defWidth        缺失此资源时，返回缺省bitmap的宽
     * @param defHeight       缺失此资源时，返回缺省bitmap的高
     * @return Bitmap
     */
    public static Bitmap getGiftBitmap(Context context, int giftID, String fileName,
                                       int resourceDensity, int defWidth, int defHeight) {
        Bitmap bitmap;
        try {
            BitmapFactory.Options opt = getDecodeOpt(context, resourceDensity);
            bitmap = BitmapFactory.decodeFile(GIFT_DIR + File.separator + giftID +
                    File.separator + fileName, opt);
        } catch (OutOfMemoryError e) {
            bitmap = null;
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(defWidth, defHeight, Bitmap.Config.ARGB_8888);
            // 有资源缺失，检查一遍资源列表
        }
        return bitmap;
    }

    public static Bitmap getGiftBitmap(Context context, int giftID, String fileName,
                                       int resourceDensity) {
        return getGiftBitmap(context, giftID, fileName, resourceDensity, 1, 1);
    }

    /**
     * 获取一整个bitmap列表资源
     *
     * @param context         Context对象
     * @param giftID          礼物ID，资源所在父目录名称（不是完整路径）
     * @param fileNames       资源文件名
     * @param resourceDensity 图片本身是按什么density制作的
     * @param fillLostBitmap  当某些图片丢失，是否需要填补它们
     */
    public static List<Bitmap> getGiftBitmapList(Context context, int giftID, String[] fileNames,
                                                 int resourceDensity, boolean fillLostBitmap) {
        BitmapFactory.Options opt = getDecodeOpt(context, resourceDensity);

        StringBuilder builder = new StringBuilder(GIFT_DIR);
        builder.append(File.separator)
                .append(giftID)
                .append(File.separator);
        int dirPathLength = builder.length();

        int size = fileNames.length;
        List<Bitmap> bitmaps = new ArrayList<>(size);
        boolean lost = false;
        for (int i = 0; i < size; i++) {
            try {
                if (i == 0) {
                    builder.append(fileNames[i]);
                } else {
                    builder.replace(dirPathLength, builder.length(), fileNames[i]);
                }
                Bitmap bitmap = BitmapFactory.decodeFile(builder.toString(), opt);
                if (bitmap == null) {
                    lost = true;
                    if (!fillLostBitmap) {
                        bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                    }
                }
                bitmaps.add(bitmap);
            } catch (OutOfMemoryError e) {
                lost = true;
                break;
            }
        }

        // 自动补帧。如果有个别图片丢失，则用它最邻近的图补上
        if (lost) {
            if (fillLostBitmap) {
                Bitmap fillBy = null;
                int firstBitmapIndex = -1;
                for (int i = 0; i < size; i++) {
                    Bitmap bitmap = bitmaps.get(i);
                    if (bitmap != null) {
                        // 找到了一张bitmap，它后面的null将使用它填补
                        fillBy = bitmap;
                        if (firstBitmapIndex >= 0) {
                            // 0~firstBitmapIndex都为null，循环填补
                            for (int j = 0; j <= firstBitmapIndex; j++) {
                                bitmaps.set(j, fillBy);
                            }
                            firstBitmapIndex = -1;
                        }
                    } else if (fillBy != null) {
                        // 遇到了null，且之前已找到了bitmap，用fillBy填补
                        bitmaps.set(i, fillBy);
                    } else {
                        // 遇到了null，且i之前都是null
                        firstBitmapIndex = i;
                    }
                }
                if (firstBitmapIndex == size - 1) {
                    Bitmap def = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                    for (int i = 0; i < size; i++) {
                        bitmaps.set(i, def);
                    }
                }
            }
        }

        return bitmaps;
    }

    public static List<Bitmap> getGiftBitmapList(Context context, int giftID, String[] fileNames,
                                                 int resourceDensity) {
        return getGiftBitmapList(context, giftID, fileNames, resourceDensity, true);
    }

    /**
     * 获取解析bitmap的参数
     *
     * @param context   Context
     * @param inDensity 原图的密度
     */
    private static BitmapFactory.Options getDecodeOpt(Context context, int inDensity) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inDensity = inDensity;
        opt.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        opt.inScaled = true;
        return opt;
    }
}
