package android.zuoxiao.com.demo2.util;

import android.content.res.Resources;
import android.zuoxiao.com.demo2.Point;
import android.zuoxiao.com.demo2.activity.Main2Activity;
import android.zuoxiao.com.demo2.arithmetic.SimplificationPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuoxiao on 2017/4/5.
 */

public class DataLoad {
   public static float scaleX = 0;
   public static float scaleY = 0;
   public static float scaleZ = 0;

static List<Float> loadFromASC(String fname, Resources r){
    //public static List<Float> loadFromASC(){
        int ncols = 0;//列数
        int nrows = 0;//行数
        int cellsize =0;//像素间距
        String nodatavalue="";//无值标志数据
        float xllcorner=0;//第一个像素的x坐标
        float yllcorner=0;//第一个像素的y坐标

        //File file = new File("/Users/zuoxiao/Downloads/s/7.asc");
        List<Float> dataSource = new ArrayList<>();
        try {
            int row=0;
            InputStream inputStream = r.getAssets().open(fname);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            //BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while((s = br.readLine())!=null){
                if(s!="") {
                    String[] temp = s.split(" ");
                    switch (temp[0]) {
                        case "ncols":
                            ncols = Integer.parseInt(temp[1]);
                            break;
                        case "nrows":
                            nrows = Integer.parseInt(temp[1]);
                            break;
                        case "cellsize":
                            cellsize = Integer.parseInt(temp[1]);
                            break;
                        case "nodata_value":
                            nodatavalue = temp[1];
                            break;
                        case "xllcorner":
                            xllcorner = Float.parseFloat(temp[1]);
                            break;
                        case "yllcorner":
                            yllcorner = Float.parseFloat(temp[1]);
                            break;
                        default:
                            if (row % Main2Activity.rowSet == 0) {
                                for (int i = 0; i < temp.length; i = i + Main2Activity.colSet) {
                                    if (!temp[i].equals(nodatavalue)) {
                                        float tempValue;

                                        float data = 4f / ncols * i;
                                        dataSource.add(data);
                                        tempValue = (data > 0 ? data : -data);
                                        scaleX = (scaleX > tempValue ? scaleX : tempValue);

                                        data = 4f - 4f / nrows * row;
                                        dataSource.add(data);
                                        tempValue = (data > 0 ? data : -data);
                                        scaleY = (scaleY > tempValue ? scaleY : tempValue);

                                        data = Float.parseFloat(temp[i]);
                                        dataSource.add(data);
                                        tempValue = (data > 0 ? data : -data);
                                        scaleZ = (scaleZ > tempValue ? scaleZ : tempValue);
                                    }

                                }
                            }
                            row++;//这个语句放在这里，简直妙哉！

                            break;
                    }
                }
            }
            br.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    System.out.println("离散取点，总共取了："+dataSource.size()/3+"个点");
        return dataSource;
    }

//获得裁剪后的数组，数字里不含无效值
private static Point[][] getPictures(float[][] pictures){
    int colstart=0 ,colend=0,rowstart=0,rowend=0;
    int colsize = pictures[0].length;
    int rowsize = pictures.length;
    for (int i = 0; i < rowsize; i++) {
        if (pictures[i][colsize/2]!=-9999.0f && rowstart == 0){
            rowstart = i;
        }
        if (rowstart!=0 && pictures[i][colsize/2]==-9999.0f){
            rowend = i-1;
            break;
        }
    }
    for (int i = 0; i < colsize; i++) {
        if (pictures[rowsize/2][i]!=-9999.0f && colstart == 0){
            colstart = i;
        }
        if (colstart!=0 && pictures[rowsize/2][i]==-9999.0f){
            colend = i-1;
            break;
        }
    }
    Point[][] cutPictures = new Point[rowend-rowstart+1][colend-colstart+1];
    int x =0;
    for (int i = rowstart; i <= rowend; i++) {
        int y = 0;
        for (int j = colstart; j <=colend ; j++) {
            float tempValue;
            float data;
            cutPictures[x][y] = new Point();

            data = 6f*j/(colend-colstart+1)-5.5f;
            cutPictures[x][y].x = data;
            tempValue = (data>0?data:-data);
            scaleX = (scaleX>tempValue?scaleX:tempValue);

            data = 5.8f-6f*i/(rowend-rowstart+1);
            cutPictures[x][y].y = data;
            tempValue = (data>0?data:-data);
            scaleY = (scaleY>tempValue?scaleY:tempValue);

            cutPictures[x][y].z = pictures[i][j];
            tempValue = (pictures[i][j]>0?pictures[i][j]:-pictures[i][j]);
            scaleZ = (scaleZ>tempValue?scaleZ:tempValue);
            y++;
        }
        x++;
    }
    for (Point[] cutPicture : cutPictures) {
        for (int j = 0; j < cutPictures[0].length; j++) {
            //缩放z轴坐标，这也是没办法才这样干
            cutPicture[j].z = 1.8f * cutPicture[j].z / scaleZ;
        }
    }
    return cutPictures;
}

//获得包括无效点的所有数据（这个方法用于散点精简算法）
private static float[][] loadFromASC2(String fname, Resources r){
//static float[][] loadFromASC2(){
        int ncols = 0;//列数
        int nrows;//行数
        int cellsize =0;//像素间距
        String nodatavalue="";//无值标志数据
        float xllcorner=0;//第一个像素的x坐标
        float yllcorner=0;//第一个像素的y坐标

        File file = new File("/Users/zuoxiao/常用资料/毕业设计/资料/示例数据/dem包/10.asc");
        float[][] pictures = new float[1][1];
        int x=0;
        try {
            InputStream inputStream = r.getAssets().open(fname);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            //BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while((s = br.readLine())!=null){
                if(s!="") {
                    try {
                        String[] temp = s.split(" ");
                        switch (temp[0]) {
                            case "ncols":
                                ncols = Integer.parseInt(temp[1]);
                                break;
                            case "nrows":
                                nrows = Integer.parseInt(temp[1]);
                                pictures = new float[nrows][ncols];
                                break;
                            case "cellsize":
                                cellsize = Integer.parseInt(temp[1]);
                                break;
                            case "nodata_value":
                                nodatavalue = temp[1];
                                break;
                            case "xllcorner":
                                xllcorner = Float.parseFloat(temp[1]);
                                break;
                            case "yllcorner":
                                yllcorner = Float.parseFloat(temp[1]);
                                break;
                            default:
                                int y=0;
                                for (String aTemp : temp) {
                                    float data = Float.parseFloat(aTemp);
                                    pictures[x][y] = data;
                                    y++;
                                }
                                x++;
                                break;
                        }
                    }
                    catch (NumberFormatException e){
                        System.out.println("asc文件格式错误，空格未处理！！");
                    }

                }
            }
            br.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pictures;
    }

//使用点云精简算法
    public static List<Float> doSimpleCloud(String fname, Resources r){
        //获得初始点集
        float[][] picture1 = loadFromASC2(fname,r);
        //float[][] picture1 = loadFromASC2();
        //对初始点集进行筛选，去杂
        Point[][] pictureCut = getPictures(picture1);
        //求取法向量数组
        Point[][] normalArrays = SimplificationPoint.getNormalArrays(pictureCut);
        //求取曲率数组，判断地形地势
        float[][] curvatureArray = SimplificationPoint.getCurvatureArray(normalArrays);
        //精简点云
        List<Point> list= SimplificationPoint.pickCurvature(curvatureArray,pictureCut);

        List<Float> floatList = new ArrayList<>(list.size()*3);

        for (int i = 0; i < list.size(); i++) {
            Point pointTemp = list.get(i);
            floatList.add(pointTemp.x);
            floatList.add(pointTemp.y);
            floatList.add(pointTemp.z);
        }
        System.out.println("最终数组大小为"+floatList.size()+"个");
        return floatList;
    }
}
