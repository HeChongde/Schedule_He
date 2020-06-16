package com.example.schedule_he.ui.richeng;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.schedule_he.R;

public class AlarmReceiver extends BroadcastReceiver {

    //private String channelId = "Schedule_He";
    //private String name = "ChannelName";
    @Override
    public void onReceive(Context context, Intent intent) {

/*
        String title = intent.getExtras().getString("title");
        String content = intent.getExtras().getString("content");
        int id = intent.getExtras().getInt("id");

        Intent intent1 = new Intent(context, DashboardFragment.class);
        intent1.putExtra("mode", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent1, 0);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.enableVibration(true);
            manager.createNotificationChannel(mChannel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title).setContentText(content).setSmallIcon(R.drawable.red_alarm_24dp)
                .setContentIntent(pendingIntent).setAutoCancel(true).setFullScreenIntent(pendingIntent, true);

        Notification notification = builder.build();

        manager.notify(1, notification);

*/
        String title = intent.getExtras().getString("title");
        String content = intent.getExtras().getString("content");
        int id = intent.getExtras().getInt("id");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //创建一个Notification对象并设置该通知的属性
        Notification notification=new Notification.Builder(context)
            //设置打开该通知,该通知自动消失
                .setAutoCancel(true)
            //设置显示在状态栏的提示信息
                .setTicker("您有新的日程")
            //设置通知的图标
                .setSmallIcon(R.drawable.ic_alarms_black_24dp)
            //设置通知内容的标题
                .setContentTitle(title)
            //设置通知内容的内容
                .setContentText(content)
            //设置通知震动时间,先静止0秒再震动一秒再停止一秒，再震动一秒
                .setVibrate(new long[]{0,1000,1000,1000})
            //设置使用系统默认的声音，默认的LED灯
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS)
            //设置通知的时间
                .setWhen(System.currentTimeMillis())
            //设置通知将要启动的程序的Intent
                //.setContentIntent(intent)
                .build();
            //使用NotificationManager的notify()方法显示通知
            //第一个参数为每个通知指定一个不同的ID,第二个参数传入Notification的对象
        manager.notify(id, notification);

    }
}