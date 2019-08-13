precision mediump float;
varying vec4 vColor;// 顶点颜色插值
varying vec2 vTexPosition;//纹理插值坐标
uniform sampler2D uTex;//纹理对象
void main() {
    gl_FragColor = vColor * texture2D(uTex, vTexPosition);
}