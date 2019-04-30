package com.xforg.demo_eventgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.xforg.rxeventgo.Subscribe;
import com.xforg.rxeventgo.ThreadMode;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "XFORG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleBaseEvent(MyEvent myEvent){
        Log.d(TAG, "handleBaseEvent: ");
    }
}
