package android.zuoxiao.com.demo2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;

/**
 * Created by zuoxiao on 2017/4/5.
 */

public class Delaunay {
    public static List<Float> doDelaunayFromGit(List<Float> floatList){
        List<Float> result = new ArrayList<>();
        try {
            List<Vector2D> pointSet = new ArrayList<>();
            for (int i = 0; i < floatList.size(); i=i+3) {
                Vector2D vertex = new Vector2D(floatList.get(i),floatList.get(i+1));
                pointSet.add(vertex);
            }

            DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(pointSet);
            delaunayTriangulator.triangulate();

            List<Triangle2D> triangleSoup = delaunayTriangulator.getTriangles();
            System.out.println("生成了"+triangleSoup.size()+"个三角形");
            result=doEdge(triangleSoup,floatList);
        } catch (NotEnoughPointsException e) {
        }

        return result;
    }
    private static List<Float> doEdge(List<Triangle2D> triangleSoup,List<Float> sourcelist){

        List<Float> result = new ArrayList<>();
        for (int i = 0; i < triangleSoup.size(); i++) {
            Vector2D vectorA = triangleSoup.get(i).a;
            Vector2D vectorB = triangleSoup.get(i).b;
            Vector2D vectorC = triangleSoup.get(i).c;
            int flag = 0;

            //点A
            result.add((float) vectorA.x);
            result.add((float) vectorA.y);
            for (int j = 0; j < sourcelist.size()-2; j=j+3) {
                if (vectorA.x == (double) sourcelist.get(j) && vectorA.y == (double) sourcelist.get(j+1)){
                    result.add(sourcelist.get(j+2));
                    flag = 1;
                }
            }
            if (flag == 0){
                result.add(0.0f);
            }
            flag = 0;
            //点B
            result.add((float) vectorB.x);
            result.add((float) vectorB.y);
            for (int j = 0; j < sourcelist.size(); j=j+3) {
                if (vectorB.x == (double) sourcelist.get(j) && vectorB.y == (double) sourcelist.get(j+1)){
                    result.add(sourcelist.get(j+2));
                    flag = 1;
                }
            }
            if (flag == 0){
                result.add(0.0f);
            }
            flag = 0;
            //点C
            result.add((float) vectorC.x);
            result.add((float) vectorC.y);
            for (int j = 0; j < sourcelist.size(); j=j+3) {
                if (vectorC.x == (double) sourcelist.get(j) && vectorC.y == (double) sourcelist.get(j+1)){
                    result.add(sourcelist.get(j+2));
                    flag = 1;
                }
            }
            if (flag == 0){
                result.add(0.0f);
            }
        }
        return result;
    }
    //坐标缩放用
    public static float getMaxNumber(List<Float> list){
        Iterator<Float> iterator = list.iterator();
        float max = 0.0f;
        while (iterator.hasNext()){
            Float temp = iterator.next();
            Float index = temp>0.0f?temp:-temp;
            if (index > max){
                max = index;
            }
        }
        return max*8f;
    }
}
