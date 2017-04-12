uniform mat4 uMVPMatrix; //�ܱ任����
uniform mat4 uMMatrix; 	 //�任����(����ƽ�ơ���ת������)
attribute vec3 aPosition;  //����λ��
attribute vec2 aTexCoor;    //������������
uniform vec3 uLightLocation;  //��Դλ��
uniform vec3 uCamera;		//�����λ��

varying float currZ;				//���ڴ��ݸ�ƬԪ��ɫ����Z����
varying vec2 vTextureCoord;     //���ڴ��ݸ�ƬԪ��ɫ���ı���
varying vec4 vAmbient;			//���ڴ��ݸ�ƬԪ��ɫ���Ļ���������ǿ��
varying vec4 vDiffuse;          //���ڴ��ݸ�ƬԪ��ɫ����ɢ�������ǿ��
varying vec4 vSpecular;			//���ڴ��ݸ�ƬԪ��ɫ���ľ��������ǿ��

void pointLight(inout vec4 diffuse, inout vec4 specular,in vec3 lightLocation,in vec4 lightDiffuse,in vec4 lightSpecular){
  vec3 normalTarget=normalize(aPosition)+aPosition;	 //����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal);//�Է��������
   //����ӱ���㵽�����������
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);
  //vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz); //����ӱ���㵽��Դλ�õ�����vp
  vec3 vp= normalize(lightLocation); //��񻯶���ⷽ������
  //vp=normalize(vp);
  vec3 halfVector=normalize(vp+eye);	//����������ߵİ�����
  float shininess=90.0;				//�ֲڶȣ�ԽСԽ�⻬
  float nDotViewPosition=max(0.0,dot(newNormal,vp));  //��������vp�ĵ����0�����ֵ
  diffuse=lightDiffuse*nDotViewPosition;        //����ɢ��������ǿ��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//������������ĵ��
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//���淴���ǿ������
  specular=lightSpecular*powerFactor;    			//���㾵��������ǿ��
  }
void main()
{
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
   vec4 diffuseTemp,specularTemp;   //������������ͨ������ǿ�ȵı���
   pointLight(diffuseTemp,specularTemp,uLightLocation,vec4(0.8,0.8,0.8,1.0),vec4(0.6,0.6,0.6,1.0));
   vAmbient= vec4(0.25,0.25,0.25,1.0);    //������������ǿ�ȴ���ƬԪ��ɫ��
   vDiffuse=diffuseTemp;    //��ɢ�������ǿ�ȴ���ƬԪ��ɫ��
   vSpecular=specularTemp; 		//�����������ǿ�ȴ���ƬԪ��ɫ��
   currZ=aPosition.z;		//�������Y���괫�ݸ�ƬԪ��ɫ��
}