package me.yintaibing.glanimation;

import android.opengl.GLES20;

public class GLTexture {
    public String path;
    public int pixelCount;
    public int id = -1;
    public int index;

    public int id_alpha = -1;
    public int index_alpha;

    public GLTexture(String path) {
        this.path = path;
    }

    public boolean isValid() {
        return index >= GLES20.GL_TEXTURE0 && id >= 0;
    }
}
