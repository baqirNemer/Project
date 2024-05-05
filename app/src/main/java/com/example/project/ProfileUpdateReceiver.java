package com.example.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ProfileUpdateReceiver extends BroadcastReceiver {
    public static final String ACTION_PROFILE_UPDATED = "com.example.project.ACTION_PROFILE_UPDATED";
    private static final String TAG = "ProfileUpdateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(EditProfile.ACTION_PROFILE_UPDATED)) {
            Log.d(TAG, "Profile update broadcast received!");
            Intent userProfileIntent = new Intent(context, UserProfile.class);
            userProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(userProfileIntent);
        }
    }
}
