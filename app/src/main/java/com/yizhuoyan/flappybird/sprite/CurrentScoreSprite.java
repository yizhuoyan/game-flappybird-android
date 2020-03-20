package com.yizhuoyan.flappybird.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.yizhuoyan.flappybird.R;
import com.yizhuoyan.flappybird.model.Game;

public class CurrentScoreSprite extends GameSprite {

    private Bitmap[] scores = new Bitmap[10];

    public CurrentScoreSprite(Game game) {
        super(game);
        Bitmap scoreBitmap = game.loadBitmap(R.drawable.sprite_scores_big);
        width = scoreBitmap.getWidth() / 5;
        height = scoreBitmap.getHeight() / 2;
        for (int i = 0; i < scores.length; i++) {
            if (i > 4) {
                scores[i] = Bitmap.createBitmap(scoreBitmap, (i - 5) * width, height, width, height);
            } else {
                scores[i] = Bitmap.createBitmap(scoreBitmap, i * width, 0, width, height);
            }
        }
        scoreBitmap.recycle();
        x = (game.world.width - width) / 2;
        y = 40;
    }

    public void draw(Canvas c) {
        int score = game.currentScore;
        if (score >= 100) {
            c.drawBitmap(scores[score / 100], x - width, y, null);
            c.drawBitmap(scores[score / 10 % 10], x, y, null);
            c.drawBitmap(scores[score % 10], x + width, y, null);
        } else if (score >= 10) {
            c.drawBitmap(scores[score / 10], x - width / 2, y, null);
            c.drawBitmap(scores[score % 10], x + width / 2, y, null);
        } else {
            c.drawBitmap(scores[score % 10], x, y, null);
        }
    }
}
