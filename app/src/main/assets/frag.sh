precision mediump float;
uniform sampler2D sTexture;//纹理内容数据

varying vec2 vTextureCoord; //接收从顶点着色器过来的参数
varying vec4 vAmbient;//接收从顶点着色器过来的环境光分量
varying vec4 vDiffuse;//接收从顶点着色器过来的散射光分量
varying vec4 vSpecular;//接收从顶点着色器过来的镜面反射光分量

void main()                         
{
   //最终颜色
    vec4 finalColor=texture2D(sTexture, vTextureCoord);
    //给此片元颜色值
    gl_FragColor=finalColor*vAmbient + finalColor*vDiffuse;
}