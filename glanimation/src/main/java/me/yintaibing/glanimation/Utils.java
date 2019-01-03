package me.yintaibing.glanimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public class Utils {
    private static final String TAG = "Utils";

    public static final int BYTES_OF_SHORT = 2;
    public static final int BYTES_OF_FLOAT = 4;

    public static ShortBuffer toShortBuffer(short[] data) {
        ShortBuffer sb = ByteBuffer.allocateDirect(data.length * BYTES_OF_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        sb.put(data).position(0);
        return sb;
    }

    public static FloatBuffer toFloatBuffer(float[] data) {
        FloatBuffer fb = ByteBuffer.allocateDirect(data.length * BYTES_OF_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        fb.put(data).position(0);
        return fb;
    }

    public static String readRaw(Context context, int rawResId) {
        InputStream in = context.getResources().openRawResource(rawResId);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            in.close();
            return baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void loadTextures(List<GLView> views) {
        if (views == null || views.isEmpty()) {
            return;
        }

        int count = 0;
        GLTexture texture;
        BitmapFactory.Options options = new BitmapFactory.Options();

        // just decode bounds to find largest bitmap
        options.inJustDecodeBounds = true;
        GLTexture largestTexture = null;
        for (GLView view : views) {
            texture = view.getTexture();
            BitmapFactory.decodeFile(texture.path, options);
            texture.pixelCount = options.outWidth * options.outHeight;
            if (largestTexture == null ||
                    texture.pixelCount > largestTexture.pixelCount) {
                largestTexture = texture;
                Log.e(TAG, "largestTexture=" + largestTexture.path);
            }
            count++;
        }

        // load bitmaps and create textures
        options.inJustDecodeBounds = false;
        int[] textureIds = new int[count];
        int index = 0;
        GLES20.glGenTextures(count, textureIds, 0);

        Bitmap bitmap = null;
        if (largestTexture != null) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[index]);
            bitmap = BitmapFactory.decodeFile(largestTexture.path, options);
            createTextureFromBitmap(bitmap);
            largestTexture.id = textureIds[index];
            largestTexture.index = GLES20.GL_TEXTURE0 + index;
            Log.e(TAG, "created texture id=" + largestTexture.id
                    + " index=" + largestTexture.index
                    + " path=" + largestTexture.path);
            index++;
        }
        options.inBitmap = bitmap;
        for (GLView view : views) {
            texture = view.getTexture();
            if (texture != largestTexture) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[index]);
                createTextureFromBitmap(BitmapFactory.decodeFile(texture.path, options));
                texture.id = textureIds[index];
                texture.index = GLES20.GL_TEXTURE0 + index;
                Log.e(TAG, "created texture id=" + texture.id
                        + " index=" + texture.index
                        + " path=" + texture.path);
                index++;
            }
        }
    }

    public static void createTextureFromBitmap(Bitmap bitmap) {
//        int[] textures = new int[1];
//        GLES20.glGenTextures(textures.length, textures, 0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
//        GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

//        bitmap.recycle();

//        return textures[0];
    }
}
