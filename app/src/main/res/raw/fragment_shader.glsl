#ifdef GL_GRAFMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif
varying vec4 vColor;// 顶点颜色插值
varying vec2 vCoord;// 纹理插值坐标
uniform sampler2D uSampler2D;// 纹理对象
uniform vec4 uAlpha;
void main() {
    gl_FragColor = uAlpha * texture2D(uSampler2D, vCoord);
    //gl_FragColor = vColor * texture2D(uSampler2D, vCoord);
}