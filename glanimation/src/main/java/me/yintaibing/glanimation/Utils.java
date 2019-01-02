package me.yintaibing.glanimation;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {
    public static final int BYTES_OF_SHORT = 2;
    public static final int BYTES_OF_FLOAT = 4;

    public static Buffer toShortBuffer(short[] data) {
        return ByteBuffer.allocateDirect(data.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(data)
                .position(0);
    }

    public static Buffer toFloatBuffer(float[] data) {
        return ByteBuffer.allocateDirect(data.length * 2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(data)
                .position(0);
    }
}
