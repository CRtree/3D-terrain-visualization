package android.zuoxiao.com.demo2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.zuoxiao.com.demo2.R;
import android.zuoxiao.com.demo2.myView.MyView2;
import android.zuoxiao.com.demo2.paint.PointCloud;
import android.zuoxiao.com.demo2.paint.Triangle1;
import android.zuoxiao.com.demo2.util.DataLoad;

import java.lang.reflect.Field;

public class Main2Activity extends AppCompatActivity {
    public static String fliename = "5.asc";
    public static int rowSet = 6;
    public static int colSet = 5;
    public static float cameraSet = 7.9f;
    public static int time = 10;


    public static int secondScale = 25;
    public static int thirdScale = 60;
    MyView2 myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeActionOverflowMenuShown();
        myView=new MyView2(this);
        setContentView(R.layout.activity_main2);
        LinearLayout ll = (LinearLayout) findViewById(R.id.activity_main2);
        ll.addView(myView);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(Main2Activity.this,Main3Activity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        myView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myView.onPause();
    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        DataLoad.scaleZ = 0f;
        DataLoad.scaleX = 0f;
        DataLoad.scaleY = 0f;
        Triangle1.triangleFlag = false;
        PointCloud.pointFlag = false;
        Main3Activity.hasloaded = false;
        cameraSet = 7.9f;

        switch(item.getItemId()){
            case R.id.item_1:
                fliename = "5.asc";
                secondScale = 25;
                thirdScale = 60;
                time = 10;
                rowSet = 6;
                colSet = 5;
                onPause();
                onResume();
                break;
            case R.id.item_2:
                fliename = "10.asc";
                secondScale = 40;
                thirdScale = 70;
                time = 14;
                rowSet = 6;
                colSet = 5;
                onPause();
                onResume();
                break;
            default:
        }
        return true;
    }
}
