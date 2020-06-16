package com.example.schedule_he;

import android.app.Service;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONException;
import static android.app.Notification.PRIORITY_MAX;

/**
 * Created by Administrator on 2018/9/13 0013.
 */

public class BackGroundService extends Service {
    Notification notification;
    private Context mContext;
    private MediaPlayer bgmediaPlayer;
    private boolean isrun = true;

    public BackGroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //1.通知栏占用，不清楚的看官网或者音乐类APP的效果
        notification = new Notification.Builder(mContext)
                .setSmallIcon(R.drawable.ic_alarms_black_24dp)
                .setWhen(System.currentTimeMillis())
                .setTicker("日程表")
                .setContentTitle("日程表")
                .setContentText("日常镖正在运行")
                .setOngoing(true)
                .setPriority(PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .build();
        /*使用startForeground,如果id为0，那么notification将不会显示*/
        startForeground(100, notification);
        //2.开启线程（或者需要定时操作的事情）
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (isrun) {

                    //你需要执行的任务
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException es) {
                        es.printStackTrace();
                    }
                }
                //进行自己的操作
            }
        }.start();
        //3.最关键的神来之笔，也是最投机的动作，没办法要骗过CPU
        //这就是播放音乐类APP不被杀的做法，自己找个无声MP3放进来循环播放
        if (bgmediaPlayer == null) {
            bgmediaPlayer = MediaPlayer.create(this,R.raw.silent);
            bgmediaPlayer.setLooping(true);
            bgmediaPlayer.start();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        isrun = false;
        stopForeground(true);
        bgmediaPlayer.release();
        stopSelf();
        super.onDestroy();
    }

}
