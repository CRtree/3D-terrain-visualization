package android.zuoxiao.com.demo2.util;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by zuoxiao on 2017/4/9.
 */

public class MatrixState {
    private static float[] mProjMatrix = new float[16];//4x4矩阵 投影用
    private static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵
    private static float[] currMatrix;//具体物体的移动旋转矩阵，旋转、平移
    private static float[] lightLocation=new float[]{0,0,0};//定位光光源位置
    public static FloatBuffer lightPositionFB;
    public static FloatBuffer cameraFB;
    //保护变换矩阵的栈
    private static float[][] mStack=new float[10][16];
    private static int stackTop=-1;

    //获取不变换初始矩阵
    public static void setInitMatrix() {
        currMatrix=new float[16];
        Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }
    //设置沿xyz轴移动
    public static void translate(float x,float y,float z) {
        Matrix.translateM(currMatrix, 0, x, y, z);
    }
    //设置绕xyz轴移动
    public static void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }
    //保护变换矩阵
    public static void pushMatrix(){
        stackTop++;
        System.arraycopy(currMatrix, 0, mStack[stackTop], 0, 16);
    }
    //恢复变换矩阵
    public static void popMatrix(){
        System.arraycopy(mStack[stackTop], 0, currMatrix, 0, 16);
        stackTop--;
    }

    //设置摄像机
    private static ByteBuffer llbb= ByteBuffer.allocateDirect(3*4);
    private static float[] cameraLocation=new float[3];//摄像机位置
    public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz)
    {
        Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
        cameraLocation[0]=cx;
        cameraLocation[1]=cy;
        cameraLocation[2]=cz;

        llbb.clear();
        llbb.order(ByteOrder.nativeOrder());//设置字节顺序
        cameraFB=llbb.asFloatBuffer();
        cameraFB.put(cameraLocation);
        cameraFB.position(0);
    }

    //设置透视投影参数
    public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far)
    {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //设置灯光位置的方法
    private static ByteBuffer llbbL = ByteBuffer.allocateDirect(3*4);
    public static void setLightLocation(float x, float y, float z)
    {
        llbbL.clear();
        lightLocation[0]=x;lightLocation[1]=y;lightLocation[2]=z;
        llbbL.order(ByteOrder.nativeOrder());//设置字节顺序
        lightPositionFB=llbbL.asFloatBuffer();
        lightPositionFB.put(lightLocation);
        lightPositionFB.position(0);
    }

    //获取具体物体的变换矩阵
    public static float[] getMMatrix()
    {
        return currMatrix;
    }

    //获取具体物体的总变换矩阵
    private static float[] mMVPMatrix=new float[16];
    public static float[] getFinalMatrix()
    {
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
}
