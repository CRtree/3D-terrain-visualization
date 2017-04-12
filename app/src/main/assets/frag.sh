precision mediump float;
uniform sampler2D sTexture;//   �����������ݣ��ݵأ�

uniform sampler2D sTextureRock;		//�����������ݣ���ʯ��
uniform float landStartY;			//����������ʼY����
uniform float landYSpan;			//����������

varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
varying vec4 vAmbient;//���մӶ�����ɫ�������Ļ��������
varying vec4 vDiffuse;//���մӶ�����ɫ��������ɢ������
varying vec4 vSpecular;//���մӶ�����ɫ�������ľ��淴������
varying float currZ; //���մӶ�����ɫ��������Z����

void main()                         
{
 vec4 gColor=texture2D(sTexture, vTextureCoord); 	//�Ӳ�Ƥ�����в�������ɫ
   vec4 rColor=texture2D(sTextureRock, vTextureCoord); 	//����ʯ�����в�������ɫ
   vec4 finalColor;
   if(currZ<landStartY){
	  finalColor=gColor;	//��ƬԪY����С�ڹ���������ʼY����ʱ���ò�Ƥ����
   }else if(currZ>landStartY+landYSpan){
	  finalColor=rColor;	//��ƬԪY������ڹ���������ʼY����ӿ��ʱ������ʯ����
   }else{
       float currYRatio=(currZ-landStartY)/landYSpan;	//������ʯ������ռ�İٷֱ�
       finalColor= currYRatio*rColor+(1.0- currYRatio)*gColor;//����ʯ����Ƥ������ɫ���������
   }

    //����ƬԪ��ɫֵ
    gl_FragColor=finalColor*vAmbient + finalColor*vDiffuse ;
}