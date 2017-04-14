package android.zuoxiao.com.demo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    MyView myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置为全屏
        myView=new MyView(this);

        // 切换到主界面
        setContentView(R.layout.activity_main);
        LinearLayout ll = (LinearLayout) findViewById(R.id.activity_main);
        ll.addView(myView);
        //普通拖拉条被拉动的处理代码
        SeekBar sb=(SeekBar)this.findViewById(R.id.SeekBar01);
        sb.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        myView.setLightOffset((seekBar.getMax()/2.0f-progress)/(seekBar.getMax()/2.0f)*-4);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {	}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }
                }
        );

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
