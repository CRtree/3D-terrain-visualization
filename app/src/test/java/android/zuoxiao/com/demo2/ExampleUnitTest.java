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
       // List<Float> list = DataLoad.loadFromASC();
    }

    @Test
    public void isRight(){
        float data;
        int nrows = 271;
        int row =100;
        data = 1-1f/nrows*row;
        System.out.println(data);

    }
}