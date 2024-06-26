package com.example.project;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class SoundService extends Service {
    private Ringtone ringtone;

    @Override
    public void onCreate() {
        super.onCreate();
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notificationSoundUri);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ringtone != null) {
            ringtone.play();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}