attribute vec4 attribute_vertex_coord;// 顶点坐标
//attribute vec4 aColor;// 顶点颜色
//varying vec4 vColor;// 顶点颜色插值
attribute vec2 attribute_texture_coord;// 纹理原始坐标
varying vec2 varying_texture_coord;// 纹理插值坐标
uniform mat4 uniform_mvp_matrix;// 变换矩阵

void main() {
    varying_texture_coord = attribute_texture_coord;
    gl_Position = uniform_mvp_matrix * attribute_vertex_coord;
}