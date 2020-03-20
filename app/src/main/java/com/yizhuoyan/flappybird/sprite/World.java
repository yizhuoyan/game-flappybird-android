package com.yizhuoyan.flappybird.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.yizhuoyan.flappybird.R;
import com.yizhuoyan.flappybird.model.Game;
import com.yizhuoyan.flappybird.util.BitmapUtil;

/**
 * Created by ben on 1/20/2015.
 */
public class World extends GameSprite {
    private Bitmap bitmap;
    private RectF drawRect;
    private Rect sourceRect;
    private int speed = 1;
    private int tileWidth, tileHeight;

    public World(Game game) {
        super(game);
        Bitmap source = game.loadBitmap(R.drawable.bg_world);
        tileWidth = source.getWidth();
        tileHeight = source.getHeight();
        int columns = game.gameView.getWidth() / tileWidth;
        if (game.gameView.getWidth() % tileWidth != 0) {
            columns++;
        }
        columns++;//for repeat
        bitmap = BitmapUtil.mergeBitmapByHorizontal(source, columns);

        this.width = game.gameView.getWidth();
        this.height = game.gameView.getHeight() - game.scrollBar.height;
        sourceRect = new Rect(0, 0, tileWidth, tileHeight);
        drawRect = new RectF(0, 0, this.width, this.height);
        source.recycle();
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, sourceRect, drawRect, null);
    }

    public void moveAndDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, sourceRect, drawRect, null);
        sourceRect.offset(speed, 0);
        if (sourceRect.left >= tileWidth) {
            sourceRect.offsetTo(0, 0);
        }

    }
}
