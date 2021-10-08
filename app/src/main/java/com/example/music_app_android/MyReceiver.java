package com.example.music_app_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //nhận dữ liệu, khai báo android Manifest
        //action nhận được khi we click
        int actionMusic = intent.getIntExtra("action_music",0);//giá trị default nếu khong nhận đc

        //truyền ngược lại MyService
        Intent intentService = new Intent(context,MyService.class);
        intentService.putExtra("action_music_service",actionMusic);

        //Khi chạy start Service nó không khởi tạo lại(OnCreate) mà chạy luôn vào hàm StartCommand
        context.startService(intentService);
    }
}
