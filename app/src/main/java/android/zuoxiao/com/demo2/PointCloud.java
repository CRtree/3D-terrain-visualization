package android.zuoxiao.com.demo2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * Created by zuoxiao on 2017/4/12.
 */

public class PointCloud {

    float xAngle=0;//绕x轴旋转的角度
    float yAngle=0;//绕x轴旋转的角度

    private int mProgram;//自定义渲染管线程序id
    private int muMVPMatrixHandle;//总变换矩阵引用id
    private int maPositionHandle; //顶点位置属性引用id
    private int gcolorHandle;//颜色属性引用id

    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器

    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    private int vCount=0;

    public PointCloud(MyView2 mv) {
        //初始化顶点坐标与着色数据
        initVertexData(mv);
        //初始化shader
        initShader(mv);
    }

    private void initVertexData(MyView2 mv)
    {
        //顶点坐标数据的初始化
        List<Float> list = DataLoad.loadFromASC("7.asc",mv.getResources());
        vCount=list.size()/3;
        float[] vertices =  new float[list.size()];
        for (int i = 0; i < list.size()-2; i=i+3) {
            vertices[i] = 6f*list.get(i)/DataLoad.scaleX-4.5f;
            vertices[i+1] = 6f*list.get(i+1)/DataLoad.scaleY-4.5f;
            vertices[i+2] = 1.5f*list.get(i+2)/DataLoad.scaleZ;
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

    }

    //初始化shader
    private void initShader(MyView2 mv)
    {
//加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssertsFile("vertex2.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssertsFile("frag2.sh", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        gcolorHandle = GLES20.glGetUniformLocation(mProgram, "gcolor");

    }

    public void drawSelf()
    {
        MatrixState.rotate(xAngle, 1, 0, 0);//绕X轴转动
        MatrixState.rotate(yAngle, 0, 0, 1);//绕Y轴转动

        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //给总变换矩阵传值
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, mVertexBuffer);

        GLES20.glUniform4f(gcolorHandle, 0f, 0f, 0f, 1.0f);
        //允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);

        GLES20.glDrawArrays(GLES20.GL_POINTS,0, vCount);

    }
}
