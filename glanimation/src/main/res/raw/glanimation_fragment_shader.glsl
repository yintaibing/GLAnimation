#ifdef GL_GRAFMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif
//varying vec4 vColor;// 顶点颜色插值
varying vec2 varying_texture_coord;// 纹理插值坐标
uniform sampler2D uniform_texture;// 纹理对象
uniform vec4 uniform_multiply_color;
void main() {
    gl_FragColor = uniform_multiply_color * texture2D(uniform_texture, varying_texture_coord);
}