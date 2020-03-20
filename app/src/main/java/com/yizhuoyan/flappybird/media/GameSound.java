package com.yizhuoyan.flappybird.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;

import com.yizhuoyan.flappybird.R;

import java.util.HashMap;

public class GameSound {
    // 游戏音效管理
    private final SoundPool soundPool;
    private final HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    private Context context;
    private MediaPlayer hitMediaPlayer, fallingMediaPlayer;

    public GameSound(Context context) {
        this.context = context;
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);
        soundMap.put(R.raw.bird_falling, soundPool.load(context, R.raw.bird_falling, 1));
        soundMap.put(R.raw.bird_jump, soundPool.load(context, R.raw.bird_jump, 1));
        soundMap.put(R.raw.bird_hit, soundPool.load(context, R.raw.bird_hit, 1));
        soundMap.put(R.raw.got_score, soundPool.load(context, R.raw.got_score, 1));
        soundMap.put(R.raw.swooshing, soundPool.load(context, R.raw.swooshing, 1));
        hitMediaPlayer = MediaPlayer.create(context, R.raw.bird_hit);
        fallingMediaPlayer = MediaPlayer.create(context, R.raw.bird_falling);
        hitMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                fallingMediaPlayer.start();
            }
        });

    }

    public void playBirdJump() {
        this.play(R.raw.bird_jump);
    }

    public void playBirdHit() {
        this.play(R.raw.bird_hit);
    }

    public void playBirdHitAndFalling() {
        hitMediaPlayer.start();
    }

    public void playGotScore() {
        this.play(R.raw.got_score);
    }

    public void playSwooshing() {
        this.play(R.raw.swooshing);
    }

    public void play(int musicId) {
        soundPool.play(soundMap.get(musicId), 1, 1, 0, 0, 1);
    }


}
