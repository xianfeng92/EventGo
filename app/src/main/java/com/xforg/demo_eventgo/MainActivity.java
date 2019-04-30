package com.xforg.demo_eventgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.xforg.rxeventgo.RxEventGo;
import com.xforg.rxeventgo.Subscribe;
import com.xforg.rxeventgo.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        post = findViewById(R.id.post);
        post.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RxEventGo.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    RxEventGo.getDefault().post(new MyEvent("Message from RxEventGo"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void HanldeEvent(MyEvent myEvent){
        Toast.makeText(this,"handleEvent"+myEvent.getMsg(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RxEventGo.getDefault().unregister(this);
    }

}
