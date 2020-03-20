package com.yizhuoyan.flappybird.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.yizhuoyan.flappybird.model.Game;

/**
 * 游戏Acitivty
 */
public class GameActivity extends Activity implements SurfaceHolder.Callback, View.OnTouchListener {

    private SurfaceView gameView;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        gameView = new SurfaceView(this);
        gameView.getHolder().addCallback(this);
        this.setContentView(gameView);
        if (savedInstanceState != null) {
            //如果游戏已运行,恢复游戏状态
            game.restoreState(savedInstanceState);
        }
    }


    /**
     * 退出游戏
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        game.destroy();
        this.finish();

    }

    /**
     * 游戏失去焦点,如被切换到后台
     */
    @Override
    protected void onPause() {
        super.onPause();
        game.pause();
    }

    /**
     * 保存游戏状态
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        game.saveState(outState);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (game == null) {
            gameView.setOnTouchListener(this);
            game = new Game(gameView);
            game.start();
        } else {
            game.resume();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        game.gameViewChange();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        game.pause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            game.handleClickEvent(event);
        }
        return true;
    }


}
