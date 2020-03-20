package com.yizhuoyan.flappybird.sprite;

import com.yizhuoyan.flappybird.model.Game;

/**
 * Created by ben on 1/20/2015.
 */
public abstract class GameSprite {
    public float x, y;
    public int width, height;
    protected Game game;

    protected GameSprite(Game game) {
        this.game = game;
    }
}
