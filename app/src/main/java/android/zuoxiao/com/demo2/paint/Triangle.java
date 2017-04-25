package android.zuoxiao.com.demo2.paint;

import android.opengl.GLES20;
import android.zuoxiao.com.demo2.arithmetic.Delaunay;
import android.zuoxiao.com.demo2.util.MatrixState;
import android.zuoxiao.com.demo2.util.ShaderUtil;
import android.zuoxiao.com.demo2.myView.MyView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import io.github.jdiemke.triangulation.Triangle2D;

/**
 * Created by zuoxiao on 2017/4/4.
 */

public class Triangle {

    private int mProgram;//自定义渲染管线程序id
    private int muMMatrixHandle;//位置、旋转变换矩阵引用
    private int muMVPMatrixHandle;//总变换矩阵引用id
    private int maPositionHandle; //顶点位置属性引用id
    private int maColorHandle; //顶点颜色属性引用id
    private int uLightHandle;//光源属性引用id
    private int uCameraHandle;//视角属性引用id

    private FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    private FloatBuffer mTexCoorBuffer;//纹理坐标数据缓冲
    private int vCount=0;
    public float xAngle=0;//绕x轴旋转的角度
    public float yAngle=0;//绕x轴旋转的角度

    //新增
    private int sTextureGrassHandle;//草地的id

    public Triangle(MyView mv)
    {
        //初始化顶点坐标与着色数据
        initVertexData(mv);
        //初始化shader
        initShader(mv);
    }
    private void initVertexData(MyView mv)
    {
        //顶点坐标数据的初始化
        //List<Float> sourcelist = DataLoad.loadFromASC(Main2Activity.fliename,mv.getResources());
        List<Float> sourcelist = PointCloud.PointCloudSourcelist;
        List<Triangle2D> trianglelist = Triangle1.trianglelist;
        List<Float> list = Delaunay.addHight(trianglelist,sourcelist);
        vCount=list.size()/3;
        float[] vertices =  new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            vertices[i] = list.get(i);
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        //顶点纹理坐标数据的初始化
        float texCoor[]=new float[list.size()*2];
        int j = 0;
        for (int i = 0; i < vertices.length; i=i+3) {
            texCoor[j] = (vertices[i]+6f)/12f;
            texCoor[j+1] = (vertices[i+1]+6f)/12f;
            j=j+2;
        }
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置

    }
    //初始化shader
    private void initShader(MyView mv)
    {
        //加载顶点着色器的脚本内容
        String mVertexShader = ShaderUtil.loadFromAssertsFile("vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        String mFragmentShader = ShaderUtil.loadFromAssertsFile("frag.sh", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用id
        maColorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取变换矩阵引用id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        //获取光源向量引用id
        uLightHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");

        uCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");

        //草地
        sTextureGrassHandle=GLES20.glGetUniformLocation(mProgram, "sTexture");
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

        GLES20.glUniform1i(sTextureGrassHandle, 0);//使用0号纹理


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0, vCount);
    }
}
