precision mediump float;
uniform sampler2D sTexture;//   纹理内容数据（草地）

uniform sampler2D sTextureRock;		//纹理内容数据（岩石）
uniform float landStartY;			//过程纹理起始Y坐标
uniform float landYSpan;			//过程纹理跨度

varying vec2 vTextureCoord; //接收从顶点着色器过来的参数
varying vec4 vAmbient;//接收从顶点着色器过来的环境光分量
varying vec4 vDiffuse;//接收从顶点着色器过来的散射光分量
varying vec4 vSpecular;//接收从顶点着色器过来的镜面反射光分量
varying float currZ; //接收从顶点着色器过来的Z坐标

void main()                         
{
 vec4 gColor=texture2D(sTexture, vTextureCoord); 	//从草皮纹理中采样出颜色
   vec4 rColor=texture2D(sTextureRock, vTextureCoord); 	//从岩石纹理中采样出颜色
   vec4 finalColor;
   if(currZ<landStartY){
	  finalColor=gColor;	//当片元Y坐标小于过程纹理起始Y坐标时采用草皮纹理
   }else if(currZ>landStartY+landYSpan){
	  finalColor=rColor;	//当片元Y坐标大于过程纹理起始Y坐标加跨度时采用岩石纹理
   }else{
       float currYRatio=(currZ-landStartY)/landYSpan;	//计算岩石纹理所占的百分比
       finalColor= currYRatio*rColor+(1.0- currYRatio)*gColor;//将岩石、草皮纹理颜色按比例混合
   }

    //给此片元颜色值
    gl_FragColor=finalColor*vAmbient + finalColor*vDiffuse ;
}