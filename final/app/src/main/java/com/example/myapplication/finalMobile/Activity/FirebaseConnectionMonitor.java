package com.example.myapplication.finalMobile.Activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.finalMobile.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseConnectionMonitor {
    private static final String CHANNEL_ID = "FirebaseConnectionChannel";
    private static final int NOTIFICATION_ID = 1;

    private static Context context;

    private static Handler handler;
    private static Runnable debounceRunnable;
    private static boolean connected;


    public static void startMonitoring(Context appContext) {
        context = appContext;
        handler = new Handler();
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                connected = Boolean.TRUE.equals(snapshot.getValue(Boolean.class));
                Log.d("FirebaseConnection", "Connection status changed: " + connected);

                if (connected) {
                    dismissNotification();
                } else {
                    debounceFalseStatus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private static void debounceFalseStatus() {
        if (debounceRunnable != null) {
            handler.removeCallbacks(debounceRunnable);
        }
        debounceRunnable = () -> {
            if (!connected) {
                showConnectionLostNotification();
            }
        };
        handler.postDelayed(debounceRunnable, 3000);
    }


    private static void dismissNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(NOTIFICATION_ID);

    }

    @SuppressLint("MissingPermission")
    private static void showConnectionLostNotification() {
        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Connection Lost")
                .setContentText("Firebase connection lost.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Firebase Connection";
            String description = "Firebase connection notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
