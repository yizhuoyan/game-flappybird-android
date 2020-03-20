package com.yizhuoyan.flappybird.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.yizhuoyan.flappybird.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SplashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gotoMainActivity();
                    }
                });
            }
        }, 2000);
    }

    private void gotoMainActivity() {
        startActivity(new Intent(this, GameActivity.class));
        this.finish();
    }
}
