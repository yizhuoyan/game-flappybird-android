package com.yizhuoyan.flappybird.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yizhuoyan.flappybird.media.GameSound;
import com.yizhuoyan.flappybird.sprite.Bird;
import com.yizhuoyan.flappybird.sprite.BirdDieSplash;
import com.yizhuoyan.flappybird.sprite.CurrentScoreSprite;
import com.yizhuoyan.flappybird.sprite.GameOverViewSprite;
import com.yizhuoyan.flappybird.sprite.GameReady;
import com.yizhuoyan.flappybird.sprite.Pipes;
import com.yizhuoyan.flappybird.sprite.ScrollBar;
import com.yizhuoyan.flappybird.sprite.World;

/**
 * Created by ben on 1/20/2015.
 * 游戏模型
 */
public class Game {
    /**
     * 游戏视图对象
     */
    public final SurfaceView gameView;
    /**
     * 游戏音效
     */
    public final GameSound gameSound;
    /**
     * 游戏界面状态常量
     */
    private final int STATUS_READY = 1, STATUS_PLAY = 2, STATUS_FAILED = 3;
    /**
     * 屏幕密度
     */
    public float density;
    /**
     * 界面元素
     */
    public World world;
    public ScrollBar scrollBar;
    public GameReady gameReady;
    public Bird bird;
    public Pipes pipes;
    public CurrentScoreSprite currentScoreSprite;
    public BirdDieSplash birdDieSplash;
    public GameOverViewSprite gameOverView;
    /**
     * 游戏数据
     */
    //当前分
    public int currentScore = 0;
    //最高分
    public int bestScore = 0;
    /**
     * 游戏界面绘制状态
     */
    private int drawStatus;
    /**
     * 游戏线程执行和暂停
     */
    private boolean gameThreadRun, gameThreadPause;
    /**
     * 游戏线程
     */
    private final Thread gameThread = new Thread(new Runnable() {
        @Override
        public void run() {
            //游戏视图控制器
            SurfaceHolder holder = gameView.getHolder();
            //游戏画布
            Canvas canvas;
            //两帧之间实际执行间隔
            long duration;
            //上一帧执行完毕时间
            long lastGameFrameTime = 0;
            /** 游戏帧频率 */
            final int fps = 1000 / 30;

            while (gameThreadRun) {
                while (gameThreadPause) {
                    synchronized (gameView) {
                        try {
                            gameView.wait();
                        } catch (InterruptedException e) {
                        }

                    }
                }
                canvas = holder.lockCanvas();
                frameLogicAndDraw(canvas);
                duration = System.currentTimeMillis() - lastGameFrameTime;
                if (duration < fps) {
                    SystemClock.sleep(fps - duration);
                }
                holder.unlockCanvasAndPost(canvas);
                lastGameFrameTime = System.currentTimeMillis();

            }
        }
    });

    /**
     * 构造器,构建游戏对象,包括游戏资源等
     *
     * @param view 游戏视图
     */
    public Game(SurfaceView view) {
        this.gameView = view;

        //初始化界面元素
        {
            scrollBar = new ScrollBar(this);
            world = new World(this);
            gameReady = new GameReady(this);
            bird = new Bird(this);
            pipes = new Pipes(this);
            currentScoreSprite = new CurrentScoreSprite(this);
            birdDieSplash = new BirdDieSplash(this);
            gameOverView = new GameOverViewSprite(this);
        }
        {
            gameSound = new GameSound(gameView.getContext());
        }
    }

    /**
     * 工具方法,加载指定图片
     */
    public Bitmap loadBitmap(int id) {
        return BitmapFactory.decodeResource(gameView.getResources(), id);
    }


    /**
     * 游戏暂停
     */
    public void pause() {
        this.gameThreadPause = true;
    }

    /**
     * 游戏从暂停恢复
     */
    public void resume() {
        this.gameThreadPause = false;
        synchronized (gameView) {
            this.gameView.notify();
        }
    }

    /**
     * 游戏恢复状态
     *
     * @param savedInstanceState 游戏存储状态
     */
    public void restoreState(Bundle savedInstanceState) {
        this.bestScore = savedInstanceState.getInt("bestScore", 0);
    }

    /**
     * 游戏保存状态
     *
     * @param outState 游戏状态
     */
    public void saveState(Bundle outState) {
        if (outState != null) {
            outState.putInt("bestScore", bestScore);
        }
    }


    /**
     * 开始游戏
     */
    public void start() {
        this.gameThreadRun = true;
        this.gameThreadPause = false;
        this.drawStatus = STATUS_READY;
        this.gameThread.start();
    }

    /**
     * 重玩
     */
    public void replay() {
        this.drawStatus = STATUS_READY;
        this.bird.reset();
        this.pipes.reset();
        currentScore = 0;

    }

    /**
     * 销毁游戏
     */
    public void destroy() {
        boolean retry = true;
        this.gameThreadRun = false;
        while (retry) {
            try {
                this.gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }


    /**
     * 游戏失败
     */
    private void gameOver() {
        this.drawStatus = STATUS_FAILED;
        if (this.currentScore > this.bestScore) {
            this.bestScore = this.currentScore;
        }

    }

    /**
     * 小鸟碰撞到管道
     */
    public void birdHitPipe() {
        bird.fainted();
        this.gameOver();
        birdDieSplash.play();
        gameSound.playBirdHitAndFalling();
    }

    /**
     * 小鸟碰撞到地面
     */
    public void birdHitGround() {
        this.gameOver();
        gameSound.playBirdHit();
        birdDieSplash.play();
    }

    /**
     * 小鸟穿过一个管道
     */
    public void birdPassAPipe() {
        this.currentScore++;
        gameSound.playGotScore();
    }

    /**
     * 处理点击事件
     */
    public void handleClickEvent(MotionEvent event) {
        if (gameThreadPause) {
            this.resume();
            return;
        }
        if (drawStatus == STATUS_READY) {
            this.drawStatus = STATUS_PLAY;
            bird.jump();
        } else if (drawStatus == STATUS_PLAY) {
            bird.jump();
        } else if (drawStatus == STATUS_FAILED) {
            gameOverView.handleClickEvent(event);
        }
    }


    public void gameViewChange() {

    }

    /**
     * 游戏每帧绘制
     */
    private void frameLogicAndDraw(Canvas canvas) {

        switch (drawStatus) {
            case STATUS_READY:
                world.moveAndDraw(canvas);
                gameReady.draw(canvas);
                bird.draw(canvas);
                scrollBar.moveAndDraw(canvas);
                currentScoreSprite.draw(canvas);
                break;
            case STATUS_PLAY:
                world.moveAndDraw(canvas);
                pipes.moveAndDraw(canvas);
                bird.draw(canvas);
                scrollBar.moveAndDraw(canvas);
                currentScoreSprite.draw(canvas);
                break;
            case STATUS_FAILED:
                world.moveAndDraw(canvas);
                pipes.draw(canvas);
                bird.draw(canvas);
                scrollBar.draw(canvas);

                birdDieSplash.draw(canvas);
                gameOverView.draw(canvas);
                break;

        }
    }

}
