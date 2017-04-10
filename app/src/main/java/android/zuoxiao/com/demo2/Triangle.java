package android.zuoxiao.com.demo2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * Created by zuoxiao on 2017/4/4.
 */

public class Triangle {

    private int mProgram;//自定义渲染管线程序id
    private int muMMatrixHandle;//位置、旋转变换矩阵引用
    private int muMVPMatrixHandle;//总变换矩阵引用id
    private int maPositionHandle; //顶点位置属性引用id
    int maColorHandle; //顶点颜色属性引用id
    int uLightHandle;//光源属性引用id
    int uCameraHandle;//视角属性引用id

    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mTexCoorBuffer;//纹理坐标数据缓冲
    private int vCount=0;
    float xAngle=0;//绕x轴旋转的角度
    float yAngle=0;//绕x轴旋转的角度
    List<Float> list;//生成的三角网数据（按顺序排列）
    float scaleX = 0;
    float scaleY = 0;
    float scaleZ = 0;
    public Triangle(MyView mv)
    {
        //初始化顶点坐标与着色数据
        initVertexData(mv);
        //初始化shader
        initShader(mv);
    }
    public void initVertexData(MyView mv)
    {
        //顶点坐标数据的初始化
        List<Float> sourcelist = DataLoad.loadFromASC("7.asc",mv.getResources());
        list = Delaunay.doDelaunayFromGit(sourcelist);
        //list =DataLoad.loadFromASC("7.asc",mv.getResources());
        vCount=list.size()/3;
        float[] vertices =  new float[list.size()];
        for (int i = 0; i < list.size()-2; i=i+3) {
            float tempValue =0;
            vertices[i] = list.get(i);
            tempValue = (list.get(i)>0?list.get(i):-list.get(i));
            scaleX = (scaleX>tempValue?scaleX:tempValue);

            vertices[i+1] = list.get(i+1);
            tempValue = (list.get(i+1)>0?list.get(i+1):-list.get(i+1));
            scaleY = (scaleY>tempValue?scaleY:tempValue);

            vertices[i+2] = list.get(i+2);
            tempValue = (list.get(i+2)>0?list.get(i+2):-list.get(i+2));
            scaleZ = (scaleZ>tempValue?scaleZ:tempValue);
        }
        for (int i = 0; i < vertices.length-2; i=i+3) {
            vertices[i] = 6f*vertices[i]/scaleX-4.5f;
            vertices[i+1] = 6f*vertices[i+1]/scaleY-4.5f;
            vertices[i+2] = vertices[i+2]/scaleZ;
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        //顶点纹理坐标数据的初始化
        float texCoor[]=new float[list.size()*2];//顶点颜色值数组，每个顶点4个色彩值RGBA
        for (int i = 0; i < texCoor.length-5; i=i+6) {
            texCoor[i] = 0.5f;texCoor[i+1] = 0f;
            texCoor[i+2] = 0f;texCoor[i+3] = 1f;
            texCoor[i+4] = 1f;texCoor[i+5] = 1f;
        }
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置

    }
    //初始化shader
    public void initShader(MyView mv)
    {
        //加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssertsFile("vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssertsFile("frag.sh", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用id
        maColorHandle= GLES20.glGetUniformLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取变换矩阵引用id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        //获取光源向量引用id
        uLightHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");

        uCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
    }

    public void drawSelf(int texId)
    {
        MatrixState.rotate(xAngle, 1, 0, 0);//绕X轴转动
        MatrixState.rotate(yAngle, 0, 0, 1);//绕Y轴转动

        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);

        //将光源位置传入着色器程序
        GLES20.glUniform3fv(uLightHandle,1,MatrixState.lightPositionFB);
        //将摄像机位置传入着色器程序
        GLES20.glUniform3fv(uCameraHandle,1,MatrixState.cameraFB);
        //给总变换矩阵传值
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将位置、旋转变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, mVertexBuffer);
        //为画笔指定顶点纹理坐标数据
        GLES20.glVertexAttribPointer(maColorHandle, 2, GLES20.GL_FLOAT, false, 2*4, mTexCoorBuffer);
        //允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);
        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

        //GLES20.glDrawArrays(GLES20.GL_POINTS,0, vCount);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0, vCount);
    }
}
