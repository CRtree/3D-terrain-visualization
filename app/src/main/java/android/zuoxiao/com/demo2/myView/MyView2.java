package android.zuoxiao.com.demo2.myView;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.zuoxiao.com.demo2.util.MatrixState;
import android.zuoxiao.com.demo2.paint.PointCloud;
import android.zuoxiao.com.demo2.activity.Main2Activity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zuoxiao
 * 2017/4/12.
 */

public class MyView2 extends GLSurfaceView{
    PointCloud pointCloud;
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    SceneRenderer2 mRenderer;

    public MyView2(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer2();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算触控笔Y位移
                float dx = x - mPreviousX;//计算触控笔X位移
                pointCloud.yAngle+= dx * TOUCH_SCALE_FACTOR;
                pointCloud.xAngle += dy * TOUCH_SCALE_FACTOR;
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }

    private class SceneRenderer2 implements GLSurfaceView.Renderer{

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(0f,0f,0f,1.0f);
            pointCloud = new PointCloud(MyView2.this);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            float ratio = (float) width / height;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 20);
            // 调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0, 0f, Main2Activity.cameraSet,  0f, 0f, 0f,  0f, 1.0f, 0.0f);

            MatrixState.setInitMatrix();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //保护现场
            MatrixState.pushMatrix();

            //绘制三角形对
            MatrixState.pushMatrix();
            pointCloud.drawSelf();
            MatrixState.popMatrix();

            //恢复现场
            MatrixState.popMatrix();
        }
    }
}
