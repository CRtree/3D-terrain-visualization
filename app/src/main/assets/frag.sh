precision mediump float;
uniform sampler2D sTexture;//������������

varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
varying vec4 vAmbient;//���մӶ�����ɫ�������Ļ��������
varying vec4 vDiffuse;//���մӶ�����ɫ��������ɢ������
varying vec4 vSpecular;//���մӶ�����ɫ�������ľ��淴������

void main()                         
{
   //������ɫ
    vec4 finalColor=texture2D(sTexture, vTextureCoord);
    //����ƬԪ��ɫֵ
    gl_FragColor=finalColor*vAmbient + finalColor*vDiffuse;
}