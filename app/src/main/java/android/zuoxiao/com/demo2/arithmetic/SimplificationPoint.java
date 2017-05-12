package android.zuoxiao.com.demo2.arithmetic;

import android.zuoxiao.com.demo2.Point;
import android.zuoxiao.com.demo2.activity.Main2Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Jama.Matrix;

/**
 * Created by zuoxiao
 * 基于点法向量判断的点云精简算法
 * 2017/4/21.
 */

public class SimplificationPoint {

    //获得散点集合对应点的法向量集合
    public static Point[][] getNormalArrays(Point[][] sourceArrays){
        //循环遍历二维数组
        //遍历到某一点
        //提取包围这个点的所有周边的点，计算其协方差矩阵的最小特征根对应的最小特征向量，即为该点的法向量
        //按原来的位置存到法向量二维数组中
        //结束循环
        Point[][] normalArrays = new Point[sourceArrays.length][sourceArrays[0].length];
        for (int i = 0; i < sourceArrays.length; i++) {
            for (int j = 0; j < sourceArrays[0].length; j++) {
                Point[] tempPoints = new Point[4];
                if (i != (sourceArrays.length-1) && j!= (sourceArrays[0].length-1)){
                    tempPoints[0] = sourceArrays[i][j];
                    tempPoints[1] = sourceArrays[i][j+1];
                    tempPoints[2] = sourceArrays[i+1][j];
                    tempPoints[3] =  sourceArrays[i+1][j+1];
                }
                else if (j == (sourceArrays[0].length-1)&&i != (sourceArrays.length-1) ){
                    tempPoints[0] = sourceArrays[i][j];
                    tempPoints[1] = sourceArrays[i][j-1];
                    tempPoints[2] = sourceArrays[i+1][j];
                    tempPoints[3] =  sourceArrays[i+1][j-1];
                }else if(j!= (sourceArrays[0].length-1)&&i == (sourceArrays.length-1)){
                    tempPoints[0] = sourceArrays[i][j];
                    tempPoints[1] = sourceArrays[i][j+1];
                    tempPoints[2] = sourceArrays[i-1][j];
                    tempPoints[3] =  sourceArrays[i-1][j+1];
                }else {
                    tempPoints[0] = sourceArrays[i][j];
                    tempPoints[1] = sourceArrays[i][j-1];
                    tempPoints[2] = sourceArrays[i-1][j];
                    tempPoints[3] =  sourceArrays[i-1][j-1];
                }
                //计算协方差矩阵
                double[][] tempArrays = getCovarianceMatrix(tempPoints);
                Point tempNormal = getNormal(tempArrays);
                normalArrays[i][j]=tempNormal;
            }
        }
        return normalArrays;
    }

    //获得由法向量数组计算得到的曲率数组
    public static float[][] getCurvatureArray(Point[][] normalArrays){
        //循环遍历法向量二维数组
        //遍历到某一点
        //提取包围这个点的所有周边的点法向量，求该点与周边点的法向量的点积的均值
        //存到曲率二维数组中
        //结束循环
        float[][] curvatureArrays = new float[normalArrays.length][normalArrays[0].length];
        for (int i = 0; i < normalArrays.length; i++) {
            for (int j = 0; j < normalArrays[0].length; j++) {
                Point[] tempPoints;
                if ( i!=0 &&i != (normalArrays.length-1) && j!=0 && j!= (normalArrays[0].length-1)){
                    tempPoints = new Point[9];
                    tempPoints[0] = normalArrays[i][j];     tempPoints[1] = normalArrays[i-1][j-1];
                    tempPoints[2] = normalArrays[i-1][j];   tempPoints[3] = normalArrays[i-1][j+1];
                    tempPoints[4] = normalArrays[i][j-1];   tempPoints[5] = normalArrays[i][j+1];
                    tempPoints[6] =  normalArrays[i+1][j-1];tempPoints[7] =  normalArrays[i+1][j];
                    tempPoints[8] =  normalArrays[i+1][j+1];
                }else if ((i == 0 && j!= (normalArrays[0].length-1)) || (j==0 && i!=(normalArrays.length-1))){
                    tempPoints = new Point[4];
                    tempPoints[0] = normalArrays[i][j];
                    tempPoints[1] = normalArrays[i][j+1];
                    tempPoints[2] = normalArrays[i+1][j];
                    tempPoints[3] =  normalArrays[i+1][j+1];
                }
                else if (j == (normalArrays[0].length-1)&&i != (normalArrays.length-1) ){
                    tempPoints = new Point[4];
                    tempPoints[0] = normalArrays[i][j];
                    tempPoints[1] = normalArrays[i][j-1];
                    tempPoints[2] = normalArrays[i+1][j];
                    tempPoints[3] =  normalArrays[i+1][j-1];
                }else if(j!= (normalArrays[0].length-1)&&i == (normalArrays.length-1)){
                    tempPoints = new Point[4];
                    tempPoints[0] = normalArrays[i][j];
                    tempPoints[1] = normalArrays[i][j+1];
                    tempPoints[2] = normalArrays[i-1][j];
                    tempPoints[3] =  normalArrays[i-1][j+1];
                }else {
                    tempPoints = new Point[4];
                    tempPoints[0] = normalArrays[i][j];
                    tempPoints[1] = normalArrays[i][j-1];
                    tempPoints[2] = normalArrays[i-1][j];
                    tempPoints[3] =  normalArrays[i-1][j-1];
                }
                float curvature = getCurvature(tempPoints);
                curvatureArrays[i][j] = curvature;
            }
        }
        return curvatureArrays;
    }

    //对曲率按大小范围进行分类,然后对各组进行点的精简，主要去除平坦地区的点
    public static List<Point> pickCurvature(float[][] curvatureArrays,Point[][] sourceArrays){
        //根据曲率来分组，大概分为6组，数值大代表平坦的地形，数值小代表地形陡峭
        //对各组进行点的精简，主要去除平坦地区的点
        List<Point> list1 = new ArrayList<>();
        List<Point> list2 = new ArrayList<>();
        List<Point> list3 = new ArrayList<>();
        List<Point> list4 = new ArrayList<>();
        List<Point> list5 = new ArrayList<>();
        for (int i = 0; i < curvatureArrays.length; i++) {
            for (int j = 0; j < curvatureArrays[i].length; j++) {
                float aCurvatureArray = curvatureArrays[i][j];
                Point aPoint = new Point();
                aPoint.x = i;
                aPoint.y = j;
                if (aCurvatureArray > 0 && aCurvatureArray <= 0.936f) {
                    list1.add(aPoint);
                } else if (aCurvatureArray > 0.936f && aCurvatureArray <= 0.968f) {
                    list2.add(aPoint);
                } else if (aCurvatureArray > 0.968f && aCurvatureArray <= 0.984f) {
                    list3.add(aPoint);
                } else if (aCurvatureArray > 0.984f && aCurvatureArray <= 0.992f) {
                    list4.add(aPoint);
                } else {
                    list5.add(aPoint);
                }
            }
        }

        int totalNumber = curvatureArrays.length*curvatureArrays[0].length;

        System.out.println("总共有"+totalNumber+"个数据");
        System.out.println("分类后，各范围分别有"+list1.size()+","+list2.size()+","+list3.size()+","+list4.size()+","+list5.size()+"个");

        Collections.sort(list1);
        Collections.sort(list2);
        Collections.sort(list3);
        Collections.sort(list4);
        Collections.sort(list5);

//        Random random = new Random();
        for (int i = 0; i < list1.size(); i=i+17) {
//            int s = random.nextInt(list1.size()-1);
//            list1.get(s).isDelete = false;
            list1.get(i).isDelete = false;
        }
        for (int i = 0; i < list2.size(); i=i+ Main2Activity.secondScale) {
//            int s = random.nextInt(list2.size()-1);
//            list2.get(s).isDelete = false;
            list2.get(i).isDelete = false;
        }
        for (int i = 0; i < list3.size(); i=i+Main2Activity.thirdScale) {
//            int s = random.nextInt(list3.size()-1);
//            list3.get(s).isDelete = false;
            list3.get(i).isDelete = false;
        }
        for (int i = 0; i < list4.size(); i=i+100) {
//            int s = random.nextInt(list4.size()-1);
//            list4.get(s).isDelete = false;
            list4.get(i).isDelete = false;
        }
        for (int i = 0; i < list5.size(); i=i+135) {
//            int s = random.nextInt(list5.size()-1);
//            list5.get(s).isDelete = false;
            list5.get(i).isDelete = false;
        }
        int sum = list1.size()+list2.size()+list3.size()+list4.size()+list5.size();
        List<Point> listAll = new ArrayList<>(sum);
        listAll.addAll(list1);
        listAll.addAll(list2);
        listAll.addAll(list3);
        listAll.addAll(list4);
        listAll.addAll(list5);

        List<Point> finalList = new ArrayList<>();

        for (int i = 0; i < listAll.size(); i++) {
            if (!listAll.get(i).isDelete){
                int indexX = (int) listAll.get(i).x;
                int indexY = (int) listAll.get(i).y;
                finalList.add(sourceArrays[indexX][indexY]);
            }
        }
        System.out.println("精简后，总共剩下的点有"+finalList.size()+"个");
        return finalList;
    }

    //求该矩阵的最小特征根对应的最小特征向量，即为该点的法向量
    private static Point getNormal(double[][] array){
        double min;
        int minEigValue;
        Matrix A = new Matrix(array);
        Matrix value = A.eig().getD();
        Matrix vector = A.eig().getV();

        min = value.get(0,0);
        if (value.get(1,1)<min){
            min = value.get(1,1);
        }
        if (value.get(2,2)<min){
            min = value.get(2,2);
        }

        if (min == value.get(0,0)){
            minEigValue = 1;
        }else if (min == value.get(1,1)){
            minEigValue =2;
        }
        else {
            minEigValue = 3;
        }

        double sqrtSum = Math.sqrt(
                Math.pow(vector.get(0,minEigValue),2)
                +Math.pow(vector.get(1,minEigValue),2)
                +Math.pow(vector.get(2,minEigValue),2));
        Point point = new Point();
        point.x = (float) (vector.get(0,minEigValue)/sqrtSum);
        point.y = (float) (vector.get(1,minEigValue)/sqrtSum);
        point.z = (float) (vector.get(2,minEigValue)/sqrtSum);

        return point;
    }

    //求某点K阶邻域的协方差矩阵
    private static double[][] getCovarianceMatrix(Point[] points){
        float averageX=0f,averageY=0f,averageZ=0f;
        for (Point point : points) {
            averageX = averageX + point.x;
            averageY = averageY + point.y;
            averageZ = averageZ + point.z;
        }
        averageX = averageX/points.length;
        averageY = averageY/points.length;
        averageZ = averageZ/points.length;

        double xx=0,yy=0,zz=0,xy=0,xz=0,yz=0;
        for (Point point : points) {
            double tempX = point.x;
            double tempY = point.y;
            double tempZ = point.z;
            xx = xx + Math.pow((tempX - averageX), 2);
            yy = yy + Math.pow((tempY - averageY), 2);
            zz = zz + Math.pow((tempZ - averageZ), 2);
            xy = xy + (tempX - averageX) * (tempY - averageY);
            xz = xz + (tempX - averageX) * (tempZ - averageZ);
            yz = yz + (tempY - averageY) * (tempZ - averageZ);
        }
        return new double[][]{{xx,xy,xz}, {xy,yy,yz}, {xz,yz,zz}};
    }

    //计算某个点的曲率
    private static float getCurvature(Point[] points){
        //当前点
        Point point0 = points[0];
        float curvature = 0;
        for (Point point:points){
            curvature = curvature + Math.abs(point0.x * point.x + point0.y * point.y + point0.z * point.z);
        }
        curvature = curvature/points.length;
        return curvature;
    }
}
