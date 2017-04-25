package android.zuoxiao.com.demo2;

/**
 * Created by zuoxiao
 * 2017/4/21.
 */

public class Point implements Comparable{
    public float x;
    public float y;
    public float z;
    public boolean isDelete;

    Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.isDelete = true;
    }
    public Point() {
        this.isDelete = true;
    }


    @Override
    public int compareTo(Object o) {
        Point temp = (Point) o;
        if (this.x > temp.x){
            return 1;
        }else if (this.x == temp.x){
            return 0;
        }else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", isDelete=" + isDelete +
                '}';
    }
}
