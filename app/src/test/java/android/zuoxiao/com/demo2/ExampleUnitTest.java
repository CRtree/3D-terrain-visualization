package android.zuoxiao.com.demo2;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        List<Float> list = DataLoad.loadFromASC();
//        System.out.println("采集了"+list.size()/3+"个点");
//        List<Triangle2D> trianglelist =Delaunay.doDelaunayFromGit(list);
//        System.out.println("生成了"+trianglelist.size()+"个三角形");
//        List<Float> res = Delaunay.addHight(trianglelist,list);
//        for (int i = 0; i < res.size()-2; i=i+3) {
//            System.out.println(res.get(i)+","+res.get(i+1)+","+res.get(i+2));
//        }
    }

    @Test
    public void isRight(){
        float nrows = 271;
        float row =100;
        System.out.println(Math.round(nrows/row));

    }
}