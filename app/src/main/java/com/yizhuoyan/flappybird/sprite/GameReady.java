package com.yizhuoyan.flappybird.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.yizhuoyan.flappybird.R;
import com.yizhuoyan.flappybird.model.Game;

public class GameReady extends GameSprite {

    private Bitmap bitmap;


    public GameReady(Game game) {
        super(game);
        bitmap = game.loadBitmap(R.drawable.txt_game_ready);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        x = (game.world.width - width) / 2;
        y = (game.gameView.getHeight() - height) / 2;
    }

    public void draw(Canvas c) {
        c.drawBitmap(bitmap, x, y, null);
    }
}
