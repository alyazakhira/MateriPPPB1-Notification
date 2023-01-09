package com.example.notifapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {
    private NotificationManager mNotificationManager;
    private final static String CHANNEL_ID = "primary-channel";
    private final static int NOTIFICATION_ID = 0;
    private final static String ACTION_UPDATE_NOTIF = "action-update-notif";
    private NotificationReceiver mReceiver = new NotificationReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIF));
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"app notif",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        findViewById(R.id.notify_btn).setOnClickListener(view -> sendNotification());

        findViewById(R.id.update_btn).setOnClickListener(view -> updateNotification());

        findViewById(R.id.cancel_btn).setOnClickListener(view -> mNotificationManager.cancel(NOTIFICATION_ID));

    }

    private void updateNotification(){
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(),R.drawable.mascot_1);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!"));
        mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity2.class);
        PendingIntent notificationPendingIntent = PendingIntent
                .getActivity(this, NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("New Notification")
                .setContentText("This is content text")
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentIntent(notificationPendingIntent);
        return notifyBuilder;
    }

    private void sendNotification(){
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIF);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this
                ,NOTIFICATION_ID,updateIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.addAction(R.drawable.ic_android_black_24dp,"Update Notification",updatePendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private class NotificationReceiver extends BroadcastReceiver{
        public NotificationReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_UPDATE_NOTIF)){
                updateNotification();
            }
        }

    }

}