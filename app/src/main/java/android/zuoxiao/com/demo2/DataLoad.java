package android.zuoxiao.com.demo2;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuoxiao on 2017/4/5.
 */

class DataLoad {
    static float scaleX = 0;
    static float scaleY = 0;
    static float scaleZ = 0;

    /**从ASC文件中读取数据
     * ncols         313
     nrows         271
     xllcorner     636373.250
     yllcorner     3607260.789
     cellsize      30
     nodata_value  -9999.0
     * @return
     */
    static List<Float> loadFromASC(String fname, Resources r){
    //public static List<Float> loadFromASC(){
        int ncols = 0;//列数
        int nrows = 0;//行数
        int cellsize =0;//像素间距
        String nodatavalue="";//无值标志数据
        float xllcorner=0;//第一个像素的x坐标
        float yllcorner=0;//第一个像素的y坐标

        File file = new File("/Users/zuoxiao/Downloads/s/s1.asc");
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
                    if(temp[0].equals("ncols")){
                        ncols = Integer.parseInt(temp[1]);
                    }
                    else if (temp[0].equals("nrows")){
                        nrows = Integer.parseInt(temp[1]);
                    }
                    else if(temp[0].equals("cellsize")){
                        cellsize = Integer.parseInt(temp[1]);
                    }
                    else if (temp[0].equals("nodata_value")){
                        nodatavalue= temp[1];
                    }
                    else if (temp[0].equals("xllcorner")){
                        xllcorner= Float.parseFloat(temp[1]);
                    }
                    else if (temp[0].equals("yllcorner")){
                        yllcorner= Float.parseFloat(temp[1]);
                    }
                    else{
                        if (row%Main2Activity.rowSet==0) {
                            for (int i = 0; i < temp.length; i = i + Main2Activity.colSet) {
                                if (!temp[i].equals(nodatavalue)) {
                                    float tempValue;

                                    float data = 4f / ncols * i;
                                    dataSource.add(data);
                                    tempValue = (data>0?data:-data);
                                    scaleX = (scaleX>tempValue?scaleX:tempValue);

                                    data = 4f - 4f / nrows * row;
                                    dataSource.add(data);
                                    tempValue = (data>0?data:-data);
                                    scaleY = (scaleY>tempValue?scaleY:tempValue);

                                    data = Float.parseFloat(temp[i]);
                                    dataSource.add(data);
                                    tempValue = (data>0?data:-data);
                                    scaleZ = (scaleZ>tempValue?scaleZ:tempValue);
                                }

                            }
                        }
                        row++;//这个语句放在这里，简直妙哉！
                    }

                }
            }
            br.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    //生成txt
    public static void writeTxt(List<Float> list){
        File file2 = new File("/Users/zuoxiao/Downloads/s/s.txt");
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(file2));//构造输出流对象
            for (int i = 0; i < list.size()-2; i=i+3) {
                StringBuilder temp = new StringBuilder();
                temp.append(list.get(i));
                temp.append(",");
                temp.append(list.get(i+1));
                temp.append(",");
                temp.append(list.get(i+2));
                ps.append(temp);
                ps.println(" ");

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //从txt文件中读取数据的方法
    public static List<Float> loadFromTxt(String fname, Resources r){
        List<Float> resultlist = new ArrayList<>();
        try {
            InputStream inputStream = r.getAssets().open(fname);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String s;
            while((s = br.readLine())!=null){
                String[] temp = s.split(",");
                for (int i = 0; i <temp.length ; i++) {
                    double data = Double.parseDouble(temp[i]);
                    float res = (float) data;
                    resultlist.add(res);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultlist;
    }
}
