#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif
//varying vec4 vColor;// 顶点颜色插值
uniform int uniform_debug;// 调试开关
varying vec2 varying_texture_coord;// 纹理插值坐标
uniform sampler2D uniform_texture;// 纹理（RGB）
uniform sampler2D uniform_texture_alpha;// 纹理（Alpha）
uniform vec4 uniform_color_filter;// 颜色滤镜
void main() {
    vec4 color = texture2D(uniform_texture, varying_texture_coord);
    if (uniform_debug == 0) {
        //color.w = texture2D(uniform_texture_alpha, varying_texture_coord).r;
    } else {
        if (color.w <= 0.2) {
            color = vec4(0.5, 0.5, 0.5, 1);// 灰色背景，用于显示GLView的尺寸
        }
    }
    gl_FragColor = uniform_color_filter * color;
}