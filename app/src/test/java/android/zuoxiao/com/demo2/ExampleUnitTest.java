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
       // assertEquals(4, 2 + 2);
//        List<Float> list = DataLoad.loadFromASC();
//        System.out.println("采集了"+list.size()+"个点");
//        List<Float> tlist =Delaunay.doDelaunayFromGit(list);
//        System.out.println("生成了"+list.size()+"个点");
        //DataLoad.writeTxt(list);
    }

    @Test
    public void isRight(){
        float nrows = 271;
        float row =100;
        System.out.println(Math.round(nrows/row));

    }
}