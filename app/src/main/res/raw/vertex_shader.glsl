attribute vec4 aPosition;// 顶点坐标
attribute vec4 aColor;// 顶点颜色
varying vec4 vColor;// 顶点颜色插值
attribute vec2 aCoord;// 纹理原始坐标
varying vec2 vCoord;// 纹理插值坐标
uniform mat4 uMVP;// 变换矩阵

void main() {
    vColor = aColor;
    vCoord = aCoord;
    gl_Position = uMVP * aPosition;
}