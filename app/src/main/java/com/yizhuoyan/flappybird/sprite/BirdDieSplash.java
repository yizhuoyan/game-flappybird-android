package com.yizhuoyan.flappybird.sprite;

import android.graphics.Canvas;

import com.yizhuoyan.flappybird.model.Game;

public class BirdDieSplash {

    private int duration = 200;
    private long playBeginTime;
    private boolean playing = false;

    public BirdDieSplash(Game game) {

    }

    public void play() {
        playBeginTime = System.currentTimeMillis();
        playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void draw(Canvas c) {
        if (playing && System.currentTimeMillis() - playBeginTime <= duration) {
            c.drawColor(0x55ffffff);
        } else {
            playing = false;
        }
    }
}
