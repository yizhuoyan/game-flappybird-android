package com.yizhuoyan.flappybird.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.yizhuoyan.flappybird.R;
import com.yizhuoyan.flappybird.model.Game;

public class Pipes {


    /**
     * 管道间距
     */
    private final int pipeBetweenSpace;
    //游戏开始管道出现初始位置
    private final int pipeFisrtShowOffset = 800;
    /**
     * 管道移动速度,等于鸟飞行速度
     */
    private final float speed;
    // 管道数组
    private Pipe[] pipes = new Pipe[3];
    //保存最后一个管道
    private Pipe lastPipe;

    public Pipes(Game game) {
        // 创建管道
        pipes[0] = new Pipe(game);
        pipes[1] = new Pipe(game);
        lastPipe = pipes[2] = new Pipe(game);
        pipeBetweenSpace = game.world.width / 2 + lastPipe.width / 2;
        speed = game.bird.getSpeed();
        reset();

    }

    /**
     * 所有管道重置
     */
    public void reset() {
        lastPipe = pipes[2];
        for (int i = 0; i < pipes.length; i++) {
            pipes[i].reset(pipeFisrtShowOffset + pipeBetweenSpace * i);
        }
    }

    /**
     * 移动水管并绘制
     */
    public void moveAndDraw(Canvas c) {
        for (Pipe pipe : pipes) {
            if (pipe.move(speed)) {//如果管道超出屏幕,则重置管道
                pipe.reset(lastPipe.x + pipeBetweenSpace);
                lastPipe = pipe;
            } else {
                pipe.draw(c);
            }
        }
    }

    /**
     * 绘制水管
     *
     * @param c
     */
    public void draw(Canvas c) {
        for (Pipe pipe : pipes) {
            pipe.draw(c);
        }
    }

    public class Pipe extends GameSprite {
        //在世界中的当前坐标
        public float x, y_up, y_down;
        //管道中间默认高度
        private int spaceHeight, halfOfspaceHeight;
        //管道图片
        private Bitmap pipeUpBitmap, pipeDownBitmap;
        /**
         * 随机中心
         */
        private int randomCenter;
        /**
         * 是否在小鸟前方
         */
        private boolean frontOfBird = true;


        public Pipe(Game game) {
            super(game);
            Bitmap bitmap = game.loadBitmap(R.drawable.sprite_pipe);
            spaceHeight = game.bird.height * 4;
            halfOfspaceHeight = spaceHeight / 2;
            width = bitmap.getWidth();
            height = bitmap.getHeight() / 2;
            pipeUpBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            pipeDownBitmap = Bitmap.createBitmap(bitmap, 0, height, width, height);
            bitmap.recycle();
        }

        /**
         * 管道属性重置
         *
         * @param initX 管道初始位置
         */
        public void reset(float initX) {
            randomCenter = (int) (Math.random() * (game.world.height - spaceHeight * 2) + spaceHeight);
            y_up = randomCenter - halfOfspaceHeight - height;
            y_down = randomCenter + halfOfspaceHeight;
            x = initX;
            frontOfBird = true;


        }

        /**
         * 管道移动
         *
         * @return 是否移动到了屏幕之外
         */
        public boolean move(float speed) {
            x -= speed;
            if (x + width < 0) {
                return true;
            }
            if (birdHitPipe(game.bird)) {
                //通知游戏模型,鸟撞击了管道
                game.birdHitPipe();
            }
            return false;
        }


        /**
         * 鸟是否撞击了柱子
         *
         * @param bird
         */
        private boolean birdHitPipe(Bird bird) {
            if (frontOfBird) {
                if (bird.x > this.x + width) {
                    game.birdPassAPipe();
                    frontOfBird = false;
                    return false;
                }
                if (bird.x + bird.width > x && bird.x < x + width) {
                    if (bird.y <= randomCenter - halfOfspaceHeight || bird.y + bird.height >= y_down) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void draw(Canvas c) {
            c.drawBitmap(pipeDownBitmap, x, y_down, null);
            c.drawBitmap(pipeUpBitmap, x, y_up, null);
        }


    }
}
