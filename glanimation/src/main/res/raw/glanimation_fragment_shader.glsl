#ifdef GL_GRAFMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif
//varying vec4 vColor;// 顶点颜色插值
varying vec2 varying_texture_coord;// 纹理插值坐标
//varying vec2 varying_texture_coord_alpha;
uniform sampler2D uniform_texture;// 纹理对象
uniform vec4 uniform_multiply_color;
void main() {
    vec4 c = uniform_multiply_color * texture2D(uniform_texture, varying_texture_coord);
    //vec4 alpha = texture2D(uniform_texture, varying_texture_coord_alpha);
    //c.w = alpha.r;
    if(c.w<=0.5)c=vec4(0.5,0.5,0.5,1);
    gl_FragColor = c;
}