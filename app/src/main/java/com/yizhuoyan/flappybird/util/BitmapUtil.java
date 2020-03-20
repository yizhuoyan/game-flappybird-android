package com.yizhuoyan.flappybird.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by ben on 2/2/2015.
 */
public class BitmapUtil {

    /**
     * @param source  source bitmap
     * @param columns columns
     * @return merged bitmap
     */
    public static Bitmap mergeBitmapByHorizontal(Bitmap source, int columns) {
        Bitmap result = Bitmap.createBitmap(source.getWidth() * columns, source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(result);
        for (int i = 0; i < columns; i++) {
            canvas.drawBitmap(source, source.getWidth() * i, 0, null);
        }
        return result;
    }
}
