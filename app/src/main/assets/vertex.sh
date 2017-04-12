uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; 	 //变换矩阵(包括平移、旋转、缩放)
attribute vec3 aPosition;  //顶点位置
attribute vec2 aTexCoor;    //顶点纹理坐标
uniform vec3 uLightLocation;  //光源位置
uniform vec3 uCamera;		//摄像机位置

varying float currZ;				//用于传递给片元着色器的Z坐标
varying vec2 vTextureCoord;     //用于传递给片元着色器的变量
varying vec4 vAmbient;			//用于传递给片元着色器的环境光最终强度
varying vec4 vDiffuse;          //用于传递给片元着色器的散射光最终强度
varying vec4 vSpecular;			//用于传递给片元着色器的镜面光最终强度

void pointLight(inout vec4 diffuse, inout vec4 specular,in vec3 lightLocation,in vec4 lightDiffuse,in vec4 lightSpecular){
  vec3 normalTarget=normalize(aPosition)+aPosition;	 //计算变换后的法向量
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal);//对法向量规格化
   //计算从表面点到摄像机的向量
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);
  //vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz); //计算从表面点到光源位置的向量vp
  vec3 vp= normalize(lightLocation); //规格化定向光方向向量
  //vp=normalize(vp);
  vec3 halfVector=normalize(vp+eye);	//求视线与光线的半向量
  float shininess=90.0;				//粗糙度，越小越光滑
  float nDotViewPosition=max(0.0,dot(newNormal,vp));  //求法向量与vp的点积与0的最大值
  diffuse=lightDiffuse*nDotViewPosition;        //计算散射光的最终强度
  float nDotViewHalfVector=dot(newNormal,halfVector);	//法线与半向量的点积
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//镜面反射光强度因子
  specular=lightSpecular*powerFactor;    			//计算镜面光的最终强度
  }
void main()
{
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器
   vec4 diffuseTemp,specularTemp;   //用来接收三个通道最终强度的变量
   pointLight(diffuseTemp,specularTemp,uLightLocation,vec4(0.8,0.8,0.8,1.0),vec4(0.6,0.6,0.6,1.0));
   vAmbient= vec4(0.25,0.25,0.25,1.0);    //将环境光最终强度传给片元着色器
   vDiffuse=diffuseTemp;    //将散射光最终强度传给片元着色器
   vSpecular=specularTemp; 		//将镜面光最终强度传给片元着色器
   currZ=aPosition.z;		//将顶点的Y坐标传递给片元着色器
}