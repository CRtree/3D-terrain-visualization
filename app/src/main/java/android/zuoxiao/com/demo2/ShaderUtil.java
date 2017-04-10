package android.zuoxiao.com.demo2;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zuoxiao on 2017/4/3.
 */

public class ShaderUtil {
    //加载指定着色器
    public static int loadShader(int shaderType, String source){
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0){
            GLES20.glShaderSource(shader,source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0)
            {
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }
    //创建着色器程序的方法
    public static int createProgram(String vertexSource,String fragmentSource){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0){
            return 0;
        }
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0)
        {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if(program != 0){
            //向程序中加入顶点着色器
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            //向程序中加入片元着色器
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            //链接程序
            GLES20.glLinkProgram(program);
            //存放链接成功program数量的数组
            int[] linkStatus = new int[1];
            //获取program的链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE)
            {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }
    public static void checkGlError(String op){
        int error;
        while((error = GLES20.glGetError()) !=GLES20.GL_NO_ERROR){
            Log.e("ES20_ERROR", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
    //从sh脚本中加载着色器内容的方法
    public static String loadFromAssertsFile(String fname, Resources r){
        String result = null;
        try {
            InputStream in = r.getAssets().open(fname);
            int ch=0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch=in.read())!=-1){
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            baos.close();
            in.close();
            result=new String(buff,"UTF-8");
            result = result.replaceAll("\\r\\n","\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
