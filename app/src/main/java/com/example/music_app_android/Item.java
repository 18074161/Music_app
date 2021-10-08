package com.example.music_app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music_app_android.model.Song;

import java.util.List;

public class Item extends AppCompatActivity {
    ImageView play, prev, next, imageView;
    TextView songTitle, songSinger;
    SeekBar mSeekBarTime, mSeekBarVol;
    List<Song> mSongs;
    static MediaPlayer mMediaPlayer;
    private Runnable runnable;
    private AudioManager mAudioManager;
    int currentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
// initializing views
        play = findViewById(R.id.play_btn);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        imageView = findViewById(R.id.imageView);
        songTitle = findViewById(R.id.songTitle);
        songSinger = findViewById(R.id.songSinger);
        mSeekBarTime = findViewById(R.id.seekBarTime);
        mSeekBarVol = findViewById(R.id.seekBarVol);

        mSongs = (List<Song>) getIntent().getSerializableExtra("listMusic");
        Song song = (Song) getIntent().getSerializableExtra("song");
        currentIndex =  getIntent().getIntExtra("index",0);

        if(song != null){
            imageView.setImageResource(song.getImage());
            songTitle.setText(song.getTitle());
            songSinger.setText(song.getSingle());
            mMediaPlayer=MediaPlayer.create(getApplicationContext(),song.getResourece());
        }
        //mSeekBar Volumn

        int maxV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curV = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSeekBarVol.setMax(maxV);
        mSeekBarVol.setProgress(curV);

        mSeekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                if(mMediaPlayer!=null && mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    play.setImageResource(R.drawable.ic_pause);
                }else{
                    mMediaPlayer.start();
                    play.setImageResource(R.drawable.ic_play);
                }
                SongNames();

                // COntext    Service muốn chạy
                Intent intent = new Intent(Item.this,MyService.class);
                intent.putExtra("object_song",mSongs.get(currentIndex));

                startService(intent);//chạy onCreate lần đầu rồi chạy onStartCommand

            }

        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer!=null){
                    play.setImageResource(R.drawable.ic_play);
                }
                if(currentIndex < mSongs.size() - 1){
                    currentIndex++;
                }else{
                    currentIndex = 0;
                }
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(),mSongs.get(currentIndex).getResourece());
                mMediaPlayer.start();
                SongNames();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer!=null){
                    play.setImageResource(R.drawable.ic_pause);
                }
                if(currentIndex > 0){
                    currentIndex--;
                }else{
                    currentIndex = mSongs.size()-1;
                }
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(),mSongs.get(currentIndex).getResourece());
                mMediaPlayer.start();
                SongNames();
            }
        });


    }


    private void SongNames(){
        Song song = mSongs.get(currentIndex);
        imageView.setImageResource(song.getImage());
        songTitle.setText(song.getTitle());
        songSinger.setText(song.getSingle());

        //seek bar duration
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                mMediaPlayer.start();
            }
        });

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mMediaPlayer.seekTo(progress);
                    mSeekBarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer!= null){
                    try {
                        if(mMediaPlayer.isPlaying()){
                            Message message = new Message();

                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @SuppressLint("Handle Leak")
    Handler handler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            mSeekBarTime.setProgress(msg.what);
        }
    };


}