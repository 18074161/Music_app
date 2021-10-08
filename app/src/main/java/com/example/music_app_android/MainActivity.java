package com.example.music_app_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music_app_android.model.Song;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    SeekBar mSeekBarTime, mSeekBarVol;
    static MediaPlayer sMediaPlayer;
    private Runnable mRunnable;
    int currentIndex = 0;

    RecyclerView rcv;
    CustomRecyclerView adt;
    List<Song> mSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcv = findViewById(R.id.rcv);
        mSongs = new ArrayList<Song>();

        mSongs.add(new Song("Nước mắt tuôn rơi trò chơi kết thúc","Issac",R.drawable.headphone,R.raw.gia_nhu_ngay_dau));
        mSongs.add(new Song("Nước mắt tuôn rơi trò chơi kết thúc1","Issac1",R.drawable.hinh_anha,R.raw.gia_nhu_ngay_dau));
        mSongs.add(new Song("Nước mắt tuôn rơi trò chơi kết thúc2","Issac2",R.drawable.hinhanhb,R.raw.gia_nhu_ngay_dau));
        mSongs.add(new Song("Nước mắt tuôn rơi trò chơi kết thúc3","Issac3",R.drawable.hinhanhc,R.raw.gia_nhu_ngay_dau));
        mSongs.add(new Song("Nước mắt tuôn rơi trò chơi kết thúc4","Issac4",R.drawable.headphone,R.raw.gia_nhu_ngay_dau));
        mSongs.add(new Song("Nước mắt tuôn rơi trò chơi kết thúc5","Issac5",R.drawable.hinhanhc,R.raw.gia_nhu_ngay_dau));


        adt = new CustomRecyclerView(mSongs,this);
        rcv.setHasFixedSize(true);
        rcv.setAdapter(adt);
        rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void clickItem(Song song) {
        Intent intent  = new Intent(MainActivity.this,Item.class);
        intent.putExtra("song",song);
        intent.putExtra("listMusic", (Serializable) mSongs);
        intent.putExtra("index",mSongs.indexOf(song));
        startActivity(intent);
    }
}