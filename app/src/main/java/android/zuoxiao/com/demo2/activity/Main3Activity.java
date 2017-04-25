package android.zuoxiao.com.demo2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.zuoxiao.com.demo2.R;
import android.zuoxiao.com.demo2.myView.MyView3;
import android.zuoxiao.com.demo2.util.WaitScreen;

public class Main3Activity extends AppCompatActivity {

    MyView3 myView;
    PopupWindow popupWindow;
    private WaitScreen waitScreen;
    public static boolean hasloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myView=new MyView3(this);
        int time = 25;
        if (hasloaded) {
            time = 2;
        }
            new Handler().postDelayed(() -> {
                if (popupWindow != null) {
                    waitScreen.close();
                }
            }, time * 1000);
            hasloaded = true;

        setContentView(R.layout.activity_main3);
        LinearLayout ll = (LinearLayout) findViewById(R.id.activity_main3);
        ll.addView(myView);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(Main3Activity.this,MainActivity.class);
            startActivity(intent);
        });
    }
    @Override
    public void onAttachedToWindow() {
        show();
        super.onAttachedToWindow();
    }

    private void show() {
        waitScreen=new WaitScreen(this);
        popupWindow=waitScreen.show();
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
}
