package com.example.goal_tracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.work.*;

import static android.provider.Settings.Global.getString;
import static com.example.goal_tracker.LogIn.APP_TAG;

public class CheckWorker extends Worker {
    private final HealthDataStore mStore;

    public CheckWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        mStore = new HealthDataStore(getApplicationContext(), mConnectionListener);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Do the work here--in this case, compress the stored images.
        // In this example no parameters are passed; the task is
        // assumed to be "compress the whole library."
        readRate();

        // Indicate success or failure with your return value:
        return Result.success();

        // (Returning Result.retry() tells WorkManager to try this task again
        // later; Result.failure() says not to try again.)
    }



    // Read the today's step count on demand
    private void readRate() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long endTime = Calendar.getInstance().getTimeInMillis();
        long startTime =  endTime - TimeUnit.MINUTES.toMillis(5);


        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.HeartRate.HEALTH_DATA_TYPE)
                .setProperties(new String[]{HealthConstants.HeartRate.HEART_RATE})
                .setLocalTimeRange(HealthConstants.HeartRate.START_TIME, HealthConstants.HeartRate.TIME_OFFSET,
                        startTime, endTime)
                .build();

        try {
            resolver.read(request).setResultListener(mListener);
        } catch (Exception e) {
        }
    }

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult healthConnectionErrorResult) {

        }

        @Override
        public void onDisconnected() {

        }
    };
    private final HealthResultHolder.ResultListener<HealthDataResolver.ReadResult> mListener = result -> {
        int count=0;
        int total=0;
        try {
            for (HealthData data : result) {
                total+=data.getInt(HealthConstants.HeartRate.HEART_RATE);
                count++;
            }
        } finally {
            result.close();
        }


        int avg = total/count;
        if(avg>90||avg<50){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                    .setSmallIcon(R.drawable.common_google_signin_btn_text_light_normal)
                    .setContentTitle("Focus")
                    .setContentText("Seems like you aren't studying")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            PendingIntent pIntent= PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
            mBuilder.setContentIntent(pIntent);
            NotificationManager mNotificationManager = (NotificationManager)
                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notif = mBuilder.build();
            notif.flags |= Notification.FLAG_ONGOING_EVENT;
            mNotificationManager.notify(1,notif);
        }

    };


}
