package com.yizhuoyan.flappybird.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.yizhuoyan.flappybird.R;
import com.yizhuoyan.flappybird.model.Game;

public class Bird extends GameSprite {
    /**
     * 鸟状态常量
     */
    public final int STATUS_NEW = 0, STATUS_FLY = 1, STATUS_FAINT = 2,
            STATUS_DIE = 9;
    // 小鸟高宽度的一半
    public final int halfWidth, halfHeight;
    /**
     * *********** 鸟活动范围属性 *********************
     */
    //世界高宽度
    private final int worldWidth, worldHeight;
    /**
     * *********** 鸟自由落体相关属性 *********************
     */
    // 自由落体下鸟两帧移动距离增量
    private final float fallingMoveDelta = 1f;
    // 鸟跳跃中每帧移动距离
    private final float jumpFrameYMove;
    // 鸟跳跃每次上升总时间
    private final int jumpMaxDuration = 320;
    /**
     * *********** 鸟扇动翅膀动画 *********************
     */
    // 每帧播放时间
    private final int frameAnimationPlayDuration = 100;
    /**
     * *********** 鸟上下浮动相关属性 *********************
     */

    //自动浮动每帧移动距离
    private final float autoFloatFrameMove = 1;
    //小鸟当前状态
    public int status;
    // 小鸟每帧XY轴移动距离
    private float frameXMove, frameYMove;
    // 鸟的飞行角度
    private float angle;
    //小鸟自由落体每帧实际距离
    private float fallingFrameYMove = 0;
    /**
     * *********** 鸟跳跃相关属性 *********************
     */
    // 鸟跳跃开始时间
    private long jumpBeginTime;
    //小鸟是否跳跃
    private boolean isJump = false;
    // 帧动画数组
    private Bitmap[] frameAnimationBitmaps = new Bitmap[3];
    // 当前播放帧
    private int frameAnimationCurrentFrameIndex = 0;
    // 最后一次帧播放时间
    private long frameAnimationLastFramePlayTime = 0;
    //浮动最大距离
    private float autoFloatMaxDistance = 10;
    // 当前方向已浮动距离
    private float autoFloatedDistance = 0;
    // 自动浮动方向控制
    private boolean autoFloatUp = false;


    /**
     * 构造器
     *
     * @param game 游戏模型
     */
    public Bird(Game game) {
        super(game);


        // 初始化鸟的帧动画
        Bitmap birdSpriteBitmap = game.loadBitmap(R.drawable.sprite_bird);

        width = birdSpriteBitmap.getWidth();
        height = birdSpriteBitmap.getHeight() / 3;
        //初始化鸟跳跃速度
        jumpFrameYMove = height*0.3f;
        halfHeight = height / 2;
        halfWidth = width / 2;
        for (int i = 0; i < frameAnimationBitmaps.length; i++) {
            frameAnimationBitmaps[i] = Bitmap.createBitmap(birdSpriteBitmap, 0, height * i, width, height);
        }
        worldWidth = game.world.width;
        worldHeight = game.world.height;
        birdSpriteBitmap.recycle();
        this.reset();
    }

    /**
     * 鸟相关属性重置
     */
    public void reset() {
        {
            // 初始化鸟的初始位置在屏幕中央
            y = (game.gameView.getHeight() - height) / 2;
            x = worldWidth * 0.25f;
        }
        {
            frameYMove = 0;
            frameXMove = 4;
            autoFloatedDistance = 0;
            this.status = STATUS_NEW;
        }
    }

    /**
     * 返回小鸟飞行速度
     */
    public float getSpeed() {
        return frameXMove;
    }


    /**
     * 帧动画绘制
     */
    private void frameAnimationPlay() {
        if (System.currentTimeMillis() - frameAnimationLastFramePlayTime >= frameAnimationPlayDuration) {
            frameAnimationCurrentFrameIndex++;
            if (frameAnimationCurrentFrameIndex >= frameAnimationBitmaps.length) {
                frameAnimationCurrentFrameIndex = 0;
            }
            frameAnimationLastFramePlayTime = System.currentTimeMillis();
        }
    }

    /**
     * 鸟撞晕了
     */
    public void fainted() {
        status = STATUS_FAINT;
        frameYMove = 0;
        fallingFrameYMove = 0;
        isJump = false;
    }

    /**
     * 鸟跳跃
     */
    public void jump() {
        if (status == STATUS_NEW) {
            status = STATUS_FLY;
        }
        jumpBeginTime = System.currentTimeMillis();
        isJump = true;
        frameYMove = 0;
        fallingFrameYMove = 0;
        game.gameSound.playBirdJump();
    }

    /**
     * 鸟跳跃帧动画
     */
    private void jumpFramePlay() {
        if (isJump) {
            if (System.currentTimeMillis() - jumpBeginTime <= jumpMaxDuration) {
                frameYMove -= jumpFrameYMove;
                if (y + height <= 0) {
                    frameYMove = 0;
                }
            } else {
                isJump = false;
                fallingFrameYMove = 0;
                frameYMove = 0;
            }
        }
    }


    /**
     * 鸟自由落体帧动画
     */
    private void fallingFramePlay() {
        fallingFrameYMove += fallingMoveDelta;
        frameYMove += fallingFrameYMove;
    }

    /**
     * 鸟自动上下浮动
     */
    public void autoFloatFramePlay() {
        if (autoFloatUp) {
            autoFloatedDistance += autoFloatFrameMove;
            if (autoFloatedDistance > autoFloatMaxDistance) {
                autoFloatUp = false;
                autoFloatedDistance = 0;
            } else {
                y -= autoFloatFrameMove;
            }

        } else {
            autoFloatedDistance += autoFloatFrameMove;
            if (autoFloatedDistance > autoFloatMaxDistance) {
                autoFloatUp = true;
                autoFloatedDistance = 0;
            } else {
                y += autoFloatFrameMove;
            }
        }

    }

    /**
     * 计算鸟是否撞击了地面
     */
    private void judgeHitFloor() {
        if (y + height >= worldHeight) {
            if (status == STATUS_FLY) {//直接撞击地面
                game.birdHitGround();
            }
            this.status = STATUS_DIE;
            y = worldHeight - height;

        }
    }

    public void draw(Canvas canvas) {
        switch (status) {
            case STATUS_NEW:
                canvas.drawBitmap(frameAnimationBitmaps[frameAnimationCurrentFrameIndex], x, y, null);
                frameAnimationPlay();
                autoFloatFramePlay();
                break;
            case STATUS_FLY:
                canvas.save();
                if (this.isJump) {
                    this.angle = -20;
                } else {
                    this.angle = (float) Math.toDegrees(Math.atan(frameYMove / frameXMove));
                }
                canvas.rotate(this.angle, this.x + halfWidth, this.y + halfHeight);
                canvas.drawBitmap(frameAnimationBitmaps[frameAnimationCurrentFrameIndex], x, y, null);
                canvas.restore();
                frameYMove = 0;
                this.frameAnimationPlay();
                this.fallingFramePlay();
                this.jumpFramePlay();
                y += frameYMove;
                judgeHitFloor();
                break;
            case STATUS_FAINT:

                canvas.save();
                this.angle = (float) Math.toDegrees(Math.atan(frameYMove / frameXMove));
                canvas.rotate(this.angle, this.x + halfWidth, this.y + halfHeight);
                canvas.drawBitmap(frameAnimationBitmaps[frameAnimationCurrentFrameIndex], x, y, null);
                canvas.restore();
                frameYMove = 0;
                this.fallingFramePlay();
                y += frameYMove;
                judgeHitFloor();
                break;
            case STATUS_DIE:
                canvas.save();
                canvas.rotate(90, this.x + halfWidth, this.y + halfHeight);
                canvas.drawBitmap(frameAnimationBitmaps[frameAnimationCurrentFrameIndex], x, y, null);
                canvas.restore();
                break;
        }


    }

}
