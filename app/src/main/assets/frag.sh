precision mediump float;
uniform sampler2D sTexture;         //   �����������ݣ��ݵأ�

varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
varying vec4 vAmbient;//���մӶ�����ɫ�������Ļ��������
varying vec4 vDiffuse;//���մӶ�����ɫ��������ɢ������
varying vec4 vSpecular;//���մӶ�����ɫ�������ľ��淴������

void main()                         
{
 vec4 gColor=texture2D(sTexture, vTextureCoord); 	//�Ӳ�Ƥ�����в�������ɫ
 vec4 finalColor = gColor;

    //����ƬԪ��ɫֵ
 gl_FragColor=finalColor*vAmbient + finalColor*vDiffuse ;
}