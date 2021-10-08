package com.example.music_app_android;

import static com.example.music_app_android.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.music_app_android.model.Song;

public class MyService extends Service {

    private static final int ACTION_PAUSE = 1;
    private static final int ACTION_RESUME = 2;
    private static final int ACTION_CLEAR = 3;


    private MediaPlayer mMediaPlayer;
    private boolean isPlaying;
    private Song mSong;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Tincoder", "My Service onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;//nếu không sử dụng BoundService
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getSerializableExtra("object_song") != null){
            Song song = (Song) intent.getSerializableExtra("object_song");

            if(song != null){
                mSong = song;
                startMusic(song);
                Log.e("TAG", "onStartCommand: " );
                sendNotification(song);
            }
        }

        int actionMusic = intent.getIntExtra("action_music_service",0);
        Log.e("Hoang","abbbbb" +actionMusic);
         handleActionMusic(actionMusic);

        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if(mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(getApplicationContext(),song.getResourece());
        }
        mMediaPlayer.start();
        isPlaying = true;
    }

    private void handleActionMusic(int action){
        switch (action){
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();// => stop service=> onDestroy
                break;
        }
    }

    private void pauseMusic(){
        if(mMediaPlayer != null && isPlaying==true){
            mMediaPlayer.pause();
            isPlaying=false;
            sendNotification(mSong);//updateView thì phải send lại Notifycation
        }

    }
    private void resumMusic(){
        if(mMediaPlayer != null && isPlaying==false){
            mMediaPlayer.start(); //tiếp tục chạy
            isPlaying = true;
            sendNotification(mSong);
        }
    }


    private void sendNotification(Song song) {
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),song.getImage());

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);
        remoteViews.setTextViewText(R.id.tv_title_song, song.getTitle());
        remoteViews.setTextViewText(R.id.tv_single_song, song.getSingle());
        remoteViews.setImageViewBitmap(R.id.img_song,bitmap);

        //icon nhỏ
        remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.ic_play);
        remoteViews.setImageViewResource(R.id.img_clear,R.drawable.ic_clear);

        //Bắt sự kiện cho component trong custom notification
        if(isPlaying){
            //Nếu như đang chạy mà click chọn sư kiện nút img_play thì nó sẽ pause lại
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause,getPendingIntent(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.ic_pause);
        }else{
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause,getPendingIntent(this, ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.ic_play);
        }

        remoteViews.setOnClickPendingIntent(R.id.img_clear,getPendingIntent(this, ACTION_CLEAR));

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)//sau khi đã có peding thì mới st, không thì th
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();
        //Tham số đầu tiên là id của noti
        //tham số hai là đối tượng notification
        startForeground(1,notification);

    }

    private PendingIntent getPendingIntent( Context context, int action){
        Intent intent = new Intent(this, MyReceiver.class);//truỳen action sang my Rêciver
        intent.putExtra("action_music",action);
        //Log.e("Tincoder",action+"" );
        return PendingIntent.getBroadcast(context.getApplicationContext(),action, intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Tincoder","MyService onDestroy");
        if (mMediaPlayer != null){
            //Khi Service bị kill thì đối tượng cũng phải close
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
