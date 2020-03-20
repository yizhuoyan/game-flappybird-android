package com.yizhuoyan.flappybird.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.yizhuoyan.flappybird.R;
import com.yizhuoyan.flappybird.model.Game;
import com.yizhuoyan.flappybird.util.BitmapUtil;

/**
 * Created by ben on 1/21/2015.
 */
public class ScrollBar extends GameSprite {

    private final int speed = 7;
    private Bitmap bitmap;
    private int tileWidth, tileHeight;

    public ScrollBar(Game game) {
        super(game);
        this.reset();

    }

    public void reset() {
        if (bitmap != null) bitmap.recycle();
        Bitmap source = game.loadBitmap(R.drawable.bg_scrollbar);
        tileWidth = source.getWidth();
        tileHeight = source.getHeight();

        int columns = game.gameView.getWidth() / tileWidth;
        if (game.gameView.getWidth() % tileWidth != 0) {
            columns++;
        }
        columns++;//for repeat draw
        bitmap = BitmapUtil.mergeBitmapByHorizontal(source, columns);
        this.width = game.gameView.getWidth();
        this.height = bitmap.getHeight();
        this.x = 0;
        this.y = game.gameView.getHeight() - this.height;


    }

    public void moveAndDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, this.x, this.y, null);
        x -= speed;
        if (x <= -tileWidth) {
            x = 0;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, this.x, this.y, null);
    }
}
