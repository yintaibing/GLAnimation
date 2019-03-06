package me.yintaibing.glanimation;

import android.opengl.GLES20;

public class GLTexture {
    public String filePath;// 文件路径，如果采用ETC纹理文件，则是非alpha部分纹理文件的路径
    public int id = -1;
    public int index = -1;

    public String filePathEtcAlpha;// 当采用ETC纹理时，它是alpha部分纹理文件的路径
    public int idEtcAlpha = -1;
    public int indexEtcAlpha = -1;

    public int pixelCount;

    public GLTexture(String filePath) {
        this.filePath = filePath;
    }

    public GLTexture(String filePath, String filePathEtcAlpha) {
        this(filePath);
        this.filePathEtcAlpha = filePathEtcAlpha;
    }

    public boolean isValid() {
        return isValidInternal(id, index);
    }

    public boolean isValidEtcAlpha() {
        return isValidInternal(idEtcAlpha, indexEtcAlpha);
    }

    private boolean isValidInternal(int id, int index) {
        return id >= 0 && index >= GLES20.GL_TEXTURE0;
    }
}
