package android.zuoxiao.com.demo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class Main2Activity extends AppCompatActivity {
    MyView2 myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置为全屏
        myView=new MyView2(this);
        setContentView(R.layout.activity_main2);
        LinearLayout ll = (LinearLayout) findViewById(R.id.activity_main2);
        ll.addView(myView);
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });


//        myView.requestFocus();
//        myView.setFocusableInTouchMode(true);
//        // makeActionOverflowMenuShown();
//        setContentView(myView);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()){
//            case R.id.tri_item:
//                // Toast.makeText(this,"add",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
//                startActivity(intent);
//                break;
//
//            default:
//        }
//        return true;
//    }
//
//    private void makeActionOverflowMenuShown() {
//        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
//        try {
//            ViewConfiguration config = ViewConfiguration.get(this);
//            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//            if (menuKeyField != null) {
//                menuKeyField.setAccessible(true);
//                menuKeyField.setBoolean(config, false);
//            }
//        } catch (Exception e) {
//
//        }
//    }
}
