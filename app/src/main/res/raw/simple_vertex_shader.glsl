attribute vec4 aPosition;//顶点坐标
attribute vec4 aColor;// 顶点颜色
varying vec4 vColor;// 顶点颜色插值
attribute vec2 aTexPosition;//纹理顶点坐标
varying vec2 vTexPosition;//纹理插值坐标
void main() {
    vColor = aColor;
    vTexPosition = aTexPosition;
    gl_Position = aPosition;
}