package com.yizhuoyan.flappybird.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.yizhuoyan.flappybird.R;
import com.yizhuoyan.flappybird.model.Game;

/**
 * 游戏结束界面绘制
 */
public class GameOverViewSprite extends GameSprite {

    private TextGameOver textGameOver;
    private ScorePanel scorePanel;
    private ReplayButton replayButton;

    public GameOverViewSprite(Game game) {
        super(game);
        replayButton = new ReplayButton(game);
        scorePanel = new ScorePanel(game);
        textGameOver = new TextGameOver(game);

    }

    public void draw(Canvas canvas) {
        textGameOver.draw(canvas);
        scorePanel.draw(canvas);
        replayButton.draw(canvas);
    }

    public void handleClickEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= replayButton.x && x <= replayButton.x + replayButton.width) {
            if (y >= replayButton.y && y <= replayButton.height + replayButton.y) {
                game.replay();
            }
        }
    }

    public class TextGameOver extends GameSprite {
        private Bitmap textGameOverBitmap;


        public TextGameOver(Game game) {
            super(game);
            textGameOverBitmap = game.loadBitmap(R.drawable.txt_game_over);
            width = textGameOverBitmap.getWidth();
            height = textGameOverBitmap.getHeight();
            this.x = (game.world.width - width) / 2;
            this.y = scorePanel.y - height - 15;
        }


        public void draw(Canvas c) {
            c.drawBitmap(textGameOverBitmap, x, y, null);
        }
    }

    public class ScorePanel extends GameSprite {
        private Bitmap[] scoreNums = new Bitmap[10];

        private Bitmap panelBitmap;

        private float right_currentScore, y_currentScore, right_bestScore, y_bestScore;
        private int numWidth, numHeight;


        public ScorePanel(Game game) {
            super(game);
            {
                panelBitmap = game.loadBitmap(R.drawable.bg_score_panel);
                width = panelBitmap.getWidth();
                height = panelBitmap.getHeight();
                x = (game.world.width - width) / 2;
                y = replayButton.y - height - 15;
            }

            {
                Bitmap scoreNumBitmap = game.loadBitmap(R.drawable.sprite_scores_small);
                numWidth = scoreNumBitmap.getWidth() / 5;
                numHeight = scoreNumBitmap.getHeight() / 2;
                for (int i = 0; i < scoreNums.length; i++) {
                    if (i > 4) {
                        scoreNums[i] = Bitmap.createBitmap(scoreNumBitmap, (i - 5) * numWidth, numHeight, numWidth, numHeight);
                    } else {
                        scoreNums[i] = Bitmap.createBitmap(scoreNumBitmap, i * numWidth, 0, numWidth, numHeight);
                    }
                }
                scoreNumBitmap.recycle();
            }
            {
                //两个分数坐标
                right_currentScore = x + width - numWidth;
                y_currentScore = y + numHeight * 1.5f;
                right_bestScore = right_currentScore;
                y_bestScore = y + height - numHeight * 1.5f;
            }


        }

        public void draw(Canvas canvas) {

            canvas.drawBitmap(panelBitmap, x, y, null);

            drawCurrentScore(canvas);

            drawBestScore(canvas);

        }

        /**
         * 绘制当前关卡分数
         */
        private void drawCurrentScore(Canvas c) {
            int score = game.currentScore;
            if (score >= 100) {
                c.drawBitmap(scoreNums[score / 100], right_currentScore - numWidth * 3, y_currentScore, null);
                c.drawBitmap(scoreNums[score / 10 % 10], right_currentScore - numWidth * 2, y_currentScore, null);
                c.drawBitmap(scoreNums[score % 10], right_currentScore - numWidth, y_currentScore, null);
            } else if (score >= 10) {
                c.drawBitmap(scoreNums[score / 10], right_currentScore - numWidth * 2, y_currentScore, null);
                c.drawBitmap(scoreNums[score % 10], right_currentScore - numWidth, y_currentScore, null);
            } else {
                c.drawBitmap(scoreNums[score % 10], right_currentScore - numWidth, y_currentScore, null);
            }
        }


        /**
         * 绘制最高分
         */
        private void drawBestScore(Canvas c) {
            int score = game.bestScore;
            if (score >= 100) {
                c.drawBitmap(scoreNums[score / 100], right_bestScore - numWidth * 3, y_bestScore, null);
                c.drawBitmap(scoreNums[score / 10 % 10], right_bestScore - numWidth * 2, y_bestScore, null);
                c.drawBitmap(scoreNums[score % 10], right_bestScore - numWidth, y_bestScore, null);
            } else if (score >= 10) {
                c.drawBitmap(scoreNums[score / 10], right_bestScore - numWidth * 2, y_bestScore, null);
                c.drawBitmap(scoreNums[score % 10], right_bestScore - numWidth, y_bestScore, null);
            } else {
                c.drawBitmap(scoreNums[score % 10], right_bestScore - numWidth, y_bestScore, null);
            }
        }
    }

    public class ReplayButton extends GameSprite {
        private Bitmap bitmap;

        public ReplayButton(Game game) {
            super(game);
            bitmap = game.loadBitmap(R.drawable.btn_replay);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            x = (game.world.width - width) / 2;
            y = game.world.height - height - 5;
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(bitmap, x, y, null);
        }
    }

}
