uniform mat4 u_MVPMatrix;
attribute vec4 a_Position;

void main()
{
gl_Position = u_MVPMatrix * a_Position;
//gl_Position =  a_Position;
    gl_PointSize = 3.0;
} 