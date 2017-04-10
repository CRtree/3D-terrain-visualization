package android.zuoxiao.com.demo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zuoxiao on 2017/4/4.
 */

public class MyView extends GLSurfaceView {
    Triangle tle;
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    int textureId;//系统分配的纹理id

    SceneRenderer mRenderer;

    public MyView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        mRenderer=new SceneRenderer();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
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
                tle.yAngle+= dx * TOUCH_SCALE_FACTOR;
                tle.xAngle += dy * TOUCH_SCALE_FACTOR;
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer{

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(0,0,0,1.0f);
            //创建三角形对对象
            tle=new Triangle(MyView.this);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //初始化纹理
            try {
                initTexture();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //关闭背面剪裁
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
             //设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            float ratio = (float) width / height;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
            // 调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0, -2f, 4.5f,  0f, 0f, 0f,  0f, 1.0f, 0.0f);

            MatrixState.setInitMatrix();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
             //清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //初始化光源位置
            MatrixState.setLightLocation(0f, 2f, 10f);
            //保护现场
            MatrixState.pushMatrix();

            //绘制三角形对
            MatrixState.pushMatrix();
            tle.drawSelf(textureId);
            MatrixState.popMatrix();

            //恢复现场
            MatrixState.popMatrix();
        }
    }

    public void initTexture() throws IOException//textureId
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);  //产生的纹理id的数量 纹理id的数组 偏移量
        textureId=textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        //通过输入流加载图片===============begin===================
        InputStream is = this.getResources().getAssets().open("th.jpeg");
        Bitmap bitmapTmp = BitmapFactory.decodeStream(is);
        is.close();
        //实际加载纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0 );  //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
    }
}
