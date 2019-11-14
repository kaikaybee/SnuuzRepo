package com.example.snuuz;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

// Because NotificationChannel class requires API 26 or higher


// NotificationHelper class
public class NotificationHelper extends ContextWrapper {
    public static final String channel1ID = "channel1ID";
    public static final String channel1Name = "Channel 1"; // In reality should describe the use of this channel
    public static final String channel2ID = "channel2ID";
    public static final String channel2Name = "Channel 2";

    private NotificationManager mManager;

    // Constructor - Note: no default constructor
    public NotificationHelper(Context base) {
        super(base);
        // NotificationChannel class requires API 26 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannels();
    }

    // Helper for constructor
    // Create a NotificationChannel object, set a bunch of settings, then set the channel in the NotificationManager
    @RequiresApi(api = Build.VERSION_CODES.O) // See annotation up top
    public void createChannels() {
        // IMPORTANCE_DEFAULT means icon will show, but no interrupting dropdown text
        NotificationChannel channel1 = new NotificationChannel(channel1ID, channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1); //

        // Channel 2
        NotificationChannel channel2 = new NotificationChannel(channel2ID, channel2Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(R.color.colorPrimary);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel2);
    }

    // Helper for constructor
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    // This is where the actual notification is
    public NotificationCompat.Builder getChannel1Notification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_one);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), channel2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_two);
    }

    // Default channel
    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), channel2ID)
                .setSmallIcon(R.drawable.ic_one);
    }
}
