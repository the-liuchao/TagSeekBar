package com.example.liuchao.numberprogressbar;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.liuchao.numberprogressbar.widge.NumberSeekBar;

public class MainActivity extends AppCompatActivity {

    private NumberSeekBar mProgressBar;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mProgressBar.getProgress() >= 100) {
                mProgressBar.setProgress(0);
            }
//            mProgressBar.setProgress(mProgressBar.getProgress() + 1);
            mProgressBar.setProgress(100);
//            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (NumberSeekBar) findViewById(R.id.numberProgressBar);
        mHandler.postDelayed(runnable, 100);
    }
}
