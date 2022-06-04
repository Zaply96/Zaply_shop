package com.crzaor.zaply_shop.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.crzaor.zaply_shop.R;

public class Notificator{

    private static NotificationManager notificationManager;
    private static final String channelId = "ZaplyShop";
    private static int notificacionId = 1;
    public Notificator(){
    }


    protected void createNotificationChannel(String channelId, String name) {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300});
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);

        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotification(View v, String title, String message){
        Notification.Builder notificacion = new Notification.Builder(v.getContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setNumber(3)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setChannelId(channelId);

        NotificationManager notificationManager = (NotificationManager)
                v.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificacionId, notificacion.build());
        notificacionId++;
    }
}
